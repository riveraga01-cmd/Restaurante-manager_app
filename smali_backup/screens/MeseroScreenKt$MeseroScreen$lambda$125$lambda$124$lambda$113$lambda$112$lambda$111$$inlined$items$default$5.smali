.class public final Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyGridDsl.kt"

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
        "Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;",
        "Ljava/lang/Integer;",
        "Landroidx/compose/runtime/Composer;",
        "Ljava/lang/Integer;",
        "Lkotlin/Unit;",
        ">;"
    }
.end annotation

.annotation system Ldalvik/annotation/SourceDebugExtension;
    value = "SMAP\nLazyGridDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyGridDsl.kt\nandroidx/compose/foundation/lazy/grid/LazyGridDslKt$items$5\n+ 2 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,569:1\n458#2,5:570\n466#2:581\n1225#3,6:575\n*S KotlinDebug\n*F\n+ 1 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n*L\n462#1:575,6\n*E\n"
.end annotation

.annotation runtime Lkotlin/Metadata;
    d1 = {
        "\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0003\u0010\u0000\u001a\u00020\u0001\"\u0004\u0008\u0000\u0010\u0002*\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u000b\u00a2\u0006\u0004\u0008\u0006\u0010\u0007\u00a8\u0006\u0008"
    }
    d2 = {
        "<anonymous>",
        "",
        "T",
        "Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;",
        "it",
        "",
        "invoke",
        "(Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;ILandroidx/compose/runtime/Composer;I)V",
        "androidx/compose/foundation/lazy/grid/LazyGridDslKt$items$5"
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
.field final synthetic $cartItems$delegate$inlined:Landroidx/compose/runtime/State;

.field final synthetic $items:Ljava/util/List;

.field final synthetic $selectedMenuItemForCustomization$delegate$inlined:Landroidx/compose/runtime/MutableState;


# direct methods
.method public constructor <init>(Ljava/util/List;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->$items:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->$cartItems$delegate$inlined:Landroidx/compose/runtime/State;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->$selectedMenuItemForCustomization$delegate$inlined:Landroidx/compose/runtime/MutableState;

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

    .line 464
    move-object v0, p1

    check-cast v0, Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->invoke(Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 17
    .param p1, "$this$items"    # Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;
    .param p2, "it"    # I
    .param p3, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p4, "$changed"    # I

    move-object/from16 v0, p0

    move/from16 v1, p2

    move-object/from16 v2, p3

    const-string v3, "C464@19670L22:LazyGridDsl.kt#7791vq"

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

    .line 465
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

    goto/16 :goto_7

    :cond_5
    :goto_3
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v5

    if-eqz v5, :cond_6

    const/4 v5, -0x1

    const-string v7, "androidx.compose.foundation.lazy.grid.items.<anonymous> (LazyGridDsl.kt:464)"

    const v8, 0x29b3c0fe

    invoke-static {v8, v3, v5, v7}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    :cond_6
    iget-object v5, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->$items:Ljava/util/List;

    invoke-interface {v5, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    and-int/lit8 v7, v3, 0xe

    .local v7, "$changed\\1":I
    check-cast v5, Lcom/example/data/entity/MenuItemEntity;

    .local v5, "item\\1":Lcom/example/data/entity/MenuItemEntity;
    move-object/from16 v8, p1

    .local v8, "$this$MeseroScreen_u24lambda_u24125_u24lambda_u24124_u24lambda_u24113_u24lambda_u24112_u24lambda_u24111_u24lambda_u24110\\1":Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;
    move-object/from16 v9, p3

    .local v9, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/4 v10, 0x0

    .line 570
    .local v10, "$i$a$-items$default-MeseroScreenKt$MeseroScreen$4$1$5$6$1$2\\1\\465\\0":I
    const v11, -0x40ae4c5

    invoke-interface {v9, v11}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v11, "C*461@23517L127,458@23326L356:MeseroScreen.kt#2thlc2"

    invoke-static {v9, v11}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    iget-object v11, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->$cartItems$delegate$inlined:Landroidx/compose/runtime/State;

    invoke-static {v11}, Lcom/example/ui/screens/MeseroScreenKt;->access$MeseroScreen$lambda$2(Landroidx/compose/runtime/State;)Ljava/util/Map;

    move-result-object v11

    invoke-interface {v11, v5}, Ljava/util/Map;->get(Ljava/lang/Object;)Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Lkotlin/Pair;

    const/4 v12, 0x0

    if-eqz v11, :cond_7

    invoke-virtual {v11}, Lkotlin/Pair;->getFirst()Ljava/lang/Object;

    move-result-object v11

    check-cast v11, Ljava/lang/Number;

    invoke-virtual {v11}, Ljava/lang/Number;->intValue()I

    move-result v11

    goto :goto_4

    :cond_7
    move v11, v12

    .line 572
    .local v11, "inCartQty\\1":I
    :goto_4
    nop

    .line 573
    nop

    .line 574
    const v13, -0x21418d

    const-string v14, "CC(remember):MeseroScreen.kt#9igjgp"

    invoke-static {v9, v13, v14}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    and-int/lit8 v13, v7, 0x70

    xor-int/lit8 v13, v13, 0x30

    if-le v13, v6, :cond_8

    invoke-interface {v9, v5}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v13

    if-nez v13, :cond_9

    :cond_8
    and-int/lit8 v13, v7, 0x30

    if-ne v13, v6, :cond_a

    :cond_9
    const/4 v12, 0x1

    .local v12, "invalid\\2":Z
    :cond_a
    move-object v6, v9

    .local v6, "$this$cache\\2":Landroidx/compose/runtime/Composer;
    const/4 v13, 0x0

    .line 575
    .local v13, "$i$f$cache\\2\\574":I
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v14

    .local v14, "it\\2":Ljava/lang/Object;
    const/4 v15, 0x0

    .line 576
    .local v15, "$i$a$-let-ComposerKt$cache$1\\3\\575\\2":I
    if-nez v12, :cond_c

    sget-object v16, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual/range {v16 .. v16}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v1

    if-ne v14, v1, :cond_b

    goto :goto_5

    .line 580
    :cond_b
    move-object v1, v14

    goto :goto_6

    .line 577
    :cond_c
    :goto_5
    const/4 v1, 0x0

    .line 574
    .local v1, "$i$a$-cache-MeseroScreenKt$MeseroScreen$4$1$5$6$1$2$1\\4\\577\\1":I
    move/from16 v16, v1

    .end local v1    # "$i$a$-cache-MeseroScreenKt$MeseroScreen$4$1$5$6$1$2$1\\4\\577\\1":I
    .local v16, "$i$a$-cache-MeseroScreenKt$MeseroScreen$4$1$5$6$1$2$1\\4\\577\\1":I
    new-instance v1, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$6$1$2$1$1;

    iget-object v2, v0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$112$lambda$111$$inlined$items$default$5;->$selectedMenuItemForCustomization$delegate$inlined:Landroidx/compose/runtime/MutableState;

    invoke-direct {v1, v5, v2}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$6$1$2$1$1;-><init>(Lcom/example/data/entity/MenuItemEntity;Landroidx/compose/runtime/MutableState;)V

    check-cast v1, Lkotlin/jvm/functions/Function0;

    .line 577
    .end local v16    # "$i$a$-cache-MeseroScreenKt$MeseroScreen$4$1$5$6$1$2$1\\4\\577\\1":I
    nop

    .line 578
    .local v1, "value\\3":Ljava/lang/Object;
    invoke-interface {v6, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 579
    nop

    .line 576
    .end local v1    # "value\\3":Ljava/lang/Object;
    :goto_6
    nop

    .line 575
    .end local v14    # "it\\2":Ljava/lang/Object;
    .end local v15    # "$i$a$-let-ComposerKt$cache$1\\3\\575\\2":I
    nop

    .line 574
    .end local v6    # "$this$cache\\2":Landroidx/compose/runtime/Composer;
    .end local v12    # "invalid\\2":Z
    .end local v13    # "$i$f$cache\\2\\574":I
    check-cast v1, Lkotlin/jvm/functions/Function0;

    invoke-static {v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    shr-int/lit8 v2, v7, 0x3

    and-int/lit8 v2, v2, 0xe

    .line 571
    invoke-static {v5, v11, v1, v9, v2}, Lcom/example/ui/screens/MeseroScreenKt;->MenuItemCard(Lcom/example/data/entity/MenuItemEntity;ILkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V

    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 581
    .end local v11    # "inCartQty\\1":I
    nop

    .line 465
    .end local v5    # "item\\1":Lcom/example/data/entity/MenuItemEntity;
    .end local v7    # "$changed\\1":I
    .end local v8    # "$this$MeseroScreen_u24lambda_u24125_u24lambda_u24124_u24lambda_u24113_u24lambda_u24112_u24lambda_u24111_u24lambda_u24110\\1":Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;
    .end local v9    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v10    # "$i$a$-items$default-MeseroScreenKt$MeseroScreen$4$1$5$6$1$2\\1\\465\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_d

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 466
    :cond_d
    :goto_7
    return-void
.end method
