.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:D

.field public final synthetic f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$3:Lcom/example/data/entity/OrderEntity;

.field public final synthetic f$4:Landroidx/compose/runtime/State;

.field public final synthetic f$5:Landroidx/compose/runtime/State;

.field public final synthetic f$6:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$7:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$8:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$9:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(DDLcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$0:D

    iput-wide p3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$1:D

    iput-object p5, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p6, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$3:Lcom/example/data/entity/OrderEntity;

    iput-object p7, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$4:Landroidx/compose/runtime/State;

    iput-object p8, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$5:Landroidx/compose/runtime/State;

    iput-object p9, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$6:Landroidx/compose/runtime/MutableState;

    iput-object p10, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$7:Landroidx/compose/runtime/MutableState;

    iput-object p11, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$8:Landroidx/compose/runtime/MutableState;

    iput-object p12, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$9:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 14

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$0:D

    iget-wide v2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$1:D

    iget-object v4, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$2:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v5, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$3:Lcom/example/data/entity/OrderEntity;

    iget-object v6, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$4:Landroidx/compose/runtime/State;

    iget-object v7, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$5:Landroidx/compose/runtime/State;

    iget-object v8, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$6:Landroidx/compose/runtime/MutableState;

    iget-object v9, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$7:Landroidx/compose/runtime/MutableState;

    iget-object v10, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$8:Landroidx/compose/runtime/MutableState;

    iget-object v11, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda27;->f$9:Landroidx/compose/runtime/MutableState;

    move-object v12, p1

    check-cast v12, Landroidx/compose/runtime/Composer;

    move-object/from16 p1, p2

    check-cast p1, Ljava/lang/Integer;

    invoke-virtual {p1}, Ljava/lang/Integer;->intValue()I

    move-result v13

    invoke-static/range {v0 .. v13}, Lcom/example/ui/screens/CajaScreenKt;->PaymentProcessingDialog$lambda$191(DDLcom/example/ui/viewmodel/RestaurantViewModel;Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
