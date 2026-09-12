.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda63;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function0;


# instance fields
.field public final synthetic f$0:Lkotlin/jvm/functions/Function1;

.field public final synthetic f$1:Lcom/example/data/entity/OrderEntity;


# direct methods
.method public synthetic constructor <init>(Lkotlin/jvm/functions/Function1;Lcom/example/data/entity/OrderEntity;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda63;->f$0:Lkotlin/jvm/functions/Function1;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda63;->f$1:Lcom/example/data/entity/OrderEntity;

    return-void
.end method


# virtual methods
.method public final invoke()Ljava/lang/Object;
    .locals 2

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda63;->f$0:Lkotlin/jvm/functions/Function1;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda63;->f$1:Lcom/example/data/entity/OrderEntity;

    invoke-static {v0, v1}, Lcom/example/ui/screens/MeseroScreenKt;->TableDetailDialog$lambda$313$lambda$312$lambda$311(Lkotlin/jvm/functions/Function1;Lcom/example/data/entity/OrderEntity;)Lkotlin/Unit;

    move-result-object v0

    return-object v0
.end method
