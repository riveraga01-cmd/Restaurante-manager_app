.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function0;


# instance fields
.field public final synthetic f$0:Landroid/content/Context;

.field public final synthetic f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$2:Lcom/example/data/entity/OrderEntity;


# direct methods
.method public synthetic constructor <init>(Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/data/entity/OrderEntity;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;->f$0:Landroid/content/Context;

    iput-object p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;->f$2:Lcom/example/data/entity/OrderEntity;

    return-void
.end method


# virtual methods
.method public final invoke()Ljava/lang/Object;
    .locals 3

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;->f$0:Landroid/content/Context;

    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda19;->f$2:Lcom/example/data/entity/OrderEntity;

    invoke-static {v0, v1, v2}, Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard$lambda$204$lambda$203$lambda$202$lambda$201$lambda$200(Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/data/entity/OrderEntity;)Lkotlin/Unit;

    move-result-object v0

    return-object v0
.end method
