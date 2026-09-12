.class public final synthetic Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Ljava/util/List;

.field public final synthetic f$1:Ljava/lang/String;

.field public final synthetic f$2:D

.field public final synthetic f$3:D

.field public final synthetic f$4:D


# direct methods
.method public synthetic constructor <init>(Ljava/util/List;Ljava/lang/String;DDD)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$0:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$1:Ljava/lang/String;

    iput-wide p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$2:D

    iput-wide p5, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$3:D

    iput-wide p7, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$4:D

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 9

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$0:Ljava/util/List;

    iget-object v1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$1:Ljava/lang/String;

    iget-wide v2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$2:D

    iget-wide v4, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$3:D

    iget-wide v6, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda64;->f$4:D

    move-object v8, p1

    check-cast v8, Landroidx/compose/foundation/lazy/LazyListScope;

    invoke-static/range {v0 .. v8}, Lcom/example/ui/screens/RepartidorScreenKt;->DeliverySettlementHistoryView$lambda$123$lambda$122(Ljava/util/List;Ljava/lang/String;DDDLandroidx/compose/foundation/lazy/LazyListScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
