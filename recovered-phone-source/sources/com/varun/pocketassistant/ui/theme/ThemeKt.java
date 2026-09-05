package com.varun.pocketassistant.ui.theme;

import androidx.compose.material3.ColorScheme;
import androidx.compose.material3.ColorSchemeKt;
import androidx.compose.material3.MaterialThemeKt;
import androidx.compose.material3.Typography;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.ColorKt;
import androidx.compose.ui.graphics.Shadow;
import androidx.compose.ui.graphics.drawscope.DrawStyle;
import androidx.compose.ui.text.PlatformTextStyle;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontSynthesis;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.font.GenericFontFamily;
import androidx.compose.ui.text.intl.LocaleList;
import androidx.compose.ui.text.style.BaselineShift;
import androidx.compose.ui.text.style.LineHeightStyle;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.text.style.TextGeometricTransform;
import androidx.compose.ui.text.style.TextIndent;
import androidx.compose.ui.text.style.TextMotion;
import androidx.compose.ui.unit.TextUnitKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: Theme.kt */
/* JADX INFO: loaded from: classes6.dex */
@Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\f\u001a\u00020\r2\u0011\u0010\u000e\u001a\r\u0012\u0004\u0012\u00020\r0\u000f¢\u0006\u0002\b\u0010H\u0007¢\u0006\u0002\u0010\u0011\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0003\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0004\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0005\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0006\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u0010\u0010\u0007\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"TealInk", "Landroidx/compose/ui/graphics/Color;", "J", "TealBright", "Fog", "Mist", "Ember", "Ink", "ColorScheme", "Landroidx/compose/material3/ColorScheme;", "Typography", "Landroidx/compose/material3/Typography;", "PocketAssistantTheme", "", "content", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "(Lkotlin/jvm/functions/Function2;Landroidx/compose/runtime/Composer;I)V", "app_debug"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class ThemeKt {
    private static final Typography Typography;
    private static final long TealInk = ColorKt.Color(4279188798L);
    private static final long TealBright = ColorKt.Color(4280250224L);
    private static final long Fog = ColorKt.Color(4294178548L);
    private static final long Mist = ColorKt.Color(4293192936L);
    private static final long Ember = ColorKt.Color(4290272312L);
    private static final long Ink = ColorKt.Color(4279903266L);
    private static final ColorScheme ColorScheme = ColorSchemeKt.m2385lightColorSchemeCXl9yA$default(TealInk, Color.INSTANCE.m4877getWhite0d7_KjU(), 0, 0, 0, TealBright, Color.INSTANCE.m4877getWhite0d7_KjU(), 0, 0, Ember, Color.INSTANCE.m4877getWhite0d7_KjU(), 0, 0, Fog, Ink, Color.INSTANCE.m4877getWhite0d7_KjU(), Ink, Mist, ColorKt.Color(4283062870L), 0, 0, 0, ColorKt.Color(4289930782L), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -4712036, 15, null);

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PocketAssistantTheme$lambda$0(Function2 function2, int i, Composer composer, int i2) {
        PocketAssistantTheme(function2, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    static {
        GenericFontFamily serif = FontFamily.INSTANCE.getSerif();
        Typography = new Typography(new TextStyle(0L, TextUnitKt.getSp(40), FontWeight.INSTANCE.getMedium(), (FontStyle) null, (FontSynthesis) null, serif, (String) null, TextUnitKt.getSp(-0.5d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(44), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null), null, null, null, new TextStyle(0L, TextUnitKt.getSp(28), FontWeight.INSTANCE.getMedium(), (FontStyle) null, (FontSynthesis) null, FontFamily.INSTANCE.getSerif(), (String) null, 0L, (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(34), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16646105, (DefaultConstructorMarker) null), null, new TextStyle(0L, TextUnitKt.getSp(20), FontWeight.INSTANCE.getSemiBold(), (FontStyle) null, (FontSynthesis) null, FontFamily.INSTANCE.getSansSerif(), (String) null, 0L, (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, 0L, (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16777177, (DefaultConstructorMarker) null), null, null, new TextStyle(0L, TextUnitKt.getSp(16), FontWeight.INSTANCE.getNormal(), (FontStyle) null, (FontSynthesis) null, FontFamily.INSTANCE.getSansSerif(), (String) null, 0L, (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(24), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16646105, (DefaultConstructorMarker) null), new TextStyle(0L, TextUnitKt.getSp(14), (FontWeight) null, (FontStyle) null, (FontSynthesis) null, FontFamily.INSTANCE.getSansSerif(), (String) null, 0L, (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(20), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16646109, (DefaultConstructorMarker) null), null, new TextStyle(0L, TextUnitKt.getSp(14), FontWeight.INSTANCE.getSemiBold(), (FontStyle) null, (FontSynthesis) null, FontFamily.INSTANCE.getSansSerif(), (String) null, 0L, (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, 0L, (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16777177, (DefaultConstructorMarker) null), null, null, 27054, null);
    }

    public static final void PocketAssistantTheme(Function2<? super Composer, ? super Integer, Unit> content, Composer $composer, final int $changed) {
        final Function2<? super Composer, ? super Integer, Unit> function2;
        Intrinsics.checkNotNullParameter(content, "content");
        Composer $composer2 = $composer.startRestartGroup(-1411752491);
        ComposerKt.sourceInformation($composer2, "C(PocketAssistantTheme)N(content)73@2152L115:Theme.kt#p75jyo");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changedInstance(content) ? 4 : 2;
        }
        if (!$composer2.shouldExecute(($dirty & 3) != 2, $dirty & 1)) {
            function2 = content;
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1411752491, $dirty, -1, "com.varun.pocketassistant.ui.theme.PocketAssistantTheme (Theme.kt:72)");
            }
            function2 = content;
            MaterialThemeKt.MaterialTheme(ColorScheme, null, Typography, function2, $composer2, (($dirty << 9) & 7168) | 390, 2);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.theme.ThemeKt$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return ThemeKt.PocketAssistantTheme$lambda$0(function2, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }
}
