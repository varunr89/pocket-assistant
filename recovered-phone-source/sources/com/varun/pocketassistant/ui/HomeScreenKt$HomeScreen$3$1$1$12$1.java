package com.varun.pocketassistant.ui;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: HomeScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class HomeScreenKt$HomeScreen$3$1$1$12$1 extends FunctionReferenceImpl implements Function2<Long, Long, Unit> {
    HomeScreenKt$HomeScreen$3$1$1$12$1(Object obj) {
        super(2, obj, HomeViewModel.class, "setTimelineSelection", "setTimelineSelection(Ljava/lang/Long;Ljava/lang/Long;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Unit invoke(Long l, Long l2) {
        invoke2(l, l2);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(Long p0, Long p1) {
        ((HomeViewModel) this.receiver).setTimelineSelection(p0, p1);
    }
}
