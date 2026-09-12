.class public final Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyGridDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/MeseroScreenKt;->TableMapTabContent(Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nLazyGridDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyGridDsl.kt\nandroidx/compose/foundation/lazy/grid/LazyGridDslKt$items$5\n+ 2 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,569:1\n1638#2,9:570\n1650#2:585\n1225#3,6:579\n*S KotlinDebug\n*F\n+ 1 MeseroScreen.kt\ncom/example/ui/screens/MeseroScreenKt\n*L\n1646#1:579,6\n*E\n"
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
.field final synthetic $allOrders$inlined:Ljava/util/List;

.field final synthetic $items:Ljava/util/List;

.field final synthetic $selectedTable$inlined:Ljava/lang/String;

.field final synthetic $selectedTableForDetail$delegate$inlined:Landroidx/compose/runtime/MutableState;


# direct methods
.method public constructor <init>(Ljava/util/List;Ljava/util/List;Ljava/lang/String;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$items:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$allOrders$inlined:Ljava/util/List;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$selectedTable$inlined:Ljava/lang/String;

    iput-object p4, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$selectedTableForDetail$delegate$inlined:Landroidx/compose/runtime/MutableState;

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->invoke(Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 19
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

    .line 465
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

    const-string v8, "androidx.compose.foundation.lazy.grid.items.<anonymous> (LazyGridDsl.kt:464)"

    const v9, 0x29b3c0fe

    invoke-static {v9, v3, v6, v8}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    :cond_6
    iget-object v6, v0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$items:Ljava/util/List;

    invoke-interface {v6, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v6

    and-int/lit8 v8, v3, 0xe

    .local v8, "$changed\\1":I
    move-object v9, v6

    check-cast v9, Lcom/example/ui/screens/RestaurantTableInfo;

    .local v9, "table\\1":Lcom/example/ui/screens/RestaurantTableInfo;
    move-object/from16 v6, p1

    .local v6, "$this$TableMapTabContent_u24lambda_u24283_u24lambda_u24282_u24lambda_u24281_u24lambda_u24280\\1":Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;
    move-object/from16 v14, p3

    .local v14, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/16 v16, 0x0

    .line 570
    .local v16, "$i$a$-items$default-MeseroScreenKt$TableMapTabContent$1$3$1$1\\1\\465\\0":I
    const v10, -0x67c53eae

    invoke-interface {v14, v10}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v10, "C*1645@77037L78,1640@76838L295:MeseroScreen.kt#2thlc2"

    invoke-static {v14, v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    invoke-virtual {v9}, Lcom/example/ui/screens/RestaurantTableInfo;->getName()Ljava/lang/String;

    move-result-object v10

    iget-object v11, v0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$allOrders$inlined:Ljava/util/List;

    const/4 v12, 0x0

    invoke-static {v10, v11, v12, v5, v12}, Lcom/example/ui/screens/MeseroScreenKt;->resolveTableStatus$default(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;ILjava/lang/Object;)Lkotlin/Pair;

    move-result-object v5

    invoke-virtual {v5}, Lkotlin/Pair;->component1()Ljava/lang/Object;

    move-result-object v10

    check-cast v10, Lcom/example/ui/screens/TableStatusType;

    .local v10, "status\\1":Lcom/example/ui/screens/TableStatusType;
    invoke-virtual {v5}, Lkotlin/Pair;->component2()Ljava/lang/Object;

    move-result-object v5

    move-object v11, v5

    check-cast v11, Lcom/example/data/entity/OrderEntity;

    .line 571
    .local v11, "order\\1":Lcom/example/data/entity/OrderEntity;
    iget-object v5, v0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$selectedTable$inlined:Ljava/lang/String;

    invoke-virtual {v9}, Lcom/example/ui/screens/RestaurantTableInfo;->getName()Ljava/lang/String;

    move-result-object v12

    const/4 v13, 0x1

    invoke-static {v5, v12, v13}, Lkotlin/text/StringsKt;->equals(Ljava/lang/String;Ljava/lang/String;Z)Z

    move-result v12

    .line 574
    .local v12, "isSelected\\1":Z
    nop

    .line 575
    nop

    .line 576
    nop

    .line 577
    nop

    .line 578
    const v5, -0x5e2f7b0e

    const-string v15, "CC(remember):MeseroScreen.kt#9igjgp"

    invoke-static {v14, v5, v15}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    and-int/lit8 v5, v8, 0x70

    xor-int/lit8 v5, v5, 0x30

    if-le v5, v7, :cond_7

    invoke-interface {v14, v9}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v5

    if-nez v5, :cond_8

    :cond_7
    and-int/lit8 v5, v8, 0x30

    if-ne v5, v7, :cond_9

    :cond_8
    goto :goto_4

    :cond_9
    const/4 v13, 0x0

    .local v13, "invalid\\2":Z
    :goto_4
    move-object v5, v14

    .local v5, "$this$cache\\2":Landroidx/compose/runtime/Composer;
    const/4 v7, 0x0

    .line 579
    .local v7, "$i$f$cache\\2\\578":I
    invoke-interface {v5}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v15

    .local v15, "it\\2":Ljava/lang/Object;
    const/16 v17, 0x0

    .line 580
    .local v17, "$i$a$-let-ComposerKt$cache$1\\3\\579\\2":I
    if-nez v13, :cond_b

    sget-object v18, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual/range {v18 .. v18}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v1

    if-ne v15, v1, :cond_a

    goto :goto_5

    .line 584
    :cond_a
    move-object v1, v15

    goto :goto_6

    .line 581
    :cond_b
    :goto_5
    const/4 v1, 0x0

    .line 578
    .local v1, "$i$a$-cache-MeseroScreenKt$TableMapTabContent$1$3$1$1$1\\4\\581\\1":I
    move/from16 v18, v1

    .end local v1    # "$i$a$-cache-MeseroScreenKt$TableMapTabContent$1$3$1$1$1\\4\\581\\1":I
    .local v18, "$i$a$-cache-MeseroScreenKt$TableMapTabContent$1$3$1$1$1\\4\\581\\1":I
    new-instance v1, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;

    iget-object v2, v0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$lambda$283$lambda$282$lambda$281$$inlined$items$default$5;->$selectedTableForDetail$delegate$inlined:Landroidx/compose/runtime/MutableState;

    invoke-direct {v1, v9, v2}, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;-><init>(Lcom/example/ui/screens/RestaurantTableInfo;Landroidx/compose/runtime/MutableState;)V

    check-cast v1, Lkotlin/jvm/functions/Function0;

    .line 581
    .end local v18    # "$i$a$-cache-MeseroScreenKt$TableMapTabContent$1$3$1$1$1\\4\\581\\1":I
    nop

    .line 582
    .local v1, "value\\3":Ljava/lang/Object;
    invoke-interface {v5, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 583
    nop

    .line 580
    .end local v1    # "value\\3":Ljava/lang/Object;
    :goto_6
    nop

    .line 579
    .end local v15    # "it\\2":Ljava/lang/Object;
    .end local v17    # "$i$a$-let-ComposerKt$cache$1\\3\\579\\2":I
    nop

    .line 578
    .end local v5    # "$this$cache\\2":Landroidx/compose/runtime/Composer;
    .end local v7    # "$i$f$cache\\2\\578":I
    .end local v13    # "invalid\\2":Z
    move-object v13, v1

    check-cast v13, Lkotlin/jvm/functions/Function0;

    invoke-static {v14}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    shr-int/lit8 v1, v8, 0x3

    and-int/lit8 v15, v1, 0xe

    .line 573
    invoke-static/range {v9 .. v15}, Lcom/example/ui/screens/MeseroScreenKt;->TableVisualCard(Lcom/example/ui/screens/RestaurantTableInfo;Lcom/example/ui/screens/TableStatusType;Lcom/example/data/entity/OrderEntity;ZLkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V

    invoke-interface {v14}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 585
    .end local v10    # "status\\1":Lcom/example/ui/screens/TableStatusType;
    .end local v11    # "order\\1":Lcom/example/data/entity/OrderEntity;
    .end local v12    # "isSelected\\1":Z
    nop

    .line 465
    .end local v6    # "$this$TableMapTabContent_u24lambda_u24283_u24lambda_u24282_u24lambda_u24281_u24lambda_u24280\\1":Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;
    .end local v8    # "$changed\\1":I
    .end local v9    # "table\\1":Lcom/example/ui/screens/RestaurantTableInfo;
    .end local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v16    # "$i$a$-items$default-MeseroScreenKt$TableMapTabContent$1$3$1$1\\1\\465\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_c

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 466
    :cond_c
    :goto_7
    return-void
.end method
