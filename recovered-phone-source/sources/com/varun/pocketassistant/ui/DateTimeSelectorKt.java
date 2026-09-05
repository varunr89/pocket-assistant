package com.varun.pocketassistant.ui;

import android.content.Context;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScope;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.RowKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.RowScopeInstance;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.material3.AndroidAlertDialog_androidKt;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.DatePickerDialog_androidKt;
import androidx.compose.material3.DatePickerKt;
import androidx.compose.material3.DatePickerState;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.TextKt;
import androidx.compose.material3.TimePickerKt;
import androidx.compose.material3.TimePickerState;
import androidx.compose.runtime.Applier;
import androidx.compose.runtime.ComposablesKt;
import androidx.compose.runtime.Composer;
import androidx.compose.runtime.ComposerKt;
import androidx.compose.runtime.CompositionLocalMap;
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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.internal.ws.WebSocketProtocol;

/* JADX INFO: compiled from: DateTimeSelector.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000J\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a;\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0012\u0010\u0006\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007¢\u0006\u0002\u0010\n\u001a_\u0010\u000b\u001a\u00020\u00012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\r2\u0011\u0010\u000e\u001a\r\u0012\u0004\u0012\u00020\u00010\r¢\u0006\u0002\b\u000f2\u0011\u0010\u0010\u001a\r\u0012\u0004\u0012\u00020\u00010\r¢\u0006\u0002\b\u000f2\u001c\u0010\u0011\u001a\u0018\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00010\u0007¢\u0006\u0002\b\u000f¢\u0006\u0002\b\u0013H\u0003¢\u0006\u0002\u0010\u0014\u001a\u0010\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0005H\u0002\u001a\u0018\u0010\u0017\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0005H\u0002\u001a \u0010\u001a\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cH\u0002¨\u0006\u001e²\u0006\n\u0010\u001f\u001a\u00020 X\u008a\u008e\u0002²\u0006\n\u0010!\u001a\u00020 X\u008a\u008e\u0002"}, d2 = {"DateTimeSelector", "", "label", "", "epochMs", "", "onChange", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/String;JLkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;II)V", "TimePickerDialog", "onDismissRequest", "Lkotlin/Function0;", "confirmButton", "Landroidx/compose/runtime/Composable;", "dismissButton", "content", "Landroidx/compose/foundation/layout/ColumnScope;", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;I)V", "localEpochToUtcDateMillis", "localEpochMs", "combineUtcDateWithLocalTime", "utcDateMillis", "keepTimeFromEpochMs", "withLocalTime", "hour", "", "minute", "app_debug", "showDate", "", "showTime"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class DateTimeSelectorKt {
    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$36(String str, long j, Function1 function1, Modifier modifier, int i, int i2, Composer composer, int i3) {
        DateTimeSelector(str, j, function1, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), i2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit TimePickerDialog$lambda$38(Function0 function0, Function2 function2, Function2 function3, Function3 function4, int i, Composer composer, int i2) {
        TimePickerDialog(function0, function2, function3, function4, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:102:0x054d  */
    /* JADX WARN: Code duplicated, block: B:104:0x058e  */
    /* JADX WARN: Code duplicated, block: B:107:0x05f6  */
    /* JADX WARN: Code duplicated, block: B:110:0x060d  */
    /* JADX WARN: Code duplicated, block: B:112:0x0626  */
    /* JADX WARN: Code duplicated, block: B:113:0x0628  */
    /* JADX WARN: Code duplicated, block: B:116:0x0633  */
    /* JADX WARN: Code duplicated, block: B:120:0x063d  */
    /* JADX WARN: Code duplicated, block: B:123:0x0684  */
    /* JADX WARN: Code duplicated, block: B:126:0x06d3  */
    /* JADX WARN: Code duplicated, block: B:129:0x06e1  */
    /* JADX WARN: Code duplicated, block: B:79:0x0372  */
    /* JADX WARN: Code duplicated, block: B:82:0x037e  */
    /* JADX WARN: Code duplicated, block: B:83:0x0384  */
    /* JADX WARN: Code duplicated, block: B:86:0x03b5  */
    /* JADX WARN: Code duplicated, block: B:89:0x03c8  */
    /* JADX WARN: Code duplicated, block: B:90:0x03cb  */
    /* JADX WARN: Code duplicated, block: B:94:0x0437  */
    /* JADX WARN: Code duplicated, block: B:95:0x044b  */
    /* JADX WARN: Code duplicated, block: B:98:0x04bd  */
    /* JADX WARN: Code duplicated, block: B:99:0x04cf  */
    public static final void DateTimeSelector(final String label, final long epochMs, final Function1<? super Long, Unit> onChange, Modifier modifier, Composer $composer, final int $changed, final int i) {
        Modifier modifier2;
        Composer $composer2;
        final Modifier modifier3;
        Modifier modifier4;
        int i2;
        String str;
        int $dirty;
        int iHashCode;
        Function0<ComposeUiNode> constructor;
        Composer composerM4159constructorimpl;
        int i3;
        Object objRememberedValue;
        final MutableState showDate$delegate;
        final MutableState showDate$delegate2;
        Object objRememberedValue2;
        final MutableState showTime$delegate;
        String str2;
        final long j;
        final MutableState showTime$delegate2;
        int i4;
        final MutableState showDate$delegate3;
        boolean z;
        boolean z2;
        Composer composer;
        Object objRememberedValue3;
        Object objRememberedValue4;
        Object objRememberedValue5;
        Intrinsics.checkNotNullParameter(label, "label");
        Intrinsics.checkNotNullParameter(onChange, "onChange");
        Composer $composer3 = $composer.startRestartGroup(-1906143356);
        ComposerKt.sourceInformation($composer3, "C(DateTimeSelector)N(label,epochMs,onChange,modifier)46@1790L7,47@1818L34,48@1873L34,49@1926L58,50@2003L57,52@2066L713:DateTimeSelector.kt#w5368b");
        int $dirty2 = $changed;
        if (($changed & 6) == 0) {
            $dirty2 |= $composer3.changed(label) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty2 |= $composer3.changed(epochMs) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty2 |= $composer3.changedInstance(onChange) ? 256 : 128;
        }
        int i5 = i & 8;
        if (i5 != 0) {
            $dirty2 |= 3072;
            modifier2 = modifier;
        } else if (($changed & 3072) == 0) {
            modifier2 = modifier;
            $dirty2 |= $composer3.changed(modifier2) ? 2048 : 1024;
        } else {
            modifier2 = modifier;
        }
        if (!$composer3.shouldExecute(($dirty2 & 1171) != 1170, $dirty2 & 1)) {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
            modifier3 = modifier2;
        } else {
            if (i5 != 0) {
                modifier4 = Modifier.INSTANCE;
            } else {
                modifier4 = modifier2;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1906143356, $dirty2, -1, "com.varun.pocketassistant.ui.DateTimeSelector (DateTimeSelector.kt:45)");
            }
            ProvidableCompositionLocal<Context> localContext = AndroidCompositionLocals_androidKt.getLocalContext();
            ComposerKt.sourceInformationMarkerStart($composer3, 2023513938, "CC(<get-current>):CompositionLocal.kt#9igjgp");
            Object objConsume = $composer3.consume(localContext);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Context context = (Context) objConsume;
            ComposerKt.sourceInformationMarkerStart($composer3, 2147277990, "CC(remember):DateTimeSelector.kt#9igjgp");
            Object objRememberedValue6 = $composer3.rememberedValue();
            if (objRememberedValue6 == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(false, null, 2, null);
                $composer3.updateRememberedValue(objMutableStateOf$default);
                objRememberedValue6 = objMutableStateOf$default;
            }
            MutableState showDate$delegate4 = (MutableState) objRememberedValue6;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 2147279750, "CC(remember):DateTimeSelector.kt#9igjgp");
            Object objRememberedValue7 = $composer3.rememberedValue();
            if (objRememberedValue7 == Composer.INSTANCE.getEmpty()) {
                i2 = 0;
                Object objMutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(false, null, 2, null);
                $composer3.updateRememberedValue(objMutableStateOf$default2);
                objRememberedValue7 = objMutableStateOf$default2;
            } else {
                i2 = 0;
            }
            MutableState showTime$delegate3 = (MutableState) objRememberedValue7;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 2147281470, "CC(remember):DateTimeSelector.kt#9igjgp");
            Object objRememberedValue8 = $composer3.rememberedValue();
            if (objRememberedValue8 == Composer.INSTANCE.getEmpty()) {
                Object dateInstance = DateFormat.getDateInstance(2);
                $composer3.updateRememberedValue(dateInstance);
                objRememberedValue8 = dateInstance;
            }
            final DateFormat dateFmt = (DateFormat) objRememberedValue8;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, 2147283933, "CC(remember):DateTimeSelector.kt#9igjgp");
            Object objRememberedValue9 = $composer3.rememberedValue();
            if (objRememberedValue9 == Composer.INSTANCE.getEmpty()) {
                Object timeInstance = DateFormat.getTimeInstance(3);
                $composer3.updateRememberedValue(timeInstance);
                objRememberedValue9 = timeInstance;
            }
            final DateFormat timeFmt = (DateFormat) objRememberedValue9;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            Arrangement.Vertical verticalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(8));
            int i6 = (($dirty2 >> 9) & 14) | 48;
            ComposerKt.sourceInformationMarkerStart($composer3, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(verticalM689spacedBy0680j_4, Alignment.INSTANCE.getStart(), $composer3, ((i6 >> 3) & 14) | ((i6 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, i2));
            CompositionLocalMap currentCompositionLocalMap = $composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer3, modifier4);
            Function0<ComposeUiNode> constructor2 = ComposeUiNode.INSTANCE.getConstructor();
            int i7 = ((((i6 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer3.startReusableNode();
            if ($composer3.getInserting()) {
                $composer3.createNode(constructor2);
            } else {
                $composer3.useNode();
            }
            Composer composerM4159constructorimpl2 = Updater.m4159constructorimpl($composer3);
            Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyColumnMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl2.getInserting()) {
                str = "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp";
            } else {
                str = "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp";
                if (!Intrinsics.areEqual(composerM4159constructorimpl2.rememberedValue(), Integer.valueOf(iHashCode2))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
                int i8 = (i7 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer3, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
                int i9 = ((i6 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer3, 488955946, "C53@2188L10,53@2154L56,54@2219L554:DateTimeSelector.kt#w5368b");
                $dirty = $dirty2;
                TextKt.m3142Text4IGK_g(label, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer3, MaterialTheme.$stable).getTitleLarge(), $composer3, $dirty2 & 14, 0, 65534);
                Modifier modifierFillMaxWidth$default = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
                Arrangement.Horizontal horizontalM689spacedBy0680j_4 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(8));
                ComposerKt.sourceInformationMarkerStart($composer3, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
                MeasurePolicy measurePolicyRowMeasurePolicy = RowKt.rowMeasurePolicy(horizontalM689spacedBy0680j_4, Alignment.INSTANCE.getTop(), $composer3, ((54 >> 3) & 14) | ((54 >> 3) & 112));
                ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, 0));
                CompositionLocalMap currentCompositionLocalMap2 = $composer3.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier($composer3, modifierFillMaxWidth$default);
                constructor = ComposeUiNode.INSTANCE.getConstructor();
                int i10 = ((((54 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart($composer3, -553112988, str);
                if (!($composer3.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                $composer3.startReusableNode();
                if ($composer3.getInserting()) {
                    $composer3.createNode(constructor);
                } else {
                    $composer3.useNode();
                }
                composerM4159constructorimpl = Updater.m4159constructorimpl($composer3);
                Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyRowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (!composerM4159constructorimpl.getInserting()) {
                    i3 = 54;
                    if (!Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                    }
                    Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                    int i11 = (i10 >> 6) & 14;
                    ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                    int i12 = ((i3 >> 6) & 112) | 6;
                    RowScope rowScope = RowScopeInstance.INSTANCE;
                    ComposerKt.sourceInformationMarkerStart($composer3, 1385461222, "C59@2405L19,61@2488L67,58@2360L195,65@2613L19,67@2696L67,64@2568L195:DateTimeSelector.kt#w5368b");
                    ComposerKt.sourceInformationMarkerStart($composer3, -370948643, "CC(remember):DateTimeSelector.kt#9igjgp");
                    objRememberedValue = $composer3.rememberedValue();
                    if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                        showDate$delegate = showDate$delegate4;
                        objRememberedValue = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda15
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$9$lambda$8(showDate$delegate);
                            }
                        };
                        $composer3.updateRememberedValue(objRememberedValue);
                    } else {
                        showDate$delegate = showDate$delegate4;
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    showDate$delegate2 = showDate$delegate;
                    ButtonKt.FilledTonalButton((Function0) objRememberedValue, RowScope.weight$default(rowScope, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(973422680, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj, Object obj2, Object obj3) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$10(dateFmt, epochMs, (RowScope) obj, (Composer) obj2, ((Integer) obj3).intValue());
                        }
                    }, $composer3, 54), $composer3, 805306374, 508);
                    ComposerKt.sourceInformationMarkerStart($composer3, -370941987, "CC(remember):DateTimeSelector.kt#9igjgp");
                    objRememberedValue2 = $composer3.rememberedValue();
                    if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                        showTime$delegate = showTime$delegate3;
                        Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda2
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$12$lambda$11(showTime$delegate);
                            }
                        };
                        $composer3.updateRememberedValue(obj);
                        objRememberedValue2 = obj;
                    } else {
                        showTime$delegate = showTime$delegate3;
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ButtonKt.FilledTonalButton((Function0) objRememberedValue2, RowScope.weight$default(rowScope, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-1193073215, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda3
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj2, Object obj3, Object obj4) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$13(timeFmt, epochMs, (RowScope) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                        }
                    }, $composer3, 54), $composer3, 805306374, 508);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    $composer3.endNode();
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    $composer3.endNode();
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    if (DateTimeSelector$lambda$1(showDate$delegate2)) {
                        $composer3.startReplaceGroup(2142106111);
                        ComposerKt.sourceInformation($composer3, "74@2825L110,78@2993L20,79@3043L393,90@3466L93,93@3571L53,77@2944L680");
                        MutableState showTime$delegate4 = showTime$delegate;
                        z = false;
                        final DatePickerState dateState = DatePickerKt.m2482rememberDatePickerStateEU0dCGE(Long.valueOf(localEpochToUtcDateMillis(epochMs)), null, null, 0, null, $composer3, 0, 30);
                        ComposerKt.sourceInformationMarkerStart($composer3, 2147315576, "CC(remember):DateTimeSelector.kt#9igjgp");
                        objRememberedValue5 = $composer3.rememberedValue();
                        if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                            Object obj2 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda4
                                @Override // kotlin.jvm.functions.Function0
                                public final Object invoke() {
                                    return DateTimeSelectorKt.DateTimeSelector$lambda$17$lambda$16(showDate$delegate2);
                                }
                            };
                            $composer3.updateRememberedValue(obj2);
                            objRememberedValue5 = obj2;
                        }
                        ComposerKt.sourceInformationMarkerEnd($composer3);
                        j = epochMs;
                        showDate$delegate3 = showDate$delegate2;
                        i4 = 2139302750;
                        str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                        showTime$delegate2 = showTime$delegate4;
                        DatePickerDialog_androidKt.m2474DatePickerDialogGmEhDVc((Function0) objRememberedValue5, ComposableLambdaKt.rememberComposableLambda(889837367, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda5
                            @Override // kotlin.jvm.functions.Function2
                            public final Object invoke(Object obj3, Object obj4) {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$20(dateState, onChange, epochMs, showDate$delegate2, (Composer) obj3, ((Integer) obj4).intValue());
                            }
                        }, $composer3, 54), null, ComposableLambdaKt.rememberComposableLambda(625189877, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda6
                            @Override // kotlin.jvm.functions.Function2
                            public final Object invoke(Object obj3, Object obj4) {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$23(showDate$delegate3, (Composer) obj3, ((Integer) obj4).intValue());
                            }
                        }, $composer3, 54), null, 0.0f, null, null, ComposableLambdaKt.rememberComposableLambda(-97584576, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda7
                            @Override // kotlin.jvm.functions.Function3
                            public final Object invoke(Object obj3, Object obj4, Object obj5) {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$24(dateState, (ColumnScope) obj3, (Composer) obj4, ((Integer) obj5).intValue());
                            }
                        }, $composer3, 54), $composer3, 100666422, 244);
                        $composer2 = $composer3;
                    } else {
                        str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                        j = epochMs;
                        showTime$delegate2 = showTime$delegate;
                        i4 = 2139302750;
                        showDate$delegate3 = showDate$delegate2;
                        $composer2 = $composer3;
                        z = false;
                        $composer2.startReplaceGroup(2139302750);
                    }
                    $composer2.endReplaceGroup();
                    if (DateTimeSelector$lambda$4(showTime$delegate2)) {
                        $composer2.startReplaceGroup(2142958642);
                        ComposerKt.sourceInformation($composer2, "99@3670L97,102@3792L211,108@4061L20,109@4111L445,123@4586L93,126@4691L53,107@4012L732");
                        String str3 = str2;
                        ComposerKt.sourceInformationMarkerStart($composer2, 2147337317, str3);
                        if (($dirty & 112) == 32) {
                            z2 = true;
                        } else {
                            z2 = z;
                        }
                        composer = $composer2;
                        objRememberedValue3 = composer.rememberedValue();
                        if (!z2 || objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(j);
                            composer.updateRememberedValue(calendar);
                            objRememberedValue3 = calendar;
                        }
                        Calendar cal = (Calendar) objRememberedValue3;
                        ComposerKt.sourceInformationMarkerEnd($composer2);
                        Composer $composer4 = $composer2;
                        final TimePickerState timeState = TimePickerKt.rememberTimePickerState(cal.get(11), cal.get(12), android.text.format.DateFormat.is24HourFormat(context), $composer4, 0, 0);
                        $composer2 = $composer4;
                        ComposerKt.sourceInformationMarkerStart($composer2, 2147349752, str3);
                        objRememberedValue4 = $composer2.rememberedValue();
                        if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                            Object obj3 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda8
                                @Override // kotlin.jvm.functions.Function0
                                public final Object invoke() {
                                    return DateTimeSelectorKt.DateTimeSelector$lambda$28$lambda$27(showTime$delegate2);
                                }
                            };
                            $composer2.updateRememberedValue(obj3);
                            objRememberedValue4 = obj3;
                        }
                        ComposerKt.sourceInformationMarkerEnd($composer2);
                        TimePickerDialog((Function0) objRememberedValue4, ComposableLambdaKt.rememberComposableLambda(-2029697759, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda9
                            @Override // kotlin.jvm.functions.Function2
                            public final Object invoke(Object obj4, Object obj5) {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$31(onChange, j, timeState, showTime$delegate2, (Composer) obj4, ((Integer) obj5).intValue());
                            }
                        }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-343797824, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda16
                            @Override // kotlin.jvm.functions.Function2
                            public final Object invoke(Object obj4, Object obj5) {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$34(showTime$delegate2, (Composer) obj4, ((Integer) obj5).intValue());
                            }
                        }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-1593977649, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda17
                            @Override // kotlin.jvm.functions.Function3
                            public final Object invoke(Object obj4, Object obj5, Object obj6) {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$35(timeState, (ColumnScope) obj4, (Composer) obj5, ((Integer) obj6).intValue());
                            }
                        }, $composer2, 54), $composer2, 3510);
                    } else {
                        $composer2.startReplaceGroup(i4);
                    }
                    $composer2.endReplaceGroup();
                    if (ComposerKt.isTraceInProgress()) {
                        ComposerKt.traceEventEnd();
                    }
                    modifier3 = modifier4;
                } else {
                    i3 = 54;
                }
                composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash2);
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                int i13 = (i10 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                int i14 = ((i3 >> 6) & 112) | 6;
                RowScope rowScope2 = RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart($composer3, 1385461222, "C59@2405L19,61@2488L67,58@2360L195,65@2613L19,67@2696L67,64@2568L195:DateTimeSelector.kt#w5368b");
                ComposerKt.sourceInformationMarkerStart($composer3, -370948643, "CC(remember):DateTimeSelector.kt#9igjgp");
                objRememberedValue = $composer3.rememberedValue();
                if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    showDate$delegate = showDate$delegate4;
                    objRememberedValue = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda15
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$9$lambda$8(showDate$delegate);
                        }
                    };
                    $composer3.updateRememberedValue(objRememberedValue);
                } else {
                    showDate$delegate = showDate$delegate4;
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                showDate$delegate2 = showDate$delegate;
                ButtonKt.FilledTonalButton((Function0) objRememberedValue, RowScope.weight$default(rowScope2, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(973422680, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj4, Object obj5, Object obj6) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$10(dateFmt, epochMs, (RowScope) obj4, (Composer) obj5, ((Integer) obj6).intValue());
                    }
                }, $composer3, 54), $composer3, 805306374, 508);
                ComposerKt.sourceInformationMarkerStart($composer3, -370941987, "CC(remember):DateTimeSelector.kt#9igjgp");
                objRememberedValue2 = $composer3.rememberedValue();
                if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                    showTime$delegate = showTime$delegate3;
                    Object obj4 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$12$lambda$11(showTime$delegate);
                        }
                    };
                    $composer3.updateRememberedValue(obj4);
                    objRememberedValue2 = obj4;
                } else {
                    showTime$delegate = showTime$delegate3;
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ButtonKt.FilledTonalButton((Function0) objRememberedValue2, RowScope.weight$default(rowScope2, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-1193073215, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj5, Object obj6, Object obj7) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$13(timeFmt, epochMs, (RowScope) obj5, (Composer) obj6, ((Integer) obj7).intValue());
                    }
                }, $composer3, 54), $composer3, 805306374, 508);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                $composer3.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                $composer3.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                if (DateTimeSelector$lambda$1(showDate$delegate2)) {
                    $composer3.startReplaceGroup(2142106111);
                    ComposerKt.sourceInformation($composer3, "74@2825L110,78@2993L20,79@3043L393,90@3466L93,93@3571L53,77@2944L680");
                    MutableState showTime$delegate5 = showTime$delegate;
                    z = false;
                    final DatePickerState dateState2 = DatePickerKt.m2482rememberDatePickerStateEU0dCGE(Long.valueOf(localEpochToUtcDateMillis(epochMs)), null, null, 0, null, $composer3, 0, 30);
                    ComposerKt.sourceInformationMarkerStart($composer3, 2147315576, "CC(remember):DateTimeSelector.kt#9igjgp");
                    objRememberedValue5 = $composer3.rememberedValue();
                    if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                        Object obj5 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda4
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$17$lambda$16(showDate$delegate2);
                            }
                        };
                        $composer3.updateRememberedValue(obj5);
                        objRememberedValue5 = obj5;
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    j = epochMs;
                    showDate$delegate3 = showDate$delegate2;
                    i4 = 2139302750;
                    str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                    showTime$delegate2 = showTime$delegate5;
                    DatePickerDialog_androidKt.m2474DatePickerDialogGmEhDVc((Function0) objRememberedValue5, ComposableLambdaKt.rememberComposableLambda(889837367, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda5
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj6, Object obj7) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$20(dateState2, onChange, epochMs, showDate$delegate2, (Composer) obj6, ((Integer) obj7).intValue());
                        }
                    }, $composer3, 54), null, ComposableLambdaKt.rememberComposableLambda(625189877, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda6
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj6, Object obj7) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$23(showDate$delegate3, (Composer) obj6, ((Integer) obj7).intValue());
                        }
                    }, $composer3, 54), null, 0.0f, null, null, ComposableLambdaKt.rememberComposableLambda(-97584576, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda7
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj6, Object obj7, Object obj8) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$24(dateState2, (ColumnScope) obj6, (Composer) obj7, ((Integer) obj8).intValue());
                        }
                    }, $composer3, 54), $composer3, 100666422, 244);
                    $composer2 = $composer3;
                } else {
                    str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                    j = epochMs;
                    showTime$delegate2 = showTime$delegate;
                    i4 = 2139302750;
                    showDate$delegate3 = showDate$delegate2;
                    $composer2 = $composer3;
                    z = false;
                    $composer2.startReplaceGroup(2139302750);
                }
                $composer2.endReplaceGroup();
                if (DateTimeSelector$lambda$4(showTime$delegate2)) {
                    $composer2.startReplaceGroup(2142958642);
                    ComposerKt.sourceInformation($composer2, "99@3670L97,102@3792L211,108@4061L20,109@4111L445,123@4586L93,126@4691L53,107@4012L732");
                    String str4 = str2;
                    ComposerKt.sourceInformationMarkerStart($composer2, 2147337317, str4);
                    if (($dirty & 112) == 32) {
                        z2 = true;
                    } else {
                        z2 = z;
                    }
                    composer = $composer2;
                    objRememberedValue3 = composer.rememberedValue();
                    if (!z2) {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTimeInMillis(j);
                        composer.updateRememberedValue(calendar2);
                        objRememberedValue3 = calendar2;
                    } else {
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTimeInMillis(j);
                        composer.updateRememberedValue(calendar3);
                        objRememberedValue3 = calendar3;
                    }
                    Calendar cal2 = (Calendar) objRememberedValue3;
                    ComposerKt.sourceInformationMarkerEnd($composer2);
                    Composer $composer5 = $composer2;
                    final TimePickerState timeState2 = TimePickerKt.rememberTimePickerState(cal2.get(11), cal2.get(12), android.text.format.DateFormat.is24HourFormat(context), $composer5, 0, 0);
                    $composer2 = $composer5;
                    ComposerKt.sourceInformationMarkerStart($composer2, 2147349752, str4);
                    objRememberedValue4 = $composer2.rememberedValue();
                    if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                        Object obj6 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda8
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$28$lambda$27(showTime$delegate2);
                            }
                        };
                        $composer2.updateRememberedValue(obj6);
                        objRememberedValue4 = obj6;
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer2);
                    TimePickerDialog((Function0) objRememberedValue4, ComposableLambdaKt.rememberComposableLambda(-2029697759, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda9
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj7, Object obj8) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$31(onChange, j, timeState2, showTime$delegate2, (Composer) obj7, ((Integer) obj8).intValue());
                        }
                    }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-343797824, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda16
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj7, Object obj8) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$34(showTime$delegate2, (Composer) obj7, ((Integer) obj8).intValue());
                        }
                    }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-1593977649, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda17
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj7, Object obj8, Object obj9) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$35(timeState2, (ColumnScope) obj7, (Composer) obj8, ((Integer) obj9).intValue());
                        }
                    }, $composer2, 54), $composer2, 3510);
                } else {
                    $composer2.startReplaceGroup(i4);
                }
                $composer2.endReplaceGroup();
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
                modifier3 = modifier4;
            }
            composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
            composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash);
            Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i15 = (i7 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer3, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance2 = ColumnScopeInstance.INSTANCE;
            int i16 = ((i6 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, 488955946, "C53@2188L10,53@2154L56,54@2219L554:DateTimeSelector.kt#w5368b");
            $dirty = $dirty2;
            TextKt.m3142Text4IGK_g(label, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer3, MaterialTheme.$stable).getTitleLarge(), $composer3, $dirty2 & 14, 0, 65534);
            Modifier modifierFillMaxWidth$default2 = SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null);
            Arrangement.Horizontal horizontalM689spacedBy0680j_5 = Arrangement.INSTANCE.m689spacedBy0680j_4(Dp.m7582constructorimpl(8));
            ComposerKt.sourceInformationMarkerStart($composer3, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            MeasurePolicy measurePolicyRowMeasurePolicy2 = RowKt.rowMeasurePolicy(horizontalM689spacedBy0680j_5, Alignment.INSTANCE.getTop(), $composer3, ((54 >> 3) & 14) | ((54 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, 0));
            CompositionLocalMap currentCompositionLocalMap3 = $composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier3 = ComposedModifierKt.materializeModifier($composer3, modifierFillMaxWidth$default2);
            constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i17 = ((((54 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, -553112988, str);
            if (!($composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer3.startReusableNode();
            if ($composer3.getInserting()) {
                $composer3.createNode(constructor);
            } else {
                $composer3.useNode();
            }
            composerM4159constructorimpl = Updater.m4159constructorimpl($composer3);
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyRowMeasurePolicy2, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap3, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash3 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl.getInserting()) {
                i3 = 54;
                if (!Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier3, ComposeUiNode.INSTANCE.getSetModifier());
                int i18 = (i17 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                int i19 = ((i3 >> 6) & 112) | 6;
                RowScope rowScope3 = RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart($composer3, 1385461222, "C59@2405L19,61@2488L67,58@2360L195,65@2613L19,67@2696L67,64@2568L195:DateTimeSelector.kt#w5368b");
                ComposerKt.sourceInformationMarkerStart($composer3, -370948643, "CC(remember):DateTimeSelector.kt#9igjgp");
                objRememberedValue = $composer3.rememberedValue();
                if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    showDate$delegate = showDate$delegate4;
                    objRememberedValue = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda15
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$9$lambda$8(showDate$delegate);
                        }
                    };
                    $composer3.updateRememberedValue(objRememberedValue);
                } else {
                    showDate$delegate = showDate$delegate4;
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                showDate$delegate2 = showDate$delegate;
                ButtonKt.FilledTonalButton((Function0) objRememberedValue, RowScope.weight$default(rowScope3, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(973422680, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda1
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj7, Object obj8, Object obj9) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$10(dateFmt, epochMs, (RowScope) obj7, (Composer) obj8, ((Integer) obj9).intValue());
                    }
                }, $composer3, 54), $composer3, 805306374, 508);
                ComposerKt.sourceInformationMarkerStart($composer3, -370941987, "CC(remember):DateTimeSelector.kt#9igjgp");
                objRememberedValue2 = $composer3.rememberedValue();
                if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                    showTime$delegate = showTime$delegate3;
                    Object obj7 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda2
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$12$lambda$11(showTime$delegate);
                        }
                    };
                    $composer3.updateRememberedValue(obj7);
                    objRememberedValue2 = obj7;
                } else {
                    showTime$delegate = showTime$delegate3;
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ButtonKt.FilledTonalButton((Function0) objRememberedValue2, RowScope.weight$default(rowScope3, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-1193073215, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj8, Object obj9, Object obj10) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$13(timeFmt, epochMs, (RowScope) obj8, (Composer) obj9, ((Integer) obj10).intValue());
                    }
                }, $composer3, 54), $composer3, 805306374, 508);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                $composer3.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                $composer3.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                if (DateTimeSelector$lambda$1(showDate$delegate2)) {
                    $composer3.startReplaceGroup(2142106111);
                    ComposerKt.sourceInformation($composer3, "74@2825L110,78@2993L20,79@3043L393,90@3466L93,93@3571L53,77@2944L680");
                    MutableState showTime$delegate6 = showTime$delegate;
                    z = false;
                    final DatePickerState dateState3 = DatePickerKt.m2482rememberDatePickerStateEU0dCGE(Long.valueOf(localEpochToUtcDateMillis(epochMs)), null, null, 0, null, $composer3, 0, 30);
                    ComposerKt.sourceInformationMarkerStart($composer3, 2147315576, "CC(remember):DateTimeSelector.kt#9igjgp");
                    objRememberedValue5 = $composer3.rememberedValue();
                    if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                        Object obj8 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda4
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$17$lambda$16(showDate$delegate2);
                            }
                        };
                        $composer3.updateRememberedValue(obj8);
                        objRememberedValue5 = obj8;
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer3);
                    j = epochMs;
                    showDate$delegate3 = showDate$delegate2;
                    i4 = 2139302750;
                    str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                    showTime$delegate2 = showTime$delegate6;
                    DatePickerDialog_androidKt.m2474DatePickerDialogGmEhDVc((Function0) objRememberedValue5, ComposableLambdaKt.rememberComposableLambda(889837367, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda5
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj9, Object obj10) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$20(dateState3, onChange, epochMs, showDate$delegate2, (Composer) obj9, ((Integer) obj10).intValue());
                        }
                    }, $composer3, 54), null, ComposableLambdaKt.rememberComposableLambda(625189877, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda6
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj9, Object obj10) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$23(showDate$delegate3, (Composer) obj9, ((Integer) obj10).intValue());
                        }
                    }, $composer3, 54), null, 0.0f, null, null, ComposableLambdaKt.rememberComposableLambda(-97584576, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda7
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj9, Object obj10, Object obj11) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$24(dateState3, (ColumnScope) obj9, (Composer) obj10, ((Integer) obj11).intValue());
                        }
                    }, $composer3, 54), $composer3, 100666422, 244);
                    $composer2 = $composer3;
                } else {
                    str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                    j = epochMs;
                    showTime$delegate2 = showTime$delegate;
                    i4 = 2139302750;
                    showDate$delegate3 = showDate$delegate2;
                    $composer2 = $composer3;
                    z = false;
                    $composer2.startReplaceGroup(2139302750);
                }
                $composer2.endReplaceGroup();
                if (DateTimeSelector$lambda$4(showTime$delegate2)) {
                    $composer2.startReplaceGroup(2142958642);
                    ComposerKt.sourceInformation($composer2, "99@3670L97,102@3792L211,108@4061L20,109@4111L445,123@4586L93,126@4691L53,107@4012L732");
                    String str5 = str2;
                    ComposerKt.sourceInformationMarkerStart($composer2, 2147337317, str5);
                    if (($dirty & 112) == 32) {
                        z2 = true;
                    } else {
                        z2 = z;
                    }
                    composer = $composer2;
                    objRememberedValue3 = composer.rememberedValue();
                    if (!z2) {
                        Calendar calendar4 = Calendar.getInstance();
                        calendar4.setTimeInMillis(j);
                        composer.updateRememberedValue(calendar4);
                        objRememberedValue3 = calendar4;
                    } else {
                        Calendar calendar5 = Calendar.getInstance();
                        calendar5.setTimeInMillis(j);
                        composer.updateRememberedValue(calendar5);
                        objRememberedValue3 = calendar5;
                    }
                    Calendar cal3 = (Calendar) objRememberedValue3;
                    ComposerKt.sourceInformationMarkerEnd($composer2);
                    Composer $composer6 = $composer2;
                    final TimePickerState timeState3 = TimePickerKt.rememberTimePickerState(cal3.get(11), cal3.get(12), android.text.format.DateFormat.is24HourFormat(context), $composer6, 0, 0);
                    $composer2 = $composer6;
                    ComposerKt.sourceInformationMarkerStart($composer2, 2147349752, str5);
                    objRememberedValue4 = $composer2.rememberedValue();
                    if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                        Object obj9 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda8
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DateTimeSelectorKt.DateTimeSelector$lambda$28$lambda$27(showTime$delegate2);
                            }
                        };
                        $composer2.updateRememberedValue(obj9);
                        objRememberedValue4 = obj9;
                    }
                    ComposerKt.sourceInformationMarkerEnd($composer2);
                    TimePickerDialog((Function0) objRememberedValue4, ComposableLambdaKt.rememberComposableLambda(-2029697759, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda9
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj10, Object obj11) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$31(onChange, j, timeState3, showTime$delegate2, (Composer) obj10, ((Integer) obj11).intValue());
                        }
                    }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-343797824, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda16
                        @Override // kotlin.jvm.functions.Function2
                        public final Object invoke(Object obj10, Object obj11) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$34(showTime$delegate2, (Composer) obj10, ((Integer) obj11).intValue());
                        }
                    }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-1593977649, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda17
                        @Override // kotlin.jvm.functions.Function3
                        public final Object invoke(Object obj10, Object obj11, Object obj12) {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$35(timeState3, (ColumnScope) obj10, (Composer) obj11, ((Integer) obj12).intValue());
                        }
                    }, $composer2, 54), $composer2, 3510);
                } else {
                    $composer2.startReplaceGroup(i4);
                }
                $composer2.endReplaceGroup();
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
                modifier3 = modifier4;
            } else {
                i3 = 54;
            }
            composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
            composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash3);
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier3, ComposeUiNode.INSTANCE.getSetModifier());
            int i110 = (i17 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            int i111 = ((i3 >> 6) & 112) | 6;
            RowScope rowScope4 = RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart($composer3, 1385461222, "C59@2405L19,61@2488L67,58@2360L195,65@2613L19,67@2696L67,64@2568L195:DateTimeSelector.kt#w5368b");
            ComposerKt.sourceInformationMarkerStart($composer3, -370948643, "CC(remember):DateTimeSelector.kt#9igjgp");
            objRememberedValue = $composer3.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                showDate$delegate = showDate$delegate4;
                objRememberedValue = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda15
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$9$lambda$8(showDate$delegate);
                    }
                };
                $composer3.updateRememberedValue(objRememberedValue);
            } else {
                showDate$delegate = showDate$delegate4;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            showDate$delegate2 = showDate$delegate;
            ButtonKt.FilledTonalButton((Function0) objRememberedValue, RowScope.weight$default(rowScope4, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(973422680, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda1
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj10, Object obj11, Object obj12) {
                    return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$10(dateFmt, epochMs, (RowScope) obj10, (Composer) obj11, ((Integer) obj12).intValue());
                }
            }, $composer3, 54), $composer3, 805306374, 508);
            ComposerKt.sourceInformationMarkerStart($composer3, -370941987, "CC(remember):DateTimeSelector.kt#9igjgp");
            objRememberedValue2 = $composer3.rememberedValue();
            if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                showTime$delegate = showTime$delegate3;
                Object obj10 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$12$lambda$11(showTime$delegate);
                    }
                };
                $composer3.updateRememberedValue(obj10);
                objRememberedValue2 = obj10;
            } else {
                showTime$delegate = showTime$delegate3;
            }
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ButtonKt.FilledTonalButton((Function0) objRememberedValue2, RowScope.weight$default(rowScope4, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(-1193073215, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj11, Object obj12, Object obj13) {
                    return DateTimeSelectorKt.DateTimeSelector$lambda$15$lambda$14$lambda$13(timeFmt, epochMs, (RowScope) obj11, (Composer) obj12, ((Integer) obj13).intValue());
                }
            }, $composer3, 54), $composer3, 805306374, 508);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            if (DateTimeSelector$lambda$1(showDate$delegate2)) {
                $composer3.startReplaceGroup(2142106111);
                ComposerKt.sourceInformation($composer3, "74@2825L110,78@2993L20,79@3043L393,90@3466L93,93@3571L53,77@2944L680");
                MutableState showTime$delegate7 = showTime$delegate;
                z = false;
                final DatePickerState dateState4 = DatePickerKt.m2482rememberDatePickerStateEU0dCGE(Long.valueOf(localEpochToUtcDateMillis(epochMs)), null, null, 0, null, $composer3, 0, 30);
                ComposerKt.sourceInformationMarkerStart($composer3, 2147315576, "CC(remember):DateTimeSelector.kt#9igjgp");
                objRememberedValue5 = $composer3.rememberedValue();
                if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                    Object obj11 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda4
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$17$lambda$16(showDate$delegate2);
                        }
                    };
                    $composer3.updateRememberedValue(obj11);
                    objRememberedValue5 = obj11;
                }
                ComposerKt.sourceInformationMarkerEnd($composer3);
                j = epochMs;
                showDate$delegate3 = showDate$delegate2;
                i4 = 2139302750;
                str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                showTime$delegate2 = showTime$delegate7;
                DatePickerDialog_androidKt.m2474DatePickerDialogGmEhDVc((Function0) objRememberedValue5, ComposableLambdaKt.rememberComposableLambda(889837367, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda5
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj12, Object obj13) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$20(dateState4, onChange, epochMs, showDate$delegate2, (Composer) obj12, ((Integer) obj13).intValue());
                    }
                }, $composer3, 54), null, ComposableLambdaKt.rememberComposableLambda(625189877, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj12, Object obj13) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$23(showDate$delegate3, (Composer) obj12, ((Integer) obj13).intValue());
                    }
                }, $composer3, 54), null, 0.0f, null, null, ComposableLambdaKt.rememberComposableLambda(-97584576, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda7
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj12, Object obj13, Object obj14) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$24(dateState4, (ColumnScope) obj12, (Composer) obj13, ((Integer) obj14).intValue());
                    }
                }, $composer3, 54), $composer3, 100666422, 244);
                $composer2 = $composer3;
            } else {
                str2 = "CC(remember):DateTimeSelector.kt#9igjgp";
                j = epochMs;
                showTime$delegate2 = showTime$delegate;
                i4 = 2139302750;
                showDate$delegate3 = showDate$delegate2;
                $composer2 = $composer3;
                z = false;
                $composer2.startReplaceGroup(2139302750);
            }
            $composer2.endReplaceGroup();
            if (DateTimeSelector$lambda$4(showTime$delegate2)) {
                $composer2.startReplaceGroup(2142958642);
                ComposerKt.sourceInformation($composer2, "99@3670L97,102@3792L211,108@4061L20,109@4111L445,123@4586L93,126@4691L53,107@4012L732");
                String str6 = str2;
                ComposerKt.sourceInformationMarkerStart($composer2, 2147337317, str6);
                if (($dirty & 112) == 32) {
                    z2 = true;
                } else {
                    z2 = z;
                }
                composer = $composer2;
                objRememberedValue3 = composer.rememberedValue();
                if (!z2) {
                    Calendar calendar6 = Calendar.getInstance();
                    calendar6.setTimeInMillis(j);
                    composer.updateRememberedValue(calendar6);
                    objRememberedValue3 = calendar6;
                } else {
                    Calendar calendar7 = Calendar.getInstance();
                    calendar7.setTimeInMillis(j);
                    composer.updateRememberedValue(calendar7);
                    objRememberedValue3 = calendar7;
                }
                Calendar cal4 = (Calendar) objRememberedValue3;
                ComposerKt.sourceInformationMarkerEnd($composer2);
                Composer $composer7 = $composer2;
                final TimePickerState timeState4 = TimePickerKt.rememberTimePickerState(cal4.get(11), cal4.get(12), android.text.format.DateFormat.is24HourFormat(context), $composer7, 0, 0);
                $composer2 = $composer7;
                ComposerKt.sourceInformationMarkerStart($composer2, 2147349752, str6);
                objRememberedValue4 = $composer2.rememberedValue();
                if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                    Object obj12 = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda8
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DateTimeSelectorKt.DateTimeSelector$lambda$28$lambda$27(showTime$delegate2);
                        }
                    };
                    $composer2.updateRememberedValue(obj12);
                    objRememberedValue4 = obj12;
                }
                ComposerKt.sourceInformationMarkerEnd($composer2);
                TimePickerDialog((Function0) objRememberedValue4, ComposableLambdaKt.rememberComposableLambda(-2029697759, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda9
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj13, Object obj14) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$31(onChange, j, timeState4, showTime$delegate2, (Composer) obj13, ((Integer) obj14).intValue());
                    }
                }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-343797824, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda16
                    @Override // kotlin.jvm.functions.Function2
                    public final Object invoke(Object obj13, Object obj14) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$34(showTime$delegate2, (Composer) obj13, ((Integer) obj14).intValue());
                    }
                }, $composer2, 54), ComposableLambdaKt.rememberComposableLambda(-1593977649, true, new Function3() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda17
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj13, Object obj14, Object obj15) {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$35(timeState4, (ColumnScope) obj13, (Composer) obj14, ((Integer) obj15).intValue());
                    }
                }, $composer2, 54), $composer2, 3510);
            } else {
                $composer2.startReplaceGroup(i4);
            }
            $composer2.endReplaceGroup();
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            modifier3 = modifier4;
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda18
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj13, Object obj14) {
                    return DateTimeSelectorKt.DateTimeSelector$lambda$36(label, epochMs, onChange, modifier3, $changed, i, (Composer) obj13, ((Integer) obj14).intValue());
                }
            });
        }
    }

    private static final boolean DateTimeSelector$lambda$1(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void DateTimeSelector$lambda$2(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    private static final boolean DateTimeSelector$lambda$4(MutableState<Boolean> mutableState) {
        return mutableState.getValue().booleanValue();
    }

    private static final void DateTimeSelector$lambda$5(MutableState<Boolean> mutableState, boolean z) {
        mutableState.setValue(Boolean.valueOf(z));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$15$lambda$14$lambda$9$lambda$8(MutableState $showDate$delegate) {
        DateTimeSelector$lambda$2($showDate$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$15$lambda$14$lambda$10(DateFormat $dateFmt, long $epochMs, RowScope FilledTonalButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(FilledTonalButton, "$this$FilledTonalButton");
        ComposerKt.sourceInformation($composer, "C62@2506L35:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(973422680, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous>.<anonymous>.<anonymous> (DateTimeSelector.kt:62)");
            }
            String str = $dateFmt.format(new Date($epochMs));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            TextKt.m3142Text4IGK_g(str, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$15$lambda$14$lambda$12$lambda$11(MutableState $showTime$delegate) {
        DateTimeSelector$lambda$5($showTime$delegate, true);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$15$lambda$14$lambda$13(DateFormat $timeFmt, long $epochMs, RowScope FilledTonalButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(FilledTonalButton, "$this$FilledTonalButton");
        ComposerKt.sourceInformation($composer, "C68@2714L35:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1193073215, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous>.<anonymous>.<anonymous> (DateTimeSelector.kt:68)");
            }
            String str = $timeFmt.format(new Date($epochMs));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            TextKt.m3142Text4IGK_g(str, (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$17$lambda$16(MutableState $showDate$delegate) {
        DateTimeSelector$lambda$2($showDate$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$20(final DatePickerState $dateState, final Function1 $onChange, final long $epochMs, final MutableState $showDate$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C81@3103L285,80@3061L361:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(889837367, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous> (DateTimeSelector.kt:80)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, 435219988, "CC(remember):DateTimeSelector.kt#9igjgp");
            boolean zChanged = $composer.changed($dateState) | $composer.changed($onChange) | $composer.changed($epochMs);
            Object objRememberedValue = $composer.rememberedValue();
            if (zChanged || objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda11
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$20$lambda$19$lambda$18($dateState, $onChange, $epochMs, $showDate$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
                objRememberedValue = obj;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.TextButton((Function0) objRememberedValue, null, false, null, null, null, null, null, null, ComposableSingletons$DateTimeSelectorKt.INSTANCE.m8165getLambda$1882791782$app_debug(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$20$lambda$19$lambda$18(DatePickerState $dateState, Function1 $onChange, long $epochMs, MutableState $showDate$delegate) {
        Long utcDay = $dateState.getSelectedDateMillis();
        if (utcDay != null) {
            $onChange.invoke(Long.valueOf(combineUtcDateWithLocalTime(utcDay.longValue(), $epochMs)));
        }
        DateTimeSelector$lambda$2($showDate$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$23(final MutableState $showDate$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C91@3505L20,91@3484L61:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(625189877, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous> (DateTimeSelector.kt:91)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, -487948375, "CC(remember):DateTimeSelector.kt#9igjgp");
            Object objRememberedValue = $composer.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda14
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$23$lambda$22$lambda$21($showDate$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
                objRememberedValue = obj;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.TextButton((Function0) objRememberedValue, null, false, null, null, null, null, null, null, ComposableSingletons$DateTimeSelectorKt.INSTANCE.m8167getLambda$2147439272$app_debug(), $composer, 805306374, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$23$lambda$22$lambda$21(MutableState $showDate$delegate) {
        DateTimeSelector$lambda$2($showDate$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$24(DatePickerState $dateState, ColumnScope DatePickerDialog, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(DatePickerDialog, "$this$DatePickerDialog");
        ComposerKt.sourceInformation($composer, "C94@3585L29:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-97584576, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous> (DateTimeSelector.kt:94)");
            }
            DatePickerKt.DatePicker($dateState, null, null, null, null, false, null, $composer, 0, WebSocketProtocol.PAYLOAD_SHORT);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$28$lambda$27(MutableState $showTime$delegate) {
        DateTimeSelector$lambda$5($showTime$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$31(final Function1 $onChange, final long $epochMs, final TimePickerState $timeState, final MutableState $showTime$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C111@4171L337,110@4129L413:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-2029697759, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous> (DateTimeSelector.kt:110)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, -624391758, "CC(remember):DateTimeSelector.kt#9igjgp");
            boolean zChanged = $composer.changed($onChange) | $composer.changed($epochMs) | $composer.changedInstance($timeState);
            Object objRememberedValue = $composer.rememberedValue();
            if (zChanged || objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda13
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$31$lambda$30$lambda$29($onChange, $epochMs, $timeState, $showTime$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
                objRememberedValue = obj;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.TextButton((Function0) objRememberedValue, null, false, null, null, null, null, null, null, ComposableSingletons$DateTimeSelectorKt.INSTANCE.getLambda$648380868$app_debug(), $composer, 805306368, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$31$lambda$30$lambda$29(Function1 $onChange, long $epochMs, TimePickerState $timeState, MutableState $showTime$delegate) {
        $onChange.invoke(Long.valueOf(withLocalTime($epochMs, $timeState.getHour(), $timeState.getMinute())));
        DateTimeSelector$lambda$5($showTime$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$34(final MutableState $showTime$delegate, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C124@4625L20,124@4604L61:DateTimeSelector.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-343797824, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous> (DateTimeSelector.kt:124)");
            }
            ComposerKt.sourceInformationMarkerStart($composer, 322774196, "CC(remember):DateTimeSelector.kt#9igjgp");
            Object objRememberedValue = $composer.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda12
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DateTimeSelectorKt.DateTimeSelector$lambda$34$lambda$33$lambda$32($showTime$delegate);
                    }
                };
                $composer.updateRememberedValue(obj);
                objRememberedValue = obj;
            }
            ComposerKt.sourceInformationMarkerEnd($composer);
            ButtonKt.TextButton((Function0) objRememberedValue, null, false, null, null, null, null, null, null, ComposableSingletons$DateTimeSelectorKt.INSTANCE.m8166getLambda$1960686493$app_debug(), $composer, 805306374, 510);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$34$lambda$33$lambda$32(MutableState $showTime$delegate) {
        DateTimeSelector$lambda$5($showTime$delegate, false);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DateTimeSelector$lambda$35(TimePickerState $timeState, ColumnScope TimePickerDialog, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(TimePickerDialog, "$this$TimePickerDialog");
        ComposerKt.sourceInformation($composer, "C127@4705L29:DateTimeSelector.kt#w5368b");
        if ($composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1593977649, $changed, -1, "com.varun.pocketassistant.ui.DateTimeSelector.<anonymous> (DateTimeSelector.kt:127)");
            }
            TimePickerKt.m3169TimePickermT9BvqQ($timeState, null, null, 0, $composer, 0, 14);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    private static final void TimePickerDialog(final Function0<Unit> function0, final Function2<? super Composer, ? super Integer, Unit> function2, final Function2<? super Composer, ? super Integer, Unit> function3, final Function3<? super ColumnScope, ? super Composer, ? super Integer, Unit> function4, Composer $composer, final int $changed) {
        Function0<Unit> function1;
        Function2<? super Composer, ? super Integer, Unit> function5;
        Function2<? super Composer, ? super Integer, Unit> function6;
        Composer $composer2;
        Composer $composer3 = $composer.startRestartGroup(1393311396);
        ComposerKt.sourceInformation($composer3, "C(TimePickerDialog)N(onDismissRequest,confirmButton,dismissButton,content)143@5124L205,139@4973L363:DateTimeSelector.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            function1 = function0;
            $dirty |= $composer3.changedInstance(function1) ? 4 : 2;
        } else {
            function1 = function0;
        }
        if (($changed & 48) == 0) {
            function5 = function2;
            $dirty |= $composer3.changedInstance(function5) ? 32 : 16;
        } else {
            function5 = function2;
        }
        if (($changed & 384) == 0) {
            function6 = function3;
            $dirty |= $composer3.changedInstance(function6) ? 256 : 128;
        } else {
            function6 = function3;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function4) ? 2048 : 1024;
        }
        if (!$composer3.shouldExecute(($dirty & 1171) != 1170, $dirty & 1)) {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1393311396, $dirty, -1, "com.varun.pocketassistant.ui.TimePickerDialog (DateTimeSelector.kt:138)");
            }
            $composer2 = $composer3;
            AndroidAlertDialog_androidKt.m2210AlertDialogOix01E0(function1, function5, null, function6, null, null, ComposableLambdaKt.rememberComposableLambda(1717424695, true, new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda0
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return DateTimeSelectorKt.TimePickerDialog$lambda$37(function4, (Composer) obj, ((Integer) obj2).intValue());
                }
            }, $composer3, 54), null, 0L, 0L, 0L, 0L, 0.0f, null, $composer2, ($dirty & 14) | 1572864 | ($dirty & 112) | (($dirty << 3) & 7168), 0, 16308);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.DateTimeSelectorKt$$ExternalSyntheticLambda10
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return DateTimeSelectorKt.TimePickerDialog$lambda$38(function0, function2, function3, function4, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit TimePickerDialog$lambda$37(Function3 $content, Composer $composer, int $changed) {
        ComposerKt.sourceInformation($composer, "C144@5138L181:DateTimeSelector.kt#w5368b");
        if ($composer.shouldExecute(($changed & 3) != 2, $changed & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1717424695, $changed, -1, "com.varun.pocketassistant.ui.TimePickerDialog.<anonymous> (DateTimeSelector.kt:144)");
            }
            Modifier modifierM834paddingqDBjuR0$default = PaddingKt.m834paddingqDBjuR0$default(Modifier.INSTANCE, 0.0f, Dp.m7582constructorimpl(8), 0.0f, 0.0f, 13, null);
            Alignment.Horizontal centerHorizontally = Alignment.INSTANCE.getCenterHorizontally();
            ComposerKt.sourceInformationMarkerStart($composer, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), centerHorizontally, $composer, ((390 >> 3) & 14) | ((390 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer, modifierM834paddingqDBjuR0$default);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i = ((((390 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer.startReusableNode();
            if ($composer.getInserting()) {
                $composer.createNode(constructor);
            } else {
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
            $content.invoke(ColumnScopeInstance.INSTANCE, $composer, Integer.valueOf(((390 >> 6) & 112) | 6));
            ComposerKt.sourceInformationMarkerEnd($composer);
            $composer.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            ComposerKt.sourceInformationMarkerEnd($composer);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    private static final long localEpochToUtcDateMillis(long localEpochMs) {
        Calendar local = Calendar.getInstance();
        local.setTimeInMillis(localEpochMs);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(1, local.get(1));
        calendar.set(2, local.get(2));
        calendar.set(5, local.get(5));
        return calendar.getTimeInMillis();
    }

    private static final long combineUtcDateWithLocalTime(long utcDateMillis, long keepTimeFromEpochMs) {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(utcDateMillis);
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(keepTimeFromEpochMs);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1, utc.get(1));
        calendar.set(2, utc.get(2));
        calendar.set(5, utc.get(5));
        calendar.set(11, time.get(11));
        calendar.set(12, time.get(12));
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }

    private static final long withLocalTime(long epochMs, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(epochMs);
        calendar.set(11, hour);
        calendar.set(12, minute);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTimeInMillis();
    }
}
