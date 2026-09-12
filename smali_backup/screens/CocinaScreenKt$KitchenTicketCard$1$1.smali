.class final Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;
.super Lkotlin/coroutines/jvm/internal/SuspendLambda;
.source "CocinaScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard(Lcom/example/data/entity/OrderEntity;JLcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/Composer;I)V
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
    c = "com.example.ui.screens.CocinaScreenKt$KitchenTicketCard$1$1"
    f = "CocinaScreen.kt"
    i = {}
    l = {
        0x2c9,
        0x2cd,
        0x2d1
    }
    m = "invokeSuspend"
    n = {}
    s = {}
.end annotation


# instance fields
.field final synthetic $cardScale:Landroidx/compose/animation/core/Animatable;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/animation/core/Animatable<",
            "Ljava/lang/Float;",
            "Landroidx/compose/animation/core/AnimationVector1D;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $order:Lcom/example/data/entity/OrderEntity;

.field final synthetic $previousStatus$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field label:I


# direct methods
.method constructor <init>(Lcom/example/data/entity/OrderEntity;Landroidx/compose/animation/core/Animatable;Landroidx/compose/runtime/MutableState;Lkotlin/coroutines/Continuation;)V
    .locals 1
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/example/data/entity/OrderEntity;",
            "Landroidx/compose/animation/core/Animatable<",
            "Ljava/lang/Float;",
            "Landroidx/compose/animation/core/AnimationVector1D;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;",
            "Lkotlin/coroutines/Continuation<",
            "-",
            "Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;",
            ">;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$order:Lcom/example/data/entity/OrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$cardScale:Landroidx/compose/animation/core/Animatable;

    iput-object p3, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$previousStatus$delegate:Landroidx/compose/runtime/MutableState;

    const/4 v0, 0x2

    invoke-direct {p0, v0, p4}, Lkotlin/coroutines/jvm/internal/SuspendLambda;-><init>(ILkotlin/coroutines/Continuation;)V

    return-void
.end method


# virtual methods
.method public final create(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;
    .locals 4
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

    new-instance v0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;

    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$order:Lcom/example/data/entity/OrderEntity;

    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$cardScale:Landroidx/compose/animation/core/Animatable;

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$previousStatus$delegate:Landroidx/compose/runtime/MutableState;

    invoke-direct {v0, v1, v2, v3, p2}, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;-><init>(Lcom/example/data/entity/OrderEntity;Landroidx/compose/animation/core/Animatable;Landroidx/compose/runtime/MutableState;Lkotlin/coroutines/Continuation;)V

    check-cast v0, Lkotlin/coroutines/Continuation;

    return-object v0
.end method

.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    check-cast p1, Lkotlinx/coroutines/CoroutineScope;

    check-cast p2, Lkotlin/coroutines/Continuation;

    invoke-virtual {p0, p1, p2}, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->invoke(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;

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

    invoke-virtual {p0, p1, p2}, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->create(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;

    move-result-object v0

    check-cast v0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;

    sget-object v1, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    invoke-virtual {v0, v1}, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->invokeSuspend(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    return-object v0
.end method

.method public final invokeSuspend(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 14
    .param p1, "$result"    # Ljava/lang/Object;

    invoke-static {}, Lkotlin/coroutines/intrinsics/IntrinsicsKt;->getCOROUTINE_SUSPENDED()Ljava/lang/Object;

    move-result-object v0

    .line 709
    iget v1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->label:I

    const/4 v2, 0x0

    const/4 v3, 0x2

    const/4 v4, 0x0

    packed-switch v1, :pswitch_data_0

    new-instance v0, Ljava/lang/IllegalStateException;

    const-string v1, "call to \'resume\' before \'invoke\' with coroutine"

    invoke-direct {v0, v1}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw v0

    :pswitch_0
    invoke-static {p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    goto/16 :goto_2

    :pswitch_1
    invoke-static {p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    goto :goto_1

    :pswitch_2
    invoke-static {p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    goto :goto_0

    :pswitch_3
    invoke-static {p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    .line 710
    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$previousStatus$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v1}, Lcom/example/ui/screens/CocinaScreenKt;->access$KitchenTicketCard$lambda$151(Landroidx/compose/runtime/MutableState;)Ljava/lang/String;

    move-result-object v1

    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$order:Lcom/example/data/entity/OrderEntity;

    invoke-virtual {v5}, Lcom/example/data/entity/OrderEntity;->getStatus()Ljava/lang/String;

    move-result-object v5

    invoke-static {v1, v5}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_3

    .line 711
    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$previousStatus$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$order:Lcom/example/data/entity/OrderEntity;

    invoke-virtual {v5}, Lcom/example/data/entity/OrderEntity;->getStatus()Ljava/lang/String;

    move-result-object v5

    invoke-static {v1, v5}, Lcom/example/ui/screens/CocinaScreenKt;->access$KitchenTicketCard$lambda$152(Landroidx/compose/runtime/MutableState;Ljava/lang/String;)V

    .line 713
    iget-object v6, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$cardScale:Landroidx/compose/animation/core/Animatable;

    .line 714
    const v1, 0x3f847ae1    # 1.035f

    invoke-static {v1}, Lkotlin/coroutines/jvm/internal/Boxing;->boxFloat(F)Ljava/lang/Float;

    move-result-object v7

    .line 715
    const/16 v1, 0x96

    invoke-static {}, Landroidx/compose/animation/core/EasingKt;->getFastOutSlowInEasing()Landroidx/compose/animation/core/Easing;

    move-result-object v5

    invoke-static {v1, v2, v5, v3, v4}, Landroidx/compose/animation/core/AnimationSpecKt;->tween$default(IILandroidx/compose/animation/core/Easing;ILjava/lang/Object;)Landroidx/compose/animation/core/TweenSpec;

    move-result-object v1

    move-object v8, v1

    check-cast v8, Landroidx/compose/animation/core/AnimationSpec;

    .line 713
    move-object v11, p0

    check-cast v11, Lkotlin/coroutines/Continuation;

    const/4 v1, 0x1

    iput v1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->label:I

    const/4 v9, 0x0

    const/4 v10, 0x0

    const/16 v12, 0xc

    const/4 v13, 0x0

    invoke-static/range {v6 .. v13}, Landroidx/compose/animation/core/Animatable;->animateTo$default(Landroidx/compose/animation/core/Animatable;Ljava/lang/Object;Landroidx/compose/animation/core/AnimationSpec;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    if-ne v1, v0, :cond_0

    .line 709
    return-object v0

    .line 717
    :cond_0
    :goto_0
    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$cardScale:Landroidx/compose/animation/core/Animatable;

    .line 718
    const v1, 0x3f7c28f6    # 0.985f

    invoke-static {v1}, Lkotlin/coroutines/jvm/internal/Boxing;->boxFloat(F)Ljava/lang/Float;

    move-result-object v6

    .line 719
    const/16 v1, 0x78

    invoke-static {}, Landroidx/compose/animation/core/EasingKt;->getLinearOutSlowInEasing()Landroidx/compose/animation/core/Easing;

    move-result-object v7

    invoke-static {v1, v2, v7, v3, v4}, Landroidx/compose/animation/core/AnimationSpecKt;->tween$default(IILandroidx/compose/animation/core/Easing;ILjava/lang/Object;)Landroidx/compose/animation/core/TweenSpec;

    move-result-object v1

    move-object v7, v1

    check-cast v7, Landroidx/compose/animation/core/AnimationSpec;

    .line 717
    move-object v10, p0

    check-cast v10, Lkotlin/coroutines/Continuation;

    iput v3, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->label:I

    const/4 v8, 0x0

    const/4 v9, 0x0

    const/16 v11, 0xc

    const/4 v12, 0x0

    invoke-static/range {v5 .. v12}, Landroidx/compose/animation/core/Animatable;->animateTo$default(Landroidx/compose/animation/core/Animatable;Ljava/lang/Object;Landroidx/compose/animation/core/AnimationSpec;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    if-ne v1, v0, :cond_1

    .line 709
    return-object v0

    .line 721
    :cond_1
    :goto_1
    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->$cardScale:Landroidx/compose/animation/core/Animatable;

    .line 722
    const/high16 v1, 0x3f800000    # 1.0f

    invoke-static {v1}, Lkotlin/coroutines/jvm/internal/Boxing;->boxFloat(F)Ljava/lang/Float;

    move-result-object v6

    .line 723
    const/high16 v1, 0x43c80000    # 400.0f

    const/4 v2, 0x4

    const v3, 0x3f19999a    # 0.6f

    invoke-static {v3, v1, v4, v2, v4}, Landroidx/compose/animation/core/AnimationSpecKt;->spring$default(FFLjava/lang/Object;ILjava/lang/Object;)Landroidx/compose/animation/core/SpringSpec;

    move-result-object v1

    move-object v7, v1

    check-cast v7, Landroidx/compose/animation/core/AnimationSpec;

    .line 721
    move-object v10, p0

    check-cast v10, Lkotlin/coroutines/Continuation;

    const/4 v1, 0x3

    iput v1, p0, Lcom/example/ui/screens/CocinaScreenKt$KitchenTicketCard$1$1;->label:I

    const/4 v8, 0x0

    const/4 v9, 0x0

    const/16 v11, 0xc

    const/4 v12, 0x0

    invoke-static/range {v5 .. v12}, Landroidx/compose/animation/core/Animatable;->animateTo$default(Landroidx/compose/animation/core/Animatable;Ljava/lang/Object;Landroidx/compose/animation/core/AnimationSpec;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;

    move-result-object v1

    if-ne v1, v0, :cond_2

    .line 709
    return-object v0

    .line 726
    :cond_2
    :goto_2
    nop

    :cond_3
    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0

    nop

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_3
        :pswitch_2
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method
