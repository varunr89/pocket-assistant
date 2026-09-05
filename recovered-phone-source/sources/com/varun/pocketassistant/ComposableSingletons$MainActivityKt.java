package com.varun.pocketassistant;

import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import com.varun.pocketassistant.ui.HomeScreenKt;
import com.varun.pocketassistant.ui.theme.ThemeKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

/* JADX INFO: compiled from: MainActivity.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
public final class ComposableSingletons$MainActivityKt {
    public static final ComposableSingletons$MainActivityKt INSTANCE = new ComposableSingletons$MainActivityKt();

    /* JADX INFO: renamed from: lambda$-1013778627, reason: not valid java name */
    private static Function2<Composer, Integer, Unit> f81lambda$1013778627 = ComposableLambdaKt.composableLambdaInstance(-1013778627, false, new Function2() { // from class: com.varun.pocketassistant.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda0
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$MainActivityKt.lambda__1013778627$lambda$0((Composer) obj, ((Integer) obj2).intValue());
        }
    });
    private static Function2<Composer, Integer, Unit> lambda$2088334923 = ComposableLambdaKt.composableLambdaInstance(2088334923, false, new Function2() { // from class: com.varun.pocketassistant.ComposableSingletons$MainActivityKt$$ExternalSyntheticLambda1
        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Object obj, Object obj2) {
            return ComposableSingletons$MainActivityKt.lambda_2088334923$lambda$1((Composer) obj, ((Integer) obj2).intValue());
        }
    });

    /* JADX INFO: renamed from: getLambda$-1013778627$app_debug, reason: not valid java name */
    public final Function2<Composer, Integer, Unit> m8146getLambda$1013778627$app_debug() {
        return f81lambda$1013778627;
    }

    public final Function2<Composer, Integer, Unit> getLambda$2088334923$app_debug() {
        return lambda$2088334923;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda_2088334923$lambda$1(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C26@948L74:MainActivity.kt#dml3tf");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(2088334923, $changed, -1, "com.varun.pocketassistant.ComposableSingletons$MainActivityKt.lambda$2088334923.<anonymous> (MainActivity.kt:26)");
            }
            ThemeKt.PocketAssistantTheme(f81lambda$1013778627, $composer, 6);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit lambda__1013778627$lambda$0(Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C27@987L21:MainActivity.kt#dml3tf");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1013778627, $changed, -1, "com.varun.pocketassistant.ComposableSingletons$MainActivityKt.lambda$-1013778627.<anonymous> (MainActivity.kt:27)");
            }
            HomeScreenKt.PocketAssistantRoot(null, $composer, 0, 1);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
