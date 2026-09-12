.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Landroidx/compose/runtime/State;

.field public final synthetic f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$2:Landroidx/compose/runtime/State;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/runtime/State;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$0:Landroidx/compose/runtime/State;

    iput-object p2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$2:Landroidx/compose/runtime/State;

    iput-object p4, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$3:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 4

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$0:Landroidx/compose/runtime/State;

    iget-object v1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$2:Landroidx/compose/runtime/State;

    iget-object v3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda13;->f$3:Landroidx/compose/runtime/MutableState;

    check-cast p1, Landroidx/compose/foundation/lazy/LazyListScope;

    invoke-static {v0, v1, v2, v3, p1}, Lcom/example/ui/screens/CajaScreenKt;->CajaScreen$lambda$134$lambda$133$lambda$44$lambda$43(Landroidx/compose/runtime/State;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/foundation/lazy/LazyListScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
