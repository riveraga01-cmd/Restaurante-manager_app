.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/OrderEntity;

.field public final synthetic f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$2:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$3:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$4:Lkotlin/jvm/functions/Function1;

.field public final synthetic f$5:I

.field public final synthetic f$6:I


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/OrderEntity;Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;II)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$0:Lcom/example/data/entity/OrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$2:Lkotlin/jvm/functions/Function0;

    iput-object p4, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$3:Lkotlin/jvm/functions/Function0;

    iput-object p5, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$4:Lkotlin/jvm/functions/Function1;

    iput p6, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$5:I

    iput p7, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$6:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 9

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$0:Lcom/example/data/entity/OrderEntity;

    iget-object v1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$1:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$2:Lkotlin/jvm/functions/Function0;

    iget-object v3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$3:Lkotlin/jvm/functions/Function0;

    iget-object v4, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$4:Lkotlin/jvm/functions/Function1;

    iget v5, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$5:I

    iget v6, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda31;->f$6:I

    move-object v7, p1

    check-cast v7, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v8

    invoke-static/range {v0 .. v8}, Lcom/example/ui/screens/CajaScreenKt;->PaymentProcessingDialog$lambda$229(Lcom/example/data/entity/OrderEntity;Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;IILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
