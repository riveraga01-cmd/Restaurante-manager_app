.class public final Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;
.super Lkotlin/jvm/internal/Lambda;
.source "LazyDsl.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/RepartidorScreenKt;->ReportDeliveryProblemDialog(Lcom/example/data/entity/WebOrderEntity;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nLazyDsl.kt\nKotlin\n*S Kotlin\n*F\n+ 1 LazyDsl.kt\nandroidx/compose/foundation/lazy/LazyDslKt$items$4\n+ 2 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,433:1\n1065#2,3:434\n1071#2,3:443\n1225#3,6:437\n*S KotlinDebug\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt\n*L\n1067#1:437,6\n*E\n"
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
.field final synthetic $incidentText$delegate$inlined:Landroidx/compose/runtime/MutableState;

.field final synthetic $items:Ljava/util/List;

.field final synthetic $selectedPreset$delegate$inlined:Landroidx/compose/runtime/MutableState;


# direct methods
.method public constructor <init>(Ljava/util/List;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$items:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$selectedPreset$delegate$inlined:Landroidx/compose/runtime/MutableState;

    iput-object p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$incidentText$delegate$inlined:Landroidx/compose/runtime/MutableState;

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

    invoke-virtual {p0, v0, v1, v2, v3}, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->invoke(Landroidx/compose/foundation/lazy/LazyItemScope;ILandroidx/compose/runtime/Composer;I)V

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

    move/from16 v26, v3

    goto/16 :goto_7

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
    iget-object v5, v0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$items:Ljava/util/List;

    invoke-interface {v5, v1}, Ljava/util/List;->get(I)Ljava/lang/Object;

    move-result-object v5

    and-int/lit8 v7, v3, 0xe

    .local v7, "$changed\\1":I
    check-cast v5, Ljava/lang/String;

    .local v5, "preset\\1":Ljava/lang/String;
    move-object/from16 v8, p1

    .local v8, "$this$ReportDeliveryProblemDialog_u24lambda_u24170_u24lambda_u24169_u24lambda_u24166_u24lambda_u24165_u24lambda_u24164\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    move-object/from16 v9, p3

    .local v9, "$composer\\1":Landroidx/compose/runtime/Composer;
    const/16 v25, 0x0

    .line 434
    .local v25, "$i$a$-items$default-RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1\\1\\153\\0":I
    const v10, -0x6f8f6ff8

    invoke-interface {v9, v10}, Landroidx/compose/runtime/Composer;->startReplaceGroup(I)V

    const-string v10, "C*1066@48580L141,1070@48759L34,1064@48465L354:RepartidorScreen.kt#2thlc2"

    invoke-static {v9, v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    .line 435
    iget-object v10, v0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$selectedPreset$delegate$inlined:Landroidx/compose/runtime/MutableState;

    invoke-static {v10}, Lcom/example/ui/screens/RepartidorScreenKt;->access$ReportDeliveryProblemDialog$lambda$157(Landroidx/compose/runtime/MutableState;)Ljava/lang/String;

    move-result-object v10

    invoke-static {v10, v5}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v10

    .line 436
    const v11, 0xceae8e6

    const-string v12, "CC(remember):RepartidorScreen.kt#9igjgp"

    invoke-static {v9, v11, v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    and-int/lit8 v11, v7, 0x70

    xor-int/lit8 v11, v11, 0x30

    if-le v11, v6, :cond_7

    invoke-interface {v9, v5}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v11

    if-nez v11, :cond_8

    :cond_7
    and-int/lit8 v11, v7, 0x30

    if-ne v11, v6, :cond_9

    :cond_8
    const/4 v6, 0x1

    goto :goto_4

    :cond_9
    const/4 v6, 0x0

    .local v6, "invalid\\2":Z
    :goto_4
    move-object v11, v9

    .local v11, "$this$cache\\2":Landroidx/compose/runtime/Composer;
    const/4 v13, 0x0

    .line 437
    .local v13, "$i$f$cache\\2\\436":I
    invoke-interface {v11}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v14

    .local v14, "it\\2":Ljava/lang/Object;
    const/4 v15, 0x0

    .line 438
    .local v15, "$i$a$-let-ComposerKt$cache$1\\3\\437\\2":I
    if-nez v6, :cond_b

    sget-object v16, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual/range {v16 .. v16}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v12

    if-ne v14, v12, :cond_a

    goto :goto_5

    .line 442
    :cond_a
    move/from16 v26, v3

    move-object v1, v14

    goto :goto_6

    .line 439
    :cond_b
    :goto_5
    const/4 v12, 0x0

    .line 436
    .local v12, "$i$a$-cache-RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1\\4\\439\\1":I
    new-instance v1, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;

    iget-object v2, v0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$selectedPreset$delegate$inlined:Landroidx/compose/runtime/MutableState;

    move/from16 v26, v3

    .end local v3    # "$dirty":I
    .local v26, "$dirty":I
    iget-object v3, v0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$lambda$170$lambda$169$lambda$166$lambda$165$$inlined$items$default$4;->$incidentText$delegate$inlined:Landroidx/compose/runtime/MutableState;

    invoke-direct {v1, v5, v2, v3}, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;-><init>(Ljava/lang/String;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V

    check-cast v1, Lkotlin/jvm/functions/Function0;

    .line 439
    .end local v12    # "$i$a$-cache-RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1\\4\\439\\1":I
    nop

    .line 440
    .local v1, "value\\3":Ljava/lang/Object;
    invoke-interface {v11, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 441
    nop

    .line 438
    .end local v1    # "value\\3":Ljava/lang/Object;
    :goto_6
    nop

    .line 437
    .end local v14    # "it\\2":Ljava/lang/Object;
    .end local v15    # "$i$a$-let-ComposerKt$cache$1\\3\\437\\2":I
    nop

    .line 436
    .end local v6    # "invalid\\2":Z
    .end local v11    # "$this$cache\\2":Landroidx/compose/runtime/Composer;
    .end local v13    # "$i$f$cache\\2\\436":I
    check-cast v1, Lkotlin/jvm/functions/Function0;

    invoke-static {v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 443
    new-instance v2, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$2;

    invoke-direct {v2, v5}, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$2;-><init>(Ljava/lang/String;)V

    const/16 v3, 0x36

    const v6, 0x5b44eae6

    const/4 v11, 0x1

    invoke-static {v6, v11, v2, v9, v3}, Landroidx/compose/runtime/internal/ComposableLambdaKt;->rememberComposableLambda(IZLjava/lang/Object;Landroidx/compose/runtime/Composer;I)Landroidx/compose/runtime/internal/ComposableLambda;

    move-result-object v2

    move-object v11, v2

    check-cast v11, Lkotlin/jvm/functions/Function2;

    .line 434
    const/4 v12, 0x0

    const/4 v13, 0x0

    const/4 v14, 0x0

    const/4 v15, 0x0

    const/16 v16, 0x0

    const/16 v17, 0x0

    const/16 v18, 0x0

    const/16 v19, 0x0

    const/16 v20, 0x0

    const/16 v22, 0x180

    const/16 v23, 0x0

    const/16 v24, 0xff8

    move-object/from16 v21, v9

    move v9, v10

    move-object v10, v1

    .end local v9    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .local v21, "$composer\\1":Landroidx/compose/runtime/Composer;
    invoke-static/range {v9 .. v24}, Landroidx/compose/material3/ChipKt;->FilterChip(ZLkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function2;Landroidx/compose/ui/Modifier;ZLkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function2;Landroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/SelectableChipColors;Landroidx/compose/material3/SelectableChipElevation;Landroidx/compose/foundation/BorderStroke;Landroidx/compose/foundation/interaction/MutableInteractionSource;Landroidx/compose/runtime/Composer;III)V

    invoke-interface/range {v21 .. v21}, Landroidx/compose/runtime/Composer;->endReplaceGroup()V

    .line 445
    nop

    .line 153
    .end local v5    # "preset\\1":Ljava/lang/String;
    .end local v7    # "$changed\\1":I
    .end local v8    # "$this$ReportDeliveryProblemDialog_u24lambda_u24170_u24lambda_u24169_u24lambda_u24166_u24lambda_u24165_u24lambda_u24164\\1":Landroidx/compose/foundation/lazy/LazyItemScope;
    .end local v21    # "$composer\\1":Landroidx/compose/runtime/Composer;
    .end local v25    # "$i$a$-items$default-RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1\\1\\153\\0":I
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_c

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 154
    :cond_c
    :goto_7
    return-void
.end method
