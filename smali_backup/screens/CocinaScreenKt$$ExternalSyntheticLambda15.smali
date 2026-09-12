.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda15;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/OrderEntity;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/OrderEntity;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda15;->f$0:Lcom/example/data/entity/OrderEntity;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda15;->f$0:Lcom/example/data/entity/OrderEntity;

    check-cast p1, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result p2

    invoke-static {v0, p1, p2}, Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard$lambda$204$lambda$203$lambda$174(Lcom/example/data/entity/OrderEntity;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
