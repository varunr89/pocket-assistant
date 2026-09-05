package com.varun.pocketassistant.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import androidx.compose.foundation.ClickableKt;
import androidx.compose.foundation.ScrollKt;
import androidx.compose.foundation.interaction.MutableInteractionSource;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.BoxKt;
import androidx.compose.foundation.layout.BoxScopeInstance;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.PaddingValues;
import androidx.compose.foundation.layout.RowKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.RowScopeInstance;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.lazy.LazyDslKt;
import androidx.compose.foundation.lazy.LazyItemScope;
import androidx.compose.foundation.lazy.LazyListScope;
import androidx.compose.foundation.text.KeyboardActions;
import androidx.compose.foundation.text.KeyboardOptions;
import androidx.compose.material3.AndroidAlertDialog_androidKt;
import androidx.compose.material3.AppBarKt;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.ChipKt;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.OutlinedTextFieldDefaults;
import androidx.compose.material3.OutlinedTextFieldKt;
import androidx.compose.material3.ScaffoldKt;
import androidx.compose.material3.SwitchKt;
import androidx.compose.material3.TextFieldColors;
import androidx.compose.material3.TextKt;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocalMap;
import androidx.compose.runtime.EffectsKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.ProvidableCompositionLocal;
import androidx.compose.runtime.RecomposeScopeImplKt;
import androidx.compose.runtime.ScopeUpdateScope;
import androidx.compose.runtime.SnapshotStateKt__SnapshotStateKt;
import androidx.compose.runtime.Updater;
import androidx.compose.runtime.internal.ComposableLambdaKt;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.ComposedModifierKt;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.graphics.Shape;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.platform.AndroidCompositionLocals_androidKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.input.VisualTransformation;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.unit.Dp;
import androidx.core.app.NotificationCompat;
import androidx.profileinstaller.ProfileVerifier;
import com.google.android.gms.actions.SearchIntents;
import com.varun.pocketassistant.PocketAssistantApp;
import com.varun.pocketassistant.pipeline.CleanupPrompts;
import com.varun.pocketassistant.pipeline.OpenRouterModelInfo;
import com.varun.pocketassistant.pipeline.OpenRouterModels;
import com.varun.pocketassistant.pipeline.OpenRouterModelsClient;
import com.varun.pocketassistant.pipeline.PipelineConfig;
import com.varun.pocketassistant.pipeline.PipelineSettings;
import com.varun.pocketassistant.pipeline.ProviderMode;
import com.varun.pocketassistant.pipeline.ReasoningEffort;
import com.varun.pocketassistant.speech.DiarizationLabels;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.enums.EnumEntries;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: compiled from: PipelineSettingsScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u001a\u001b\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0007¢\u0006\u0002\u0010\u0004\u001ae\u0010\u0005\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\u00072\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00072\b\b\u0002\u0010\u0011\u001a\u00020\u000f2\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0007H\u0003¢\u0006\u0002\u0010\u0013\u001aU\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u000f2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\r2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u0003H\u0003¢\u0006\u0002\u0010\u0017\u001a&\u0010\u0018\u001a\u00020\u00072\u0006\u0010\u0019\u001a\u00020\u00072\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u001b\u001a\u00020\u0007H\u0002\u001a\u0010\u0010\u001c\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0002\u001a\u0018\u0010\u001e\u001a\u00020\u00072\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u0007H\u0002\u001a\r\u0010!\u001a\u00020\u0001H\u0003¢\u0006\u0002\u0010\"\u001a)\u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020%2\u0012\u0010\f\u001a\u000e\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\u00010\rH\u0003¢\u0006\u0002\u0010&¨\u0006'²\u0006\n\u0010(\u001a\u00020)X\u008a\u008e\u0002²\u0006\f\u0010*\u001a\u0004\u0018\u00010\u0007X\u008a\u008e\u0002²\u0006\n\u0010+\u001a\u00020\u000fX\u008a\u008e\u0002²\u0006\u0010\u0010,\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u008a\u008e\u0002²\u0006\u0010\u0010-\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u008a\u008e\u0002²\u0006\n\u0010.\u001a\u00020\u000fX\u008a\u008e\u0002²\u0006\n\u0010/\u001a\u00020\u0007X\u008a\u008e\u0002²\u0006\n\u00100\u001a\u00020\u000fX\u008a\u008e\u0002"}, d2 = {"PipelineSettingsScreen", "", "onBack", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "ModelDropdown", "label", "", "models", "", "Lcom/varun/pocketassistant/pipeline/OpenRouterModelInfo;", "selectedId", "onSelect", "Lkotlin/Function1;", "enabled", "", "emptyHint", "preferReasoningFirst", "supportingText", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;ZLjava/lang/String;ZLjava/lang/String;Landroidx/compose/runtime/Composer;II)V", "ModelPickerDialog", "title", "onDismiss", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "ensureModelId", "current", "catalog", "fallback", "normalizeModelId", "id", "normalizeTemplate", "value", "default", "BatteryOptimizationRow", "(Landroidx/compose/runtime/Composer;I)V", "ModeRow", "selected", "Lcom/varun/pocketassistant/pipeline/ProviderMode;", "(Lcom/varun/pocketassistant/pipeline/ProviderMode;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V", "app_debug", "settings", "Lcom/varun/pocketassistant/pipeline/PipelineSettings;", NotificationCompat.CATEGORY_STATUS, "loadingModels", "sttModels", "chatModels", "showPicker", SearchIntents.EXTRA_QUERY, "ignoring"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class PipelineSettingsScreenKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit BatteryOptimizationRow$lambda$132(int i, Composer composer, int i2) {
        BatteryOptimizationRow(composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModeRow$lambda$138(ProviderMode providerMode, Function1 function1, int i, Composer composer, int i2) {
        ModeRow(providerMode, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$96(String str, List list, String str2, Function1 function1, boolean z, String str3, boolean z2, String str4, int i, int i2, Composer composer, int i3) {
        ModelDropdown(str, list, str2, function1, z, str3, z2, str4, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelPickerDialog$lambda$117(String str, List list, String str2, boolean z, Function1 function1, Function0 function0, int i, Composer composer, int i2) {
        ModelPickerDialog(str, list, str2, z, function1, function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$79(Function0 function0, int i, Composer composer, int i2) {
        PipelineSettingsScreen(function0, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    public static final void PipelineSettingsScreen(final Function0<Unit> onBack, Composer $composer, final int $changed) {
        boolean z;
        Object next;
        EnumEntries<ReasoningEffort> selectable;
        MutableState settings$delegate;
        OpenRouterModelsClient modelsClient;
        final CoroutineScope scope;
        final PipelineConfig config;
        Intrinsics.checkNotNullParameter(onBack, "onBack");
        Composer $composer2 = $composer.startRestartGroup(90931997);
        ComposerKt.sourceInformation($composer2, "C(PipelineSettingsScreen)N(onBack)63@2832L7,66@2972L43,67@3032L24,69@3078L42,70@3139L42,71@3207L34,72@3263L67,73@3353L67,78@3575L363,153@6793L154,153@6772L175,160@6980L214,168@7202L16463,159@6953L16712:PipelineSettingsScreen.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer2.changedInstance(onBack) ? 4 : 2;
        }
        if ($composer2.shouldExecute(($dirty & 3) != 2, $dirty & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(90931997, $dirty, -1, "com.varun.pocketassistant.ui.PipelineSettingsScreen (PipelineSettingsScreen.kt:62)");
            }
            ProvidableCompositionLocal<Context> localContext = AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart($composer2, 2023513938, "CC(<get-current>):CompositionLocal.kt#9igjgp");
            Object objConsume = $composer2.consume(localContext);
            ComposerKt.sourceInformationMarkerEnd($composer2);
            Context context = (Context) objConsume;
            Context applicationContext = context.getApplicationContext();
            Intrinsics.checkNotNull(applicationContext, "null cannot be cast to non-null type com.varun.pocketassistant.PocketAssistantApp");
            final PocketAssistantApp app = (PocketAssistantApp) applicationContext;
            PipelineConfig config2 = app.getContainer().getPipelineConfig();
            ComposerKt.sourceInformationMarkerStart($composer2, 1486311944, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue = $composer2.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                z = false;
                Object openRouterModelsClient = new OpenRouterModelsClient(config2);
                $composer2.updateRememberedValue(openRouterModelsClient);
                objRememberedValue = openRouterModelsClient;
            } else {
                z = false;
            }
            OpenRouterModelsClient modelsClient2 = (OpenRouterModelsClient) objRememberedValue;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 773894976, "CC(rememberCoroutineScope)N(getContext)608@27648L68:Effects.kt#9igjgp");
            ComposerKt.sourceInformationMarkerStart($composer2, 683737348, "CC(remember):Effects.kt#9igjgp");
            Object objRememberedValue2 = $composer2.rememberedValue();
            if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                Object objCreateCompositionCoroutineScope = EffectsKt.createCompositionCoroutineScope(EmptyCoroutineContext.INSTANCE, $composer2);
                $composer2.updateRememberedValue(objCreateCompositionCoroutineScope);
                objRememberedValue2 = objCreateCompositionCoroutineScope;
            }
            CoroutineScope scope2 = (CoroutineScope) objRememberedValue2;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1486315335, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue3 = $composer2.rememberedValue();
            if (objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(config2.load(), null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default);
                objRememberedValue3 = objMutableStateOf$default;
            }
            MutableState settings$delegate2 = (MutableState) objRememberedValue3;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1486317287, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue4 = $composer2.rememberedValue();
            if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(null, null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default2);
                objRememberedValue4 = objMutableStateOf$default2;
            }
            final MutableState status$delegate = (MutableState) objRememberedValue4;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1486319455, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue5 = $composer2.rememberedValue();
            if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default3 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(z), null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default3);
                objRememberedValue5 = objMutableStateOf$default3;
            }
            final MutableState loadingModels$delegate = (MutableState) objRememberedValue5;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1486321280, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue6 = $composer2.rememberedValue();
            if (objRememberedValue6 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default4 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(CollectionsKt.emptyList(), null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default4);
                objRememberedValue6 = objMutableStateOf$default4;
            }
            final MutableState sttModels$delegate = (MutableState) objRememberedValue6;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            ComposerKt.sourceInformationMarkerStart($composer2, 1486324160, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue7 = $composer2.rememberedValue();
            if (objRememberedValue7 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default5 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(CollectionsKt.emptyList(), null, 2, null);
                $composer2.updateRememberedValue(objMutableStateOf$default5);
                objRememberedValue7 = objMutableStateOf$default5;
            }
            final MutableState chatModels$delegate = (MutableState) objRememberedValue7;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            Iterator it = PipelineSettingsScreen$lambda$14(chatModels$delegate).iterator();
            do {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
            } while (!OpenRouterModels.INSTANCE.matches(((OpenRouterModelInfo) next).getId(), PipelineSettingsScreen$lambda$2(settings$delegate2).getCloudSummaryModel()));
            final OpenRouterModelInfo selectedSummaryMeta = (OpenRouterModelInfo) next;
            Object cloudSummaryModel = PipelineSettingsScreen$lambda$2(settings$delegate2).getCloudSummaryModel();
            ComposerKt.sourceInformationMarkerStart($composer2, 1486331560, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            boolean zChanged = $composer2.changed(cloudSummaryModel) | $composer2.changed(selectedSummaryMeta);
            int i = 0;
            Object objRememberedValue8 = $composer2.rememberedValue();
            if (zChanged || objRememberedValue8 == Composer.INSTANCE.getEmpty()) {
                List<String> supportedEfforts = selectedSummaryMeta != null ? selectedSummaryMeta.getSupportedEfforts() : null;
                if (supportedEfforts == null) {
                    supportedEfforts = CollectionsKt.emptyList();
                }
                if (supportedEfforts.isEmpty()) {
                    selectable = ReasoningEffort.INSTANCE.getSelectable();
                } else {
                    List listListOf = CollectionsKt.listOf(ReasoningEffort.DEFAULT);
                    Iterable entries = ReasoningEffort.getEntries();
                    Collection arrayList = new ArrayList();
                    for (ReasoningEffort reasoningEffort : entries) {
                        boolean z2 = zChanged;
                        int i2 = i;
                        if (supportedEfforts.contains(reasoningEffort.getApiValue())) {
                            arrayList.add(reasoningEffort);
                        }
                        zChanged = z2;
                        i = i2;
                    }
                    selectable = CollectionsKt.plus((Collection) listListOf, arrayList);
                }
                $composer2.updateRememberedValue(selectable);
                objRememberedValue8 = selectable;
            } else {
                selectedSummaryMeta = selectedSummaryMeta;
            }
            final List effortOptions = (List) objRememberedValue8;
            ComposerKt.sourceInformationMarkerEnd($composer2);
            Unit unit = Unit.INSTANCE;
            ComposerKt.sourceInformationMarkerStart($composer2, 1486434327, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            boolean zChangedInstance = $composer2.changedInstance(scope2) | $composer2.changedInstance(modelsClient2) | $composer2.changedInstance(config2);
            Object objRememberedValue9 = $composer2.rememberedValue();
            if (zChangedInstance || objRememberedValue9 == Composer.INSTANCE.getEmpty()) {
                settings$delegate = settings$delegate2;
                modelsClient = modelsClient2;
                Object pipelineSettingsScreenKt$PipelineSettingsScreen$1$1 = new PipelineSettingsScreenKt$PipelineSettingsScreen$1$1(settings$delegate, sttModels$delegate, chatModels$delegate, scope2, status$delegate, config2, loadingModels$delegate, modelsClient, null);
                scope = scope2;
                config = config2;
                chatModels$delegate = chatModels$delegate;
                sttModels$delegate = sttModels$delegate;
                status$delegate = status$delegate;
                Object obj = (Function2) pipelineSettingsScreenKt$PipelineSettingsScreen$1$1;
                $composer2.updateRememberedValue(obj);
                objRememberedValue9 = obj;
            } else {
                scope = scope2;
                config = config2;
                modelsClient = modelsClient2;
                settings$delegate = settings$delegate2;
            }
            ComposerKt.sourceInformationMarkerEnd($composer2);
            EffectsKt.LaunchedEffect(unit, (Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object>) objRememberedValue9, $composer2, 6);
            final MutableState settings$delegate3 = settings$delegate;
            final OpenRouterModelsClient modelsClient3 = modelsClient;
            ScaffoldKt.m2857ScaffoldTvnljyQ(null, ComposableLambdaKt.rememberComposableLambda(-272964639, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda23
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$21(onBack, (Composer) obj2, ((Integer) obj3).intValue());
                }
            }, $composer2, 54), null, null, null, 0, 0L, 0L, null, ComposableLambdaKt.rememberComposableLambda(-508185684, true, new Function3() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda24
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj2, Object obj3, Object obj4) {
                    return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78(scope, modelsClient3, config, selectedSummaryMeta, app, settings$delegate3, loadingModels$delegate, status$delegate, sttModels$delegate, chatModels$delegate, effortOptions, (PaddingValues) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer2, 54), $composer2, 805306416, 509);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer2.skipToGroupEnd();
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda25
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj2, Object obj3) {
                    return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$79(onBack, $changed, (Composer) obj2, ((Integer) obj3).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final PipelineSettings PipelineSettingsScreen$lambda$2(MutableState<PipelineSettings> mutableState) {
        return mutableState.getValue();
    }

    private static final String PipelineSettingsScreen$lambda$5(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    private static final boolean PipelineSettingsScreen$lambda$8(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void PipelineSettingsScreen$lambda$9(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List<OpenRouterModelInfo> PipelineSettingsScreen$lambda$11(MutableState<List<OpenRouterModelInfo>> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final List<OpenRouterModelInfo> PipelineSettingsScreen$lambda$14(MutableState<List<OpenRouterModelInfo>> mutableState) {
        return mutableState.getValue();
    }

    static /* synthetic */ void PipelineSettingsScreen$refreshModels$default(CoroutineScope coroutineScope, MutableState mutableState, MutableState mutableState2, PipelineConfig pipelineConfig, MutableState mutableState3, OpenRouterModelsClient openRouterModelsClient, MutableState mutableState4, MutableState mutableState5, boolean z, int i, Object obj) {
        PipelineSettingsScreen$refreshModels(coroutineScope, mutableState, mutableState2, pipelineConfig, mutableState3, openRouterModelsClient, mutableState4, mutableState5, (i & 256) != 0 ? false : z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void PipelineSettingsScreen$refreshModels(CoroutineScope scope, MutableState<PipelineSettings> mutableState, MutableState<String> mutableState2, PipelineConfig config, MutableState<Boolean> mutableState3, OpenRouterModelsClient modelsClient, MutableState<List<OpenRouterModelInfo>> mutableState4, MutableState<List<OpenRouterModelInfo>> mutableState5, boolean silent) {
        MutableState<PipelineSettings> mutableState6;
        String key = StringsKt.trim((CharSequence) PipelineSettingsScreen$lambda$2(mutableState).getCloudApiKey()).toString();
        if (!StringsKt.isBlank(key)) {
            if (Intrinsics.areEqual(PipelineSettingsScreen$lambda$2(mutableState).getCloudBaseUrl(), PipelineSettings.DEFAULT_BASE_URL)) {
                mutableState6 = mutableState;
            } else {
                mutableState6 = mutableState;
                mutableState6.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2(mutableState), null, null, null, null, false, PipelineSettings.DEFAULT_BASE_URL, null, null, null, null, null, false, null, false, null, null, null, null, null, 524255, null));
            }
            BuildersKt__Builders_commonKt.launch$default(scope, null, null, new PipelineSettingsScreenKt$PipelineSettingsScreen$refreshModels$1(silent, PipelineSettings.DEFAULT_BASE_URL, config, mutableState3, mutableState2, modelsClient, key, mutableState4, mutableState5, mutableState6, null), 3, null);
            return;
        }
        if (silent) {
            return;
        }
        mutableState2.setValue("Set an OpenRouter API key to load models");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$21(final Function0 $onBack, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C163@7084L85,161@6994L190:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-272964639, $changed, -1, "com.varun.pocketassistant.ui.PipelineSettingsScreen.<anonymous> (PipelineSettingsScreen.kt:161)");
            }
            AppBarKt.m2222TopAppBarGHTll3U(ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$996345245$app_debug(), null, ComposableLambdaKt.rememberComposableLambda(-728263077, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda26
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$21$lambda$20($onBack, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer, 54), null, 0.0f, null, null, null, $composer, 390, 250);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$21$lambda$20(Function0 $onBack, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C164@7106L45:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-728263077, $changed, -1, "com.varun.pocketassistant.ui.PipelineSettingsScreen.<anonymous>.<anonymous> (PipelineSettingsScreen.kt:164)");
            }
            ButtonKt.TextButton($onBack, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$1070823870$app_debug(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:103:0x089c  */
    /* JADX WARN: Code duplicated, block: B:109:0x094f  */
    /* JADX WARN: Code duplicated, block: B:112:0x0957  */
    /* JADX WARN: Code duplicated, block: B:113:0x095c  */
    /* JADX WARN: Code duplicated, block: B:116:0x097b  */
    /* JADX WARN: Code duplicated, block: B:120:0x0a2d  */
    /* JADX WARN: Code duplicated, block: B:123:0x0a39  */
    /* JADX WARN: Code duplicated, block: B:124:0x0a3f  */
    /* JADX WARN: Code duplicated, block: B:135:0x0b81  */
    /* JADX WARN: Code duplicated, block: B:138:0x0b8d  */
    /* JADX WARN: Code duplicated, block: B:139:0x0b93  */
    /* JADX WARN: Code duplicated, block: B:150:0x0c65  */
    /* JADX WARN: Code duplicated, block: B:151:0x0c6a  */
    /* JADX WARN: Code duplicated, block: B:156:0x0cd8  */
    /* JADX WARN: Code duplicated, block: B:159:0x0cf2  */
    /* JADX WARN: Code duplicated, block: B:166:0x0db0  */
    /* JADX WARN: Code duplicated, block: B:169:0x0db8  */
    /* JADX WARN: Code duplicated, block: B:170:0x0dbd  */
    /* JADX WARN: Code duplicated, block: B:173:0x0ddb  */
    /* JADX WARN: Code duplicated, block: B:180:0x0e7d  */
    /* JADX WARN: Code duplicated, block: B:183:0x0e85  */
    /* JADX WARN: Code duplicated, block: B:184:0x0e8a  */
    /* JADX WARN: Code duplicated, block: B:190:0x0e9b  */
    /* JADX WARN: Code duplicated, block: B:192:0x0e9e  */
    /* JADX WARN: Code duplicated, block: B:194:0x0ea4  */
    /* JADX WARN: Code duplicated, block: B:196:0x0ebc  */
    /* JADX WARN: Code duplicated, block: B:198:0x0ec8  */
    /* JADX WARN: Code duplicated, block: B:199:0x0ecd  */
    /* JADX WARN: Code duplicated, block: B:202:0x0ee9  */
    /* JADX WARN: Code duplicated, block: B:206:0x0ff9  */
    /* JADX WARN: Code duplicated, block: B:209:0x1005  */
    /* JADX WARN: Code duplicated, block: B:210:0x100b  */
    /* JADX WARN: Code duplicated, block: B:222:0x10ba  */
    /* JADX WARN: Code duplicated, block: B:224:0x10da  */
    /* JADX WARN: Code duplicated, block: B:225:0x10dd  */
    /* JADX WARN: Code duplicated, block: B:228:0x10fd  */
    /* JADX WARN: Code duplicated, block: B:232:0x1109  */
    /* JADX WARN: Code duplicated, block: B:237:0x11a9  */
    /* JADX WARN: Code duplicated, block: B:241:0x1264  */
    /* JADX WARN: Code duplicated, block: B:244:0x129e  */
    /* JADX WARN: Code duplicated, block: B:248:0x1305  */
    /* JADX WARN: Code duplicated, block: B:252:0x134a  */
    /* JADX WARN: Code duplicated, block: B:255:0x1384  */
    /* JADX WARN: Code duplicated, block: B:259:0x13eb  */
    /* JADX WARN: Code duplicated, block: B:263:0x1430  */
    /* JADX WARN: Code duplicated, block: B:266:0x146a  */
    /* JADX WARN: Code duplicated, block: B:270:0x14d1  */
    /* JADX WARN: Code duplicated, block: B:278:0x1525  */
    /* JADX WARN: Code duplicated, block: B:285:0x158f  */
    /* JADX WARN: Code duplicated, block: B:292:0x15f4  */
    /* JADX WARN: Code duplicated, block: B:295:0x163c  */
    /* JADX WARN: Code duplicated, block: B:296:0x1685  */
    /* JADX WARN: Code duplicated, block: B:299:0x1705  */
    /* JADX WARN: Code duplicated, block: B:305:0x1118 A[SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:35:0x0257  */
    /* JADX WARN: Code duplicated, block: B:36:0x0267  */
    /* JADX WARN: Code duplicated, block: B:39:0x02d2  */
    /* JADX WARN: Code duplicated, block: B:40:0x02e0  */
    /* JADX WARN: Code duplicated, block: B:43:0x0348  */
    /* JADX WARN: Code duplicated, block: B:44:0x0356  */
    /* JADX WARN: Code duplicated, block: B:47:0x03be  */
    /* JADX WARN: Code duplicated, block: B:48:0x03cc  */
    /* JADX WARN: Code duplicated, block: B:51:0x0460  */
    /* JADX WARN: Code duplicated, block: B:54:0x046c  */
    /* JADX WARN: Code duplicated, block: B:55:0x0472  */
    /* JADX WARN: Code duplicated, block: B:66:0x05af  */
    /* JADX WARN: Code duplicated, block: B:69:0x05bb  */
    /* JADX WARN: Code duplicated, block: B:70:0x05c1  */
    /* JADX WARN: Code duplicated, block: B:81:0x0697  */
    /* JADX WARN: Code duplicated, block: B:82:0x069c  */
    /* JADX WARN: Code duplicated, block: B:85:0x0719  */
    /* JADX WARN: Code duplicated, block: B:86:0x0727  */
    /* JADX WARN: Code duplicated, block: B:89:0x07e8  */
    /* JADX WARN: Code duplicated, block: B:96:0x0859  */
    public static final Unit PipelineSettingsScreen$lambda$78(CoroutineScope $scope, final OpenRouterModelsClient $modelsClient, PipelineConfig $config, OpenRouterModelInfo $selectedSummaryMeta, final PocketAssistantApp $app, final MutableState $settings$delegate, final MutableState $loadingModels$delegate, final MutableState $status$delegate, final MutableState $sttModels$delegate, final MutableState $chatModels$delegate, List $effortOptions, PaddingValues padding, Composer $composer, int $changed) {
        Composer composer;
        Composer composer2;
        Object objRememberedValue;
        Object objRememberedValue2;
        Object objRememberedValue3;
        Object objRememberedValue4;
        int iHashCode;
        Function0<ComposeUiNode> constructor;
        Function0<ComposeUiNode> function0;
        Composer composerM4159constructorimpl;
        int iHashCode2;
        Function0<ComposeUiNode> constructor2;
        Function0<ComposeUiNode> function1;
        Composer composerM4159constructorimpl2;
        String str;
        Object objRememberedValue5;
        Object objRememberedValue6;
        boolean z;
        boolean zChangedInstance;
        Object objRememberedValue7;
        int i;
        boolean z2;
        String str2;
        Object objRememberedValue8;
        boolean zModelSupportsDiarize;
        int iHashCode3;
        Function0<ComposeUiNode> constructor3;
        Function0<ComposeUiNode> function2;
        Composer composerM4159constructorimpl3;
        int iHashCode4;
        Function0<ComposeUiNode> constructor4;
        Function0<ComposeUiNode> function3;
        Composer composerM4159constructorimpl4;
        String str3;
        boolean z3;
        Object objRememberedValue9;
        boolean z4;
        String str4;
        Object objRememberedValue10;
        boolean z5;
        String str5;
        boolean z6;
        String str6;
        Object objRememberedValue11;
        Composer composer3;
        int iHashCode5;
        Function0<ComposeUiNode> constructor5;
        Function0<ComposeUiNode> function4;
        Composer composerM4159constructorimpl5;
        int i2;
        List<ReasoningEffort> list;
        int i3;
        Object objRememberedValue12;
        String glossary;
        Object objRememberedValue13;
        Object objRememberedValue14;
        String cleanupPrompt;
        Object objRememberedValue15;
        Object objRememberedValue16;
        String summaryPrompt;
        Object objRememberedValue17;
        Object objRememberedValue18;
        boolean zChangedInstance2;
        Object objRememberedValue19;
        final MutableState mutableState;
        boolean zChangedInstance3;
        Object objRememberedValue20;
        boolean zChangedInstance4;
        Object objRememberedValue21;
        boolean z7;
        boolean zChanged;
        Object objRememberedValue22;
        Composer composer4;
        String defaultEffort;
        final CoroutineScope coroutineScope = $scope;
        final PipelineConfig pipelineConfig = $config;
        final MutableState mutableState2 = $settings$delegate;
        Intrinsics.checkNotNullParameter(padding, "padding");
        ComposerKt.sourceInformation($composer, "CN(padding)174@7392L21,169@7223L16436:PipelineSettingsScreen.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer.changed(padding) ? 4 : 2;
        }
        int $dirty2 = $dirty;
        if ($composer.shouldExecute(($dirty2 & 19) != 18, $dirty2 & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-508185684, $dirty2, -1, "com.varun.pocketassistant.ui.PipelineSettingsScreen.<anonymous> (PipelineSettingsScreen.kt:169)");
            }
            Modifier modifierVerticalScroll$default = ScrollKt.verticalScroll$default(PaddingKt.m830padding3ABfNKs(PaddingKt.padding(SizeKt.fillMaxSize$default(Modifier.INSTANCE, 0.0f, 1, null), padding), Dp.m7582constructorimpl(20)), ScrollKt.rememberScrollState(0, $composer, 0, 1), false, null, false, 14, null);
            Arrangement.Vertical verticalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(12));
            ComposerKt.sourceInformationMarkerStart($composer, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(verticalM689spacedBy0680j_4, Alignment.INSTANCE.getStart(), $composer, ((48 >> 3) & 14) | ((48 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode6 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer, modifierVerticalScroll$default);
            Function0<ComposeUiNode> constructor6 = ComposeUiNode.INSTANCE.getConstructor();
            int i4 = ((((48 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer.startReusableNode();
            if ($composer.getInserting()) {
                $composer.createNode(constructor6);
            } else {
                $composer.useNode();
            }
            Composer composerM4159constructorimpl6 = Updater.m4159constructorimpl($composer);
            Updater.m4166setimpl(composerM4159constructorimpl6, measurePolicyColumnMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl6, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl6.getInserting()) {
                composer = $composer;
            } else {
                composer = $composer;
                if (!Intrinsics.areEqual(composerM4159constructorimpl6.rememberedValue(), Integer.valueOf(iHashCode6))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl6, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
                int i5 = (i4 >> 6) & 14;
                composer2 = composer;
                ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
                int i6 = ((48 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, 2041934907, "C181@7827L10,182@7888L11,177@7503L428,185@7983L10,185@7945L60,188@8159L10,189@8219L11,186@8018L244,191@8319L10,191@8275L67,192@8381L42,192@8355L68,194@8475L10,194@8437L61,195@8541L46,195@8511L76,197@8645L10,197@8601L67,198@8711L46,198@8681L76,200@8809L10,200@8771L61,201@8875L46,201@8845L76,203@8935L1072,226@10021L24,228@10100L10,228@10059L63,234@10511L10,235@10571L11,229@10135L479,239@10724L46,237@10627L371,247@11054L19,250@11220L331,246@11012L539,260@11616L10,260@11565L74,271@12076L69,261@11652L508,275@12272L1167,299@13497L10,299@13453L67,302@13653L10,303@13713L11,300@13533L223,316@14252L73,305@13769L571,319@14398L10,319@14354L67,322@14569L10,323@14629L11,320@14434L238,342@15518L600,325@14685L1448,356@16204L10,356@16147L80,360@16451L10,361@16511L11,357@16240L314,366@16682L21,363@16567L664,382@17342L46,380@17245L399,389@17696L10,389@17658L60,393@17929L10,394@17989L11,390@17731L301,399@18184L43,397@18046L389,406@18469L43,406@18448L131,412@18742L48,410@18593L422,419@19049L48,419@19028L142,425@19333L48,423@19184L425,432@19643L48,432@19622L142,437@19812L884,436@19778L1030,463@20856L881,462@20822L1053,487@21923L1002,486@21889L1186,515@23214L29,520@23545L10,521@23606L11,516@23256L393:PipelineSettingsScreen.kt#w5368b");
                TextKt.m3142Text4IGK_g("Cloud (OpenRouter): silence-trim only (no speedup), ~10 min capture rolls when cloud ASR is preferred, and long files are chunked at ~10 min. Local Parakeet still uses trim + 1.35×. Models load from OpenRouter.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodyMedium(), composer2, 0, 0, 65530);
                TextKt.m3142Text4IGK_g("Routing", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleLarge(), composer2, 6, 0, 65534);
                TextKt.m3142Text4IGK_g("Pick Cloud or Local per stage. Fallback (below) is one switch for all stages.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 6, 0, 65530);
                TextKt.m3142Text4IGK_g("Transcription", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                ProviderMode asrMode = PipelineSettingsScreen$lambda$2(mutableState2).getAsrMode();
                ComposerKt.sourceInformationMarkerStart(composer2, 204428192, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue = composer2.rememberedValue();
                if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda44
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$23$lambda$22(mutableState2, (ProviderMode) obj);
                        }
                    };
                    composer2.updateRememberedValue(objRememberedValue);
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModeRow(asrMode, (Function1) objRememberedValue, composer2, 48);
                TextKt.m3142Text4IGK_g("Cleanup", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                ProviderMode cleanupMode = PipelineSettingsScreen$lambda$2(mutableState2).getCleanupMode();
                ComposerKt.sourceInformationMarkerStart(composer2, 204433316, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue2 = composer2.rememberedValue();
                if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue2 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda8
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$25$lambda$24(mutableState2, (ProviderMode) obj);
                        }
                    };
                    composer2.updateRememberedValue(objRememberedValue2);
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModeRow(cleanupMode, (Function1) objRememberedValue2, composer2, 48);
                TextKt.m3142Text4IGK_g("Summarization", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                ProviderMode summaryMode = PipelineSettingsScreen$lambda$2(mutableState2).getSummaryMode();
                ComposerKt.sourceInformationMarkerStart(composer2, 204438756, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue3 = composer2.rememberedValue();
                if (objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue3 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda14
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$27$lambda$26(mutableState2, (ProviderMode) obj);
                        }
                    };
                    composer2.updateRememberedValue(objRememberedValue3);
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModeRow(summaryMode, (Function1) objRememberedValue3, composer2, 48);
                TextKt.m3142Text4IGK_g("Actions", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                ProviderMode actionsMode = PipelineSettingsScreen$lambda$2(mutableState2).getActionsMode();
                ComposerKt.sourceInformationMarkerStart(composer2, 204444004, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue4 = composer2.rememberedValue();
                if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue4 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda15
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$29$lambda$28(mutableState2, (ProviderMode) obj);
                        }
                    };
                    composer2.updateRememberedValue(objRememberedValue4);
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModeRow(actionsMode, (Function1) objRememberedValue4, composer2, 48);
                Modifier modifierFillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                Alignment.Vertical centerVertically = Alignment.INSTANCE.getCenterVertically();
                Arrangement.Horizontal spaceBetween = Arrangement.INSTANCE.getSpaceBetween();
                ComposerKt.sourceInformationMarkerStart(composer2, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
                MeasurePolicy measurePolicyRowMeasurePolicy = RowKt.rowMeasurePolicy(spaceBetween, centerVertically, composer2, ((438 >> 3) & 14) | ((438 >> 3) & 112));
                ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
                CompositionLocalMap currentCompositionLocalMap2 = composer2.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier(composer2, modifierFillMaxWidth$default);
                constructor = ComposeUiNode.INSTANCE.getConstructor();
                int i7 = ((((438 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!(composer2.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer2.startReusableNode();
                if (composer2.getInserting()) {
                    function0 = constructor;
                    composer2.createNode(function0);
                } else {
                    function0 = constructor;
                    composer2.useNode();
                }
                composerM4159constructorimpl = Updater.m4159constructorimpl(composer2);
                Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyRowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                    composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                    composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash2);
                }
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                int i8 = (i7 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                int i9 = ((438 >> 6) & 112) | 6;
                RowScope rowScope = RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart(composer2, -1065560182, "C208@9154L655,222@9926L48,220@9826L167:PipelineSettingsScreen.kt#w5368b");
                Modifier modifierM834paddingqDBjuR0$default = PaddingKt.m834paddingqDBjuR0$default(RowScope.weight$default(rowScope, Modifier.INSTANCE, 1.0f, false, 2, null), 0.0f, 0.0f, Dp.m7582constructorimpl(12), 0.0f, 11, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
                MeasurePolicy measurePolicyColumnMeasurePolicy2 = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), composer2, ((0 >> 3) & 14) | ((0 >> 3) & 112));
                ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
                CompositionLocalMap currentCompositionLocalMap3 = composer2.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier3 = ComposedModifierKt.materializeModifier(composer2, modifierM834paddingqDBjuR0$default);
                constructor2 = ComposeUiNode.INSTANCE.getConstructor();
                int i10 = ((((0 << 3) & 112) << 6) & 896) | 6;
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
                composerM4159constructorimpl2 = Updater.m4159constructorimpl(composer2);
                Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyColumnMeasurePolicy2, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap3, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash3 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl2.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl2.rememberedValue(), Integer.valueOf(iHashCode2))) {
                    composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
                    composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash3);
                }
                Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier3, ComposeUiNode.INSTANCE.getSetModifier());
                int i11 = (i10 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance2 = ColumnScopeInstance.INSTANCE;
                int i12 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, -719514340, "C209@9281L10,209@9236L68,216@9672L10,217@9740L11,210@9325L466:PipelineSettingsScreen.kt#w5368b");
                TextKt.m3142Text4IGK_g("Allow fallback", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                if (PipelineSettingsScreen$lambda$2(mutableState2).getAllowFallback()) {
                    str = "If the primary side fails or is unavailable, try the other.";
                } else {
                    str = "Only the selected Cloud/Local side runs — no cross-fallback.";
                }
                TextKt.m3142Text4IGK_g(str, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer2.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                boolean allowFallback = PipelineSettingsScreen$lambda$2(mutableState2).getAllowFallback();
                ComposerKt.sourceInformationMarkerStart(composer2, -1974011646, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue5 = composer2.rememberedValue();
                if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                    Object obj = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda16
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj2) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$33$lambda$32$lambda$31(mutableState2, ((Boolean) obj2).booleanValue());
                        }
                    };
                    composer2.updateRememberedValue(obj);
                    objRememberedValue5 = obj;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                SwitchKt.Switch(allowFallback, (Function1) objRememberedValue5, null, null, false, null, null, composer2, 48, 124);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer2.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                BatteryOptimizationRow(composer2, 0);
                TextKt.m3142Text4IGK_g("OpenRouter", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleLarge(), composer2, 6, 0, 65534);
                TextKt.m3142Text4IGK_g("Endpoint: https://openrouter.ai/api/v1\nPaste an OpenRouter key (sk-or-…). Model ids like openai/whisper-1 are OpenRouter slugs, not the OpenAI API.\nTap Save after changing key/models/routing — reprocess uses the saved settings.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
                String cloudApiKey = PipelineSettingsScreen$lambda$2(mutableState2).getCloudApiKey();
                Modifier modifierFillMaxWidth$default2 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 204503172, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue6 = composer2.rememberedValue();
                if (objRememberedValue6 == Composer.INSTANCE.getEmpty()) {
                    Object obj2 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda17
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj3) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$35$lambda$34(mutableState2, (String) obj3);
                        }
                    };
                    composer2.updateRememberedValue(obj2);
                    objRememberedValue6 = obj2;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                OutlinedTextFieldKt.OutlinedTextField(cloudApiKey, (Function1<? super String, Unit>) objRememberedValue6, modifierFillMaxWidth$default2, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8221getLambda$1056086244$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8225getLambda$1868747025$app_debug(), false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 12583296, 0, 8253368);
                if (!PipelineSettingsScreen$lambda$8($loadingModels$delegate) || StringsKt.isBlank(PipelineSettingsScreen$lambda$2(mutableState2).getCloudApiKey())) {
                    z = false;
                } else {
                    z = true;
                }
                Modifier modifierFillMaxWidth$default3 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 204513705, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                zChangedInstance = composer2.changedInstance(coroutineScope) | composer2.changedInstance($modelsClient) | composer2.changedInstance(pipelineConfig);
                objRememberedValue7 = composer2.rememberedValue();
                if (!zChangedInstance || objRememberedValue7 == Composer.INSTANCE.getEmpty()) {
                    i = 48;
                    objRememberedValue7 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda18
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$37$lambda$36(coroutineScope, mutableState2, $status$delegate, pipelineConfig, $loadingModels$delegate, $modelsClient, $sttModels$delegate, $chatModels$delegate);
                        }
                    };
                    mutableState2 = mutableState2;
                    coroutineScope = coroutineScope;
                    pipelineConfig = pipelineConfig;
                    composer2.updateRememberedValue(objRememberedValue7);
                } else {
                    i = 48;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.OutlinedButton((Function0) objRememberedValue7, modifierFillMaxWidth$default3, z, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-1357868348, true, new Function3() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda19
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj3, Object obj4, Object obj5) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$38($loadingModels$delegate, $sttModels$delegate, $chatModels$delegate, (RowScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                    }
                }, composer2, 54), composer2, 805306416, 504);
                TextKt.m3142Text4IGK_g("Speech-to-text model", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                List<OpenRouterModelInfo> listPipelineSettingsScreen$lambda$11 = PipelineSettingsScreen$lambda$11($sttModels$delegate);
                String cloudAsrModel = PipelineSettingsScreen$lambda$2(mutableState2).getCloudAsrModel();
                if (!PipelineSettingsScreen$lambda$11($sttModels$delegate).isEmpty() || PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                    str2 = "Loading models…";
                } else {
                    str2 = "Load models to choose an STT model";
                }
                ComposerKt.sourceInformationMarkerStart(composer2, 204546459, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue8 = composer2.rememberedValue();
                if (objRememberedValue8 == Composer.INSTANCE.getEmpty()) {
                    Object obj3 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda20
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj4) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$40$lambda$39(mutableState2, (OpenRouterModelInfo) obj4);
                        }
                    };
                    composer2.updateRememberedValue(obj3);
                    objRememberedValue8 = obj3;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModelDropdown("STT model", listPipelineSettingsScreen$lambda$11, cloudAsrModel, (Function1) objRememberedValue8, z2, str2, false, null, composer2, 3078, 192);
                zModelSupportsDiarize = DiarizationLabels.INSTANCE.modelSupportsDiarize(PipelineSettingsScreen$lambda$2(mutableState2).getCloudAsrModel());
                Modifier modifierFillMaxWidth$default4 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                Alignment.Vertical centerVertically2 = Alignment.INSTANCE.getCenterVertically();
                Arrangement.Horizontal spaceBetween2 = Arrangement.INSTANCE.getSpaceBetween();
                ComposerKt.sourceInformationMarkerStart(composer2, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
                MeasurePolicy measurePolicyRowMeasurePolicy2 = RowKt.rowMeasurePolicy(spaceBetween2, centerVertically2, composer2, ((438 >> 3) & 14) | ((438 >> 3) & 112));
                ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode3 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
                CompositionLocalMap currentCompositionLocalMap4 = composer2.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier4 = ComposedModifierKt.materializeModifier(composer2, modifierFillMaxWidth$default4);
                constructor3 = ComposeUiNode.INSTANCE.getConstructor();
                int i13 = ((((438 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!(composer2.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer2.startReusableNode();
                if (composer2.getInserting()) {
                    function2 = constructor3;
                    composer2.createNode(function2);
                } else {
                    function2 = constructor3;
                    composer2.useNode();
                }
                composerM4159constructorimpl3 = Updater.m4159constructorimpl(composer2);
                Updater.m4166setimpl(composerM4159constructorimpl3, measurePolicyRowMeasurePolicy2, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl3, currentCompositionLocalMap4, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash4 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl3.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl3.rememberedValue(), Integer.valueOf(iHashCode3))) {
                    composerM4159constructorimpl3.updateRememberedValue(Integer.valueOf(iHashCode3));
                    composerM4159constructorimpl3.apply(Integer.valueOf(iHashCode3), setCompositeKeyHash4);
                }
                Updater.m4166setimpl(composerM4159constructorimpl3, modifierMaterializeModifier4, ComposeUiNode.INSTANCE.getSetModifier());
                int i14 = (i13 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                int i15 = ((438 >> 6) & 112) | 6;
                RowScope rowScope2 = RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart(composer2, 1850670210, "C280@12491L674,294@13306L52,292@13182L243:PipelineSettingsScreen.kt#w5368b");
                Modifier modifierM834paddingqDBjuR0$default2 = PaddingKt.m834paddingqDBjuR0$default(RowScope.weight$default(rowScope2, Modifier.INSTANCE, 1.0f, false, 2, null), 0.0f, 0.0f, Dp.m7582constructorimpl(12), 0.0f, 11, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
                MeasurePolicy measurePolicyColumnMeasurePolicy3 = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), composer2, ((0 >> 3) & 14) | ((0 >> 3) & 112));
                ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode4 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
                CompositionLocalMap currentCompositionLocalMap5 = composer2.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier5 = ComposedModifierKt.materializeModifier(composer2, modifierM834paddingqDBjuR0$default2);
                constructor4 = ComposeUiNode.INSTANCE.getConstructor();
                int i16 = ((((0 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!(composer2.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer2.startReusableNode();
                if (composer2.getInserting()) {
                    function3 = constructor4;
                    composer2.createNode(function3);
                } else {
                    function3 = constructor4;
                    composer2.useNode();
                }
                composerM4159constructorimpl4 = Updater.m4159constructorimpl(composer2);
                Updater.m4166setimpl(composerM4159constructorimpl4, measurePolicyColumnMeasurePolicy3, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl4, currentCompositionLocalMap5, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash5 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl4.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl4.rememberedValue(), Integer.valueOf(iHashCode4))) {
                    composerM4159constructorimpl4.updateRememberedValue(Integer.valueOf(iHashCode4));
                    composerM4159constructorimpl4.apply(Integer.valueOf(iHashCode4), setCompositeKeyHash5);
                }
                Updater.m4166setimpl(composerM4159constructorimpl4, modifierMaterializeModifier5, ComposeUiNode.INSTANCE.getSetModifier());
                int i17 = (i16 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance3 = ColumnScopeInstance.INSTANCE;
                int i18 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, -602084512, "C281@12623L10,281@12573L73,288@13028L10,289@13096L11,282@12667L480:PipelineSettingsScreen.kt#w5368b");
                TextKt.m3142Text4IGK_g("Speaker diarization", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                if (zModelSupportsDiarize) {
                    str3 = "Label speakers with Grok STT. You'll confirm who is You once per clip.";
                } else {
                    str3 = "Requires Grok STT (x-ai/grok-stt-1.0). Whisper has no speaker labels.";
                }
                TextKt.m3142Text4IGK_g(str3, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer2.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                if (PipelineSettingsScreen$lambda$2(mutableState2).getAsrDiarizeEnabled() || !zModelSupportsDiarize) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                ComposerKt.sourceInformationMarkerStart(composer2, -2018485745, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue9 = composer2.rememberedValue();
                if (objRememberedValue9 == Composer.INSTANCE.getEmpty()) {
                    Object obj4 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda21
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj5) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$44$lambda$43$lambda$42(mutableState2, ((Boolean) obj5).booleanValue());
                        }
                    };
                    composer2.updateRememberedValue(obj4);
                    objRememberedValue9 = obj4;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                SwitchKt.Switch(z3, (Function1) objRememberedValue9, null, null, zModelSupportsDiarize, null, null, composer2, 48, 108);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer2.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                TextKt.m3142Text4IGK_g("Cleanup model", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                TextKt.m3142Text4IGK_g("Cheap/fast model for deduping and polishing transcripts.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 6, 0, 65530);
                List<OpenRouterModelInfo> listPipelineSettingsScreen$lambda$14 = PipelineSettingsScreen$lambda$14($chatModels$delegate);
                String cloudCleanupModel = PipelineSettingsScreen$lambda$2(mutableState2).getCloudCleanupModel();
                if (!PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty() || PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                    str4 = "Loading models…";
                } else {
                    str4 = "Load models to choose a cleanup model";
                }
                ComposerKt.sourceInformationMarkerStart(composer2, 204616095, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue10 = composer2.rememberedValue();
                if (objRememberedValue10 == Composer.INSTANCE.getEmpty()) {
                    Object obj5 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda45
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj6) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$46$lambda$45(mutableState2, (OpenRouterModelInfo) obj6);
                        }
                    };
                    composer2.updateRememberedValue(obj5);
                    objRememberedValue10 = obj5;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModelDropdown("Cleanup model", listPipelineSettingsScreen$lambda$14, cloudCleanupModel, (Function1) objRememberedValue10, z4, str4, false, null, composer2, 1575942, 128);
                TextKt.m3142Text4IGK_g("Summary model", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                TextKt.m3142Text4IGK_g("Stronger model for Title/Overview (and future multi-meeting synthesis).", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 6, 0, 65530);
                List<OpenRouterModelInfo> listPipelineSettingsScreen$lambda$15 = PipelineSettingsScreen$lambda$14($chatModels$delegate);
                String cloudSummaryModel = PipelineSettingsScreen$lambda$2(mutableState2).getCloudSummaryModel();
                if (!PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty() || PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                    z5 = false;
                } else {
                    z5 = true;
                }
                if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                    str5 = "Loading models…";
                } else {
                    str5 = "Load models to choose a summary model";
                }
                if ($selectedSummaryMeta == null && $selectedSummaryMeta.getSupportsReasoning()) {
                    z6 = true;
                } else {
                    z6 = false;
                }
                if (z6) {
                    defaultEffort = $selectedSummaryMeta.getDefaultEffort();
                    if (defaultEffort == null) {
                        defaultEffort = "—";
                    }
                    str6 = "Supports reasoning · default effort: " + defaultEffort;
                } else if (PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty()) {
                    str6 = null;
                } else {
                    str6 = "Pick from the OpenRouter catalog";
                }
                ComposerKt.sourceInformationMarkerStart(composer2, 204657134, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue11 = composer2.rememberedValue();
                if (objRememberedValue11 == Composer.INSTANCE.getEmpty()) {
                    Object obj6 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda46
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj7) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$48$lambda$47(mutableState2, (OpenRouterModelInfo) obj7);
                        }
                    };
                    composer2.updateRememberedValue(obj6);
                    objRememberedValue11 = obj6;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ModelDropdown("Summary model", listPipelineSettingsScreen$lambda$15, cloudSummaryModel, (Function1) objRememberedValue11, z5, str5, true, str6, composer2, 1575942, 0);
                TextKt.m3142Text4IGK_g("Reasoning effort (summary)", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
                TextKt.m3142Text4IGK_g("Applied only to summarization. Example: deepseek/deepseek-r1 + High. Reasoning tokens are excluded from the returned text.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
                Modifier modifierHorizontalScroll$default = ScrollKt.horizontalScroll$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), ScrollKt.rememberScrollState(0, composer2, 0, 1), false, null, false, 14, null);
                Arrangement.Horizontal horizontalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(6));
                composer3 = composer2;
                ComposerKt.sourceInformationMarkerStart(composer3, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
                MeasurePolicy measurePolicyRowMeasurePolicy3 = RowKt.rowMeasurePolicy(horizontalM689spacedBy0680j_4, Alignment.INSTANCE.getTop(), composer3, ((i >> 3) & 14) | ((i >> 3) & 112));
                ComposerKt.sourceInformationMarkerStart(composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode5 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer3, 0));
                CompositionLocalMap currentCompositionLocalMap6 = composer3.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier6 = ComposedModifierKt.materializeModifier(composer3, modifierHorizontalScroll$default);
                constructor5 = ComposeUiNode.INSTANCE.getConstructor();
                int i19 = ((((i << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart(composer3, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!(composer3.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                composer3.startReusableNode();
                if (composer3.getInserting()) {
                    function4 = constructor5;
                    composer3.createNode(function4);
                } else {
                    function4 = constructor5;
                    composer3.useNode();
                }
                composerM4159constructorimpl5 = Updater.m4159constructorimpl(composer3);
                Updater.m4166setimpl(composerM4159constructorimpl5, measurePolicyRowMeasurePolicy3, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl5, currentCompositionLocalMap6, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash6 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl5.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl5.rememberedValue(), Integer.valueOf(iHashCode5))) {
                    composerM4159constructorimpl5.updateRememberedValue(Integer.valueOf(iHashCode5));
                    composerM4159constructorimpl5.apply(Integer.valueOf(iHashCode5), setCompositeKeyHash6);
                }
                Updater.m4166setimpl(composerM4159constructorimpl5, modifierMaterializeModifier6, ComposeUiNode.INSTANCE.getSetModifier());
                int i20 = (i19 >> 6) & 14;
                i2 = 0;
                ComposerKt.sourceInformationMarkerStart(composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                RowScopeInstance rowScopeInstance = RowScopeInstance.INSTANCE;
                int i21 = ((i >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart(composer3, -1178242514, "C:PipelineSettingsScreen.kt#w5368b");
                composer3.startReplaceGroup(100539943);
                ComposerKt.sourceInformation(composer3, "*372@17005L115,375@17154L22,370@16860L339");
                list = $effortOptions;
                i3 = 0;
                for (final ReasoningEffort reasoningEffort : list) {
                    Iterable iterable = list;
                    int i22 = i3;
                    int i23 = i2;
                    if (ReasoningEffort.INSTANCE.fromStored(PipelineSettingsScreen$lambda$2(mutableState2).getReasoningEffort()) == reasoningEffort) {
                        z7 = true;
                    } else {
                        z7 = false;
                    }
                    ComposerKt.sourceInformationMarkerStart(composer3, -132857478, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                    zChanged = composer3.changed(reasoningEffort.ordinal());
                    objRememberedValue22 = composer3.rememberedValue();
                    if (!zChanged) {
                        composer4 = composer3;
                        if (objRememberedValue22 == Composer.INSTANCE.getEmpty()) {
                        }
                        ComposerKt.sourceInformationMarkerEnd(composer3);
                        ChipKt.FilterChip(z7, (Function0) objRememberedValue22, ComposableLambdaKt.rememberComposableLambda(1868080404, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda1
                            @Override // kotlin.jvm.functions.Function2
                            public final Object invoke(Object obj7, Object obj8) {
                                return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$51(reasoningEffort, (Composer) obj7, ((Integer) obj8).intValue());
                            }
                        }, composer3, 54), null, false, null, null, null, null, null, null, null, composer3, 384, 0, 4088);
                        list = iterable;
                        i3 = i22;
                        i2 = i23;
                        composer3 = composer4;
                    } else {
                        composer4 = composer3;
                    }
                    objRememberedValue22 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda47
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$50$lambda$49(reasoningEffort, mutableState2);
                        }
                    };
                    composer3.updateRememberedValue(objRememberedValue22);
                    ComposerKt.sourceInformationMarkerEnd(composer3);
                    ChipKt.FilterChip(z7, (Function0) objRememberedValue22, ComposableLambdaKt.rememberComposableLambda(1868080404, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj7, Object obj8) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$51(reasoningEffort, (Composer) obj7, ((Integer) obj8).intValue());
                        }
                    }, composer3, 54), null, false, null, null, null, null, null, null, null, composer3, 384, 0, 4088);
                    list = iterable;
                    i3 = i22;
                    i2 = i23;
                    composer3 = composer4;
                }
                Composer composer5 = composer3;
                composer3.endReplaceGroup();
                ComposerKt.sourceInformationMarkerEnd(composer3);
                ComposerKt.sourceInformationMarkerEnd(composer3);
                composer5.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer5);
                ComposerKt.sourceInformationMarkerEnd(composer5);
                ComposerKt.sourceInformationMarkerEnd(composer5);
                String sttLanguage = PipelineSettingsScreen$lambda$2(mutableState2).getSttLanguage();
                Modifier modifierFillMaxWidth$default5 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 204714948, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue12 = composer2.rememberedValue();
                if (objRememberedValue12 == Composer.INSTANCE.getEmpty()) {
                    Object obj7 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj8) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$55$lambda$54(mutableState2, (String) obj8);
                        }
                    };
                    composer2.updateRememberedValue(obj7);
                    objRememberedValue12 = obj7;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                OutlinedTextFieldKt.OutlinedTextField(sttLanguage, (Function1<? super String, Unit>) objRememberedValue12, modifierFillMaxWidth$default5, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$1306566277$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$2023603608$app_debug(), false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 12583296, 0, 8253368);
                TextKt.m3142Text4IGK_g("Prompts", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleLarge(), composer2, 6, 0, 65534);
                TextKt.m3142Text4IGK_g("Placeholders: {{transcript}}, {{glossary}}, {{speaker_note}}. Meetings run cleanup first, then summarization.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
                glossary = PipelineSettingsScreen$lambda$2(mutableState2).getGlossary();
                if (StringsKt.isBlank(glossary)) {
                    glossary = CleanupPrompts.INSTANCE.getDEFAULT_GLOSSARY();
                }
                String str7 = glossary;
                Modifier modifierM870heightInVpY3zN4$default = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), Dp.m7582constructorimpl(120), 0.0f, 2, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 204741889, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue13 = composer2.rememberedValue();
                if (objRememberedValue13 == Composer.INSTANCE.getEmpty()) {
                    Object obj8 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda3
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj9) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$58$lambda$57(mutableState2, (String) obj9);
                        }
                    };
                    composer2.updateRememberedValue(obj8);
                    objRememberedValue13 = obj8;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                OutlinedTextFieldKt.OutlinedTextField(str7, (Function1<? super String, Unit>) objRememberedValue13, modifierM870heightInVpY3zN4$default, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$821621028$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 4, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 805306368, 0, 7864248);
                ComposerKt.sourceInformationMarkerStart(composer2, 204751009, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue14 = composer2.rememberedValue();
                if (objRememberedValue14 == Composer.INSTANCE.getEmpty()) {
                    Object obj9 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda4
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$60$lambda$59(mutableState2);
                        }
                    };
                    composer2.updateRememberedValue(obj9);
                    objRememberedValue14 = obj9;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.TextButton((Function0) objRememberedValue14, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8227getLambda$2140057447$app_debug(), composer2, 805306374, 510);
                cleanupPrompt = PipelineSettingsScreen$lambda$2(mutableState2).getCleanupPrompt();
                if (StringsKt.isBlank(cleanupPrompt)) {
                    cleanupPrompt = CleanupPrompts.INSTANCE.getDEFAULT_CLEANUP_PROMPT();
                }
                String str8 = cleanupPrompt;
                Modifier modifierM870heightInVpY3zN4$default2 = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), Dp.m7582constructorimpl(160), 0.0f, 2, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 204759750, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue15 = composer2.rememberedValue();
                if (objRememberedValue15 == Composer.INSTANCE.getEmpty()) {
                    Object obj10 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda5
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj11) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$63$lambda$62(mutableState2, (String) obj11);
                        }
                    };
                    composer2.updateRememberedValue(obj10);
                    objRememberedValue15 = obj10;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                OutlinedTextFieldKt.OutlinedTextField(str8, (Function1<? super String, Unit>) objRememberedValue15, modifierM870heightInVpY3zN4$default2, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$336675779$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 6, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 805306368, 0, 7864248);
                ComposerKt.sourceInformationMarkerStart(composer2, 204769574, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue16 = composer2.rememberedValue();
                if (objRememberedValue16 == Composer.INSTANCE.getEmpty()) {
                    Object obj11 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda6
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$65$lambda$64(mutableState2);
                        }
                    };
                    composer2.updateRememberedValue(obj11);
                    objRememberedValue16 = obj11;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.TextButton((Function0) objRememberedValue16, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8220getLambda$1007674174$app_debug(), composer2, 805306374, 510);
                summaryPrompt = PipelineSettingsScreen$lambda$2(mutableState2).getSummaryPrompt();
                if (StringsKt.isBlank(summaryPrompt)) {
                    summaryPrompt = CleanupPrompts.INSTANCE.getDEFAULT_SUMMARY_PROMPT();
                }
                String str9 = summaryPrompt;
                Modifier modifierM870heightInVpY3zN4$default3 = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), Dp.m7582constructorimpl(160), 0.0f, 2, null);
                ComposerKt.sourceInformationMarkerStart(composer2, 204778662, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue17 = composer2.rememberedValue();
                if (objRememberedValue17 == Composer.INSTANCE.getEmpty()) {
                    Object obj12 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda7
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj13) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$68$lambda$67(mutableState2, (String) obj13);
                        }
                    };
                    composer2.updateRememberedValue(obj12);
                    objRememberedValue17 = obj12;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                OutlinedTextFieldKt.OutlinedTextField(str9, (Function1<? super String, Unit>) objRememberedValue17, modifierM870heightInVpY3zN4$default3, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8223getLambda$148269470$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 6, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 805306368, 0, 7864248);
                ComposerKt.sourceInformationMarkerStart(composer2, 204788582, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue18 = composer2.rememberedValue();
                if (objRememberedValue18 == Composer.INSTANCE.getEmpty()) {
                    Object obj13 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda9
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$70$lambda$69(mutableState2);
                        }
                    };
                    composer2.updateRememberedValue(obj13);
                    objRememberedValue18 = obj13;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.TextButton((Function0) objRememberedValue18, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8224getLambda$1492619423$app_debug(), composer2, 805306374, 510);
                ComposerKt.sourceInformationMarkerStart(composer2, 204794826, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                zChangedInstance2 = composer2.changedInstance(pipelineConfig);
                objRememberedValue19 = composer2.rememberedValue();
                if (!zChangedInstance2 || objRememberedValue19 == Composer.INSTANCE.getEmpty()) {
                    mutableState = $status$delegate;
                    Object obj14 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda10
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$72$lambda$71(pipelineConfig, mutableState2, mutableState);
                        }
                    };
                    composer2.updateRememberedValue(obj14);
                    objRememberedValue19 = obj14;
                } else {
                    mutableState = $status$delegate;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.Button((Function0) objRememberedValue19, SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$390900614$app_debug(), composer2, 805306416, 508);
                ComposerKt.sourceInformationMarkerStart(composer2, 204828231, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                zChangedInstance3 = composer2.changedInstance(pipelineConfig) | composer2.changedInstance(coroutineScope) | composer2.changedInstance($app);
                objRememberedValue20 = composer2.rememberedValue();
                if (!zChangedInstance3 || objRememberedValue20 == Composer.INSTANCE.getEmpty()) {
                    final MutableState mutableState3 = mutableState;
                    Object obj15 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda12
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$74$lambda$73(pipelineConfig, coroutineScope, mutableState2, mutableState3, $app);
                        }
                    };
                    composer2.updateRememberedValue(obj15);
                    objRememberedValue20 = obj15;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.Button((Function0) objRememberedValue20, SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$291532911$app_debug(), composer2, 805306416, 508);
                ComposerKt.sourceInformationMarkerStart(composer2, 204862496, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                zChangedInstance4 = composer2.changedInstance(pipelineConfig) | composer2.changedInstance(coroutineScope) | composer2.changedInstance($app);
                objRememberedValue21 = composer2.rememberedValue();
                if (!zChangedInstance4 || objRememberedValue21 == Composer.INSTANCE.getEmpty()) {
                    Object obj16 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda13
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$76$lambda$75(pipelineConfig, coroutineScope, $settings$delegate, $status$delegate, $app);
                        }
                    };
                    composer2.updateRememberedValue(obj16);
                    objRememberedValue21 = obj16;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ButtonKt.Button((Function0) objRememberedValue21, SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8226getLambda$193412338$app_debug(), composer2, 805306416, 508);
                if (PipelineSettingsScreen$lambda$5($status$delegate) != null) {
                    composer2.startReplaceGroup(2056918353);
                    ComposerKt.sourceInformation(composer2, "512@23164L11,512@23127L59");
                    String strPipelineSettingsScreen$lambda$5 = PipelineSettingsScreen$lambda$5($status$delegate);
                    Intrinsics.checkNotNull(strPipelineSettingsScreen$lambda$5);
                    TextKt.m3142Text4IGK_g(strPipelineSettingsScreen$lambda$5, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getSecondary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, composer2, 0, 0, 131066);
                } else {
                    composer2.startReplaceGroup(2033990412);
                }
                composer2.endReplaceGroup();
                SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer2, 6);
                TextKt.m3142Text4IGK_g("Sideload via scripts/push-local-models.sh:\nfiles/models/parakeet/{model_npu.tflite,model.tflite,tokenizer.json}\nfiles/models/gemma4b/gemma-4-E2B-it_Google_Tensor_G5.litertlm", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodyMedium(), composer2, 0, 0, 65530);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd(composer);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
            }
            composerM4159constructorimpl6.updateRememberedValue(Integer.valueOf(iHashCode6));
            composerM4159constructorimpl6.apply(Integer.valueOf(iHashCode6), setCompositeKeyHash);
            Updater.m4166setimpl(composerM4159constructorimpl6, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i24 = (i4 >> 6) & 14;
            composer2 = composer;
            ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance4 = ColumnScopeInstance.INSTANCE;
            int i25 = ((48 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, 2041934907, "C181@7827L10,182@7888L11,177@7503L428,185@7983L10,185@7945L60,188@8159L10,189@8219L11,186@8018L244,191@8319L10,191@8275L67,192@8381L42,192@8355L68,194@8475L10,194@8437L61,195@8541L46,195@8511L76,197@8645L10,197@8601L67,198@8711L46,198@8681L76,200@8809L10,200@8771L61,201@8875L46,201@8845L76,203@8935L1072,226@10021L24,228@10100L10,228@10059L63,234@10511L10,235@10571L11,229@10135L479,239@10724L46,237@10627L371,247@11054L19,250@11220L331,246@11012L539,260@11616L10,260@11565L74,271@12076L69,261@11652L508,275@12272L1167,299@13497L10,299@13453L67,302@13653L10,303@13713L11,300@13533L223,316@14252L73,305@13769L571,319@14398L10,319@14354L67,322@14569L10,323@14629L11,320@14434L238,342@15518L600,325@14685L1448,356@16204L10,356@16147L80,360@16451L10,361@16511L11,357@16240L314,366@16682L21,363@16567L664,382@17342L46,380@17245L399,389@17696L10,389@17658L60,393@17929L10,394@17989L11,390@17731L301,399@18184L43,397@18046L389,406@18469L43,406@18448L131,412@18742L48,410@18593L422,419@19049L48,419@19028L142,425@19333L48,423@19184L425,432@19643L48,432@19622L142,437@19812L884,436@19778L1030,463@20856L881,462@20822L1053,487@21923L1002,486@21889L1186,515@23214L29,520@23545L10,521@23606L11,516@23256L393:PipelineSettingsScreen.kt#w5368b");
            TextKt.m3142Text4IGK_g("Cloud (OpenRouter): silence-trim only (no speedup), ~10 min capture rolls when cloud ASR is preferred, and long files are chunked at ~10 min. Local Parakeet still uses trim + 1.35×. Models load from OpenRouter.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodyMedium(), composer2, 0, 0, 65530);
            TextKt.m3142Text4IGK_g("Routing", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleLarge(), composer2, 6, 0, 65534);
            TextKt.m3142Text4IGK_g("Pick Cloud or Local per stage. Fallback (below) is one switch for all stages.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 6, 0, 65530);
            TextKt.m3142Text4IGK_g("Transcription", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            ProviderMode asrMode2 = PipelineSettingsScreen$lambda$2(mutableState2).getAsrMode();
            ComposerKt.sourceInformationMarkerStart(composer2, 204428192, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue = composer2.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                objRememberedValue = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda44
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj17) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$23$lambda$22(mutableState2, (ProviderMode) obj17);
                    }
                };
                composer2.updateRememberedValue(objRememberedValue);
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModeRow(asrMode2, (Function1) objRememberedValue, composer2, 48);
            TextKt.m3142Text4IGK_g("Cleanup", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            ProviderMode cleanupMode2 = PipelineSettingsScreen$lambda$2(mutableState2).getCleanupMode();
            ComposerKt.sourceInformationMarkerStart(composer2, 204433316, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue2 = composer2.rememberedValue();
            if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                objRememberedValue2 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda8
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj17) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$25$lambda$24(mutableState2, (ProviderMode) obj17);
                    }
                };
                composer2.updateRememberedValue(objRememberedValue2);
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModeRow(cleanupMode2, (Function1) objRememberedValue2, composer2, 48);
            TextKt.m3142Text4IGK_g("Summarization", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            ProviderMode summaryMode2 = PipelineSettingsScreen$lambda$2(mutableState2).getSummaryMode();
            ComposerKt.sourceInformationMarkerStart(composer2, 204438756, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue3 = composer2.rememberedValue();
            if (objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                objRememberedValue3 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda14
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj17) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$27$lambda$26(mutableState2, (ProviderMode) obj17);
                    }
                };
                composer2.updateRememberedValue(objRememberedValue3);
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModeRow(summaryMode2, (Function1) objRememberedValue3, composer2, 48);
            TextKt.m3142Text4IGK_g("Actions", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            ProviderMode actionsMode2 = PipelineSettingsScreen$lambda$2(mutableState2).getActionsMode();
            ComposerKt.sourceInformationMarkerStart(composer2, 204444004, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue4 = composer2.rememberedValue();
            if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                objRememberedValue4 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda15
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj17) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$29$lambda$28(mutableState2, (ProviderMode) obj17);
                    }
                };
                composer2.updateRememberedValue(objRememberedValue4);
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModeRow(actionsMode2, (Function1) objRememberedValue4, composer2, 48);
            Modifier modifierFillMaxWidth$default6 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            Alignment.Vertical centerVertically3 = Alignment.INSTANCE.getCenterVertically();
            Arrangement.Horizontal spaceBetween3 = Arrangement.INSTANCE.getSpaceBetween();
            ComposerKt.sourceInformationMarkerStart(composer2, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            MeasurePolicy measurePolicyRowMeasurePolicy4 = RowKt.rowMeasurePolicy(spaceBetween3, centerVertically3, composer2, ((438 >> 3) & 14) | ((438 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
            CompositionLocalMap currentCompositionLocalMap7 = composer2.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier7 = ComposedModifierKt.materializeModifier(composer2, modifierFillMaxWidth$default6);
            constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i26 = ((((438 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer2.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer2.startReusableNode();
            if (composer2.getInserting()) {
                function0 = constructor;
                composer2.createNode(function0);
            } else {
                function0 = constructor;
                composer2.useNode();
            }
            composerM4159constructorimpl = Updater.m4159constructorimpl(composer2);
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyRowMeasurePolicy4, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap7, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash7 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl.getInserting()) {
            }
            composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
            composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash7);
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier7, ComposeUiNode.INSTANCE.getSetModifier());
            int i27 = (i26 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            int i28 = ((438 >> 6) & 112) | 6;
            RowScope rowScope3 = RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart(composer2, -1065560182, "C208@9154L655,222@9926L48,220@9826L167:PipelineSettingsScreen.kt#w5368b");
            Modifier modifierM834paddingqDBjuR0$default3 = PaddingKt.m834paddingqDBjuR0$default(RowScope.weight$default(rowScope3, Modifier.INSTANCE, 1.0f, false, 2, null), 0.0f, 0.0f, Dp.m7582constructorimpl(12), 0.0f, 11, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy4 = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), composer2, ((0 >> 3) & 14) | ((0 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
            CompositionLocalMap currentCompositionLocalMap8 = composer2.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier8 = ComposedModifierKt.materializeModifier(composer2, modifierM834paddingqDBjuR0$default3);
            constructor2 = ComposeUiNode.INSTANCE.getConstructor();
            int i110 = ((((0 << 3) & 112) << 6) & 896) | 6;
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
            composerM4159constructorimpl2 = Updater.m4159constructorimpl(composer2);
            Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyColumnMeasurePolicy4, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap8, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash8 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl2.getInserting()) {
            }
            composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
            composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash8);
            Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier8, ComposeUiNode.INSTANCE.getSetModifier());
            int i111 = (i110 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance5 = ColumnScopeInstance.INSTANCE;
            int i112 = ((0 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -719514340, "C209@9281L10,209@9236L68,216@9672L10,217@9740L11,210@9325L466:PipelineSettingsScreen.kt#w5368b");
            TextKt.m3142Text4IGK_g("Allow fallback", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            if (PipelineSettingsScreen$lambda$2(mutableState2).getAllowFallback()) {
                str = "If the primary side fails or is unavailable, try the other.";
            } else {
                str = "Only the selected Cloud/Local side runs — no cross-fallback.";
            }
            TextKt.m3142Text4IGK_g(str, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer2.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            boolean allowFallback2 = PipelineSettingsScreen$lambda$2(mutableState2).getAllowFallback();
            ComposerKt.sourceInformationMarkerStart(composer2, -1974011646, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue5 = composer2.rememberedValue();
            if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                Object obj17 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda16
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj18) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$33$lambda$32$lambda$31(mutableState2, ((Boolean) obj18).booleanValue());
                    }
                };
                composer2.updateRememberedValue(obj17);
                objRememberedValue5 = obj17;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            SwitchKt.Switch(allowFallback2, (Function1) objRememberedValue5, null, null, false, null, null, composer2, 48, 124);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer2.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            BatteryOptimizationRow(composer2, 0);
            TextKt.m3142Text4IGK_g("OpenRouter", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleLarge(), composer2, 6, 0, 65534);
            TextKt.m3142Text4IGK_g("Endpoint: https://openrouter.ai/api/v1\nPaste an OpenRouter key (sk-or-…). Model ids like openai/whisper-1 are OpenRouter slugs, not the OpenAI API.\nTap Save after changing key/models/routing — reprocess uses the saved settings.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
            String cloudApiKey2 = PipelineSettingsScreen$lambda$2(mutableState2).getCloudApiKey();
            Modifier modifierFillMaxWidth$default7 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 204503172, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue6 = composer2.rememberedValue();
            if (objRememberedValue6 == Composer.INSTANCE.getEmpty()) {
                Object obj18 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda17
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj19) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$35$lambda$34(mutableState2, (String) obj19);
                    }
                };
                composer2.updateRememberedValue(obj18);
                objRememberedValue6 = obj18;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            OutlinedTextFieldKt.OutlinedTextField(cloudApiKey2, (Function1<? super String, Unit>) objRememberedValue6, modifierFillMaxWidth$default7, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8221getLambda$1056086244$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8225getLambda$1868747025$app_debug(), false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 12583296, 0, 8253368);
            if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                z = false;
            } else {
                z = false;
            }
            Modifier modifierFillMaxWidth$default8 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 204513705, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            zChangedInstance = composer2.changedInstance(coroutineScope) | composer2.changedInstance($modelsClient) | composer2.changedInstance(pipelineConfig);
            objRememberedValue7 = composer2.rememberedValue();
            if (zChangedInstance) {
                i = 48;
                objRememberedValue7 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda18
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$37$lambda$36(coroutineScope, mutableState2, $status$delegate, pipelineConfig, $loadingModels$delegate, $modelsClient, $sttModels$delegate, $chatModels$delegate);
                    }
                };
                mutableState2 = mutableState2;
                coroutineScope = coroutineScope;
                pipelineConfig = pipelineConfig;
                composer2.updateRememberedValue(objRememberedValue7);
            } else {
                i = 48;
                objRememberedValue7 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda18
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$37$lambda$36(coroutineScope, mutableState2, $status$delegate, pipelineConfig, $loadingModels$delegate, $modelsClient, $sttModels$delegate, $chatModels$delegate);
                    }
                };
                mutableState2 = mutableState2;
                coroutineScope = coroutineScope;
                pipelineConfig = pipelineConfig;
                composer2.updateRememberedValue(objRememberedValue7);
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.OutlinedButton((Function0) objRememberedValue7, modifierFillMaxWidth$default8, z, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-1357868348, true, new Function3() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda19
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj19, Object obj20, Object obj21) {
                    return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$38($loadingModels$delegate, $sttModels$delegate, $chatModels$delegate, (RowScope) obj19, (Composer) obj20, ((Integer) obj21).intValue());
                }
            }, composer2, 54), composer2, 805306416, 504);
            TextKt.m3142Text4IGK_g("Speech-to-text model", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            List<OpenRouterModelInfo> listPipelineSettingsScreen$lambda$12 = PipelineSettingsScreen$lambda$11($sttModels$delegate);
            String cloudAsrModel2 = PipelineSettingsScreen$lambda$2(mutableState2).getCloudAsrModel();
            if (PipelineSettingsScreen$lambda$11($sttModels$delegate).isEmpty()) {
                z2 = false;
            } else {
                z2 = false;
            }
            if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                str2 = "Loading models…";
            } else {
                str2 = "Load models to choose an STT model";
            }
            ComposerKt.sourceInformationMarkerStart(composer2, 204546459, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue8 = composer2.rememberedValue();
            if (objRememberedValue8 == Composer.INSTANCE.getEmpty()) {
                Object obj19 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda20
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj20) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$40$lambda$39(mutableState2, (OpenRouterModelInfo) obj20);
                    }
                };
                composer2.updateRememberedValue(obj19);
                objRememberedValue8 = obj19;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModelDropdown("STT model", listPipelineSettingsScreen$lambda$12, cloudAsrModel2, (Function1) objRememberedValue8, z2, str2, false, null, composer2, 3078, 192);
            zModelSupportsDiarize = DiarizationLabels.INSTANCE.modelSupportsDiarize(PipelineSettingsScreen$lambda$2(mutableState2).getCloudAsrModel());
            Modifier modifierFillMaxWidth$default9 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            Alignment.Vertical centerVertically4 = Alignment.INSTANCE.getCenterVertically();
            Arrangement.Horizontal spaceBetween4 = Arrangement.INSTANCE.getSpaceBetween();
            ComposerKt.sourceInformationMarkerStart(composer2, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            MeasurePolicy measurePolicyRowMeasurePolicy5 = RowKt.rowMeasurePolicy(spaceBetween4, centerVertically4, composer2, ((438 >> 3) & 14) | ((438 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            iHashCode3 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
            CompositionLocalMap currentCompositionLocalMap9 = composer2.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier9 = ComposedModifierKt.materializeModifier(composer2, modifierFillMaxWidth$default9);
            constructor3 = ComposeUiNode.INSTANCE.getConstructor();
            int i113 = ((((438 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer2.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer2.startReusableNode();
            if (composer2.getInserting()) {
                function2 = constructor3;
                composer2.createNode(function2);
            } else {
                function2 = constructor3;
                composer2.useNode();
            }
            composerM4159constructorimpl3 = Updater.m4159constructorimpl(composer2);
            Updater.m4166setimpl(composerM4159constructorimpl3, measurePolicyRowMeasurePolicy5, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl3, currentCompositionLocalMap9, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash9 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl3.getInserting()) {
            }
            composerM4159constructorimpl3.updateRememberedValue(Integer.valueOf(iHashCode3));
            composerM4159constructorimpl3.apply(Integer.valueOf(iHashCode3), setCompositeKeyHash9);
            Updater.m4166setimpl(composerM4159constructorimpl3, modifierMaterializeModifier9, ComposeUiNode.INSTANCE.getSetModifier());
            int i114 = (i113 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            int i115 = ((438 >> 6) & 112) | 6;
            RowScope rowScope4 = RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart(composer2, 1850670210, "C280@12491L674,294@13306L52,292@13182L243:PipelineSettingsScreen.kt#w5368b");
            Modifier modifierM834paddingqDBjuR0$default4 = PaddingKt.m834paddingqDBjuR0$default(RowScope.weight$default(rowScope4, Modifier.INSTANCE, 1.0f, false, 2, null), 0.0f, 0.0f, Dp.m7582constructorimpl(12), 0.0f, 11, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy5 = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), composer2, ((0 >> 3) & 14) | ((0 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer2, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            iHashCode4 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer2, 0));
            CompositionLocalMap currentCompositionLocalMap10 = composer2.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier10 = ComposedModifierKt.materializeModifier(composer2, modifierM834paddingqDBjuR0$default4);
            constructor4 = ComposeUiNode.INSTANCE.getConstructor();
            int i116 = ((((0 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer2.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer2.startReusableNode();
            if (composer2.getInserting()) {
                function3 = constructor4;
                composer2.createNode(function3);
            } else {
                function3 = constructor4;
                composer2.useNode();
            }
            composerM4159constructorimpl4 = Updater.m4159constructorimpl(composer2);
            Updater.m4166setimpl(composerM4159constructorimpl4, measurePolicyColumnMeasurePolicy5, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl4, currentCompositionLocalMap10, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash10 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl4.getInserting()) {
            }
            composerM4159constructorimpl4.updateRememberedValue(Integer.valueOf(iHashCode4));
            composerM4159constructorimpl4.apply(Integer.valueOf(iHashCode4), setCompositeKeyHash10);
            Updater.m4166setimpl(composerM4159constructorimpl4, modifierMaterializeModifier10, ComposeUiNode.INSTANCE.getSetModifier());
            int i117 = (i116 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance6 = ColumnScopeInstance.INSTANCE;
            int i118 = ((0 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart(composer2, -602084512, "C281@12623L10,281@12573L73,288@13028L10,289@13096L11,282@12667L480:PipelineSettingsScreen.kt#w5368b");
            TextKt.m3142Text4IGK_g("Speaker diarization", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            if (zModelSupportsDiarize) {
                str3 = "Label speakers with Grok STT. You'll confirm who is You once per clip.";
            } else {
                str3 = "Requires Grok STT (x-ai/grok-stt-1.0). Whisper has no speaker labels.";
            }
            TextKt.m3142Text4IGK_g(str3, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer2.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            if (PipelineSettingsScreen$lambda$2(mutableState2).getAsrDiarizeEnabled()) {
                z3 = false;
            } else {
                z3 = false;
            }
            ComposerKt.sourceInformationMarkerStart(composer2, -2018485745, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue9 = composer2.rememberedValue();
            if (objRememberedValue9 == Composer.INSTANCE.getEmpty()) {
                Object obj20 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda21
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj21) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$44$lambda$43$lambda$42(mutableState2, ((Boolean) obj21).booleanValue());
                    }
                };
                composer2.updateRememberedValue(obj20);
                objRememberedValue9 = obj20;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            SwitchKt.Switch(z3, (Function1) objRememberedValue9, null, null, zModelSupportsDiarize, null, null, composer2, 48, 108);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer2.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            TextKt.m3142Text4IGK_g("Cleanup model", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            TextKt.m3142Text4IGK_g("Cheap/fast model for deduping and polishing transcripts.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 6, 0, 65530);
            List<OpenRouterModelInfo> listPipelineSettingsScreen$lambda$16 = PipelineSettingsScreen$lambda$14($chatModels$delegate);
            String cloudCleanupModel2 = PipelineSettingsScreen$lambda$2(mutableState2).getCloudCleanupModel();
            if (PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty()) {
                z4 = false;
            } else {
                z4 = false;
            }
            if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                str4 = "Loading models…";
            } else {
                str4 = "Load models to choose a cleanup model";
            }
            ComposerKt.sourceInformationMarkerStart(composer2, 204616095, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue10 = composer2.rememberedValue();
            if (objRememberedValue10 == Composer.INSTANCE.getEmpty()) {
                Object obj21 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda45
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj22) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$46$lambda$45(mutableState2, (OpenRouterModelInfo) obj22);
                    }
                };
                composer2.updateRememberedValue(obj21);
                objRememberedValue10 = obj21;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModelDropdown("Cleanup model", listPipelineSettingsScreen$lambda$16, cloudCleanupModel2, (Function1) objRememberedValue10, z4, str4, false, null, composer2, 1575942, 128);
            TextKt.m3142Text4IGK_g("Summary model", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            TextKt.m3142Text4IGK_g("Stronger model for Title/Overview (and future multi-meeting synthesis).", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 6, 0, 65530);
            List<OpenRouterModelInfo> listPipelineSettingsScreen$lambda$17 = PipelineSettingsScreen$lambda$14($chatModels$delegate);
            String cloudSummaryModel2 = PipelineSettingsScreen$lambda$2(mutableState2).getCloudSummaryModel();
            if (PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty()) {
                z5 = false;
            } else {
                z5 = false;
            }
            if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                str5 = "Loading models…";
            } else {
                str5 = "Load models to choose a summary model";
            }
            if ($selectedSummaryMeta == null) {
                z6 = false;
            } else {
                z6 = false;
            }
            if (z6) {
                defaultEffort = $selectedSummaryMeta.getDefaultEffort();
                if (defaultEffort == null) {
                    defaultEffort = "—";
                }
                str6 = "Supports reasoning · default effort: " + defaultEffort;
            } else if (PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty()) {
                str6 = "Pick from the OpenRouter catalog";
            } else {
                str6 = null;
            }
            ComposerKt.sourceInformationMarkerStart(composer2, 204657134, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue11 = composer2.rememberedValue();
            if (objRememberedValue11 == Composer.INSTANCE.getEmpty()) {
                Object obj22 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda46
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj23) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$48$lambda$47(mutableState2, (OpenRouterModelInfo) obj23);
                    }
                };
                composer2.updateRememberedValue(obj22);
                objRememberedValue11 = obj22;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ModelDropdown("Summary model", listPipelineSettingsScreen$lambda$17, cloudSummaryModel2, (Function1) objRememberedValue11, z5, str5, true, str6, composer2, 1575942, 0);
            TextKt.m3142Text4IGK_g("Reasoning effort (summary)", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleMedium(), composer2, 6, 0, 65534);
            TextKt.m3142Text4IGK_g("Applied only to summarization. Example: deepseek/deepseek-r1 + High. Reasoning tokens are excluded from the returned text.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
            Modifier modifierHorizontalScroll$default2 = ScrollKt.horizontalScroll$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), ScrollKt.rememberScrollState(0, composer2, 0, 1), false, null, false, 14, null);
            Arrangement.Horizontal horizontalM689spacedBy0680j_5 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(6));
            composer3 = composer2;
            ComposerKt.sourceInformationMarkerStart(composer3, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            MeasurePolicy measurePolicyRowMeasurePolicy6 = RowKt.rowMeasurePolicy(horizontalM689spacedBy0680j_5, Alignment.INSTANCE.getTop(), composer3, ((i >> 3) & 14) | ((i >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            iHashCode5 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer3, 0));
            CompositionLocalMap currentCompositionLocalMap11 = composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier11 = ComposedModifierKt.materializeModifier(composer3, modifierHorizontalScroll$default2);
            constructor5 = ComposeUiNode.INSTANCE.getConstructor();
            int i119 = ((((i << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer3, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer3.startReusableNode();
            if (composer3.getInserting()) {
                function4 = constructor5;
                composer3.createNode(function4);
            } else {
                function4 = constructor5;
                composer3.useNode();
            }
            composerM4159constructorimpl5 = Updater.m4159constructorimpl(composer3);
            Updater.m4166setimpl(composerM4159constructorimpl5, measurePolicyRowMeasurePolicy6, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl5, currentCompositionLocalMap11, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash11 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl5.getInserting()) {
            }
            composerM4159constructorimpl5.updateRememberedValue(Integer.valueOf(iHashCode5));
            composerM4159constructorimpl5.apply(Integer.valueOf(iHashCode5), setCompositeKeyHash11);
            Updater.m4166setimpl(composerM4159constructorimpl5, modifierMaterializeModifier11, ComposeUiNode.INSTANCE.getSetModifier());
            int i29 = (i119 >> 6) & 14;
            i2 = 0;
            ComposerKt.sourceInformationMarkerStart(composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            RowScopeInstance rowScopeInstance2 = RowScopeInstance.INSTANCE;
            int i210 = ((i >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart(composer3, -1178242514, "C:PipelineSettingsScreen.kt#w5368b");
            composer3.startReplaceGroup(100539943);
            ComposerKt.sourceInformation(composer3, "*372@17005L115,375@17154L22,370@16860L339");
            list = $effortOptions;
            i3 = 0;
            while (r67.hasNext()) {
                Iterable iterable2 = list;
                int i211 = i3;
                int i212 = i2;
                if (ReasoningEffort.INSTANCE.fromStored(PipelineSettingsScreen$lambda$2(mutableState2).getReasoningEffort()) == reasoningEffort) {
                    z7 = true;
                } else {
                    z7 = false;
                }
                ComposerKt.sourceInformationMarkerStart(composer3, -132857478, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                zChanged = composer3.changed(reasoningEffort.ordinal());
                objRememberedValue22 = composer3.rememberedValue();
                if (!zChanged) {
                    composer4 = composer3;
                    if (objRememberedValue22 == Composer.INSTANCE.getEmpty()) {
                    }
                    ComposerKt.sourceInformationMarkerEnd(composer3);
                    ChipKt.FilterChip(z7, (Function0) objRememberedValue22, ComposableLambdaKt.rememberComposableLambda(1868080404, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj23, Object obj24) {
                            return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$51(reasoningEffort, (Composer) obj23, ((Integer) obj24).intValue());
                        }
                    }, composer3, 54), null, false, null, null, null, null, null, null, null, composer3, 384, 0, 4088);
                    list = iterable2;
                    i3 = i211;
                    i2 = i212;
                    composer3 = composer4;
                } else {
                    composer4 = composer3;
                }
                objRememberedValue22 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda47
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$50$lambda$49(reasoningEffort, mutableState2);
                    }
                };
                composer3.updateRememberedValue(objRememberedValue22);
                ComposerKt.sourceInformationMarkerEnd(composer3);
                ChipKt.FilterChip(z7, (Function0) objRememberedValue22, ComposableLambdaKt.rememberComposableLambda(1868080404, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj23, Object obj24) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$51(reasoningEffort, (Composer) obj23, ((Integer) obj24).intValue());
                    }
                }, composer3, 54), null, false, null, null, null, null, null, null, null, composer3, 384, 0, 4088);
                list = iterable2;
                i3 = i211;
                i2 = i212;
                composer3 = composer4;
            }
            Composer composer6 = composer3;
            composer3.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd(composer3);
            ComposerKt.sourceInformationMarkerEnd(composer3);
            composer6.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer6);
            ComposerKt.sourceInformationMarkerEnd(composer6);
            ComposerKt.sourceInformationMarkerEnd(composer6);
            String sttLanguage2 = PipelineSettingsScreen$lambda$2(mutableState2).getSttLanguage();
            Modifier modifierFillMaxWidth$default10 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 204714948, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue12 = composer2.rememberedValue();
            if (objRememberedValue12 == Composer.INSTANCE.getEmpty()) {
                Object obj23 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj24) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$55$lambda$54(mutableState2, (String) obj24);
                    }
                };
                composer2.updateRememberedValue(obj23);
                objRememberedValue12 = obj23;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            OutlinedTextFieldKt.OutlinedTextField(sttLanguage2, (Function1<? super String, Unit>) objRememberedValue12, modifierFillMaxWidth$default10, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$1306566277$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$2023603608$app_debug(), false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 12583296, 0, 8253368);
            TextKt.m3142Text4IGK_g("Prompts", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleLarge(), composer2, 6, 0, 65534);
            TextKt.m3142Text4IGK_g("Placeholders: {{transcript}}, {{glossary}}, {{speaker_note}}. Meetings run cleanup first, then summarization.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
            glossary = PipelineSettingsScreen$lambda$2(mutableState2).getGlossary();
            if (StringsKt.isBlank(glossary)) {
                glossary = CleanupPrompts.INSTANCE.getDEFAULT_GLOSSARY();
            }
            String str10 = glossary;
            Modifier modifierM870heightInVpY3zN4$default4 = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), Dp.m7582constructorimpl(120), 0.0f, 2, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 204741889, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue13 = composer2.rememberedValue();
            if (objRememberedValue13 == Composer.INSTANCE.getEmpty()) {
                Object obj24 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj25) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$58$lambda$57(mutableState2, (String) obj25);
                    }
                };
                composer2.updateRememberedValue(obj24);
                objRememberedValue13 = obj24;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            OutlinedTextFieldKt.OutlinedTextField(str10, (Function1<? super String, Unit>) objRememberedValue13, modifierM870heightInVpY3zN4$default4, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$821621028$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 4, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 805306368, 0, 7864248);
            ComposerKt.sourceInformationMarkerStart(composer2, 204751009, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue14 = composer2.rememberedValue();
            if (objRememberedValue14 == Composer.INSTANCE.getEmpty()) {
                Object obj25 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$60$lambda$59(mutableState2);
                    }
                };
                composer2.updateRememberedValue(obj25);
                objRememberedValue14 = obj25;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.TextButton((Function0) objRememberedValue14, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8227getLambda$2140057447$app_debug(), composer2, 805306374, 510);
            cleanupPrompt = PipelineSettingsScreen$lambda$2(mutableState2).getCleanupPrompt();
            if (StringsKt.isBlank(cleanupPrompt)) {
                cleanupPrompt = CleanupPrompts.INSTANCE.getDEFAULT_CLEANUP_PROMPT();
            }
            String str11 = cleanupPrompt;
            Modifier modifierM870heightInVpY3zN4$default5 = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), Dp.m7582constructorimpl(160), 0.0f, 2, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 204759750, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue15 = composer2.rememberedValue();
            if (objRememberedValue15 == Composer.INSTANCE.getEmpty()) {
                Object obj110 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda5
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj111) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$63$lambda$62(mutableState2, (String) obj111);
                    }
                };
                composer2.updateRememberedValue(obj110);
                objRememberedValue15 = obj110;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            OutlinedTextFieldKt.OutlinedTextField(str11, (Function1<? super String, Unit>) objRememberedValue15, modifierM870heightInVpY3zN4$default5, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$336675779$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 6, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 805306368, 0, 7864248);
            ComposerKt.sourceInformationMarkerStart(composer2, 204769574, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue16 = composer2.rememberedValue();
            if (objRememberedValue16 == Composer.INSTANCE.getEmpty()) {
                Object obj111 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$65$lambda$64(mutableState2);
                    }
                };
                composer2.updateRememberedValue(obj111);
                objRememberedValue16 = obj111;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.TextButton((Function0) objRememberedValue16, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8220getLambda$1007674174$app_debug(), composer2, 805306374, 510);
            summaryPrompt = PipelineSettingsScreen$lambda$2(mutableState2).getSummaryPrompt();
            if (StringsKt.isBlank(summaryPrompt)) {
                summaryPrompt = CleanupPrompts.INSTANCE.getDEFAULT_SUMMARY_PROMPT();
            }
            String str12 = summaryPrompt;
            Modifier modifierM870heightInVpY3zN4$default6 = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), Dp.m7582constructorimpl(160), 0.0f, 2, null);
            ComposerKt.sourceInformationMarkerStart(composer2, 204778662, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue17 = composer2.rememberedValue();
            if (objRememberedValue17 == Composer.INSTANCE.getEmpty()) {
                Object obj112 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj113) {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$68$lambda$67(mutableState2, (String) obj113);
                    }
                };
                composer2.updateRememberedValue(obj112);
                objRememberedValue17 = obj112;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            OutlinedTextFieldKt.OutlinedTextField(str12, (Function1<? super String, Unit>) objRememberedValue17, modifierM870heightInVpY3zN4$default6, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8223getLambda$148269470$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 6, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, composer2, 1573296, 805306368, 0, 7864248);
            ComposerKt.sourceInformationMarkerStart(composer2, 204788582, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue18 = composer2.rememberedValue();
            if (objRememberedValue18 == Composer.INSTANCE.getEmpty()) {
                Object obj113 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda9
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$70$lambda$69(mutableState2);
                    }
                };
                composer2.updateRememberedValue(obj113);
                objRememberedValue18 = obj113;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.TextButton((Function0) objRememberedValue18, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8224getLambda$1492619423$app_debug(), composer2, 805306374, 510);
            ComposerKt.sourceInformationMarkerStart(composer2, 204794826, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            zChangedInstance2 = composer2.changedInstance(pipelineConfig);
            objRememberedValue19 = composer2.rememberedValue();
            if (!zChangedInstance2) {
                mutableState = $status$delegate;
                Object obj114 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda10
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$72$lambda$71(pipelineConfig, mutableState2, mutableState);
                    }
                };
                composer2.updateRememberedValue(obj114);
                objRememberedValue19 = obj114;
            } else {
                mutableState = $status$delegate;
                Object obj115 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda10
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$72$lambda$71(pipelineConfig, mutableState2, mutableState);
                    }
                };
                composer2.updateRememberedValue(obj115);
                objRememberedValue19 = obj115;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.Button((Function0) objRememberedValue19, SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$390900614$app_debug(), composer2, 805306416, 508);
            ComposerKt.sourceInformationMarkerStart(composer2, 204828231, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            zChangedInstance3 = composer2.changedInstance(pipelineConfig) | composer2.changedInstance(coroutineScope) | composer2.changedInstance($app);
            objRememberedValue20 = composer2.rememberedValue();
            if (!zChangedInstance3) {
                final MutableState mutableState4 = mutableState;
                Object obj116 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda12
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$74$lambda$73(pipelineConfig, coroutineScope, mutableState2, mutableState4, $app);
                    }
                };
                composer2.updateRememberedValue(obj116);
                objRememberedValue20 = obj116;
            } else {
                final MutableState mutableState5 = mutableState;
                Object obj117 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda12
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$74$lambda$73(pipelineConfig, coroutineScope, mutableState2, mutableState5, $app);
                    }
                };
                composer2.updateRememberedValue(obj117);
                objRememberedValue20 = obj117;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.Button((Function0) objRememberedValue20, SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$291532911$app_debug(), composer2, 805306416, 508);
            ComposerKt.sourceInformationMarkerStart(composer2, 204862496, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            zChangedInstance4 = composer2.changedInstance(pipelineConfig) | composer2.changedInstance(coroutineScope) | composer2.changedInstance($app);
            objRememberedValue21 = composer2.rememberedValue();
            if (!zChangedInstance4) {
                Object obj118 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda13
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$76$lambda$75(pipelineConfig, coroutineScope, $settings$delegate, $status$delegate, $app);
                    }
                };
                composer2.updateRememberedValue(obj118);
                objRememberedValue21 = obj118;
            } else {
                Object obj119 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda13
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return PipelineSettingsScreenKt.PipelineSettingsScreen$lambda$78$lambda$77$lambda$76$lambda$75(pipelineConfig, coroutineScope, $settings$delegate, $status$delegate, $app);
                    }
                };
                composer2.updateRememberedValue(obj119);
                objRememberedValue21 = obj119;
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ButtonKt.Button((Function0) objRememberedValue21, SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8226getLambda$193412338$app_debug(), composer2, 805306416, 508);
            if (PipelineSettingsScreen$lambda$5($status$delegate) != null) {
                composer2.startReplaceGroup(2056918353);
                ComposerKt.sourceInformation(composer2, "512@23164L11,512@23127L59");
                String strPipelineSettingsScreen$lambda$6 = PipelineSettingsScreen$lambda$5($status$delegate);
                Intrinsics.checkNotNull(strPipelineSettingsScreen$lambda$6);
                TextKt.m3142Text4IGK_g(strPipelineSettingsScreen$lambda$6, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getSecondary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, composer2, 0, 0, 131066);
            } else {
                composer2.startReplaceGroup(2033990412);
            }
            composer2.endReplaceGroup();
            SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer2, 6);
            TextKt.m3142Text4IGK_g("Sideload via scripts/push-local-models.sh:\nfiles/models/parakeet/{model_npu.tflite,model.tflite,tokenizer.json}\nfiles/models/gemma4b/gemma-4-E2B-it_Google_Tensor_G5.litertlm", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodyMedium(), composer2, 0, 0, 65530);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerEnd(composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$23$lambda$22(MutableState $settings$delegate, ProviderMode it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), it, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, null, null, 524286, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$25$lambda$24(MutableState $settings$delegate, ProviderMode it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, it, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, null, null, 524285, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$27$lambda$26(MutableState $settings$delegate, ProviderMode it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, it, null, false, null, null, null, null, null, null, false, null, false, null, null, null, null, null, 524283, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$29$lambda$28(MutableState $settings$delegate, ProviderMode it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, it, false, null, null, null, null, null, null, false, null, false, null, null, null, null, null, 524279, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$33$lambda$32$lambda$31(MutableState $settings$delegate, boolean it) {
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, it, null, null, null, null, null, null, false, null, false, null, null, null, null, null, 524271, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$35$lambda$34(MutableState $settings$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, it, null, null, null, null, false, null, false, null, null, null, null, null, 524223, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$37$lambda$36(CoroutineScope $scope, MutableState $settings$delegate, MutableState $status$delegate, PipelineConfig $config, MutableState $loadingModels$delegate, OpenRouterModelsClient $modelsClient, MutableState $sttModels$delegate, MutableState $chatModels$delegate) {
        PipelineSettingsScreen$refreshModels$default($scope, $settings$delegate, $status$delegate, $config, $loadingModels$delegate, $modelsClient, $sttModels$delegate, $chatModels$delegate, false, 256, null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$38(MutableState $loadingModels$delegate, MutableState $sttModels$delegate, MutableState $chatModels$delegate, RowScope OutlinedButton, Composer $composer, int $changed) {
        String str;
        Intrinsics.checkNotNullParameter(OutlinedButton, "$this$OutlinedButton");
        ComposerKt.sourceInformation($composer, "C251@11238L299:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1357868348, $changed, -1, "com.varun.pocketassistant.ui.PipelineSettingsScreen.<anonymous>.<anonymous>.<anonymous> (PipelineSettingsScreen.kt:251)");
            }
            if (PipelineSettingsScreen$lambda$8($loadingModels$delegate)) {
                str = "Fetching models…";
            } else {
                str = (PipelineSettingsScreen$lambda$11($sttModels$delegate).isEmpty() && PipelineSettingsScreen$lambda$14($chatModels$delegate).isEmpty()) ? "Load models from OpenRouter" : "Refresh models from OpenRouter";
            }
            TextKt.m3142Text4IGK_g(str, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$40$lambda$39(MutableState $settings$delegate, OpenRouterModelInfo it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, normalizeModelId(it.getId()), null, null, null, false, null, false, null, null, null, null, null, 524159, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$44$lambda$43$lambda$42(MutableState $settings$delegate, boolean it) {
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, it, null, null, null, null, null, 516095, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$46$lambda$45(MutableState $settings$delegate, OpenRouterModelInfo it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, normalizeModelId(it.getId()), null, null, false, null, false, null, null, null, null, null, 524031, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:18:0x0045  */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$48$lambda$47(MutableState $settings$delegate, OpenRouterModelInfo model) {
        String nextEffort;
        Intrinsics.checkNotNullParameter(model, "model");
        boolean z = true;
        if (!(PipelineSettingsScreen$lambda$2($settings$delegate).getReasoningEffort().length() > 0) && model.getSupportsReasoning()) {
            String defaultEffort = model.getDefaultEffort();
            if (defaultEffort != null && !StringsKt.isBlank(defaultEffort)) {
                z = false;
            }
            if (!z) {
                nextEffort = model.getDefaultEffort();
            } else {
                nextEffort = PipelineSettingsScreen$lambda$2($settings$delegate).getReasoningEffort();
            }
        } else {
            nextEffort = PipelineSettingsScreen$lambda$2($settings$delegate).getReasoningEffort();
        }
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, normalizeModelId(model.getId()), nextEffort == null ? "" : nextEffort, false, null, false, null, null, null, null, null, 522751, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$50$lambda$49(ReasoningEffort $effort, MutableState $settings$delegate) {
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, $effort.getApiValue(), false, null, false, null, null, null, null, null, 523263, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$53$lambda$52$lambda$51(ReasoningEffort $effort, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C375@17156L18:PipelineSettingsScreen.kt#w5368b");
        if ($composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1868080404, $changed, -1, "com.varun.pocketassistant.ui.PipelineSettingsScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (PipelineSettingsScreen.kt:375)");
            }
            TextKt.m3142Text4IGK_g($effort.getLabel(), (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$55$lambda$54(MutableState $settings$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, it, false, null, null, null, null, null, 520191, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$58$lambda$57(MutableState $settings$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, null, it, 262143, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$60$lambda$59(MutableState $settings$delegate) {
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, null, "", 262143, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$63$lambda$62(MutableState $settings$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, it, null, null, 458751, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$65$lambda$64(MutableState $settings$delegate) {
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, "", null, null, 458751, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$68$lambda$67(MutableState $settings$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, it, null, 393215, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$70$lambda$69(MutableState $settings$delegate) {
        $settings$delegate.setValue(PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, null, null, null, null, null, null, false, null, false, null, null, null, "", null, 393215, null));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$72$lambda$71(PipelineConfig $config, MutableState $settings$delegate, MutableState $status$delegate) {
        PipelineSettings toSave = PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, PipelineSettings.DEFAULT_BASE_URL, null, null, null, null, null, false, null, false, null, null, normalizeTemplate(PipelineSettingsScreen$lambda$2($settings$delegate).getCleanupPrompt(), CleanupPrompts.INSTANCE.getDEFAULT_CLEANUP_PROMPT()), normalizeTemplate(PipelineSettingsScreen$lambda$2($settings$delegate).getSummaryPrompt(), CleanupPrompts.INSTANCE.getDEFAULT_SUMMARY_PROMPT()), normalizeTemplate(PipelineSettingsScreen$lambda$2($settings$delegate).getGlossary(), CleanupPrompts.INSTANCE.getDEFAULT_GLOSSARY()), 65503, null);
        $config.save(toSave);
        $settings$delegate.setValue($config.load());
        $status$delegate.setValue("Saved");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$74$lambda$73(PipelineConfig $config, CoroutineScope $scope, MutableState $settings$delegate, MutableState $status$delegate, PocketAssistantApp $app) {
        PipelineSettings toSave = PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, PipelineSettings.DEFAULT_BASE_URL, null, null, null, null, null, false, null, false, null, null, null, null, null, 524255, null);
        $config.save(toSave);
        $settings$delegate.setValue($config.load());
        if ($config.cloudConfigured() || PipelineSettingsScreen$lambda$2($settings$delegate).getAsrMode() != ProviderMode.PREFER_CLOUD) {
            BuildersKt__Builders_commonKt.launch$default($scope, null, null, new PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$25$1$1($app, $status$delegate, null), 3, null);
            return Unit.INSTANCE;
        }
        $status$delegate.setValue("Save an OpenRouter API key before cloud ASR reprocess");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit PipelineSettingsScreen$lambda$78$lambda$77$lambda$76$lambda$75(PipelineConfig $config, CoroutineScope $scope, MutableState $settings$delegate, MutableState $status$delegate, PocketAssistantApp $app) {
        PipelineSettings toSave = PipelineSettings.copy$default(PipelineSettingsScreen$lambda$2($settings$delegate), null, null, null, null, false, PipelineSettings.DEFAULT_BASE_URL, null, null, null, null, null, false, null, false, null, null, null, null, null, 524255, null);
        $config.save(toSave);
        $settings$delegate.setValue($config.load());
        if ($config.cloudConfigured() || (PipelineSettingsScreen$lambda$2($settings$delegate).getCleanupMode() != ProviderMode.PREFER_CLOUD && PipelineSettingsScreen$lambda$2($settings$delegate).getSummaryMode() != ProviderMode.PREFER_CLOUD)) {
            BuildersKt__Builders_commonKt.launch$default($scope, null, null, new PipelineSettingsScreenKt$PipelineSettingsScreen$3$1$26$1$1($app, $status$delegate, null), 3, null);
            return Unit.INSTANCE;
        }
        $status$delegate.setValue("Save an OpenRouter API key before cloud cleanup/summary");
        return Unit.INSTANCE;
    }

    private static final void ModelDropdown(final String str, final List<OpenRouterModelInfo> list, final String str2, Function1<? super OpenRouterModelInfo, Unit> function1, final boolean z, final String str3, boolean z2, String str4, Composer composer, final int i, final int i2) {
        final Function1<? super OpenRouterModelInfo, Unit> function2;
        Composer composer2;
        final boolean z3;
        final String str5;
        boolean z4;
        Object next;
        String strNormalizeId;
        Function0<ComposeUiNode> function0;
        Composer composerStartRestartGroup = composer.startRestartGroup(-2050900161);
        ComposerKt.sourceInformation(composerStartRestartGroup, "C(ModelDropdown)N(label,models,selectedId,onSelect,enabled,emptyHint,preferReasoningFirst,supportingText)538@23985L34,547@24331L1730:PipelineSettingsScreen.kt#w5368b");
        int i3 = i;
        if ((i & 6) == 0) {
            i3 |= composerStartRestartGroup.changed(str) ? 4 : 2;
        }
        if ((i & 48) == 0) {
            i3 |= composerStartRestartGroup.changedInstance(list) ? 32 : 16;
        }
        if ((i & 384) == 0) {
            i3 |= composerStartRestartGroup.changed(str2) ? 256 : 128;
        }
        if ((i & 3072) == 0) {
            i3 |= composerStartRestartGroup.changedInstance(function1) ? 2048 : 1024;
        }
        if ((i & 24576) == 0) {
            i3 |= composerStartRestartGroup.changed(z) ? 16384 : 8192;
        }
        if ((i & ProfileVerifier.CompilationStatus.RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE) == 0) {
            i3 |= composerStartRestartGroup.changed(str3) ? 131072 : 65536;
        }
        int i4 = i2 & 64;
        if (i4 != 0) {
            i3 |= 1572864;
        } else if ((1572864 & i) == 0) {
            i3 |= composerStartRestartGroup.changed(z2) ? 1048576 : 524288;
        }
        int i5 = i2 & 128;
        if (i5 != 0) {
            i3 |= 12582912;
        } else if ((i & 12582912) == 0) {
            i3 |= composerStartRestartGroup.changed(str4) ? 8388608 : 4194304;
        }
        if (composerStartRestartGroup.shouldExecute((i3 & 4793491) != 4793490, i3 & 1)) {
            boolean z5 = i4 != 0 ? false : z2;
            final String str6 = i5 != 0 ? null : str4;
            if (ComposerKt.isTraceInProgress()) {
                z4 = false;
                ComposerKt.traceEventStart(-2050900161, i3, -1, "com.varun.pocketassistant.ui.ModelDropdown (PipelineSettingsScreen.kt:537)");
            } else {
                z4 = false;
            }
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, 474944289, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue = composerStartRestartGroup.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                MutableState mutableStateMutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(Boolean.valueOf(z4), null, 2, null);
                composerStartRestartGroup.updateRememberedValue(mutableStateMutableStateOf$default);
                objRememberedValue = mutableStateMutableStateOf$default;
            }
            final MutableState mutableState = (MutableState) objRememberedValue;
            ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
            List<OpenRouterModelInfo> list2 = list;
            Iterator<T> it = list2.iterator();
            while (true) {
                if (!it.hasNext()) {
                    next = null;
                    break;
                }
                next = it.next();
                List<OpenRouterModelInfo> list3 = list2;
                if (OpenRouterModels.INSTANCE.matches(((OpenRouterModelInfo) next).getId(), str2)) {
                    break;
                } else {
                    list2 = list3;
                }
            }
            OpenRouterModelInfo openRouterModelInfo = (OpenRouterModelInfo) next;
            if (openRouterModelInfo != null) {
                strNormalizeId = openRouterModelInfo.getId();
            } else if (StringsKt.isBlank(str2) || !z) {
                strNormalizeId = z ? "Select a model" : str3;
            } else {
                strNormalizeId = OpenRouterModels.INSTANCE.normalizeId(str2);
            }
            Modifier modifierFillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, 1042775818, "CC(Box)N(modifier,contentAlignment,propagateMinConstraints,content)71@3424L131:Box.kt#2w3rfo");
            MeasurePolicy measurePolicyMaybeCachedBoxMeasurePolicy = BoxKt.maybeCachedBoxMeasurePolicy(Alignment.INSTANCE.getTopStart(), false);
            String str7 = strNormalizeId;
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composerStartRestartGroup, z4 ? 1 : 0));
            CompositionLocalMap currentCompositionLocalMap = composerStartRestartGroup.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier(composerStartRestartGroup, modifierFillMaxWidth$default);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i6 = ((((6 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composerStartRestartGroup.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composerStartRestartGroup.startReusableNode();
            if (composerStartRestartGroup.getInserting()) {
                function0 = constructor;
                composerStartRestartGroup.createNode(function0);
            } else {
                function0 = constructor;
                composerStartRestartGroup.useNode();
            }
            Composer composerM4159constructorimpl = Updater.m4159constructorimpl(composerStartRestartGroup);
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyMaybeCachedBoxMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash);
            }
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i7 = (i6 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, 1833054614, "C72@3469L9:Box.kt#2w3rfo");
            int i8 = ((6 >> 6) & 112) | 6;
            BoxScopeInstance boxScopeInstance = BoxScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, 114662676, "C576@25483L11,577@25558L11,578@25630L11,579@25720L11,580@25808L11,574@25339L512,550@24457L2,553@24541L15,563@24920L322,554@24587L304,548@24381L1481:PipelineSettingsScreen.kt#w5368b");
            int i9 = i3;
            Modifier modifierFillMaxWidth$default2 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            TextFieldColors textFieldColorsM2792colors0hiis_0 = OutlinedTextFieldDefaults.INSTANCE.m2792colors0hiis_0(0L, 0L, MaterialTheme.INSTANCE.getColorScheme(composerStartRestartGroup, MaterialTheme.$stable).getOnSurface(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, null, 0L, 0L, MaterialTheme.INSTANCE.getColorScheme(composerStartRestartGroup, MaterialTheme.$stable).getOutline(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, MaterialTheme.INSTANCE.getColorScheme(composerStartRestartGroup, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, 0L, 0L, MaterialTheme.INSTANCE.getColorScheme(composerStartRestartGroup, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, MaterialTheme.INSTANCE.getColorScheme(composerStartRestartGroup, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, composerStartRestartGroup, 0, 0, 0, 0, 3072, 2111823867, 4091);
            ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, -966131769, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue2 = composerStartRestartGroup.rememberedValue();
            if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                Function1 function3 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda34
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj) {
                        return PipelineSettingsScreenKt.ModelDropdown$lambda$91$lambda$85$lambda$84((String) obj);
                    }
                };
                composerStartRestartGroup.updateRememberedValue(function3);
                objRememberedValue2 = function3;
            }
            ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
            Composer composer3 = composerStartRestartGroup;
            boolean z6 = false;
            String str8 = str6;
            OutlinedTextFieldKt.OutlinedTextField(str7, (Function1<? super String, Unit>) objRememberedValue2, modifierFillMaxWidth$default2, z, true, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(1579514079, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda35
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return PipelineSettingsScreenKt.ModelDropdown$lambda$91$lambda$86(str, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, composerStartRestartGroup, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-653267588, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda36
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return PipelineSettingsScreenKt.ModelDropdown$lambda$91$lambda$87(z, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, composerStartRestartGroup, 54), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableLambdaKt.rememberComposableLambda(-785181908, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda37
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return PipelineSettingsScreenKt.ModelDropdown$lambda$91$lambda$88(z, str3, str6, list, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, composerStartRestartGroup, 54), false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, false, 0, 0, (MutableInteractionSource) null, (Shape) null, textFieldColorsM2792colors0hiis_0, composerStartRestartGroup, ((i9 >> 3) & 7168) | 806904240, 384, 0, 4189600);
            if (z) {
                composerStartRestartGroup.startReplaceGroup(116107058);
                ComposerKt.sourceInformation(composerStartRestartGroup, "587@26009L21,584@25898L147");
                Modifier modifierMatchParentSize = boxScopeInstance.matchParentSize(Modifier.INSTANCE);
                ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, -966082086, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                Object objRememberedValue3 = composerStartRestartGroup.rememberedValue();
                if (objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                    Function0 function4 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda38
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.ModelDropdown$lambda$91$lambda$90$lambda$89(mutableState);
                        }
                    };
                    composerStartRestartGroup.updateRememberedValue(function4);
                    objRememberedValue3 = function4;
                }
                ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
                z6 = false;
                BoxKt.Box(ClickableKt.m296clickableoSLSa3U$default(modifierMatchParentSize, false, null, null, null, (Function0) objRememberedValue3, 15, null), composerStartRestartGroup, 0);
            } else {
                composer3 = composer3;
                composerStartRestartGroup.startReplaceGroup(90423837);
            }
            composerStartRestartGroup.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
            ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
            composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer3);
            ComposerKt.sourceInformationMarkerEnd(composer3);
            ComposerKt.sourceInformationMarkerEnd(composer3);
            if (ModelDropdown$lambda$81(mutableState) && z) {
                composerStartRestartGroup.startReplaceGroup(1840472873);
                ComposerKt.sourceInformation(composerStartRestartGroup, "598@26296L79,602@26401L22,593@26104L330");
                ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, 475018286, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                boolean z7 = (i9 & 7168) == 2048 ? true : z6;
                Object objRememberedValue4 = composerStartRestartGroup.rememberedValue();
                if (z7 || objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                    function2 = function1;
                    Function1 function5 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda39
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipelineSettingsScreenKt.ModelDropdown$lambda$93$lambda$92(function2, mutableState, (OpenRouterModelInfo) obj);
                        }
                    };
                    composerStartRestartGroup.updateRememberedValue(function5);
                    objRememberedValue4 = function5;
                } else {
                    function2 = function1;
                }
                Function1 function6 = (Function1) objRememberedValue4;
                ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
                ComposerKt.sourceInformationMarkerStart(composerStartRestartGroup, 475021589, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                Object objRememberedValue5 = composerStartRestartGroup.rememberedValue();
                if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                    Function0 function7 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda40
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.ModelDropdown$lambda$95$lambda$94(mutableState);
                        }
                    };
                    composerStartRestartGroup.updateRememberedValue(function7);
                    objRememberedValue5 = function7;
                }
                ComposerKt.sourceInformationMarkerEnd(composerStartRestartGroup);
                ModelPickerDialog(str, list, str2, z5, function6, (Function0) objRememberedValue5, composerStartRestartGroup, (i9 & 14) | ProfileVerifier.CompilationStatus.RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE | (i9 & 112) | (i9 & 896) | ((i9 >> 9) & 7168));
                composer2 = composerStartRestartGroup;
            } else {
                function2 = function1;
                composer2 = composerStartRestartGroup;
                composer2.startReplaceGroup(1814575907);
            }
            composer2.endReplaceGroup();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            z3 = z5;
            str5 = str8;
        } else {
            function2 = function1;
            composer2 = composerStartRestartGroup;
            composer2.skipToGroupEnd();
            z3 = z2;
            str5 = str4;
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            final Function1<? super OpenRouterModelInfo, Unit> function8 = function2;
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda41
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return PipelineSettingsScreenKt.ModelDropdown$lambda$96(str, list, str2, function8, z, str3, z3, str5, i, i2, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    private static final boolean ModelDropdown$lambda$81(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void ModelDropdown$lambda$82(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$91$lambda$85$lambda$84(String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$91$lambda$86(String $label, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C553@24543L11:PipelineSettingsScreen.kt#w5368b");
        if ($composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1579514079, $changed, -1, "com.varun.pocketassistant.ui.ModelDropdown.<anonymous>.<anonymous> (PipelineSettingsScreen.kt:553)");
            }
            TextKt.m3142Text4IGK_g($label, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$91$lambda$88(boolean $enabled, String $emptyHint, String $supportingText, List $models, Composer $composer, int $changed) {
        String str;
        ComposerKt.sourceInformation($composer, "C555@24605L272:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-785181908, $changed, -1, "com.varun.pocketassistant.ui.ModelDropdown.<anonymous>.<anonymous> (PipelineSettingsScreen.kt:555)");
            }
            if (!$enabled) {
                str = $emptyHint;
            } else if ($supportingText == null) {
                str = $models.size() + " models from OpenRouter — tap to choose";
            } else {
                str = $supportingText;
            }
            TextKt.m3142Text4IGK_g(str, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$91$lambda$87(boolean $enabled, Composer $composer, int $changed) {
        long jM4838copywmQWz5c;
        ComposerKt.sourceInformation($composer, "C564@24938L290:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-653267588, $changed, -1, "com.varun.pocketassistant.ui.ModelDropdown.<anonymous>.<anonymous> (PipelineSettingsScreen.kt:564)");
            }
            if ($enabled) {
                $composer.startReplaceGroup(-997803860);
                ComposerKt.sourceInformation($composer, "567@25050L11");
                jM4838copywmQWz5c = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurfaceVariant();
                $composer.endReplaceGroup();
            } else {
                $composer.startReplaceGroup(-997708225);
                ComposerKt.sourceInformation($composer, "569@25146L11");
                long onSurface = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurface();
                jM4838copywmQWz5c = Color.m4838copywmQWz5c(onSurface, (14 & 1) != 0 ? Color.m4842getAlphaimpl(onSurface) : 0.38f, (14 & 2) != 0 ? Color.m4846getRedimpl(onSurface) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(onSurface) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(onSurface) : 0.0f);
                $composer.endReplaceGroup();
            }
            TextKt.m3142Text4IGK_g("▼", (Modifier) null, jM4838copywmQWz5c, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 6, 0, 131066);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$91$lambda$90$lambda$89(MutableState $showPicker$delegate) {
        ModelDropdown$lambda$82($showPicker$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$93$lambda$92(Function1 $onSelect, MutableState $showPicker$delegate, OpenRouterModelInfo it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $onSelect.invoke(it);
        ModelDropdown$lambda$82($showPicker$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelDropdown$lambda$95$lambda$94(MutableState $showPicker$delegate) {
        ModelDropdown$lambda$82($showPicker$delegate, false);
        return Unit.INSTANCE;
    }

    private static final void ModelPickerDialog(final String title, final List<OpenRouterModelInfo> list, final String selectedId, final boolean preferReasoningFirst, final Function1<? super OpenRouterModelInfo, Unit> function1, final Function0<Unit> function0, Composer $composer, final int $changed) {
        Composer $composer2;
        int $dirty;
        Object objSortedWith = list;
        Composer $composer3 = $composer.startRestartGroup(-1978351076);
        ComposerKt.sourceInformation($composer3, "C(ModelPickerDialog)N(title,models,selectedId,preferReasoningFirst,onSelect,onDismiss)616@26697L31,617@26747L255,624@27022L224,697@30284L73,634@27319L15,635@27351L2907,632@27252L3112:PipelineSettingsScreen.kt#w5368b");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= $composer3.changed(title) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty2 |= $composer3.changedInstance(objSortedWith) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty2 |= $composer3.changed(selectedId) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty2 |= $composer3.changed(preferReasoningFirst) ? 2048 : 1024;
        }
        if (($changed & 24576) == 0) {
            $dirty2 |= $composer3.changedInstance(function1) ? 16384 : 8192;
        }
        if ((196608 & $changed) == 0) {
            $dirty2 |= $composer3.changedInstance(function0) ? 131072 : 65536;
        }
        if (!$composer3.shouldExecute((74899 & $dirty2) != 74898, $dirty2 & 1)) {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1978351076, $dirty2, -1, "com.varun.pocketassistant.ui.ModelPickerDialog (PipelineSettingsScreen.kt:615)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 1475136219, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            Object objRememberedValue = $composer3.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default("", null, 2, null);
                $composer3.updateRememberedValue(objMutableStateOf$default);
                objRememberedValue = objMutableStateOf$default;
            }
            final MutableState query$delegate = (MutableState) objRememberedValue;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 1475138043, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            boolean zChanged = $composer3.changed(objSortedWith) | (($dirty2 & 7168) == 2048);
            Object objRememberedValue2 = $composer3.rememberedValue();
            if (zChanged || objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                if (preferReasoningFirst) {
                    final Comparator comparator = new Comparator() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$lambda$102$$inlined$compareByDescending$1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.util.Comparator
                        public final int compare(T t, T t2) {
                            return ComparisonsKt.compareValues(Boolean.valueOf(((OpenRouterModelInfo) t2).getSupportsReasoning()), Boolean.valueOf(((OpenRouterModelInfo) t).getSupportsReasoning()));
                        }
                    };
                    objSortedWith = CollectionsKt.sortedWith(list, new Comparator() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$lambda$102$$inlined$thenBy$1
                        /* JADX WARN: Multi-variable type inference failed */
                        @Override // java.util.Comparator
                        public final int compare(T t, T t2) {
                            int previousCompare = comparator.compare(t, t2);
                            if (previousCompare != 0) {
                                return previousCompare;
                            }
                            String lowerCase = ((OpenRouterModelInfo) t).getId().toLowerCase(Locale.ROOT);
                            Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                            String lowerCase2 = ((OpenRouterModelInfo) t2).getId().toLowerCase(Locale.ROOT);
                            Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                            return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                        }
                    });
                }
                $composer3.updateRememberedValue(objSortedWith);
                objRememberedValue2 = objSortedWith;
            }
            Object ordered = (List) objRememberedValue2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            String strModelPickerDialog$lambda$98 = ModelPickerDialog$lambda$98(query$delegate);
            ComposerKt.sourceInformationMarkerStart($composer3, 1475146812, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            boolean zChanged2 = $composer3.changed(strModelPickerDialog$lambda$98) | $composer3.changed(ordered);
            int i = 0;
            Object objRememberedValue3 = $composer3.rememberedValue();
            if (zChanged2 || objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                if (StringsKt.isBlank(ModelPickerDialog$lambda$98(query$delegate))) {
                    $dirty = $dirty2;
                } else {
                    Iterable iterable = (Iterable) ordered;
                    Collection arrayList = new ArrayList();
                    for (Object obj : iterable) {
                        boolean z = zChanged2;
                        OpenRouterModelInfo openRouterModelInfo = (OpenRouterModelInfo) obj;
                        int $dirty3 = $dirty2;
                        int i2 = i;
                        Object obj2 = objRememberedValue3;
                        if (StringsKt.contains((CharSequence) openRouterModelInfo.getId(), (CharSequence) ModelPickerDialog$lambda$98(query$delegate), true) || StringsKt.contains((CharSequence) openRouterModelInfo.getName(), (CharSequence) ModelPickerDialog$lambda$98(query$delegate), true)) {
                            arrayList.add(obj);
                        }
                        zChanged2 = z;
                        objRememberedValue3 = obj2;
                        $dirty2 = $dirty3;
                        i = i2;
                    }
                    $dirty = $dirty2;
                    ordered = (List) arrayList;
                }
                $composer3.updateRememberedValue(ordered);
                objRememberedValue3 = ordered;
            } else {
                $dirty = $dirty2;
            }
            final List filtered = (List) objRememberedValue3;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            int i3 = (($dirty >> 15) & 14) | 1769520;
            $composer2 = $composer3;
            AndroidAlertDialog_androidKt.m2210AlertDialogOix01E0(function0, ComposableLambdaKt.rememberComposableLambda(-184457516, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return PipelineSettingsScreenKt.ModelPickerDialog$lambda$105(function0, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer3, 54), null, null, null, ComposableLambdaKt.rememberComposableLambda(1777452112, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda11
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return PipelineSettingsScreenKt.ModelPickerDialog$lambda$106(title, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer3, 54), ComposableLambdaKt.rememberComposableLambda(120445871, true, new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda22
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return PipelineSettingsScreenKt.ModelPickerDialog$lambda$116(filtered, selectedId, function1, query$delegate, (Composer) obj3, ((Integer) obj4).intValue());
                }
            }, $composer3, 54), null, 0L, 0L, 0L, 0L, 0.0f, null, $composer2, i3, 0, 16284);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda33
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return PipelineSettingsScreenKt.ModelPickerDialog$lambda$117(title, list, selectedId, preferReasoningFirst, function1, function0, $changed, (Composer) obj3, ((Integer) obj4).intValue());
                }
            });
        }
    }

    private static final String ModelPickerDialog$lambda$98(MutableState<String> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelPickerDialog$lambda$106(String $title, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C634@27321L11:PipelineSettingsScreen.kt#w5368b");
        if ($composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1777452112, $changed, -1, "com.varun.pocketassistant.ui.ModelPickerDialog.<anonymous> (PipelineSettingsScreen.kt:634)");
            }
            TextKt.m3142Text4IGK_g($title, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:28:0x018d  */
    /* JADX WARN: Code duplicated, block: B:29:0x019f  */
    /* JADX WARN: Code duplicated, block: B:32:0x0203  */
    /* JADX WARN: Code duplicated, block: B:33:0x0277  */
    /* JADX WARN: Code duplicated, block: B:35:0x02bd  */
    /* JADX WARN: Code duplicated, block: B:39:0x02c9  */
    /* JADX WARN: Code duplicated, block: B:44:0x0317  */
    public static final Unit ModelPickerDialog$lambda$116(final List $filtered, final String $selectedId, final Function1 $onSelect, final MutableState $query$delegate, Composer $composer, int $changed) {
        Function0<ComposeUiNode> function0;
        int i;
        Object objRememberedValue;
        boolean zChangedInstance;
        Object objRememberedValue2;
        Composer composer;
        ComposerKt.sourceInformation($composer, "C636@27365L2883:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(120445871, $changed, -1, "com.varun.pocketassistant.ui.ModelPickerDialog.<anonymous> (PipelineSettingsScreen.kt:636)");
            }
            Modifier modifierM870heightInVpY3zN4$default = SizeKt.m870heightInVpY3zN4$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), 0.0f, Dp.m7582constructorimpl(480), 1, null);
            ComposerKt.sourceInformationMarkerStart($composer, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), $composer, ((6 >> 3) & 14) | ((6 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer, modifierM870heightInVpY3zN4$default);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i2 = ((((6 << 3) & 112) << 6) & 896) | 6;
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
            if (composerM4159constructorimpl.getInserting()) {
                i = iHashCode;
            } else {
                i = iHashCode;
                if (!Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(i))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
                int i3 = (i2 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
                int i4 = ((6 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, -440082030, "C643@27612L14,641@27522L266,648@27805L29:PipelineSettingsScreen.kt#w5368b");
                String strModelPickerDialog$lambda$98 = ModelPickerDialog$lambda$98($query$delegate);
                Modifier modifierFillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                ComposerKt.sourceInformationMarkerStart($composer, -429838009, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                objRememberedValue = $composer.rememberedValue();
                if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda42
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj) {
                            return PipelineSettingsScreenKt.ModelPickerDialog$lambda$116$lambda$115$lambda$108$lambda$107($query$delegate, (String) obj);
                        }
                    };
                    $composer.updateRememberedValue(objRememberedValue);
                }
                ComposerKt.sourceInformationMarkerEnd($composer);
                OutlinedTextFieldKt.OutlinedTextField(strModelPickerDialog$lambda$98, (Function1<? super String, Unit>) objRememberedValue, modifierFillMaxWidth$default, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8228getLambda$551527649$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, $composer, 1573296, 12582912, 0, 8257464);
                SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), $composer, 6);
                if ($filtered.isEmpty()) {
                    $composer.startReplaceGroup(-439807743);
                    ComposerKt.sourceInformation($composer, "652@28001L10,653@28070L11,650@27897L224");
                    TextKt.m3142Text4IGK_g("No models match “" + ModelPickerDialog$lambda$98($query$delegate) + "”", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodyMedium(), $composer, 0, 0, 65530);
                    $composer.endReplaceGroup();
                    composer = $composer;
                } else {
                    $composer.startReplaceGroup(-439483328);
                    ComposerKt.sourceInformation($composer, "659@28335L1881,656@28167L2049");
                    Modifier modifierFillMaxWidth$default2 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                    Arrangement.HorizontalOrVertical horizontalOrVerticalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(0));
                    ComposerKt.sourceInformationMarkerStart($composer, -429813006, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                    zChangedInstance = $composer.changedInstance($filtered) | $composer.changed($selectedId) | $composer.changed($onSelect);
                    objRememberedValue2 = $composer.rememberedValue();
                    if (!zChangedInstance) {
                        composer = $composer;
                        if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                        }
                        ComposerKt.sourceInformationMarkerEnd(composer);
                        LazyDslKt.LazyColumn(modifierFillMaxWidth$default2, null, null, false, horizontalOrVerticalM689spacedBy0680j_4, null, null, false, null, (Function1) objRememberedValue2, composer, 24582, 494);
                        composer.endReplaceGroup();
                    } else {
                        composer = $composer;
                    }
                    Object obj = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda43
                        @Override // kotlin.jvm.functions.Function1
                        public final Object invoke(Object obj2) {
                            return PipelineSettingsScreenKt.ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113($filtered, $selectedId, $onSelect, (LazyListScope) obj2);
                        }
                    };
                    $composer.updateRememberedValue(obj);
                    objRememberedValue2 = obj;
                    ComposerKt.sourceInformationMarkerEnd(composer);
                    LazyDslKt.LazyColumn(modifierFillMaxWidth$default2, null, null, false, horizontalOrVerticalM689spacedBy0680j_4, null, null, false, null, (Function1) objRememberedValue2, composer, 24582, 494);
                    composer.endReplaceGroup();
                }
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
            }
            composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(i));
            composerM4159constructorimpl.apply(Integer.valueOf(i), setCompositeKeyHash);
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i5 = (i2 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance2 = ColumnScopeInstance.INSTANCE;
            int i6 = ((6 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -440082030, "C643@27612L14,641@27522L266,648@27805L29:PipelineSettingsScreen.kt#w5368b");
            String strModelPickerDialog$lambda$99 = ModelPickerDialog$lambda$98($query$delegate);
            Modifier modifierFillMaxWidth$default3 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart($composer, -429838009, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            objRememberedValue = $composer.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                objRememberedValue = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda42
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj2) {
                        return PipelineSettingsScreenKt.ModelPickerDialog$lambda$116$lambda$115$lambda$108$lambda$107($query$delegate, (String) obj2);
                    }
                };
                $composer.updateRememberedValue(objRememberedValue);
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            OutlinedTextFieldKt.OutlinedTextField(strModelPickerDialog$lambda$99, (Function1<? super String, Unit>) objRememberedValue, modifierFillMaxWidth$default3, false, false, (TextStyle) null, (Function2<? super Composer, ? super Integer, Unit>) ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8228getLambda$551527649$app_debug(), (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, (Function2<? super Composer, ? super Integer, Unit>) null, false, (VisualTransformation) null, (KeyboardOptions) null, (KeyboardActions) null, true, 0, 0, (MutableInteractionSource) null, (Shape) null, (TextFieldColors) null, $composer, 1573296, 12582912, 0, 8257464);
            SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), $composer, 6);
            if ($filtered.isEmpty()) {
                $composer.startReplaceGroup(-439807743);
                ComposerKt.sourceInformation($composer, "652@28001L10,653@28070L11,650@27897L224");
                TextKt.m3142Text4IGK_g("No models match “" + ModelPickerDialog$lambda$98($query$delegate) + "”", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodyMedium(), $composer, 0, 0, 65530);
                $composer.endReplaceGroup();
                composer = $composer;
            } else {
                $composer.startReplaceGroup(-439483328);
                ComposerKt.sourceInformation($composer, "659@28335L1881,656@28167L2049");
                Modifier modifierFillMaxWidth$default4 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                Arrangement.HorizontalOrVertical horizontalOrVerticalM689spacedBy0680j_5 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(0));
                ComposerKt.sourceInformationMarkerStart($composer, -429813006, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                zChangedInstance = $composer.changedInstance($filtered) | $composer.changed($selectedId) | $composer.changed($onSelect);
                objRememberedValue2 = $composer.rememberedValue();
                if (!zChangedInstance) {
                    composer = $composer;
                    if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                    }
                    ComposerKt.sourceInformationMarkerEnd(composer);
                    LazyDslKt.LazyColumn(modifierFillMaxWidth$default4, null, null, false, horizontalOrVerticalM689spacedBy0680j_5, null, null, false, null, (Function1) objRememberedValue2, composer, 24582, 494);
                    composer.endReplaceGroup();
                } else {
                    composer = $composer;
                }
                Object obj2 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda43
                    @Override // kotlin.jvm.functions.Function1
                    public final Object invoke(Object obj3) {
                        return PipelineSettingsScreenKt.ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113($filtered, $selectedId, $onSelect, (LazyListScope) obj3);
                    }
                };
                $composer.updateRememberedValue(obj2);
                objRememberedValue2 = obj2;
                ComposerKt.sourceInformationMarkerEnd(composer);
                LazyDslKt.LazyColumn(modifierFillMaxWidth$default4, null, null, false, horizontalOrVerticalM689spacedBy0680j_5, null, null, false, null, (Function1) objRememberedValue2, composer, 24582, 494);
                composer.endReplaceGroup();
            }
            ComposerKt.sourceInformationMarkerEnd(composer);
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
    public static final Unit ModelPickerDialog$lambda$116$lambda$115$lambda$108$lambda$107(MutableState $query$delegate, String it) {
        Intrinsics.checkNotNullParameter(it, "it");
        $query$delegate.setValue(it);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113(final List $filtered, final String $selectedId, final Function1 $onSelect, LazyListScope LazyColumn) {
        Intrinsics.checkNotNullParameter(LazyColumn, "$this$LazyColumn");
        final Function1 function1 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda29
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return PipelineSettingsScreenKt.ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113$lambda$109((OpenRouterModelInfo) obj);
            }
        };
        final Function1 function2 = new Function1() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113$$inlined$items$default$1
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                return invoke((OpenRouterModelInfo) p1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final Void invoke(OpenRouterModelInfo openRouterModelInfo) {
                return null;
            }
        };
        LazyColumn.items($filtered.size(), new Function1<Integer, Object>() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113$$inlined$items$default$2
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Integer num) {
                return invoke(num.intValue());
            }

            public final Object invoke(int index) {
                return function1.invoke($filtered.get(index));
            }
        }, new Function1<Integer, Object>() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113$$inlined$items$default$3
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Object invoke(Integer num) {
                return invoke(num.intValue());
            }

            public final Object invoke(int index) {
                return function2.invoke($filtered.get(index));
            }
        }, ComposableLambdaKt.composableLambdaInstance(802480018, true, new Function4<LazyItemScope, Integer, Composer, Integer, Unit>() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113$$inlined$items$default$4
            @Override // kotlin.jvm.functions.Function4
            public /* bridge */ /* synthetic */ Unit invoke(LazyItemScope lazyItemScope, Integer num, Composer composer, Integer num2) {
                invoke(lazyItemScope, num.intValue(), composer, num2.intValue());
                return Unit.INSTANCE;
            }

            /* JADX WARN: Code duplicated, block: B:36:0x015f  */
            /* JADX WARN: Code duplicated, block: B:39:0x016b  */
            /* JADX WARN: Code duplicated, block: B:40:0x0171  */
            /* JADX WARN: Code duplicated, block: B:51:0x0216  */
            /* JADX WARN: Code duplicated, block: B:52:0x0233  */
            /* JADX WARN: Code duplicated, block: B:58:0x02e8  */
            /* JADX WARN: Code duplicated, block: B:61:0x02f7  */
            /* JADX WARN: Code duplicated, block: B:62:0x0348  */
            /* JADX WARN: Code duplicated, block: B:65:0x0372  */
            /* JADX WARN: Code duplicated, block: B:68:? A[RETURN, SYNTHETIC] */
            public final void invoke(LazyItemScope $this$items, int it, Composer $composer, int $changed) {
                int i;
                int iHashCode;
                Function0<ComposeUiNode> constructor;
                Function0<ComposeUiNode> function0;
                Composer composerM4159constructorimpl;
                long j;
                ComposerKt.sourceInformation($composer, "CN(it)178@8834L22:LazyDsl.kt#428nma");
                int $dirty = $changed;
                if (($changed & 6) == 0) {
                    $dirty |= $composer.changed($this$items) ? 4 : 2;
                }
                if (($changed & 48) == 0) {
                    $dirty |= $composer.changed(it) ? 32 : 16;
                }
                if (!$composer.shouldExecute(($dirty & 147) != 146, $dirty & 1)) {
                    $composer.skipToGroupEnd();
                    return;
                }
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventStart(802480018, $dirty, -1, "androidx.compose.foundation.lazy.items.<anonymous> (LazyDsl.kt:178)");
                }
                int i2 = $dirty & 14;
                final OpenRouterModelInfo openRouterModelInfo = (OpenRouterModelInfo) $filtered.get(it);
                $composer.startReplaceGroup(-1764316102);
                ComposerKt.sourceInformation($composer, "CN(model)*665@28682L19,662@28523L1645:PipelineSettingsScreen.kt#w5368b");
                boolean zMatches = OpenRouterModels.INSTANCE.matches(openRouterModelInfo.getId(), $selectedId);
                Modifier modifierFillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                ComposerKt.sourceInformationMarkerStart($composer, -334001835, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                boolean zChanged = $composer.changed($onSelect) | $composer.changedInstance(openRouterModelInfo);
                Object objRememberedValue = $composer.rememberedValue();
                if (!zChanged) {
                    i = 0;
                    if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer);
                    Modifier modifierM831paddingVpY3zN4 = PaddingKt.m831paddingVpY3zN4(ClickableKt.m296clickableoSLSa3U$default(modifierFillMaxWidth$default, false, null, null, null, (Function0) objRememberedValue, 15, null), Dp.m7582constructorimpl(4), Dp.m7582constructorimpl(10));
                    int i3 = i;
                    ComposerKt.sourceInformationMarkerStart($composer, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
                    MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), $composer, ((i3 >> 3) & 14) | ((i3 >> 3) & 112));
                    int i4 = (i3 << 3) & 112;
                    ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                    iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, i));
                    CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
                    Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer, modifierM831paddingVpY3zN4);
                    constructor = ComposeUiNode.INSTANCE.getConstructor();
                    int i5 = ((i4 << 6) & 896) | 6;
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
                    composerM4159constructorimpl = Updater.m4159constructorimpl($composer);
                    Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyColumnMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                    Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                    Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                    if (!composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                        composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                        composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash);
                    }
                    Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
                    int i6 = (i5 >> 6) & 14;
                    ComposerKt.sourceInformationMarkerStart($composer, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                    ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
                    int i7 = ((i3 >> 6) & 112) | 6;
                    ComposerKt.sourceInformationMarkerStart($composer, -1313883698, "C670@28959L10,668@28849L460:PipelineSettingsScreen.kt#w5368b");
                    String id = openRouterModelInfo.getId();
                    TextStyle bodyMedium = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodyMedium();
                    if (zMatches) {
                        $composer.startReplaceGroup(-1313730683);
                        ComposerKt.sourceInformation($composer, "672@29096L11");
                        long primary = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getPrimary();
                        $composer.endReplaceGroup();
                        j = primary;
                    } else {
                        $composer.startReplaceGroup(-1313612573);
                        ComposerKt.sourceInformation($composer, "674@29215L11");
                        long onSurface = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurface();
                        $composer.endReplaceGroup();
                        j = onSurface;
                    }
                    TextKt.m3142Text4IGK_g(id, (Modifier) null, j, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, bodyMedium, $composer, 0, 0, 65530);
                    if (!StringsKt.isBlank(openRouterModelInfo.getName()) || Intrinsics.areEqual(openRouterModelInfo.getName(), openRouterModelInfo.getId())) {
                        $composer.startReplaceGroup(-1342542858);
                    } else {
                        $composer.startReplaceGroup(-1313369409);
                        ComposerKt.sourceInformation($composer, "680@29555L10,681@29639L11,678@29435L271");
                        TextKt.m3142Text4IGK_g(openRouterModelInfo.getName(), (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodySmall(), $composer, 0, 0, 65530);
                    }
                    $composer.endReplaceGroup();
                    if (openRouterModelInfo.getSupportsReasoning()) {
                        $composer.startReplaceGroup(-1312967866);
                        ComposerKt.sourceInformation($composer, "687@29961L10,688@30046L11,685@29840L264");
                        TextKt.m3142Text4IGK_g("reasoning", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getPrimary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall(), $composer, 6, 0, 65530);
                    } else {
                        $composer.startReplaceGroup(-1342542858);
                    }
                    $composer.endReplaceGroup();
                    ComposerKt.sourceInformationMarkerEnd($composer);
                    ComposerKt.sourceInformationMarkerEnd($composer);
                    $composer.endNode();
                    ComposerKt.sourceInformationMarkerEnd($composer);
                    ComposerKt.sourceInformationMarkerEnd($composer);
                    ComposerKt.sourceInformationMarkerEnd($composer);
                    $composer.endReplaceGroup();
                    if (ComposerKt.isTraceInProgress()) {
                        ComposerKt.traceEventEnd();
                    }
                }
                i = 0;
                final Function1 function3 = $onSelect;
                Object obj = (Function0) new Function0<Unit>() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$ModelPickerDialog$3$1$2$1$2$1$1
                    @Override // kotlin.jvm.functions.Function0
                    public /* bridge */ /* synthetic */ Unit invoke() {
                        invoke2();
                        return Unit.INSTANCE;
                    }

                    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                    public final void invoke2() {
                        function3.invoke(openRouterModelInfo);
                    }
                };
                $composer.updateRememberedValue(obj);
                objRememberedValue = obj;
                ComposerKt.sourceInformationMarkerEnd($composer);
                Modifier modifierM831paddingVpY3zN5 = PaddingKt.m831paddingVpY3zN4(ClickableKt.m296clickableoSLSa3U$default(modifierFillMaxWidth$default, false, null, null, null, (Function0) objRememberedValue, 15, null), Dp.m7582constructorimpl(4), Dp.m7582constructorimpl(10));
                int i8 = i;
                ComposerKt.sourceInformationMarkerStart($composer, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
                MeasurePolicy measurePolicyColumnMeasurePolicy2 = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), $composer, ((i8 >> 3) & 14) | ((i8 >> 3) & 112));
                int i9 = (i8 << 3) & 112;
                ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, i));
                CompositionLocalMap currentCompositionLocalMap2 = $composer.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier($composer, modifierM831paddingVpY3zN5);
                constructor = ComposeUiNode.INSTANCE.getConstructor();
                int i10 = ((i9 << 6) & 896) | 6;
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
                composerM4159constructorimpl = Updater.m4159constructorimpl($composer);
                Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyColumnMeasurePolicy2, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl.getInserting()) {
                }
                composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash2);
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                int i11 = (i10 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance2 = ColumnScopeInstance.INSTANCE;
                int i12 = ((i8 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, -1313883698, "C670@28959L10,668@28849L460:PipelineSettingsScreen.kt#w5368b");
                String id2 = openRouterModelInfo.getId();
                TextStyle bodyMedium2 = MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getBodyMedium();
                if (zMatches) {
                    $composer.startReplaceGroup(-1313730683);
                    ComposerKt.sourceInformation($composer, "672@29096L11");
                    long primary2 = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getPrimary();
                    $composer.endReplaceGroup();
                    j = primary2;
                } else {
                    $composer.startReplaceGroup(-1313612573);
                    ComposerKt.sourceInformation($composer, "674@29215L11");
                    long onSurface2 = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getOnSurface();
                    $composer.endReplaceGroup();
                    j = onSurface2;
                }
                TextKt.m3142Text4IGK_g(id2, (Modifier) null, j, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, bodyMedium2, $composer, 0, 0, 65530);
                if (StringsKt.isBlank(openRouterModelInfo.getName())) {
                    $composer.startReplaceGroup(-1342542858);
                } else {
                    $composer.startReplaceGroup(-1342542858);
                }
                $composer.endReplaceGroup();
                if (openRouterModelInfo.getSupportsReasoning()) {
                    $composer.startReplaceGroup(-1312967866);
                    ComposerKt.sourceInformation($composer, "687@29961L10,688@30046L11,685@29840L264");
                    TextKt.m3142Text4IGK_g("reasoning", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getPrimary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall(), $composer, 6, 0, 65530);
                } else {
                    $composer.startReplaceGroup(-1342542858);
                }
                $composer.endReplaceGroup();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endReplaceGroup();
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
            }
        }));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object ModelPickerDialog$lambda$116$lambda$115$lambda$114$lambda$113$lambda$109(OpenRouterModelInfo it) {
        Intrinsics.checkNotNullParameter(it, "it");
        return it.getId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModelPickerDialog$lambda$105(Function0 $onDismiss, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C698@30298L49:PipelineSettingsScreen.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-184457516, $changed, -1, "com.varun.pocketassistant.ui.ModelPickerDialog.<anonymous> (PipelineSettingsScreen.kt:698)");
            }
            ButtonKt.TextButton($onDismiss, null, false, null, null, null, null, null, null, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$18636087$app_debug(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String ensureModelId(String current, List<OpenRouterModelInfo> list, String fallback) {
        Object obj;
        Object next;
        String normalized = OpenRouterModels.INSTANCE.normalizeId(current);
        if (list.isEmpty()) {
            String str = normalized;
            if (StringsKt.isBlank(str)) {
                str = fallback;
            }
            return str;
        }
        Iterator it = list.iterator();
        do {
            obj = null;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
        } while (!OpenRouterModels.INSTANCE.matches(((OpenRouterModelInfo) next).getId(), normalized));
        OpenRouterModelInfo openRouterModelInfo = (OpenRouterModelInfo) next;
        if (openRouterModelInfo != null) {
            return OpenRouterModels.INSTANCE.normalizeId(openRouterModelInfo.getId());
        }
        for (Object obj2 : list) {
            if (OpenRouterModels.INSTANCE.matches(((OpenRouterModelInfo) obj2).getId(), fallback)) {
                obj = obj2;
                break;
            }
        }
        OpenRouterModelInfo openRouterModelInfo2 = (OpenRouterModelInfo) obj;
        if (openRouterModelInfo2 != null) {
            return OpenRouterModels.INSTANCE.normalizeId(openRouterModelInfo2.getId());
        }
        return OpenRouterModels.INSTANCE.normalizeId(((OpenRouterModelInfo) CollectionsKt.first((List) list)).getId());
    }

    private static final String normalizeModelId(String id) {
        return OpenRouterModels.INSTANCE.normalizeId(id);
    }

    private static final String normalizeTemplate(String value, String str) {
        String trimmed = StringsKt.trim((CharSequence) value).toString();
        if ((trimmed.length() == 0) || Intrinsics.areEqual(trimmed, StringsKt.trim((CharSequence) str).toString())) {
            return "";
        }
        return value;
    }

    /*  JADX ERROR: Type inference failed
        jadx.core.utils.exceptions.JadxOverflowException: Type inference error: updates count limit reached with updateSeq = 11231. Try increasing type updates limit count.
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:79)
        */
    private static final void BatteryOptimizationRow(androidx.compose.runtime.Composer r80, int r81) {
        /*
            Method dump skipped, instruction units count: 1123
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.varun.pocketassistant.ui.PipelineSettingsScreenKt.BatteryOptimizationRow(androidx.compose.runtime.Composer, int):void");
    }

    private static final boolean BatteryOptimizationRow$lambda$124(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void BatteryOptimizationRow$lambda$125(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit BatteryOptimizationRow$lambda$131$lambda$130$lambda$129(RowScope $this_Row, PowerManager $pm, Context $context, MutableState $ignoring$delegate) {
        Intent intent = new Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
        intent.setData(Uri.parse("package:" + $context.getPackageName()));
        try {
            Result.Companion companion = Result.INSTANCE;
            $context.startActivity(intent);
            Result.m8304constructorimpl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.INSTANCE;
            Result.m8304constructorimpl(ResultKt.createFailure(th));
        }
        boolean z = false;
        if ($pm != null && $pm.isIgnoringBatteryOptimizations($context.getPackageName())) {
            z = true;
        }
        BatteryOptimizationRow$lambda$125($ignoring$delegate, z);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:42:0x0196  */
    /* JADX WARN: Code duplicated, block: B:43:0x0199  */
    /* JADX WARN: Code duplicated, block: B:46:0x01ab  */
    /* JADX WARN: Code duplicated, block: B:47:0x01ad  */
    /* JADX WARN: Code duplicated, block: B:58:0x0206  */
    /* JADX WARN: Code duplicated, block: B:59:0x0209  */
    /* JADX WARN: Code duplicated, block: B:62:0x0217  */
    /* JADX WARN: Code duplicated, block: B:63:0x021a  */
    /* JADX WARN: Code duplicated, block: B:74:0x0289  */
    private static final void ModeRow(final ProviderMode selected, final Function1<? super ProviderMode, Unit> function1, Composer $composer, final int $changed) {
        Composer $composer2;
        Function0<ComposeUiNode> function0;
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        Object objRememberedValue;
        boolean z4;
        boolean z5;
        Object objRememberedValue2;
        Composer $composer3 = $composer.startRestartGroup(1492180172);
        ComposerKt.sourceInformation($composer3, "C(ModeRow)N(selected,onSelect)780@33237L21,777@33146L576:PipelineSettingsScreen.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= $composer3.changed(selected.ordinal()) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 32 : 16;
        }
        if (!$composer3.shouldExecute(($dirty & 19) != 18, $dirty & 1)) {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1492180172, $dirty, -1, "com.varun.pocketassistant.ui.ModeRow (PipelineSettingsScreen.kt:776)");
            }
            Modifier modifierHorizontalScroll$default = ScrollKt.horizontalScroll$default(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), ScrollKt.rememberScrollState(0, $composer3, 0, 1), false, null, false, 14, null);
            Arrangement.Horizontal horizontalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(6));
            ComposerKt.sourceInformationMarkerStart($composer3, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            MeasurePolicy measurePolicyRowMeasurePolicy = RowKt.rowMeasurePolicy(horizontalM689spacedBy0680j_4, Alignment.INSTANCE.getTop(), $composer3, ((48 >> 3) & 14) | ((48 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer3, modifierHorizontalScroll$default);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            $composer2 = $composer3;
            int i2 = ((((48 << 3) & 112) << 6) & 896) | 6;
            int $dirty2 = $dirty;
            ComposerKt.sourceInformationMarkerStart($composer3, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer3.startReusableNode();
            if ($composer3.getInserting()) {
                function0 = constructor;
                $composer3.createNode(function0);
            } else {
                function0 = constructor;
                $composer3.useNode();
            }
            Composer composerM4159constructorimpl = Updater.m4159constructorimpl($composer3);
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyRowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl.getInserting()) {
                i = iHashCode;
            } else {
                i = iHashCode;
                if (!Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(i))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
                int i3 = (i2 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                RowScopeInstance rowScopeInstance = RowScopeInstance.INSTANCE;
                int i4 = ((48 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer3, 1566703736, "C785@33433L39,783@33337L185,790@33627L39,788@33531L185:PipelineSettingsScreen.kt#w5368b");
                if (selected == ProviderMode.PREFER_CLOUD) {
                    z = true;
                } else {
                    z = false;
                }
                ComposerKt.sourceInformationMarkerStart($composer3, 1020372887, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                if (($dirty2 & 112) == 32) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                z3 = z2;
                objRememberedValue = $composer3.rememberedValue();
                if (!z3 || objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda30
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.ModeRow$lambda$137$lambda$134$lambda$133(function1);
                        }
                    };
                    $composer3.updateRememberedValue(objRememberedValue);
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ChipKt.FilterChip(z, (Function0) objRememberedValue, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8222getLambda$1079695773$app_debug(), null, false, null, null, null, null, null, null, null, $composer3, 384, 0, 4088);
                if (selected == ProviderMode.PREFER_LOCAL) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                ComposerKt.sourceInformationMarkerStart($composer3, 1020379095, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
                if (($dirty2 & 112) == 32) {
                    z5 = true;
                } else {
                    z5 = false;
                }
                objRememberedValue2 = $composer3.rememberedValue();
                if (!z5 || objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                    Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda31
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return PipelineSettingsScreenKt.ModeRow$lambda$137$lambda$136$lambda$135(function1);
                        }
                    };
                    $composer3.updateRememberedValue(obj);
                    objRememberedValue2 = obj;
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ChipKt.FilterChip(z4, (Function0) objRememberedValue2, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$911749594$app_debug(), null, false, null, null, null, null, null, null, null, $composer3, 384, 0, 4088);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                $composer3.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
            }
            composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(i));
            composerM4159constructorimpl.apply(Integer.valueOf(i), setCompositeKeyHash);
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i5 = (i2 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            RowScopeInstance rowScopeInstance2 = RowScopeInstance.INSTANCE;
            int i6 = ((48 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, 1566703736, "C785@33433L39,783@33337L185,790@33627L39,788@33531L185:PipelineSettingsScreen.kt#w5368b");
            if (selected == ProviderMode.PREFER_CLOUD) {
                z = true;
            } else {
                z = false;
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 1020372887, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            if (($dirty2 & 112) == 32) {
                z2 = true;
            } else {
                z2 = false;
            }
            z3 = z2;
            objRememberedValue = $composer3.rememberedValue();
            if (!z3) {
            }
            objRememberedValue = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda30
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return PipelineSettingsScreenKt.ModeRow$lambda$137$lambda$134$lambda$133(function1);
                }
            };
            $composer3.updateRememberedValue(objRememberedValue);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ChipKt.FilterChip(z, (Function0) objRememberedValue, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.m8222getLambda$1079695773$app_debug(), null, false, null, null, null, null, null, null, null, $composer3, 384, 0, 4088);
            if (selected == ProviderMode.PREFER_LOCAL) {
                z4 = true;
            } else {
                z4 = false;
            }
            ComposerKt.sourceInformationMarkerStart($composer3, 1020379095, "CC(remember):PipelineSettingsScreen.kt#9igjgp");
            if (($dirty2 & 112) == 32) {
                z5 = true;
            } else {
                z5 = false;
            }
            objRememberedValue2 = $composer3.rememberedValue();
            if (!z5) {
            }
            Object obj2 = new Function0() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda31
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return PipelineSettingsScreenKt.ModeRow$lambda$137$lambda$136$lambda$135(function1);
                }
            };
            $composer3.updateRememberedValue(obj2);
            objRememberedValue2 = obj2;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ChipKt.FilterChip(z4, (Function0) objRememberedValue2, ComposableSingletons$PipelineSettingsScreenKt.INSTANCE.getLambda$911749594$app_debug(), null, false, null, null, null, null, null, null, null, $composer3, 384, 0, 4088);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.PipelineSettingsScreenKt$$ExternalSyntheticLambda32
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj3, Object obj4) {
                    return PipelineSettingsScreenKt.ModeRow$lambda$138(selected, function1, $changed, (Composer) obj3, ((Integer) obj4).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModeRow$lambda$137$lambda$134$lambda$133(Function1 $onSelect) {
        $onSelect.invoke(ProviderMode.PREFER_CLOUD);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ModeRow$lambda$137$lambda$136$lambda$135(Function1 $onSelect) {
        $onSelect.invoke(ProviderMode.PREFER_LOCAL);
        return Unit.INSTANCE;
    }
}
