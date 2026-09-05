package com.varun.pocketassistant.ui;

import androidx.compose.foundation.gestures.DragGestureDetectorKt;
import androidx.compose.runtime.MutableState;
import androidx.compose.ui.geometry.Offset;
import androidx.compose.ui.input.pointer.PointerInputChange;
import androidx.compose.ui.input.pointer.PointerInputEventHandler;
import androidx.compose.ui.input.pointer.PointerInputScope;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: DayTimeline.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final class DayTimelineKt$DayTimelinePanel$1$4$7$1 implements PointerInputEventHandler {
    final /* synthetic */ MutableState<Long> $dragAnchor$delegate;
    final /* synthetic */ Function2<Long, Long, Unit> $onSelectionChange;
    final /* synthetic */ float $totalPx;
    final /* synthetic */ long $visibleEnd;
    final /* synthetic */ long $visibleStart;

    /* JADX WARN: Multi-variable type inference failed */
    DayTimelineKt$DayTimelinePanel$1$4$7$1(Function2<? super Long, ? super Long, Unit> function2, float f, long j, long j2, MutableState<Long> mutableState) {
        this.$onSelectionChange = function2;
        this.$totalPx = f;
        this.$visibleStart = j;
        this.$visibleEnd = j2;
        this.$dragAnchor$delegate = mutableState;
    }

    @Override // androidx.compose.ui.input.pointer.PointerInputEventHandler
    public final Object invoke(PointerInputScope $this$pointerInput, Continuation<? super Unit> continuation) {
        final Function2<Long, Long, Unit> function2 = this.$onSelectionChange;
        final float f = this.$totalPx;
        final long j = this.$visibleStart;
        final long j2 = this.$visibleEnd;
        final MutableState<Long> mutableState = this.$dragAnchor$delegate;
        Function1 function1 = new Function1() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$DayTimelinePanel$1$4$7$1$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return DayTimelineKt$DayTimelinePanel$1$4$7$1.invoke$lambda$0(function2, f, j, j2, mutableState, (Offset) obj);
            }
        };
        final MutableState<Long> mutableState2 = this.$dragAnchor$delegate;
        Function0 function0 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$DayTimelinePanel$1$4$7$1$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return DayTimelineKt$DayTimelinePanel$1$4$7$1.invoke$lambda$1(mutableState2);
            }
        };
        final Function2<Long, Long, Unit> function3 = this.$onSelectionChange;
        final MutableState<Long> mutableState3 = this.$dragAnchor$delegate;
        Function0 function4 = new Function0() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$DayTimelinePanel$1$4$7$1$$ExternalSyntheticLambda2
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return DayTimelineKt$DayTimelinePanel$1$4$7$1.invoke$lambda$2(function3, mutableState3);
            }
        };
        final Function2<Long, Long, Unit> function5 = this.$onSelectionChange;
        final MutableState<Long> mutableState4 = this.$dragAnchor$delegate;
        final float f2 = this.$totalPx;
        final long j3 = this.$visibleStart;
        final long j4 = this.$visibleEnd;
        Object objDetectDragGestures = DragGestureDetectorKt.detectDragGestures($this$pointerInput, function1, function0, function4, new Function2() { // from class: com.varun.pocketassistant.ui.DayTimelineKt$DayTimelinePanel$1$4$7$1$$ExternalSyntheticLambda3
            @Override // kotlin.jvm.functions.Function2
            public final Object invoke(Object obj, Object obj2) {
                return DayTimelineKt$DayTimelinePanel$1$4$7$1.invoke$lambda$3(function5, mutableState4, f2, j3, j4, (PointerInputChange) obj, (Offset) obj2);
            }
        }, continuation);
        return objDetectDragGestures == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objDetectDragGestures : Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit invoke$lambda$0(Function2 $onSelectionChange, float $totalPx, long $visibleStart, long $visibleEnd, MutableState $dragAnchor$delegate, Offset offset) {
        long ms = DayTimelineKt.DayTimelinePanel$lambda$46$lambda$45$msFor($totalPx, $visibleStart, $visibleEnd, Float.intBitsToFloat((int) (4294967295L & offset.m4606unboximpl())));
        $dragAnchor$delegate.setValue(Long.valueOf(ms));
        $onSelectionChange.invoke(Long.valueOf(ms), Long.valueOf(ms));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit invoke$lambda$3(Function2 $onSelectionChange, MutableState $dragAnchor$delegate, float $totalPx, long $visibleStart, long $visibleEnd, PointerInputChange change, Offset offset) {
        Intrinsics.checkNotNullParameter(change, "change");
        change.consume();
        Long lDayTimelinePanel$lambda$46$lambda$11 = DayTimelineKt.DayTimelinePanel$lambda$46$lambda$11($dragAnchor$delegate);
        if (lDayTimelinePanel$lambda$46$lambda$11 == null) {
            return Unit.INSTANCE;
        }
        long anchor = lDayTimelinePanel$lambda$46$lambda$11.longValue();
        $onSelectionChange.invoke(Long.valueOf(anchor), Long.valueOf(DayTimelineKt.DayTimelinePanel$lambda$46$lambda$45$msFor($totalPx, $visibleStart, $visibleEnd, Float.intBitsToFloat((int) (4294967295L & change.getPosition())))));
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit invoke$lambda$1(MutableState $dragAnchor$delegate) {
        $dragAnchor$delegate.setValue(null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Unit invoke$lambda$2(Function2 $onSelectionChange, MutableState $dragAnchor$delegate) {
        $dragAnchor$delegate.setValue(null);
        $onSelectionChange.invoke(null, null);
        return Unit.INSTANCE;
    }
}
