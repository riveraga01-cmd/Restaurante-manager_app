.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/OrderEntity;

.field public final synthetic f$1:Lcom/example/ui/screens/KitchenPriority;

.field public final synthetic f$2:J

.field public final synthetic f$3:Ljava/lang/String;

.field public final synthetic f$4:Landroidx/compose/runtime/State;

.field public final synthetic f$5:Landroidx/compose/runtime/State;

.field public final synthetic f$6:Landroid/content/Context;

.field public final synthetic f$7:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/OrderEntity;Lcom/example/ui/screens/KitchenPriority;JLjava/lang/String;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$0:Lcom/example/data/entity/OrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$1:Lcom/example/ui/screens/KitchenPriority;

    iput-wide p3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$2:J

    iput-object p5, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$3:Ljava/lang/String;

    iput-object p6, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$4:Landroidx/compose/runtime/State;

    iput-object p7, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$5:Landroidx/compose/runtime/State;

    iput-object p8, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$6:Landroid/content/Context;

    iput-object p9, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$7:Lcom/example/ui/viewmodel/RestaurantViewModel;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 12

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$0:Lcom/example/data/entity/OrderEntity;

    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$1:Lcom/example/ui/screens/KitchenPriority;

    iget-wide v2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$2:J

    iget-object v4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$3:Ljava/lang/String;

    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$4:Landroidx/compose/runtime/State;

    iget-object v6, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$5:Landroidx/compose/runtime/State;

    iget-object v7, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$6:Landroid/content/Context;

    iget-object v8, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda3;->f$7:Lcom/example/ui/viewmodel/RestaurantViewModel;

    move-object v9, p1

    check-cast v9, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v10, p2

    check-cast v10, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v11

    invoke-static/range {v0 .. v11}, Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard$lambda$204(Lcom/example/data/entity/OrderEntity;Lcom/example/ui/screens/KitchenPriority;JLjava/lang/String;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroid/content/Context;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
