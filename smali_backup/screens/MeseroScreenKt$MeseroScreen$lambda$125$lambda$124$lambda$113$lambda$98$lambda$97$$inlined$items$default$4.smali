.class public final Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nLazyDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt$items$4\n+ 2 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,433:1\n391#2,4:434\n395#2,11:444\n1225#3,6:438\n*S KotlinDebug\n*F\n+ 1 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n*L\n394#1:438,6\n*E\n"
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
.field final synthetic $allOrders$delegate$inlined:Landroidx/compose/runtime/State;

.field final synthetic $items:Ljava/util/List;

.field final synthetic $selectedTable$delegate$inlined:Landroidx/compose/runtime/State;

.field final synthetic $viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public constructor <init>(Ljava/util/List;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$items:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$allOrders$delegate$inlined:Landroidx/compose/runtime/State;

    iput-object p4, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$selectedTable$delegate$inlined:Landroidx/compose/runtime/State;

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 27
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

    const/4 v5, 0x4

    if-nez v4, :cond_1

    move-object/from16 v4, p1

    invoke-interface {v2, v4}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v6

    if-eqz v6, :cond_0

    move v6, v5

    goto :goto_0

    :cond_0
    const/4 v6, 0x2

    :goto_0
    or-int/2addr v3, v6

    goto :goto_1

    :cond_1
    move-object/from16 v4, p1

    :goto_1
    and-int/lit8 v6, p4, 0x30

    const/16 v7, 0x20

    if-nez v6, :cond_3

    invoke-interface {v2, v1}, Landroidx/compose/runtime/Composer;->changed(I)Z

    move-result v6

    if-eqz v6, :cond_2

    move v6, v7

    goto :goto_2

    :cond_2
    const/16 v6, 0x10

    :goto_2
    or-int/2addr v3, v6

    .line 153
    :cond_3
    and-int/lit16 v6, v3, 0x93

    const/16 v8, 0x92

    if-ne v6, v8, :cond_5

    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v6

    if-nez v6, :cond_4

    goto :goto_3

    :cond_4
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    goto/16 :goto_7

    :cond_5
    :goto_3
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v6

    if-eqz v6, :cond_6

    const/4 v6, -0x1

    const-string v8, "androidx.compose.foundation.lazy.items.<anonymous> (LazyDsl.kt:152)"

    const v9, -0x25b7f321

    invoke-static {v9, v3, v6, v8}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    :cond_6
    iget-object v6, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$items:Ljava/util/List;

    invoke-interface {v6, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    and-int/lit8 v8, v3, 0xe

    .local v8, "$changed\\1":I
    check-cast v6, Lcom/example/ui/screens/RestaurantTableInfo;

    .local v6, "tableInfo\\1":Lcom/example/ui/screens/RestaurantTableInfo;
    move-object/from16 v9, p1

    .local v9, "$this$MeseroScreen_u24lambda_u24125_u24lambda_u24124_u24lambda_u24113_u24lambda_u2498_u24lambda_u2497_u24lambda_u2496\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    move-object/from16 v10, p3

    .local v10, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/16 v26, 0x0

    .line 434
    .local v26, "$i$a$-items$default-MeseroScreenKt$MeseroScreen$4$1$5$2$1$1\\1\\153\\0":I
    const v11, 0x42c92730

    invoke-interface {v10, v11}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v11, "C*393@19856L46,394@19948L24,395@20024L397,391@19718L737:MeseroScreen.kt#2thlc2"

    invoke-static {v10, v11}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    invoke-virtual {v6}, Lcom/example/ui/screens/RestaurantTableInfo;->getName()Ljava/lang/String;

    move-result-object v11

    iget-object v12, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$allOrders$delegate$inlined:Landroidx/compose/runtime/State;

    invoke-static {v12}, Lcom/example/ui/screens/MeseroScreenKt;->access$MeseroScreen$lambda$10(Landroidx/compose/runtime/State;)Ljava/util/List;

    move-result-object v12

    const/4 v13, 0x0

    invoke-static {v11, v12, v13, v5, v13}, Lcom/example/ui/screens/MeseroScreenKt;->resolveTableStatus$default(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;ILjava/lang/Object;)Lkotlin/Pair;

    move-result-object v5

    invoke-virtual {v5}, Lkotlin/Pair;->component1()Ljava/lang/Object;

    move-result-object v5

    check-cast v5, Lcom/example/ui/screens/TableStatusType;

    .line 436
    .local v5, "status\\1":Lcom/example/ui/screens/TableStatusType;
    iget-object v11, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$selectedTable$delegate$inlined:Landroidx/compose/runtime/State;

    invoke-static {v11}, Lcom/example/ui/screens/MeseroScreenKt;->access$MeseroScreen$lambda$3(Landroidx/compose/runtime/State;)Ljava/lang/String;

    move-result-object v11

    invoke-virtual {v6}, Lcom/example/ui/screens/RestaurantTableInfo;->getName()Ljava/lang/String;

    move-result-object v12

    invoke-static {v11, v12}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v11

    .line 437
    const v12, -0x60f126a0

    const-string v13, "CC(remember):MeseroScreen.kt#9igjgp"

    invoke-static {v10, v12, v13}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    iget-object v12, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    invoke-interface {v10, v12}, Landroidx/compose/runtime/Composer;->changedInstance(Ljava/lang/Object;)Z

    move-result v12

    and-int/lit8 v13, v8, 0x70

    xor-int/lit8 v13, v13, 0x30

    if-le v13, v7, :cond_7

    invoke-interface {v10, v6}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v13

    if-nez v13, :cond_8

    :cond_7
    and-int/lit8 v13, v8, 0x30

    if-ne v13, v7, :cond_9

    :cond_8
    const/4 v7, 0x1

    goto :goto_4

    :cond_9
    const/4 v7, 0x0

    :goto_4
    or-int/2addr v7, v12

    .local v7, "invalid\\2":Z
    move-object v12, v10

    .local v12, "$this$cache\\2":Landroidx/compose/runtime/Composer;
    const/4 v13, 0x0

    .line 438
    .local v13, "$i$f$cache\\2\\437":I
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v15

    .local v15, "it\\2":Ljava/lang/Object;
    const/16 v16, 0x0

    .line 439
    .local v16, "$i$a$-let-ComposerKt$cache$1\\3\\438\\2":I
    if-nez v7, :cond_b

    sget-object v17, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual/range {v17 .. v17}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v14

    if-ne v15, v14, :cond_a

    goto :goto_5

    .line 443
    :cond_a
    move-object v1, v15

    goto :goto_6

    .line 440
    :cond_b
    :goto_5
    const/4 v14, 0x0

    .line 437
    .local v14, "$i$a$-cache-MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$1\\4\\440\\1":I
    new-instance v1, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$1$1;

    iget-object v2, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$98$lambda$97$$inlined$items$default$4;->$viewModel$inlined:Lcom/example/ui/viewmodel/RestaurantViewModel;

    invoke-direct {v1, v2, v6}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$1$1;-><init>(Lcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/ui/screens/RestaurantTableInfo;)V

    check-cast v1, Lkotlin/jvm/functions/Function0;

    .line 440
    .end local v14    # "$i$a$-cache-MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$1\\4\\440\\1":I
    nop

    .line 441
    .local v1, "value\\3":Ljava/lang/Object;
    invoke-interface {v12, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 442
    nop

    .line 439
    .end local v1    # "value\\3":Ljava/lang/Object;
    :goto_6
    nop

    .line 438
    .end local v15    # "it\\2":Ljava/lang/Object;
    .end local v16    # "$i$a$-let-ComposerKt$cache$1\\3\\438\\2":I
    nop

    .line 437
    .end local v7    # "invalid\\2":Z
    .end local v12    # "$this$cache\\2":Landroidx/compose/runtime/Composer;
    .end local v13    # "$i$f$cache\\2\\437":I
    check-cast v1, Lkotlin/jvm/functions/Function0;

    invoke-static {v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 444
    new-instance v2, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$2;

    invoke-direct {v2, v6}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$2;-><init>(Lcom/example/ui/screens/RestaurantTableInfo;)V

    const v7, 0x517c5505

    const/16 v12, 0x36

    const/4 v13, 0x1

    invoke-static {v7, v13, v2, v10, v12}, Landroidx/compose/runtime/internal/ComposableLambdaKt;->rememberComposableLambda(IZLjava/lang/Object;Landroidx/compose/runtime/Composer;I)Landroidx/compose/runtime/internal/ComposableLambda;

    move-result-object v2

    check-cast v2, Lkotlin/jvm/functions/Function2;

    .line 445
    new-instance v7, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$3;

    invoke-direct {v7, v5}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$2$1$1$3;-><init>(Lcom/example/ui/screens/TableStatusType;)V

    const v14, -0x606f8

    invoke-static {v14, v13, v7, v10, v12}, Landroidx/compose/runtime/internal/ComposableLambdaKt;->rememberComposableLambda(IZLjava/lang/Object;Landroidx/compose/runtime/Composer;I)Landroidx/compose/runtime/internal/ComposableLambda;

    move-result-object v7

    move-object v15, v7

    check-cast v15, Lkotlin/jvm/functions/Function2;

    .line 435
    const/4 v13, 0x0

    const/4 v14, 0x0

    const/16 v16, 0x0

    const/16 v17, 0x0

    const/16 v18, 0x0

    const/16 v19, 0x0

    const/16 v20, 0x0

    const/16 v21, 0x0

    const v23, 0x30180

    const/16 v24, 0x0

    const/16 v25, 0xfd8

    move-object v12, v2

    move-object/from16 v22, v10

    move v10, v11

    move-object v11, v1

    .end local v10    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .local v22, "$composer\\1":Landroidx/compose/runtime/Composer;
    invoke-static/range {v10 .. v25}, Landroidx/compose/material3/ChipKt;->FilterChip(ZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;Landroidx/compose/ui/Modifier;ZLkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Landroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/SelectableChipColors;Landroidx/compose/material3/SelectableChipElevation;Landroidx/compose/foundation/BorderStroke;Landroidx/compose/foundation/interaction/MutableInteractionSource;Landroidx/compose/runtime/Composer;III)V

    invoke-interface/range {v22 .. v22}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 454
    .end local v5    # "status\\1":Lcom/example/ui/screens/TableStatusType;
    nop

    .line 153
    .end local v6    # "tableInfo\\1":Lcom/example/ui/screens/RestaurantTableInfo;
    .end local v8    # "$changed\\1":I
    .end local v9    # "$this$MeseroScreen_u24lambda_u24125_u24lambda_u24124_u24lambda_u24113_u24lambda_u2498_u24lambda_u2497_u24lambda_u2496\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    .end local v22    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v26    # "$i$a$-items$default-MeseroScreenKt$MeseroScreen$4$1$5$2$1$1\\1\\153\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_c

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 154
    :cond_c
    :goto_7
    return-void
.end method
