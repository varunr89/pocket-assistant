package com.varun.pocketassistant.ui;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HomeScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class HomeScreenKt$HomeScreen$3$1$1$15$1 extends FunctionReferenceImpl implements Function1<String, Unit> {
    HomeScreenKt$HomeScreen$3$1$1$15$1(Object obj) {
        super(1, obj, HomeViewModel.class, "setMeetingSearch", "setMeetingSearch(Ljava/lang/String;)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Unit invoke(String str) {
        invoke2(str);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2(String p0) {
        Intrinsics.checkNotNullParameter(p0, "p0");
        ((HomeViewModel) this.receiver).setMeetingSearch(p0);
    }
}
