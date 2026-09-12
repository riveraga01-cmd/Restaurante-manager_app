.class public final synthetic Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Landroid/content/Context;

.field public final synthetic f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$2:I

.field public final synthetic f$3:I

.field public final synthetic f$4:Landroidx/compose/runtime/State;

.field public final synthetic f$5:Landroidx/compose/runtime/State;

.field public final synthetic f$6:Landroidx/compose/runtime/State;


# direct methods
.method public synthetic constructor <init>(Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;IILandroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$0:Landroid/content/Context;

    iput-object p2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput p3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$2:I

    iput p4, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$3:I

    iput-object p5, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$4:Landroidx/compose/runtime/State;

    iput-object p6, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$5:Landroidx/compose/runtime/State;

    iput-object p7, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$6:Landroidx/compose/runtime/State;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 8

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$0:Landroid/content/Context;

    iget-object v1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget v2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$2:I

    iget v3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$3:I

    iget-object v4, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$4:Landroidx/compose/runtime/State;

    iget-object v5, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$5:Landroidx/compose/runtime/State;

    iget-object v6, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda18;->f$6:Landroidx/compose/runtime/State;

    move-object v7, p1

    check-cast v7, Landroidx/compose/foundation/lazy/grid/LazyGridScope;

    invoke-static/range {v0 .. v7}, Lcom/example/ui/screens/InicioScreenKt;->InicioScreen$lambda$71$lambda$70$lambda$66$lambda$65$lambda$64(Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;IILandroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/foundation/lazy/grid/LazyGridScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
