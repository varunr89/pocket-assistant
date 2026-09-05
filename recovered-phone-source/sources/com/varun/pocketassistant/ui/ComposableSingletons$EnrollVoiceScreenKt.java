package com.varun.pocketassistant.ui;

import androidx.compose.foundation.layout.RowScope;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: EnrollVoiceScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
public final class ComposableSingletons$EnrollVoiceScreenKt {
    public static final ComposableSingletons$EnrollVoiceScreenKt INSTANCE = new ComposableSingletons$EnrollVoiceScreenKt();

    /* JADX INFO: renamed from: lambda$-610868653, reason: not valid java name */
    private static Function2<Composer, Integer, Unit> f88lambda$610868653 = ComposableLambdaKt.composableLambdaInstance(-610868653, false, new Function2() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$EnrollVoiceScreenKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$EnrollVoiceScreenKt.lambda__610868653$lambda$0((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* JADX INFO: renamed from: lambda$-582820940, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f87lambda$582820940 = ComposableLambdaKt.composableLambdaInstance(-582820940, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$EnrollVoiceScreenKt$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$EnrollVoiceScreenKt.lambda__582820940$lambda$1((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: getLambda$-582820940$app_debug, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m8172getLambda$582820940$app_debug() {
        return f87lambda$582820940;
    }

    /* JADX INFO: renamed from: getLambda$-610868653$app_debug, reason: not valid java name */
    public final Function2<Composer, Integer, Unit> m8173getLambda$610868653$app_debug() {
        return f88lambda$610868653;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__610868653$lambda$0(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C157@6353L22:EnrollVoiceScreen.kt#w5368b");
        if ($composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-610868653, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$EnrollVoiceScreenKt.lambda$-610868653.<anonymous> (EnrollVoiceScreen.kt:157)");
            }
            TextKt.m3142Text4IGK_g("Train my voice", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__582820940$lambda$1(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C159@6465L12:EnrollVoiceScreen.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-582820940, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$EnrollVoiceScreenKt.lambda$-582820940.<anonymous> (EnrollVoiceScreen.kt:159)");
            }
            TextKt.m3142Text4IGK_g("Back", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }
}
