.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/screens/TableStatusType;

.field public final synthetic f$1:Lcom/example/ui/screens/RestaurantTableInfo;

.field public final synthetic f$2:Lcom/example/data/entity/OrderEntity;

.field public final synthetic f$3:I


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/screens/TableStatusType;Lcom/example/ui/screens/RestaurantTableInfo;Lcom/example/data/entity/OrderEntity;I)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$0:Lcom/example/ui/screens/TableStatusType;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$1:Lcom/example/ui/screens/RestaurantTableInfo;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$2:Lcom/example/data/entity/OrderEntity;

    iput p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$3:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$0:Lcom/example/ui/screens/TableStatusType;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$1:Lcom/example/ui/screens/RestaurantTableInfo;

    iget-object v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$2:Lcom/example/data/entity/OrderEntity;

    iget v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda103;->f$3:I

    move-object v4, p1

    check-cast v4, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v5, p2

    check-cast v5, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v6

    invoke-static/range {v0 .. v6}, Lcom/example/ui/screens/MeseroScreenKt;->TableVisualCard$lambda$305(Lcom/example/ui/screens/TableStatusType;Lcom/example/ui/screens/RestaurantTableInfo;Lcom/example/data/entity/OrderEntity;ILandroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
