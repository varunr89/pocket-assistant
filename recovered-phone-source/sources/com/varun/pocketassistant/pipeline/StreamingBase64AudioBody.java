package com.varun.pocketassistant.pipeline;

import android.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import org.apache.commons.math3.geometry.VectorFormat;

/* JADX INFO: compiled from: CloudAndLocalProviders.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B3\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0004\b\n\u0010\u000bJ\b\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"Lcom/varun/pocketassistant/pipeline/StreamingBase64AudioBody;", "Lokhttp3/RequestBody;", "wav", "Ljava/io/File;", "model", "", "format", "language", "diarize", "", "<init>", "(Ljava/io/File;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "contentType", "Lokhttp3/MediaType;", "writeTo", "", "sink", "Lokio/BufferedSink;", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
final class StreamingBase64AudioBody extends RequestBody {
    private final boolean diarize;
    private final String format;
    private final String language;
    private final String model;
    private final File wav;

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ StreamingBase64AudioBody(File file, String str, String str2, String str3, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        boolean z2;
        if ((i & 16) == 0) {
            z2 = z;
        } else {
            z2 = false;
        }
        this(file, str, str2, str3, z2);
    }

    public StreamingBase64AudioBody(File wav, String model, String format, String language, boolean diarize) {
        Intrinsics.checkNotNullParameter(wav, "wav");
        Intrinsics.checkNotNullParameter(model, "model");
        Intrinsics.checkNotNullParameter(format, "format");
        this.wav = wav;
        this.model = model;
        this.format = format;
        this.language = language;
        this.diarize = diarize;
    }

    @Override // okhttp3.RequestBody
    /* JADX INFO: renamed from: contentType */
    public MediaType get$contentType() {
        return MediaType.INSTANCE.get("application/json; charset=utf-8");
    }

    @Override // okhttp3.RequestBody
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] bArrCopyOf;
        int i;
        Intrinsics.checkNotNullParameter(sink, "sink");
        sink.writeUtf8("{\"model\":\"");
        sink.writeUtf8(this.model);
        sink.writeUtf8("\",\"input_audio\":{\"data\":\"");
        FileInputStream fileInputStream = new FileInputStream(this.wav);
        try {
            FileInputStream fileInputStream2 = fileInputStream;
            byte[] bArr = new byte[24576];
            while (true) {
                int i2 = 0;
                while (i2 < bArr.length && (i = fileInputStream2.read(bArr, i2, bArr.length - i2)) >= 0) {
                    i2 += i;
                }
                if (i2 <= 0) {
                    break;
                }
                if (i2 == bArr.length) {
                    bArrCopyOf = bArr;
                } else {
                    bArrCopyOf = Arrays.copyOf(bArr, i2);
                    Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
                }
                String strEncodeToString = Base64.encodeToString(bArrCopyOf, 2);
                Intrinsics.checkNotNullExpressionValue(strEncodeToString, "encodeToString(...)");
                sink.writeUtf8(strEncodeToString);
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(fileInputStream, null);
            sink.writeUtf8("\",\"format\":\"");
            sink.writeUtf8(this.format);
            sink.writeUtf8("\"}");
            String str = this.language;
            if (!(str == null || StringsKt.isBlank(str))) {
                sink.writeUtf8(",\"language\":\"");
                sink.writeUtf8(this.language);
                sink.writeUtf8("\"");
            }
            if (this.diarize) {
                sink.writeUtf8(",\"response_format\":\"verbose_json\"");
                sink.writeUtf8(",\"timestamp_granularities\":[\"word\"]");
                sink.writeUtf8(",\"provider\":{\"options\":{\"xai\":{\"diarize\":true}}}");
            }
            sink.writeUtf8(VectorFormat.DEFAULT_SUFFIX);
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(fileInputStream, th);
                throw th2;
            }
        }
    }
}
