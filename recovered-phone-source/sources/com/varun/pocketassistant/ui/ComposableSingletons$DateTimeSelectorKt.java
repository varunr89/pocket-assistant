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
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DateTimeSelector.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
public final class ComposableSingletons$DateTimeSelectorKt {
    public static final ComposableSingletons$DateTimeSelectorKt INSTANCE = new ComposableSingletons$DateTimeSelectorKt();

    /* JADX INFO: renamed from: lambda$-1882791782, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f82lambda$1882791782 = ComposableLambdaKt.composableLambdaInstance(-1882791782, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DateTimeSelectorKt.lambda__1882791782$lambda$0((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: lambda$-2147439272, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f84lambda$2147439272 = ComposableLambdaKt.composableLambdaInstance(-2147439272, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DateTimeSelectorKt.lambda__2147439272$lambda$1((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });
    private static Function3<RowScope, Composer, Integer, Unit> lambda$648380868 = ComposableLambdaKt.composableLambdaInstance(648380868, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DateTimeSelectorKt.lambda_648380868$lambda$2((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: lambda$-1960686493, reason: not valid java name */
    private static Function3<RowScope, Composer, Integer, Unit> f83lambda$1960686493 = ComposableLambdaKt.composableLambdaInstance(-1960686493, false, new Function3() { // from class: com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt$$ExternalSyntheticLambda3
        @Override // kotlin.jvm.functions.Function3
        public final Object invoke(Object obj, Object obj2, Object obj3) {
            return ComposableSingletons$DateTimeSelectorKt.lambda__1960686493$lambda$3((RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
        }
    });

    /* JADX INFO: renamed from: getLambda$-1882791782$app_debug, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m8165getLambda$1882791782$app_debug() {
        return f82lambda$1882791782;
    }

    /* JADX INFO: renamed from: getLambda$-1960686493$app_debug, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m8166getLambda$1960686493$app_debug() {
        return f83lambda$1960686493;
    }

    /* JADX INFO: renamed from: getLambda$-2147439272$app_debug, reason: not valid java name */
    public final Function3<RowScope, Composer, Integer, Unit> m8167getLambda$2147439272$app_debug() {
        return f84lambda$2147439272;
    }

    public final Function3<RowScope, Composer, Integer, Unit> getLambda$648380868$app_debug() {
        return lambda$648380868;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__1882791782$lambda$0(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C88@3410L10:DateTimeSelector.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1882791782, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt.lambda$-1882791782.<anonymous> (DateTimeSelector.kt:88)");
            }
            TextKt.m3142Text4IGK_g("OK", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__2147439272$lambda$1(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C91@3529L14:DateTimeSelector.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-2147439272, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt.lambda$-2147439272.<anonymous> (DateTimeSelector.kt:91)");
            }
            TextKt.m3142Text4IGK_g("Cancel", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda_648380868$lambda$2(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C121@4530L10:DateTimeSelector.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(648380868, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt.lambda$648380868.<anonymous> (DateTimeSelector.kt:121)");
            }
            TextKt.m3142Text4IGK_g("OK", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__1960686493$lambda$3(RowScope TextButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TextButton, "$this$TextButton");
        ComposerKt.sourceInformation($composer, "C124@4649L14:DateTimeSelector.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1960686493, $changed, -1, "com.varun.pocketassistant.ui.ComposableSingletons$DateTimeSelectorKt.lambda$-1960686493.<anonymous> (DateTimeSelector.kt:124)");
            }
            TextKt.m3142Text4IGK_g("Cancel", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }
}
