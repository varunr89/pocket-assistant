package com.varun.pocketassistant.ui;

import android.content.Context;
import androidx.activity.compose.ActivityResultRegistryKt;
import androidx.activity.compose.ManagedActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.RowScopeInstance;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.MicKt;
import androidx.compose.material3.AppBarKt;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.IconKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.ScaffoldKt;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocalMap;
import androidx.compose.runtime.EffectsKt;
import androidx.compose.runtime.MutableIntState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.ProvidableCompositionLocal;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.SnapshotIntStateKt;
import androidx.compose.runtime.SnapshotStateKt__SnapshotStateKt;
import androidx.compose.runtime.Updater;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.ComposedModifierKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.draw.ClipKt;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.platform.AndroidCompositionLocals_androidKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.varun.pocketassistant.PocketAssistantApp;
import com.varun.pocketassistant.capture.RecordingService;
import com.varun.pocketassistant.speech.SpeakerProfileStore;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

/* JADX INFO: compiled from: EnrollVoiceScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\u001a\u001b\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0007¢\u0006\u0002\u0010\u0004¨\u0006\u0005²\u0006\n\u0010\u0006\u001a\u00020\u0007X\u008a\u008e\u0002²\u0006\n\u0010\b\u001a\u00020\tX\u008a\u008e\u0002²\u0006\n\u0010\n\u001a\u00020\u000bX\u008a\u008e\u0002²\u0006\f\u0010\f\u001a\u0004\u0018\u00010\u0007X\u008a\u008e\u0002"}, d2 = {"EnrollVoiceScreen", "", "onBack", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "app_debug", NotificationCompat.CATEGORY_STATUS, "", RecordingService.CHANNEL_ID, "", "secondsLeft", "", "error"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class EnrollVoiceScreenKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit EnrollVoiceScreen$lambda$22(Function0 function0, int i, Composer composer, int i2) {
        EnrollVoiceScreen(function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    public static final void EnrollVoiceScreen(final Function0<Unit> onBack, Composer $composer, final int $changed) {
        Intrinsics.checkNotNullParameter(onBack, "onBack");
        Composer $composer2 = $composer.startRestartGroup(-1323941933);
        ComposerKt.sourceInformation($composer2, "C(EnrollVoiceScreen)N(onBack)54@2334L7,57@2464L24,59@2508L175,65@2705L34,66@2763L33,67@2814L42,71@2981L81,69@2887L175,155@6302L220,163@6563L11,164@6593L1917,154@6275L2235:EnrollVoiceScreen.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changedInstance(onBack) ? 4 : 2;
        }
        if ($composer2.shouldExecute(($dirty & 3) != 2, $dirty & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1323941933, $dirty, -1, "com.varun.pocketassistant.ui.EnrollVoiceScreen (EnrollVoiceScreen.kt:53)");
            }
            ProvidableCompositionLocal<Context> localContext = AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC(<get-current>):CompositionLocal.kt#9igjgp");
            Object objConsume = $composer2.consume(localContext);
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final Context context = (Context) objConsume;
            Context applicationContext = context.getApplicationContext();
            Intrinsics.checkNotNull(applicationContext, "null cannot be cast to non-null type com.varun.pocketassistant.PocketAssistantApp");
            PocketAssistantApp app = (PocketAssistantApp) applicationContext;
            final SpeakerProfileStore store = app.getContainer().getSpeakerStore();
            ComposerKt.sourceInformationMarkerStart($composer2, 773894976, "CC(rememberCoroutineScope)N(getContext)608@27648L68:Effects.kt#9igjgp");
            ComposerKt.sourceInformationMarkerStart($composer2, 683737348, "CC(remember):Effects.kt#9igjgp");
            Object objRememberedValue = $composer2.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object objCreateCompositionCoroutineScope = EffectsKt.createCompositionCoroutineScope(EmptyCoroutineContext.INSTANCE, $composer2);
                $composer2.updateRememberedValue(objCreateCompositionCoroutineScope);
                objRememberedValue = objCreateCompositionCoroutineScope;
            }
            final CoroutineScope scope = (CoroutineScope) objRememberedValue;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, -997868190, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
            Object objRememberedValue2 = $composer2.rememberedValue();
            if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(store.hasEnrollment() ? "Voice profile enrolled. Re-train anytime." : "No voice profile yet.", null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default);
                objRememberedValue2 = objMutableStateOf$default;
            }
            final MutableState status$delegate = (MutableState) objRememberedValue2;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, -997862027, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
            Object objRememberedValue3 = $composer2.rememberedValue();
            if (objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(false, null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default2);
                objRememberedValue3 = objMutableStateOf$default2;
            }
            final MutableState recording$delegate = (MutableState) objRememberedValue3;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, -997860172, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
            Object objRememberedValue4 = $composer2.rememberedValue();
            if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                Object objMutableIntStateOf = SnapshotIntStateKt.mutableIntStateOf(0);
                $composer2.updateRememberedValue(objMutableIntStateOf);
                objRememberedValue4 = objMutableIntStateOf;
            }
            final MutableIntState secondsLeft$delegate = (MutableIntState) objRememberedValue4;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, -997858531, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
            Object objRememberedValue5 = $composer2.rememberedValue();
            if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(null, null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default3);
                objRememberedValue5 = objMutableStateOf$default3;
            }
            final MutableState error$delegate = (MutableState) objRememberedValue5;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ActivityResultContracts.RequestPermission requestPermission = new ActivityResultContracts.RequestPermission();
            ComposerKt.sourceInformationMarkerStart($composer2, -997853148, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
            Object objRememberedValue6 = $composer2.rememberedValue();
            if (objRememberedValue6 == Composer.INSTANCE.getEmpty()) {
                Object obj = new Function1() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$13$lambda$12(error$delegate, ((Boolean) obj2).booleanValue());
                    }
                };
                $composer2.updateRememberedValue(obj);
                objRememberedValue6 = obj;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            final ManagedActivityResultLauncher permissionLauncher = ActivityResultRegistryKt.rememberLauncherForActivityResult(requestPermission, (Function1) objRememberedValue6, $composer2, 48);
            ScaffoldKt.m2857ScaffoldTvnljyQ(null, ComposableLambdaKt.rememberComposableLambda(280954263, true, new Function2() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$15(onBack, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), null, null, null, 0, MaterialTheme.INSTANCE.getColorScheme($composer2, MaterialTheme.$stable).getBackground(), 0L, null, ComposableLambdaKt.rememberComposableLambda(-610662622, true, new Function3() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda4
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj2, Object obj3, Object obj4) {
                    return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$21(status$delegate, recording$delegate, secondsLeft$delegate, error$delegate, context, permissionLauncher, scope, store, (PaddingValues) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer2, 54), $composer2, 805306416, 445);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer2.skipToGroupEnd();
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda5
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$22(onBack, $changed, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    private static final String EnrollVoiceScreen$lambda$1(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean EnrollVoiceScreen$lambda$4(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void EnrollVoiceScreen$lambda$5(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final int EnrollVoiceScreen$lambda$7(MutableIntState $secondsLeft$delegate) {
        return $secondsLeft$delegate.getIntValue();
    }

    private static final String EnrollVoiceScreen$lambda$10(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit EnrollVoiceScreen$lambda$13$lambda$12(MutableState $error$delegate, boolean granted) {
        if (!granted) {
            $error$delegate.setValue("Microphone permission required");
        }
        return Unit.INSTANCE;
    }

    private static final void EnrollVoiceScreen$startEnrollment(Context context, ManagedActivityResultLauncher<String, Boolean> managedActivityResultLauncher, CoroutineScope scope, MutableState<String> mutableState, MutableState<Boolean> mutableState2, MutableIntState secondsLeft$delegate, SpeakerProfileStore store, MutableState<String> mutableState3) {
        boolean granted = ContextCompat.checkSelfPermission(context, "android.permission.RECORD_AUDIO") == 0;
        if (!granted) {
            managedActivityResultLauncher.launch("android.permission.RECORD_AUDIO");
            return;
        }
        mutableState.setValue(null);
        EnrollVoiceScreen$lambda$5(mutableState2, true);
        secondsLeft$delegate.setIntValue(15);
        BuildersKt__Builders_commonKt.launch$default(scope, Dispatchers.getIO(), null, new EnrollVoiceScreenKt$EnrollVoiceScreen$startEnrollment$1(store, mutableState2, mutableState, secondsLeft$delegate, mutableState3, null), 2, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit EnrollVoiceScreen$lambda$15(final Function0 $onBack, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C158@6412L85,156@6316L196:EnrollVoiceScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(280954263, $changed, -1, "com.varun.pocketassistant.ui.EnrollVoiceScreen.<anonymous> (EnrollVoiceScreen.kt:156)");
            }
            AppBarKt.m2222TopAppBarGHTll3U(ComposableSingletons$EnrollVoiceScreenKt.INSTANCE.m8173getLambda$610868653$app_debug(), null, ComposableLambdaKt.rememberComposableLambda(-1222137711, true, new Function2() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$15$lambda$14($onBack, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), null, 0.0f, null, null, null, $composer, 390, 250);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit EnrollVoiceScreen$lambda$15$lambda$14(Function0 $onBack, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C159@6434L45:EnrollVoiceScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1222137711, $changed, -1, "com.varun.pocketassistant.ui.EnrollVoiceScreen.<anonymous>.<anonymous> (EnrollVoiceScreen.kt:159)");
            }
            ButtonKt.TextButton($onBack, null, false, null, null, null, null, null, null, ComposableSingletons$EnrollVoiceScreenKt.INSTANCE.m8172getLambda$582820940$app_debug(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:62:0x045a  */
    /* JADX WARN: Code duplicated, block: B:65:0x053c  */
    public static final Unit EnrollVoiceScreen$lambda$21(final MutableState $status$delegate, final MutableState $recording$delegate, final MutableIntState $secondsLeft$delegate, final MutableState $error$delegate, final Context $context, final ManagedActivityResultLauncher $permissionLauncher, final CoroutineScope $scope, final SpeakerProfileStore $store, PaddingValues padding, Composer $composer, int $changed) {
        Function0<ComposeUiNode> function0;
        Composer composer;
        Function0<ComposeUiNode> function1;
        MeasurePolicy measurePolicy;
        Function0<ComposeUiNode> function2;
        boolean zChangedInstance;
        Object obj;
        Intrinsics.checkNotNullParameter(padding, "padding");
        ComposerKt.sourceInformation($composer, "CN(padding)165@6614L1890:EnrollVoiceScreen.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(padding) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if (!$composer.shouldExecute(($dirty2 & 19) != 18, $dirty2 & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-610662622, $dirty2, -1, "com.varun.pocketassistant.ui.EnrollVoiceScreen.<anonymous> (EnrollVoiceScreen.kt:165)");
            }
            Modifier modifierM830padding3ABfNKs = PaddingKt.m830padding3ABfNKs(PaddingKt.padding(SizeKt.fillMaxSize$default(Modifier.INSTANCE, 0.0f, 1, null), padding), Dp.m7582constructorimpl(20));
            Arrangement.Vertical verticalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(verticalM689spacedBy0680j_4, Alignment.INSTANCE.getStart(), $composer, ((48 >> 3) & 14) | ((48 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer, modifierM830padding3ABfNKs);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i = ((((48 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer.startReusableNode();
            if ($composer.getInserting()) {
                function0 = constructor;
                $composer.createNode(function0);
            } else {
                function0 = constructor;
                $composer.useNode();
            }
            Composer composerM4159constructorimpl = Updater.m4159constructorimpl($composer);
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyColumnMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash);
            }
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i2 = (i >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
            int i3 = ((48 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -171186752, "C175@7052L10,176@7112L11,172@6839L316,178@7203L10,178@7168L57,189@7628L29,190@7670L496,205@8390L10,206@8451L11,202@8179L315:EnrollVoiceScreen.kt#w5368b");
            TextKt.m3142Text4IGK_g("Speak naturally for 15 seconds in a quiet place. We’ll use this as your voice profile so transcripts can mark what you said.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodyLarge(), $composer, 0, 0, 65530);
            TextKt.m3142Text4IGK_g(EnrollVoiceScreen$lambda$1($status$delegate), (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getTitleLarge(), $composer, 0, 0, 65534);
            if (EnrollVoiceScreen$lambda$4($recording$delegate)) {
                $composer.startReplaceGroup(-170819806);
                ComposerKt.sourceInformation($composer, "182@7374L10,183@7443L11,180@7271L212");
                TextKt.m3142Text4IGK_g("Recording… " + EnrollVoiceScreen$lambda$7($secondsLeft$delegate) + "s left", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getSecondary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getHeadlineMedium(), $composer, 0, 0, 65530);
            } else {
                $composer.startReplaceGroup(-178023338);
            }
            $composer.endReplaceGroup();
            if (EnrollVoiceScreen$lambda$10($error$delegate) != null) {
                $composer.startReplaceGroup(-170550912);
                ComposerKt.sourceInformation($composer, "187@7583L11,187@7547L54");
                String strEnrollVoiceScreen$lambda$10 = EnrollVoiceScreen$lambda$10($error$delegate);
                Intrinsics.checkNotNull(strEnrollVoiceScreen$lambda$10);
                TextKt.m3142Text4IGK_g(strEnrollVoiceScreen$lambda$10, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getError(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131066);
                composer = $composer;
            } else {
                composer = $composer;
                composer.startReplaceGroup(-178023338);
            }
            composer.endReplaceGroup();
            SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer, 6);
            Composer composer2 = composer;
            ComposerKt.sourceInformationMarkerStart(composer2, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            Modifier modifier = Modifier.INSTANCE;
            Composer composer3 = composer;
            MeasurePolicy measurePolicyRowMeasurePolicy = RowKt.rowMeasurePolicy(Arrangement.INSTANCE.getStart(), Alignment.INSTANCE.getTop(), composer2, ((0 >> 3) & 14) | ((0 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
            CompositionLocalMap currentCompositionLocalMap2 = composer2.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier(composer2, modifier);
            Function0<ComposeUiNode> constructor2 = ComposeUiNode.INSTANCE.getConstructor();
            int i4 = ((((0 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer2.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer2.startReusableNode();
            if (composer2.getInserting()) {
                function1 = constructor2;
                composer2.createNode(function1);
            } else {
                function1 = constructor2;
                composer2.useNode();
            }
            Composer composerM4159constructorimpl2 = Updater.m4159constructorimpl(composer2);
            Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyRowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl2.getInserting()) {
                measurePolicy = measurePolicyRowMeasurePolicy;
                function2 = function1;
            } else {
                measurePolicy = measurePolicyRowMeasurePolicy;
                function2 = function1;
                if (!Intrinsics.areEqual(composerM4159constructorimpl2.rememberedValue(), Integer.valueOf(iHashCode2))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                int i5 = (i4 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                RowScopeInstance rowScopeInstance = RowScopeInstance.INSTANCE;
                int i6 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, -243686673, "C192@7730L37,196@7927L225,191@7692L460:EnrollVoiceScreen.kt#w5368b");
                boolean z = !EnrollVoiceScreen$lambda$4($recording$delegate);
                Modifier modifierClip = ClipKt.clip(Modifier.INSTANCE, RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(12)));
                ComposerKt.sourceInformationMarkerStart(composer2, 546329261, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
                zChangedInstance = composer2.changedInstance($context) | composer2.changedInstance($permissionLauncher) | composer2.changedInstance($scope) | composer2.changedInstance($store);
                Object objRememberedValue = composer2.rememberedValue();
                if (!zChangedInstance || objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    obj = new Function0() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda0
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$17$lambda$16($recording$delegate, $context, $permissionLauncher, $scope, $error$delegate, $secondsLeft$delegate, $store, $status$delegate);
                        }
                    };
                    composer2.updateRememberedValue(obj);
                } else {
                    obj = objRememberedValue;
                }
                ComposerKt.sourceInformationMarkerEnd(r2);
                ButtonKt.Button((Function0) obj, modifierClip, z, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-668268648, true, new Function3() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj2, Object obj3, Object obj4) {
                        return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$18($store, (RowScope) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                    }
                }, r2, 54), r2, 805306368, 504);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer2.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                TextKt.m3142Text4IGK_g("This is a lightweight on-device profile for now. We’ll swap in a Tensor speaker-embedding model later for higher accuracy.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer3, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer3, MaterialTheme.$stable).getBodyMedium(), composer3, 0, 0, 65530);
                ComposerKt.sourceInformationMarkerEnd(composer3);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
            }
            composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
            composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash2);
            Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
            int i7 = (i4 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            RowScopeInstance rowScopeInstance2 = RowScopeInstance.INSTANCE;
            int i8 = ((0 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -243686673, "C192@7730L37,196@7927L225,191@7692L460:EnrollVoiceScreen.kt#w5368b");
            boolean z2 = !EnrollVoiceScreen$lambda$4($recording$delegate);
            Modifier modifierClip2 = ClipKt.clip(Modifier.INSTANCE, RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(12)));
            ComposerKt.sourceInformationMarkerStart(composer2, 546329261, "CC(remember):EnrollVoiceScreen.kt#9igjgp");
            zChangedInstance = composer2.changedInstance($context) | composer2.changedInstance($permissionLauncher) | composer2.changedInstance($scope) | composer2.changedInstance($store);
            Object objRememberedValue2 = composer2.rememberedValue();
            if (zChangedInstance) {
                obj = new Function0() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$17$lambda$16($recording$delegate, $context, $permissionLauncher, $scope, $error$delegate, $secondsLeft$delegate, $store, $status$delegate);
                    }
                };
                composer2.updateRememberedValue(obj);
            } else {
                obj = new Function0() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda0
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$17$lambda$16($recording$delegate, $context, $permissionLauncher, $scope, $error$delegate, $secondsLeft$delegate, $store, $status$delegate);
                    }
                };
                composer2.updateRememberedValue(obj);
            }
            ComposerKt.sourceInformationMarkerEnd(r2);
            ButtonKt.Button((Function0) obj, modifierClip2, z2, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-668268648, true, new Function3() { // from class: com.varun.pocketassistant.ui.EnrollVoiceScreenKt$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj2, Object obj3, Object obj4) {
                    return EnrollVoiceScreenKt.EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$18($store, (RowScope) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, r2, 54), r2, 805306368, 504);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer2.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            TextKt.m3142Text4IGK_g("This is a lightweight on-device profile for now. We’ll swap in a Tensor speaker-embedding model later for higher accuracy.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer3, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer3, MaterialTheme.$stable).getBodyMedium(), composer3, 0, 0, 65530);
            ComposerKt.sourceInformationMarkerEnd(composer3);
            ComposerKt.sourceInformationMarkerEnd($composer);
            $composer.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$17$lambda$16(MutableState $recording$delegate, Context $context, ManagedActivityResultLauncher $permissionLauncher, CoroutineScope $scope, MutableState $error$delegate, MutableIntState $secondsLeft$delegate, SpeakerProfileStore $store, MutableState $status$delegate) {
        if (!EnrollVoiceScreen$lambda$4($recording$delegate)) {
            EnrollVoiceScreen$startEnrollment($context, $permissionLauncher, $scope, $error$delegate, $recording$delegate, $secondsLeft$delegate, $store, $status$delegate);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit EnrollVoiceScreen$lambda$21$lambda$20$lambda$19$lambda$18(SpeakerProfileStore $store, RowScope Button, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(Button, "$this$Button");
        ComposerKt.sourceInformation($composer, "C197@7949L50,198@8020L28,199@8069L65:EnrollVoiceScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-668268648, $changed, -1, "com.varun.pocketassistant.ui.EnrollVoiceScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous> (EnrollVoiceScreen.kt:197)");
            }
            IconKt.m2599Iconww6aTOc(MicKt.getMic(Icons.INSTANCE.getDefault()), (String) null, (Modifier) null, 0L, $composer, 48, 12);
            SpacerKt.Spacer(SizeKt.m887width3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), $composer, 6);
            TextKt.m3142Text4IGK_g($store.hasEnrollment() ? "Re-train" : "Start training", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }
}
