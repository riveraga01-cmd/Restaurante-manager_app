.class final Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;
.super Ljava/lang/Object;
.source "CajaScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/CajaScreenKt;->CajaScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x18
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Lkotlin/jvm/functions/Function1<",
        "Ljava/util/List<",
        "+",
        "Lcom/example/data/entity/OrderItemEntity;",
        ">;",
        "Lkotlin/Unit;",
        ">;"
    }
.end annotation

.annotation runtime Lkotlin/Metadata;
    k = 0x3
    mv = {
        0x2,
        0x2,
        0x0
    }
    xi = 0x30
.end annotation


# instance fields
.field final synthetic $cashierName$delegate:Landroidx/compose/runtime/State;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/State<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $order:Lcom/example/data/entity/OrderEntity;

.field final synthetic $ticketToPrint$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Lkotlin/Triple<",
            "Lcom/example/data/entity/OrderEntity;",
            "Ljava/util/List<",
            "Lcom/example/data/entity/OrderItemEntity;",
            ">;",
            "Lkotlin/Triple<",
            "Ljava/lang/String;",
            "Ljava/lang/Double;",
            "Ljava/lang/Double;",
            ">;>;>;"
        }
    .end annotation
.end field


# direct methods
.method constructor <init>(Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/example/data/entity/OrderEntity;",
            "Landroidx/compose/runtime/State<",
            "Ljava/lang/String;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Lkotlin/Triple<",
            "Lcom/example/data/entity/OrderEntity;",
            "Ljava/util/List<",
            "Lcom/example/data/entity/OrderItemEntity;",
            ">;",
            "Lkotlin/Triple<",
            "Ljava/lang/String;",
            "Ljava/lang/Double;",
            "Ljava/lang/Double;",
            ">;>;>;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->$order:Lcom/example/data/entity/OrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->$cashierName$delegate:Landroidx/compose/runtime/State;

    iput-object p3, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->$ticketToPrint$delegate:Landroidx/compose/runtime/MutableState;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1
    .param p1, "p1"    # Ljava/lang/Object;

    .line 178
    move-object v0, p1

    check-cast v0, Ljava/util/List;

    invoke-virtual {p0, v0}, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->invoke(Ljava/util/List;)V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke(Ljava/util/List;)V
    .locals 7
    .param p1, "items"    # Ljava/util/List;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/util/List<",
            "Lcom/example/data/entity/OrderItemEntity;",
            ">;)V"
        }
    .end annotation

    const-string v0, "items"

    invoke-static {p1, v0}, Lkotlin/jvm/internal/Intrinsics;->checkNotNullParameter(Ljava/lang/Object;Ljava/lang/String;)V

    .line 179
    iget-object v0, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->$ticketToPrint$delegate:Landroidx/compose/runtime/MutableState;

    new-instance v1, Lkotlin/Triple;

    iget-object v2, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->$order:Lcom/example/data/entity/OrderEntity;

    new-instance v3, Lkotlin/Triple;

    iget-object v4, p0, Lcom/example/ui/screens/CajaScreenKt$CajaScreen$3$1$2$1$2$2$1;->$cashierName$delegate:Landroidx/compose/runtime/State;

    invoke-static {v4}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$3(Landroidx/compose/runtime/State;)Ljava/lang/String;

    move-result-object v4

    const-wide/16 v5, 0x0

    invoke-static {v5, v6}, Ljava/lang/Double;->valueOf(D)Ljava/lang/Double;

    move-result-object v5

    invoke-direct {v3, v4, v5, v5}, Lkotlin/Triple;-><init>(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V

    invoke-direct {v1, v2, p1, v3}, Lkotlin/Triple;-><init>(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V

    invoke-static {v0, v1}, Lcom/example/ui/screens/CajaScreenKt;->access$CajaScreen$lambda$11(Landroidx/compose/runtime/MutableState;Lkotlin/Triple;)V

    .line 180
    return-void
.end method
