.class final Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1;
.super Ljava/lang/Object;
.source "GerenteScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
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
    value = "SMAP\nGerenteScreen.kt\nKotlin\n*S Kotlin\n*F\n+ 1 GerenteScreen.kt\ncom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1\n+ 2 Dp.kt\nandroidx/compose/ui/unit/DpKt\n+ 3 Row.kt\nandroidx/compose/foundation/layout/RowKt\n+ 4 Layout.kt\nandroidx/compose/ui/layout/LayoutKt\n+ 5 Composables.kt\nandroidx/compose/runtime/ComposablesKt\n+ 6 Composer.kt\nandroidx/compose/runtime/Updater\n+ 7 Column.kt\nandroidx/compose/foundation/layout/ColumnKt\n*L\n1#1,1118:1\n148#2:1119\n98#3,3:1120\n101#3:1151\n105#3:1196\n78#4,6:1123\n85#4,4:1138\n89#4,2:1148\n78#4,6:1160\n85#4,4:1175\n89#4,2:1185\n93#4:1191\n93#4:1195\n368#5,9:1129\n377#5:1150\n368#5,9:1166\n377#5:1187\n378#5,2:1189\n378#5,2:1193\n4032#6,6:1142\n4032#6,6:1179\n85#7:1152\n81#7,7:1153\n88#7:1188\n92#7:1192\n*S KotlinDebug\n*F\n+ 1 GerenteScreen.kt\ncom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1\n*L\n569#1:1119\n566#1:1120,3\n566#1:1151\n566#1:1196\n566#1:1123,6\n566#1:1138,4\n566#1:1148,2\n573#1:1160,6\n573#1:1175,4\n573#1:1185,2\n573#1:1191\n566#1:1195\n566#1:1129,9\n566#1:1150\n573#1:1166,9\n573#1:1187\n573#1:1189,2\n566#1:1193,2\n566#1:1142,6\n573#1:1179,6\n573#1:1152\n573#1:1153,7\n573#1:1188\n573#1:1192\n*E\n"
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


# direct methods
.method constructor <init>(Lcom/example/data/entity/SaleEntity;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1;->$sale:Lcom/example/data/entity/SaleEntity;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 3
    .param p1, "p1"    # Ljava/lang/Object;
    .param p2, "p2"    # Ljava/lang/Object;
    .param p3, "p3"    # Ljava/lang/Object;

    .line 565
    move-object v0, p1

    check-cast v0, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v1, p2

    check-cast v1, Landroidx/compose/runtime/Composer;

    move-object v2, p3

    check-cast v2, Ljava/lang/Number;

    invoke-virtual {v2}, Ljava/lang/Number;->intValue()I

    move-result v2

    invoke-virtual {p0, v0, v1, v2}, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1;->invoke(Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)V
    .locals 91
    .param p1, "$this$Card"    # Landroidx/compose/foundation/layout/ColumnScope;
    .param p2, "$composer"    # Landroidx/compose/runtime/Composer;
    .param p3, "$changed"    # I

    move/from16 v0, p3

    const-string v1, "$this$Card"

    move-object/from16 v2, p1

    invoke-static {v2, v1}, Lkotlin/jvm/internal/Intrinsics;->checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V

    const-string v1, "C565@30909L1688:GerenteScreen.kt#2thlc2"

    move-object/from16 v3, p2

    invoke-static {v3, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformation(Landroidx/compose/runtime/Composer;Ljava/lang/String;)V

    and-int/lit8 v1, v0, 0x11

    const/16 v4, 0x10

    if-ne v1, v4, :cond_1

    invoke-interface {v3}, Landroidx/compose/runtime/Composer;->getSkipping()Z

    move-result v1

    if-nez v1, :cond_0

    goto :goto_0

    .line 565
    :cond_0
    invoke-interface {v3}, Landroidx/compose/runtime/Composer;->skipToGroupEnd()V

    goto/16 :goto_7

    .line 0
    :cond_1
    :goto_0
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v1

    if-eqz v1, :cond_2

    const/4 v1, -0x1

    const-string v4, "com.example.ui.screens.GerenteScreen.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous>.<anonymous> (GerenteScreen.kt:565)"

    const v5, 0x559f13fb

    invoke-static {v5, v0, v1, v4}, Landroidx/compose/runtime/ComposerKt;->traceEventStart(IIILjava/lang/String;)V

    .line 567
    :cond_2
    sget-object v1, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v1, Landroidx/compose/ui/Modifier;

    .line 568
    const/4 v4, 0x1

    const/4 v5, 0x0

    const/4 v6, 0x0

    invoke-static {v1, v6, v4, v5}, Landroidx/compose/foundation/layout/SizeKt;->fillMaxWidth$default(Landroidx/compose/ui/Modifier;FILjava/lang/Object;)Landroidx/compose/ui/Modifier;

    move-result-object v1

    .line 569
    const/16 v4, 0xc

    .local v4, "$this$dp\\1":I
    const/4 v5, 0x0

    .line 1119
    .local v5, "$i$f$getDp\\1\\569":I
    int-to-float v6, v4

    invoke-static {v6}, Landroidx/compose/ui/unit/Dp;->constructor-impl(F)F

    move-result v4

    .line 569
    .end local v4    # "$this$dp\\1":I
    .end local v5    # "$i$f$getDp\\1\\569":I
    invoke-static {v1, v4}, Landroidx/compose/foundation/layout/PaddingKt;->padding-3ABfNKs(Landroidx/compose/ui/Modifier;F)Landroidx/compose/ui/Modifier;

    move-result-object v1

    .line 570
    sget-object v4, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    invoke-virtual {v4}, Landroidx/compose/foundation/layout/Arrangement;->getSpaceBetween()Landroidx/compose/foundation/layout/Arrangement$HorizontalOrVertical;

    move-result-object v4

    check-cast v4, Landroidx/compose/foundation/layout/Arrangement$Horizontal;

    .line 571
    sget-object v5, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    invoke-virtual {v5}, Landroidx/compose/ui/Alignment$Companion;->getCenterVertically()Landroidx/compose/ui/Alignment$Vertical;

    move-result-object v5

    .line 566
    move-object/from16 v6, p0

    iget-object v7, v6, Lcom/example/ui/screens/GerenteScreenKt$GerenteScreen$3$1$10$1$9$1;->$sale:Lcom/example/data/entity/SaleEntity;

    const/16 v8, 0x1b6

    .local v1, "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v5, "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .local v8, "$changed\\2":I
    move-object/from16 v9, p2

    .local v4, "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .local v9, "$composer\\2":Landroidx/compose/runtime/Composer;
    const/4 v10, 0x0

    .line 1120
    .local v10, "$i$f$Row\\2\\566":I
    const v11, 0x2952b718

    const-string v12, "CC(Row)P(2,1,3)98@4939L58,99@5002L130:Row.kt#2w3rfo"

    invoke-static {v9, v11, v12}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1121
    shr-int/lit8 v11, v8, 0x3

    and-int/lit8 v11, v11, 0xe

    shr-int/lit8 v12, v8, 0x3

    and-int/lit8 v12, v12, 0x70

    or-int/2addr v11, v12

    invoke-static {v4, v5, v9, v11}, Landroidx/compose/foundation/layout/RowKt;->rowMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Horizontal;Landroidx/compose/ui/Alignment$Vertical;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v11

    .local v11, "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v12, v8, 0x3

    and-int/lit8 v12, v12, 0x70

    .line 1122
    nop

    .local v12, "$changed\\3":I
    const/4 v13, 0x0

    .line 1123
    .local v13, "$i$f$Layout\\3\\1122":I
    const v14, -0x4ee9b9da

    const-string v15, "CC(Layout)P(!1,2)78@3182L23,81@3333L411:Layout.kt#80mrfh"

    invoke-static {v9, v14, v15}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1124
    const/4 v14, 0x0

    invoke-static {v9, v14}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v17

    .line 1125
    .local v17, "compositeKeyHash\\3":I
    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v14

    .line 1126
    .local v14, "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    invoke-static {v9, v1}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v0

    .line 1128
    .local v0, "materialized\\3":Landroidx/compose/ui/Modifier;
    sget-object v19, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v19 .. v19}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v19

    move-object/from16 v20, v1

    .end local v1    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .local v20, "modifier\\2":Landroidx/compose/ui/Modifier;
    shl-int/lit8 v1, v12, 0x6

    and-int/lit16 v1, v1, 0x380

    or-int/lit8 v1, v1, 0x6

    .line 1127
    move-object/from16 v21, v19

    .local v1, "$changed\\4":I
    .local v21, "factory\\4":Lkotlin/jvm/functions/Function0;
    const/16 v19, 0x0

    .line 1129
    .local v19, "$i$f$ReusableComposeNode\\4\\1127":I
    move/from16 v22, v1

    .end local v1    # "$changed\\4":I
    .local v22, "$changed\\4":I
    const v1, -0x2942ffcf

    const-string v2, "CC(ReusableComposeNode)P(1,2)376@14062L9:Composables.kt#9igjgp"

    invoke-static {v9, v1, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1130
    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v1

    instance-of v1, v1, Landroidx/compose/runtime/Applier;

    if-nez v1, :cond_3

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1131
    :cond_3
    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1132
    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v1

    if-eqz v1, :cond_4

    .line 1133
    move-object/from16 v1, v21

    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .local v1, "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v9, v1}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_1

    .line 1135
    .end local v1    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    :cond_4
    move-object/from16 v1, v21

    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v1    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1137
    :goto_1
    move-object/from16 v21, v1

    .end local v1    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .restart local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    invoke-static {v9}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v1

    .local v1, "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    const/16 v24, 0x0

    .line 1138
    .local v24, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1137\\3":I
    sget-object v25, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v25 .. v25}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v3

    invoke-static {v1, v11, v3}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1139
    sget-object v3, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v3}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v3

    invoke-static {v1, v14, v3}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1141
    sget-object v3, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v3}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v3

    .local v3, "block\\6":Lkotlin/jvm/functions/Function2;
    const/16 v25, 0x0

    .line 1142
    .local v25, "$i$f$set-impl\\6\\1141":I
    move-object/from16 v26, v1

    .local v26, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    const/16 v27, 0x0

    .line 1143
    .local v27, "$i$a$-with-Updater$set$1\\7\\1142\\6":I
    invoke-interface/range {v26 .. v26}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v28

    if-nez v28, :cond_6

    move-object/from16 v28, v4

    .end local v4    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .local v28, "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    invoke-interface/range {v26 .. v26}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v4

    move-object/from16 v29, v5

    .end local v5    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .local v29, "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    invoke-static/range {v17 .. v17}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-static {v4, v5}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v4

    if-nez v4, :cond_5

    goto :goto_2

    :cond_5
    move-object/from16 v5, v26

    goto :goto_3

    .end local v28    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v29    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .restart local v4    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .restart local v5    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    :cond_6
    move-object/from16 v28, v4

    move-object/from16 v29, v5

    .line 1144
    .end local v4    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v5    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    .restart local v28    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .restart local v29    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    :goto_2
    invoke-static/range {v17 .. v17}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    move-object/from16 v5, v26

    .end local v26    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .local v5, "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    invoke-interface {v5, v4}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1145
    invoke-static/range {v17 .. v17}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v4

    invoke-interface {v1, v4, v3}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1147
    :goto_3
    nop

    .line 1142
    .end local v5    # "$this$set_impl_u24lambda_u240\\6":Landroidx/compose/runtime/Composer;
    .end local v27    # "$i$a$-with-Updater$set$1\\7\\1142\\6":I
    nop

    .line 1147
    nop

    .line 1148
    .end local v3    # "block\\6":Lkotlin/jvm/functions/Function2;
    .end local v25    # "$i$f$set-impl\\6\\1141":I
    sget-object v3, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v3}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v3

    invoke-static {v1, v0, v3}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1149
    nop

    .line 1137
    .end local v1    # "$this$Layout_u24lambda_u240\\5":Landroidx/compose/runtime/Composer;
    .end local v24    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\5\\1137\\3":I
    nop

    .line 1150
    shr-int/lit8 v1, v22, 0x6

    and-int/lit8 v1, v1, 0xe

    .local v1, "$changed\\8":I
    move-object v3, v9

    .local v3, "$composer\\8":Landroidx/compose/runtime/Composer;
    const/4 v4, 0x0

    .line 1151
    .local v4, "$i$a$-Layout-RowKt$Row$1\\8\\1150\\2":I
    const v5, -0x18505826

    move-object/from16 v24, v0

    .end local v0    # "materialized\\3":Landroidx/compose/ui/Modifier;
    .local v24, "materialized\\3":Landroidx/compose/ui/Modifier;
    const-string v0, "C100@5047L9:Row.kt#2w3rfo"

    invoke-static {v3, v5, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/RowScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/RowScopeInstance;

    shr-int/lit8 v5, v8, 0x6

    and-int/lit8 v5, v5, 0x70

    or-int/lit8 v5, v5, 0x6

    .local v5, "$changed\\9":I
    check-cast v0, Landroidx/compose/foundation/layout/RowScope;

    .local v0, "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    move-object/from16 v51, v3

    .local v51, "$composer\\9":Landroidx/compose/runtime/Composer;
    const/16 v25, 0x0

    .line 573
    .local v25, "$i$a$-Row-GerenteScreenKt$GerenteScreen$3$1$10$1$9$1$1\\9\\1151\\0":I
    move-object/from16 v26, v0

    .end local v0    # "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    .local v26, "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    const v0, 0x4d626d2d    # 2.3742536E8f

    move/from16 v27, v1

    .end local v1    # "$changed\\8":I
    .local v27, "$changed\\8":I
    const-string v1, "C572@31352L748,586@32294L10,584@32142L417:GerenteScreen.kt#2thlc2"

    move-object/from16 v55, v3

    .end local v51    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v3, "$composer\\9":Landroidx/compose/runtime/Composer;
    .local v55, "$composer\\8":Landroidx/compose/runtime/Composer;
    invoke-static {v3, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    const/4 v0, 0x0

    .local v0, "$changed\\10":I
    move-object v1, v3

    .local v1, "$composer\\10":Landroidx/compose/runtime/Composer;
    const/16 v30, 0x0

    .line 1152
    .local v30, "$i$f$Column\\10\\573":I
    move/from16 v31, v0

    .end local v0    # "$changed\\10":I
    .local v31, "$changed\\10":I
    const v0, -0x1cd0f17e

    move/from16 v56, v4

    .end local v4    # "$i$a$-Layout-RowKt$Row$1\\8\\1150\\2":I
    .local v56, "$i$a$-Layout-RowKt$Row$1\\8\\1150\\2":I
    const-string v4, "CC(Column)P(2,3,1)85@4251L61,86@4317L133:Column.kt#2w3rfo"

    invoke-static {v1, v0, v4}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1153
    sget-object v0, Landroidx/compose/ui/Modifier;->Companion:Landroidx/compose/ui/Modifier$Companion;

    check-cast v0, Landroidx/compose/ui/Modifier;

    .line 1154
    .local v0, "modifier\\10":Landroidx/compose/ui/Modifier;
    sget-object v4, Landroidx/compose/foundation/layout/Arrangement;->INSTANCE:Landroidx/compose/foundation/layout/Arrangement;

    invoke-virtual {v4}, Landroidx/compose/foundation/layout/Arrangement;->getTop()Landroidx/compose/foundation/layout/Arrangement$Vertical;

    move-result-object v4

    .line 1155
    .local v4, "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    sget-object v32, Landroidx/compose/ui/Alignment;->Companion:Landroidx/compose/ui/Alignment$Companion;

    move/from16 v57, v5

    .end local v5    # "$changed\\9":I
    .local v57, "$changed\\9":I
    invoke-virtual/range {v32 .. v32}, Landroidx/compose/ui/Alignment$Companion;->getStart()Landroidx/compose/ui/Alignment$Horizontal;

    move-result-object v5

    .line 1158
    .local v5, "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    shr-int/lit8 v32, v31, 0x3

    and-int/lit8 v32, v32, 0xe

    shr-int/lit8 v33, v31, 0x3

    and-int/lit8 v33, v33, 0x70

    or-int v6, v32, v33

    invoke-static {v4, v5, v1, v6}, Landroidx/compose/foundation/layout/ColumnKt;->columnMeasurePolicy(Landroidx/compose/foundation/layout/Arrangement$Vertical;Landroidx/compose/ui/Alignment$Horizontal;Landroidx/compose/runtime/Composer;I)Landroidx/compose/ui/layout/MeasurePolicy;

    move-result-object v6

    .local v6, "measurePolicy\\10":Landroidx/compose/ui/layout/MeasurePolicy;
    shl-int/lit8 v32, v31, 0x3

    and-int/lit8 v32, v32, 0x70

    .line 1159
    nop

    .local v32, "$changed\\11":I
    const/16 v33, 0x0

    .line 1160
    .local v33, "$i$f$Layout\\11\\1159":I
    move-object/from16 v34, v4

    const v4, -0x4ee9b9da

    .end local v4    # "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .local v34, "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    invoke-static {v1, v4, v15}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1161
    const/4 v4, 0x0

    invoke-static {v1, v4}, Landroidx/compose/runtime/ComposablesKt;->getCurrentCompositeKeyHash(Landroidx/compose/runtime/Composer;I)I

    move-result v4

    .line 1162
    .local v4, "compositeKeyHash\\11":I
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->getCurrentCompositionLocalMap()Landroidx/compose/runtime/CompositionLocalMap;

    move-result-object v15

    .line 1163
    .local v15, "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    move/from16 v16, v4

    .end local v4    # "compositeKeyHash\\11":I
    .local v16, "compositeKeyHash\\11":I
    invoke-static {v1, v0}, Landroidx/compose/ui/ComposedModifierKt;->materializeModifier(Landroidx/compose/runtime/Composer;Landroidx/compose/ui/Modifier;)Landroidx/compose/ui/Modifier;

    move-result-object v4

    .line 1165
    .local v4, "materialized\\11":Landroidx/compose/ui/Modifier;
    sget-object v18, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual/range {v18 .. v18}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getConstructor()Lkotlin/jvm/functions/Function0;

    move-result-object v18

    move-object/from16 v35, v0

    .end local v0    # "modifier\\10":Landroidx/compose/ui/Modifier;
    .local v35, "modifier\\10":Landroidx/compose/ui/Modifier;
    shl-int/lit8 v0, v32, 0x6

    and-int/lit16 v0, v0, 0x380

    or-int/lit8 v0, v0, 0x6

    .line 1164
    nop

    .local v0, "$changed\\12":I
    move-object/from16 v36, v18

    .local v36, "factory\\12":Lkotlin/jvm/functions/Function0;
    const/16 v18, 0x0

    .line 1166
    .local v18, "$i$f$ReusableComposeNode\\12\\1164":I
    move/from16 v37, v0

    const v0, -0x2942ffcf

    .end local v0    # "$changed\\12":I
    .local v37, "$changed\\12":I
    invoke-static {v1, v0, v2}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 1167
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->getApplier()Landroidx/compose/runtime/Applier;

    move-result-object v0

    instance-of v0, v0, Landroidx/compose/runtime/Applier;

    if-nez v0, :cond_7

    invoke-static {}, Landroidx/compose/runtime/ComposablesKt;->invalidApplier()V

    .line 1168
    :cond_7
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->startReusableNode()V

    .line 1169
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v0

    if-eqz v0, :cond_8

    .line 1170
    move-object/from16 v0, v36

    .end local v36    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .local v0, "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-interface {v1, v0}, Landroidx/compose/runtime/Composer;->createNode(Lkotlin/jvm/functions/Function0;)V

    goto :goto_4

    .line 1172
    .end local v0    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .restart local v36    # "factory\\12":Lkotlin/jvm/functions/Function0;
    :cond_8
    move-object/from16 v0, v36

    .end local v36    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .restart local v0    # "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-interface {v1}, Landroidx/compose/runtime/Composer;->useNode()V

    .line 1174
    :goto_4
    invoke-static {v1}, Landroidx/compose/runtime/Updater;->constructor-impl(Landroidx/compose/runtime/Composer;)Landroidx/compose/runtime/Composer;

    move-result-object v2

    .local v2, "$this$Layout_u24lambda_u240\\13":Landroidx/compose/runtime/Composer;
    const/16 v23, 0x0

    .line 1175
    .local v23, "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\13\\1174\\11":I
    sget-object v36, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    move-object/from16 v38, v0

    .end local v0    # "factory\\12":Lkotlin/jvm/functions/Function0;
    .local v38, "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-virtual/range {v36 .. v36}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetMeasurePolicy()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    invoke-static {v2, v6, v0}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1176
    sget-object v0, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetResolvedCompositionLocals()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    invoke-static {v2, v15, v0}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1178
    sget-object v0, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetCompositeKeyHash()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    .local v0, "block\\14":Lkotlin/jvm/functions/Function2;
    const/16 v36, 0x0

    .line 1179
    .local v36, "$i$f$set-impl\\14\\1178":I
    move-object/from16 v39, v2

    .local v39, "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    const/16 v40, 0x0

    .line 1180
    .local v40, "$i$a$-with-Updater$set$1\\15\\1179\\14":I
    invoke-interface/range {v39 .. v39}, Landroidx/compose/runtime/Composer;->getInserting()Z

    move-result v41

    if-nez v41, :cond_a

    move-object/from16 v41, v1

    .end local v1    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .local v41, "$composer\\10":Landroidx/compose/runtime/Composer;
    invoke-interface/range {v39 .. v39}, Landroidx/compose/runtime/Composer;->rememberedValue()Ljava/lang/Object;

    move-result-object v1

    move-object/from16 v42, v5

    .end local v5    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    .local v42, "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v5

    invoke-static {v1, v5}, Lkotlin/jvm/internal/Intrinsics;->areEqual(Ljava/lang/Object;Ljava/lang/Object;)Z

    move-result v1

    if-nez v1, :cond_9

    goto :goto_5

    :cond_9
    move-object/from16 v5, v39

    goto :goto_6

    .end local v41    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .end local v42    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    .restart local v1    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v5    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    :cond_a
    move-object/from16 v41, v1

    move-object/from16 v42, v5

    .line 1181
    .end local v1    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .end local v5    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    .restart local v41    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .restart local v42    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    :goto_5
    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    move-object/from16 v5, v39

    .end local v39    # "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    .local v5, "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    invoke-interface {v5, v1}, Landroidx/compose/runtime/Composer;->updateRememberedValue(Ljava/lang/Object;)V

    .line 1182
    invoke-static/range {v16 .. v16}, Ljava/lang/Integer;->valueOf(I)Ljava/lang/Integer;

    move-result-object v1

    invoke-interface {v2, v1, v0}, Landroidx/compose/runtime/Composer;->apply(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1184
    :goto_6
    nop

    .line 1179
    .end local v5    # "$this$set_impl_u24lambda_u240\\14":Landroidx/compose/runtime/Composer;
    .end local v40    # "$i$a$-with-Updater$set$1\\15\\1179\\14":I
    nop

    .line 1184
    nop

    .line 1185
    .end local v0    # "block\\14":Lkotlin/jvm/functions/Function2;
    .end local v36    # "$i$f$set-impl\\14\\1178":I
    sget-object v0, Landroidx/compose/ui/node/ComposeUiNode;->Companion:Landroidx/compose/ui/node/ComposeUiNode$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/node/ComposeUiNode$Companion;->getSetModifier()Lkotlin/jvm/functions/Function2;

    move-result-object v0

    invoke-static {v2, v4, v0}, Landroidx/compose/runtime/Updater;->set-impl(Landroidx/compose/runtime/Composer;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)V

    .line 1186
    nop

    .line 1174
    .end local v2    # "$this$Layout_u24lambda_u240\\13":Landroidx/compose/runtime/Composer;
    .end local v23    # "$i$a$-ReusableComposeNode-LayoutKt$Layout$1\\13\\1174\\11":I
    nop

    .line 1187
    shr-int/lit8 v0, v37, 0x6

    and-int/lit8 v0, v0, 0xe

    .local v0, "$changed\\16":I
    move-object/from16 v1, v41

    .local v1, "$composer\\16":Landroidx/compose/runtime/Composer;
    const/4 v2, 0x0

    .line 1188
    .local v2, "$i$a$-Layout-ColumnKt$Column$1\\16\\1187\\10":I
    const v5, -0x16f088b9

    move/from16 v23, v0

    .end local v0    # "$changed\\16":I
    .local v23, "$changed\\16":I
    const-string v0, "C87@4365L9:Column.kt#2w3rfo"

    invoke-static {v1, v5, v0}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    sget-object v0, Landroidx/compose/foundation/layout/ColumnScopeInstance;->INSTANCE:Landroidx/compose/foundation/layout/ColumnScopeInstance;

    shr-int/lit8 v5, v31, 0x6

    and-int/lit8 v5, v5, 0x70

    or-int/lit8 v5, v5, 0x6

    .local v5, "$changed\\17":I
    check-cast v0, Landroidx/compose/foundation/layout/ColumnScope;

    .local v0, "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    move-object/from16 v79, v1

    .local v79, "$composer\\17":Landroidx/compose/runtime/Composer;
    const/16 v36, 0x0

    .line 574
    .local v36, "$i$a$-Column-GerenteScreenKt$GerenteScreen$3$1$10$1$9$1$1$1\\17\\1188\\9":I
    move-object/from16 v39, v0

    .end local v0    # "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    .local v39, "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    const v0, 0x1bd91261

    move-object/from16 v40, v1

    .end local v1    # "$composer\\16":Landroidx/compose/runtime/Composer;
    .local v40, "$composer\\16":Landroidx/compose/runtime/Composer;
    const-string v1, "C575@31583L10,573@31405L280,579@31892L10,580@31984L11,577@31730L328:GerenteScreen.kt#2thlc2"

    move/from16 v43, v2

    move-object/from16 v2, v79

    .end local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .local v2, "$composer\\17":Landroidx/compose/runtime/Composer;
    .local v43, "$i$a$-Layout-ColumnKt$Column$1\\16\\1187\\10":I
    invoke-static {v2, v0, v1}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerStart(Landroidx/compose/runtime/Composer;ILjava/lang/String;)V

    .line 575
    invoke-virtual {v7}, Lcom/example/data/entity/SaleEntity;->getOrderNumber()Ljava/lang/String;

    move-result-object v0

    invoke-virtual {v7}, Lcom/example/data/entity/SaleEntity;->getPaymentMethod()Ljava/lang/String;

    move-result-object v1

    move-object/from16 v44, v4

    .end local v4    # "materialized\\11":Landroidx/compose/ui/Modifier;
    .local v44, "materialized\\11":Landroidx/compose/ui/Modifier;
    new-instance v4, Ljava/lang/StringBuilder;

    invoke-direct {v4}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v4, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    const-string v4, " \u2022 "

    invoke-virtual {v0, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0, v1}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v58

    .line 576
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getBodyMedium()Landroidx/compose/ui/text/TextStyle;

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

    .line 574
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

    .line 579
    .end local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .restart local v2    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-virtual {v7}, Lcom/example/data/entity/SaleEntity;->getCashierName()Ljava/lang/String;

    move-result-object v0

    new-instance v1, Ljava/lang/StringBuilder;

    invoke-direct {v1}, Ljava/lang/StringBuilder;-><init>()V

    const-string v4, "Cajero: "

    invoke-virtual {v1, v4}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v1

    invoke-virtual {v1, v0}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v0

    invoke-virtual {v0}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v58

    .line 580
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getBodySmall()Landroidx/compose/ui/text/TextStyle;

    move-result-object v78

    .line 581
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v2, v1}, Landroidx/compose/material3/MaterialTheme;->getColorScheme(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/ColorScheme;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/ColorScheme;->getOnSurfaceVariant-0d7_KjU()J

    move-result-wide v60

    .line 579
    nop

    .line 581
    nop

    .line 580
    nop

    .line 578
    const v82, 0xfffa

    .end local v2    # "$composer\\17":Landroidx/compose/runtime/Composer;
    .restart local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-static/range {v58 .. v82}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 574
    invoke-static/range {v79 .. v79}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 583
    nop

    .line 1188
    .end local v5    # "$changed\\17":I
    .end local v36    # "$i$a$-Column-GerenteScreenKt$GerenteScreen$3$1$10$1$9$1$1$1\\17\\1188\\9":I
    .end local v39    # "$this$invoke_u24lambda_u241_u24lambda_u240\\17":Landroidx/compose/foundation/layout/ColumnScope;
    .end local v79    # "$composer\\17":Landroidx/compose/runtime/Composer;
    invoke-static/range {v40 .. v40}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1187
    .end local v23    # "$changed\\16":I
    .end local v40    # "$composer\\16":Landroidx/compose/runtime/Composer;
    .end local v43    # "$i$a$-Layout-ColumnKt$Column$1\\16\\1187\\10":I
    nop

    .line 1189
    invoke-interface/range {v41 .. v41}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1166
    invoke-static/range {v41 .. v41}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1190
    nop

    .line 1160
    .end local v18    # "$i$f$ReusableComposeNode\\12\\1164":I
    .end local v37    # "$changed\\12":I
    .end local v38    # "factory\\12":Lkotlin/jvm/functions/Function0;
    invoke-static/range {v41 .. v41}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1191
    nop

    .line 1152
    .end local v15    # "localMap\\11":Landroidx/compose/runtime/CompositionLocalMap;
    .end local v16    # "compositeKeyHash\\11":I
    .end local v32    # "$changed\\11":I
    .end local v33    # "$i$f$Layout\\11\\1159":I
    .end local v44    # "materialized\\11":Landroidx/compose/ui/Modifier;
    invoke-static/range {v41 .. v41}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1192
    nop

    .line 586
    .end local v6    # "measurePolicy\\10":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v30    # "$i$f$Column\\10\\573":I
    .end local v31    # "$changed\\10":I
    .end local v34    # "verticalArrangement\\10":Landroidx/compose/foundation/layout/Arrangement$Vertical;
    .end local v35    # "modifier\\10":Landroidx/compose/ui/Modifier;
    .end local v41    # "$composer\\10":Landroidx/compose/runtime/Composer;
    .end local v42    # "horizontalAlignment\\10":Landroidx/compose/ui/Alignment$Horizontal;
    invoke-virtual {v7}, Lcom/example/data/entity/SaleEntity;->getTotal()D

    move-result-wide v0

    invoke-static {v0, v1}, Lcom/example/ui/components/CommonComponentsKt;->formatQuetzales(D)Ljava/lang/String;

    move-result-object v30

    .line 587
    sget-object v0, Landroidx/compose/material3/MaterialTheme;->INSTANCE:Landroidx/compose/material3/MaterialTheme;

    sget v1, Landroidx/compose/material3/MaterialTheme;->$stable:I

    invoke-virtual {v0, v3, v1}, Landroidx/compose/material3/MaterialTheme;->getTypography(Landroidx/compose/runtime/Composer;I)Landroidx/compose/material3/Typography;

    move-result-object v0

    invoke-virtual {v0}, Landroidx/compose/material3/Typography;->getTitleMedium()Landroidx/compose/ui/text/TextStyle;

    move-result-object v58

    .line 588
    sget-object v0, Landroidx/compose/ui/text/font/FontWeight;->Companion:Landroidx/compose/ui/text/font/FontWeight$Companion;

    invoke-virtual {v0}, Landroidx/compose/ui/text/font/FontWeight$Companion;->getBold()Landroidx/compose/ui/text/font/FontWeight;

    move-result-object v63

    .line 589
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getEmeraldSuccess()J

    move-result-wide v59

    .line 587
    nop

    .line 589
    nop

    .line 587
    nop

    .line 588
    nop

    .line 587
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

    .line 585
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

    move-object/from16 v51, v3

    .end local v3    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .restart local v51    # "$composer\\9":Landroidx/compose/runtime/Composer;
    invoke-static/range {v30 .. v54}, Landroidx/compose/material3/TextKt;->Text--4IGK_g(Ljava/lang/String;Landroidx/compose/ui/Modifier;JJLandroidx/compose/ui/text/font/FontStyle;Landroidx/compose/ui/text/font/FontWeight;Landroidx/compose/ui/text/font/FontFamily;JLandroidx/compose/ui/text/style/TextDecoration;Landroidx/compose/ui/text/style/TextAlign;JIZIILkotlin/jvm/functions/Function1;Landroidx/compose/ui/text/TextStyle;Landroidx/compose/runtime/Composer;III)V

    .line 573
    .end local v51    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .restart local v3    # "$composer\\9":Landroidx/compose/runtime/Composer;
    invoke-static {v3}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 592
    nop

    .line 1151
    .end local v3    # "$composer\\9":Landroidx/compose/runtime/Composer;
    .end local v25    # "$i$a$-Row-GerenteScreenKt$GerenteScreen$3$1$10$1$9$1$1\\9\\1151\\0":I
    .end local v26    # "$this$invoke_u24lambda_u241\\9":Landroidx/compose/foundation/layout/RowScope;
    .end local v57    # "$changed\\9":I
    invoke-static/range {v55 .. v55}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1150
    .end local v27    # "$changed\\8":I
    .end local v55    # "$composer\\8":Landroidx/compose/runtime/Composer;
    .end local v56    # "$i$a$-Layout-RowKt$Row$1\\8\\1150\\2":I
    nop

    .line 1193
    invoke-interface {v9}, Landroidx/compose/runtime/Composer;->endNode()V

    .line 1129
    invoke-static {v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1194
    nop

    .line 1123
    .end local v19    # "$i$f$ReusableComposeNode\\4\\1127":I
    .end local v21    # "factory\\4":Lkotlin/jvm/functions/Function0;
    .end local v22    # "$changed\\4":I
    invoke-static {v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1195
    nop

    .line 1120
    .end local v12    # "$changed\\3":I
    .end local v13    # "$i$f$Layout\\3\\1122":I
    .end local v14    # "localMap\\3":Landroidx/compose/runtime/CompositionLocalMap;
    .end local v17    # "compositeKeyHash\\3":I
    .end local v24    # "materialized\\3":Landroidx/compose/ui/Modifier;
    invoke-static {v9}, Landroidx/compose/runtime/ComposerKt;->sourceInformationMarkerEnd(Landroidx/compose/runtime/Composer;)V

    .line 1196
    nop

    .end local v8    # "$changed\\2":I
    .end local v9    # "$composer\\2":Landroidx/compose/runtime/Composer;
    .end local v10    # "$i$f$Row\\2\\566":I
    .end local v11    # "measurePolicy\\2":Landroidx/compose/ui/layout/MeasurePolicy;
    .end local v20    # "modifier\\2":Landroidx/compose/ui/Modifier;
    .end local v28    # "horizontalArrangement\\2":Landroidx/compose/foundation/layout/Arrangement$Horizontal;
    .end local v29    # "verticalAlignment\\2":Landroidx/compose/ui/Alignment$Vertical;
    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->isTraceInProgress()Z

    move-result v0

    if-eqz v0, :cond_b

    invoke-static {}, Landroidx/compose/runtime/ComposerKt;->traceEventEnd()V

    .line 593
    :cond_b
    :goto_7
    return-void
.end method
