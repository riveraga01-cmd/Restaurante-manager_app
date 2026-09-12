.class public final Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$lambda$173$lambda$172$lambda$155$lambda$154$$inlined$items$default$4;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nLazyDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt$items$4\n+ 2 GerenteScreen.kt\ncom/example/ui/screens/GerenteScreenKt\n+ 3 Dp.kt\nandroidx/compose/ui/unit/DpKt\n*L\n1#1,433:1\n561#2,2:434\n563#2,3:437\n594#2:440\n148#3:436\n*S KotlinDebug\n*F\n+ 1 GerenteScreen.kt\ncom/example/ui/screens/GerenteScreenKt\n*L\n562#1:436\n*E\n"
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

    iput-object p1, p0, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$lambda$173$lambda$172$lambda$155$lambda$154$$inlined$items$default$4;->$items:Ljava/util/List;

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$lambda$173$lambda$172$lambda$155$lambda$154$$inlined$items$default$4;->invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 22
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

    move-object/from16 v4, p0

    goto/16 :goto_4

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

    iget-object v5, v4, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$lambda$173$lambda$172$lambda$155$lambda$154$$inlined$items$default$4;->$items:Ljava/util/List;

    invoke-interface {v5, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    and-int/lit8 v6, v2, 0xe

    .local v6, "$changed\\1":I
    check-cast v5, Lcom/example/data/entity/SaleEntity;

    .local v5, "sale\\1":Lcom/example/data/entity/SaleEntity;
    move-object/from16 v7, p1

    .local v7, "$this$GerenteScreen_u24lambda_u24173_u24lambda_u24172_u24lambda_u24155_u24lambda_u24154_u24lambda_u24153\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    move-object/from16 v14, p3

    .local v14, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/16 v20, 0x0

    .line 434
    .local v20, "$i$a$-items$default-GerenteScreenKt$GerenteScreen$3$1$10$1$9\\1\\153\\0":I
    const v8, 0x5c98118a

    invoke-interface {v14, v8}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v8, "C*562@30744L11,562@30702L62,564@30871L1760,560@30567L2064:GerenteScreen.kt#2thlc2"

    invoke-static {v14, v8}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    .line 435
    const/16 v8, 0xa

    .local v8, "$this$dp\\2":I
    const/4 v9, 0x0

    .line 436
    .local v9, "$i$f$getDp\\2\\435":I
    int-to-float v10, v8

    invoke-static {v10}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v8

    .line 435
    .end local v8    # "$this$dp\\2":I
    .end local v9    # "$i$f$getDp\\2\\435":I
    invoke-static {v8}, Landroidx/compose/foundation/shape/RoundedCornerShapeKt;->RoundedCornerShape-0680j_4(F)Landroidx/compose/foundation/shape/RoundedCornerShape;

    move-result-object v21

    .line 437
    sget-object v8, Landroidx/compose/material3/CardDefaults;->INSTANCE:Landroidx/compose/material3/CardDefaults;

    sget-object v9, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v10, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v9, v14, v10}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v9

    invoke-virtual {v9}, Landroidx/compose/material3/ColorScheme;->getSurface-0d7_KjU()J

    move-result-wide v9

    sget v11, Landroidx/compose/material3/CardDefaults;->$stable:I

    shl-int/lit8 v18, v11, 0xc

    const/16 v19, 0xe

    const-wide/16 v11, 0x0

    move-object/from16 v17, v14

    .end local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .local v17, "$composer\\1":Landroidx/compose/runtime/Composer;
    const-wide/16 v13, 0x0

    const-wide/16 v15, 0x0

    invoke-virtual/range {v8 .. v19}, Landroidx/compose/material3/CardDefaults;->cardColors-ro_MJ88(JJJJLandroidx/compose/runtime/Composer;II)Landroidx/compose/material3/CardColors;

    move-result-object v10

    .line 438
    move-object/from16 v14, v17

    .end local v17    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .restart local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    sget-object v8, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v8, Landroidx/compose/ui/Modifier;

    const/4 v9, 0x0

    const/4 v11, 0x0

    const/4 v12, 0x1

    invoke-static {v8, v9, v12, v11}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v8

    .line 435
    move-object/from16 v9, v21

    check-cast v9, Landroidx/compose/ui/graphics/Shape;

    .line 437
    nop

    .line 439
    new-instance v11, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1;

    invoke-direct {v11, v5}, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1;-><init>(Lcom/example/data/entity/SaleEntity;)V

    const/16 v13, 0x36

    const v15, 0x559f13fb

    invoke-static {v15, v12, v11, v14, v13}, Landroidx/compose/runtime/internal/ComposableLambdaKt;->rememberComposableLambda(IZLjava/lang/Object;Landroidx/compose/runtime/Composer;I)Landroidx/compose/runtime/internal/ComposableLambda;

    move-result-object v11

    move-object v13, v11

    check-cast v13, Lkotlin/jvm/functions/Function3;

    .line 434
    const/4 v11, 0x0

    const/4 v12, 0x0

    const v15, 0x30006

    const/16 v16, 0x18

    invoke-static/range {v8 .. v16}, Landroidx/compose/material3/CardKt;->Card(Landroidx/compose/ui/Modifier;Landroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/CardColors;Landroidx/compose/material3/CardElevation;Landroidx/compose/foundation/BorderStroke;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V

    invoke-interface {v14}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 440
    nop

    .line 153
    .end local v5    # "sale\\1":Lcom/example/data/entity/SaleEntity;
    .end local v6    # "$changed\\1":I
    .end local v7    # "$this$GerenteScreen_u24lambda_u24173_u24lambda_u24172_u24lambda_u24155_u24lambda_u24154_u24lambda_u24153\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    .end local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v20    # "$i$a$-items$default-GerenteScreenKt$GerenteScreen$3$1$10$1$9\\1\\153\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v5

    if-eqz v5, :cond_7

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 154
    :cond_7
    :goto_4
    return-void
.end method
