.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/OrderEntity;

.field public final synthetic f$1:Landroidx/compose/runtime/State;

.field public final synthetic f$2:Lkotlin/jvm/functions/Function1;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/State;Lkotlin/jvm/functions/Function1;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;->f$0:Lcom/example/data/entity/OrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;->f$1:Landroidx/compose/runtime/State;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;->f$2:Lkotlin/jvm/functions/Function1;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;->f$0:Lcom/example/data/entity/OrderEntity;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;->f$1:Landroidx/compose/runtime/State;

    iget-object v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda92;->f$2:Lkotlin/jvm/functions/Function1;

    move-object v3, p1

    check-cast v3, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v4, p2

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/MeseroScreenKt;->OrderCard$lambda$256(Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/State;Lkotlin/jvm/functions/Function1;Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
