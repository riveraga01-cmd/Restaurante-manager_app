.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda45;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda45;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda45;->f$0:Lcom/example/ui/viewmodel/RestaurantViewModel;

    check-cast p1, Lcom/example/data/entity/UserEntity;

    invoke-static {v0, p1}, Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen$lambda$204$lambda$203(Lcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/data/entity/UserEntity;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
