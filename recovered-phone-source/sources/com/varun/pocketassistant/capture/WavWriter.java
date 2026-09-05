package com.varun.pocketassistant.capture;

import com.google.ai.edge.examples.asr.FileAudioSource;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

/* JADX INFO: compiled from: WavWriter.kt */
/* JADX INFO: loaded from: classes5.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0017\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B!\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005¢\u0006\u0004\b\u0007\u0010\bJ\u001e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u0005J\u0006\u0010\u0015\u001a\u00020\u0010J\b\u0010\u0016\u001a\u00020\u0010H\u0016J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0018\u001a\u00020\u0005H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lcom/varun/pocketassistant/capture/WavWriter;", "Ljava/io/Closeable;", "file", "Ljava/io/File;", "sampleRate", "", "channels", "<init>", "(Ljava/io/File;II)V", "raf", "Ljava/io/RandomAccessFile;", "dataBytes", "", "closed", "", "writePcm", "", "buffer", "", "offset", "length", "flush", "close", "writeHeader", "dataSize", "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class WavWriter implements Closeable {
    public static final int $stable = 8;
    private final int channels;
    private boolean closed;
    private long dataBytes;
    private final File file;
    private final RandomAccessFile raf;
    private final int sampleRate;

    public WavWriter(File file, int sampleRate, int channels) throws IOException {
        Intrinsics.checkNotNullParameter(file, "file");
        this.file = file;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.raf = new RandomAccessFile(this.file, "rw");
        this.raf.setLength(0L);
        writeHeader(0);
    }

    public /* synthetic */ WavWriter(File file, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(file, i, (i3 & 4) != 0 ? 1 : i2);
    }

    public final synchronized void writePcm(short[] buffer, int offset, int length) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        if (this.closed) {
            throw new IllegalStateException("WavWriter already closed".toString());
        }
        ByteBuffer bytes = ByteBuffer.allocate(length * 2).order(ByteOrder.LITTLE_ENDIAN);
        int i = offset + length;
        for (int i2 = offset; i2 < i; i2++) {
            bytes.putShort(buffer[i2]);
        }
        byte[] array = bytes.array();
        this.raf.write(array);
        this.dataBytes += (long) array.length;
    }

    public final synchronized void flush() {
        if (this.closed) {
            return;
        }
        this.raf.getFD().sync();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            if (this.closed) {
                return;
            }
            writeHeader((int) this.dataBytes);
            this.raf.getFD().sync();
            this.raf.close();
            this.closed = true;
            Unit unit = Unit.INSTANCE;
        }
    }

    private final void writeHeader(int dataSize) throws IOException {
        int byteRate = this.sampleRate * this.channels * 2;
        int blockAlign = this.channels * 2;
        ByteBuffer buffer = ByteBuffer.allocate(44).order(ByteOrder.LITTLE_ENDIAN);
        byte[] bytes = "RIFF".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
        buffer.put(bytes);
        buffer.putInt(dataSize + 36);
        byte[] bytes2 = "WAVE".getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes2, "getBytes(...)");
        buffer.put(bytes2);
        byte[] bytes3 = FileAudioSource.FMT_CHUNK_ID.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes3, "getBytes(...)");
        buffer.put(bytes3);
        buffer.putInt(16);
        buffer.putShort((short) 1);
        buffer.putShort((short) this.channels);
        buffer.putInt(this.sampleRate);
        buffer.putInt(byteRate);
        buffer.putShort((short) blockAlign);
        buffer.putShort((short) 16);
        byte[] bytes4 = FileAudioSource.DATA_CHUNK_ID.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes4, "getBytes(...)");
        buffer.put(bytes4);
        buffer.putInt(dataSize);
        this.raf.seek(0L);
        this.raf.write(buffer.array());
        this.raf.seek(this.dataBytes + 44);
    }
}
