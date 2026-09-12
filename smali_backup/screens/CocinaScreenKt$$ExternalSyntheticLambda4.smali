.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/OrderEntity;

.field public final synthetic f$1:J

.field public final synthetic f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$3:I


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/OrderEntity;JLcom/example/ui/viewmodel/RestaurantViewModel;I)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$0:Lcom/example/data/entity/OrderEntity;

    iput-wide p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$1:J

    iput-object p4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput p5, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$3:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$0:Lcom/example/data/entity/OrderEntity;

    iget-wide v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$1:J

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget v4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda4;->f$3:I

    move-object v5, p1

    check-cast v5, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v6

    invoke-static/range {v0 .. v6}, Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard$lambda$205(Lcom/example/data/entity/OrderEntity;JLcom/example/ui/viewmodel/RestaurantViewModel;ILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
