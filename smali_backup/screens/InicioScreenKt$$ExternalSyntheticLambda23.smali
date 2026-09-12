.class public final synthetic Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda23;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:Landroid/content/Context;

.field public final synthetic f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda23;->f$0:Landroid/content/Context;

    iput-object p2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda23;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 2

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda23;->f$0:Landroid/content/Context;

    iget-object v1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda23;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    check-cast p1, Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;

    check-cast p2, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result p3

    invoke-static {v0, v1, p1, p2, p3}, Lcom/example/ui/screens/InicioScreenKt;->InicioScreen$lambda$71$lambda$70$lambda$66$lambda$65$lambda$64$lambda$51(Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
