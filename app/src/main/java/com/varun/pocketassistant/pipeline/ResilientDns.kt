package com.varun.pocketassistant.pipeline

import android.util.Log
import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * DNS that survives flaky Android/Wi‑Fi resolvers:
 * 1) system DNS
 * 2) Cloudflare DNS-over-HTTPS via https://1.1.1.1 (no DNS needed to reach it)
 * 3) last known-good resolution cache (no hardcoded edge IPs)
 */
object ResilientDns : Dns {
    private const val TAG = "ResilientDns"
    private const val CACHE_TTL_MS = 30 * 60 * 1000L

    private data class Cached(val addresses: List<InetAddress>, val expiresAtMs: Long)

    private val resolutionCache = ConcurrentHashMap<String, Cached>()

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
            if (system.isNotEmpty()) {
                cache(host, system)
                return system
            }
        } catch (e: UnknownHostException) {
            Log.w(TAG, "system DNS failed for $host: ${e.message}")
        }

        try {
            val doh = lookupDoh(host)
            if (doh.isNotEmpty()) {
                Log.i(TAG, "DoH resolved $host -> ${doh.joinToString { it.hostAddress ?: "?" }}")
                cache(host, doh)
                return doh
            }
        } catch (t: Throwable) {
            Log.w(TAG, "DoH failed for $host: ${t.message}")
        }

        val cached = resolutionCache[host]
        if (cached != null && cached.expiresAtMs >= System.currentTimeMillis() && cached.addresses.isNotEmpty()) {
            Log.w(TAG, "using cached resolution for $host")
            return cached.addresses
        }

        throw UnknownHostException("Unable to resolve host \"$hostname\": no address associated with hostname")
    }

    private fun cache(host: String, addresses: List<InetAddress>) {
        resolutionCache[host] = Cached(addresses, System.currentTimeMillis() + CACHE_TTL_MS)
    }

    private fun lookupDoh(hostname: String): List<InetAddress> {
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
