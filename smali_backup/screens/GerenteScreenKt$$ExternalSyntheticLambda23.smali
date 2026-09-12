.class public final synthetic Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda23;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function9;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda23;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 16

    .line 0
    move-object/from16 v0, p0

    iget-object v1, v0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda23;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    move-object/from16 v2, p1

    check-cast v2, Ljava/lang/Long;

    invoke-virtual {v2}, Ljava/lang/Long;->longValue()J

    move-result-wide v2

    move-object/from16 v4, p2

    check-cast v4, Ljava/lang/String;

    move-object/from16 v5, p3

    check-cast v5, Ljava/lang/Double;

    invoke-virtual {v5}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v5

    move-object/from16 v7, p4

    check-cast v7, Ljava/lang/Double;

    invoke-virtual {v7}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v7

    move-object/from16 v9, p5

    check-cast v9, Ljava/lang/Double;

    invoke-virtual {v9}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v9

    move-object/from16 v11, p6

    check-cast v11, Ljava/lang/Double;

    invoke-virtual {v11}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v11

    move-object/from16 v13, p7

    check-cast v13, Ljava/lang/String;

    move-object/from16 v14, p8

    check-cast v14, Ljava/lang/String;

    move-object/from16 v15, p9

    check-cast v15, Ljava/lang/Long;

    invoke-static/range {v1 .. v15}, Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen$lambda$173$lambda$172$lambda$110$lambda$101$lambda$100(Lcom/example/ui/viewmodel/RestaurantViewModel;JLjava/lang/String;DDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/Long;)Lkotlin/Unit;

    move-result-object v1

    return-object v1
.end method
