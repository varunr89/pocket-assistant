package com.varun.pocketassistant.ui;

import androidx.compose.foundation.CanvasKt;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.geometry.Offset;
import androidx.compose.ui.geometry.Size;
import androidx.compose.ui.graphics.AndroidPath_androidKt;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.ColorKt;
import androidx.compose.ui.graphics.Path;
import androidx.compose.ui.graphics.StrokeCap;
import androidx.compose.ui.graphics.drawscope.DrawScope;
import androidx.compose.ui.graphics.drawscope.Stroke;
import androidx.compose.ui.unit.Dp;
import com.varun.pocketassistant.capture.CapturePhase;
import com.varun.pocketassistant.capture.RmsSample;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

/* JADX INFO: compiled from: RmsHistoryGraph.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a-\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u0007¢\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, d2 = {"RmsHistoryGraph", "", "samples", "", "Lcom/varun/pocketassistant/capture/RmsSample;", "threshold", "", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/util/List;FLandroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "phaseColor", "Landroidx/compose/ui/graphics/Color;", "phase", "Lcom/varun/pocketassistant/capture/CapturePhase;", "(Lcom/varun/pocketassistant/capture/CapturePhase;)J", "app_debug"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class RmsHistoryGraphKt {

    /* JADX INFO: compiled from: RmsHistoryGraph.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[CapturePhase.values().length];
            try {
                iArr[CapturePhase.LISTENING.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CapturePhase.PRE_ROLL_FLUSH.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CapturePhase.LIVE_SPEECH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[CapturePhase.POST_ROLL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[CapturePhase.PAUSED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[CapturePhase.IDLE.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit RmsHistoryGraph$lambda$4(List list, float f, Modifier modifier, int i, int i2, Composer composer, int i3) {
        RmsHistoryGraph(list, f, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    public static final void RmsHistoryGraph(final List<RmsSample> samples, final float threshold, Modifier modifier, Composer $composer, final int $changed, final int i) {
        float f;
        Modifier modifier2;
        Composer $composer2;
        final Modifier modifier3;
        Modifier.Companion modifier4;
        Object obj;
        final long buffer;
        Intrinsics.checkNotNullParameter(samples, "samples");
        Composer $composer3 = $composer.startRestartGroup(-844656336);
        ComposerKt.sourceInformation($composer3, "C(RmsHistoryGraph)N(samples,threshold,modifier)35@1140L2145,31@1041L2244:RmsHistoryGraph.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changedInstance(samples) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            f = threshold;
            $dirty |= $composer3.changed(f) ? 32 : 16;
        } else {
            f = threshold;
        }
        int i2 = i & 4;
        if (i2 != 0) {
            $dirty |= 384;
            modifier2 = modifier;
        } else if (($changed & 384) == 0) {
            modifier2 = modifier;
            $dirty |= $composer3.changed(modifier2) ? 256 : 128;
        } else {
            modifier2 = modifier;
        }
        if (!$composer3.shouldExecute(($dirty & 147) != 146, $dirty & 1)) {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
            modifier3 = modifier2;
        } else {
            if (i2 != 0) {
                modifier4 = Modifier.INSTANCE;
            } else {
                modifier4 = modifier2;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-844656336, $dirty, -1, "com.varun.pocketassistant.ui.RmsHistoryGraph (RmsHistoryGraph.kt:22)");
            }
            long buffer2 = ColorKt.Color(4287604899L);
            final long preroll = ColorKt.Color(4291396906L);
            final long live = ColorKt.Color(4280250224L);
            final long postroll = ColorKt.Color(4290272312L);
            final long idle = ColorKt.Color(4291877076L);
            final long line = ColorKt.Color(4279903266L);
            final long thresh = ColorKt.Color(4289930782L);
            Modifier modifierM868height3ABfNKs = SizeKt.m868height3ABfNKs(SizeKt.fillMaxWidth$default(modifier4, 0.0f, 1, null), Dp.m7582constructorimpl(140));
            ComposerKt.sourceInformationMarkerStart($composer3, 34097233, "CC(remember):RmsHistoryGraph.kt#9igjgp");
            boolean zChangedInstance = $composer3.changedInstance(samples) | (($dirty & 112) == 32);
            Object objRememberedValue = $composer3.rememberedValue();
            if (zChangedInstance || objRememberedValue == Composer.INSTANCE.getEmpty()) {
                final float f2 = f;
                buffer = buffer2;
                obj = new Function1() { // from class: com.varun.pocketassistant.ui.RmsHistoryGraphKt$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return RmsHistoryGraphKt.RmsHistoryGraph$lambda$3$lambda$2(samples, f2, buffer, preroll, live, postroll, idle, thresh, line, (DrawScope) obj2);
                    }
                };
                $composer3.updateRememberedValue(obj);
            } else {
                obj = objRememberedValue;
                buffer = buffer2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer2 = $composer3;
            CanvasKt.Canvas(modifierM868height3ABfNKs, (Function1) obj, $composer2, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            modifier3 = modifier4;
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.RmsHistoryGraphKt$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return RmsHistoryGraphKt.RmsHistoryGraph$lambda$4(samples, threshold, modifier3, $changed, i, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit RmsHistoryGraph$lambda$3$lambda$2(List $samples, float $threshold, long $buffer, long $preroll, long $live, long $postroll, long $idle, long $thresh, long $line, DrawScope Canvas) {
        Float fValueOf;
        float maxRms;
        long startMs;
        float windowMs;
        float w;
        float maxRms2;
        long color;
        List list = $samples;
        Intrinsics.checkNotNullParameter(Canvas, "$this$Canvas");
        float w2 = Float.intBitsToFloat((int) (Canvas.mo5419getSizeNHjbRc() >> 32));
        float h = Float.intBitsToFloat((int) (Canvas.mo5419getSizeNHjbRc() & 4294967295L));
        if (w2 <= 0.0f || h <= 0.0f) {
            return Unit.INSTANCE;
        }
        RmsSample rmsSample = (RmsSample) CollectionsKt.lastOrNull(list);
        long now = rmsSample != null ? rmsSample.getAtMs() : System.currentTimeMillis();
        float windowMs2 = 60000.0f;
        long startMs2 = now - ((long) 60000.0f);
        float f = 2.5f * $threshold;
        Iterator it = list.iterator();
        if (it.hasNext()) {
            float rms = ((RmsSample) it.next()).getRms();
            while (it.hasNext()) {
                rms = Math.max(rms, ((RmsSample) it.next()).getRms());
            }
            fValueOf = Float.valueOf(rms);
        } else {
            fValueOf = null;
        }
        float maxRms3 = Math.max(f, Math.max(fValueOf != null ? fValueOf.floatValue() : $threshold, 1.0f));
        if (list.size() < 2) {
            maxRms = maxRms3;
            startMs = startMs2;
            windowMs = 60000.0f;
            w = w2;
        } else {
            int i = 0;
            int lastIndex = CollectionsKt.getLastIndex(list);
            while (i < lastIndex) {
                RmsSample a = (RmsSample) list.get(i);
                RmsSample b = (RmsSample) list.get(i + 1);
                float left = RmsHistoryGraph$lambda$3$lambda$2$xFor(startMs2, windowMs2, w2, a.getAtMs());
                float right = RmsHistoryGraph$lambda$3$lambda$2$xFor(startMs2, windowMs2, w2, b.getAtMs());
                long startMs3 = startMs2;
                float windowMs3 = windowMs2;
                float w3 = w2;
                if (right <= left) {
                    maxRms2 = maxRms3;
                } else {
                    switch (WhenMappings.$EnumSwitchMapping$0[a.getPhase().ordinal()]) {
                        case 1:
                            color = Color.m4838copywmQWz5c($buffer, (14 & 1) != 0 ? Color.m4842getAlphaimpl($buffer) : 0.22f, (14 & 2) != 0 ? Color.m4846getRedimpl($buffer) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl($buffer) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl($buffer) : 0.0f);
                            break;
                        case 2:
                            color = Color.m4838copywmQWz5c($preroll, (14 & 1) != 0 ? Color.m4842getAlphaimpl($preroll) : 0.28f, (14 & 2) != 0 ? Color.m4846getRedimpl($preroll) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl($preroll) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl($preroll) : 0.0f);
                            break;
                        case 3:
                            color = Color.m4838copywmQWz5c($live, (14 & 1) != 0 ? Color.m4842getAlphaimpl($live) : 0.28f, (14 & 2) != 0 ? Color.m4846getRedimpl($live) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl($live) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl($live) : 0.0f);
                            break;
                        case 4:
                            color = Color.m4838copywmQWz5c($postroll, (14 & 1) != 0 ? Color.m4842getAlphaimpl($postroll) : 0.28f, (14 & 2) != 0 ? Color.m4846getRedimpl($postroll) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl($postroll) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl($postroll) : 0.0f);
                            break;
                        default:
                            color = Color.m4838copywmQWz5c($idle, (14 & 1) != 0 ? Color.m4842getAlphaimpl($idle) : 0.12f, (14 & 2) != 0 ? Color.m4846getRedimpl($idle) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl($idle) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl($idle) : 0.0f);
                            break;
                    }
                    long jM4588constructorimpl = Offset.m4588constructorimpl((((long) Float.floatToRawIntBits(left)) << 32) | (((long) Float.floatToRawIntBits(0.0f)) & 4294967295L));
                    long color2 = Size.m4656constructorimpl((((long) Float.floatToRawIntBits(right - left)) << 32) | (((long) Float.floatToRawIntBits(h)) & 4294967295L));
                    maxRms2 = maxRms3;
                    DrawScope.m5413drawRectnJ9OG0$default(Canvas, color, jM4588constructorimpl, color2, 0.0f, null, null, 0, 120, null);
                }
                i++;
                maxRms3 = maxRms2;
                w2 = w3;
                windowMs2 = windowMs3;
                startMs2 = startMs3;
                lastIndex = lastIndex;
                list = $samples;
            }
            maxRms = maxRms3;
            startMs = startMs2;
            windowMs = windowMs2;
            w = w2;
        }
        float ty = RmsHistoryGraph$lambda$3$lambda$2$yFor(h, maxRms, $threshold);
        DrawScope.m5405drawLineNGM6Ib0$default(Canvas, Color.m4838copywmQWz5c($thresh, (14 & 1) != 0 ? Color.m4842getAlphaimpl($thresh) : 0.7f, (14 & 2) != 0 ? Color.m4846getRedimpl($thresh) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl($thresh) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl($thresh) : 0.0f), Offset.m4588constructorimpl((((long) Float.floatToRawIntBits(0.0f)) << 32) | (((long) Float.floatToRawIntBits(ty)) & 4294967295L)), Offset.m4588constructorimpl((((long) Float.floatToRawIntBits(w)) << 32) | (((long) Float.floatToRawIntBits(ty)) & 4294967295L)), 2.0f, 0, null, 0.0f, null, 0, 496, null);
        if ($samples.size() < 2) {
            return Unit.INSTANCE;
        }
        Path path = AndroidPath_androidKt.Path();
        int i2 = 0;
        for (Object obj : $samples) {
            int i3 = i2 + 1;
            if (i2 < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            RmsSample rmsSample2 = (RmsSample) obj;
            float w4 = w;
            float fRmsHistoryGraph$lambda$3$lambda$2$xFor = RmsHistoryGraph$lambda$3$lambda$2$xFor(startMs, windowMs, w4, rmsSample2.getAtMs());
            float fRmsHistoryGraph$lambda$3$lambda$2$yFor = RmsHistoryGraph$lambda$3$lambda$2$yFor(h, maxRms, rmsSample2.getRms());
            if (i2 == 0) {
                path.moveTo(fRmsHistoryGraph$lambda$3$lambda$2$xFor, fRmsHistoryGraph$lambda$3$lambda$2$yFor);
            } else {
                path.lineTo(fRmsHistoryGraph$lambda$3$lambda$2$xFor, fRmsHistoryGraph$lambda$3$lambda$2$yFor);
            }
            i2 = i3;
            w = w4;
        }
        DrawScope.m5409drawPathLG529CI$default(Canvas, path, $line, 0.0f, new Stroke(3.0f, 0.0f, StrokeCap.INSTANCE.m5211getRoundKaPHkGw(), 0, null, 26, null), null, 0, 52, null);
        return Unit.INSTANCE;
    }

    private static final float RmsHistoryGraph$lambda$3$lambda$2$xFor(long startMs, float windowMs, float w, long t) {
        return RangesKt.coerceIn((t - startMs) / windowMs, 0.0f, 1.0f) * w;
    }

    private static final float RmsHistoryGraph$lambda$3$lambda$2$yFor(float h, float maxRms, float rms) {
        return h - (RangesKt.coerceIn(rms / maxRms, 0.0f, 1.0f) * h);
    }

    public static final long phaseColor(CapturePhase phase) {
        Intrinsics.checkNotNullParameter(phase, "phase");
        switch (WhenMappings.$EnumSwitchMapping$0[phase.ordinal()]) {
            case 1:
                return ColorKt.Color(4287604899L);
            case 2:
                return ColorKt.Color(4291396906L);
            case 3:
                return ColorKt.Color(4280250224L);
            case 4:
                return ColorKt.Color(4290272312L);
            case 5:
                return ColorKt.Color(4290272312L);
            case 6:
                return ColorKt.Color(4291877076L);
            default:
                throw new NoWhenBranchMatchedException();
        }
    }
}
