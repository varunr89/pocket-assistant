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
import com.google.common.net.HttpHeaders;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DayTimeline.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
public final class ComposableSingletons$DayTimelineKt {
    public static final ComposableSingletons$DayTimelineKt INSTANCE = new ComposableSingletons$DayTimelineKt();
    private static Function3<RowScope, Composer, Integer, Unit> lambda$635985752 = ComposableLambdaKt.composableLambdaInstance(635985752, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DayTimelineKt.lambda_635985752$lambda$0((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });
    private static Function3<RowScope, Composer, Integer, Unit> lambda$268093455 = ComposableLambdaKt.composableLambdaInstance(268093455, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DayTimelineKt.lambda_268093455$lambda$1((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });
    private static Function3<RowScope, Composer, Integer, Unit> lambda$1490696080 = ComposableLambdaKt.composableLambdaInstance(1490696080, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DayTimelineKt.lambda_1490696080$lambda$2((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: lambda$-1162321825, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f85lambda$1162321825 = ComposableLambdaKt.composableLambdaInstance(-1162321825, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt$$ExternalSyntheticLambda3
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DayTimelineKt.lambda__1162321825$lambda$3((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });
    private static Function3<RowScope, Composer, Integer, Unit> lambda$1327585042 = ComposableLambdaKt.composableLambdaInstance(1327585042, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt$$ExternalSyntheticLambda4
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DayTimelineKt.lambda_1327585042$lambda$4((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: lambda$-229378629, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f86lambda$229378629 = ComposableLambdaKt.composableLambdaInstance(-229378629, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt$$ExternalSyntheticLambda5
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DayTimelineKt.lambda__229378629$lambda$5((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: getLambda$-1162321825$app_debug, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m8170getLambda$1162321825$app_debug() {
        return f85lambda$1162321825;
    }

    /* JADX INFO: renamed from: getLambda$-229378629$app_debug, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m8171getLambda$229378629$app_debug() {
        return f86lambda$229378629;
    }

    public final Function3<RowScope, Composer, Integer, Unit> getLambda$1327585042$app_debug() {
        return lambda$1327585042;
    }

    public final Function3<RowScope, Composer, Integer, Unit> getLambda$1490696080$app_debug() {
        return lambda$1490696080;
    }

    public final Function3<RowScope, Composer, Integer, Unit> getLambda$268093455$app_debug() {
        return lambda$268093455;
    }

    public final Function3<RowScope, Composer, Integer, Unit> getLambda$635985752$app_debug() {
        return lambda$635985752;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda_635985752$lambda$0(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C81@3353L9:DayTimeline.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(635985752, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt.lambda$635985752.<anonymous> (DayTimeline.kt:81)");
            }
            TextKt.m3142Text4IGK_g("‹", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda_268093455$lambda$1(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C87@3600L13:DayTimeline.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(268093455, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt.lambda$268093455.<anonymous> (DayTimeline.kt:87)");
            }
            TextKt.m3142Text4IGK_g("Today", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda_1490696080$lambda$2(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C88@3662L9:DayTimeline.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1490696080, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt.lambda$1490696080.<anonymous> (DayTimeline.kt:88)");
            }
            TextKt.m3142Text4IGK_g("›", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__1162321825$lambda$3(RowScope OutlinedButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(OutlinedButton, "$this$OutlinedButton");
        ComposerKt.sourceInformation($composer, "C103@4268L29:DayTimeline.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1162321825, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt.lambda$-1162321825.<anonymous> (DayTimeline.kt:103)");
            }
            TextKt.m3142Text4IGK_g("Create from selection", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda_1327585042$lambda$4(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C325@13847L15:DayTimeline.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1327585042, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt.lambda$1327585042.<anonymous> (DayTimeline.kt:325)");
            }
            TextKt.m3142Text4IGK_g("Dismiss", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__229378629$lambda$5(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C326@13906L14:DayTimeline.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-229378629, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DayTimelineKt.lambda$-229378629.<anonymous> (DayTimeline.kt:326)");
            }
            TextKt.m3142Text4IGK_g(HttpHeaders.ACCEPT, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }
}
