.class public final Lcom/example/ui/screens/RepartidorScreenKt$DeliverySettlementHistoryView$lambda$123$lambda$122$$inlined$items$default$4;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/RepartidorScreenKt;->DeliverySettlementHistoryView(Ljava/util/List;Ljava/util/List;Ljava/lang/String;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nLazyDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt$items$4\n+ 2 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt\n+ 3 Dp.kt\nandroidx/compose/ui/unit/DpKt\n*L\n1#1,433:1\n676#2,4:434\n680#2:439\n681#2,2:441\n737#2:443\n148#3:438\n148#3:440\n*S KotlinDebug\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt\n*L\n679#1:438\n680#1:440\n*E\n"
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

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$DeliverySettlementHistoryView$lambda$123$lambda$122$$inlined$items$default$4;->$items:Ljava/util/List;

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/RepartidorScreenKt$DeliverySettlementHistoryView$lambda$123$lambda$122$$inlined$items$default$4;->invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V
    .locals 30
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

    iget-object v5, v4, Lcom/example/ui/screens/RepartidorScreenKt$DeliverySettlementHistoryView$lambda$123$lambda$122$$inlined$items$default$4;->$items:Ljava/util/List;

    invoke-interface {v5, v0}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    and-int/lit8 v6, v2, 0xe

    .local v6, "$changed\\1":I
    check-cast v5, Lcom/example/data/entity/DeliverySettlementEntity;

    .local v5, "item\\1":Lcom/example/data/entity/DeliverySettlementEntity;
    move-object/from16 v7, p1

    .local v7, "$this$DeliverySettlementHistoryView_u24lambda_u24123_u24lambda_u24122_u24lambda_u24121\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    move-object/from16 v14, p3

    .local v14, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/16 v20, 0x0

    .line 434
    .local v20, "$i$a$-items$default-RepartidorScreenKt$DeliverySettlementHistoryView$1$1$3\\1\\153\\0":I
    const v8, 0x4bd81129    # 2.8320338E7f

    invoke-interface {v14, v8}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v8, "C*677@30814L11,677@30772L62,679@30953L11,681@31074L2917,676@30724L3267:RepartidorScreen.kt#2thlc2"

    invoke-static {v14, v8}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    new-instance v8, Ljava/text/SimpleDateFormat;

    const-string v9, "hh:mm a"

    invoke-static {}, Ljava/util/Locale;->getDefault()Ljava/util/Locale;

    move-result-object v10

    invoke-direct {v8, v9, v10}, Ljava/text/SimpleDateFormat;-><init>(Ljava/lang/String;Ljava/util/Locale;)V

    new-instance v9, Ljava/util/Date;

    invoke-virtual {v5}, Lcom/example/data/entity/DeliverySettlementEntity;->getCompletedAt()J

    move-result-wide v10

    invoke-direct {v9, v10, v11}, Ljava/util/Date;-><init>(J)V

    invoke-virtual {v8, v9}, Ljava/text/SimpleDateFormat;->format(Ljava/util/Date;)Ljava/lang/String;

    move-result-object v8

    .line 436
    .local v8, "timeFormatted\\1":Ljava/lang/String;
    move-object v9, v8

    .end local v8    # "timeFormatted\\1":Ljava/lang/String;
    .local v9, "timeFormatted\\1":Ljava/lang/String;
    sget-object v8, Landroidx/compose/material3/CardDefaults;->INSTANCE:Landroidx/compose/material3/CardDefaults;

    sget-object v10, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v11, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v10, v14, v11}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v10

    invoke-virtual {v10}, Landroidx/compose/material3/ColorScheme;->getSurface-0d7_KjU()J

    move-result-wide v10

    sget v12, Landroidx/compose/material3/CardDefaults;->$stable:I

    shl-int/lit8 v18, v12, 0xc

    const/16 v19, 0xe

    move-object v13, v9

    move-wide v9, v10

    .end local v9    # "timeFormatted\\1":Ljava/lang/String;
    .local v13, "timeFormatted\\1":Ljava/lang/String;
    const-wide/16 v11, 0x0

    move-object v15, v13

    move-object/from16 v17, v14

    .end local v13    # "timeFormatted\\1":Ljava/lang/String;
    .end local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .local v15, "timeFormatted\\1":Ljava/lang/String;
    .local v17, "$composer\\1":Landroidx/compose/runtime/Composer;
    const-wide/16 v13, 0x0

    move-object/from16 v21, v15

    .end local v15    # "timeFormatted\\1":Ljava/lang/String;
    .local v21, "timeFormatted\\1":Ljava/lang/String;
    const-wide/16 v15, 0x0

    move-object/from16 v0, v21

    .end local v21    # "timeFormatted\\1":Ljava/lang/String;
    .local v0, "timeFormatted\\1":Ljava/lang/String;
    invoke-virtual/range {v8 .. v19}, Landroidx/compose/material3/CardDefaults;->cardColors-ro_MJ88(JJJJLandroidx/compose/runtime/Composer;II)Landroidx/compose/material3/CardColors;

    move-result-object v10

    .line 437
    move-object/from16 v14, v17

    .end local v17    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .restart local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    const/16 v8, 0xa

    .local v8, "$this$dp\\2":I
    const/4 v9, 0x0

    .line 438
    .local v9, "$i$f$getDp\\2\\437":I
    int-to-float v11, v8

    invoke-static {v11}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v8

    .line 437
    .end local v8    # "$this$dp\\2":I
    .end local v9    # "$i$f$getDp\\2\\437":I
    invoke-static {v8}, Landroidx/compose/foundation/shape/RoundedCornerShapeKt;->RoundedCornerShape-0680j_4(F)Landroidx/compose/foundation/shape/RoundedCornerShape;

    move-result-object v8

    .line 439
    const/4 v9, 0x1

    .local v9, "$this$dp\\3":I
    const/4 v11, 0x0

    .line 440
    .local v11, "$i$f$getDp\\3\\439":I
    int-to-float v12, v9

    invoke-static {v12}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v9

    .line 439
    .end local v9    # "$this$dp\\3":I
    .end local v11    # "$i$f$getDp\\3\\439":I
    sget-object v11, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v12, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v11, v14, v12}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v11

    invoke-virtual {v11}, Landroidx/compose/material3/ColorScheme;->getOutlineVariant-0d7_KjU()J

    move-result-wide v21

    const/16 v27, 0xe

    const/16 v28, 0x0

    const/high16 v23, 0x3f000000    # 0.5f

    const/16 v24, 0x0

    const/16 v25, 0x0

    const/16 v26, 0x0

    invoke-static/range {v21 .. v28}, Landroidx/compose/ui/graphics/Color;->copy-wmQWz5c$default(JFFFFILjava/lang/Object;)J

    move-result-wide v11

    invoke-static {v9, v11, v12}, Landroidx/compose/foundation/BorderStrokeKt;->BorderStroke-cXLIe8U(FJ)Landroidx/compose/foundation/BorderStroke;

    move-result-object v12

    .line 441
    sget-object v9, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v9, Landroidx/compose/ui/Modifier;

    const/4 v11, 0x0

    const/4 v13, 0x0

    const/4 v15, 0x1

    invoke-static {v9, v11, v15, v13}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v9

    .line 437
    check-cast v8, Landroidx/compose/ui/graphics/Shape;

    .line 436
    nop

    .line 439
    nop

    .line 442
    new-instance v11, Lcom/example/ui/screens/RepartidorScreenKt$DeliverySettlementHistoryView$1$1$3$1;

    invoke-direct {v11, v5, v0}, Lcom/example/ui/screens/RepartidorScreenKt$DeliverySettlementHistoryView$1$1$3$1;-><init>(Lcom/example/data/entity/DeliverySettlementEntity;Ljava/lang/String;)V

    const/16 v13, 0x36

    move-object/from16 v21, v0

    .end local v0    # "timeFormatted\\1":Ljava/lang/String;
    .restart local v21    # "timeFormatted\\1":Ljava/lang/String;
    const v0, -0x4cf34ecf

    invoke-static {v0, v15, v11, v14, v13}, Landroidx/compose/runtime/internal/ComposableLambdaKt;->rememberComposableLambda(IZLjava/lang/Object;Landroidx/compose/runtime/Composer;I)Landroidx/compose/runtime/internal/ComposableLambda;

    move-result-object v0

    move-object v13, v0

    check-cast v13, Lkotlin/jvm/functions/Function3;

    .line 435
    const/4 v11, 0x0

    const v15, 0x30006

    const/16 v16, 0x8

    move-object/from16 v29, v9

    move-object v9, v8

    move-object/from16 v8, v29

    invoke-static/range {v8 .. v16}, Landroidx/compose/material3/CardKt;->Card(Landroidx/compose/ui/Modifier;Landroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/CardColors;Landroidx/compose/material3/CardElevation;Landroidx/compose/foundation/BorderStroke;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V

    invoke-interface {v14}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 443
    .end local v21    # "timeFormatted\\1":Ljava/lang/String;
    nop

    .line 153
    .end local v5    # "item\\1":Lcom/example/data/entity/DeliverySettlementEntity;
    .end local v6    # "$changed\\1":I
    .end local v7    # "$this$DeliverySettlementHistoryView_u24lambda_u24123_u24lambda_u24122_u24lambda_u24121\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    .end local v14    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v20    # "$i$a$-items$default-RepartidorScreenKt$DeliverySettlementHistoryView$1$1$3\\1\\153\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_7

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 154
    :cond_7
    :goto_4
    return-void
.end method
