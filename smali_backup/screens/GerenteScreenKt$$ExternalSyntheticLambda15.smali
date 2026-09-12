.class public final synthetic Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda15;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function8;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda15;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 11

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda15;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    check-cast p1, Ljava/lang/Long;

    invoke-virtual {p1}, Ljava/lang/Long;->longValue()J

    move-result-wide v1

    move-object v3, p2

    check-cast v3, Ljava/lang/String;

    move-object v4, p3

    check-cast v4, Ljava/lang/String;

    check-cast p4, Ljava/lang/Double;

    invoke-virtual {p4}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v5

    move-object/from16 v7, p5

    check-cast v7, Ljava/lang/String;

    move-object/from16 v8, p6

    check-cast v8, Ljava/lang/String;

    move-object/from16 p1, p7

    check-cast p1, Ljava/lang/Boolean;

    invoke-virtual {p1}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v9

    move-object/from16 p1, p8

    check-cast p1, Ljava/lang/Boolean;

    invoke-virtual {p1}, Ljava/lang/Boolean;->booleanValue()Z

    move-result v10

    invoke-static/range {v0 .. v10}, Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen$lambda$173$lambda$172$lambda$87$lambda$86(Lcom/example/ui/viewmodel/RestaurantViewModel;JLjava/lang/String;Ljava/lang/String;DLjava/lang/String;Ljava/lang/String;ZZ)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
