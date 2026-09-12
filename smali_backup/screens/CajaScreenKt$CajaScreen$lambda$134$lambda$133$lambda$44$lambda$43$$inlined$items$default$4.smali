.class public final Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/CajaScreenKt;->CajaScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x19
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Lkotlin/jvm/internal/Lambda;",
        "Lkotlin/jvm/functions/Function4<",
        "Landroidx/compose/foundation/lazy/LazyItemScope;",
        "Ljava/lang/Integer;",
        "Landroidx/compose/runtime/Composer;",
        "Ljava/lang/Integer;",
        "Lkotlin/Unit;",
        ">;"
    }
.end annotation

.annotation system Ldalvik/annotation/SourceDebugExtension;
    value = "SMAP\nLazyDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt$items$4\n+ 2 CajaScreen.kt\ncom/example/ui/screens/CajaScreenKt\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,433:1\n174#2,4:434\n178#2:444\n182#2:451\n1225#3,6:438\n1225#3,6:445\n*S KotlinDebug\n*F\n+ 1 CajaScreen.kt\ncom/example/ui/screens/CajaScreenKt\n*L\n177#1:438,6\n178#1:445,6\n*E\n"
.end annotation

.annotation runtime Lkotlin/Metadata;
    d1 = {
        "\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\u0008\u0000\u0010\u0002*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u000b\u00a2\u0006\u0004\u0008\u0006\u0010\u0007\u00a8\u0006\u0008"
    }
    d2 = {
        "<anonymous>",
        "",
        "T",
        "Landroidx/compose/foundation/lazy/LazyItemScope;",
        "it",
        "",
        "invoke",
        "(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V",
        "androidx/compose/foundation/lazy/LazyDslKt$items$4"
    }
    k = 0x3
    mv = {
        0x2,
        0x2,
        0x0
    }
    xi = 0x30
.end annotation


# instance fields
.field final synthetic $cashierName$delegate$inlined:Landroidx/compose/runtime/State;

.field final synthetic $items:Ljava/util/List;

.field final synthetic $ticketToPrint$delegate$inlined:Landroidx/compose/runtime/MutableState;

.field final synthetic $viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public constructor <init>(Ljava/util/List;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$items:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p3, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$cashierName$delegate$inlined:Landroidx/compose/runtime/State;

    iput-object p4, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$ticketToPrint$delegate$inlined:Landroidx/compose/runtime/MutableState;

    const/4 p2, 0x4

    invoke-direct {p0, p2}, Lkotlin/jvm/internal/Lambda;-><init>(I)V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 4
    .param p1, "p1"    # Ljava/lang/Object;
    .param p2, "p2"    # Ljava/lang/Object;
    .param p3, "p3"    # Ljava/lang/Object;
    .param p4, "p4"    # Ljava/lang/Object;

    .line 152
    move-object v0, p1

    check-cast v0, Landroidx/compose/foundation/lazy/LazyItemScope;

    move-object v1, p2

    check-cast v1, Ljava/lang/Number;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    move-object v2, p3

    check-cast v2, Landroidx/compose/runtime/Composer;

    move-object v3, p4

    check-cast v3, Ljava/lang/Number;

    invoke-virtual {v3}, Ljava/lang/Number;->intValue()I

    move-result v3

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 20
    .param p1, "$this$items"    # Landroidx/compose/foundation/lazy/LazyItemScope;
    .param p2, "it"    # I
    .param p3, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p4, "$changed"    # I

    move-object/from16 v0, p0

    move/from16 v1, p2

    move-object/from16 v2, p3

    const-string v3, "C152@7074L22:LazyDsl.kt#428nma"

    invoke-static {v2, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    move/from16 v3, p4

    .local v3, "$dirty":I
    and-int/lit8 v4, p4, 0x6

    if-nez v4, :cond_1

    move-object/from16 v4, p1

    invoke-interface {v2, v4}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v5

    if-eqz v5, :cond_0

    const/4 v5, 0x4

    goto :goto_0

    :cond_0
    const/4 v5, 0x2

    :goto_0
    or-int/2addr v3, v5

    goto :goto_1

    :cond_1
    move-object/from16 v4, p1

    :goto_1
    and-int/lit8 v5, p4, 0x30

    const/16 v6, 0x20

    if-nez v5, :cond_3

    invoke-interface {v2, v1}, Landroidx/compose/runtime/Composer;->changed(I)Z

    move-result v5

    if-eqz v5, :cond_2

    move v5, v6

    goto :goto_2

    :cond_2
    const/16 v5, 0x10

    :goto_2
    or-int/2addr v3, v5

    .line 153
    :cond_3
    and-int/lit16 v5, v3, 0x93

    const/16 v7, 0x92

    if-ne v5, v7, :cond_5

    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v5

    if-nez v5, :cond_4

    goto :goto_3

    :cond_4
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    move/from16 v17, v3

    goto/16 :goto_a

    :cond_5
    :goto_3
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v5

    if-eqz v5, :cond_6

    const/4 v5, -0x1

    const-string v7, "androidx.compose.foundation.lazy.items.<anonymous> (LazyDsl.kt:152)"

    const v8, -0x25b7f321

    invoke-static {v8, v3, v5, v7}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    :cond_6
    iget-object v5, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$items:Ljava/util/List;

    invoke-interface {v5, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    and-int/lit8 v7, v3, 0xe

    .local v7, "$changed\\1":I
    move-object v8, v5

    check-cast v8, Lcom/example/data/entity/OrderEntity;

    .local v8, "order\\1":Lcom/example/data/entity/OrderEntity;
    move-object/from16 v5, p1

    .local v5, "$this$CajaScreen_u24lambda_u24134_u24lambda_u24133_u24lambda_u2444_u24lambda_u2443_u24lambda_u2442\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    move-object/from16 v12, p3

    .local v12, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/4 v15, 0x0

    .line 434
    .local v15, "$i$a$-items$default-CajaScreenKt$CajaScreen$3$1$2$1$2\\1\\153\\0":I
    const v9, 0x6c6cee73

    invoke-interface {v12, v9}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v9, "C*176@7953L44,177@8056L156,173@7773L473:CajaScreen.kt#2thlc2"

    invoke-static {v12, v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    .line 435
    nop

    .line 436
    iget-object v9, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    .line 437
    const v10, 0x14039863

    const-string v11, "CC(remember):CajaScreen.kt#9igjgp"

    invoke-static {v12, v10, v11}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    iget-object v10, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    invoke-interface {v12, v10}, Landroidx/compose/runtime/Composer;->changedInstance(Ljava/lang/Object;)Z

    move-result v10

    and-int/lit8 v13, v7, 0x70

    xor-int/lit8 v13, v13, 0x30

    const/16 v16, 0x1

    if-le v13, v6, :cond_7

    invoke-interface {v12, v8}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v13

    if-nez v13, :cond_8

    :cond_7
    and-int/lit8 v13, v7, 0x30

    if-ne v13, v6, :cond_9

    :cond_8
    move/from16 v13, v16

    goto :goto_4

    :cond_9
    const/4 v13, 0x0

    :goto_4
    or-int/2addr v10, v13

    .local v10, "invalid\\2":Z
    move-object v13, v12

    .local v13, "$this$cache\\2":Landroidx/compose/runtime/Composer;
    const/16 v17, 0x0

    .line 438
    .local v17, "$i$f$cache\\2\\437":I
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v14

    .local v14, "it\\2":Ljava/lang/Object;
    const/16 v18, 0x0

    .line 439
    .local v18, "$i$a$-let-ComposerKt$cache$1\\3\\438\\2":I
    if-nez v10, :cond_b

    sget-object v19, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual/range {v19 .. v19}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v6

    if-ne v14, v6, :cond_a

    goto :goto_5

    .line 443
    :cond_a
    move-object v1, v14

    goto :goto_6

    .line 440
    :cond_b
    :goto_5
    const/4 v6, 0x0

    .line 437
    .local v6, "$i$a$-cache-CajaScreenKt$CajaScreen$3$1$2$1$2$1\\4\\440\\1":I
    new-instance v1, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$1$1;

    iget-object v2, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    invoke-direct {v1, v2, v8}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$1$1;-><init>(Lcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/data/entity/OrderEntity;)V

    check-cast v1, Lkotlin/jvm/functions/Function0;

    .line 440
    .end local v6    # "$i$a$-cache-CajaScreenKt$CajaScreen$3$1$2$1$2$1\\4\\440\\1":I
    nop

    .line 441
    .local v1, "value\\3":Ljava/lang/Object;
    invoke-interface {v13, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 442
    nop

    .line 439
    .end local v1    # "value\\3":Ljava/lang/Object;
    :goto_6
    nop

    .line 438
    .end local v14    # "it\\2":Ljava/lang/Object;
    .end local v18    # "$i$a$-let-ComposerKt$cache$1\\3\\438\\2":I
    nop

    .line 437
    .end local v10    # "invalid\\2":Z
    .end local v13    # "$this$cache\\2":Landroidx/compose/runtime/Composer;
    .end local v17    # "$i$f$cache\\2\\437":I
    move-object v10, v1

    check-cast v10, Lkotlin/jvm/functions/Function0;

    invoke-static {v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 444
    const v1, 0x1403a5b3

    invoke-static {v12, v1, v11}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    and-int/lit8 v1, v7, 0x70

    xor-int/lit8 v1, v1, 0x30

    const/16 v2, 0x20

    if-le v1, v2, :cond_c

    invoke-interface {v12, v8}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_d

    :cond_c
    and-int/lit8 v1, v7, 0x30

    const/16 v2, 0x20

    if-ne v1, v2, :cond_e

    :cond_d
    move/from16 v14, v16

    goto :goto_7

    :cond_e
    const/4 v14, 0x0

    :goto_7
    iget-object v1, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$cashierName$delegate$inlined:Landroidx/compose/runtime/State;

    invoke-interface {v12, v1}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v1

    or-int/2addr v1, v14

    .local v1, "invalid\\5":Z
    move-object v2, v12

    .local v2, "$this$cache\\5":Landroidx/compose/runtime/Composer;
    const/4 v6, 0x0

    .line 445
    .local v6, "$i$f$cache\\5\\444":I
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v11

    .local v11, "it\\5":Ljava/lang/Object;
    const/4 v13, 0x0

    .line 446
    .local v13, "$i$a$-let-ComposerKt$cache$1\\6\\445\\5":I
    if-nez v1, :cond_10

    sget-object v14, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual {v14}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v14

    if-ne v11, v14, :cond_f

    goto :goto_8

    .line 450
    :cond_f
    move/from16 v16, v1

    move/from16 v17, v3

    move-object v1, v11

    goto :goto_9

    .line 447
    :cond_10
    :goto_8
    const/4 v14, 0x0

    .line 444
    .local v14, "$i$a$-cache-CajaScreenKt$CajaScreen$3$1$2$1$2$2\\7\\447\\1":I
    move/from16 v16, v1

    .end local v1    # "invalid\\5":Z
    .local v16, "invalid\\5":Z
    new-instance v1, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;

    move/from16 v17, v3

    .end local v3    # "$dirty":I
    .local v17, "$dirty":I
    iget-object v3, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$cashierName$delegate$inlined:Landroidx/compose/runtime/State;

    iget-object v4, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43$$inlined$items$default$4;->$ticketToPrint$delegate$inlined:Landroidx/compose/runtime/MutableState;

    invoke-direct {v1, v8, v3, v4}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;-><init>(Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V

    check-cast v1, Lkotlin/jvm/functions/Function1;

    .line 447
    .end local v14    # "$i$a$-cache-CajaScreenKt$CajaScreen$3$1$2$1$2$2\\7\\447\\1":I
    nop

    .line 448
    .local v1, "value\\6":Ljava/lang/Object;
    invoke-interface {v2, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 449
    nop

    .line 446
    .end local v1    # "value\\6":Ljava/lang/Object;
    :goto_9
    nop

    .line 445
    .end local v11    # "it\\5":Ljava/lang/Object;
    .end local v13    # "$i$a$-let-ComposerKt$cache$1\\6\\445\\5":I
    nop

    .line 444
    .end local v2    # "$this$cache\\5":Landroidx/compose/runtime/Composer;
    .end local v6    # "$i$f$cache\\5\\444":I
    .end local v16    # "invalid\\5":Z
    move-object v11, v1

    check-cast v11, Lkotlin/jvm/functions/Function1;

    invoke-static {v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    shr-int/lit8 v1, v7, 0x3

    and-int/lit8 v13, v1, 0xe

    .line 434
    const/4 v14, 0x0

    invoke-static/range {v8 .. v14}, Lcom/example/ui/screens/CajaScreenKt;->CashierOrderCard(Lcom/example/data/entity/OrderEntity;Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;II)V

    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 451
    nop

    .line 153
    .end local v5    # "$this$CajaScreen_u24lambda_u24134_u24lambda_u24133_u24lambda_u2444_u24lambda_u2443_u24lambda_u2442\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    .end local v7    # "$changed\\1":I
    .end local v8    # "order\\1":Lcom/example/data/entity/OrderEntity;
    .end local v12    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v15    # "$i$a$-items$default-CajaScreenKt$CajaScreen$3$1$2$1$2\\1\\153\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_11

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 154
    :cond_11
    :goto_a
    return-void
.end method
