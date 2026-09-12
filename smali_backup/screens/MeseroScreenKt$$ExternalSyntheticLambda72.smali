.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:I

.field public final synthetic f$2:Landroidx/compose/runtime/State;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(DILandroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$0:D

    iput p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$1:I

    iput-object p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$2:Landroidx/compose/runtime/State;

    iput-object p5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$3:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 8

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$0:D

    iget v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$1:I

    iget-object v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$2:Landroidx/compose/runtime/State;

    iget-object v4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda72;->f$3:Landroidx/compose/runtime/MutableState;

    move-object v5, p1

    check-cast v5, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v6, p2

    check-cast v6, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v7

    invoke-static/range {v0 .. v7}, Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen$lambda$125$lambda$124$lambda$113$lambda$94(DILandroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
