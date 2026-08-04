package com.varun.pocketassistant.pipeline

import android.util.Log
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * DNS that survives flaky Android/Wi‑Fi resolvers:
 * 1) system DNS
 * 2) Cloudflare DNS-over-HTTPS via https://1.1.1.1 (no DNS needed to reach it)
 * 3) static fallbacks for openrouter.ai
 */
object ResilientDns : Dns {
    private const val TAG = "ResilientDns"

    private val staticFallback = mapOf(
        "openrouter.ai" to listOf("104.18.2.115", "104.18.3.115"),
    )

    /** Bootstrap client: resolve nothing via system DNS — talk to 1.1.1.1 by IP. */
    private val dohClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(8, TimeUnit.SECONDS)
            .dns(object : Dns {
                override fun lookup(hostname: String): List<InetAddress> =
                    listOf(InetAddress.getByName("1.1.1.1"))
            })
            .build()
    }

    override fun lookup(hostname: String): List<InetAddress> {
        val host = hostname.trim().lowercase()
        try {
            val system = Dns.SYSTEM.lookup(host)
            if (system.isNotEmpty()) return system
        } catch (e: UnknownHostException) {
            Log.w(TAG, "system DNS failed for $host: ${e.message}")
        }

        try {
            val doh = lookupDoh(host)
            if (doh.isNotEmpty()) {
                Log.i(TAG, "DoH resolved $host -> ${doh.joinToString { it.hostAddress ?: "?" }}")
                return doh
            }
        } catch (t: Throwable) {
            Log.w(TAG, "DoH failed for $host: ${t.message}")
        }

        val fallback = staticFallback[host]
        if (!fallback.isNullOrEmpty()) {
            Log.w(TAG, "using static fallback for $host")
            return fallback.map { InetAddress.getByName(it) }
        }

        throw UnknownHostException("Unable to resolve host \"$hostname\": no address associated with hostname")
    }

    private fun lookupDoh(hostname: String): List<InetAddress> {
        // JSON DoH against Cloudflare by IP — works even when device DNS is broken.
        val url = "https://1.1.1.1/dns-query?name=$hostname&type=A".toHttpUrl()
        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/dns-json")
            .get()
            .build()
        dohClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return emptyList()
            val body = response.body?.string().orEmpty()
            val answer = JSONObject(body).optJSONArray("Answer") ?: return emptyList()
            val out = ArrayList<InetAddress>()
            for (i in 0 until answer.length()) {
                val row = answer.optJSONObject(i) ?: continue
                if (row.optInt("type") != 1) continue // A
                val ip = row.optString("data").trim()
                if (ip.isNotEmpty()) {
                    out += InetAddress.getByName(ip)
                }
            }
            return out
        }
    }
}
