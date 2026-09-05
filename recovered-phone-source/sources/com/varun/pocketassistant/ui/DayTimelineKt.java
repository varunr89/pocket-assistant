package com.varun.pocketassistant.ui;

import androidx.autofill.HintConstants;
import androidx.compose.foundation.BackgroundKt;
import androidx.compose.foundation.BorderKt;
import androidx.compose.foundation.ClickableKt;
import androidx.compose.foundation.ScrollKt;
import androidx.compose.foundation.ScrollState;
import androidx.compose.foundation.layout.Arrangement;
import androidx.compose.foundation.layout.BoxKt;
import androidx.compose.foundation.layout.BoxScopeInstance;
import androidx.compose.foundation.layout.BoxWithConstraintsKt;
import androidx.compose.foundation.layout.BoxWithConstraintsScope;
import androidx.compose.foundation.layout.ColumnKt;
import androidx.compose.foundation.layout.ColumnScopeInstance;
import androidx.compose.foundation.layout.OffsetKt;
import androidx.compose.foundation.layout.PaddingKt;
import androidx.compose.foundation.layout.RowKt;
import androidx.compose.foundation.layout.RowScope;
import androidx.compose.foundation.layout.RowScopeInstance;
import androidx.compose.foundation.layout.SizeKt;
import androidx.compose.foundation.layout.SpacerKt;
import androidx.compose.foundation.shape.RoundedCornerShapeKt;
import androidx.compose.material3.ButtonKt;
import androidx.compose.material3.ColorScheme;
import androidx.compose.material3.MaterialTheme;
import androidx.compose.material3.TextKt;
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
import androidx.compose.ui.draw.ClipKt;
import androidx.compose.ui.graphics.Color;
import androidx.compose.ui.input.pointer.PointerInputEventHandler;
import androidx.compose.ui.input.pointer.SuspendingPointerInputFilterKt;
import androidx.compose.ui.layout.MeasurePolicy;
import androidx.compose.ui.node.ComposeUiNode;
import androidx.compose.ui.platform.CompositionLocalsKt;
import androidx.compose.ui.text.TextLayoutResult;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.Constraints;
import androidx.compose.ui.unit.Density;
import androidx.compose.ui.unit.Dp;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.profileinstaller.ProfileVerifier;
import com.varun.pocketassistant.data.MeetingEntity;
import com.varun.pocketassistant.data.SegmentEntity;
import com.varun.pocketassistant.meeting.GapClusterer;
import com.varun.pocketassistant.pipeline.PipelineTelemetry;
import com.varun.pocketassistant.pipeline.work.AsrWorker;
import com.varun.pocketassistant.pipeline.work.MeetingStageWorker;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: DayTimeline.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000z\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\b\u001a¿\u0002\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u000b2\b\u0010\u0011\u001a\u0004\u0018\u00010\t2\b\u0010\u0012\u001a\u0004\u0018\u00010\t2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\b\b\u0002\u0010\u0018\u001a\u00020\u00192\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00070\u001b2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00070\u001b2\u0012\u0010\u001d\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00070\u001b2\u001c\u0010\u001f\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0006\u0012\u0004\u0018\u00010\t\u0012\u0004\u0012\u00020\u00070 2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00070\u001426\u0010\"\u001a2\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b#\u0012\b\b$\u0012\u0004\b\b(%\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b#\u0012\b\b$\u0012\u0004\b\b(&\u0012\u0004\u0012\u00020\u00070 2\b\b\u0002\u0010'\u001a\u00020(H\u0007¢\u0006\u0002\u0010)\u001a9\u0010*\u001a\u00020\u00072\u0006\u0010+\u001a\u00020\u00102\u0006\u0010,\u001a\u00020-2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00070\u00142\f\u0010/\u001a\b\u0012\u0004\u0012\u00020\u00070\u0014H\u0003¢\u0006\u0002\u00100\u001a\u0015\u00101\u001a\u0002022\u0006\u00103\u001a\u00020\fH\u0003¢\u0006\u0002\u00104\u001a\u0010\u00105\u001a\u00020\u001e2\u0006\u00106\u001a\u00020\tH\u0002\u001a\u0010\u00107\u001a\u00020\t2\b\b\u0002\u00108\u001a\u00020\t\"\u0010\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0002\"\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u00069²\u0006\f\u0010:\u001a\u0004\u0018\u00010\tX\u008a\u008e\u0002"}, d2 = {"HOUR_HEIGHT", "Landroidx/compose/ui/unit/Dp;", "F", "DAY_START_HOUR", "", "DAY_END_HOUR", "DayTimelinePanel", "", "dayStartMs", "", "segments", "", "Lcom/varun/pocketassistant/data/SegmentEntity;", "meetings", "Lcom/varun/pocketassistant/data/MeetingEntity;", "proposals", "Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;", "selectionStartMs", "selectionEndMs", "onPrevDay", "Lkotlin/Function0;", "onNextDay", "onToday", "onSuggest", "detecting", "", "onAcceptProposal", "Lkotlin/Function1;", "onDismissProposal", "onOpenMeeting", "", "onSelectionChange", "Lkotlin/Function2;", "onCreateFromSelection", "onAssignSegment", "Lkotlin/ParameterName;", HintConstants.AUTOFILL_HINT_NAME, AsrWorker.KEY_SEGMENT_ID, MeetingStageWorker.KEY_MEETING_ID, "modifier", "Landroidx/compose/ui/Modifier;", "(JLjava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/Long;Ljava/lang/Long;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;ZLkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;Landroidx/compose/ui/Modifier;Landroidx/compose/runtime/Composer;III)V", "ProposalCard", "proposal", "timeFmt", "Ljava/text/DateFormat;", "onAccept", "onDismiss", "(Lcom/varun/pocketassistant/meeting/GapClusterer$Proposal;Ljava/text/DateFormat;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V", "segmentColor", "Landroidx/compose/ui/graphics/Color;", "seg", "(Lcom/varun/pocketassistant/data/SegmentEntity;Landroidx/compose/runtime/Composer;I)J", "formatDuration", "ms", "startOfDayMs", "epochMs", "app_debug", "dragAnchor"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class DayTimelineKt {
    private static final int DAY_END_HOUR = 23;
    private static final int DAY_START_HOUR = 6;
    private static final float HOUR_HEIGHT = Dp.m7582constructorimpl(56);

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DayTimelinePanel$lambda$47(long j, List list, List list2, List list3, Long l, Long l2, Function0 function0, Function0 function1, Function0 function2, Function0 function3, boolean z, Function1 function4, Function1 function5, Function1 function6, Function2 function7, Function0 function8, Function2 function9, Modifier modifier, int i, int i2, int i3, Composer composer, int i4) {
        DayTimelinePanel(j, list, list2, list3, l, l2, function0, function1, function2, function3, z, function4, function5, function6, function7, function8, function9, modifier, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1), RecomposeScopeImplKt.updateChangedFlags(i2), i3);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit ProposalCard$lambda$50(GapClusterer.Proposal proposal, DateFormat dateFormat, Function0 function0, Function0 function1, int i, Composer composer, int i2) {
        ProposalCard(proposal, dateFormat, function0, function1, composer, RecomposeScopeImplKt.updateChangedFlags(i | 1));
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:215:0x07c0  */
    /* JADX WARN: Code duplicated, block: B:218:0x07ea  */
    /* JADX WARN: Code duplicated, block: B:221:0x0818  */
    /* JADX WARN: Code duplicated, block: B:223:0x0830  */
    /* JADX WARN: Code duplicated, block: B:224:0x0832  */
    /* JADX WARN: Code duplicated, block: B:227:0x0842  */
    /* JADX WARN: Code duplicated, block: B:231:0x084e  */
    /* JADX WARN: Code duplicated, block: B:235:0x0873  */
    /* JADX WARN: Code duplicated, block: B:236:0x0875  */
    /* JADX WARN: Code duplicated, block: B:239:0x0885  */
    /* JADX WARN: Code duplicated, block: B:243:0x0893  */
    /* JADX WARN: Code duplicated, block: B:246:0x08e0  */
    /* JADX WARN: Code duplicated, block: B:249:0x0993  */
    /* JADX WARN: Code duplicated, block: B:253:0x0a66  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public static final void DayTimelinePanel(final long dayStartMs, final List<SegmentEntity> segments, final List<MeetingEntity> meetings, final List<GapClusterer.Proposal> proposals, final Long selectionStartMs, final Long selectionEndMs, final Function0<Unit> onPrevDay, final Function0<Unit> onNextDay, final Function0<Unit> onToday, final Function0<Unit> onSuggest, boolean detecting, final Function1<? super GapClusterer.Proposal, Unit> function1, final Function1<? super GapClusterer.Proposal, Unit> onDismissProposal, final Function1<? super String, Unit> onOpenMeeting, final Function2<? super Long, ? super Long, Unit> onSelectionChange, final Function0<Unit> onCreateFromSelection, final Function2<? super String, ? super String, Unit> onAssignSegment, Modifier modifier, Composer $composer, final int $changed, final int $changed1, final int i) {
        int i2;
        Composer $composer2;
        final boolean detecting2;
        final Modifier modifier2;
        final boolean detecting3;
        Modifier.Companion modifier3;
        Object obj;
        Object timeInstance;
        Function0<ComposeUiNode> function0;
        Function0<ComposeUiNode> function2;
        Function0<ComposeUiNode> function3;
        Composer composer;
        Composer composer2;
        Composer composer3;
        Composer composer4;
        Object objRememberedValue;
        List<GapClusterer.Proposal> list;
        boolean z;
        boolean zChangedInstance;
        Object objRememberedValue2;
        Iterable iterable;
        boolean z2;
        boolean zChangedInstance2;
        Composer composer5;
        Object objRememberedValue3;
        final Function1<? super GapClusterer.Proposal, Unit> onAcceptProposal = function1;
        Intrinsics.checkNotNullParameter(segments, "segments");
        Intrinsics.checkNotNullParameter(meetings, "meetings");
        Intrinsics.checkNotNullParameter(proposals, "proposals");
        Intrinsics.checkNotNullParameter(onPrevDay, "onPrevDay");
        Intrinsics.checkNotNullParameter(onNextDay, "onNextDay");
        Intrinsics.checkNotNullParameter(onToday, "onToday");
        Intrinsics.checkNotNullParameter(onSuggest, "onSuggest");
        Intrinsics.checkNotNullParameter(onAcceptProposal, "onAcceptProposal");
        Intrinsics.checkNotNullParameter(onDismissProposal, "onDismissProposal");
        Intrinsics.checkNotNullParameter(onOpenMeeting, "onOpenMeeting");
        Intrinsics.checkNotNullParameter(onSelectionChange, "onSelectionChange");
        Intrinsics.checkNotNullParameter(onCreateFromSelection, "onCreateFromSelection");
        Intrinsics.checkNotNullParameter(onAssignSegment, "onAssignSegment");
        Composer $composer3 = $composer.startRestartGroup(1009426177);
        ComposerKt.sourceInformation($composer3, "C(DayTimelinePanel)N(dayStartMs,segments,meetings,proposals,selectionStartMs,selectionEndMs,onPrevDay,onNextDay,onToday,onSuggest,detecting,onAcceptProposal,onDismissProposal,onOpenMeeting,onSelectionChange,onCreateFromSelection,onAssignSegment,modifier)72@2806L58,73@2883L57,79@3200L9630:DayTimeline.kt#w5368b");
        int $dirty1 = $changed1;
        int $dirty = ($changed & 6) == 0 ? $changed | ($composer3.changed(dayStartMs) ? 4 : 2) : $changed;
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(segments) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            $dirty |= $composer3.changedInstance(meetings) ? 256 : 128;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(proposals) ? 2048 : 1024;
        }
        if (($changed & 24576) == 0) {
            $dirty |= $composer3.changed(selectionStartMs) ? 16384 : 8192;
        }
        if (($changed & ProfileVerifier.CompilationStatus.RESULT_CODE_ERROR_CANT_WRITE_PROFILE_VERIFICATION_RESULT_CACHE_FILE) == 0) {
            $dirty |= $composer3.changed(selectionEndMs) ? 131072 : 65536;
        }
        if (($changed & 1572864) == 0) {
            $dirty |= $composer3.changedInstance(onPrevDay) ? 1048576 : 524288;
        }
        int i3 = 12582912;
        if (($changed & 12582912) == 0) {
            $dirty |= $composer3.changedInstance(onNextDay) ? 8388608 : 4194304;
        }
        if (($changed & 100663296) == 0) {
            $dirty |= $composer3.changedInstance(onToday) ? AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL : 33554432;
        }
        if (($changed & 805306368) == 0) {
            $dirty |= $composer3.changedInstance(onSuggest) ? 536870912 : 268435456;
        }
        int $dirty2 = $dirty;
        int i4 = i & 1024;
        if (i4 != 0) {
            $dirty1 |= 6;
            i2 = $changed1;
        } else {
            i2 = $changed1;
            if ((i2 & 6) == 0) {
                $dirty1 |= $composer3.changed(detecting) ? 4 : 2;
            }
        }
        if ((i2 & 48) == 0) {
            $dirty1 |= $composer3.changedInstance(onAcceptProposal) ? 32 : 16;
        }
        if ((i2 & 384) == 0) {
            $dirty1 |= $composer3.changedInstance(onDismissProposal) ? 256 : 128;
        }
        if ((i2 & 3072) == 0) {
            $dirty1 |= $composer3.changedInstance(onOpenMeeting) ? 2048 : 1024;
        }
        if ((i2 & 24576) == 0) {
            $dirty1 |= $composer3.changedInstance(onSelectionChange) ? 16384 : 8192;
        }
        if ((196608 & i2) == 0) {
            $dirty1 |= $composer3.changedInstance(onCreateFromSelection) ? 131072 : 65536;
        }
        if ((1572864 & i2) == 0) {
            $dirty1 |= $composer3.changedInstance(onAssignSegment) ? 1048576 : 524288;
        }
        int i5 = i & 131072;
        if (i5 != 0) {
            $dirty1 |= i3;
        } else if ((i2 & 12582912) == 0) {
            i3 = $composer3.changed(modifier) ? 8388608 : 4194304;
            $dirty1 |= i3;
        }
        int $dirty3 = $dirty1;
        if ($composer3.shouldExecute((($dirty2 & 306783379) == 306783378 && (4793491 & $dirty3) == 4793490) ? false : true, $dirty2 & 1)) {
            if (i4 != 0) {
                detecting3 = false;
            } else {
                detecting3 = detecting;
            }
            if (i5 == 0) {
                modifier3 = modifier;
            } else {
                modifier3 = Modifier.INSTANCE;
            }
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(1009426177, $dirty2, $dirty3, "com.varun.pocketassistant.ui.DayTimelinePanel (DayTimeline.kt:71)");
            }
            ComposerKt.sourceInformationMarkerStart($composer3, -604029605, "CC(remember):DayTimeline.kt#9igjgp");
            Object objRememberedValue4 = $composer3.rememberedValue();
            if (objRememberedValue4 == Composer.INSTANCE.getEmpty()) {
                Object dateInstance = DateFormat.getDateInstance(2);
                $composer3.updateRememberedValue(dateInstance);
                obj = dateInstance;
            } else {
                obj = objRememberedValue4;
            }
            DateFormat dayFmt = (DateFormat) obj;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerStart($composer3, -604027142, "CC(remember):DayTimeline.kt#9igjgp");
            Object objRememberedValue5 = $composer3.rememberedValue();
            if (objRememberedValue5 == Composer.INSTANCE.getEmpty()) {
                timeInstance = DateFormat.getTimeInstance(3);
                $composer3.updateRememberedValue(timeInstance);
            } else {
                timeInstance = objRememberedValue5;
            }
            DateFormat timeFmt = (DateFormat) timeInstance;
            ComposerKt.sourceInformationMarkerEnd($composer3);
            final long visibleStart = TimeUnit.HOURS.toMillis(6L) + dayStartMs;
            final long visibleEnd = dayStartMs + TimeUnit.HOURS.toMillis(23L);
            final int hours = 17;
            DateFormat timeFmt2 = timeFmt;
            final float totalHeight = Dp.m7582constructorimpl(17 * HOUR_HEIGHT);
            Modifier modifierFillMaxWidth$default = SizeKt.fillMaxWidth$default(modifier3, 0.0f, 1, null);
            ComposerKt.sourceInformationMarkerStart($composer3, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            Modifier modifier4 = modifier3;
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), $composer3, ((0 >> 3) & 14) | ((0 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer3, modifierFillMaxWidth$default);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i6 = ((((0 << 3) & 112) << 6) & 896) | 6;
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
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyColumnMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash);
            }
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i7 = (i6 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer3, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
            int i8 = ((0 >> 6) & 112) | 6;
            Composer composer6 = $composer3;
            ComposerKt.sourceInformationMarkerStart(composer6, -1478454295, "C80@3253L430,90@3692L647,119@4770L29,123@5027L10,124@5083L11,120@4808L314,126@5131L29,128@5183L21,129@5240L7,130@5274L40,137@5575L11,138@5634L7190,131@5323L7501:DayTimeline.kt#w5368b");
            Alignment.Vertical centerVertically = Alignment.INSTANCE.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart(composer6, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            Modifier modifier5 = Modifier.INSTANCE;
            MeasurePolicy measurePolicyRowMeasurePolicy = RowKt.rowMeasurePolicy(Arrangement.INSTANCE.getStart(), centerVertically, composer6, ((384 >> 3) & 14) | ((384 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer6, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer6, 0));
            CompositionLocalMap currentCompositionLocalMap2 = composer6.getCurrentCompositionLocalMap();
            int $dirty4 = $dirty3;
            Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier(composer6, modifier5);
            Function0<ComposeUiNode> constructor2 = ComposeUiNode.INSTANCE.getConstructor();
            int i9 = ((((384 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer6, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer6.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer6.startReusableNode();
            if (composer6.getInserting()) {
                function2 = constructor2;
                composer6.createNode(function2);
            } else {
                function2 = constructor2;
                composer6.useNode();
            }
            Composer composerM4159constructorimpl2 = Updater.m4159constructorimpl(composer6);
            Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyRowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl2.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl2.rememberedValue(), Integer.valueOf(iHashCode2))) {
                composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
                composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash2);
            }
            Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
            int i10 = (i9 >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart(composer6, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            int i11 = ((384 >> 6) & 112) | 6;
            RowScope rowScope = RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart(composer6, 293143142, "C81@3319L45,84@3470L10,82@3377L178,87@3568L47,88@3628L45:DayTimeline.kt#w5368b");
            ButtonKt.TextButton(onPrevDay, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.getLambda$635985752$app_debug(), composer6, (($dirty2 >> 18) & 14) | 805306368, 510);
            String str = dayFmt.format(new Date(dayStartMs));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            TextKt.m3142Text4IGK_g(str, RowScope.weight$default(rowScope, Modifier.INSTANCE, 1.0f, false, 2, null), 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer6, MaterialTheme.$stable).getTitleMedium(), composer6, 0, 0, 65532);
            ButtonKt.TextButton(onToday, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.getLambda$268093455$app_debug(), composer6, (($dirty2 >> 24) & 14) | 805306368, 510);
            ButtonKt.TextButton(onNextDay, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.getLambda$1490696080$app_debug(), composer6, (($dirty2 >> 21) & 14) | 805306368, 510);
            ComposerKt.sourceInformationMarkerEnd(composer6);
            ComposerKt.sourceInformationMarkerEnd(composer6);
            composer6.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer6);
            ComposerKt.sourceInformationMarkerEnd(composer6);
            ComposerKt.sourceInformationMarkerEnd(composer6);
            ComposerKt.sourceInformationMarkerStart(composer6, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            Modifier modifier6 = Modifier.INSTANCE;
            MeasurePolicy measurePolicyRowMeasurePolicy2 = RowKt.rowMeasurePolicy(Arrangement.INSTANCE.getStart(), Alignment.INSTANCE.getTop(), composer6, ((0 >> 3) & 14) | ((0 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart(composer6, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode3 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode(composer6, 0));
            CompositionLocalMap currentCompositionLocalMap3 = composer6.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier3 = ComposedModifierKt.materializeModifier(composer6, modifier6);
            Function0<ComposeUiNode> constructor3 = ComposeUiNode.INSTANCE.getConstructor();
            int i12 = ((((0 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart(composer6, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!(composer6.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            composer6.startReusableNode();
            if (composer6.getInserting()) {
                function3 = constructor3;
                composer6.createNode(function3);
            } else {
                function3 = constructor3;
                composer6.useNode();
            }
            Composer composerM4159constructorimpl3 = Updater.m4159constructorimpl(composer6);
            Updater.m4166setimpl(composerM4159constructorimpl3, measurePolicyRowMeasurePolicy2, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl3, currentCompositionLocalMap3, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash3 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (!composerM4159constructorimpl3.getInserting()) {
                composer = composer6;
                if (!Intrinsics.areEqual(composerM4159constructorimpl3.rememberedValue(), Integer.valueOf(iHashCode3))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl3, modifierMaterializeModifier3, ComposeUiNode.INSTANCE.getSetModifier());
                int i13 = (i12 >> 6) & 14;
                composer2 = composer;
                ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
                int i14 = ((0 >> 6) & 112) | 6;
                RowScope rowScope2 = RowScopeInstance.INSTANCE;
                ComposerKt.sourceInformationMarkerStart(composer2, -1370853402, "C95@3863L89,91@3710L242:DayTimeline.kt#w5368b");
                boolean detecting4 = detecting3;
                ButtonKt.OutlinedButton(onSuggest, RowScope.weight$default(rowScope2, Modifier.INSTANCE, 1.0f, false, 2, null), !detecting3, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(150488196, true, new Function3() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda3
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj2, Object obj3, Object obj4) {
                        return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$4$lambda$3(detecting3, (RowScope) obj2, (Composer) obj3, ((Integer) obj4).intValue());
                    }
                }, composer2, 54), composer2, (($dirty2 >> 27) & 14) | 805306368, 504);
                if (selectionStartMs != null || selectionEndMs == null || Math.abs(selectionEndMs.longValue() - selectionStartMs.longValue()) < PipelineTelemetry.ASR_HTTP_WRITE_TIMEOUT_MS) {
                    composer2.startReplaceGroup(-1374553904);
                } else {
                    composer2.startReplaceGroup(-1370475730);
                    ComposerKt.sourceInformation(composer2, "101@4121L28,102@4166L149");
                    SpacerKt.Spacer(SizeKt.m887width3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer2, 6);
                    ButtonKt.OutlinedButton(onCreateFromSelection, RowScope.weight$default(rowScope2, Modifier.INSTANCE, 1.0f, false, 2, null), false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.m8170getLambda$1162321825$app_debug(), composer2, (($dirty4 >> 15) & 14) | 805306368, 508);
                }
                composer2.endReplaceGroup();
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd(composer);
                if (proposals.isEmpty()) {
                    composer3 = composer6;
                    composer3.startReplaceGroup(-1481978965);
                } else {
                    composer6.startReplaceGroup(-1477625046);
                    ComposerKt.sourceInformation(composer6, "108@4390L29,*113@4591L23,114@4648L24,110@4473L218,116@4708L29");
                    SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer6, 6);
                    list = proposals;
                    for (final GapClusterer.Proposal proposal : list) {
                        Intrinsics.checkNotNull(timeFmt2);
                        ComposerKt.sourceInformationMarkerStart(composer6, 492022074, "CC(remember):DayTimeline.kt#9igjgp");
                        if (($dirty4 & 112) == 32) {
                            z = true;
                        } else {
                            z = false;
                        }
                        zChangedInstance = z | $composer3.changedInstance(proposal);
                        Composer composer7 = composer6;
                        objRememberedValue2 = composer7.rememberedValue();
                        if (!zChangedInstance) {
                            iterable = list;
                            if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                            }
                            Function0 function4 = (Function0) objRememberedValue2;
                            ComposerKt.sourceInformationMarkerEnd(composer6);
                            ComposerKt.sourceInformationMarkerStart(composer6, 492023899, "CC(remember):DayTimeline.kt#9igjgp");
                            $dirty4 = $dirty4;
                            if (($dirty4 & 896) == 256) {
                                z2 = true;
                            } else {
                                z2 = false;
                            }
                            zChangedInstance2 = z2 | $composer3.changedInstance(proposal);
                            composer5 = composer6;
                            objRememberedValue3 = composer5.rememberedValue();
                            if (!zChangedInstance2 || objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                                Object obj2 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                                    @Override // kotlin.jvm.functions.Function0
                                    public final Object invoke() {
                                        return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                                    }
                                };
                                composer5.updateRememberedValue(obj2);
                                objRememberedValue3 = obj2;
                            }
                            ComposerKt.sourceInformationMarkerEnd(composer6);
                            DateFormat timeFmt3 = timeFmt2;
                            Composer composer8 = composer6;
                            ProposalCard(proposal, timeFmt3, function4, (Function0) objRememberedValue3, composer8, GapClusterer.Proposal.$stable);
                            SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(6)), composer8, 6);
                            onAcceptProposal = function1;
                            composer6 = composer8;
                            timeFmt2 = timeFmt3;
                            list = iterable;
                        } else {
                            iterable = list;
                        }
                        Object obj3 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda4
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$6$lambda$5(onAcceptProposal, proposal);
                            }
                        };
                        composer7.updateRememberedValue(obj3);
                        objRememberedValue2 = obj3;
                        Function0 function5 = (Function0) objRememberedValue2;
                        ComposerKt.sourceInformationMarkerEnd(composer6);
                        ComposerKt.sourceInformationMarkerStart(composer6, 492023899, "CC(remember):DayTimeline.kt#9igjgp");
                        $dirty4 = $dirty4;
                        if (($dirty4 & 896) == 256) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        zChangedInstance2 = z2 | $composer3.changedInstance(proposal);
                        composer5 = composer6;
                        objRememberedValue3 = composer5.rememberedValue();
                        if (!zChangedInstance2) {
                            Object obj4 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                                @Override // kotlin.jvm.functions.Function0
                                public final Object invoke() {
                                    return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                                }
                            };
                            composer5.updateRememberedValue(obj4);
                            objRememberedValue3 = obj4;
                        } else {
                            Object obj5 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                                @Override // kotlin.jvm.functions.Function0
                                public final Object invoke() {
                                    return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                                }
                            };
                            composer5.updateRememberedValue(obj5);
                            objRememberedValue3 = obj5;
                        }
                        ComposerKt.sourceInformationMarkerEnd(composer6);
                        DateFormat timeFmt4 = timeFmt2;
                        Composer composer9 = composer6;
                        ProposalCard(proposal, timeFmt4, function5, (Function0) objRememberedValue3, composer9, GapClusterer.Proposal.$stable);
                        SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(6)), composer9, 6);
                        onAcceptProposal = function1;
                        composer6 = composer9;
                        timeFmt2 = timeFmt4;
                        list = iterable;
                    }
                    composer3 = composer6;
                }
                composer3.endReplaceGroup();
                SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer3, 6);
                composer4 = composer3;
                TextKt.m3142Text4IGK_g("Drag on the left time gutter to select a range. Tap a meeting band to open it; tap an unassigned clip to attach it to the overlapping meeting.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer3, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer3, MaterialTheme.$stable).getBodySmall(), composer4, 0, 0, 65530);
                SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer4, 6);
                ScrollState scrollStateRememberScrollState = ScrollKt.rememberScrollState(0, composer4, 0, 1);
                ProvidableCompositionLocal<Density> localDensity = CompositionLocalsKt.getLocalDensity();
                ComposerKt.sourceInformationMarkerStart(composer4, 2023513938, "CC(<get-current>):CompositionLocal.kt#9igjgp");
                Object objConsume = composer4.consume(localDensity);
                ComposerKt.sourceInformationMarkerEnd(composer4);
                final Density density = (Density) objConsume;
                ComposerKt.sourceInformationMarkerStart(composer4, 90910399, "CC(remember):DayTimeline.kt#9igjgp");
                objRememberedValue = composer4.rememberedValue();
                if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    Object objMutableStateOf$default = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(null, null, 2, null);
                    composer4.updateRememberedValue(objMutableStateOf$default);
                    objRememberedValue = objMutableStateOf$default;
                }
                final MutableState mutableState = (MutableState) objRememberedValue;
                ComposerKt.sourceInformationMarkerEnd(composer4);
                Modifier modifierClip = ClipKt.clip(ScrollKt.verticalScroll$default(SizeKt.m868height3ABfNKs(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), totalHeight), scrollStateRememberScrollState, false, null, false, 14, null), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(12)));
                long jM2374getSurfaceVariant0d7_KjU = MaterialTheme.INSTANCE.getColorScheme(composer4, MaterialTheme.$stable).getSurfaceVariant();
                $composer2 = $composer3;
                BoxWithConstraintsKt.BoxWithConstraints(BackgroundKt.m255backgroundbw27NRU$default(modifierClip, Color.m4838copywmQWz5c(jM2374getSurfaceVariant0d7_KjU, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2374getSurfaceVariant0d7_KjU) : 0.35f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2374getSurfaceVariant0d7_KjU) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2374getSurfaceVariant0d7_KjU) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2374getSurfaceVariant0d7_KjU) : 0.0f), null, 2, null), null, false, ComposableLambdaKt.rememberComposableLambda(-3715891, true, new Function3() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda6
                    @Override // kotlin.jvm.functions.Function3
                    public final Object invoke(Object obj6, Object obj7, Object obj8) {
                        return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$45(density, hours, meetings, proposals, segments, selectionStartMs, selectionEndMs, visibleStart, visibleEnd, totalHeight, dayStartMs, onSelectionChange, onOpenMeeting, onAssignSegment, mutableState, (BoxWithConstraintsScope) obj6, (Composer) obj7, ((Integer) obj8).intValue());
                    }
                }, composer4, 54), composer4, 3072, 6);
                ComposerKt.sourceInformationMarkerEnd(composer4);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                $composer3.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                ComposerKt.sourceInformationMarkerEnd($composer3);
                if (ComposerKt.isTraceInProgress()) {
                    ComposerKt.traceEventEnd();
                }
                modifier2 = modifier4;
                detecting2 = detecting4;
            } else {
                composer = composer6;
            }
            composerM4159constructorimpl3.updateRememberedValue(Integer.valueOf(iHashCode3));
            composerM4159constructorimpl3.apply(Integer.valueOf(iHashCode3), setCompositeKeyHash3);
            Updater.m4166setimpl(composerM4159constructorimpl3, modifierMaterializeModifier3, ComposeUiNode.INSTANCE.getSetModifier());
            int i15 = (i12 >> 6) & 14;
            composer2 = composer;
            ComposerKt.sourceInformationMarkerStart(composer2, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            int i16 = ((0 >> 6) & 112) | 6;
            RowScope rowScope3 = RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart(composer2, -1370853402, "C95@3863L89,91@3710L242:DayTimeline.kt#w5368b");
            boolean detecting5 = detecting3;
            ButtonKt.OutlinedButton(onSuggest, RowScope.weight$default(rowScope3, Modifier.INSTANCE, 1.0f, false, 2, null), !detecting3, null, null, null, null, null, null, ComposableLambdaKt.rememberComposableLambda(150488196, true, new Function3() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda3
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj6, Object obj7, Object obj8) {
                    return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$4$lambda$3(detecting3, (RowScope) obj6, (Composer) obj7, ((Integer) obj8).intValue());
                }
            }, composer2, 54), composer2, (($dirty2 >> 27) & 14) | 805306368, 504);
            if (selectionStartMs != null) {
                composer2.startReplaceGroup(-1374553904);
            } else {
                composer2.startReplaceGroup(-1374553904);
            }
            composer2.endReplaceGroup();
            ComposerKt.sourceInformationMarkerEnd(composer2);
            ComposerKt.sourceInformationMarkerEnd(composer2);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerEnd(composer);
            if (proposals.isEmpty()) {
                composer6.startReplaceGroup(-1477625046);
                ComposerKt.sourceInformation(composer6, "108@4390L29,*113@4591L23,114@4648L24,110@4473L218,116@4708L29");
                SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer6, 6);
                list = proposals;
                while (r3.hasNext()) {
                    Intrinsics.checkNotNull(timeFmt2);
                    ComposerKt.sourceInformationMarkerStart(composer6, 492022074, "CC(remember):DayTimeline.kt#9igjgp");
                    if (($dirty4 & 112) == 32) {
                        z = true;
                    } else {
                        z = false;
                    }
                    zChangedInstance = z | $composer3.changedInstance(proposal);
                    Composer composer10 = composer6;
                    objRememberedValue2 = composer10.rememberedValue();
                    if (!zChangedInstance) {
                        iterable = list;
                        if (objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                        }
                        Function0 function6 = (Function0) objRememberedValue2;
                        ComposerKt.sourceInformationMarkerEnd(composer6);
                        ComposerKt.sourceInformationMarkerStart(composer6, 492023899, "CC(remember):DayTimeline.kt#9igjgp");
                        $dirty4 = $dirty4;
                        if (($dirty4 & 896) == 256) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        zChangedInstance2 = z2 | $composer3.changedInstance(proposal);
                        composer5 = composer6;
                        objRememberedValue3 = composer5.rememberedValue();
                        if (!zChangedInstance2) {
                            Object obj6 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                                @Override // kotlin.jvm.functions.Function0
                                public final Object invoke() {
                                    return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                                }
                            };
                            composer5.updateRememberedValue(obj6);
                            objRememberedValue3 = obj6;
                        } else {
                            Object obj7 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                                @Override // kotlin.jvm.functions.Function0
                                public final Object invoke() {
                                    return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                                }
                            };
                            composer5.updateRememberedValue(obj7);
                            objRememberedValue3 = obj7;
                        }
                        ComposerKt.sourceInformationMarkerEnd(composer6);
                        DateFormat timeFmt5 = timeFmt2;
                        Composer composer11 = composer6;
                        ProposalCard(proposal, timeFmt5, function6, (Function0) objRememberedValue3, composer11, GapClusterer.Proposal.$stable);
                        SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(6)), composer11, 6);
                        onAcceptProposal = function1;
                        composer6 = composer11;
                        timeFmt2 = timeFmt5;
                        list = iterable;
                    } else {
                        iterable = list;
                    }
                    Object obj8 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda4
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$6$lambda$5(onAcceptProposal, proposal);
                        }
                    };
                    composer10.updateRememberedValue(obj8);
                    objRememberedValue2 = obj8;
                    Function0 function7 = (Function0) objRememberedValue2;
                    ComposerKt.sourceInformationMarkerEnd(composer6);
                    ComposerKt.sourceInformationMarkerStart(composer6, 492023899, "CC(remember):DayTimeline.kt#9igjgp");
                    $dirty4 = $dirty4;
                    if (($dirty4 & 896) == 256) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    zChangedInstance2 = z2 | $composer3.changedInstance(proposal);
                    composer5 = composer6;
                    objRememberedValue3 = composer5.rememberedValue();
                    if (!zChangedInstance2) {
                        Object obj9 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                            }
                        };
                        composer5.updateRememberedValue(obj9);
                        objRememberedValue3 = obj9;
                    } else {
                        Object obj10 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda5
                            @Override // kotlin.jvm.functions.Function0
                            public final Object invoke() {
                                return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(onDismissProposal, proposal);
                            }
                        };
                        composer5.updateRememberedValue(obj10);
                        objRememberedValue3 = obj10;
                    }
                    ComposerKt.sourceInformationMarkerEnd(composer6);
                    DateFormat timeFmt6 = timeFmt2;
                    Composer composer12 = composer6;
                    ProposalCard(proposal, timeFmt6, function7, (Function0) objRememberedValue3, composer12, GapClusterer.Proposal.$stable);
                    SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(6)), composer12, 6);
                    onAcceptProposal = function1;
                    composer6 = composer12;
                    timeFmt2 = timeFmt6;
                    list = iterable;
                }
                composer3 = composer6;
            } else {
                composer3 = composer6;
                composer3.startReplaceGroup(-1481978965);
            }
            composer3.endReplaceGroup();
            SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer3, 6);
            composer4 = composer3;
            TextKt.m3142Text4IGK_g("Drag on the left time gutter to select a range. Tap a meeting band to open it; tap an unassigned clip to attach it to the overlapping meeting.", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer3, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer3, MaterialTheme.$stable).getBodySmall(), composer4, 0, 0, 65530);
            SpacerKt.Spacer(SizeKt.m868height3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(8)), composer4, 6);
            ScrollState scrollStateRememberScrollState2 = ScrollKt.rememberScrollState(0, composer4, 0, 1);
            ProvidableCompositionLocal<Density> localDensity2 = CompositionLocalsKt.getLocalDensity();
            ComposerKt.sourceInformationMarkerStart(composer4, 2023513938, "CC(<get-current>):CompositionLocal.kt#9igjgp");
            Object objConsume2 = composer4.consume(localDensity2);
            ComposerKt.sourceInformationMarkerEnd(composer4);
            final Density density2 = (Density) objConsume2;
            ComposerKt.sourceInformationMarkerStart(composer4, 90910399, "CC(remember):DayTimeline.kt#9igjgp");
            objRememberedValue = composer4.rememberedValue();
            if (objRememberedValue == Composer.INSTANCE.getEmpty()) {
                Object objMutableStateOf$default2 = SnapshotStateKt__SnapshotStateKt.mutableStateOf$default(null, null, 2, null);
                composer4.updateRememberedValue(objMutableStateOf$default2);
                objRememberedValue = objMutableStateOf$default2;
            }
            final MutableState mutableState2 = (MutableState) objRememberedValue;
            ComposerKt.sourceInformationMarkerEnd(composer4);
            Modifier modifierClip2 = ClipKt.clip(ScrollKt.verticalScroll$default(SizeKt.m868height3ABfNKs(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), totalHeight), scrollStateRememberScrollState2, false, null, false, 14, null), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(12)));
            long jM2374getSurfaceVariant0d7_KjU2 = MaterialTheme.INSTANCE.getColorScheme(composer4, MaterialTheme.$stable).getSurfaceVariant();
            $composer2 = $composer3;
            BoxWithConstraintsKt.BoxWithConstraints(BackgroundKt.m255backgroundbw27NRU$default(modifierClip2, Color.m4838copywmQWz5c(jM2374getSurfaceVariant0d7_KjU2, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2374getSurfaceVariant0d7_KjU2) : 0.35f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2374getSurfaceVariant0d7_KjU2) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2374getSurfaceVariant0d7_KjU2) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2374getSurfaceVariant0d7_KjU2) : 0.0f), null, 2, null), null, false, ComposableLambdaKt.rememberComposableLambda(-3715891, true, new Function3() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda6
                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(Object obj11, Object obj12, Object obj13) {
                    return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$45(density2, hours, meetings, proposals, segments, selectionStartMs, selectionEndMs, visibleStart, visibleEnd, totalHeight, dayStartMs, onSelectionChange, onOpenMeeting, onAssignSegment, mutableState2, (BoxWithConstraintsScope) obj11, (Composer) obj12, ((Integer) obj13).intValue());
                }
            }, composer4, 54), composer4, 3072, 6);
            ComposerKt.sourceInformationMarkerEnd(composer4);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
            modifier2 = modifier4;
            detecting2 = detecting5;
        } else {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
            detecting2 = detecting;
            modifier2 = modifier;
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda7
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj11, Object obj12) {
                    return DayTimelineKt.DayTimelinePanel$lambda$47(dayStartMs, segments, meetings, proposals, selectionStartMs, selectionEndMs, onPrevDay, onNextDay, onToday, onSuggest, detecting2, function1, onDismissProposal, onOpenMeeting, onSelectionChange, onCreateFromSelection, onAssignSegment, modifier2, $changed, $changed1, i, (Composer) obj11, ((Integer) obj12).intValue());
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DayTimelinePanel$lambda$46$lambda$4$lambda$3(boolean $detecting, RowScope OutlinedButton, Composer $composer, int $changed) {
        Intrinsics.checkNotNullParameter(OutlinedButton, "$this$OutlinedButton");
        ComposerKt.sourceInformation($composer, "C96@3881L57:DayTimeline.kt#w5368b");
        if (!$composer.shouldExecute(($changed & 17) != 16, $changed & 1)) {
            $composer.skipToGroupEnd();
        } else {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(150488196, $changed, -1, "com.varun.pocketassistant.ui.DayTimelinePanel.<anonymous>.<anonymous>.<anonymous> (DayTimeline.kt:96)");
            }
            TextKt.m3142Text4IGK_g($detecting ? "Detecting…" : "Suggest meetings", (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, (TextStyle) null, $composer, 0, 0, 131070);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DayTimelinePanel$lambda$46$lambda$9$lambda$6$lambda$5(Function1 $onAcceptProposal, GapClusterer.Proposal $p) {
        $onAcceptProposal.invoke($p);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DayTimelinePanel$lambda$46$lambda$9$lambda$8$lambda$7(Function1 $onDismissProposal, GapClusterer.Proposal $p) {
        $onDismissProposal.invoke($p);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Long DayTimelinePanel$lambda$46$lambda$11(MutableState<Long> mutableState) {
        return mutableState.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:55:0x03d8  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$PrimitiveArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:596)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public static final Unit DayTimelinePanel$lambda$46$lambda$45(Density $density, int $hours, List $meetings, List $proposals, List $segments, Long $selectionStartMs, Long $selectionEndMs, long $visibleStart, long $visibleEnd, float $totalHeight, long $dayStartMs, Function2 $onSelectionChange, final Function1 $onOpenMeeting, final Function2 $onAssignSegment, MutableState $dragAnchor$delegate, BoxWithConstraintsScope BoxWithConstraints, Composer $composer, int $changed) {
        String str;
        long b;
        float totalPx;
        Object next;
        Function0<ComposeUiNode> function0;
        Function0<ComposeUiNode> function1;
        Function0<ComposeUiNode> function2;
        int h = $hours;
        long j = $visibleStart;
        Composer composer = $composer;
        Intrinsics.checkNotNullParameter(BoxWithConstraints, "$this$BoxWithConstraints");
        ComposerKt.sourceInformation(composer, "C275@11906L893,271@11755L1059:DayTimeline.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= composer.changed(BoxWithConstraints) ? 4 : 2;
        }
        int i = 1;
        if (composer.shouldExecute(($dirty & 19) != 18, $dirty & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-3715891, $dirty, -1, "com.varun.pocketassistant.ui.DayTimelinePanel.<anonymous>.<anonymous> (DayTimeline.kt:139)");
            }
            float widthPx = Constraints.m7535getMaxWidthimpl(BoxWithConstraints.mo716getConstraintsmsEJaDk());
            float hourPx = $density.mo417toPx0680j_4(HOUR_HEIGHT);
            float totalPx2 = h * hourPx;
            composer.startReplaceGroup(724015639);
            ComposerKt.sourceInformation(composer, "*160@6511L11,155@6308L268,164@6707L10,165@6772L11,162@6593L362");
            int h2 = 0;
            while (h2 < h) {
                float top = Dp.m7582constructorimpl(h2 * HOUR_HEIGHT);
                Modifier modifierM868height3ABfNKs = SizeKt.m868height3ABfNKs(SizeKt.fillMaxWidth$default(OffsetKt.m785offsetVpY3zN4$default(Modifier.INSTANCE, 0.0f, top, i, null), 0.0f, i, null), Dp.m7582constructorimpl(1));
                long jM2359getOutlineVariant0d7_KjU = MaterialTheme.INSTANCE.getColorScheme(composer, MaterialTheme.$stable).getOutlineVariant();
                BoxKt.Box(BackgroundKt.m255backgroundbw27NRU$default(modifierM868height3ABfNKs, Color.m4838copywmQWz5c(jM2359getOutlineVariant0d7_KjU, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2359getOutlineVariant0d7_KjU) : 0.5f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2359getOutlineVariant0d7_KjU) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2359getOutlineVariant0d7_KjU) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2359getOutlineVariant0d7_KjU) : 0.0f), null, 2, null), composer, 0);
                StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
                String str2 = String.format("%02d:00", Arrays.copyOf(new Object[]{Integer.valueOf(h2 + 6)}, i));
                Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
                TextKt.m3142Text4IGK_g(str2, PaddingKt.m834paddingqDBjuR0$default(OffsetKt.m785offsetVpY3zN4$default(Modifier.INSTANCE, 0.0f, Dp.m7582constructorimpl(top + Dp.m7582constructorimpl(2)), 1, null), Dp.m7582constructorimpl(4), 0.0f, 0.0f, 0.0f, 14, null), MaterialTheme.INSTANCE.getColorScheme(composer, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer, MaterialTheme.$stable).getLabelSmall(), $composer, 0, 0, 65528);
                h2++;
                h = $hours;
                i = 1;
                composer = $composer;
                $dirty = $dirty;
            }
            Composer composer2 = composer;
            int $dirty2 = i;
            composer2.endReplaceGroup();
            composer2.startReplaceGroup(724040633);
            ComposerKt.sourceInformation(composer2, "*182@7578L11,183@7654L23,176@7226L884");
            List list = $meetings;
            int i2 = 0;
            Iterator it = list.iterator();
            while (true) {
                Iterable iterable = list;
                str = "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh";
                int i3 = i2;
                if (!it.hasNext()) {
                    break;
                }
                final MeetingEntity meetingEntity = (MeetingEntity) it.next();
                float widthPx2 = widthPx;
                float widthPx3 = totalPx2;
                float fDayTimelinePanel$lambda$46$lambda$45$yFor = DayTimelinePanel$lambda$46$lambda$45$yFor(j, $visibleEnd, widthPx3, Math.max(meetingEntity.getStartedAtMs(), j));
                Iterator it2 = it;
                j = $visibleStart;
                Modifier modifierClip = ClipKt.clip(SizeKt.m868height3ABfNKs(SizeKt.m887width3ABfNKs(OffsetKt.m784offsetVpY3zN4(Modifier.INSTANCE, Dp.m7582constructorimpl(48), $density.mo413toDpu2uoSUM(fDayTimelinePanel$lambda$46$lambda$45$yFor)), Dp.m7580boximpl($density.mo413toDpu2uoSUM(widthPx2 * 0.55f)).m7596unboximpl()), Dp.m7580boximpl($density.mo413toDpu2uoSUM(Math.max(DayTimelinePanel$lambda$46$lambda$45$yFor(j, $visibleEnd, widthPx3, Math.min(meetingEntity.getEndedAtMs(), $visibleEnd)) - fDayTimelinePanel$lambda$46$lambda$45$yFor, $density.mo417toPx0680j_4(Dp.m7582constructorimpl(12))))).m7596unboximpl()), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(8)));
                long jM2360getPrimary0d7_KjU = MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getPrimary();
                Modifier modifierM255backgroundbw27NRU$default = BackgroundKt.m255backgroundbw27NRU$default(modifierClip, Color.m4838copywmQWz5c(jM2360getPrimary0d7_KjU, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2360getPrimary0d7_KjU) : 0.18f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2360getPrimary0d7_KjU) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2360getPrimary0d7_KjU) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2360getPrimary0d7_KjU) : 0.0f), null, 2, null);
                ComposerKt.sourceInformationMarkerStart(composer2, -1907472437, "CC(remember):DayTimeline.kt#9igjgp");
                boolean zChanged = composer2.changed($onOpenMeeting) | composer2.changed(meetingEntity);
                Object objRememberedValue = $composer.rememberedValue();
                if (zChanged || objRememberedValue == Composer.INSTANCE.getEmpty()) {
                    Object obj = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda0
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$45$lambda$22$lambda$19$lambda$18($onOpenMeeting, meetingEntity);
                        }
                    };
                    $composer.updateRememberedValue(obj);
                    objRememberedValue = obj;
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                Modifier modifierM830padding3ABfNKs = PaddingKt.m830padding3ABfNKs(ClickableKt.m296clickableoSLSa3U$default(modifierM255backgroundbw27NRU$default, false, null, null, null, (Function0) objRememberedValue, 15, null), Dp.m7582constructorimpl(6));
                ComposerKt.sourceInformationMarkerStart($composer, 1042775818, "CC(Box)N(modifier,contentAlignment,propagateMinConstraints,content)71@3424L131:Box.kt#2w3rfo");
                MeasurePolicy measurePolicyMaybeCachedBoxMeasurePolicy = BoxKt.maybeCachedBoxMeasurePolicy(Alignment.INSTANCE.getTopStart(), false);
                ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
                CompositionLocalMap currentCompositionLocalMap = $composer.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer, modifierM830padding3ABfNKs);
                Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
                int i4 = ((((0 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!($composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                $composer.startReusableNode();
                if ($composer.getInserting()) {
                    function2 = constructor;
                    $composer.createNode(function2);
                } else {
                    function2 = constructor;
                    $composer.useNode();
                }
                Composer composerM4159constructorimpl = Updater.m4159constructorimpl($composer);
                Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyMaybeCachedBoxMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                    composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                    composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash);
                }
                Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
                int i5 = (i4 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer, 1833054614, "C72@3469L9:Box.kt#2w3rfo");
                BoxScopeInstance boxScopeInstance = BoxScopeInstance.INSTANCE;
                int i6 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, 1383881211, "C188@7884L10,191@8050L11,186@7758L334:DayTimeline.kt#w5368b");
                String title = meetingEntity.getTitle();
                if (title == null) {
                    title = "Meeting";
                } else {
                    if (StringsKt.isBlank(title)) {
                        title = null;
                    }
                    if (title == null) {
                        title = "Meeting";
                    }
                }
                TextKt.m3142Text4IGK_g(title, (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getPrimary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, TextOverflow.INSTANCE.m7490getEllipsisgIe3tQ8(), false, 2, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelMedium(), $composer, 0, 3120, 55290);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                list = iterable;
                i2 = i3;
                widthPx = widthPx2;
                it = it2;
                totalPx2 = widthPx3;
                $dirty2 = 1;
            }
            int i7 = $dirty2;
            float widthPx4 = widthPx;
            float totalPx3 = totalPx2;
            composer2.endReplaceGroup();
            composer2.startReplaceGroup(724077555);
            ComposerKt.sourceInformation(composer2, "*206@8728L11,207@8826L11,200@8374L822");
            List<GapClusterer.Proposal> list2 = $proposals;
            for (GapClusterer.Proposal proposal : list2) {
                float fDayTimelinePanel$lambda$46$lambda$45$yFor2 = DayTimelinePanel$lambda$46$lambda$45$yFor(j, $visibleEnd, totalPx3, Math.max(proposal.getStartMs(), j));
                Iterable iterable2 = list2;
                j = $visibleStart;
                Modifier modifierM267borderxT4_qwU = BorderKt.m267borderxT4_qwU(ClipKt.clip(SizeKt.m868height3ABfNKs(SizeKt.m887width3ABfNKs(OffsetKt.m784offsetVpY3zN4(Modifier.INSTANCE, Dp.m7582constructorimpl(48), $density.mo413toDpu2uoSUM(fDayTimelinePanel$lambda$46$lambda$45$yFor2)), Dp.m7580boximpl($density.mo413toDpu2uoSUM(widthPx4 * 0.55f)).m7596unboximpl()), Dp.m7580boximpl($density.mo413toDpu2uoSUM(Math.max(DayTimelinePanel$lambda$46$lambda$45$yFor(j, $visibleEnd, totalPx3, Math.min(proposal.getEndMs(), $visibleEnd)) - fDayTimelinePanel$lambda$46$lambda$45$yFor2, $density.mo417toPx0680j_4(Dp.m7582constructorimpl(12))))).m7596unboximpl()), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(8))), Dp.m7582constructorimpl(1), MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getTertiary(), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(8)));
                long jM2375getTertiary0d7_KjU = MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getTertiary();
                Modifier modifierM830padding3ABfNKs2 = PaddingKt.m830padding3ABfNKs(BackgroundKt.m255backgroundbw27NRU$default(modifierM267borderxT4_qwU, Color.m4838copywmQWz5c(jM2375getTertiary0d7_KjU, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2375getTertiary0d7_KjU) : 0.1f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2375getTertiary0d7_KjU) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2375getTertiary0d7_KjU) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2375getTertiary0d7_KjU) : 0.0f), null, 2, null), Dp.m7582constructorimpl(6));
                ComposerKt.sourceInformationMarkerStart($composer, 1042775818, "CC(Box)N(modifier,contentAlignment,propagateMinConstraints,content)71@3424L131:Box.kt#2w3rfo");
                MeasurePolicy measurePolicyMaybeCachedBoxMeasurePolicy2 = BoxKt.maybeCachedBoxMeasurePolicy(Alignment.INSTANCE.getTopStart(), false);
                ComposerKt.sourceInformationMarkerStart($composer, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
                int iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
                CompositionLocalMap currentCompositionLocalMap2 = $composer.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier($composer, modifierM830padding3ABfNKs2);
                Function0<ComposeUiNode> constructor2 = ComposeUiNode.INSTANCE.getConstructor();
                int i8 = ((((0 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!($composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                $composer.startReusableNode();
                if ($composer.getInserting()) {
                    function1 = constructor2;
                    $composer.createNode(function1);
                } else {
                    function1 = constructor2;
                    $composer.useNode();
                }
                Composer composerM4159constructorimpl2 = Updater.m4159constructorimpl($composer);
                Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyMaybeCachedBoxMeasurePolicy2, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (composerM4159constructorimpl2.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl2.rememberedValue(), Integer.valueOf(iHashCode2))) {
                    composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
                    composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash2);
                }
                Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                int i9 = (i8 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer, 1833054614, "C72@3469L9:Box.kt#2w3rfo");
                BoxScopeInstance boxScopeInstance2 = BoxScopeInstance.INSTANCE;
                int i10 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, -1171667954, "C212@9066L10,213@9135L11,210@8948L230:DayTimeline.kt#w5368b");
                TextKt.m3142Text4IGK_g("Suggested · " + proposal.getSegmentIds().size() + " clips", (Modifier) null, MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable).getTertiary(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall(), $composer, 0, 0, 65530);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                list2 = iterable2;
            }
            composer2.endReplaceGroup();
            composer2.startReplaceGroup(724112986);
            ComposerKt.sourceInformation(composer2, "*239@10323L17,240@10377L281,230@9865L1127");
            List<SegmentEntity> list3 = $segments;
            for (final SegmentEntity segmentEntity : list3) {
                Iterable iterable3 = list3;
                float fDayTimelinePanel$lambda$46$lambda$45$yFor3 = DayTimelinePanel$lambda$46$lambda$45$yFor(j, $visibleEnd, totalPx3, Math.max(segmentEntity.getStartedAtMs(), j));
                j = $visibleStart;
                float fMax = Math.max(DayTimelinePanel$lambda$46$lambda$45$yFor(j, $visibleEnd, totalPx3, Math.min(segmentEntity.getEndedAtMs(), $visibleEnd)) - fDayTimelinePanel$lambda$46$lambda$45$yFor3, $density.mo417toPx0680j_4(Dp.m7582constructorimpl(10)));
                List list4 = $meetings;
                Collection arrayList = new ArrayList();
                for (Object obj2 : list4) {
                    Iterable iterable4 = list4;
                    MeetingEntity meetingEntity2 = (MeetingEntity) obj2;
                    if (((segmentEntity.getStartedAtMs() >= meetingEntity2.getEndedAtMs() || segmentEntity.getEndedAtMs() <= meetingEntity2.getStartedAtMs()) ? 0 : i7) != 0) {
                        arrayList.add(obj2);
                    }
                    list4 = iterable4;
                }
                Iterator it3 = ((List) arrayList).iterator();
                if (it3.hasNext()) {
                    next = it3.next();
                    if (it3.hasNext()) {
                        MeetingEntity meetingEntity3 = (MeetingEntity) next;
                        long j2 = 2;
                        long jAbs = Math.abs(((meetingEntity3.getStartedAtMs() + meetingEntity3.getEndedAtMs()) / j2) - ((segmentEntity.getStartedAtMs() + segmentEntity.getEndedAtMs()) / j2));
                        do {
                            Object next2 = it3.next();
                            MeetingEntity meetingEntity4 = (MeetingEntity) next2;
                            long jAbs2 = Math.abs(((meetingEntity4.getStartedAtMs() + meetingEntity4.getEndedAtMs()) / j2) - ((segmentEntity.getStartedAtMs() + segmentEntity.getEndedAtMs()) / j2));
                            if (jAbs > jAbs2) {
                                next = next2;
                                jAbs = jAbs2;
                            }
                        } while (it3.hasNext());
                    }
                } else {
                    next = null;
                }
                final MeetingEntity meetingEntity5 = (MeetingEntity) next;
                Modifier modifierM255backgroundbw27NRU$default2 = BackgroundKt.m255backgroundbw27NRU$default(ClipKt.clip(SizeKt.m868height3ABfNKs(SizeKt.m887width3ABfNKs(OffsetKt.m784offsetVpY3zN4(Modifier.INSTANCE, Dp.m7580boximpl($density.mo413toDpu2uoSUM(widthPx4 * 0.62f)).m7596unboximpl(), $density.mo413toDpu2uoSUM(fDayTimelinePanel$lambda$46$lambda$45$yFor3)), Dp.m7580boximpl($density.mo413toDpu2uoSUM(widthPx4 * 0.35f)).m7596unboximpl()), Dp.m7580boximpl($density.mo413toDpu2uoSUM(fMax)).m7596unboximpl()), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(6))), segmentColor(segmentEntity, composer2, 0), null, 2, null);
                ComposerKt.sourceInformationMarkerStart(composer2, -331136067, "CC(remember):DayTimeline.kt#9igjgp");
                boolean zChanged2 = composer2.changed(segmentEntity) | composer2.changed($onOpenMeeting) | composer2.changed(meetingEntity5) | composer2.changed($onAssignSegment);
                Object objRememberedValue2 = $composer.rememberedValue();
                if (zChanged2 || objRememberedValue2 == Composer.INSTANCE.getEmpty()) {
                    objRememberedValue2 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda1
                        @Override // kotlin.jvm.functions.Function0
                        public final Object invoke() {
                            return DayTimelineKt.DayTimelinePanel$lambda$46$lambda$45$lambda$39$lambda$37$lambda$36(segmentEntity, $onOpenMeeting, meetingEntity5, $onAssignSegment);
                        }
                    };
                    $composer.updateRememberedValue(objRememberedValue2);
                }
                ComposerKt.sourceInformationMarkerEnd(composer2);
                Modifier modifierM830padding3ABfNKs3 = PaddingKt.m830padding3ABfNKs(ClickableKt.m296clickableoSLSa3U$default(modifierM255backgroundbw27NRU$default2, false, null, null, null, (Function0) objRememberedValue2, 15, null), Dp.m7582constructorimpl(4));
                ComposerKt.sourceInformationMarkerStart($composer, 1042775818, "CC(Box)N(modifier,contentAlignment,propagateMinConstraints,content)71@3424L131:Box.kt#2w3rfo");
                MeasurePolicy measurePolicyMaybeCachedBoxMeasurePolicy3 = BoxKt.maybeCachedBoxMeasurePolicy(Alignment.INSTANCE.getTopStart(), false);
                ComposerKt.sourceInformationMarkerStart($composer, -1159599143, str);
                int iHashCode3 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer, 0));
                String str3 = str;
                CompositionLocalMap currentCompositionLocalMap3 = $composer.getCurrentCompositionLocalMap();
                Modifier modifierMaterializeModifier3 = ComposedModifierKt.materializeModifier($composer, modifierM830padding3ABfNKs3);
                Function0<ComposeUiNode> constructor3 = ComposeUiNode.INSTANCE.getConstructor();
                int i11 = ((((0 << 3) & 112) << 6) & 896) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
                if (!($composer.getApplier() instanceof Applier)) {
                    ComposablesKt.invalidApplier();
                }
                $composer.startReusableNode();
                if ($composer.getInserting()) {
                    function0 = constructor3;
                    $composer.createNode(function0);
                } else {
                    function0 = constructor3;
                    $composer.useNode();
                }
                Composer composerM4159constructorimpl3 = Updater.m4159constructorimpl($composer);
                Updater.m4166setimpl(composerM4159constructorimpl3, measurePolicyMaybeCachedBoxMeasurePolicy3, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
                Updater.m4166setimpl(composerM4159constructorimpl3, currentCompositionLocalMap3, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
                Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash3 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
                if (composerM4159constructorimpl3.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl3.rememberedValue(), Integer.valueOf(iHashCode3))) {
                    composerM4159constructorimpl3.updateRememberedValue(Integer.valueOf(iHashCode3));
                    composerM4159constructorimpl3.apply(Integer.valueOf(iHashCode3), setCompositeKeyHash3);
                }
                Updater.m4166setimpl(composerM4159constructorimpl3, modifierMaterializeModifier3, ComposeUiNode.INSTANCE.getSetModifier());
                int i12 = (i11 >> 6) & 14;
                ComposerKt.sourceInformationMarkerStart($composer, 1833054614, "C72@3469L9:Box.kt#2w3rfo");
                BoxScopeInstance boxScopeInstance3 = BoxScopeInstance.INSTANCE;
                int i13 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart($composer, 480861902, "C250@10847L10,248@10739L235:DayTimeline.kt#w5368b");
                TextKt.m3142Text4IGK_g(formatDuration(segmentEntity.getDurationMs()), (Modifier) null, Color.INSTANCE.m4877getWhite0d7_KjU(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 1, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography($composer, MaterialTheme.$stable).getLabelSmall(), $composer, 384, 3072, 57338);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                $composer.endNode();
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                ComposerKt.sourceInformationMarkerEnd($composer);
                list3 = iterable3;
                str = str3;
            }
            composer2.endReplaceGroup();
            if ($selectionStartMs == null || $selectionEndMs == null) {
                b = $visibleEnd;
                totalPx = totalPx3;
                composer2.startReplaceGroup(963450997);
            } else {
                composer2.startReplaceGroup(974459996);
                ComposerKt.sourceInformation(composer2, "267@11666L11,262@11325L402");
                long a = Math.min($selectionStartMs.longValue(), $selectionEndMs.longValue());
                long b2 = Math.max($selectionStartMs.longValue(), $selectionEndMs.longValue());
                totalPx = totalPx3;
                b = $visibleEnd;
                float top2 = DayTimelinePanel$lambda$46$lambda$45$yFor(j, b, totalPx, Math.max(a, j));
                j = $visibleStart;
                float bottom = DayTimelinePanel$lambda$46$lambda$45$yFor(j, b, totalPx, Math.min(b2, b));
                float bottom2 = 48;
                Modifier modifierM868height3ABfNKs2 = SizeKt.m868height3ABfNKs(SizeKt.m887width3ABfNKs(OffsetKt.m784offsetVpY3zN4(Modifier.INSTANCE, Dp.m7582constructorimpl(bottom2), $density.mo413toDpu2uoSUM(top2)), $density.mo413toDpu2uoSUM(widthPx4 - $density.mo417toPx0680j_4(Dp.m7582constructorimpl(48)))), $density.mo413toDpu2uoSUM(Math.max(bottom - top2, 4.0f)));
                long jM2363getSecondary0d7_KjU = MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getSecondary();
                BoxKt.Box(BackgroundKt.m255backgroundbw27NRU$default(modifierM868height3ABfNKs2, Color.m4838copywmQWz5c(jM2363getSecondary0d7_KjU, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2363getSecondary0d7_KjU) : 0.25f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2363getSecondary0d7_KjU) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2363getSecondary0d7_KjU) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2363getSecondary0d7_KjU) : 0.0f), null, 2, null), composer2, 0);
            }
            composer2.endReplaceGroup();
            Modifier modifierM868height3ABfNKs3 = SizeKt.m868height3ABfNKs(SizeKt.m887width3ABfNKs(Modifier.INSTANCE, Dp.m7582constructorimpl(48)), $totalHeight);
            Long lValueOf = Long.valueOf($dayStartMs);
            ComposerKt.sourceInformationMarkerStart(composer2, 724197642, "CC(remember):DayTimeline.kt#9igjgp");
            boolean zChanged3 = composer2.changed(totalPx) | composer2.changed(j) | composer2.changed(b) | composer2.changed($onSelectionChange);
            Object objRememberedValue3 = $composer.rememberedValue();
            if (zChanged3 || objRememberedValue3 == Composer.INSTANCE.getEmpty()) {
                objRememberedValue3 = new DayTimelineKt$DayTimelinePanel$1$4$7$1($onSelectionChange, totalPx, $visibleStart, b, $dragAnchor$delegate);
                $composer.updateRememberedValue(objRememberedValue3);
            }
            ComposerKt.sourceInformationMarkerEnd(composer2);
            BoxKt.Box(SuspendingPointerInputFilterKt.pointerInput(modifierM868height3ABfNKs3, lValueOf, (PointerInputEventHandler) objRememberedValue3), composer2, 0);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            composer.skipToGroupEnd();
        }
        return Unit.INSTANCE;
    }

    private static final float DayTimelinePanel$lambda$46$lambda$45$yFor(long visibleStart, long visibleEnd, float totalPx, long ms) {
        long clamped = RangesKt.coerceIn(ms, visibleStart, visibleEnd);
        return ((clamped - visibleStart) / (visibleEnd - visibleStart)) * totalPx;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final long DayTimelinePanel$lambda$46$lambda$45$msFor(float totalPx, long visibleStart, long visibleEnd, float y) {
        float t = RangesKt.coerceIn(y / totalPx, 0.0f, 1.0f);
        return ((long) ((visibleEnd - visibleStart) * t)) + visibleStart;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DayTimelinePanel$lambda$46$lambda$45$lambda$22$lambda$19$lambda$18(Function1 $onOpenMeeting, MeetingEntity $m) {
        $onOpenMeeting.invoke($m.getId());
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit DayTimelinePanel$lambda$46$lambda$45$lambda$39$lambda$37$lambda$36(SegmentEntity $seg, Function1 $onOpenMeeting, MeetingEntity $nearestMeeting, Function2 $onAssignSegment) {
        if ($seg.getMeetingId() != null) {
            String meetingId = $seg.getMeetingId();
            Intrinsics.checkNotNull(meetingId);
            $onOpenMeeting.invoke(meetingId);
        } else if ($nearestMeeting != null) {
            $onAssignSegment.invoke($seg.getId(), $nearestMeeting.getId());
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Code duplicated, block: B:75:0x044a  */
    private static final void ProposalCard(final GapClusterer.Proposal proposal, final DateFormat timeFmt, final Function0<Unit> function0, final Function0<Unit> function1, Composer $composer, final int $changed) {
        Function0<Unit> function2;
        Composer $composer2;
        Function0<ComposeUiNode> function3;
        Composer composer;
        Composer $composer3 = $composer.startRestartGroup(-1908276141);
        ComposerKt.sourceInformation($composer3, "C(ProposalCard)N(proposal,timeFmt,onAccept,onDismiss)310@13139L11,306@12995L933:DayTimeline.kt#w5368b");
        int $dirty = $changed;
        if (($changed & 6) == 0) {
            $dirty |= ($changed & 8) == 0 ? $composer3.changed(proposal) : $composer3.changedInstance(proposal) ? 4 : 2;
        }
        if (($changed & 48) == 0) {
            $dirty |= $composer3.changedInstance(timeFmt) ? 32 : 16;
        }
        if (($changed & 384) == 0) {
            function2 = function0;
            $dirty |= $composer3.changedInstance(function2) ? 256 : 128;
        } else {
            function2 = function0;
        }
        if (($changed & 3072) == 0) {
            $dirty |= $composer3.changedInstance(function1) ? 2048 : 1024;
        }
        if ($composer3.shouldExecute(($dirty & 1171) != 1170, $dirty & 1)) {
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventStart(-1908276141, $dirty, -1, "com.varun.pocketassistant.ui.ProposalCard (DayTimeline.kt:305)");
            }
            Modifier modifierClip = ClipKt.clip(SizeKt.fillMaxWidth$default(Modifier.INSTANCE, 0.0f, 1, null), RoundedCornerShapeKt.m1195RoundedCornerShape0680j_4(Dp.m7582constructorimpl(10)));
            long jM2376getTertiaryContainer0d7_KjU = MaterialTheme.INSTANCE.getColorScheme($composer3, MaterialTheme.$stable).getTertiaryContainer();
            Modifier modifierM830padding3ABfNKs = PaddingKt.m830padding3ABfNKs(BackgroundKt.m255backgroundbw27NRU$default(modifierClip, Color.m4838copywmQWz5c(jM2376getTertiaryContainer0d7_KjU, (14 & 1) != 0 ? Color.m4842getAlphaimpl(jM2376getTertiaryContainer0d7_KjU) : 0.5f, (14 & 2) != 0 ? Color.m4846getRedimpl(jM2376getTertiaryContainer0d7_KjU) : 0.0f, (14 & 4) != 0 ? Color.m4845getGreenimpl(jM2376getTertiaryContainer0d7_KjU) : 0.0f, (14 & 8) != 0 ? Color.m4843getBlueimpl(jM2376getTertiaryContainer0d7_KjU) : 0.0f), null, 2, null), Dp.m7582constructorimpl(10));
            Alignment.Vertical centerVertically = Alignment.INSTANCE.getCenterVertically();
            ComposerKt.sourceInformationMarkerStart($composer3, 844473419, "CC(Row)N(modifier,horizontalArrangement,verticalAlignment,content)99@5125L58,100@5188L131:Row.kt#2w3rfo");
            MeasurePolicy measurePolicyRowMeasurePolicy = RowKt.rowMeasurePolicy(Arrangement.INSTANCE.getStart(), centerVertically, $composer3, ((384 >> 3) & 14) | ((384 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, 0));
            CompositionLocalMap currentCompositionLocalMap = $composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier = ComposedModifierKt.materializeModifier($composer3, modifierM830padding3ABfNKs);
            Function0<ComposeUiNode> constructor = ComposeUiNode.INSTANCE.getConstructor();
            int i = ((((384 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer3.startReusableNode();
            if ($composer3.getInserting()) {
                $composer3.createNode(constructor);
            } else {
                $composer3.useNode();
            }
            Composer composerM4159constructorimpl = Updater.m4159constructorimpl($composer3);
            int $dirty2 = $dirty;
            Updater.m4166setimpl(composerM4159constructorimpl, measurePolicyRowMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl, currentCompositionLocalMap, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl.getInserting() || !Intrinsics.areEqual(composerM4159constructorimpl.rememberedValue(), Integer.valueOf(iHashCode))) {
                composerM4159constructorimpl.updateRememberedValue(Integer.valueOf(iHashCode));
                composerM4159constructorimpl.apply(Integer.valueOf(iHashCode), setCompositeKeyHash);
            }
            Updater.m4166setimpl(composerM4159constructorimpl, modifierMaterializeModifier, ComposeUiNode.INSTANCE.getSetModifier());
            int i2 = (i >> 6) & 14;
            ComposerKt.sourceInformationMarkerStart($composer3, 1456264949, "C101@5233L9:Row.kt#2w3rfo");
            int i3 = ((384 >> 6) & 112) | 6;
            RowScope rowScope = RowScopeInstance.INSTANCE;
            ComposerKt.sourceInformationMarkerStart($composer3, -1423379332, "C314@13290L514,325@13813L51,326@13873L49:DayTimeline.kt#w5368b");
            Modifier modifierWeight$default = RowScope.weight$default(rowScope, Modifier.INSTANCE, 1.0f, false, 2, null);
            ComposerKt.sourceInformationMarkerStart($composer3, 1341605231, "CC(Column)N(modifier,verticalArrangement,horizontalAlignment,content)87@4443L61,88@4509L134:Column.kt#2w3rfo");
            MeasurePolicy measurePolicyColumnMeasurePolicy = ColumnKt.columnMeasurePolicy(Arrangement.INSTANCE.getTop(), Alignment.INSTANCE.getStart(), $composer3, ((0 >> 3) & 14) | ((0 >> 3) & 112));
            ComposerKt.sourceInformationMarkerStart($composer3, -1159599143, "CC(Layout)P(!1,2)80@3267L27,83@3433L360:Layout.kt#80mrfh");
            int iHashCode2 = Long.hashCode(ComposablesKt.getCurrentCompositeKeyHashCode($composer3, 0));
            CompositionLocalMap currentCompositionLocalMap2 = $composer3.getCurrentCompositionLocalMap();
            Modifier modifierMaterializeModifier2 = ComposedModifierKt.materializeModifier($composer3, modifierWeight$default);
            Function0<ComposeUiNode> constructor2 = ComposeUiNode.INSTANCE.getConstructor();
            int i4 = ((((0 << 3) & 112) << 6) & 896) | 6;
            ComposerKt.sourceInformationMarkerStart($composer3, -553112988, "CC(ReusableComposeNode)N(factory,update,content)399@15590L9:Composables.kt#9igjgp");
            if (!($composer3.getApplier() instanceof Applier)) {
                ComposablesKt.invalidApplier();
            }
            $composer3.startReusableNode();
            if ($composer3.getInserting()) {
                function3 = constructor2;
                $composer3.createNode(function3);
            } else {
                function3 = constructor2;
                $composer3.useNode();
            }
            Composer composerM4159constructorimpl2 = Updater.m4159constructorimpl($composer3);
            Updater.m4166setimpl(composerM4159constructorimpl2, measurePolicyColumnMeasurePolicy, ComposeUiNode.INSTANCE.getSetMeasurePolicy());
            Updater.m4166setimpl(composerM4159constructorimpl2, currentCompositionLocalMap2, ComposeUiNode.INSTANCE.getSetResolvedCompositionLocals());
            Function2<ComposeUiNode, Integer, Unit> setCompositeKeyHash2 = ComposeUiNode.INSTANCE.getSetCompositeKeyHash();
            if (composerM4159constructorimpl2.getInserting()) {
                composer = $composer3;
            } else {
                composer = $composer3;
                if (!Intrinsics.areEqual(composerM4159constructorimpl2.rememberedValue(), Integer.valueOf(iHashCode2))) {
                }
                Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
                int i5 = (i4 >> 6) & 14;
                Composer composer2 = composer;
                ComposerKt.sourceInformationMarkerStart(composer2, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
                ColumnScopeInstance columnScopeInstance = ColumnScopeInstance.INSTANCE;
                int i6 = ((0 >> 6) & 112) | 6;
                ComposerKt.sourceInformationMarkerStart(composer2, 218033159, "C317@13490L10,315@13343L183,321@13691L10,322@13751L11,319@13539L255:DayTimeline.kt#w5368b");
                $composer2 = $composer3;
                TextKt.m3142Text4IGK_g(timeFmt.format(new Date(proposal.getStartMs())) + " – " + timeFmt.format(new Date(proposal.getEndMs())), (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getTitleSmall(), composer2, 0, 0, 65534);
                TextKt.m3142Text4IGK_g(proposal.getSegmentIds().size() + " clips · " + formatDuration(proposal.getEndMs() - proposal.getStartMs()), (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer2, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer2, MaterialTheme.$stable).getBodySmall(), composer2, 0, 0, 65530);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                ComposerKt.sourceInformationMarkerEnd(composer2);
                composer.endNode();
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd(composer);
                ComposerKt.sourceInformationMarkerEnd(composer);
                ButtonKt.TextButton(function1, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.getLambda$1327585042$app_debug(), $composer3, (($dirty2 >> 9) & 14) | 805306368, 510);
                ButtonKt.TextButton(function2, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.m8171getLambda$229378629$app_debug(), $composer3, (($dirty2 >> 6) & 14) | 805306368, 510);
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
            composerM4159constructorimpl2.updateRememberedValue(Integer.valueOf(iHashCode2));
            composerM4159constructorimpl2.apply(Integer.valueOf(iHashCode2), setCompositeKeyHash2);
            Updater.m4166setimpl(composerM4159constructorimpl2, modifierMaterializeModifier2, ComposeUiNode.INSTANCE.getSetModifier());
            int i7 = (i4 >> 6) & 14;
            Composer composer3 = composer;
            ComposerKt.sourceInformationMarkerStart(composer3, 2093002350, "C89@4557L9:Column.kt#2w3rfo");
            ColumnScopeInstance columnScopeInstance2 = ColumnScopeInstance.INSTANCE;
            int i8 = ((0 >> 6) & 112) | 6;
            ComposerKt.sourceInformationMarkerStart(composer3, 218033159, "C317@13490L10,315@13343L183,321@13691L10,322@13751L11,319@13539L255:DayTimeline.kt#w5368b");
            $composer2 = $composer3;
            TextKt.m3142Text4IGK_g(timeFmt.format(new Date(proposal.getStartMs())) + " – " + timeFmt.format(new Date(proposal.getEndMs())), (Modifier) null, 0L, 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer3, MaterialTheme.$stable).getTitleSmall(), composer3, 0, 0, 65534);
            TextKt.m3142Text4IGK_g(proposal.getSegmentIds().size() + " clips · " + formatDuration(proposal.getEndMs() - proposal.getStartMs()), (Modifier) null, MaterialTheme.INSTANCE.getColorScheme(composer3, MaterialTheme.$stable).getOnSurfaceVariant(), 0L, (FontStyle) null, (FontWeight) null, (FontFamily) null, 0L, (TextDecoration) null, (TextAlign) null, 0L, 0, false, 0, 0, (Function1<? super TextLayoutResult, Unit>) null, MaterialTheme.INSTANCE.getTypography(composer3, MaterialTheme.$stable).getBodySmall(), composer3, 0, 0, 65530);
            ComposerKt.sourceInformationMarkerEnd(composer3);
            ComposerKt.sourceInformationMarkerEnd(composer3);
            composer.endNode();
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerEnd(composer);
            ComposerKt.sourceInformationMarkerEnd(composer);
            ButtonKt.TextButton(function1, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.getLambda$1327585042$app_debug(), $composer3, (($dirty2 >> 9) & 14) | 805306368, 510);
            ButtonKt.TextButton(function2, null, false, null, null, null, null, null, null, ComposableSingletons$DayTimelineKt.INSTANCE.m8171getLambda$229378629$app_debug(), $composer3, (($dirty2 >> 6) & 14) | 805306368, 510);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            $composer3.endNode();
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            ComposerKt.sourceInformationMarkerEnd($composer3);
            if (ComposerKt.isTraceInProgress()) {
                ComposerKt.traceEventEnd();
            }
        } else {
            $composer2 = $composer3;
            $composer2.skipToGroupEnd();
        }
        ScopeUpdateScope scopeUpdateScopeEndRestartGroup = $composer2.endRestartGroup();
        if (scopeUpdateScopeEndRestartGroup != null) {
            scopeUpdateScopeEndRestartGroup.updateScope(new Function2() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(Object obj, Object obj2) {
                    return DayTimelineKt.ProposalCard$lambda$50(proposal, timeFmt, function0, function1, $changed, (Composer) obj, ((Integer) obj2).intValue());
                }
            });
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:14:0x003a  */
    /* JADX WARN: Code duplicated, block: B:24:0x005a  */
    /* JADX WARN: Code duplicated, block: B:28:0x0068  */
    /* JADX WARN: Code duplicated, block: B:29:0x006d  */
    private static final long segmentColor(SegmentEntity seg, Composer $composer, int $changed) {
        long jM2358getOutline0d7_KjU;
        ComposerKt.sourceInformationMarkerStart($composer, 415739675, "C(segmentColor)N(seg)332@14029L11:DayTimeline.kt#w5368b");
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventStart(415739675, $changed, -1, "com.varun.pocketassistant.ui.segmentColor (DayTimeline.kt:331)");
        }
        ColorScheme scheme = MaterialTheme.INSTANCE.getColorScheme($composer, MaterialTheme.$stable);
        switch (seg.getTranscriptStatus()) {
            case "PENDING":
                jM2358getOutline0d7_KjU = scheme.getSecondary();
                break;
            case "READY":
                jM2358getOutline0d7_KjU = scheme.getPrimary();
                break;
            case "PROCESSING":
                jM2358getOutline0d7_KjU = scheme.getSecondary();
                break;
            case "CLEANED":
                jM2358getOutline0d7_KjU = scheme.getPrimary();
                break;
            case "CLEAN_FAILED":
            case "FAILED":
                jM2358getOutline0d7_KjU = scheme.getError();
                break;
            default:
                jM2358getOutline0d7_KjU = scheme.getOutline();
                break;
        }
        if (ComposerKt.isTraceInProgress()) {
            ComposerKt.traceEventEnd();
        }
        ComposerKt.sourceInformationMarkerEnd($composer);
        return jM2358getOutline0d7_KjU;
    }

    private static final String formatDuration(long ms) {
        long totalSec = TimeUnit.MILLISECONDS.toSeconds(RangesKt.coerceAtLeast(ms, 0L));
        long j = 60;
        long m = totalSec / j;
        long s = totalSec % j;
        if (m >= 60) {
            long h = m / j;
            String str = String.format("%dh %02dm", Arrays.copyOf(new Object[]{Long.valueOf(h), Long.valueOf(m % j)}, 2));
            Intrinsics.checkNotNullExpressionValue(str, "format(...)");
            return str;
        }
        String str2 = String.format("%d:%02d", Arrays.copyOf(new Object[]{Long.valueOf(m), Long.valueOf(s)}, 2));
        Intrinsics.checkNotNullExpressionValue(str2, "format(...)");
        return str2;
    }

    public static /* synthetic */ long startOfDayMs$default(long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = System.currentTimeMillis();
        }
        return startOfDayMs(j);
    }

    public static final long startOfDayMs(long epochMs) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(epochMs);
        cal.set(11, 0);
        cal.set(12, 0);
        cal.set(13, 0);
        cal.set(14, 0);
        return cal.getTimeInMillis();
    }
}
