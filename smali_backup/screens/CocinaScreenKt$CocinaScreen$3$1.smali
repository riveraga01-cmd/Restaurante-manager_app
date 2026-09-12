.class final Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;
.super Lkotlin/coroutines/jvm/internal/SuspendLambda;
.source "CocinaScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/CocinaScreenKt;->CocinaScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x18
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Lkotlin/coroutines/jvm/internal/SuspendLambda;",
        "Lkotlin/jvm/functions/Function2<",
        "Lkotlinx/coroutines/CoroutineScope;",
        "Lkotlin/coroutines/Continuation<",
        "-",
        "Lkotlin/Unit;",
        ">;",
        "Ljava/lang/Object;",
        ">;"
    }
.end annotation

.annotation system Ldalvik/annotation/SourceDebugExtension;
    value = "SMAP\nCocinaScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CocinaScreen.kt\ncom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1\n+ 2 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 3 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1229:1\n774#2:1230\n865#2,2:1231\n1563#2:1233\n1634#2,3:1234\n1#3:1237\n*S KotlinDebug\n*F\n+ 1 CocinaScreen.kt\ncom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1\n*L\n187#1:1230\n187#1:1231,2\n187#1:1233\n187#1:1234,3\n*E\n"
.end annotation

.annotation runtime Lkotlin/Metadata;
    d1 = {
        "\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n"
    }
    d2 = {
        "<anonymous>",
        "",
        "Lkotlinx/coroutines/CoroutineScope;"
    }
    k = 0x3
    mv = {
        0x2,
        0x2,
        0x0
    }
    xi = 0x30
.end annotation

.annotation runtime Lkotlin/coroutines/jvm/internal/DebugMetadata;
    c = "com.example.ui.screens.CocinaScreenKt$CocinaScreen$3$1"
    f = "CocinaScreen.kt"
    i = {}
    l = {}
    m = "invokeSuspend"
    n = {}
    s = {}
.end annotation


# instance fields
.field final synthetic $context:Landroid/content/Context;

.field final synthetic $kitchenOrders$delegate:Landroidx/compose/runtime/State;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/State<",
            "Ljava/util/List<",
            "Lcom/example/data/entity/OrderEntity;",
            ">;>;"
        }
    .end annotation
.end field

.field final synthetic $previousPendingOrderIds$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/util/Set<",
            "Ljava/lang/Long;",
            ">;>;"
        }
    .end annotation
.end field

.field final synthetic $systemSettings$delegate:Landroidx/compose/runtime/State;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/State<",
            "Lcom/example/data/entity/SystemSettingsEntity;",
            ">;"
        }
    .end annotation
.end field

.field label:I


# direct methods
.method constructor <init>(Landroid/content/Context;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/State;Lkotlin/coroutines/Continuation;)V
    .locals 1
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Landroid/content/Context;",
            "Landroidx/compose/runtime/State<",
            "+",
            "Ljava/util/List<",
            "Lcom/example/data/entity/OrderEntity;",
            ">;>;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/util/Set<",
            "Ljava/lang/Long;",
            ">;>;",
            "Landroidx/compose/runtime/State<",
            "Lcom/example/data/entity/SystemSettingsEntity;",
            ">;",
            "Lkotlin/coroutines/Continuation<",
            "-",
            "Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;",
            ">;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$context:Landroid/content/Context;

    iput-object p2, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$kitchenOrders$delegate:Landroidx/compose/runtime/State;

    iput-object p3, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$previousPendingOrderIds$delegate:Landroidx/compose/runtime/MutableState;

    iput-object p4, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    const/4 v0, 0x2

    invoke-direct {p0, v0, p5}, Lkotlin/coroutines/jvm/internal/SuspendLambda;-><init>(ILkotlin/coroutines/Continuation;)V

    return-void
.end method


# virtual methods
.method public final create(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;
    .locals 6
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/Object;",
            "Lkotlin/coroutines/Continuation<",
            "*>;)",
            "Lkotlin/coroutines/Continuation<",
            "Lkotlin/Unit;",
            ">;"
        }
    .end annotation

    new-instance v0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;

    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$context:Landroid/content/Context;

    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$kitchenOrders$delegate:Landroidx/compose/runtime/State;

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$previousPendingOrderIds$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v4, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    move-object v5, p2

    invoke-direct/range {v0 .. v5}, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;-><init>(Landroid/content/Context;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/State;Lkotlin/coroutines/Continuation;)V

    check-cast v0, Lkotlin/coroutines/Continuation;

    return-object v0
.end method

.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    check-cast p1, Lkotlinx/coroutines/CoroutineScope;

    check-cast p2, Lkotlin/coroutines/Continuation;

    invoke-virtual {p0, p1, p2}, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->invoke(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;

    move-result-object v0

    return-object v0
.end method

.method public final invoke(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
    .locals 2
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lkotlinx/coroutines/CoroutineScope;",
            "Lkotlin/coroutines/Continuation<",
            "-",
            "Lkotlin/Unit;",
            ">;)",
            "Ljava/lang/Object;"
        }
    .end annotation

    invoke-virtual {p0, p1, p2}, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->create(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;

    move-result-object v0

    check-cast v0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;

    sget-object v1, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    invoke-virtual {v0, v1}, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->invokeSuspend(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    return-object v0
.end method

.method public final invokeSuspend(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 11
    .param p1, "$result"    # Ljava/lang/Object;

    invoke-static {}, Lkotlin/coroutines/intrinsics/IntrinsicsKt;->getCOROUTINE_SUSPENDED()Ljava/lang/Object;

    .line 186
    iget v0, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->label:I

    packed-switch v0, :pswitch_data_0

    new-instance v0, Ljava/lang/IllegalStateException;

    const-string v1, "call to \'resume\' before \'invoke\' with coroutine"

    invoke-direct {v0, v1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw v0

    :pswitch_0
    invoke-static {p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    .line 187
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$kitchenOrders$delegate:Landroidx/compose/runtime/State;

    invoke-static {v0}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$0(Landroidx/compose/runtime/State;)Ljava/util/List;

    move-result-object v0

    check-cast v0, Ljava/lang/Iterable;

    .local v0, "$this$filter\\1":Ljava/lang/Iterable;
    const/4 v1, 0x0

    .line 1230
    .local v1, "$i$f$filter\\1\\187":I
    new-instance v2, Ljava/util/ArrayList;

    invoke-direct {v2}, Ljava/util/ArrayList;-><init>()V

    check-cast v2, Ljava/util/Collection;

    .local v2, "destination\\2":Ljava/util/Collection;
    move-object v3, v0

    .local v3, "$this$filterTo\\2":Ljava/lang/Iterable;
    const/4 v4, 0x0

    .line 1231
    .local v4, "$i$f$filterTo\\2\\1230":I
    invoke-interface {v3}, Ljava/lang/Iterable;->iterator()Ljava/util/Iterator;

    move-result-object v5

    :cond_0
    :goto_0
    invoke-interface {v5}, Ljava/util/Iterator;->hasNext()Z

    move-result v6

    if-eqz v6, :cond_1

    invoke-interface {v5}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v6

    .local v6, "element\\2":Ljava/lang/Object;
    move-object v7, v6

    check-cast v7, Lcom/example/data/entity/OrderEntity;

    .local v7, "it\\3":Lcom/example/data/entity/OrderEntity;
    const/4 v8, 0x0

    .line 187
    .local v8, "$i$a$-filter-CocinaScreenKt$CocinaScreen$3$1$currentPendingIds$1\\3\\1231\\0":I
    invoke-virtual {v7}, Lcom/example/data/entity/OrderEntity;->getStatus()Ljava/lang/String;

    move-result-object v9

    const-string v10, "PENDIENTE"

    invoke-static {v9, v10}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v7

    .line 1231
    .end local v7    # "it\\3":Lcom/example/data/entity/OrderEntity;
    .end local v8    # "$i$a$-filter-CocinaScreenKt$CocinaScreen$3$1$currentPendingIds$1\\3\\1231\\0":I
    if-eqz v7, :cond_0

    invoke-interface {v2, v6}, Ljava/util/Collection;->add(Ljava/lang/Object;)Z

    goto :goto_0

    .line 1232
    .end local v6    # "element\\2":Ljava/lang/Object;
    :cond_1
    nop

    .end local v2    # "destination\\2":Ljava/util/Collection;
    .end local v3    # "$this$filterTo\\2":Ljava/lang/Iterable;
    .end local v4    # "$i$f$filterTo\\2\\1230":I
    check-cast v2, Ljava/util/List;

    .line 1230
    nop

    .end local v0    # "$this$filter\\1":Ljava/lang/Iterable;
    .end local v1    # "$i$f$filter\\1\\187":I
    check-cast v2, Ljava/lang/Iterable;

    .line 187
    nop

    .local v2, "$this$map\\4":Ljava/lang/Iterable;
    const/4 v0, 0x0

    .line 1233
    .local v0, "$i$f$map\\4\\187":I
    new-instance v1, Ljava/util/ArrayList;

    const/16 v3, 0xa

    invoke-static {v2, v3}, Lkotlin/collections/CollectionsKt;->collectionSizeOrDefault(Ljava/lang/Iterable;I)I

    move-result v3

    invoke-direct {v1, v3}, Ljava/util/ArrayList;-><init>(I)V

    check-cast v1, Ljava/util/Collection;

    .local v1, "destination\\5":Ljava/util/Collection;
    move-object v3, v2

    .local v3, "$this$mapTo\\5":Ljava/lang/Iterable;
    const/4 v4, 0x0

    .line 1234
    .local v4, "$i$f$mapTo\\5\\1233":I
    invoke-interface {v3}, Ljava/lang/Iterable;->iterator()Ljava/util/Iterator;

    move-result-object v5

    :goto_1
    invoke-interface {v5}, Ljava/util/Iterator;->hasNext()Z

    move-result v6

    if-eqz v6, :cond_2

    invoke-interface {v5}, Ljava/util/Iterator;->next()Ljava/lang/Object;

    move-result-object v6

    .line 1235
    .local v6, "item\\5":Ljava/lang/Object;
    move-object v7, v6

    check-cast v7, Lcom/example/data/entity/OrderEntity;

    .local v7, "it\\6":Lcom/example/data/entity/OrderEntity;
    const/4 v8, 0x0

    .line 187
    .local v8, "$i$a$-map-CocinaScreenKt$CocinaScreen$3$1$currentPendingIds$2\\6\\1235\\0":I
    invoke-virtual {v7}, Lcom/example/data/entity/OrderEntity;->getId()J

    move-result-wide v7

    .end local v7    # "it\\6":Lcom/example/data/entity/OrderEntity;
    .end local v8    # "$i$a$-map-CocinaScreenKt$CocinaScreen$3$1$currentPendingIds$2\\6\\1235\\0":I
    invoke-static {v7, v8}, Lkotlin/coroutines/jvm/internal/Boxing;->boxLong(J)Ljava/lang/Long;

    move-result-object v7

    .line 1235
    invoke-interface {v1, v7}, Ljava/util/Collection;->add(Ljava/lang/Object;)Z

    goto :goto_1

    .line 1236
    .end local v6    # "item\\5":Ljava/lang/Object;
    :cond_2
    nop

    .end local v1    # "destination\\5":Ljava/util/Collection;
    .end local v3    # "$this$mapTo\\5":Ljava/lang/Iterable;
    .end local v4    # "$i$f$mapTo\\5\\1233":I
    check-cast v1, Ljava/util/List;

    .line 1233
    nop

    .end local v0    # "$i$f$map\\4\\187":I
    .end local v2    # "$this$map\\4":Ljava/lang/Iterable;
    check-cast v1, Ljava/lang/Iterable;

    .line 187
    invoke-static {v1}, Lkotlin/collections/CollectionsKt;->toSet(Ljava/lang/Iterable;)Ljava/util/Set;

    move-result-object v0

    .line 188
    .local v0, "currentPendingIds":Ljava/util/Set;
    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$previousPendingOrderIds$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v1}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$42(Landroidx/compose/runtime/MutableState;)Ljava/util/Set;

    move-result-object v1

    check-cast v1, Ljava/util/Collection;

    invoke-interface {v1}, Ljava/util/Collection;->isEmpty()Z

    move-result v1

    if-nez v1, :cond_5

    .line 189
    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$previousPendingOrderIds$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v1}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$42(Landroidx/compose/runtime/MutableState;)Ljava/util/Set;

    move-result-object v1

    check-cast v1, Ljava/lang/Iterable;

    invoke-static {v0, v1}, Lkotlin/collections/SetsKt;->minus(Ljava/util/Set;Ljava/lang/Iterable;)Ljava/util/Set;

    move-result-object v1

    .line 190
    .local v1, "newOrders":Ljava/util/Set;
    move-object v2, v1

    check-cast v2, Ljava/util/Collection;

    invoke-interface {v2}, Ljava/util/Collection;->isEmpty()Z

    move-result v2

    if-nez v2, :cond_5

    .line 191
    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$26(Landroidx/compose/runtime/State;)Lcom/example/data/entity/SystemSettingsEntity;

    move-result-object v2

    invoke-virtual {v2}, Lcom/example/data/entity/SystemSettingsEntity;->getKitchenSoundEnabled()Z

    move-result v2

    if-eqz v2, :cond_4

    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$26(Landroidx/compose/runtime/State;)Lcom/example/data/entity/SystemSettingsEntity;

    move-result-object v2

    invoke-virtual {v2}, Lcom/example/data/entity/SystemSettingsEntity;->getNotificationSoundEnabled()Z

    move-result v2

    if-eqz v2, :cond_4

    .line 192
    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$26(Landroidx/compose/runtime/State;)Lcom/example/data/entity/SystemSettingsEntity;

    move-result-object v2

    invoke-virtual {v2}, Lcom/example/data/entity/SystemSettingsEntity;->getKitchenRingtoneUri()Ljava/lang/String;

    move-result-object v2

    check-cast v2, Ljava/lang/CharSequence;

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lkotlin/text/StringsKt;->isBlank(Ljava/lang/CharSequence;)Z

    move-result v4

    if-eqz v4, :cond_3

    .line 1237
    const/4 v2, 0x0

    .line 192
    .local v2, "$i$a$-ifBlank-CocinaScreenKt$CocinaScreen$3$1$customTone$1\\7\\192\\0":I
    invoke-static {v3}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$26(Landroidx/compose/runtime/State;)Lcom/example/data/entity/SystemSettingsEntity;

    move-result-object v3

    invoke-virtual {v3}, Lcom/example/data/entity/SystemSettingsEntity;->getNotificationRingtoneUri()Ljava/lang/String;

    move-result-object v2

    .end local v2    # "$i$a$-ifBlank-CocinaScreenKt$CocinaScreen$3$1$customTone$1\\7\\192\\0":I
    :cond_3
    check-cast v2, Ljava/lang/String;

    .line 193
    .local v2, "customTone":Ljava/lang/String;
    sget-object v3, Lcom/example/util/NotificationHelper;->INSTANCE:Lcom/example/util/NotificationHelper;

    iget-object v4, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$context:Landroid/content/Context;

    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    invoke-static {v5}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$26(Landroidx/compose/runtime/State;)Lcom/example/data/entity/SystemSettingsEntity;

    move-result-object v5

    invoke-virtual {v5}, Lcom/example/data/entity/SystemSettingsEntity;->getNotificationVolume()F

    move-result v5

    invoke-virtual {v3, v4, v2, v5}, Lcom/example/util/NotificationHelper;->playOrderAlertChime(Landroid/content/Context;Ljava/lang/String;F)V

    .line 195
    .end local v2    # "customTone":Ljava/lang/String;
    :cond_4
    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$systemSettings$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$26(Landroidx/compose/runtime/State;)Lcom/example/data/entity/SystemSettingsEntity;

    move-result-object v2

    invoke-virtual {v2}, Lcom/example/data/entity/SystemSettingsEntity;->getNotificationVibrationEnabled()Z

    move-result v2

    if-eqz v2, :cond_5

    .line 196
    sget-object v2, Lcom/example/util/HapticHelper;->INSTANCE:Lcom/example/util/HapticHelper;

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$context:Landroid/content/Context;

    invoke-virtual {v2, v3}, Lcom/example/util/HapticHelper;->triggerAlertVibration(Landroid/content/Context;)V

    .line 200
    .end local v1    # "newOrders":Ljava/util/Set;
    :cond_5
    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$CocinaScreen$3$1;->$previousPendingOrderIds$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v1, v0}, Lcom/example/ui/screens/CocinaScreenKt;->access$CocinaScreen$lambda$43(Landroidx/compose/runtime/MutableState;Ljava/util/Set;)V

    .line 201
    sget-object v1, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v1

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_0
    .end packed-switch
.end method
