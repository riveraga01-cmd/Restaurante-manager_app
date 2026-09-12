.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/OrderItemEntity;

.field public final synthetic f$1:Landroid/content/Context;

.field public final synthetic f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/OrderItemEntity;Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;->f$0:Lcom/example/data/entity/OrderItemEntity;

    iput-object p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;->f$1:Landroid/content/Context;

    iput-object p3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 3

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;->f$0:Lcom/example/data/entity/OrderItemEntity;

    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;->f$1:Landroid/content/Context;

    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda16;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    check-cast p1, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result p2

    invoke-static {v0, v1, v2, p1, p2}, Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard$lambda$204$lambda$203$lambda$196$lambda$190$lambda$189(Lcom/example/data/entity/OrderItemEntity;Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
