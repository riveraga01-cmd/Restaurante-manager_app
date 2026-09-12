.class public final synthetic Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda29;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function5;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$1:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda29;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p2, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda29;->f$1:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 10

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda29;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda29;->f$1:Landroidx/compose/runtime/MutableState;

    check-cast p1, Ljava/lang/Long;

    invoke-virtual {p1}, Ljava/lang/Long;->longValue()J

    move-result-wide v2

    move-object v4, p2

    check-cast v4, Ljava/lang/String;

    check-cast p3, Ljava/lang/Double;

    invoke-virtual {p3}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v5

    check-cast p4, Ljava/lang/Double;

    invoke-virtual {p4}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v7

    move-object v9, p5

    check-cast v9, Ljava/lang/String;

    invoke-static/range {v0 .. v9}, Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen$lambda$178$lambda$177$lambda$176(Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/MutableState;JLjava/lang/String;DDLjava/lang/String;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
