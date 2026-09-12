.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda18;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Landroidx/compose/runtime/State;

.field public final synthetic f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/runtime/State;Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda18;->f$0:Landroidx/compose/runtime/State;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda18;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 2

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda18;->f$0:Landroidx/compose/runtime/State;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda18;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    check-cast p1, Landroidx/compose/foundation/lazy/LazyListScope;

    invoke-static {v0, v1, p1}, Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen$lambda$184$lambda$183$lambda$148$lambda$147(Landroidx/compose/runtime/State;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/foundation/lazy/LazyListScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
