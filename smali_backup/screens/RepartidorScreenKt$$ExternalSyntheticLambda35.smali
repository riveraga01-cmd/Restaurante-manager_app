.class public final synthetic Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$1:Landroid/content/Context;

.field public final synthetic f$2:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;Landroid/content/Context;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;->f$1:Landroid/content/Context;

    iput-object p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;->f$2:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 3

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;->f$1:Landroid/content/Context;

    iget-object v2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda35;->f$2:Landroidx/compose/runtime/MutableState;

    check-cast p1, Ljava/lang/String;

    invoke-static {v0, v1, v2, p1}, Lcom/example/ui/screens/RepartidorScreenKt;->RepartidorScreen$lambda$80$lambda$79(Lcom/example/ui/viewmodel/RestaurantViewModel;Landroid/content/Context;Landroidx/compose/runtime/MutableState;Ljava/lang/String;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
