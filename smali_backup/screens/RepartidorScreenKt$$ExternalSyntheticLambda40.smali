.class public final synthetic Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda40;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# direct methods
.method public synthetic constructor <init>()V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 0

    .line 0
    check-cast p1, Lcom/example/data/entity/WebOrderEntity;

    invoke-static {p1}, Lcom/example/ui/screens/RepartidorScreenKt;->ReadyDeliveryOrdersView$lambda$90$lambda$89$lambda$86(Lcom/example/data/entity/WebOrderEntity;)Ljava/lang/Object;

    move-result-object p1

    return-object p1
.end method
