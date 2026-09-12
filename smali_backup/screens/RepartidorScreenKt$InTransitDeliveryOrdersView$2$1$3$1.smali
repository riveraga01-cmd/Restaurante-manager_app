.class final Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;
.super Ljava/lang/Object;
.source "RepartidorScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/RepartidorScreenKt;->InTransitDeliveryOrdersView(Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nRepartidorScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1\n+ 2 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 3 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n+ 4 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 5 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 6 Composer.kt\nandroidx/compose/runtime/Updater\n+ 7 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 8 Composer.kt\nandroidx/compose/runtime/ComposerKt\n*L\n1#1,1342:1\n148#2:1343\n148#2:1379\n148#2:1415\n148#2:1416\n148#2:1423\n85#3:1344\n83#3,5:1345\n88#3:1378\n92#3:1437\n78#4,6:1350\n85#4,4:1365\n89#4,2:1375\n78#4,6:1386\n85#4,4:1401\n89#4,2:1411\n93#4:1432\n93#4:1436\n368#5,9:1356\n377#5:1377\n368#5,9:1392\n377#5:1413\n378#5,2:1430\n378#5,2:1434\n4032#6,6:1369\n4032#6,6:1405\n98#7:1380\n96#7,5:1381\n101#7:1414\n105#7:1433\n1225#8,6:1417\n1225#8,6:1424\n*S KotlinDebug\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1\n*L\n422#1:1343\n427#1:1379\n433#1:1415\n434#1:1416\n448#1:1423\n421#1:1344\n421#1:1345,5\n421#1:1378\n421#1:1437\n421#1:1350,6\n421#1:1365,4\n421#1:1375,2\n425#1:1386,6\n425#1:1401,4\n425#1:1411,2\n425#1:1432\n421#1:1436\n421#1:1356,9\n421#1:1377\n425#1:1392,9\n425#1:1413\n425#1:1430,2\n421#1:1434,2\n421#1:1369,6\n425#1:1405,6\n425#1:1380\n425#1:1381,5\n425#1:1414\n425#1:1433\n431#1:1417,6\n446#1:1424,6\n*E\n"
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
.field final synthetic $onCompleteDelivery:Lkotlin/jvm/functions/Function1;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Lkotlin/jvm/functions/Function1<",
            "Ljava/lang/Long;",
            "Lkotlin/Unit;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $onReportProblem:Lkotlin/jvm/functions/Function1;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Lkotlin/jvm/functions/Function1<",
            "Lcom/example/data/entity/WebOrderEntity;",
            "Lkotlin/Unit;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $order:Lcom/example/data/entity/WebOrderEntity;


# direct methods
.method constructor <init>(Lcom/example/data/entity/WebOrderEntity;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/example/data/entity/WebOrderEntity;",
            "Lkotlin/jvm/functions/Function1<",
            "-",
            "Lcom/example/data/entity/WebOrderEntity;",
            "Lkotlin/Unit;",
            ">;",
            "Lkotlin/jvm/functions/Function1<",
            "-",
            "Ljava/lang/Long;",
            "Lkotlin/Unit;",
            ">;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->$order:Lcom/example/data/entity/WebOrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->$onReportProblem:Lkotlin/jvm/functions/Function1;

    iput-object p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->$onCompleteDelivery:Lkotlin/jvm/functions/Function1;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 2
    .param p1, "p1"    # Ljava/lang/Object;
    .param p2, "p2"    # Ljava/lang/Object;

    .line 420
    move-object v0, p1

    check-cast v0, Landroidx/compose/runtime/Composer;

    move-object v1, p2

    check-cast v1, Ljava/lang/Number;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->invoke(Landroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/runtime/Composer;I)V
    .locals 65
    .param p1, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p2, "$changed"    # I

    move-object/from16 v0, p0

    move/from16 v1, p2

    const-string v2, "C420@18116L2450:RepartidorScreen.kt#2thlc2"

    move-object/from16 v3, p1

    invoke-static {v3, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    and-int/lit8 v2, v1, 0x3

    const/4 v4, 0x2

    if-ne v2, v4, :cond_1

    invoke-interface {v3}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v2

    if-nez v2, :cond_0

    goto :goto_0

    .line 420
    :cond_0
    invoke-interface {v3}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    goto/16 :goto_b

    .line 0
    :cond_1
    :goto_0
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v2

    if-eqz v2, :cond_2

    const/4 v2, -0x1

    const-string v4, "com.example.ui.screens.InTransitDeliveryOrdersView.<anonymous>.<anonymous>.<anonymous>.<anonymous> (RepartidorScreen.kt:420)"

    const v5, 0x63ecd85e    # 8.738045E21f

    invoke-static {v5, v1, v2, v4}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    .line 422
    :cond_2
    sget-object v2, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    const/16 v4, 0x8

    .local v4, "$this$dp\\1":I
    const/4 v5, 0x0

    .line 1343
    .local v5, "$i$f$getDp\\1\\422":I
    int-to-float v6, v4

    invoke-static {v6}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v4

    .line 422
    .end local v4    # "$this$dp\\1":I
    .end local v5    # "$i$f$getDp\\1\\422":I
    invoke-virtual {v2, v4}, Landroidx/compose/foundation/layout/Arrangement;->spacedBy-0680j_4(F)Landroidx/compose/foundation/layout/Arrangement$HorizontalOrVertical;

    move-result-object v2

    .line 423
    sget-object v4, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v4, Landroidx/compose/ui/Modifier;

    const/4 v5, 0x0

    const/4 v6, 0x1

    const/4 v7, 0x0

    invoke-static {v4, v5, v6, v7}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v4

    .line 422
    check-cast v2, Landroidx/compose/foundation/layout/Arrangement$Vertical;

    .line 421
    iget-object v8, v0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->$order:Lcom/example/data/entity/WebOrderEntity;

    iget-object v9, v0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->$onReportProblem:Lkotlin/jvm/functions/Function1;

    iget-object v10, v0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1;->$onCompleteDelivery:Lkotlin/jvm/functions/Function1;

    const/16 v11, 0x36

    .local v2, "verticalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .local v11, "$changed\\2":I
    move-object/from16 v12, p1

    .local v4, "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v12, "$composer\\2":Landroidx/compose/runtime/Composer;
    const/4 v13, 0x0

    .line 1344
    .local v13, "$i$f$Column\\2\\421":I
    const v14, -0x1cd0f17e

    const-string v15, "CC(Column)P(2,3,1)85@4251L61,86@4317L133:Column.kt#2w3rfo"

    invoke-static {v12, v14, v15}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1345
    sget-object v14, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    invoke-virtual {v14}, Landroidx/compose/ui/Alignment$Companion;->getStart()Landroidx/compose/ui/Alignment$Horizontal;

    move-result-object v14

    .line 1348
    .local v14, "horizontalAlignment\\2":Landroidx/compose/ui/Alignment$Horizontal;
    shr-int/lit8 v15, v11, 0x3

    and-int/lit8 v15, v15, 0xe

    shr-int/lit8 v16, v11, 0x3

    and-int/lit8 v16, v16, 0x70

    or-int v15, v15, v16

    invoke-static {v2, v14, v12, v15}, Landroidx/compose/foundation/layout/ColumnKt;->columnMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Vertical;Landroidx/compose/ui/Alignment$Horizontal;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v15

    .local v15, "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v16, v11, 0x3

    and-int/lit8 v16, v16, 0x70

    .line 1349
    nop

    .local v16, "$changed\\3":I
    const/16 v17, 0x0

    .line 1350
    .local v17, "$i$f$Layout\\3\\1349":I
    const v5, -0x4ee9b9da

    const-string v6, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh"

    invoke-static {v12, v5, v6}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1351
    const/4 v5, 0x0

    invoke-static {v12, v5}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v21

    .line 1352
    .local v21, "compositeKeyHash\\3":I
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v5

    .line 1353
    .local v5, "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static {v12, v4}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v7

    .line 1355
    .local v7, "materialized\\3":Landroidx/compose/ui/Modifier;
    sget-object v24, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v24 .. v24}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v24

    shl-int/lit8 v0, v16, 0x6

    and-int/lit16 v0, v0, 0x380

    or-int/lit8 v0, v0, 0x6

    .line 1354
    nop

    .local v0, "$changed\\4":I
    move-object/from16 v25, v24

    .local v25, "factory\\4":Lkotlin/jvm/functions/Function0;
    const/16 v24, 0x0

    .line 1356
    .local v24, "$i$f$ReusableComposeNode\\4\\1354":I
    move/from16 v26, v0

    .end local v0    # "$changed\\4":I
    .local v26, "$changed\\4":I
    const v0, -0x2942ffcf

    const-string v1, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp"

    invoke-static {v12, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1357
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v0

    instance-of v0, v0, Landroidx/compose/runtime/Applier;

    if-nez v0, :cond_3

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1358
    :cond_3
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1359
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v0

    if-eqz v0, :cond_4

    .line 1360
    move-object/from16 v0, v25

    .end local v25    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .local v0, "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v12, v0}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_1

    .line 1362
    .end local v0    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v25    # "factory\\4":Lkotlin/jvm/functions/Function0;
    :cond_4
    move-object/from16 v0, v25

    .end local v25    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v0    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1364
    :goto_1
    move-object/from16 v25, v0

    .end local v0    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v25    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-static {v12}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v0

    .local v0, "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    const/16 v28, 0x0

    .line 1365
    .local v28, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1364\\3":I
    sget-object v29, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    move-object/from16 v30, v2

    .end local v2    # "verticalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .local v30, "verticalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    invoke-virtual/range {v29 .. v29}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    invoke-static {v0, v15, v2}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1366
    sget-object v2, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v2}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    invoke-static {v0, v5, v2}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1368
    sget-object v2, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v2}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    .local v2, "block\\6":Lkotlin/jvm/functions/Function2;
    const/16 v29, 0x0

    .line 1369
    .local v29, "$i$f$set-impl\\6\\1368":I
    move-object/from16 v31, v0

    .local v31, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    const/16 v32, 0x0

    .line 1370
    .local v32, "$i$a$-with-Updater$set$1\\7\\1369\\6":I
    invoke-interface/range {v31 .. v31}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v33

    if-nez v33, :cond_6

    invoke-interface/range {v31 .. v31}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v3

    move-object/from16 v33, v4

    .end local v4    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v33, "modifier\\2":Landroidx/compose/ui/Modifier;
    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    invoke-static {v3, v4}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v3

    if-nez v3, :cond_5

    goto :goto_2

    :cond_5
    move-object/from16 v4, v31

    goto :goto_3

    .end local v33    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .restart local v4    # "modifier\\2":Landroidx/compose/ui/Modifier;
    :cond_6
    move-object/from16 v33, v4

    .line 1371
    .end local v4    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .restart local v33    # "modifier\\2":Landroidx/compose/ui/Modifier;
    :goto_2
    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    move-object/from16 v4, v31

    .end local v31    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .local v4, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    invoke-interface {v4, v3}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1372
    invoke-static/range {v21 .. v21}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    invoke-interface {v0, v3, v2}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1374
    :goto_3
    nop

    .line 1369
    .end local v4    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .end local v32    # "$i$a$-with-Updater$set$1\\7\\1369\\6":I
    nop

    .line 1374
    nop

    .line 1375
    .end local v2    # "block\\6":Lkotlin/jvm/functions/Function2;
    .end local v29    # "$i$f$set-impl\\6\\1368":I
    sget-object v2, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v2}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v2

    invoke-static {v0, v7, v2}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1376
    nop

    .line 1364
    .end local v0    # "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    .end local v28    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1364\\3":I
    nop

    .line 1377
    shr-int/lit8 v0, v26, 0x6

    and-int/lit8 v0, v0, 0xe

    .local v0, "$changed\\8":I
    move-object v2, v12

    .local v2, "$composer\\8":Landroidx/compose/runtime/Composer;
    const/4 v3, 0x0

    .line 1378
    .local v3, "$i$a$-Layout-ColumnKt$Column$1\\8\\1377\\2":I
    const v4, -0x16f088b9

    move/from16 v28, v0

    .end local v0    # "$changed\\8":I
    .local v28, "$changed\\8":I
    const-string v0, "C87@4365L9:Column.kt#2w3rfo"

    invoke-static {v2, v4, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/ColumnScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/ColumnScopeInstance;

    shr-int/lit8 v4, v11, 0x6

    and-int/lit8 v4, v4, 0x70

    or-int/lit8 v4, v4, 0x6

    .local v4, "$changed\\9":I
    check-cast v0, Landroidx/compose/foundation/layout/ColumnScope;

    .local v0, "$this$invoke_u24lambda_u243\\9":Landroidx/compose/foundation/layout/ColumnScope;
    move-object/from16 v29, v2

    .local v29, "$composer\\9":Landroidx/compose/runtime/Composer;
    const/16 v31, 0x0

    .line 425
    .local v31, "$i$a$-Column-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1\\9\\1378\\0":I
    move-object/from16 v32, v0

    .end local v0    # "$this$invoke_u24lambda_u243\\9":Landroidx/compose/foundation/layout/ColumnScope;
    .local v32, "$this$invoke_u24lambda_u243\\9":Landroidx/compose/foundation/layout/ColumnScope;
    const v0, -0x4f6325b0

    move-object/from16 v34, v2

    .end local v2    # "$composer\\8":Landroidx/compose/runtime/Composer;
    .local v34, "$composer\\8":Landroidx/compose/runtime/Composer;
    const-string v2, "C424@18321L2219:RepartidorScreen.kt#2thlc2"

    move/from16 v35, v3

    move-object/from16 v3, v29

    .end local v29    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v3, "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v35, "$i$a$-Layout-ColumnKt$Column$1\\8\\1377\\2":I
    invoke-static {v3, v0, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 426
    sget-object v0, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v0, Landroidx/compose/ui/Modifier;

    move-object/from16 v18, v3

    move/from16 v19, v4

    const/4 v2, 0x0

    const/4 v3, 0x1

    const/4 v4, 0x0

    .end local v3    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .end local v4    # "$changed\\9":I
    .local v18, "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v19, "$changed\\9":I
    invoke-static {v0, v4, v3, v2}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v0

    .line 427
    sget-object v2, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    const/16 v3, 0x8

    .local v3, "$this$dp\\10":I
    const/4 v4, 0x0

    .line 1379
    .local v4, "$i$f$getDp\\10\\427":I
    move-object/from16 v23, v0

    int-to-float v0, v3

    invoke-static {v0}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v0

    .line 427
    .end local v3    # "$this$dp\\10":I
    .end local v4    # "$i$f$getDp\\10\\427":I
    invoke-virtual {v2, v0}, Landroidx/compose/foundation/layout/Arrangement;->spacedBy-0680j_4(F)Landroidx/compose/foundation/layout/Arrangement$HorizontalOrVertical;

    move-result-object v0

    check-cast v0, Landroidx/compose/foundation/layout/Arrangement$Horizontal;

    .line 425
    nop

    .local v0, "horizontalArrangement\\11":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    move-object/from16 v2, v18

    .local v2, "$composer\\11":Landroidx/compose/runtime/Composer;
    move-object/from16 v3, v23

    .local v3, "modifier\\11":Landroidx/compose/ui/Modifier;
    const/16 v4, 0x36

    .local v4, "$changed\\11":I
    const/16 v23, 0x0

    .line 1380
    .local v23, "$i$f$Row\\11\\425":I
    move/from16 v29, v4

    .end local v4    # "$changed\\11":I
    .local v29, "$changed\\11":I
    const v4, 0x2952b718

    move-object/from16 v36, v5

    .end local v5    # "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    .local v36, "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    const-string v5, "CC(Row)P(2,1,3)98@4939L58,99@5002L130:Row.kt#2w3rfo"

    invoke-static {v2, v4, v5}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1381
    sget-object v4, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    invoke-virtual {v4}, Landroidx/compose/ui/Alignment$Companion;->getTop()Landroidx/compose/ui/Alignment$Vertical;

    move-result-object v4

    .line 1384
    .local v4, "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    shr-int/lit8 v5, v29, 0x3

    and-int/lit8 v5, v5, 0xe

    shr-int/lit8 v37, v29, 0x3

    and-int/lit8 v37, v37, 0x70

    or-int v5, v5, v37

    invoke-static {v0, v4, v2, v5}, Landroidx/compose/foundation/layout/RowKt;->rowMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Horizontal;Landroidx/compose/ui/Alignment$Vertical;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v5

    .local v5, "measurePolicy\\11":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v37, v29, 0x3

    and-int/lit8 v37, v37, 0x70

    .line 1385
    nop

    .local v37, "$changed\\12":I
    const/16 v38, 0x0

    .line 1386
    .local v38, "$i$f$Layout\\12\\1385":I
    move-object/from16 v39, v0

    const v0, -0x4ee9b9da

    .end local v0    # "horizontalArrangement\\11":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .local v39, "horizontalArrangement\\11":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    invoke-static {v2, v0, v6}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1387
    const/4 v0, 0x0

    invoke-static {v2, v0}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v0

    .line 1388
    .local v0, "compositeKeyHash\\12":I
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v6

    .line 1389
    .local v6, "localMap\\12":Landroidx/compose/runtime/CompositionLocalMap;
    move/from16 v20, v0

    .end local v0    # "compositeKeyHash\\12":I
    .local v20, "compositeKeyHash\\12":I
    invoke-static {v2, v3}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v0

    .line 1391
    .local v0, "materialized\\12":Landroidx/compose/ui/Modifier;
    sget-object v22, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v22 .. v22}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v22

    move-object/from16 v40, v3

    .end local v3    # "modifier\\11":Landroidx/compose/ui/Modifier;
    .local v40, "modifier\\11":Landroidx/compose/ui/Modifier;
    shl-int/lit8 v3, v37, 0x6

    and-int/lit16 v3, v3, 0x380

    or-int/lit8 v3, v3, 0x6

    .line 1390
    nop

    .local v3, "$changed\\13":I
    move-object/from16 v41, v22

    .local v41, "factory\\13":Lkotlin/jvm/functions/Function0;
    const/16 v22, 0x0

    .line 1392
    .local v22, "$i$f$ReusableComposeNode\\13\\1390":I
    move/from16 v42, v3

    const v3, -0x2942ffcf

    .end local v3    # "$changed\\13":I
    .local v42, "$changed\\13":I
    invoke-static {v2, v3, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1393
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v1

    instance-of v1, v1, Landroidx/compose/runtime/Applier;

    if-nez v1, :cond_7

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1394
    :cond_7
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1395
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v1

    if-eqz v1, :cond_8

    .line 1396
    move-object/from16 v1, v41

    .end local v41    # "factory\\13":Lkotlin/jvm/functions/Function0;
    .local v1, "factory\\13":Lkotlin/jvm/functions/Function0;
    invoke-interface {v2, v1}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_4

    .line 1398
    .end local v1    # "factory\\13":Lkotlin/jvm/functions/Function0;
    .restart local v41    # "factory\\13":Lkotlin/jvm/functions/Function0;
    :cond_8
    move-object/from16 v1, v41

    .end local v41    # "factory\\13":Lkotlin/jvm/functions/Function0;
    .restart local v1    # "factory\\13":Lkotlin/jvm/functions/Function0;
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1400
    :goto_4
    invoke-static {v2}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v3

    .local v3, "$this$Layout_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    const/16 v27, 0x0

    .line 1401
    .local v27, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\14\\1400\\12":I
    sget-object v41, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    move-object/from16 v43, v1

    .end local v1    # "factory\\13":Lkotlin/jvm/functions/Function0;
    .local v43, "factory\\13":Lkotlin/jvm/functions/Function0;
    invoke-virtual/range {v41 .. v41}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v5, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1402
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v6, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1404
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    .local v1, "block\\15":Lkotlin/jvm/functions/Function2;
    const/16 v41, 0x0

    .line 1405
    .local v41, "$i$f$set-impl\\15\\1404":I
    move-object/from16 v44, v3

    .local v44, "$this$set_impl_u24lambda_u240\\15":Landroidx/compose/runtime/Composer;
    const/16 v45, 0x0

    .line 1406
    .local v45, "$i$a$-with-Updater$set$1\\16\\1405\\15":I
    invoke-interface/range {v44 .. v44}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v46

    if-nez v46, :cond_a

    move-object/from16 v46, v2

    .end local v2    # "$composer\\11":Landroidx/compose/runtime/Composer;
    .local v46, "$composer\\11":Landroidx/compose/runtime/Composer;
    invoke-interface/range {v44 .. v44}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v2

    move-object/from16 v47, v4

    .end local v4    # "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    .local v47, "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    invoke-static/range {v20 .. v20}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    invoke-static {v2, v4}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_9

    goto :goto_5

    :cond_9
    move-object/from16 v4, v44

    goto :goto_6

    .end local v46    # "$composer\\11":Landroidx/compose/runtime/Composer;
    .end local v47    # "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    .restart local v2    # "$composer\\11":Landroidx/compose/runtime/Composer;
    .restart local v4    # "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    :cond_a
    move-object/from16 v46, v2

    move-object/from16 v47, v4

    .line 1407
    .end local v2    # "$composer\\11":Landroidx/compose/runtime/Composer;
    .end local v4    # "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    .restart local v46    # "$composer\\11":Landroidx/compose/runtime/Composer;
    .restart local v47    # "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    :goto_5
    invoke-static/range {v20 .. v20}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    move-object/from16 v4, v44

    .end local v44    # "$this$set_impl_u24lambda_u240\\15":Landroidx/compose/runtime/Composer;
    .local v4, "$this$set_impl_u24lambda_u240\\15":Landroidx/compose/runtime/Composer;
    invoke-interface {v4, v2}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1408
    invoke-static/range {v20 .. v20}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-interface {v3, v2, v1}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1410
    :goto_6
    nop

    .line 1405
    .end local v4    # "$this$set_impl_u24lambda_u240\\15":Landroidx/compose/runtime/Composer;
    .end local v45    # "$i$a$-with-Updater$set$1\\16\\1405\\15":I
    nop

    .line 1410
    nop

    .line 1411
    .end local v1    # "block\\15":Lkotlin/jvm/functions/Function2;
    .end local v41    # "$i$f$set-impl\\15\\1404":I
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v0, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1412
    nop

    .line 1400
    .end local v3    # "$this$Layout_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    .end local v27    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\14\\1400\\12":I
    nop

    .line 1413
    shr-int/lit8 v1, v42, 0x6

    and-int/lit8 v1, v1, 0xe

    .local v1, "$changed\\17":I
    move-object/from16 v2, v46

    .local v2, "$composer\\17":Landroidx/compose/runtime/Composer;
    const/4 v3, 0x0

    .line 1414
    .local v3, "$i$a$-Layout-RowKt$Row$1\\17\\1413\\11":I
    const v4, -0x18505826

    move-object/from16 v27, v0

    .end local v0    # "materialized\\12":Landroidx/compose/ui/Modifier;
    .local v27, "materialized\\12":Landroidx/compose/ui/Modifier;
    const-string v0, "C100@5047L9:Row.kt#2w3rfo"

    invoke-static {v2, v4, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/RowScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/RowScopeInstance;

    shr-int/lit8 v4, v29, 0x6

    and-int/lit8 v4, v4, 0x70

    or-int/lit8 v4, v4, 0x6

    .local v4, "$changed\\18":I
    check-cast v0, Landroidx/compose/foundation/layout/RowScope;

    .local v0, "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    move-object/from16 v57, v2

    .local v57, "$composer\\18":Landroidx/compose/runtime/Composer;
    const/16 v41, 0x0

    .line 430
    .local v41, "$i$a$-Row-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1\\18\\1414\\9":I
    move-object/from16 v44, v0

    .end local v0    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    .local v44, "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    const v0, -0x316be58

    move/from16 v45, v1

    .end local v1    # "$changed\\17":I
    .local v45, "$changed\\17":I
    const-string v1, "C431@18799L11,431@18749L68,432@18897L11,430@18661L26,429@18599L971,446@19809L43,445@19715L32,444@19661L849:RepartidorScreen.kt#2thlc2"

    move-object/from16 v61, v2

    .end local v57    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .local v2, "$composer\\18":Landroidx/compose/runtime/Composer;
    .local v61, "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-static {v2, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 432
    sget-object v48, Landroidx/compose/material3/ButtonDefaults;->INSTANCE:Landroidx/compose/material3/ButtonDefaults;

    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/ColorScheme;->getError-0d7_KjU()J

    move-result-wide v51

    sget v0, Landroidx/compose/material3/ButtonDefaults;->$stable:I

    shl-int/lit8 v58, v0, 0xc

    const/16 v59, 0xd

    const-wide/16 v49, 0x0

    const-wide/16 v53, 0x0

    const-wide/16 v55, 0x0

    .end local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v57    # "$composer\\18":Landroidx/compose/runtime/Composer;
    invoke-virtual/range {v48 .. v59}, Landroidx/compose/material3/ButtonDefaults;->outlinedButtonColors-ro_MJ88(JJJJLandroidx/compose/runtime/Composer;II)Landroidx/compose/material3/ButtonColors;

    move-result-object v0

    .line 433
    .end local v57    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    const/4 v1, 0x1

    .local v1, "$this$dp\\19":I
    const/16 v48, 0x0

    .line 1415
    .local v48, "$i$f$getDp\\19\\433":I
    move-object/from16 v54, v0

    int-to-float v0, v1

    invoke-static {v0}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v0

    .line 433
    .end local v1    # "$this$dp\\19":I
    .end local v48    # "$i$f$getDp\\19\\433":I
    sget-object v1, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    move/from16 v62, v3

    .end local v3    # "$i$a$-Layout-RowKt$Row$1\\17\\1413\\11":I
    .local v62, "$i$a$-Layout-RowKt$Row$1\\17\\1413\\11":I
    sget v3, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v1, v2, v3}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v1

    move/from16 v63, v4

    .end local v4    # "$changed\\18":I
    .local v63, "$changed\\18":I
    invoke-virtual {v1}, Landroidx/compose/material3/ColorScheme;->getError-0d7_KjU()J

    move-result-wide v3

    invoke-static {v0, v3, v4}, Landroidx/compose/foundation/BorderStrokeKt;->BorderStroke-cXLIe8U(FJ)Landroidx/compose/foundation/BorderStroke;

    move-result-object v0

    .line 434
    const/16 v1, 0x8

    .local v1, "$this$dp\\20":I
    const/4 v3, 0x0

    .line 1416
    .local v3, "$i$f$getDp\\20\\434":I
    int-to-float v4, v1

    invoke-static {v4}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v1

    .line 434
    .end local v1    # "$this$dp\\20":I
    .end local v3    # "$i$f$getDp\\20\\434":I
    invoke-static {v1}, Landroidx/compose/foundation/shape/RoundedCornerShapeKt;->RoundedCornerShape-0680j_4(F)Landroidx/compose/foundation/shape/RoundedCornerShape;

    move-result-object v1

    .line 435
    sget-object v3, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    move-object/from16 v49, v3

    check-cast v49, Landroidx/compose/ui/Modifier;

    .line 436
    const/16 v52, 0x2

    const/16 v53, 0x0

    const/high16 v50, 0x3f800000    # 1.0f

    const/16 v51, 0x0

    move-object/from16 v48, v44

    .end local v44    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    .local v48, "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    invoke-static/range {v48 .. v53}, Landroidx/compose/foundation/layout/RowScope;->weight$default(Landroidx/compose/foundation/layout/RowScope;Landroidx/compose/ui/Modifier;FZILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v3

    .line 437
    .end local v48    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    .restart local v44    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    move-object v4, v0

    move-object/from16 v48, v1

    invoke-virtual {v8}, Lcom/example/data/entity/WebOrderEntity;->getId()J

    move-result-wide v0

    move-object/from16 v49, v4

    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v64, v5

    .end local v5    # "measurePolicy\\11":Landroidx/compose/ui/layout/MeasurePolicy;
    .local v64, "measurePolicy\\11":Landroidx/compose/ui/layout/MeasurePolicy;
    const-string v5, "btn_reportar_problema_"

    invoke-virtual {v4, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v4

    invoke-virtual {v4, v0, v1}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v3, v0}, Landroidx/compose/ui/platform/TestTagKt;->testTag(Landroidx/compose/ui/Modifier;Ljava/lang/String;)Landroidx/compose/ui/Modifier;

    move-result-object v0

    .line 431
    const v1, 0x7bc5761e

    const-string v3, "CC(remember):RepartidorScreen.kt#9igjgp"

    invoke-static {v2, v1, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    invoke-interface {v2, v9}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v1

    invoke-interface {v2, v8}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v4

    or-int/2addr v1, v4

    .local v1, "invalid\\21":Z
    move-object v4, v2

    .local v4, "$this$cache\\21":Landroidx/compose/runtime/Composer;
    const/4 v5, 0x0

    .line 1417
    .local v5, "$i$f$cache\\21\\431":I
    move-object/from16 v50, v0

    invoke-interface {v4}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v0

    .local v0, "it\\21":Ljava/lang/Object;
    const/16 v51, 0x0

    .line 1418
    .local v51, "$i$a$-let-ComposerKt$cache$1\\22\\1417\\21":I
    if-nez v1, :cond_c

    sget-object v52, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    move/from16 v53, v1

    .end local v1    # "invalid\\21":Z
    .local v53, "invalid\\21":Z
    invoke-virtual/range {v52 .. v52}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v1

    if-ne v0, v1, :cond_b

    goto :goto_7

    .line 1422
    :cond_b
    move-object/from16 v52, v0

    goto :goto_8

    .line 1418
    .end local v53    # "invalid\\21":Z
    .restart local v1    # "invalid\\21":Z
    :cond_c
    move/from16 v53, v1

    .line 1419
    .end local v1    # "invalid\\21":Z
    .restart local v53    # "invalid\\21":Z
    :goto_7
    const/4 v1, 0x0

    .line 431
    .local v1, "$i$a$-cache-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$1\\23\\1419\\18":I
    move-object/from16 v52, v0

    .end local v0    # "it\\21":Ljava/lang/Object;
    .local v52, "it\\21":Ljava/lang/Object;
    new-instance v0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$1$1;

    invoke-direct {v0, v9, v8}, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$1$1;-><init>(Lkotlin/jvm/functions/Function1;Lcom/example/data/entity/WebOrderEntity;)V

    check-cast v0, Lkotlin/jvm/functions/Function0;

    .line 1419
    .end local v1    # "$i$a$-cache-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$1\\23\\1419\\18":I
    nop

    .line 1420
    .local v0, "value\\22":Ljava/lang/Object;
    invoke-interface {v4, v0}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1421
    nop

    .line 1418
    .end local v0    # "value\\22":Ljava/lang/Object;
    :goto_8
    nop

    .line 1417
    .end local v51    # "$i$a$-let-ComposerKt$cache$1\\22\\1417\\21":I
    .end local v52    # "it\\21":Ljava/lang/Object;
    nop

    .line 431
    .end local v4    # "$this$cache\\21":Landroidx/compose/runtime/Composer;
    .end local v5    # "$i$f$cache\\21\\431":I
    .end local v53    # "invalid\\21":Z
    check-cast v0, Lkotlin/jvm/functions/Function0;

    invoke-static {v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 437
    nop

    .line 434
    move-object/from16 v51, v48

    check-cast v51, Landroidx/compose/ui/graphics/Shape;

    .line 432
    nop

    .line 433
    sget-object v1, Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;->INSTANCE:Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;

    invoke-virtual {v1}, Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;->getLambda$-783225070$app()Lkotlin/jvm/functions/Function3;

    move-result-object v57

    .line 430
    move-object/from16 v4, v49

    move-object/from16 v49, v50

    const/16 v50, 0x0

    const/16 v53, 0x0

    const/16 v55, 0x0

    const/16 v56, 0x0

    const/high16 v59, 0x30000000

    const/16 v60, 0x1a4

    move-object/from16 v48, v0

    move-object/from16 v58, v2

    move-object/from16 v52, v54

    move-object/from16 v54, v4

    .end local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .local v58, "$composer\\18":Landroidx/compose/runtime/Composer;
    invoke-static/range {v48 .. v60}, Landroidx/compose/material3/ButtonKt;->OutlinedButton(Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;ZLandroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/ButtonColors;Landroidx/compose/material3/ButtonElevation;Landroidx/compose/foundation/BorderStroke;Landroidx/compose/foundation/layout/PaddingValues;Landroidx/compose/foundation/interaction/MutableInteractionSource;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V

    .line 447
    .end local v58    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    sget-object v48, Landroidx/compose/material3/ButtonDefaults;->INSTANCE:Landroidx/compose/material3/ButtonDefaults;

    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getSuccessGreen()J

    move-result-wide v49

    sget v0, Landroidx/compose/material3/ButtonDefaults;->$stable:I

    shl-int/lit8 v0, v0, 0xc

    or-int/lit8 v58, v0, 0x6

    const/16 v59, 0xe

    const-wide/16 v51, 0x0

    const-wide/16 v53, 0x0

    const-wide/16 v55, 0x0

    move-object/from16 v57, v2

    .end local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v57    # "$composer\\18":Landroidx/compose/runtime/Composer;
    invoke-virtual/range {v48 .. v59}, Landroidx/compose/material3/ButtonDefaults;->buttonColors-ro_MJ88(JJJJLandroidx/compose/runtime/Composer;II)Landroidx/compose/material3/ButtonColors;

    move-result-object v0

    .line 448
    .end local v57    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    const/16 v1, 0x8

    .local v1, "$this$dp\\24":I
    const/4 v4, 0x0

    .line 1423
    .local v4, "$i$f$getDp\\24\\448":I
    int-to-float v5, v1

    invoke-static {v5}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v1

    .line 448
    .end local v1    # "$this$dp\\24":I
    .end local v4    # "$i$f$getDp\\24\\448":I
    invoke-static {v1}, Landroidx/compose/foundation/shape/RoundedCornerShapeKt;->RoundedCornerShape-0680j_4(F)Landroidx/compose/foundation/shape/RoundedCornerShape;

    move-result-object v1

    .line 449
    sget-object v4, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    move-object/from16 v49, v4

    check-cast v49, Landroidx/compose/ui/Modifier;

    .line 450
    const/16 v52, 0x2

    const/16 v53, 0x0

    const v50, 0x3fa66666    # 1.3f

    const/16 v51, 0x0

    move-object/from16 v48, v44

    .end local v44    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    .restart local v48    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    invoke-static/range {v48 .. v53}, Landroidx/compose/foundation/layout/RowScope;->weight$default(Landroidx/compose/foundation/layout/RowScope;Landroidx/compose/ui/Modifier;FZILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v4

    .line 451
    .end local v48    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    .restart local v44    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    move-object/from16 v52, v0

    move-object v5, v1

    invoke-virtual {v8}, Lcom/example/data/entity/WebOrderEntity;->getId()J

    move-result-wide v0

    new-instance v9, Ljava/lang/StringBuilder;

    invoke-direct {v9}, Ljava/lang/StringBuilder;-><init>()V

    move-object/from16 v48, v5

    const-string v5, "btn_entregado_"

    invoke-virtual {v9, v5}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v5

    invoke-virtual {v5, v0, v1}, Ljava/lang/StringBuilder;->append(J)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v0

    invoke-static {v4, v0}, Landroidx/compose/ui/platform/TestTagKt;->testTag(Landroidx/compose/ui/Modifier;Ljava/lang/String;)Landroidx/compose/ui/Modifier;

    move-result-object v49

    .line 446
    const v0, 0x7bc5f9e4

    invoke-static {v2, v0, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    invoke-interface {v2, v10}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v0

    invoke-interface {v2, v8}, Landroidx/compose/runtime/Composer;->changed(Ljava/lang/Object;)Z

    move-result v1

    or-int/2addr v0, v1

    .local v0, "invalid\\25":Z
    move-object v1, v2

    .local v1, "$this$cache\\25":Landroidx/compose/runtime/Composer;
    const/4 v3, 0x0

    .line 1424
    .local v3, "$i$f$cache\\25\\446":I
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v4

    .local v4, "it\\25":Ljava/lang/Object;
    const/4 v5, 0x0

    .line 1425
    .local v5, "$i$a$-let-ComposerKt$cache$1\\26\\1424\\25":I
    if-nez v0, :cond_e

    sget-object v9, Landroidx/compose/runtime/Composer;->Companion:Landroidx/compose/runtime/Composer$Companion;

    invoke-virtual {v9}, Landroidx/compose/runtime/Composer$Companion;->getEmpty()Ljava/lang/Object;

    move-result-object v9

    if-ne v4, v9, :cond_d

    goto :goto_9

    .line 1429
    :cond_d
    move/from16 v50, v0

    move-object v0, v4

    goto :goto_a

    .line 1426
    :cond_e
    :goto_9
    const/4 v9, 0x0

    .line 446
    .local v9, "$i$a$-cache-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$2\\27\\1426\\18":I
    move/from16 v50, v0

    .end local v0    # "invalid\\25":Z
    .local v50, "invalid\\25":Z
    new-instance v0, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$2$1;

    invoke-direct {v0, v10, v8}, Lcom/example/ui/screens/RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$2$1;-><init>(Lkotlin/jvm/functions/Function1;Lcom/example/data/entity/WebOrderEntity;)V

    check-cast v0, Lkotlin/jvm/functions/Function0;

    .line 1426
    .end local v9    # "$i$a$-cache-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1$2\\27\\1426\\18":I
    nop

    .line 1427
    .local v0, "value\\26":Ljava/lang/Object;
    invoke-interface {v1, v0}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1428
    nop

    .line 1425
    .end local v0    # "value\\26":Ljava/lang/Object;
    :goto_a
    nop

    .line 1424
    .end local v4    # "it\\25":Ljava/lang/Object;
    .end local v5    # "$i$a$-let-ComposerKt$cache$1\\26\\1424\\25":I
    nop

    .line 446
    .end local v1    # "$this$cache\\25":Landroidx/compose/runtime/Composer;
    .end local v3    # "$i$f$cache\\25\\446":I
    .end local v50    # "invalid\\25":Z
    check-cast v0, Lkotlin/jvm/functions/Function0;

    invoke-static {v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 451
    nop

    .line 448
    move-object/from16 v51, v48

    check-cast v51, Landroidx/compose/ui/graphics/Shape;

    .line 447
    sget-object v1, Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;->INSTANCE:Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;

    invoke-virtual {v1}, Lcom/example/ui/screens/ComposableSingletons$RepartidorScreenKt;->getLambda$-1333004844$app()Lkotlin/jvm/functions/Function3;

    move-result-object v57

    .line 445
    const/16 v50, 0x0

    const/16 v53, 0x0

    const/16 v54, 0x0

    const/16 v55, 0x0

    const/16 v56, 0x0

    const/high16 v59, 0x30000000

    const/16 v60, 0x1e4

    move-object/from16 v48, v0

    move-object/from16 v58, v2

    .end local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v58    # "$composer\\18":Landroidx/compose/runtime/Composer;
    invoke-static/range {v48 .. v60}, Landroidx/compose/material3/ButtonKt;->Button(Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;ZLandroidx/compose/ui/graphics/Shape;Landroidx/compose/material3/ButtonColors;Landroidx/compose/material3/ButtonElevation;Landroidx/compose/foundation/BorderStroke;Landroidx/compose/foundation/layout/PaddingValues;Landroidx/compose/foundation/interaction/MutableInteractionSource;Lkotlin/jvm/functions/Function3;Landroidx/compose/runtime/Composer;II)V

    .line 430
    .end local v58    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    invoke-static {v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 457
    nop

    .line 1414
    .end local v2    # "$composer\\18":Landroidx/compose/runtime/Composer;
    .end local v41    # "$i$a$-Row-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1$1\\18\\1414\\9":I
    .end local v44    # "$this$invoke_u24lambda_u243_u24lambda_u242\\18":Landroidx/compose/foundation/layout/RowScope;
    .end local v63    # "$changed\\18":I
    invoke-static/range {v61 .. v61}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1413
    .end local v45    # "$changed\\17":I
    .end local v61    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .end local v62    # "$i$a$-Layout-RowKt$Row$1\\17\\1413\\11":I
    nop

    .line 1430
    invoke-interface/range {v46 .. v46}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1392
    invoke-static/range {v46 .. v46}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1431
    nop

    .line 1386
    .end local v22    # "$i$f$ReusableComposeNode\\13\\1390":I
    .end local v42    # "$changed\\13":I
    .end local v43    # "factory\\13":Lkotlin/jvm/functions/Function0;
    invoke-static/range {v46 .. v46}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1432
    nop

    .line 1380
    .end local v6    # "localMap\\12":Landroidx/compose/runtime/CompositionLocalMap;
    .end local v20    # "compositeKeyHash\\12":I
    .end local v27    # "materialized\\12":Landroidx/compose/ui/Modifier;
    .end local v37    # "$changed\\12":I
    .end local v38    # "$i$f$Layout\\12\\1385":I
    invoke-static/range {v46 .. v46}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1433
    nop

    .line 425
    .end local v23    # "$i$f$Row\\11\\425":I
    .end local v29    # "$changed\\11":I
    .end local v39    # "horizontalArrangement\\11":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v40    # "modifier\\11":Landroidx/compose/ui/Modifier;
    .end local v46    # "$composer\\11":Landroidx/compose/runtime/Composer;
    .end local v47    # "verticalAlignment\\11":Landroidx/compose/ui/Alignment$Vertical;
    .end local v64    # "measurePolicy\\11":Landroidx/compose/ui/layout/MeasurePolicy;
    invoke-static/range {v18 .. v18}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 458
    nop

    .line 1378
    .end local v18    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .end local v19    # "$changed\\9":I
    .end local v31    # "$i$a$-Column-RepartidorScreenKt$InTransitDeliveryOrdersView$2$1$3$1$1\\9\\1378\\0":I
    .end local v32    # "$this$invoke_u24lambda_u243\\9":Landroidx/compose/foundation/layout/ColumnScope;
    invoke-static/range {v34 .. v34}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1377
    .end local v28    # "$changed\\8":I
    .end local v34    # "$composer\\8":Landroidx/compose/runtime/Composer;
    .end local v35    # "$i$a$-Layout-ColumnKt$Column$1\\8\\1377\\2":I
    nop

    .line 1434
    invoke-interface {v12}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1356
    invoke-static {v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1435
    nop

    .line 1350
    .end local v24    # "$i$f$ReusableComposeNode\\4\\1354":I
    .end local v25    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .end local v26    # "$changed\\4":I
    invoke-static {v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1436
    nop

    .line 1344
    .end local v7    # "materialized\\3":Landroidx/compose/ui/Modifier;
    .end local v16    # "$changed\\3":I
    .end local v17    # "$i$f$Layout\\3\\1349":I
    .end local v21    # "compositeKeyHash\\3":I
    .end local v36    # "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static {v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1437
    nop

    .end local v11    # "$changed\\2":I
    .end local v12    # "$composer\\2":Landroidx/compose/runtime/Composer;
    .end local v13    # "$i$f$Column\\2\\421":I
    .end local v14    # "horizontalAlignment\\2":Landroidx/compose/ui/Alignment$Horizontal;
    .end local v15    # "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v30    # "verticalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .end local v33    # "modifier\\2":Landroidx/compose/ui/Modifier;
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_f

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 459
    :cond_f
    :goto_b
    return-void
.end method
