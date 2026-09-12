.class final Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;
.super Lkotlin/coroutines/jvm/internal/SuspendLambda;
.source "CajaScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/CajaScreenKt;->CajaScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
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
    c = "com.example.ui.screens.CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1"
    f = "CajaScreen.kt"
    i = {}
    l = {
        0x25e
    }
    m = "invokeSuspend"
    n = {}
    s = {}
.end annotation


# instance fields
.field final synthetic $actualCashVal:D

.field final synthetic $cardSalesToday:D

.field final synthetic $cashierName$delegate:Landroidx/compose/runtime/State;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/State<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $initialCashVal:D

.field final synthetic $isProcessingClose$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/Boolean;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $isVerifyingPin$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/Boolean;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $pinErrorForCorte$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $pinInputForCorte$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $showCloseConfirmationDialog$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Lcom/example/data/entity/DailyCloseEntity;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $showPinModalForCorte$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/Boolean;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $totalSalesToday:D

.field final synthetic $transferSalesToday:D

.field final synthetic $viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field label:I


# direct methods
.method constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;DDDDDLandroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Lkotlin/coroutines/Continuation;)V
    .locals 16
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/example/ui/viewmodel/RestaurantViewModel;",
            "DDDDD",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/Boolean;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/Boolean;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/Boolean;",
            ">;",
            "Landroidx/compose/runtime/State<",
            "Ljava/lang/String;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Lcom/example/data/entity/DailyCloseEntity;",
            ">;",
            "Lkotlin/coroutines/Continuation<",
            "-",
            "Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;",
            ">;)V"
        }
    .end annotation

    move-object/from16 v0, p0

    move-object/from16 v1, p1

    iput-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    move-wide/from16 v2, p2

    iput-wide v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$initialCashVal:D

    move-wide/from16 v4, p4

    iput-wide v4, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$actualCashVal:D

    move-wide/from16 v6, p6

    iput-wide v6, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cardSalesToday:D

    move-wide/from16 v8, p8

    iput-wide v8, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$transferSalesToday:D

    move-wide/from16 v10, p10

    iput-wide v10, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$totalSalesToday:D

    move-object/from16 v12, p12

    iput-object v12, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinInputForCorte$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v13, p13

    iput-object v13, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isVerifyingPin$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v14, p14

    iput-object v14, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$showPinModalForCorte$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v15, p15

    iput-object v15, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinErrorForCorte$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v1, p16

    iput-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isProcessingClose$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v1, p17

    iput-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cashierName$delegate:Landroidx/compose/runtime/State;

    move-object/from16 v1, p18

    iput-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$showCloseConfirmationDialog$delegate:Landroidx/compose/runtime/MutableState;

    const/4 v1, 0x2

    move-object/from16 v2, p19

    invoke-direct {v0, v1, v2}, Lkotlin/coroutines/jvm/internal/SuspendLambda;-><init>(ILkotlin/coroutines/Continuation;)V

    return-void
.end method

.method static final invokeSuspend$lambda$0(Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Lcom/example/data/entity/DailyCloseEntity;)Lkotlin/Unit;
    .locals 1
    .param p0, "$isProcessingClose$delegate"    # Landroidx/compose/runtime/MutableState;
    .param p1, "$showCloseConfirmationDialog$delegate"    # Landroidx/compose/runtime/MutableState;
    .param p2, "dailyClose"    # Lcom/example/data/entity/DailyCloseEntity;

    .line 621
    const/4 v0, 0x0

    invoke-static {p0, v0}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$81(Landroidx/compose/runtime/MutableState;Z)V

    .line 622
    invoke-static {p1, p2}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$78(Landroidx/compose/runtime/MutableState;Lcom/example/data/entity/DailyCloseEntity;)V

    .line 623
    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method


# virtual methods
.method public final create(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;
    .locals 21
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

    move-object/from16 v0, p0

    new-instance v1, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;

    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-wide v3, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$initialCashVal:D

    iget-wide v5, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$actualCashVal:D

    iget-wide v7, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cardSalesToday:D

    iget-wide v9, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$transferSalesToday:D

    iget-wide v11, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$totalSalesToday:D

    iget-object v13, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinInputForCorte$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v14, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isVerifyingPin$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v15, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$showPinModalForCorte$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v16, v1

    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinErrorForCorte$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v17, v1

    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isProcessingClose$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v18, v1

    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cashierName$delegate:Landroidx/compose/runtime/State;

    move-object/from16 v19, v1

    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$showCloseConfirmationDialog$delegate:Landroidx/compose/runtime/MutableState;

    move-object/from16 v20, v19

    move-object/from16 v19, v1

    move-object/from16 v1, v16

    move-object/from16 v16, v17

    move-object/from16 v17, v18

    move-object/from16 v18, v20

    move-object/from16 v20, p2

    invoke-direct/range {v1 .. v20}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;-><init>(Lcom/example/ui/viewmodel/RestaurantViewModel;DDDDDLandroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Lkotlin/coroutines/Continuation;)V

    move-object/from16 v16, v1

    move-object/from16 v1, v16

    check-cast v1, Lkotlin/coroutines/Continuation;

    return-object v1
.end method

.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    check-cast p1, Lkotlinx/coroutines/CoroutineScope;

    check-cast p2, Lkotlin/coroutines/Continuation;

    invoke-virtual {p0, p1, p2}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->invoke(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;

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

    invoke-virtual {p0, p1, p2}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->create(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;

    move-result-object v0

    check-cast v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;

    sget-object v1, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    invoke-virtual {v0, v1}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->invokeSuspend(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v0

    return-object v0
.end method

.method public final invokeSuspend(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 18
    .param p1, "$result"    # Ljava/lang/Object;

    move-object/from16 v0, p0

    invoke-static {}, Lkotlin/coroutines/intrinsics/IntrinsicsKt;->getCOROUTINE_SUSPENDED()Ljava/lang/Object;

    move-result-object v1

    .line 605
    iget v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->label:I

    const/4 v3, 0x1

    packed-switch v2, :pswitch_data_0

    new-instance v1, Ljava/lang/IllegalStateException;

    const-string v2, "call to \'resume\' before \'invoke\' with coroutine"

    invoke-direct {v1, v2}, Ljava/lang/IllegalStateException;-><init>(Ljava/lang/String;)V

    throw v1

    :pswitch_0
    invoke-static/range {p1 .. p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    move-object/from16 v2, p1

    goto :goto_0

    :pswitch_1
    invoke-static/range {p1 .. p1}, Lkotlin/ResultKt;->throwOnFailure(Ljava/lang/Object;)V

    .line 606
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v4, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinInputForCorte$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v4}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$68(Landroidx/compose/runtime/MutableState;)Ljava/lang/String;

    move-result-object v4

    move-object v5, v0

    check-cast v5, Lkotlin/coroutines/Continuation;

    iput v3, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->label:I

    invoke-virtual {v2, v4, v5}, Lcom/example/ui/viewmodel/RestaurantViewModel;->validateManagerPin(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;

    move-result-object v2

    if-ne v2, v1, :cond_0

    .line 605
    return-object v1

    .line 606
    :cond_0
    :goto_0
    check-cast v2, Ljava/lang/Boolean;

    invoke-virtual {v2}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v1

    .line 607
    .local v1, "isValid":Z
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isVerifyingPin$delegate:Landroidx/compose/runtime/MutableState;

    const/4 v4, 0x0

    invoke-static {v2, v4}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$75(Landroidx/compose/runtime/MutableState;Z)V

    .line 608
    if-eqz v1, :cond_1

    .line 609
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$showPinModalForCorte$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v2, v4}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$66(Landroidx/compose/runtime/MutableState;Z)V

    .line 610
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinInputForCorte$delegate:Landroidx/compose/runtime/MutableState;

    const-string v4, ""

    invoke-static {v2, v4}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$69(Landroidx/compose/runtime/MutableState;Ljava/lang/String;)V

    .line 611
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinErrorForCorte$delegate:Landroidx/compose/runtime/MutableState;

    const/4 v4, 0x0

    invoke-static {v2, v4}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$72(Landroidx/compose/runtime/MutableState;Ljava/lang/String;)V

    .line 612
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isProcessingClose$delegate:Landroidx/compose/runtime/MutableState;

    invoke-static {v2, v3}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$81(Landroidx/compose/runtime/MutableState;Z)V

    .line 613
    iget-object v4, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    .line 614
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cashierName$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$3(Landroidx/compose/runtime/State;)Ljava/lang/String;

    move-result-object v5

    .line 615
    iget-wide v6, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$initialCashVal:D

    .line 616
    iget-wide v8, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$actualCashVal:D

    .line 617
    iget-wide v10, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cardSalesToday:D

    .line 618
    iget-wide v12, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$transferSalesToday:D

    .line 619
    iget-wide v14, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$totalSalesToday:D

    .line 613
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$isProcessingClose$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v3, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$showCloseConfirmationDialog$delegate:Landroidx/compose/runtime/MutableState;

    move/from16 v17, v1

    .end local v1    # "isValid":Z
    .local v17, "isValid":Z
    new-instance v1, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1$$ExternalSyntheticLambda0;

    invoke-direct {v1, v2, v3}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1$$ExternalSyntheticLambda0;-><init>(Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V

    move-object/from16 v16, v1

    invoke-virtual/range {v4 .. v16}, Lcom/example/ui/viewmodel/RestaurantViewModel;->recordCorteDeCaja(Ljava/lang/String;DDDDDLkotlin/jvm/functions/Function1;)V

    goto :goto_1

    .line 626
    .end local v17    # "isValid":Z
    .restart local v1    # "isValid":Z
    :cond_1
    move/from16 v17, v1

    .end local v1    # "isValid":Z
    .restart local v17    # "isValid":Z
    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$pinErrorForCorte$delegate:Landroidx/compose/runtime/MutableState;

    const-string v2, "PIN no v\u00e1lido. No se realiz\u00f3 el corte"

    invoke-static {v1, v2}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$134$lambda$133$lambda$72(Landroidx/compose/runtime/MutableState;Ljava/lang/String;)V

    .line 627
    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    .line 628
    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$executeCortePinSubmit$1$1$1;->$cashierName$delegate:Landroidx/compose/runtime/State;

    invoke-static {v2}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$3(Landroidx/compose/runtime/State;)Ljava/lang/String;

    move-result-object v2

    .line 629
    nop

    .line 630
    nop

    .line 627
    const-string v3, "CAJA"

    const-string v4, "Corte de Caja Diario"

    invoke-virtual {v1, v2, v3, v4}, Lcom/example/ui/viewmodel/RestaurantViewModel;->logFailedPinAttempt(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V

    .line 633
    :goto_1
    sget-object v1, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v1

    :pswitch_data_0
    .packed-switch 0x0
        :pswitch_1
        :pswitch_0
    .end packed-switch
.end method
