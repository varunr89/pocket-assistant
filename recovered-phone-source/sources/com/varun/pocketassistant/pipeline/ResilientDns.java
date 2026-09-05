package com.varun.pocketassistant.pipeline;

import android.util.Log;
import androidx.savedstate.serialization.ClassDiscriminatorModeKt;
import com.google.ai.edge.examples.asr.FileAudioSource;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: ResilientDns.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\bÇ\u0002\u0018\u00002\u00020\u0001:\u0001\u001aB\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u0006\u0010\u0014\u001a\u00020\u0005H\u0016J\u001e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00052\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0002J\u0016\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00130\u00122\u0006\u0010\u0014\u001a\u00020\u0005H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u000b\u001a\u00020\f8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000e¨\u0006\u001b"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ResilientDns;", "Lokhttp3/Dns;", "<init>", "()V", "TAG", "", "CACHE_TTL_MS", "", "resolutionCache", "Ljava/util/concurrent/ConcurrentHashMap;", "Lcom/varun/pocketassistant/pipeline/ResilientDns$Cached;", "dohClient", "Lokhttp3/OkHttpClient;", "getDohClient", "()Lokhttp3/OkHttpClient;", "dohClient$delegate", "Lkotlin/Lazy;", "lookup", "", "Ljava/net/InetAddress;", "hostname", "cache", "", "host", "addresses", "lookupDoh", "Cached", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class ResilientDns implements Dns {
    private static final long CACHE_TTL_MS = 1800000;
    private static final String TAG = "ResilientDns";
    public static final ResilientDns INSTANCE = new ResilientDns();
    private static final ConcurrentHashMap<String, Cached> resolutionCache = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: dohClient$delegate, reason: from kotlin metadata */
    private static final Lazy dohClient = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.pipeline.ResilientDns$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function0
        public final Object invoke() {
            return ResilientDns.dohClient_delegate$lambda$0();
        }
    });
    public static final int $stable = 8;

    private ResilientDns() {
    }

    /* JADX INFO: compiled from: ResilientDns.kt */
    @Metadata(d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0082\b\u0018\u00002\u00020\u0001B\u001d\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0006HÆ\u0003J#\u0010\u000f\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0013\u001a\u00020\u0014HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0016HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0017"}, d2 = {"Lcom/varun/pocketassistant/pipeline/ResilientDns$Cached;", "", "addresses", "", "Ljava/net/InetAddress;", "expiresAtMs", "", "<init>", "(Ljava/util/List;J)V", "getAddresses", "()Ljava/util/List;", "getExpiresAtMs", "()J", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
    private static final /* data */ class Cached {
        private final List<InetAddress> addresses;
        private final long expiresAtMs;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ Cached copy$default(Cached cached, List list, long j, int i, Object obj) {
            if ((i & 1) != 0) {
                list = cached.addresses;
            }
            if ((i & 2) != 0) {
                j = cached.expiresAtMs;
            }
            return cached.copy(list, j);
        }

        public final List<InetAddress> component1() {
            return this.addresses;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final long getExpiresAtMs() {
            return this.expiresAtMs;
        }

        public final Cached copy(List<? extends InetAddress> addresses, long expiresAtMs) {
            Intrinsics.checkNotNullParameter(addresses, "addresses");
            return new Cached(addresses, expiresAtMs);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Cached)) {
                return false;
            }
            Cached cached = (Cached) other;
            return Intrinsics.areEqual(this.addresses, cached.addresses) && this.expiresAtMs == cached.expiresAtMs;
        }

        public int hashCode() {
            return (this.addresses.hashCode() * 31) + Long.hashCode(this.expiresAtMs);
        }

        public String toString() {
            return "Cached(addresses=" + this.addresses + ", expiresAtMs=" + this.expiresAtMs + ")";
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Cached(List<? extends InetAddress> addresses, long expiresAtMs) {
            Intrinsics.checkNotNullParameter(addresses, "addresses");
            this.addresses = addresses;
            this.expiresAtMs = expiresAtMs;
        }

        public final List<InetAddress> getAddresses() {
            return this.addresses;
        }

        public final long getExpiresAtMs() {
            return this.expiresAtMs;
        }
    }

    private final OkHttpClient getDohClient() {
        return (OkHttpClient) dohClient.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final OkHttpClient dohClient_delegate$lambda$0() {
        return new OkHttpClient.Builder().connectTimeout(5L, TimeUnit.SECONDS).readTimeout(5L, TimeUnit.SECONDS).callTimeout(8L, TimeUnit.SECONDS).dns(new Dns() { // from class: com.varun.pocketassistant.pipeline.ResilientDns$dohClient$2$1
            @Override // okhttp3.Dns
            public List<InetAddress> lookup(String hostname) {
                Intrinsics.checkNotNullParameter(hostname, "hostname");
                return CollectionsKt.listOf(InetAddress.getByName("1.1.1.1"));
            }
        }).build();
    }

    @Override // okhttp3.Dns
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        Intrinsics.checkNotNullParameter(hostname, "hostname");
        String host = StringsKt.trim((CharSequence) hostname).toString().toLowerCase(Locale.ROOT);
        Intrinsics.checkNotNullExpressionValue(host, "toLowerCase(...)");
        try {
            List<InetAddress> listLookup = Dns.SYSTEM.lookup(host);
            if (!listLookup.isEmpty()) {
                cache(host, listLookup);
                return listLookup;
            }
        } catch (UnknownHostException e) {
            Log.w(TAG, "system DNS failed for " + host + ": " + e.getMessage());
        }
        try {
            List<InetAddress> listLookupDoh = lookupDoh(host);
            if (!listLookupDoh.isEmpty()) {
                Log.i(TAG, "DoH resolved " + host + " -> " + CollectionsKt.joinToString$default(listLookupDoh, null, null, null, 0, null, new Function1() { // from class: com.varun.pocketassistant.pipeline.ResilientDns$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return ResilientDns.lookup$lambda$1((InetAddress) obj);
                    }
                }, 31, null));
                cache(host, listLookupDoh);
                return listLookupDoh;
            }
        } catch (Throwable t) {
            Log.w(TAG, "DoH failed for " + host + ": " + t.getMessage());
        }
        Cached cached = resolutionCache.get(host);
        if (cached != null && cached.getExpiresAtMs() >= System.currentTimeMillis() && !cached.getAddresses().isEmpty()) {
            Log.w(TAG, "using cached resolution for " + host);
            return cached.getAddresses();
        }
        throw new UnknownHostException("Unable to resolve host \"" + hostname + "\": no address associated with hostname");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence lookup$lambda$1(InetAddress it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String hostAddress = it.getHostAddress();
        if (hostAddress == null) {
            hostAddress = "?";
        }
        return hostAddress;
    }

    private final void cache(String host, List<? extends InetAddress> addresses) {
        resolutionCache.put(host, new Cached(addresses, System.currentTimeMillis() + CACHE_TTL_MS));
    }

    private final List<InetAddress> lookupDoh(String hostname) throws IOException {
        HttpUrl url = HttpUrl.INSTANCE.get("https://1.1.1.1/dns-query?name=" + hostname + "&type=A");
        Request request = new Request.Builder().url(url).header(HttpHeaders.ACCEPT, "application/dns-json").get().build();
        Response responseExecute = getDohClient().newCall(request).execute();
        try {
            Response response = responseExecute;
            if (!response.isSuccessful()) {
                List<InetAddress> listEmptyList = CollectionsKt.emptyList();
                CloseableKt.closeFinally(responseExecute, null);
                return listEmptyList;
            }
            ResponseBody responseBodyBody = response.body();
            String strString = responseBodyBody != null ? responseBodyBody.string() : null;
            if (strString == null) {
                strString = "";
            }
            JSONArray jSONArrayOptJSONArray = new JSONObject(strString).optJSONArray("Answer");
            if (jSONArrayOptJSONArray == null) {
                List<InetAddress> listEmptyList2 = CollectionsKt.emptyList();
                CloseableKt.closeFinally(responseExecute, null);
                return listEmptyList2;
            }
            ArrayList arrayList = new ArrayList();
            int length = jSONArrayOptJSONArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null) {
                    boolean z = true;
                    if (jSONObjectOptJSONObject.optInt(ClassDiscriminatorModeKt.CLASS_DISCRIMINATOR_KEY) == 1) {
                        String strOptString = jSONObjectOptJSONObject.optString(FileAudioSource.DATA_CHUNK_ID);
                        Intrinsics.checkNotNullExpressionValue(strOptString, "optString(...)");
                        String string = StringsKt.trim((CharSequence) strOptString).toString();
                        if (string.length() <= 0) {
                            z = false;
                        }
                        if (z) {
                            arrayList.add(InetAddress.getByName(string));
                        }
                    }
                }
            }
            ArrayList arrayList2 = arrayList;
            CloseableKt.closeFinally(responseExecute, null);
            return arrayList2;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(responseExecute, th);
                throw th2;
            }
        }
    }
}
