.class public final synthetic Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/WebOrderEntity;

.field public final synthetic f$1:Ljava/util/List;

.field public final synthetic f$2:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/WebOrderEntity;Ljava/util/List;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$0:Lcom/example/data/entity/WebOrderEntity;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$1:Ljava/util/List;

    iput-object p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$2:Landroidx/compose/runtime/MutableState;

    iput-object p4, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$3:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$0:Lcom/example/data/entity/WebOrderEntity;

    iget-object v1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$1:Ljava/util/List;

    iget-object v2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$2:Landroidx/compose/runtime/MutableState;

    iget-object v3, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda43;->f$3:Landroidx/compose/runtime/MutableState;

    move-object v4, p1

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/RepartidorScreenKt;->ReportDeliveryProblemDialog$lambda$170(Lcom/example/data/entity/WebOrderEntity;Ljava/util/List;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
