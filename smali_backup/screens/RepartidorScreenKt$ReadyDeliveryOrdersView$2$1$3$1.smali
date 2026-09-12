.class final Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;
.super Ljava/lang/Object;
.source "RepartidorScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/RepartidorScreenKt;->ReadyDeliveryOrdersView(Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x18
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Lkotlin/jvm/functions/Function2<",
        "Landroidx/compose/runtime/Composer;",
        "Ljava/lang/Integer;",
        "Lkotlin/Unit;",
        ">;"
    }
.end annotation

.annotation system Ldalvik/annotation/SourceDebugExtension;
    value = "SMAP\nRepartidorScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1\n+ 2 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 3 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,1342:1\n148#2:1343\n1225#3,6:1344\n*S KotlinDebug\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1\n*L\n348#1:1343\n346#1:1344,6\n*E\n"
.end annotation

.annotation runtime Lkotlin/Metadata;
    k = 0x3
    mv = {
        0x2,
        0x2,
        0x0
    }
    xi = 0x30
.end annotation


# instance fields
.field final synthetic $onStartDelivery:Lkotlin/jvm/functions/Function1;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Lkotlin/jvm/functions/Function1<",
            "Ljava/lang/Long;",
            "Lkotlin/Unit;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $order:Lcom/example/data/entity/WebOrderEntity;


# direct methods
.method constructor <init>(Lcom/example/data/entity/WebOrderEntity;Lkotlin/jvm/functions/Function1;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/example/data/entity/WebOrderEntity;",
            "Lkotlin/jvm/functions/Function1<",
            "-",
            "Ljava/lang/Long;",
            "Lkotlin/Unit;",
            ">;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$order:Lcom/example/data/entity/WebOrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$onStartDelivery:Lkotlin/jvm/functions/Function1;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 2
    .param p1, "p1"    # Ljava/lang/Object;
    .param p2, "p2"    # Ljava/lang/Object;

    .line 344
    move-object v0, p1

    check-cast v0, Landroidx/compose/runtime/Composer;

    move-object v1, p2

    check-cast v1, Ljava/lang/Number;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->invoke(Landroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/runtime/Composer;I)V
    .locals 14
    .param p1, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p2, "$changed"    # I

    move-object v9, p1

    move/from16 v13, p2

    const-string v0, "C346@15314L11,346@15270L64,345@15187L29,344@15141L763:RepartidorScreen.kt#2thlc2"

    invoke-static {p1, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    and-int/lit8 v0, v13, 0x3

    const/4 v1, 0x2

    if-ne v0, v1, :cond_1

    invoke-interface {p1}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v0

    if-nez v0, :cond_0

    goto :goto_0

    .line 344
    :cond_0
    invoke-interface {p1}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    goto/16 :goto_3

    .line 0
    :cond_1
    :goto_0
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_2

    const/4 v0, -0x1

    const-string v1, "com.example.ui.screens.ReadyDeliveryOrdersView.<anonymous>.<anonymous>.<anonymous>.<anonymous> (RepartidorScreen.kt:344)"

    const v2, -0x3f8778ca

    invoke-static {v2, v13, v0, v1}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    .line 347
    :cond_2
    sget-object v0, Landroidx/compose/material3/ButtonDefaults;->INSTANCE:Landroidx/compose/material3/ButtonDefaults;

    sget-object v1, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v2, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v1, p1, v2}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v1

    invoke-virtual {v1}, Landroidx/compose/material3/ColorScheme;->getPrimary-0d7_KjU()J

    move-result-wide v1

    sget v3, Landroidx/compose/material3/ButtonDefaults;->$stable:I

    shl-int/lit8 v10, v3, 0xc

    const/16 v11, 0xe

    const-wide/16 v3, 0x0

    const-wide/16 v5, 0x0

    const-wide/16 v7, 0x0

    invoke-virtual/range {v0 .. v11}, Landroidx/compose/material3/ButtonDefaults;->buttonColors-ro_MJ88(JJJJLandroidx/compose/runtime/Composer;II)Landroidx/compose/material3/ButtonColors;

    move-result-object v4

    .line 348
    const/16 v0, 0x8

    .local v0, "$this$dp\\1":I
    const/4 v1, 0x0

    .line 1343
    .local v1, "$i$f$getDp\\1\\348":I
    int-to-float v2, v0

    invoke-static {v2}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v0

    .line 348
    .end local v0    # "$this$dp\\1":I
    .end local v1    # "$i$f$getDp\\1\\348":I
    invoke-static {v0}, Landroidx/compose/foundation/shape/RoundedCornerShapeKt;->RoundedCornerShape-0680j_4(F)Landroidx/compose/foundation/shape/RoundedCornerShape;

    move-result-object v0

    .line 349
    sget-object v1, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v1, Landroidx/compose/ui/Modifier;

    .line 350
    const/4 v2, 0x1

    const/4 v3, 0x0

    const/4 v5, 0x0

    invoke-static {v1, v5, v2, v3}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v1

    .line 351
    iget-object v2, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$order:Lcom/example/data/entity/WebOrderEntity;

    invoke-virtual {v2}, Lcom/example/data/entity/WebOrderEntity;->getId()J

    move-result-wide v2

    new-instance v5, Ljava/lang/StringBuilder;

    invoke-direct {v5}, Ljava/lang/StringBuilder;-><init>()V

    const-string v6, "btn_iniciar_entrega_"

    invoke-virtual {v5, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v2, v3}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v2

    invoke-virtual {v2}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v2

    invoke-static {v1, v2}, Landroidx/compose/ui/platform/TestTagKt;->testTag(Landroidx/compose/ui/Modifier;Ljava/lang/String;)Landroidx/compose/ui/Modifier;

    move-result-object v1

    .line 346
    const v2, -0x7b8c652d

    const-string v3, "CC(remember):RepartidorScreen.kt#9igjgp"

    invoke-static {p1, v2, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    iget-object v2, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$onStartDelivery:Lkotlin/jvm/functions/Function1;

    invoke-interface {p1, v2}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v2

    iget-object v3, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$order:Lcom/example/data/entity/WebOrderEntity;

    invoke-interface {p1, v3}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v3

    or-int/2addr v2, v3

    .local v2, "invalid\\2":Z
    iget-object v3, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$onStartDelivery:Lkotlin/jvm/functions/Function1;

    iget-object v5, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1;->$order:Lcom/example/data/entity/WebOrderEntity;

    move-object v6, p1

    .local v6, "$this$cache\\2":Landroidx/compose/runtime/Composer;
    const/4 v7, 0x0

    .line 1344
    .local v7, "$i$f$cache\\2\\346":I
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v8

    .local v8, "it\\2":Ljava/lang/Object;
    const/4 v10, 0x0

    .line 1345
    .local v10, "$i$a$-let-ComposerKt$cache$1\\3\\1344\\2":I
    if-nez v2, :cond_4

    sget-object v11, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual {v11}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v11

    if-ne v8, v11, :cond_3

    goto :goto_1

    .line 1349
    :cond_3
    move-object v12, v8

    goto :goto_2

    .line 1346
    :cond_4
    :goto_1
    const/4 v11, 0x0

    .line 346
    .local v11, "$i$a$-cache-RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1$1\\4\\1346\\0":I
    new-instance v12, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1$1$1;

    invoke-direct {v12, v3, v5}, Lcom/example/ui/screens/RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1$1$1;-><init>(Lkotlin/jvm/functions/Function1;Lcom/example/data/entity/WebOrderEntity;)V

    check-cast v12, Lkotlin/jvm/functions/Function0;

    .line 1346
    .end local v11    # "$i$a$-cache-RepartidorScreenKt$ReadyDeliveryOrdersView$2$1$3$1$1\\4\\1346\\0":I
    nop

    .line 1347
    .local v12, "value\\3":Ljava/lang/Object;
    invoke-interface {v6, v12}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1348
    nop

    .line 1345
    .end local v12    # "value\\3":Ljava/lang/Object;
    :goto_2
    nop

    .line 1344
    .end local v8    # "it\\2":Ljava/lang/Object;
    .end local v10    # "$i$a$-let-ComposerKt$cache$1\\3\\1344\\2":I
    nop

    .line 346
    .end local v2    # "invalid\\2":Z
    .end local v6    # "$this$cache\\2":Landroidx/compose/runtime/Composer;
    .end local v7    # "$i$f$cache\\2\\346":I
    check-cast v12, Lkotlin/jvm/functions/Function0;

    invoke-static {p1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 351
    nop

    .line 348
    move-object v3, v0

    check-cast v3, Landroidx/compose/ui/graphics/Shape;

    .line 347
    sget-object v0, Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;->INSTANCE:Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;

    invoke-virtual {v0}, Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;->getLambda$1588074822$app()Lkotlin/jvm/functions/Function3;

    move-result-object v0

    .line 345
    const/4 v2, 0x0

    const/4 v5, 0x0

    const/4 v6, 0x0

    const/4 v7, 0x0

    const/4 v8, 0x0

    const/high16 v11, 0x30000000

    move-object v9, v0

    move-object v0, v12

    const/16 v12, 0x1e4

    move-object v10, p1

    invoke-static/range {v0 .. v12}, Landroidx/compose/material3/ButtonKt;->Button(Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;ZLandroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/ButtonColors;Landroidx/compose/material3/ButtonElevation;Landroidx/compose/foundation/BorderStroke;Landroidx/compose/foundation/layout/PaddingValues;Landroidx/compose/foundation/interaction/MutableInteractionSource;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_5

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 357
    :cond_5
    :goto_3
    return-void
.end method
