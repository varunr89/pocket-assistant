package com.varun.pocketassistant.ui;

import androidx.compose.runtime.MutableLongState;
import androidx.compose.runtime.MutableState;
import androidx.compose.runtime.State;
import com.varun.pocketassistant.data.MeetingEntity;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* JADX INFO: compiled from: HomeScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
@DebugMetadata(c = "com.varun.pocketassistant.ui.HomeScreenKt$MeetingDetailScreen$1$1", f = "HomeScreen.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class HomeScreenKt$MeetingDetailScreen$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ MutableLongState $editEnd$delegate;
    final /* synthetic */ MutableState<Boolean> $editInitialized$delegate;
    final /* synthetic */ MutableLongState $editStart$delegate;
    final /* synthetic */ MutableState<Boolean> $editing$delegate;
    final /* synthetic */ State<MeetingEntity> $meeting$delegate;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    HomeScreenKt$MeetingDetailScreen$1$1(State<MeetingEntity> state, MutableState<Boolean> mutableState, MutableState<Boolean> mutableState2, MutableLongState mutableLongState, MutableLongState mutableLongState2, Continuation<? super HomeScreenKt$MeetingDetailScreen$1$1> continuation) {
        super(2, continuation);
        this.$meeting$delegate = state;
        this.$editInitialized$delegate = mutableState;
        this.$editing$delegate = mutableState2;
        this.$editStart$delegate = mutableLongState;
        this.$editEnd$delegate = mutableLongState2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HomeScreenKt$MeetingDetailScreen$1$1(this.$meeting$delegate, this.$editInitialized$delegate, this.$editing$delegate, this.$editStart$delegate, this.$editEnd$delegate, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((HomeScreenKt$MeetingDetailScreen$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                MeetingEntity m = HomeScreenKt.MeetingDetailScreen$lambda$124(this.$meeting$delegate);
                if (m == null) {
                    return Unit.INSTANCE;
                }
                if (!HomeScreenKt.MeetingDetailScreen$lambda$139(this.$editInitialized$delegate) || !HomeScreenKt.MeetingDetailScreen$lambda$130(this.$editing$delegate)) {
                    this.$editStart$delegate.setLongValue(m.getStartedAtMs());
                    this.$editEnd$delegate.setLongValue(m.getEndedAtMs());
                    HomeScreenKt.MeetingDetailScreen$lambda$140(this.$editInitialized$delegate, true);
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
