.class final Lcom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3;
.super Ljava/lang/Object;
.source "RepartidorScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1;->invoke(Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nRepartidorScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3\n+ 2 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 3 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n+ 4 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 5 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 6 Composer.kt\nandroidx/compose/runtime/Updater\n+ 7 fake.kt\nkotlin/jvm/internal/FakeKt\n*L\n1#1,1342:1\n148#2:1343\n85#3:1344\n82#3,6:1345\n88#3:1379\n92#3:1384\n78#4,6:1351\n85#4,4:1366\n89#4,2:1376\n93#4:1383\n368#5,9:1357\n377#5:1378\n378#5,2:1381\n4032#6,6:1370\n1#7:1380\n*S KotlinDebug\n*F\n+ 1 RepartidorScreen.kt\ncom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3\n*L\n538#1:1343\n538#1:1344\n538#1:1345,6\n538#1:1379\n538#1:1384\n538#1:1351,6\n538#1:1366,4\n538#1:1376,2\n538#1:1383\n538#1:1357,9\n538#1:1378\n538#1:1381,2\n538#1:1370,6\n*E\n"
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
.field final synthetic $order:Lcom/example/data/entity/WebOrderEntity;


# direct methods
.method constructor <init>(Lcom/example/data/entity/WebOrderEntity;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3;->$order:Lcom/example/data/entity/WebOrderEntity;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 2
    .param p1, "p1"    # Ljava/lang/Object;
    .param p2, "p2"    # Ljava/lang/Object;

    .line 537
    move-object v0, p1

    check-cast v0, Landroidx/compose/runtime/Composer;

    move-object v1, p2

    check-cast v1, Ljava/lang/Number;

    invoke-virtual {v1}, Ljava/lang/Number;->intValue()I

    move-result v1

    invoke-virtual {p0, v0, v1}, Lcom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3;->invoke(Landroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/runtime/Composer;I)V
    .locals 50
    .param p1, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p2, "$changed"    # I

    move/from16 v0, p2

    const-string v1, "C537@23930L741:RepartidorScreen.kt#2thlc2"

    move-object/from16 v2, p1

    invoke-static {v2, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    and-int/lit8 v1, v0, 0x3

    const/4 v3, 0x2

    if-ne v1, v3, :cond_1

    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v1

    if-nez v1, :cond_0

    goto :goto_0

    .line 537
    :cond_0
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    goto/16 :goto_4

    .line 0
    :cond_1
    :goto_0
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_2

    const/4 v1, -0x1

    const-string v3, "com.example.ui.screens.IncidentDeliveryOrdersView.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (RepartidorScreen.kt:537)"

    const v4, 0x69941b3e

    invoke-static {v4, v0, v1, v3}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    .line 538
    :cond_2
    sget-object v1, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v1, Landroidx/compose/ui/Modifier;

    const/16 v3, 0xa

    .local v3, "$this$dp\\1":I
    const/4 v4, 0x0

    .line 1343
    .local v4, "$i$f$getDp\\1\\538":I
    int-to-float v5, v3

    invoke-static {v5}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v3

    .line 538
    .end local v3    # "$this$dp\\1":I
    .end local v4    # "$i$f$getDp\\1\\538":I
    invoke-static {v1, v3}, Landroidx/compose/foundation/layout/PaddingKt;->padding-3ABfNKs(Landroidx/compose/ui/Modifier;F)Landroidx/compose/ui/Modifier;

    move-result-object v1

    move-object/from16 v3, p0

    .local v1, "modifier\\2":Landroidx/compose/ui/Modifier;
    iget-object v4, v3, Lcom/example/ui/screens/RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3;->$order:Lcom/example/data/entity/WebOrderEntity;

    const/4 v5, 0x6

    .local v5, "$changed\\2":I
    move-object/from16 v6, p1

    .local v6, "$composer\\2":Landroidx/compose/runtime/Composer;
    const/4 v7, 0x0

    .line 1344
    .local v7, "$i$f$Column\\2\\538":I
    const v8, -0x1cd0f17e

    const-string v9, "CC(Column)P(2,3,1)85@4251L61,86@4317L133:Column.kt#2w3rfo"

    invoke-static {v6, v8, v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1345
    sget-object v8, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    invoke-virtual {v8}, Landroidx/compose/foundation/layout/Arrangement;->getTop()Landroidx/compose/foundation/layout/Arrangement$Vertical;

    move-result-object v8

    .line 1346
    .local v8, "verticalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    sget-object v9, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    invoke-virtual {v9}, Landroidx/compose/ui/Alignment$Companion;->getStart()Landroidx/compose/ui/Alignment$Horizontal;

    move-result-object v9

    .line 1349
    .local v9, "horizontalAlignment\\2":Landroidx/compose/ui/Alignment$Horizontal;
    shr-int/lit8 v10, v5, 0x3

    and-int/lit8 v10, v10, 0xe

    shr-int/lit8 v11, v5, 0x3

    and-int/lit8 v11, v11, 0x70

    or-int/2addr v10, v11

    invoke-static {v8, v9, v6, v10}, Landroidx/compose/foundation/layout/ColumnKt;->columnMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Vertical;Landroidx/compose/ui/Alignment$Horizontal;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v10

    .local v10, "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v11, v5, 0x3

    and-int/lit8 v11, v11, 0x70

    .line 1350
    nop

    .local v11, "$changed\\3":I
    const/4 v12, 0x0

    .line 1351
    .local v12, "$i$f$Layout\\3\\1350":I
    const v13, -0x4ee9b9da

    const-string v14, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh"

    invoke-static {v6, v13, v14}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1352
    const/4 v13, 0x0

    invoke-static {v6, v13}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v13

    .line 1353
    .local v13, "compositeKeyHash\\3":I
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v14

    .line 1354
    .local v14, "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static {v6, v1}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v15

    .line 1356
    .local v15, "materialized\\3":Landroidx/compose/ui/Modifier;
    sget-object v16, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v16 .. v16}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v16

    shl-int/lit8 v0, v11, 0x6

    and-int/lit16 v0, v0, 0x380

    or-int/lit8 v0, v0, 0x6

    .line 1355
    move-object/from16 v17, v16

    .local v0, "$changed\\4":I
    .local v17, "factory\\4":Lkotlin/jvm/functions/Function0;
    const/16 v16, 0x0

    .line 1357
    .local v16, "$i$f$ReusableComposeNode\\4\\1355":I
    move/from16 v18, v0

    .end local v0    # "$changed\\4":I
    .local v18, "$changed\\4":I
    const v0, -0x2942ffcf

    move-object/from16 v19, v1

    .end local v1    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v19, "modifier\\2":Landroidx/compose/ui/Modifier;
    const-string v1, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp"

    invoke-static {v6, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1358
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v0

    instance-of v0, v0, Landroidx/compose/runtime/Applier;

    if-nez v0, :cond_3

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1359
    :cond_3
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1360
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v0

    if-eqz v0, :cond_4

    .line 1361
    move-object/from16 v0, v17

    .end local v17    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .local v0, "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v6, v0}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_1

    .line 1363
    .end local v0    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v17    # "factory\\4":Lkotlin/jvm/functions/Function0;
    :cond_4
    move-object/from16 v0, v17

    .end local v17    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v0    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1365
    :goto_1
    invoke-static {v6}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v1

    .local v1, "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    const/16 v17, 0x0

    .line 1366
    .local v17, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1365\\3":I
    sget-object v20, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    move-object/from16 v21, v0

    .end local v0    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .local v21, "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-virtual/range {v20 .. v20}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    invoke-static {v1, v10, v0}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1367
    sget-object v0, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    invoke-static {v1, v14, v0}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1369
    sget-object v0, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    .local v0, "block\\6":Lkotlin/jvm/functions/Function2;
    const/16 v20, 0x0

    .line 1370
    .local v20, "$i$f$set-impl\\6\\1369":I
    move-object/from16 v22, v1

    .local v22, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    const/16 v23, 0x0

    .line 1371
    .local v23, "$i$a$-with-Updater$set$1\\7\\1370\\6":I
    invoke-interface/range {v22 .. v22}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v24

    if-nez v24, :cond_6

    invoke-interface/range {v22 .. v22}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v2

    invoke-static {v13}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v3

    invoke-static {v2, v3}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_5

    goto :goto_2

    :cond_5
    move-object/from16 v3, v22

    goto :goto_3

    .line 1372
    :cond_6
    :goto_2
    invoke-static {v13}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    move-object/from16 v3, v22

    .end local v22    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .local v3, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    invoke-interface {v3, v2}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1373
    invoke-static {v13}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-interface {v1, v2, v0}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1375
    :goto_3
    nop

    .line 1370
    .end local v3    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .end local v23    # "$i$a$-with-Updater$set$1\\7\\1370\\6":I
    nop

    .line 1375
    nop

    .line 1376
    .end local v0    # "block\\6":Lkotlin/jvm/functions/Function2;
    .end local v20    # "$i$f$set-impl\\6\\1369":I
    sget-object v0, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    invoke-static {v1, v15, v0}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1377
    nop

    .line 1365
    .end local v1    # "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    .end local v17    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1365\\3":I
    nop

    .line 1378
    shr-int/lit8 v0, v18, 0x6

    and-int/lit8 v0, v0, 0xe

    .local v0, "$changed\\8":I
    move-object v1, v6

    .local v1, "$composer\\8":Landroidx/compose/runtime/Composer;
    const/4 v2, 0x0

    .line 1379
    .local v2, "$i$a$-Layout-ColumnKt$Column$1\\8\\1378\\2":I
    const v3, -0x16f088b9

    move/from16 v17, v0

    .end local v0    # "$changed\\8":I
    .local v17, "$changed\\8":I
    const-string v0, "C87@4365L9:Column.kt#2w3rfo"

    invoke-static {v1, v3, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/ColumnScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/ColumnScopeInstance;

    shr-int/lit8 v3, v5, 0x6

    and-int/lit8 v3, v3, 0x70

    or-int/lit8 v3, v3, 0x6

    .local v3, "$changed\\9":I
    check-cast v0, Landroidx/compose/foundation/layout/ColumnScope;

    .local v0, "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/ColumnScope;
    move-object/from16 v43, v1

    .local v43, "$composer\\9":Landroidx/compose/runtime/Composer;
    const/16 v20, 0x0

    .line 539
    .local v20, "$i$a$-Column-RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3$1\\9\\1379\\0":I
    move-object/from16 v47, v0

    .end local v0    # "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/ColumnScope;
    .local v47, "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/ColumnScope;
    const v0, 0x26f7af81

    move-object/from16 v48, v1

    .end local v1    # "$composer\\8":Landroidx/compose/runtime/Composer;
    .local v48, "$composer\\8":Landroidx/compose/runtime/Composer;
    const-string v1, "C542@24262L11,538@24007L306,544@24346L295:RepartidorScreen.kt#2thlc2"

    move/from16 v49, v2

    move-object/from16 v2, v43

    .end local v43    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v2, "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v49, "$i$a$-Layout-ColumnKt$Column$1\\8\\1378\\2":I
    invoke-static {v2, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 541
    sget-object v0, Landroidx/compose/ui/text/font/FontWeight;->Companion:Landroidx/compose/ui/text/font/FontWeight$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/text/font/FontWeight$Companion;->getBold()Landroidx/compose/ui/text/font/FontWeight;

    move-result-object v29

    .line 542
    const/16 v0, 0xb

    invoke-static {v0}, Landroidx/compose/ui/unit/TextUnitKt;->getSp(I)J

    move-result-wide v26

    .line 543
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/ColorScheme;->getError-0d7_KjU()J

    move-result-wide v24

    .line 540
    nop

    .line 543
    nop

    .line 542
    nop

    .line 541
    nop

    .line 539
    const-string v22, "Motivo de la incidencia:"

    const/16 v23, 0x0

    const/16 v28, 0x0

    const/16 v30, 0x0

    const-wide/16 v31, 0x0

    const/16 v33, 0x0

    const/16 v34, 0x0

    const-wide/16 v35, 0x0

    const/16 v37, 0x0

    const/16 v38, 0x0

    const/16 v39, 0x0

    const/16 v40, 0x0

    const/16 v41, 0x0

    const/16 v42, 0x0

    const v44, 0x30c06

    const/16 v45, 0x0

    const v46, 0x1ffd2

    .end local v2    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .restart local v43    # "$composer\\9":Landroidx/compose/runtime/Composer;
    invoke-static/range {v22 .. v46}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 546
    invoke-virtual {v4}, Lcom/example/data/entity/WebOrderEntity;->getDeliveryIssueNote()Ljava/lang/String;

    move-result-object v0

    check-cast v0, Ljava/lang/CharSequence;

    invoke-static {v0}, Lkotlin/text/StringsKt;->isBlank(Ljava/lang/CharSequence;)Z

    move-result v1

    if-eqz v1, :cond_7

    .line 1380
    const/4 v0, 0x0

    .line 546
    .local v0, "$i$a$-ifBlank-RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3$1$1\\10\\546\\9":I
    nop

    .end local v0    # "$i$a$-ifBlank-RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3$1$1\\10\\546\\9":I
    const-string v0, "Sin detalle especificado"

    :cond_7
    move-object/from16 v22, v0

    check-cast v22, Ljava/lang/String;

    .line 547
    const/16 v0, 0xd

    invoke-static {v0}, Landroidx/compose/ui/unit/TextUnitKt;->getSp(I)J

    move-result-wide v26

    .line 548
    sget-object v0, Landroidx/compose/ui/text/font/FontStyle;->Companion:Landroidx/compose/ui/text/font/FontStyle$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/text/font/FontStyle$Companion;->getItalic-_-LCdwA()I

    move-result v0

    invoke-static {v0}, Landroidx/compose/ui/text/font/FontStyle;->box-impl(I)Landroidx/compose/ui/text/font/FontStyle;

    move-result-object v28

    .line 545
    const/16 v23, 0x0

    const-wide/16 v24, 0x0

    const/16 v29, 0x0

    const/16 v30, 0x0

    const-wide/16 v31, 0x0

    const/16 v33, 0x0

    const/16 v34, 0x0

    const-wide/16 v35, 0x0

    const/16 v37, 0x0

    const/16 v38, 0x0

    const/16 v39, 0x0

    const/16 v40, 0x0

    const/16 v41, 0x0

    const/16 v42, 0x0

    const/16 v44, 0xc00

    const/16 v45, 0x0

    const v46, 0x1ffe6

    invoke-static/range {v22 .. v46}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 539
    invoke-static/range {v43 .. v43}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 550
    nop

    .line 1379
    .end local v3    # "$changed\\9":I
    .end local v20    # "$i$a$-Column-RepartidorScreenKt$IncidentDeliveryOrdersView$2$1$2$1$1$3$1\\9\\1379\\0":I
    .end local v43    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .end local v47    # "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/ColumnScope;
    invoke-static/range {v48 .. v48}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1378
    .end local v17    # "$changed\\8":I
    .end local v48    # "$composer\\8":Landroidx/compose/runtime/Composer;
    .end local v49    # "$i$a$-Layout-ColumnKt$Column$1\\8\\1378\\2":I
    nop

    .line 1381
    invoke-interface {v6}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1357
    invoke-static {v6}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1382
    nop

    .line 1351
    .end local v16    # "$i$f$ReusableComposeNode\\4\\1355":I
    .end local v18    # "$changed\\4":I
    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-static {v6}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1383
    nop

    .line 1344
    .end local v11    # "$changed\\3":I
    .end local v12    # "$i$f$Layout\\3\\1350":I
    .end local v13    # "compositeKeyHash\\3":I
    .end local v14    # "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    .end local v15    # "materialized\\3":Landroidx/compose/ui/Modifier;
    invoke-static {v6}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1384
    nop

    .end local v5    # "$changed\\2":I
    .end local v6    # "$composer\\2":Landroidx/compose/runtime/Composer;
    .end local v7    # "$i$f$Column\\2\\538":I
    .end local v8    # "verticalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .end local v9    # "horizontalAlignment\\2":Landroidx/compose/ui/Alignment$Horizontal;
    .end local v10    # "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v19    # "modifier\\2":Landroidx/compose/ui/Modifier;
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_8

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 551
    :cond_8
    :goto_4
    return-void
.end method
