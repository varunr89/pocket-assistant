package com.varun.pocketassistant.ui;

import com.varun.pocketassistant.capture.CaptureState;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HomeViewModel.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002¨\u0006\u0003"}, d2 = {"label", "", "Lcom/varun/pocketassistant/capture/CaptureState;", "app_debug"}, k = 2, mv = {2, 2, 0}, xi = 48)
public final class HomeViewModelKt {

    /* JADX INFO: compiled from: HomeViewModel.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = 48)
    public static final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[CaptureState.values().length];
            try {
                iArr[CaptureState.IDLE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[CaptureState.RECORDING.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[CaptureState.PAUSED.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static final String label(CaptureState $this$label) {
        Intrinsics.checkNotNullParameter($this$label, "<this>");
        switch (WhenMappings.$EnumSwitchMapping$0[$this$label.ordinal()]) {
            case 1:
                return "Idle";
            case 2:
                return "Listening";
            case 3:
                return "Paused";
            default:
                throw new NoWhenBranchMatchedException();
        }
    }
}
