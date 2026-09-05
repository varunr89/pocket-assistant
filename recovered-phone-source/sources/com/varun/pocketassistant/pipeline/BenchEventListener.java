package com.varun.pocketassistant.pipeline;

import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.DurationKt;
import okhttp3.Call;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Response;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u0000 %2\u00020\u0001:\u0001%B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0005H\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J&\u0010\u000b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fH\u0016J*\u0010\u0011\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017H\u0016J\u001a\u0010\u0018\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0018\u0010\u001b\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u0005H\u0016J\u0018\u0010\u001d\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0018\u0010 \u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u0005H\u0016J\u0010\u0010!\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J\u0018\u0010\"\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010#\u001a\u00020$H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006&"}, d2 = {"Lcom/varun/pocketassistant/pipeline/BenchEventListener;", "Lokhttp3/EventListener;", "<init>", "()V", "t0", "", "ms", "callStart", "", NotificationCompat.CATEGORY_CALL, "Lokhttp3/Call;", "dnsEnd", "domainName", "", "inetAddressList", "", "Ljava/net/InetAddress;", "connectEnd", "inetSocketAddress", "Ljava/net/InetSocketAddress;", "proxy", "Ljava/net/Proxy;", "protocol", "Lokhttp3/Protocol;", "secureConnectEnd", "handshake", "Lokhttp3/Handshake;", "requestBodyEnd", "byteCount", "responseHeadersEnd", "response", "Lokhttp3/Response;", "responseBodyEnd", "callEnd", "callFailed", "ioe", "Ljava/io/IOException;", "Companion", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
final class BenchEventListener extends EventListener {
    private static final String TAG = "OpenAiClient";
    private long t0 = System.nanoTime();

    private final long ms() {
        return (System.nanoTime() - this.t0) / ((long) DurationKt.NANOS_IN_MILLIS);
    }

    @Override // okhttp3.EventListener
    public void callStart(Call call) {
        Intrinsics.checkNotNullParameter(call, "call");
        this.t0 = System.nanoTime();
        Log.i(TAG, "BENCH ev callStart");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence dnsEnd$lambda$0(InetAddress it) {
        Intrinsics.checkNotNullParameter(it, "it");
        String hostAddress = it.getHostAddress();
        if (hostAddress == null) {
            hostAddress = "?";
        }
        return hostAddress;
    }

    @Override // okhttp3.EventListener
    public void dnsEnd(Call call, String domainName, List<? extends InetAddress> inetAddressList) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(domainName, "domainName");
        Intrinsics.checkNotNullParameter(inetAddressList, "inetAddressList");
        Log.i(TAG, "BENCH ev dnsEnd +" + ms() + "ms addrs=" + CollectionsKt.joinToString$default(inetAddressList, null, null, null, 0, null, new Function1() { // from class: com.varun.pocketassistant.pipeline.BenchEventListener$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return BenchEventListener.dnsEnd$lambda$0((InetAddress) obj);
            }
        }, 31, null));
    }

    @Override // okhttp3.EventListener
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(inetSocketAddress, "inetSocketAddress");
        Intrinsics.checkNotNullParameter(proxy, "proxy");
        Log.i(TAG, "BENCH ev connectEnd +" + ms() + "ms peer=" + inetSocketAddress + " proto=" + protocol);
    }

    @Override // okhttp3.EventListener
    public void secureConnectEnd(Call call, Handshake handshake) {
        Intrinsics.checkNotNullParameter(call, "call");
        Log.i(TAG, "BENCH ev tlsEnd +" + ms() + "ms");
    }

    @Override // okhttp3.EventListener
    public void requestBodyEnd(Call call, long byteCount) {
        Intrinsics.checkNotNullParameter(call, "call");
        Log.i(TAG, "BENCH ev requestBodyEnd +" + ms() + "ms bytes=" + byteCount);
    }

    @Override // okhttp3.EventListener
    public void responseHeadersEnd(Call call, Response response) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(response, "response");
        Log.i(TAG, "BENCH ev responseHeadersEnd(TTFB) +" + ms() + "ms code=" + response.code());
    }

    @Override // okhttp3.EventListener
    public void responseBodyEnd(Call call, long byteCount) {
        Intrinsics.checkNotNullParameter(call, "call");
        Log.i(TAG, "BENCH ev responseBodyEnd +" + ms() + "ms bytes=" + byteCount);
    }

    @Override // okhttp3.EventListener
    public void callEnd(Call call) {
        Intrinsics.checkNotNullParameter(call, "call");
        Log.i(TAG, "BENCH ev callEnd +" + ms() + "ms");
    }

    @Override // okhttp3.EventListener
    public void callFailed(Call call, IOException ioe) {
        Intrinsics.checkNotNullParameter(call, "call");
        Intrinsics.checkNotNullParameter(ioe, "ioe");
        Log.w(TAG, "BENCH ev callFailed +" + ms() + "ms " + ioe.getClass().getSimpleName() + ": " + ioe.getMessage());
    }
}
