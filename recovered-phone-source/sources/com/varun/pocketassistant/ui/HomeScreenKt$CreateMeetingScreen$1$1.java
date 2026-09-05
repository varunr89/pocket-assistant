package com.varun.pocketassistant.ui;

import androidx.compose.runtime.MutableLongState;
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
@DebugMetadata(c = "com.varun.pocketassistant.ui.HomeScreenKt$CreateMeetingScreen$1$1", f = "HomeScreen.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
final class HomeScreenKt$CreateMeetingScreen$1$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    final /* synthetic */ MutableLongState $endMs$delegate;
    final /* synthetic */ MutableLongState $startMs$delegate;
    final /* synthetic */ HomeViewModel $viewModel;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    HomeScreenKt$CreateMeetingScreen$1$1(HomeViewModel homeViewModel, MutableLongState mutableLongState, MutableLongState mutableLongState2, Continuation<? super HomeScreenKt$CreateMeetingScreen$1$1> continuation) {
        super(2, continuation);
        this.$viewModel = homeViewModel;
        this.$startMs$delegate = mutableLongState;
        this.$endMs$delegate = mutableLongState2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HomeScreenKt$CreateMeetingScreen$1$1(this.$viewModel, this.$startMs$delegate, this.$endMs$delegate, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((HomeScreenKt$CreateMeetingScreen$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object $result) {
        IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (this.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                this.$viewModel.previewMeetingRange(HomeScreenKt.CreateMeetingScreen$lambda$98(this.$startMs$delegate), HomeScreenKt.CreateMeetingScreen$lambda$101(this.$endMs$delegate));
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }
}
