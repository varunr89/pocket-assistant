package com.varun.pocketassistant.ui;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* JADX INFO: compiled from: HomeScreen.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
final /* synthetic */ class HomeScreenKt$HomeScreen$3$1$1$9$1 extends FunctionReferenceImpl implements Function0<Unit> {
    HomeScreenKt$HomeScreen$3$1$1$9$1(Object obj) {
        super(0, obj, HomeViewModel.class, "suggestMeetingsForDay", "suggestMeetingsForDay()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    public /* bridge */ /* synthetic */ Unit invoke() {
        invoke2();
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public final void invoke2() {
        ((HomeViewModel) this.receiver).suggestMeetingsForDay();
    }
}
