package com.varun.pocketassistant.pipeline;

import androidx.autofill.HintConstants;
import com.google.ai.edge.examples.asr.FileAudioSource;
import com.google.android.gms.actions.SearchIntents;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: OpenRouterModels.kt */
/* JADX INFO: loaded from: classes8.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J,\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0086@¢\u0006\u0002\u0010\u0012J,\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0086@¢\u0006\u0002\u0010\u0012J0\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\b\u0010\u0015\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0010H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t¨\u0006\u0016"}, d2 = {"Lcom/varun/pocketassistant/pipeline/OpenRouterModelsClient;", "", "config", "Lcom/varun/pocketassistant/pipeline/PipelineConfig;", "<init>", "(Lcom/varun/pocketassistant/pipeline/PipelineConfig;)V", "http", "Lokhttp3/OkHttpClient;", "getHttp", "()Lokhttp3/OkHttpClient;", "http$delegate", "Lkotlin/Lazy;", "listTranscriptionModels", "", "Lcom/varun/pocketassistant/pipeline/OpenRouterModelInfo;", "apiKey", "", "baseUrl", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "listChatModels", "fetchModels", SearchIntents.EXTRA_QUERY, "app_debug"}, k = 1, mv = {2, 2, 0}, xi = 48)
public final class OpenRouterModelsClient {
    public static final int $stable = 8;
    private final PipelineConfig config;

    /* JADX INFO: renamed from: http$delegate, reason: from kotlin metadata */
    private final Lazy http;

    public OpenRouterModelsClient(PipelineConfig config) {
        Intrinsics.checkNotNullParameter(config, "config");
        this.config = config;
        this.http = LazyKt.lazy(new Function0() { // from class: com.varun.pocketassistant.pipeline.OpenRouterModelsClient$$ExternalSyntheticLambda0
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                return OpenRouterModelsClient.http_delegate$lambda$0();
            }
        });
    }

    private final OkHttpClient getHttp() {
        return (OkHttpClient) this.http.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final OkHttpClient http_delegate$lambda$0() {
        return new OkHttpClient.Builder().dns(ResilientDns.INSTANCE).connectTimeout(30L, TimeUnit.SECONDS).readTimeout(60L, TimeUnit.SECONDS).retryOnConnectionFailure(true).build();
    }

    public static /* synthetic */ Object listTranscriptionModels$default(OpenRouterModelsClient openRouterModelsClient, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        return openRouterModelsClient.listTranscriptionModels(str, str2, continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenRouterModelsClient$listTranscriptionModels$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: OpenRouterModels.kt */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "Lcom/varun/pocketassistant/pipeline/OpenRouterModelInfo;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenRouterModelsClient$listTranscriptionModels$2", f = "OpenRouterModels.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class C06802 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends OpenRouterModelInfo>>, Object> {
        final /* synthetic */ String $apiKey;
        final /* synthetic */ String $baseUrl;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C06802(String str, String str2, Continuation<? super C06802> continuation) {
            super(2, continuation);
            this.$apiKey = str;
            this.$baseUrl = str2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return OpenRouterModelsClient.this.new C06802(this.$apiKey, this.$baseUrl, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends OpenRouterModelInfo>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super List<OpenRouterModelInfo>>) continuation);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super List<OpenRouterModelInfo>> continuation) {
            return ((C06802) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws IOException {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    List filtered = OpenRouterModelsClient.this.fetchModels("output_modalities=transcription", this.$apiKey, this.$baseUrl);
                    if (!filtered.isEmpty()) {
                        return filtered;
                    }
                    Iterable iterableFetchModels = OpenRouterModelsClient.this.fetchModels(null, this.$apiKey, this.$baseUrl);
                    Collection arrayList = new ArrayList();
                    for (Object obj : iterableFetchModels) {
                        String lowerCase = ((OpenRouterModelInfo) obj).getId().toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                        if (StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "whisper", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "transcri", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "speech", false, 2, (Object) null) || StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "asr", false, 2, (Object) null)) {
                            arrayList.add(obj);
                        }
                    }
                    return (List) arrayList;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final Object listTranscriptionModels(String apiKey, String baseUrl, Continuation<? super List<OpenRouterModelInfo>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new C06802(apiKey, baseUrl, null), continuation);
    }

    public static /* synthetic */ Object listChatModels$default(OpenRouterModelsClient openRouterModelsClient, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        if ((i & 2) != 0) {
            str2 = null;
        }
        return openRouterModelsClient.listChatModels(str, str2, continuation);
    }

    /* JADX INFO: renamed from: com.varun.pocketassistant.pipeline.OpenRouterModelsClient$listChatModels$2, reason: invalid class name */
    /* JADX INFO: compiled from: OpenRouterModels.kt */
    @Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\n"}, d2 = {"<anonymous>", "", "Lcom/varun/pocketassistant/pipeline/OpenRouterModelInfo;", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {2, 2, 0}, xi = 48)
    @DebugMetadata(c = "com.varun.pocketassistant.pipeline.OpenRouterModelsClient$listChatModels$2", f = "OpenRouterModels.kt", i = {}, l = {}, m = "invokeSuspend", n = {}, s = {})
    static final class AnonymousClass2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends OpenRouterModelInfo>>, Object> {
        final /* synthetic */ String $apiKey;
        final /* synthetic */ String $baseUrl;
        private /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(String str, String str2, Continuation<? super AnonymousClass2> continuation) {
            super(2, continuation);
            this.$apiKey = str;
            this.$baseUrl = str2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass2 anonymousClass2 = OpenRouterModelsClient.this.new AnonymousClass2(this.$apiKey, this.$baseUrl, continuation);
            anonymousClass2.L$0 = obj;
            return anonymousClass2;
        }

        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(CoroutineScope coroutineScope, Continuation<? super List<? extends OpenRouterModelInfo>> continuation) {
            return invoke2(coroutineScope, (Continuation<? super List<OpenRouterModelInfo>>) continuation);
        }

        /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(CoroutineScope coroutineScope, Continuation<? super List<OpenRouterModelInfo>> continuation) {
            return ((AnonymousClass2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object $result) throws IOException {
            Object objM8304constructorimpl;
            CoroutineScope $this$withContext = (CoroutineScope) this.L$0;
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    OpenRouterModelsClient openRouterModelsClient = OpenRouterModelsClient.this;
                    String str = this.$apiKey;
                    String str2 = this.$baseUrl;
                    try {
                        Result.Companion companion = Result.INSTANCE;
                        objM8304constructorimpl = Result.m8304constructorimpl(openRouterModelsClient.fetchModels("output_modalities=text", str, str2));
                        break;
                    } catch (Throwable th) {
                        Result.Companion companion2 = Result.INSTANCE;
                        objM8304constructorimpl = Result.m8304constructorimpl(ResultKt.createFailure(th));
                    }
                    List listEmptyList = CollectionsKt.emptyList();
                    if (Result.m8310isFailureimpl(objM8304constructorimpl)) {
                        objM8304constructorimpl = listEmptyList;
                    }
                    List text = (List) objM8304constructorimpl;
                    List listFetchModels = text;
                    OpenRouterModelsClient openRouterModelsClient2 = OpenRouterModelsClient.this;
                    String str3 = this.$apiKey;
                    String str4 = this.$baseUrl;
                    if (listFetchModels.isEmpty()) {
                        listFetchModels = openRouterModelsClient2.fetchModels(null, str3, str4);
                    }
                    List catalog = listFetchModels;
                    Collection arrayList = new ArrayList();
                    for (Object obj : catalog) {
                        String lowerCase = ((OpenRouterModelInfo) obj).getId().toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                        List text2 = text;
                        boolean z = false;
                        CoroutineScope $this$withContext2 = $this$withContext;
                        if (!StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "whisper", false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "tts", false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "embed", false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "moderation", false, 2, (Object) null) && !StringsKt.contains$default((CharSequence) lowerCase, (CharSequence) "transcri", false, 2, (Object) null)) {
                            z = true;
                        }
                        if (z) {
                            arrayList.add(obj);
                        }
                        text = text2;
                        $this$withContext = $this$withContext2;
                    }
                    return (List) arrayList;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public final Object listChatModels(String apiKey, String baseUrl, Continuation<? super List<OpenRouterModelInfo>> continuation) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass2(apiKey, baseUrl, null), continuation);
    }

    static /* synthetic */ List fetchModels$default(OpenRouterModelsClient openRouterModelsClient, String str, String str2, String str3, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = null;
        }
        if ((i & 4) != 0) {
            str3 = null;
        }
        return openRouterModelsClient.fetchModels(str, str2, str3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:11:0x0029  */
    /* JADX WARN: Code duplicated, block: B:20:0x0055  */
    public final List<OpenRouterModelInfo> fetchModels(String query, String apiKey, String baseUrl) throws IOException {
        String key;
        String cloudBaseUrl;
        Throwable th;
        String strString;
        int i;
        String strOptString;
        JSONArray jSONArrayOptJSONArray;
        String string;
        String string2;
        PipelineSettings saved = this.config.load();
        if (apiKey == null || (string2 = StringsKt.trim((CharSequence) apiKey).toString()) == null) {
            key = StringsKt.trim((CharSequence) saved.getCloudApiKey()).toString();
        } else {
            String str = string2;
            if (StringsKt.isBlank(str)) {
                str = null;
            }
            key = str;
            if (key == null) {
                key = StringsKt.trim((CharSequence) saved.getCloudApiKey()).toString();
            }
        }
        if (baseUrl == null || (string = StringsKt.trim((CharSequence) baseUrl).toString()) == null) {
            cloudBaseUrl = saved.getCloudBaseUrl();
        } else {
            String str2 = string;
            if (StringsKt.isBlank(str2)) {
                str2 = null;
            }
            cloudBaseUrl = str2;
            if (cloudBaseUrl == null) {
                cloudBaseUrl = saved.getCloudBaseUrl();
            }
        }
        String base = StringsKt.trimEnd(cloudBaseUrl, '/');
        if (StringsKt.isBlank(key)) {
            throw new IllegalArgumentException("Cloud API key not set".toString());
        }
        if (StringsKt.isBlank(base)) {
            throw new IllegalArgumentException("Cloud base URL not set".toString());
        }
        String str3 = query;
        String url = str3 == null || StringsKt.isBlank(str3) ? base + "/models" : base + "/models?" + query;
        boolean isOpenRouter = StringsKt.contains((CharSequence) base, (CharSequence) "openrouter.ai", true);
        Request.Builder builder = new Request.Builder().url(url).get().header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
        if (isOpenRouter) {
            builder.header("HTTP-Referer", "https://github.com/varunramesh/pocket-assistant");
            builder.header("X-Title", "Pocket Assistant");
        }
        Response responseExecute = getHttp().newCall(builder.build()).execute();
        try {
            Response response = responseExecute;
            ResponseBody responseBodyBody = response.body();
            if (responseBodyBody != null) {
                try {
                    strString = responseBodyBody.string();
                } catch (Throwable th2) {
                    th = th2;
                }
            } else {
                strString = null;
            }
            if (strString == null) {
                strString = "";
            }
            try {
                if (!response.isSuccessful()) {
                    throw new IllegalStateException("Models HTTP " + response.code() + ": " + strString);
                }
                JSONArray jSONArrayOptJSONArray2 = new JSONObject(strString).optJSONArray(FileAudioSource.DATA_CHUNK_ID);
                if (jSONArrayOptJSONArray2 == null) {
                    List<OpenRouterModelInfo> listEmptyList = CollectionsKt.emptyList();
                    CloseableKt.closeFinally(responseExecute, null);
                    return listEmptyList;
                }
                Iterable arrayList = new ArrayList(jSONArrayOptJSONArray2.length());
                int i2 = 0;
                int length = jSONArrayOptJSONArray2.length();
                while (i2 < length) {
                    JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray2.optJSONObject(i2);
                    if (jSONObjectOptJSONObject == null) {
                        i = i2;
                        saved = saved;
                        key = key;
                    } else {
                        i = i2;
                        String strOptString2 = jSONObjectOptJSONObject.optString("id");
                        Intrinsics.checkNotNullExpressionValue(strOptString2, "optString(...)");
                        String string3 = StringsKt.trim((CharSequence) strOptString2).toString();
                        if (string3.length() == 0) {
                            saved = saved;
                            key = key;
                        } else {
                            JSONObject jSONObjectOptJSONObject2 = jSONObjectOptJSONObject.optJSONObject("reasoning");
                            List arrayList2 = new ArrayList();
                            if (jSONObjectOptJSONObject2 != null && (jSONArrayOptJSONArray = jSONObjectOptJSONObject2.optJSONArray("supported_efforts")) != null) {
                                try {
                                    int i3 = 0;
                                    for (int length2 = jSONArrayOptJSONArray.length(); i3 < length2; length2 = length2) {
                                        String strOptString3 = jSONArrayOptJSONArray.optString(i3);
                                        Intrinsics.checkNotNull(strOptString3);
                                        if (StringsKt.isBlank(strOptString3)) {
                                            strOptString3 = null;
                                        }
                                        if (strOptString3 != null) {
                                            arrayList2.add(strOptString3);
                                        }
                                        i3++;
                                        jSONArrayOptJSONArray = jSONArrayOptJSONArray;
                                    }
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                            }
                            ArrayList arrayList3 = (Collection) arrayList;
                            String strOptString4 = jSONObjectOptJSONObject.optString(HintConstants.AUTOFILL_HINT_NAME);
                            if (StringsKt.isBlank(strOptString4)) {
                                strOptString4 = string3;
                            }
                            Intrinsics.checkNotNullExpressionValue(strOptString4, "ifBlank(...)");
                            arrayList3.add(new OpenRouterModelInfo(string3, strOptString4, jSONObjectOptJSONObject2 != null, arrayList2, (jSONObjectOptJSONObject2 == null || (strOptString = jSONObjectOptJSONObject2.optString("default_effort")) == null || StringsKt.isBlank(strOptString)) ? null : strOptString, jSONObjectOptJSONObject2 != null && jSONObjectOptJSONObject2.optBoolean("mandatory", false)));
                        }
                    }
                    i2 = i + 1;
                    saved = saved;
                    key = key;
                }
                List<OpenRouterModelInfo> listSortedWith = CollectionsKt.sortedWith(arrayList, new Comparator() { // from class: com.varun.pocketassistant.pipeline.OpenRouterModelsClient$fetchModels$lambda$11$$inlined$sortedBy$1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.util.Comparator
                    public final int compare(T t, T t2) {
                        String lowerCase = ((OpenRouterModelInfo) t).getId().toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase, "toLowerCase(...)");
                        String lowerCase2 = ((OpenRouterModelInfo) t2).getId().toLowerCase(Locale.ROOT);
                        Intrinsics.checkNotNullExpressionValue(lowerCase2, "toLowerCase(...)");
                        return ComparisonsKt.compareValues(lowerCase, lowerCase2);
                    }
                });
                CloseableKt.closeFinally(responseExecute, null);
                return listSortedWith;
            } catch (Throwable th4) {
                th = th4;
            }
        } catch (Throwable th5) {
            th = th5;
        }
        try {
            throw th;
        } catch (Throwable th6) {
            CloseableKt.closeFinally(responseExecute, th);
            throw th6;
        }
    }
}
