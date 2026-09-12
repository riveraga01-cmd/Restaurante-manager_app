.class public final synthetic Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda31;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function4;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$1:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda31;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p2, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda31;->f$1:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda31;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda31;->f$1:Landroidx/compose/runtime/MutableState;

    check-cast p1, Ljava/lang/Long;

    invoke-virtual {p1}, Ljava/lang/Long;->longValue()J

    move-result-wide v2

    move-object v4, p2

    check-cast v4, Ljava/lang/String;

    move-object v5, p3

    check-cast v5, Ljava/lang/String;

    move-object v6, p4

    check-cast v6, Ljava/lang/String;

    invoke-static/range {v0 .. v6}, Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen$lambda$183$lambda$182$lambda$181(Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/MutableState;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
