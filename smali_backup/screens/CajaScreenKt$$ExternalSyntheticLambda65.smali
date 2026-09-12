.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda65;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:D


# direct methods
.method public synthetic constructor <init>(DD)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda65;->f$0:D

    iput-wide p3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda65;->f$1:D

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda65;->f$0:D

    iget-wide v2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda65;->f$1:D

    move-object v4, p1

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/CajaScreenKt;->PaymentProcessingDialog$lambda$228$lambda$227$lambda$222(DDLandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
