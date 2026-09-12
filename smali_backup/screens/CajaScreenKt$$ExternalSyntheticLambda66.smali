.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$2:D


# direct methods
.method public synthetic constructor <init>(DLandroidx/compose/runtime/MutableState;D)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;->f$0:D

    iput-object p3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;->f$1:Landroidx/compose/runtime/MutableState;

    iput-wide p4, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;->f$2:D

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;->f$0:D

    iget-object v2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;->f$1:Landroidx/compose/runtime/MutableState;

    iget-wide v3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda66;->f$2:D

    move-object v5, p1

    check-cast v5, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v6

    invoke-static/range {v0 .. v6}, Lcom/example/ui/screens/CajaScreenKt;->CajaScreen$lambda$134$lambda$133$lambda$106$lambda$105$lambda$104$lambda$103$lambda$102$lambda$96(DLandroidx/compose/runtime/MutableState;DLandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
