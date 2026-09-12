.class public final Lcom/example/ui/screens/MeseroScreenKt$TableDetailDialog$lambda$328$lambda$327$lambda$326$lambda$325$$inlined$items$default$4;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/MeseroScreenKt;->TableDetailDialog(Lcom/example/ui/screens/RestaurantTableInfo;Lcom/example/ui/screens/TableStatusType;Lcom/example/data/entity/OrderEntity;Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nLazyDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt$items$4\n+ 2 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n+ 3 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 4 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 5 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 6 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 7 Composer.kt\nandroidx/compose/runtime/Updater\n*L\n1#1,433:1\n1932#2,4:434\n1936#2:439\n1938#2,9:475\n1947#2:488\n148#3:438\n98#4:440\n96#4,5:441\n101#4:474\n105#4:487\n78#5,6:446\n85#5,4:461\n89#5,2:471\n93#5:486\n368#6,9:452\n377#6:473\n378#6,2:484\n4032#7,6:465\n*S KotlinDebug\n*F\n+ 1 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n*L\n1935#1:438\n1932#1:440\n1932#1:441,5\n1932#1:474\n1932#1:487\n1932#1:446,6\n1932#1:461,4\n1932#1:471,2\n1932#1:486\n1932#1:452,9\n1932#1:473\n1932#1:484,2\n1932#1:465,6\n*E\n"
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
.field final synthetic $items:Ljava/util/List;


# direct methods
.method public constructor <init>(Ljava/util/List;)V
    .locals 1

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$TableDetailDialog$lambda$328$lambda$327$lambda$326$lambda$325$$inlined$items$default$4;->$items:Ljava/util/List;

    const/4 v0, 0x4

    invoke-direct {p0, v0}, Lkotlin/jvm/internal/Lambda;-><init>(I)V

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/MeseroScreenKt$TableDetailDialog$lambda$328$lambda$327$lambda$326$lambda$325$$inlined$items$default$4;->invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 88
    .param p1, "$this$items"    # Landroidx/compose/foundation/lazy/LazyItemScope;
    .param p2, "it"    # I
    .param p3, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p4, "$changed"    # I

    move/from16 v0, p2

    move-object/from16 v1, p3

    const-string v2, "C152@7074L22:LazyDsl.kt#428nma"

    invoke-static {v1, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    move/from16 v2, p4

    .local v2, "$dirty":I
    and-int/lit8 v3, p4, 0x6

    if-nez v3, :cond_1

    move-object/from16 v3, p1

    invoke-interface {v1, v3}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v4

    if-eqz v4, :cond_0

    const/4 v4, 0x4

    goto :goto_0

    :cond_0
    const/4 v4, 0x2

    :goto_0
    or-int/2addr v2, v4

    goto :goto_1

    :cond_1
    move-object/from16 v3, p1

    :goto_1
    and-int/lit8 v4, p4, 0x30

    if-nez v4, :cond_3

    invoke-interface {v1, v0}, Landroidx/compose/runtime/Composer;->changed(I)Z

    move-result v4

    if-eqz v4, :cond_2

    const/16 v4, 0x20

    goto :goto_2

    :cond_2
    const/16 v4, 0x10

    :goto_2
    or-int/2addr v2, v4

    .line 153
    :cond_3
    and-int/lit16 v4, v2, 0x93

    const/16 v5, 0x92

    if-ne v4, v5, :cond_5

    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v4

    if-nez v4, :cond_4

    goto :goto_3

    :cond_4
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    move/from16 v21, v2

    goto/16 :goto_7

    :cond_5
    :goto_3
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v4

    if-eqz v4, :cond_6

    const/4 v4, -0x1

    const-string v5, "androidx.compose.foundation.lazy.items.<anonymous> (LazyDsl.kt:152)"

    const v6, -0x25b7f321

    invoke-static {v6, v2, v4, v5}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    :cond_6
    move-object/from16 v4, p0

    iget-object v5, v4, Lcom/example/ui/screens/MeseroScreenKt$TableDetailDialog$lambda$328$lambda$327$lambda$326$lambda$325$$inlined$items$default$4;->$items:Ljava/util/List;

    invoke-interface {v5, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    and-int/lit8 v6, v2, 0xe

    .local v6, "$changed\\1":I
    check-cast v5, Lcom/example/data/entity/OrderItemEntity;

    .local v5, "item\\1":Lcom/example/data/entity/OrderItemEntity;
    move-object/from16 v7, p1

    .local v7, "$this$TableDetailDialog_u24lambda_u24328_u24lambda_u24327_u24lambda_u24326_u24lambda_u24325_u24lambda_u24324\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    move-object/from16 v8, p3

    .local v8, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/4 v9, 0x0

    .line 434
    .local v9, "$i$a$-items$default-MeseroScreenKt$TableDetailDialog$5$1$2$1$1\\1\\153\\0":I
    const v10, 0x67db1d11

    invoke-interface {v8, v10}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v10, "C*1931@88420L816:MeseroScreen.kt#2thlc2"

    invoke-static {v8, v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    .line 435
    sget-object v10, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v10, Landroidx/compose/ui/Modifier;

    .line 436
    const/4 v11, 0x0

    const/4 v12, 0x1

    const/4 v13, 0x0

    invoke-static {v10, v11, v12, v13}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v10

    .line 437
    const/4 v14, 0x4

    .local v14, "$this$dp\\2":I
    const/4 v15, 0x0

    .line 438
    .local v15, "$i$f$getDp\\2\\437":I
    int-to-float v11, v14

    invoke-static {v11}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v11

    .line 437
    .end local v14    # "$this$dp\\2":I
    .end local v15    # "$i$f$getDp\\2\\437":I
    const/4 v14, 0x0

    invoke-static {v10, v14, v11, v12, v13}, Landroidx/compose/foundation/layout/PaddingKt;->padding-VpY3zN4$default(Landroidx/compose/ui/Modifier;FFILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v10

    .line 439
    sget-object v11, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    invoke-virtual {v11}, Landroidx/compose/foundation/layout/Arrangement;->getSpaceBetween()Landroidx/compose/foundation/layout/Arrangement$HorizontalOrVertical;

    move-result-object v11

    check-cast v11, Landroidx/compose/foundation/layout/Arrangement$Horizontal;

    .line 434
    const/16 v12, 0x36

    .local v11, "horizontalArrangement\\3":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .local v12, "$changed\\3":I
    move-object v13, v8

    .local v10, "modifier\\3":Landroidx/compose/ui/Modifier;
    .local v13, "$composer\\3":Landroidx/compose/runtime/Composer;
    const/4 v14, 0x0

    .line 440
    .local v14, "$i$f$Row\\3\\434":I
    const v15, 0x2952b718

    const-string v0, "CC(Row)P(2,1,3)98@4939L58,99@5002L130:Row.kt#2w3rfo"

    invoke-static {v13, v15, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 441
    sget-object v0, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/Alignment$Companion;->getTop()Landroidx/compose/ui/Alignment$Vertical;

    move-result-object v0

    .line 444
    .local v0, "verticalAlignment\\3":Landroidx/compose/ui/Alignment$Vertical;
    shr-int/lit8 v15, v12, 0x3

    and-int/lit8 v15, v15, 0xe

    shr-int/lit8 v16, v12, 0x3

    and-int/lit8 v16, v16, 0x70

    or-int v15, v15, v16

    invoke-static {v11, v0, v13, v15}, Landroidx/compose/foundation/layout/RowKt;->rowMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Horizontal;Landroidx/compose/ui/Alignment$Vertical;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v15

    .local v15, "measurePolicy\\3":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v16, v12, 0x3

    and-int/lit8 v16, v16, 0x70

    .line 445
    nop

    .local v16, "$changed\\4":I
    const/16 v17, 0x0

    .line 446
    .local v17, "$i$f$Layout\\4\\445":I
    move-object/from16 v18, v0

    .end local v0    # "verticalAlignment\\3":Landroidx/compose/ui/Alignment$Vertical;
    .local v18, "verticalAlignment\\3":Landroidx/compose/ui/Alignment$Vertical;
    const v0, -0x4ee9b9da

    const-string v1, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh"

    invoke-static {v13, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 447
    const/4 v0, 0x0

    invoke-static {v13, v0}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v0

    .line 448
    .local v0, "compositeKeyHash\\4":I
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v1

    .line 449
    .local v1, "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    move/from16 v19, v0

    .end local v0    # "compositeKeyHash\\4":I
    .local v19, "compositeKeyHash\\4":I
    invoke-static {v13, v10}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v0

    .line 451
    .local v0, "materialized\\4":Landroidx/compose/ui/Modifier;
    sget-object v20, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v20 .. v20}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v20

    move/from16 v21, v2

    .end local v2    # "$dirty":I
    .local v21, "$dirty":I
    shl-int/lit8 v2, v16, 0x6

    and-int/lit16 v2, v2, 0x380

    or-int/lit8 v2, v2, 0x6

    .line 450
    nop

    .local v2, "$changed\\5":I
    move-object/from16 v22, v20

    .local v22, "factory\\5":Lkotlin/jvm/functions/Function0;
    const/16 v20, 0x0

    .line 452
    .local v20, "$i$f$ReusableComposeNode\\5\\450":I
    move/from16 v23, v2

    .end local v2    # "$changed\\5":I
    .local v23, "$changed\\5":I
    const v2, -0x2942ffcf

    const-string v3, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp"

    invoke-static {v13, v2, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 453
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v2

    instance-of v2, v2, Landroidx/compose/runtime/Applier;

    if-nez v2, :cond_7

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 454
    :cond_7
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 455
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v2

    if-eqz v2, :cond_8

    .line 456
    move-object/from16 v2, v22

    .end local v22    # "factory\\5":Lkotlin/jvm/functions/Function0;
    .local v2, "factory\\5":Lkotlin/jvm/functions/Function0;
    invoke-interface {v13, v2}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_4

    .line 458
    .end local v2    # "factory\\5":Lkotlin/jvm/functions/Function0;
    .restart local v22    # "factory\\5":Lkotlin/jvm/functions/Function0;
    :cond_8
    move-object/from16 v2, v22

    .end local v22    # "factory\\5":Lkotlin/jvm/functions/Function0;
    .restart local v2    # "factory\\5":Lkotlin/jvm/functions/Function0;
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 460
    :goto_4
    invoke-static {v13}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v3

    .local v3, "$this$Layout_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    const/16 v22, 0x0

    .line 461
    .local v22, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\6\\460\\4":I
    sget-object v24, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    move-object/from16 v25, v2

    .end local v2    # "factory\\5":Lkotlin/jvm/functions/Function0;
    .local v25, "factory\\5":Lkotlin/jvm/functions/Function0;
    invoke-virtual/range {v24 .. v24}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    invoke-static {v3, v15, v2}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 462
    sget-object v2, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v2}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    invoke-static {v3, v1, v2}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 464
    sget-object v2, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v2}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    .local v2, "block\\7":Lkotlin/jvm/functions/Function2;
    const/16 v24, 0x0

    .line 465
    .local v24, "$i$f$set-impl\\7\\464":I
    move-object/from16 v26, v3

    .local v26, "$this$set_impl_u24lambda_u240\\7":Landroidx/compose/runtime/Composer;
    const/16 v27, 0x0

    .line 466
    .local v27, "$i$a$-with-Updater$set$1\\8\\465\\7":I
    invoke-interface/range {v26 .. v26}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v28

    if-nez v28, :cond_a

    move-object/from16 v28, v1

    .end local v1    # "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    .local v28, "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-interface/range {v26 .. v26}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v1

    invoke-static/range {v19 .. v19}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    invoke-static {v1, v4}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_9

    goto :goto_5

    :cond_9
    move-object/from16 v4, v26

    goto :goto_6

    .end local v28    # "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    .restart local v1    # "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    :cond_a
    move-object/from16 v28, v1

    .line 467
    .end local v1    # "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    .restart local v28    # "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    :goto_5
    invoke-static/range {v19 .. v19}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    move-object/from16 v4, v26

    .end local v26    # "$this$set_impl_u24lambda_u240\\7":Landroidx/compose/runtime/Composer;
    .local v4, "$this$set_impl_u24lambda_u240\\7":Landroidx/compose/runtime/Composer;
    invoke-interface {v4, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 468
    invoke-static/range {v19 .. v19}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-interface {v3, v1, v2}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 470
    :goto_6
    nop

    .line 465
    .end local v4    # "$this$set_impl_u24lambda_u240\\7":Landroidx/compose/runtime/Composer;
    .end local v27    # "$i$a$-with-Updater$set$1\\8\\465\\7":I
    nop

    .line 470
    nop

    .line 471
    .end local v2    # "block\\7":Lkotlin/jvm/functions/Function2;
    .end local v24    # "$i$f$set-impl\\7\\464":I
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v0, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 472
    nop

    .line 460
    .end local v3    # "$this$Layout_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .end local v22    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\6\\460\\4":I
    nop

    .line 473
    shr-int/lit8 v1, v23, 0x6

    and-int/lit8 v1, v1, 0xe

    .local v1, "$changed\\9":I
    move-object v2, v13

    .local v2, "$composer\\9":Landroidx/compose/runtime/Composer;
    const/4 v3, 0x0

    .line 474
    .local v3, "$i$a$-Layout-RowKt$Row$1\\9\\473\\3":I
    const v4, -0x18505826

    move-object/from16 v22, v0

    .end local v0    # "materialized\\4":Landroidx/compose/ui/Modifier;
    .local v22, "materialized\\4":Landroidx/compose/ui/Modifier;
    const-string v0, "C100@5047L9:Row.kt#2w3rfo"

    invoke-static {v2, v4, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/RowScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/RowScopeInstance;

    shr-int/lit8 v4, v12, 0x6

    and-int/lit8 v4, v4, 0x70

    or-int/lit8 v4, v4, 0x6

    .local v4, "$changed\\10":I
    check-cast v0, Landroidx/compose/foundation/layout/RowScope;

    .local v0, "$this$TableDetailDialog_u24lambda_u24328_u24lambda_u24327_u24lambda_u24326_u24lambda_u24325_u24lambda_u24324_u24lambda_u24323\\10":Landroidx/compose/foundation/layout/RowScope;
    move-object/from16 v50, v2

    .local v50, "$composer\\10":Landroidx/compose/runtime/Composer;
    const/16 v24, 0x0

    .line 475
    .local v24, "$i$a$-Row-MeseroScreenKt$TableDetailDialog$5$1$2$1$1$1\\10\\474\\1":I
    move-object/from16 v26, v0

    .end local v0    # "$this$TableDetailDialog_u24lambda_u24328_u24lambda_u24327_u24lambda_u24326_u24lambda_u24325_u24lambda_u24324_u24lambda_u24323\\10":Landroidx/compose/foundation/layout/RowScope;
    .local v26, "$this$TableDetailDialog_u24lambda_u24328_u24lambda_u24327_u24lambda_u24326_u24lambda_u24325_u24lambda_u24324_u24lambda_u24323\\10":Landroidx/compose/foundation/layout/RowScope;
    const v0, 0xe6faba8

    move/from16 v27, v1

    .end local v1    # "$changed\\9":I
    .local v27, "$changed\\9":I
    const-string v1, "C1939@88885L10,1937@88737L203,1943@89112L10,1941@88973L233:MeseroScreen.kt#2thlc2"

    move-object/from16 v54, v2

    .end local v50    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .local v2, "$composer\\10":Landroidx/compose/runtime/Composer;
    .local v54, "$composer\\9":Landroidx/compose/runtime/Composer;
    invoke-static {v2, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 476
    invoke-virtual {v5}, Lcom/example/data/entity/OrderItemEntity;->getQuantity()I

    move-result v0

    invoke-virtual {v5}, Lcom/example/data/entity/OrderItemEntity;->getProductName()Ljava/lang/String;

    move-result-object v1

    move/from16 v55, v3

    .end local v3    # "$i$a$-Layout-RowKt$Row$1\\9\\473\\3":I
    .local v55, "$i$a$-Layout-RowKt$Row$1\\9\\473\\3":I
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v3, "x "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v29

    .line 477
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getBodyMedium()Landroidx/compose/ui/text/TextStyle;

    move-result-object v49

    .line 475
    const/16 v30, 0x0

    const-wide/16 v31, 0x0

    const-wide/16 v33, 0x0

    const/16 v35, 0x0

    const/16 v36, 0x0

    const/16 v37, 0x0

    const-wide/16 v38, 0x0

    const/16 v40, 0x0

    const/16 v41, 0x0

    const-wide/16 v42, 0x0

    const/16 v44, 0x0

    const/16 v45, 0x0

    const/16 v46, 0x0

    const/16 v47, 0x0

    const/16 v48, 0x0

    const/16 v51, 0x0

    const/16 v52, 0x0

    const v53, 0xfffe

    .end local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v50    # "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-static/range {v29 .. v53}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 480
    .end local v50    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-virtual {v5}, Lcom/example/data/entity/OrderItemEntity;->getSubtotal()D

    move-result-wide v0

    invoke-static {v0, v1}, Lcom/example/ui/components/CommonComponentsKt;->formatQuetzales(D)Ljava/lang/String;

    move-result-object v29

    .line 481
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getBodyMedium()Landroidx/compose/ui/text/TextStyle;

    move-result-object v56

    sget-object v0, Landroidx/compose/ui/text/font/FontWeight;->Companion:Landroidx/compose/ui/text/font/FontWeight$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/text/font/FontWeight$Companion;->getSemiBold()Landroidx/compose/ui/text/font/FontWeight;

    move-result-object v61

    const v86, 0xfffffb

    const/16 v87, 0x0

    const-wide/16 v57, 0x0

    const-wide/16 v59, 0x0

    const/16 v62, 0x0

    const/16 v63, 0x0

    const/16 v64, 0x0

    const/16 v65, 0x0

    const-wide/16 v66, 0x0

    const/16 v68, 0x0

    const/16 v69, 0x0

    const/16 v70, 0x0

    const-wide/16 v71, 0x0

    const/16 v73, 0x0

    const/16 v74, 0x0

    const/16 v75, 0x0

    const/16 v76, 0x0

    const/16 v77, 0x0

    const-wide/16 v78, 0x0

    const/16 v80, 0x0

    const/16 v81, 0x0

    const/16 v82, 0x0

    const/16 v83, 0x0

    const/16 v84, 0x0

    const/16 v85, 0x0

    invoke-static/range {v56 .. v87}, Landroidx/compose/ui/text/TextStyle;->copy-p1EtxEg$default(Landroidx/compose/ui/text/TextStyle;JJLandroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontSynthesis;Landroidx/compose/ui/text/font/FontFamily;Ljava/lang/String;JLandroidx/compose/ui/text/style/BaselineShift;Landroidx/compose/ui/text/style/TextGeometricTransform;Landroidx/compose/ui/text/intl/LocaleList;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/graphics/Shadow;Landroidx/compose/ui/graphics/drawscope/DrawStyle;IIJLandroidx/compose/ui/text/style/TextIndent;Landroidx/compose/ui/text/PlatformTextStyle;Landroidx/compose/ui/text/style/LineHeightStyle;IILandroidx/compose/ui/text/style/TextMotion;ILjava/lang/Object;)Landroidx/compose/ui/text/TextStyle;

    move-result-object v49

    .line 479
    nop

    .end local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v50    # "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-static/range {v29 .. v53}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 475
    .end local v50    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-static {v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 483
    nop

    .line 474
    .end local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .end local v4    # "$changed\\10":I
    .end local v24    # "$i$a$-Row-MeseroScreenKt$TableDetailDialog$5$1$2$1$1$1\\10\\474\\1":I
    .end local v26    # "$this$TableDetailDialog_u24lambda_u24328_u24lambda_u24327_u24lambda_u24326_u24lambda_u24325_u24lambda_u24324_u24lambda_u24323\\10":Landroidx/compose/foundation/layout/RowScope;
    invoke-static/range {v54 .. v54}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 473
    .end local v27    # "$changed\\9":I
    .end local v54    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .end local v55    # "$i$a$-Layout-RowKt$Row$1\\9\\473\\3":I
    nop

    .line 484
    invoke-interface {v13}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 452
    invoke-static {v13}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 485
    nop

    .line 446
    .end local v20    # "$i$f$ReusableComposeNode\\5\\450":I
    .end local v23    # "$changed\\5":I
    .end local v25    # "factory\\5":Lkotlin/jvm/functions/Function0;
    invoke-static {v13}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 486
    nop

    .line 440
    .end local v16    # "$changed\\4":I
    .end local v17    # "$i$f$Layout\\4\\445":I
    .end local v19    # "compositeKeyHash\\4":I
    .end local v22    # "materialized\\4":Landroidx/compose/ui/Modifier;
    .end local v28    # "localMap\\4":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static {v13}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 487
    nop

    .end local v10    # "modifier\\3":Landroidx/compose/ui/Modifier;
    .end local v11    # "horizontalArrangement\\3":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v12    # "$changed\\3":I
    .end local v13    # "$composer\\3":Landroidx/compose/runtime/Composer;
    .end local v14    # "$i$f$Row\\3\\434":I
    .end local v15    # "measurePolicy\\3":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v18    # "verticalAlignment\\3":Landroidx/compose/ui/Alignment$Vertical;
    invoke-interface {v8}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 488
    nop

    .line 153
    .end local v5    # "item\\1":Lcom/example/data/entity/OrderItemEntity;
    .end local v6    # "$changed\\1":I
    .end local v7    # "$this$TableDetailDialog_u24lambda_u24328_u24lambda_u24327_u24lambda_u24326_u24lambda_u24325_u24lambda_u24324\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    .end local v8    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v9    # "$i$a$-items$default-MeseroScreenKt$TableDetailDialog$5$1$2$1$1\\1\\153\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_b

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 154
    :cond_b
    :goto_7
    return-void
.end method
