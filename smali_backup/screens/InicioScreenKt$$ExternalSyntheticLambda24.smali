.class public final synthetic Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:I

.field public final synthetic f$1:Landroid/content/Context;

.field public final synthetic f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(ILandroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;->f$0:I

    iput-object p2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;->f$1:Landroid/content/Context;

    iput-object p3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget v0, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;->f$0:I

    iget-object v1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;->f$1:Landroid/content/Context;

    iget-object v2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda24;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    move-object v3, p1

    check-cast v3, Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;

    move-object v4, p2

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/InicioScreenKt;->InicioScreen$lambda$71$lambda$70$lambda$66$lambda$65$lambda$64$lambda$54(ILandroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/foundation/lazy/grid/LazyGridItemScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
