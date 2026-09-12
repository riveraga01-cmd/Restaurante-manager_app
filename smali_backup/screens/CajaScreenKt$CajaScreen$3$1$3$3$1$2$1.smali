.class final Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1;
.super Ljava/lang/Object;
.source "CajaScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/CajaScreenKt;->CajaScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x18
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Lkotlin/jvm/functions/Function3<",
        "Landroidx/compose/foundation/layout/ColumnScope;",
        "Landroidx/compose/runtime/Composer;",
        "Ljava/lang/Integer;",
        "Lkotlin/Unit;",
        ">;"
    }
.end annotation

.annotation system Ldalvik/annotation/SourceDebugExtension;
    value = "SMAP\nCajaScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 CajaScreen.kt\ncom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1\n+ 2 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 3 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 4 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 5 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 6 Composer.kt\nandroidx/compose/runtime/Updater\n+ 7 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n*L\n1#1,1278:1\n148#2:1279\n98#3,3:1280\n101#3:1311\n105#3:1356\n78#4,6:1283\n85#4,4:1298\n89#4,2:1308\n78#4,6:1320\n85#4,4:1335\n89#4,2:1345\n93#4:1351\n93#4:1355\n368#5,9:1289\n377#5:1310\n368#5,9:1326\n377#5:1347\n378#5,2:1349\n378#5,2:1353\n4032#6,6:1302\n4032#6,6:1339\n85#7:1312\n81#7,7:1313\n88#7:1348\n92#7:1352\n*S KotlinDebug\n*F\n+ 1 CajaScreen.kt\ncom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1\n*L\n311#1:1279\n308#1:1280,3\n308#1:1311\n308#1:1356\n308#1:1283,6\n308#1:1298,4\n308#1:1308,2\n315#1:1320,6\n315#1:1335,4\n315#1:1345,2\n315#1:1351\n308#1:1355\n308#1:1289,9\n308#1:1310\n315#1:1326,9\n315#1:1347\n315#1:1349,2\n308#1:1353,2\n308#1:1302,6\n315#1:1339,6\n315#1:1312\n315#1:1313,7\n315#1:1348\n315#1:1352\n*E\n"
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
.field final synthetic $sale:Lcom/example/data/entity/SaleEntity;

.field final synthetic $timeStr:Ljava/lang/String;


# direct methods
.method constructor <init>(Lcom/example/data/entity/SaleEntity;Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1;->$sale:Lcom/example/data/entity/SaleEntity;

    iput-object p2, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1;->$timeStr:Ljava/lang/String;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 3
    .param p1, "p1"    # Ljava/lang/Object;
    .param p2, "p2"    # Ljava/lang/Object;
    .param p3, "p3"    # Ljava/lang/Object;

    .line 307
    move-object v0, p1

    check-cast v0, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v1, p2

    check-cast v1, Landroidx/compose/runtime/Composer;

    move-object v2, p3

    check-cast v2, Ljava/lang/Number;

    invoke-virtual {v2}, Ljava/lang/Number;->intValue()I

    move-result v2

    invoke-virtual {p0, v0, v1, v2}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1;->invoke(Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)V
    .locals 91
    .param p1, "$this$Card"    # Landroidx/compose/foundation/layout/ColumnScope;
    .param p2, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p3, "$changed"    # I

    move-object/from16 v0, p0

    move/from16 v1, p3

    const-string v2, "$this$Card"

    move-object/from16 v3, p1

    invoke-static {v3, v2}, Lkotlin/jvm/internal/Intrinsics;->checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v2, "C307@15441L1804:CajaScreen.kt#2thlc2"

    move-object/from16 v4, p2

    invoke-static {v4, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    and-int/lit8 v2, v1, 0x11

    const/16 v5, 0x10

    if-ne v2, v5, :cond_1

    invoke-interface {v4}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v2

    if-nez v2, :cond_0

    goto :goto_0

    .line 307
    :cond_0
    invoke-interface {v4}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    goto/16 :goto_7

    .line 0
    :cond_1
    :goto_0
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v2

    if-eqz v2, :cond_2

    const/4 v2, -0x1

    const-string v5, "com.example.ui.screens.CajaScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (CajaScreen.kt:307)"

    const v6, -0x55fee247

    invoke-static {v6, v1, v2, v5}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    .line 309
    :cond_2
    sget-object v2, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v2, Landroidx/compose/ui/Modifier;

    .line 310
    const/4 v5, 0x1

    const/4 v6, 0x0

    const/4 v7, 0x0

    invoke-static {v2, v7, v5, v6}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v2

    .line 311
    const/16 v5, 0xe

    .local v5, "$this$dp\\1":I
    const/4 v6, 0x0

    .line 1279
    .local v6, "$i$f$getDp\\1\\311":I
    int-to-float v7, v5

    invoke-static {v7}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v5

    .line 311
    .end local v5    # "$this$dp\\1":I
    .end local v6    # "$i$f$getDp\\1\\311":I
    invoke-static {v2, v5}, Landroidx/compose/foundation/layout/PaddingKt;->padding-3ABfNKs(Landroidx/compose/ui/Modifier;F)Landroidx/compose/ui/Modifier;

    move-result-object v2

    .line 312
    sget-object v5, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    invoke-virtual {v5}, Landroidx/compose/foundation/layout/Arrangement;->getSpaceBetween()Landroidx/compose/foundation/layout/Arrangement$HorizontalOrVertical;

    move-result-object v5

    check-cast v5, Landroidx/compose/foundation/layout/Arrangement$Horizontal;

    .line 313
    sget-object v6, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    invoke-virtual {v6}, Landroidx/compose/ui/Alignment$Companion;->getCenterVertically()Landroidx/compose/ui/Alignment$Vertical;

    move-result-object v6

    .line 308
    iget-object v7, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1;->$sale:Lcom/example/data/entity/SaleEntity;

    iget-object v8, v0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$3$3$1$2$1;->$timeStr:Ljava/lang/String;

    const/16 v9, 0x1b6

    .local v2, "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v6, "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .local v9, "$changed\\2":I
    move-object/from16 v10, p2

    .local v5, "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .local v10, "$composer\\2":Landroidx/compose/runtime/Composer;
    const/4 v11, 0x0

    .line 1280
    .local v11, "$i$f$Row\\2\\308":I
    const v12, 0x2952b718

    const-string v13, "CC(Row)P(2,1,3)98@4939L58,99@5002L130:Row.kt#2w3rfo"

    invoke-static {v10, v12, v13}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1281
    shr-int/lit8 v12, v9, 0x3

    and-int/lit8 v12, v12, 0xe

    shr-int/lit8 v13, v9, 0x3

    and-int/lit8 v13, v13, 0x70

    or-int/2addr v12, v13

    invoke-static {v5, v6, v10, v12}, Landroidx/compose/foundation/layout/RowKt;->rowMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Horizontal;Landroidx/compose/ui/Alignment$Vertical;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v12

    .local v12, "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v13, v9, 0x3

    and-int/lit8 v13, v13, 0x70

    .line 1282
    nop

    .local v13, "$changed\\3":I
    const/4 v14, 0x0

    .line 1283
    .local v14, "$i$f$Layout\\3\\1282":I
    const v15, -0x4ee9b9da

    const-string v0, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh"

    invoke-static {v10, v15, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1284
    const/4 v15, 0x0

    invoke-static {v10, v15}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v17

    .line 1285
    .local v17, "compositeKeyHash\\3":I
    invoke-interface {v10}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v15

    .line 1286
    .local v15, "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static {v10, v2}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v1

    .line 1288
    .local v1, "materialized\\3":Landroidx/compose/ui/Modifier;
    sget-object v19, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v19 .. v19}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v19

    move-object/from16 v20, v2

    .end local v2    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v20, "modifier\\2":Landroidx/compose/ui/Modifier;
    shl-int/lit8 v2, v13, 0x6

    and-int/lit16 v2, v2, 0x380

    or-int/lit8 v2, v2, 0x6

    .line 1287
    nop

    .local v2, "$changed\\4":I
    move-object/from16 v21, v19

    .local v21, "factory\\4":Lkotlin/jvm/functions/Function0;
    const/16 v19, 0x0

    .line 1289
    .local v19, "$i$f$ReusableComposeNode\\4\\1287":I
    move/from16 v22, v2

    .end local v2    # "$changed\\4":I
    .local v22, "$changed\\4":I
    const v2, -0x2942ffcf

    const-string v3, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp"

    invoke-static {v10, v2, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1290
    invoke-interface {v10}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v2

    instance-of v2, v2, Landroidx/compose/runtime/Applier;

    if-nez v2, :cond_3

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1291
    :cond_3
    invoke-interface {v10}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1292
    invoke-interface {v10}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v2

    if-eqz v2, :cond_4

    .line 1293
    move-object/from16 v2, v21

    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .local v2, "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v10, v2}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_1

    .line 1295
    .end local v2    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    :cond_4
    move-object/from16 v2, v21

    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v2    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v10}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1297
    :goto_1
    move-object/from16 v21, v2

    .end local v2    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-static {v10}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v2

    .local v2, "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    const/16 v24, 0x0

    .line 1298
    .local v24, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1297\\3":I
    sget-object v25, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v25 .. v25}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v4

    invoke-static {v2, v12, v4}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1299
    sget-object v4, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v4}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v4

    invoke-static {v2, v15, v4}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1301
    sget-object v4, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v4}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v4

    .local v4, "block\\6":Lkotlin/jvm/functions/Function2;
    const/16 v25, 0x0

    .line 1302
    .local v25, "$i$f$set-impl\\6\\1301":I
    move-object/from16 v26, v2

    .local v26, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    const/16 v27, 0x0

    .line 1303
    .local v27, "$i$a$-with-Updater$set$1\\7\\1302\\6":I
    invoke-interface/range {v26 .. v26}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v28

    if-nez v28, :cond_6

    move-object/from16 v28, v5

    .end local v5    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .local v28, "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    invoke-interface/range {v26 .. v26}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v5

    move-object/from16 v29, v6

    .end local v6    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .local v29, "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    invoke-static/range {v17 .. v17}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v6

    invoke-static {v5, v6}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v5

    if-nez v5, :cond_5

    goto :goto_2

    :cond_5
    move-object/from16 v6, v26

    goto :goto_3

    .end local v28    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v29    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .restart local v5    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .restart local v6    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    :cond_6
    move-object/from16 v28, v5

    move-object/from16 v29, v6

    .line 1304
    .end local v5    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v6    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .restart local v28    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .restart local v29    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    :goto_2
    invoke-static/range {v17 .. v17}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    move-object/from16 v6, v26

    .end local v26    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .local v6, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    invoke-interface {v6, v5}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1305
    invoke-static/range {v17 .. v17}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-interface {v2, v5, v4}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1307
    :goto_3
    nop

    .line 1302
    .end local v6    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .end local v27    # "$i$a$-with-Updater$set$1\\7\\1302\\6":I
    nop

    .line 1307
    nop

    .line 1308
    .end local v4    # "block\\6":Lkotlin/jvm/functions/Function2;
    .end local v25    # "$i$f$set-impl\\6\\1301":I
    sget-object v4, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v4}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v4

    invoke-static {v2, v1, v4}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1309
    nop

    .line 1297
    .end local v2    # "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    .end local v24    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1297\\3":I
    nop

    .line 1310
    shr-int/lit8 v2, v22, 0x6

    and-int/lit8 v2, v2, 0xe

    .local v2, "$changed\\8":I
    move-object v4, v10

    .local v4, "$composer\\8":Landroidx/compose/runtime/Composer;
    const/4 v5, 0x0

    .line 1311
    .local v5, "$i$a$-Layout-RowKt$Row$1\\8\\1310\\2":I
    const v6, -0x18505826

    move-object/from16 v24, v1

    .end local v1    # "materialized\\3":Landroidx/compose/ui/Modifier;
    .local v24, "materialized\\3":Landroidx/compose/ui/Modifier;
    const-string v1, "C100@5047L9:Row.kt#2w3rfo"

    invoke-static {v4, v6, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v1, Landroidx/compose/foundation/layout/RowScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/RowScopeInstance;

    shr-int/lit8 v6, v9, 0x6

    and-int/lit8 v6, v6, 0x70

    or-int/lit8 v6, v6, 0x6

    .local v6, "$changed\\9":I
    check-cast v1, Landroidx/compose/foundation/layout/RowScope;

    .local v1, "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    move-object/from16 v51, v4

    .local v51, "$composer\\9":Landroidx/compose/runtime/Composer;
    const/16 v25, 0x0

    .line 315
    .local v25, "$i$a$-Row-CajaScreenKt$CajaScreen$3$1$3$3$1$2$1$1\\9\\1311\\0":I
    move-object/from16 v26, v1

    .end local v1    # "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    .local v26, "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    const v1, 0x3cf0825b

    move/from16 v27, v2

    .end local v2    # "$changed\\8":I
    .local v27, "$changed\\8":I
    const-string v2, "C314@15912L805,327@16922L10,325@16762L441:CajaScreen.kt#2thlc2"

    move-object/from16 v55, v4

    .end local v51    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v4, "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v55, "$composer\\8":Landroidx/compose/runtime/Composer;
    invoke-static {v4, v1, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    const/4 v1, 0x0

    .local v1, "$changed\\10":I
    move-object v2, v4

    .local v2, "$composer\\10":Landroidx/compose/runtime/Composer;
    const/16 v30, 0x0

    .line 1312
    .local v30, "$i$f$Column\\10\\315":I
    move/from16 v31, v1

    .end local v1    # "$changed\\10":I
    .local v31, "$changed\\10":I
    const v1, -0x1cd0f17e

    move/from16 v56, v5

    .end local v5    # "$i$a$-Layout-RowKt$Row$1\\8\\1310\\2":I
    .local v56, "$i$a$-Layout-RowKt$Row$1\\8\\1310\\2":I
    const-string v5, "CC(Column)P(2,3,1)85@4251L61,86@4317L133:Column.kt#2w3rfo"

    invoke-static {v2, v1, v5}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1313
    sget-object v1, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v1, Landroidx/compose/ui/Modifier;

    .line 1314
    .local v1, "modifier\\10":Landroidx/compose/ui/Modifier;
    sget-object v5, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    invoke-virtual {v5}, Landroidx/compose/foundation/layout/Arrangement;->getTop()Landroidx/compose/foundation/layout/Arrangement$Vertical;

    move-result-object v5

    .line 1315
    .local v5, "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    sget-object v32, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    move/from16 v57, v6

    .end local v6    # "$changed\\9":I
    .local v57, "$changed\\9":I
    invoke-virtual/range {v32 .. v32}, Landroidx/compose/ui/Alignment$Companion;->getStart()Landroidx/compose/ui/Alignment$Horizontal;

    move-result-object v6

    .line 1318
    .local v6, "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    shr-int/lit8 v32, v31, 0x3

    and-int/lit8 v32, v32, 0xe

    shr-int/lit8 v33, v31, 0x3

    and-int/lit8 v33, v33, 0x70

    move-object/from16 v34, v7

    or-int v7, v32, v33

    invoke-static {v5, v6, v2, v7}, Landroidx/compose/foundation/layout/ColumnKt;->columnMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Vertical;Landroidx/compose/ui/Alignment$Horizontal;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v7

    .local v7, "measurePolicy\\10":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v32, v31, 0x3

    and-int/lit8 v32, v32, 0x70

    .line 1319
    nop

    .local v32, "$changed\\11":I
    const/16 v33, 0x0

    .line 1320
    .local v33, "$i$f$Layout\\11\\1319":I
    move-object/from16 v35, v5

    const v5, -0x4ee9b9da

    .end local v5    # "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .local v35, "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    invoke-static {v2, v5, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1321
    const/4 v0, 0x0

    invoke-static {v2, v0}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v0

    .line 1322
    .local v0, "compositeKeyHash\\11":I
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v5

    .line 1323
    .local v5, "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    move/from16 v16, v0

    .end local v0    # "compositeKeyHash\\11":I
    .local v16, "compositeKeyHash\\11":I
    invoke-static {v2, v1}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v0

    .line 1325
    .local v0, "materialized\\11":Landroidx/compose/ui/Modifier;
    sget-object v18, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v18 .. v18}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v18

    move-object/from16 v36, v1

    .end local v1    # "modifier\\10":Landroidx/compose/ui/Modifier;
    .local v36, "modifier\\10":Landroidx/compose/ui/Modifier;
    shl-int/lit8 v1, v32, 0x6

    and-int/lit16 v1, v1, 0x380

    or-int/lit8 v1, v1, 0x6

    .line 1324
    nop

    .local v1, "$changed\\12":I
    move-object/from16 v37, v18

    .local v37, "factory\\12":Lkotlin/jvm/functions/Function0;
    const/16 v18, 0x0

    .line 1326
    .local v18, "$i$f$ReusableComposeNode\\12\\1324":I
    move/from16 v38, v1

    const v1, -0x2942ffcf

    .end local v1    # "$changed\\12":I
    .local v38, "$changed\\12":I
    invoke-static {v2, v1, v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1327
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v1

    instance-of v1, v1, Landroidx/compose/runtime/Applier;

    if-nez v1, :cond_7

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1328
    :cond_7
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1329
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v1

    if-eqz v1, :cond_8

    .line 1330
    move-object/from16 v1, v37

    .end local v37    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .local v1, "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-interface {v2, v1}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_4

    .line 1332
    .end local v1    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .restart local v37    # "factory\\12":Lkotlin/jvm/functions/Function0;
    :cond_8
    move-object/from16 v1, v37

    .end local v37    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .restart local v1    # "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-interface {v2}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1334
    :goto_4
    invoke-static {v2}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v3

    .local v3, "$this$Layout_u24lambda_u240\\13":Landroidx/compose/runtime/Composer;
    const/16 v23, 0x0

    .line 1335
    .local v23, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\13\\1334\\11":I
    sget-object v37, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    move-object/from16 v39, v1

    .end local v1    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .local v39, "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-virtual/range {v37 .. v37}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v7, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1336
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v5, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1338
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    .local v1, "block\\14":Lkotlin/jvm/functions/Function2;
    const/16 v37, 0x0

    .line 1339
    .local v37, "$i$f$set-impl\\14\\1338":I
    move-object/from16 v40, v3

    .local v40, "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    const/16 v41, 0x0

    .line 1340
    .local v41, "$i$a$-with-Updater$set$1\\15\\1339\\14":I
    invoke-interface/range {v40 .. v40}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v42

    if-nez v42, :cond_a

    move-object/from16 v42, v2

    .end local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .local v42, "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-interface/range {v40 .. v40}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v2

    move-object/from16 v43, v5

    .end local v5    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    .local v43, "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-static {v2, v5}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v2

    if-nez v2, :cond_9

    goto :goto_5

    :cond_9
    move-object/from16 v5, v40

    goto :goto_6

    .end local v42    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .end local v43    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    .restart local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v5    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    :cond_a
    move-object/from16 v42, v2

    move-object/from16 v43, v5

    .line 1341
    .end local v2    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .end local v5    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    .restart local v42    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v43    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    :goto_5
    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    move-object/from16 v5, v40

    .end local v40    # "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    .local v5, "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    invoke-interface {v5, v2}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1342
    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v2

    invoke-interface {v3, v2, v1}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1344
    :goto_6
    nop

    .line 1339
    .end local v5    # "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    .end local v41    # "$i$a$-with-Updater$set$1\\15\\1339\\14":I
    nop

    .line 1344
    nop

    .line 1345
    .end local v1    # "block\\14":Lkotlin/jvm/functions/Function2;
    .end local v37    # "$i$f$set-impl\\14\\1338":I
    sget-object v1, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v1}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v1

    invoke-static {v3, v0, v1}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1346
    nop

    .line 1334
    .end local v3    # "$this$Layout_u24lambda_u240\\13":Landroidx/compose/runtime/Composer;
    .end local v23    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\13\\1334\\11":I
    nop

    .line 1347
    shr-int/lit8 v1, v38, 0x6

    and-int/lit8 v1, v1, 0xe

    .local v1, "$changed\\16":I
    move-object/from16 v2, v42

    .local v2, "$composer\\16":Landroidx/compose/runtime/Composer;
    const/4 v3, 0x0

    .line 1348
    .local v3, "$i$a$-Layout-ColumnKt$Column$1\\16\\1347\\10":I
    const v5, -0x16f088b9

    move-object/from16 v23, v0

    .end local v0    # "materialized\\11":Landroidx/compose/ui/Modifier;
    .local v23, "materialized\\11":Landroidx/compose/ui/Modifier;
    const-string v0, "C87@4365L9:Column.kt#2w3rfo"

    invoke-static {v2, v5, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/ColumnScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/ColumnScopeInstance;

    shr-int/lit8 v5, v31, 0x6

    and-int/lit8 v5, v5, 0x70

    or-int/lit8 v5, v5, 0x6

    .local v5, "$changed\\17":I
    check-cast v0, Landroidx/compose/foundation/layout/ColumnScope;

    .local v0, "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    move-object/from16 v79, v2

    .local v79, "$composer\\17":Landroidx/compose/runtime/Composer;
    const/16 v37, 0x0

    .line 316
    .local v37, "$i$a$-Column-CajaScreenKt$CajaScreen$3$1$3$3$1$2$1$1$1\\17\\1348\\9":I
    move-object/from16 v40, v0

    .end local v0    # "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    .local v40, "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    const v0, 0x595c9c52

    move/from16 v41, v1

    .end local v1    # "$changed\\16":I
    .local v41, "$changed\\16":I
    const-string v1, "C317@16155L10,315@15969L292,321@16497L10,322@16593L11,319@16310L361:CajaScreen.kt#2thlc2"

    move-object/from16 v44, v2

    .end local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .local v2, "$composer\\17":Landroidx/compose/runtime/Composer;
    .local v44, "$composer\\16":Landroidx/compose/runtime/Composer;
    invoke-static {v2, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 317
    invoke-virtual/range {v34 .. v34}, Lcom/example/data/entity/SaleEntity;->getOrderNumber()Ljava/lang/String;

    move-result-object v0

    invoke-virtual/range {v34 .. v34}, Lcom/example/data/entity/SaleEntity;->getPaymentMethod()Ljava/lang/String;

    move-result-object v1

    move/from16 v45, v3

    .end local v3    # "$i$a$-Layout-ColumnKt$Column$1\\16\\1347\\10":I
    .local v45, "$i$a$-Layout-ColumnKt$Column$1\\16\\1347\\10":I
    new-instance v3, Ljava/lang/StringBuilder;

    invoke-direct {v3}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v3, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v3, " \u2022 "

    invoke-virtual {v0, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v58

    .line 318
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getTitleSmall()Landroidx/compose/ui/text/TextStyle;

    move-result-object v59

    sget-object v0, Landroidx/compose/ui/text/font/FontWeight;->Companion:Landroidx/compose/ui/text/font/FontWeight$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/text/font/FontWeight$Companion;->getBold()Landroidx/compose/ui/text/font/FontWeight;

    move-result-object v64

    const v89, 0xfffffb

    const/16 v90, 0x0

    const-wide/16 v60, 0x0

    const-wide/16 v62, 0x0

    const/16 v65, 0x0

    const/16 v66, 0x0

    const/16 v67, 0x0

    const/16 v68, 0x0

    const-wide/16 v69, 0x0

    const/16 v71, 0x0

    const/16 v72, 0x0

    const/16 v73, 0x0

    const-wide/16 v74, 0x0

    const/16 v76, 0x0

    const/16 v77, 0x0

    const/16 v78, 0x0

    const/16 v79, 0x0

    const/16 v80, 0x0

    const-wide/16 v81, 0x0

    const/16 v83, 0x0

    const/16 v84, 0x0

    const/16 v85, 0x0

    const/16 v86, 0x0

    const/16 v87, 0x0

    const/16 v88, 0x0

    invoke-static/range {v59 .. v90}, Landroidx/compose/ui/text/TextStyle;->copy-p1EtxEg$default(Landroidx/compose/ui/text/TextStyle;JJLandroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontSynthesis;Landroidx/compose/ui/text/font/FontFamily;Ljava/lang/String;JLandroidx/compose/ui/text/style/BaselineShift;Landroidx/compose/ui/text/style/TextGeometricTransform;Landroidx/compose/ui/text/intl/LocaleList;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/graphics/Shadow;Landroidx/compose/ui/graphics/drawscope/DrawStyle;IIJLandroidx/compose/ui/text/style/TextIndent;Landroidx/compose/ui/text/PlatformTextStyle;Landroidx/compose/ui/text/style/LineHeightStyle;IILandroidx/compose/ui/text/style/TextMotion;ILjava/lang/Object;)Landroidx/compose/ui/text/TextStyle;

    move-result-object v78

    .line 316
    const/16 v59, 0x0

    const/16 v64, 0x0

    const-wide/16 v67, 0x0

    const/16 v69, 0x0

    const/16 v70, 0x0

    const-wide/16 v71, 0x0

    const/16 v73, 0x0

    const/16 v74, 0x0

    const/16 v75, 0x0

    const/16 v76, 0x0

    const/16 v81, 0x0

    const v82, 0xfffe

    move-object/from16 v79, v2

    .end local v2    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .restart local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-static/range {v58 .. v82}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 321
    .end local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-virtual/range {v34 .. v34}, Lcom/example/data/entity/SaleEntity;->getCashierName()Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v3, "Cajero: "

    invoke-virtual {v1, v3}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v1, " \u2022 Hora: "

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, v8}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v58

    .line 322
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getBodySmall()Landroidx/compose/ui/text/TextStyle;

    move-result-object v78

    .line 323
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/ColorScheme;->getOnSurfaceVariant-0d7_KjU()J

    move-result-wide v60

    .line 321
    nop

    .line 323
    nop

    .line 322
    nop

    .line 320
    const v82, 0xfffa

    .end local v2    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .restart local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-static/range {v58 .. v82}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 316
    invoke-static/range {v79 .. v79}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 325
    nop

    .line 1348
    .end local v5    # "$changed\\17":I
    .end local v37    # "$i$a$-Column-CajaScreenKt$CajaScreen$3$1$3$3$1$2$1$1$1\\17\\1348\\9":I
    .end local v40    # "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    .end local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-static/range {v44 .. v44}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1347
    .end local v41    # "$changed\\16":I
    .end local v44    # "$composer\\16":Landroidx/compose/runtime/Composer;
    .end local v45    # "$i$a$-Layout-ColumnKt$Column$1\\16\\1347\\10":I
    nop

    .line 1349
    invoke-interface/range {v42 .. v42}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1326
    invoke-static/range {v42 .. v42}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1350
    nop

    .line 1320
    .end local v18    # "$i$f$ReusableComposeNode\\12\\1324":I
    .end local v38    # "$changed\\12":I
    .end local v39    # "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-static/range {v42 .. v42}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1351
    nop

    .line 1312
    .end local v16    # "compositeKeyHash\\11":I
    .end local v23    # "materialized\\11":Landroidx/compose/ui/Modifier;
    .end local v32    # "$changed\\11":I
    .end local v33    # "$i$f$Layout\\11\\1319":I
    .end local v43    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static/range {v42 .. v42}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1352
    nop

    .line 327
    .end local v6    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    .end local v7    # "measurePolicy\\10":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v30    # "$i$f$Column\\10\\315":I
    .end local v31    # "$changed\\10":I
    .end local v35    # "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .end local v36    # "modifier\\10":Landroidx/compose/ui/Modifier;
    .end local v42    # "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-virtual/range {v34 .. v34}, Lcom/example/data/entity/SaleEntity;->getTotal()D

    move-result-wide v0

    invoke-static {v0, v1}, Lcom/example/ui/components/CommonComponentsKt;->formatQuetzales(D)Ljava/lang/String;

    move-result-object v30

    .line 328
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v4, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getTitleMedium()Landroidx/compose/ui/text/TextStyle;

    move-result-object v58

    .line 329
    sget-object v0, Landroidx/compose/ui/text/font/FontWeight;->Companion:Landroidx/compose/ui/text/font/FontWeight$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/text/font/FontWeight$Companion;->getBold()Landroidx/compose/ui/text/font/FontWeight;

    move-result-object v63

    .line 330
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getEmeraldSuccess()J

    move-result-wide v59

    .line 328
    nop

    .line 330
    nop

    .line 328
    nop

    .line 329
    nop

    .line 328
    const v88, 0xfffffa

    const/16 v89, 0x0

    const-wide/16 v61, 0x0

    const/16 v67, 0x0

    const-wide/16 v68, 0x0

    const/16 v71, 0x0

    const/16 v72, 0x0

    const-wide/16 v73, 0x0

    const/16 v75, 0x0

    const/16 v76, 0x0

    const/16 v78, 0x0

    const/16 v79, 0x0

    const-wide/16 v80, 0x0

    const/16 v82, 0x0

    const/16 v85, 0x0

    const/16 v87, 0x0

    invoke-static/range {v58 .. v89}, Landroidx/compose/ui/text/TextStyle;->copy-p1EtxEg$default(Landroidx/compose/ui/text/TextStyle;JJLandroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontSynthesis;Landroidx/compose/ui/text/font/FontFamily;Ljava/lang/String;JLandroidx/compose/ui/text/style/BaselineShift;Landroidx/compose/ui/text/style/TextGeometricTransform;Landroidx/compose/ui/text/intl/LocaleList;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/graphics/Shadow;Landroidx/compose/ui/graphics/drawscope/DrawStyle;IIJLandroidx/compose/ui/text/style/TextIndent;Landroidx/compose/ui/text/PlatformTextStyle;Landroidx/compose/ui/text/style/LineHeightStyle;IILandroidx/compose/ui/text/style/TextMotion;ILjava/lang/Object;)Landroidx/compose/ui/text/TextStyle;

    move-result-object v50

    .line 326
    const/16 v31, 0x0

    const-wide/16 v32, 0x0

    const-wide/16 v34, 0x0

    const/16 v36, 0x0

    const/16 v37, 0x0

    const/16 v38, 0x0

    const-wide/16 v39, 0x0

    const/16 v41, 0x0

    const/16 v42, 0x0

    const-wide/16 v43, 0x0

    const/16 v45, 0x0

    const/16 v46, 0x0

    const/16 v47, 0x0

    const/16 v48, 0x0

    const/16 v49, 0x0

    const/16 v52, 0x0

    const/16 v53, 0x0

    const v54, 0xfffe

    move-object/from16 v51, v4

    .end local v4    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .restart local v51    # "$composer\\9":Landroidx/compose/runtime/Composer;
    invoke-static/range {v30 .. v54}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 315
    invoke-static/range {v51 .. v51}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 333
    nop

    .line 1311
    .end local v25    # "$i$a$-Row-CajaScreenKt$CajaScreen$3$1$3$3$1$2$1$1\\9\\1311\\0":I
    .end local v26    # "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    .end local v51    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .end local v57    # "$changed\\9":I
    invoke-static/range {v55 .. v55}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1310
    .end local v27    # "$changed\\8":I
    .end local v55    # "$composer\\8":Landroidx/compose/runtime/Composer;
    .end local v56    # "$i$a$-Layout-RowKt$Row$1\\8\\1310\\2":I
    nop

    .line 1353
    invoke-interface {v10}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1289
    invoke-static {v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1354
    nop

    .line 1283
    .end local v19    # "$i$f$ReusableComposeNode\\4\\1287":I
    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .end local v22    # "$changed\\4":I
    invoke-static {v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1355
    nop

    .line 1280
    .end local v13    # "$changed\\3":I
    .end local v14    # "$i$f$Layout\\3\\1282":I
    .end local v15    # "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    .end local v17    # "compositeKeyHash\\3":I
    .end local v24    # "materialized\\3":Landroidx/compose/ui/Modifier;
    invoke-static {v10}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1356
    nop

    .end local v9    # "$changed\\2":I
    .end local v10    # "$composer\\2":Landroidx/compose/runtime/Composer;
    .end local v11    # "$i$f$Row\\2\\308":I
    .end local v12    # "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v20    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .end local v28    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v29    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_b

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 334
    :cond_b
    :goto_7
    return-void
.end method
