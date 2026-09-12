.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:D

.field public final synthetic f$2:D

.field public final synthetic f$3:D


# direct methods
.method public synthetic constructor <init>(DDDD)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$0:D

    iput-wide p3, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$1:D

    iput-wide p5, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$2:D

    iput-wide p7, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$3:D

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 11

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$0:D

    iget-wide v2, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$1:D

    iget-wide v4, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$2:D

    iget-wide v6, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda14;->f$3:D

    move-object v8, p1

    check-cast v8, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v9, p2

    check-cast v9, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v10

    invoke-static/range {v0 .. v10}, Lcom/example/ui/screens/CajaScreenKt;->CajaScreen$lambda$134$lambda$133$lambda$57$lambda$47(DDDDLandroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
