.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:I

.field public final synthetic f$1:D

.field public final synthetic f$2:Landroidx/compose/runtime/State;

.field public final synthetic f$3:Landroidx/compose/runtime/State;

.field public final synthetic f$4:Landroidx/compose/runtime/State;

.field public final synthetic f$5:Landroidx/compose/runtime/State;


# direct methods
.method public synthetic constructor <init>(IDLandroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$0:I

    iput-wide p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$1:D

    iput-object p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$2:Landroidx/compose/runtime/State;

    iput-object p5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$3:Landroidx/compose/runtime/State;

    iput-object p6, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$4:Landroidx/compose/runtime/State;

    iput-object p7, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$5:Landroidx/compose/runtime/State;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 9

    .line 0
    iget v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$0:I

    iget-wide v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$1:D

    iget-object v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$2:Landroidx/compose/runtime/State;

    iget-object v4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$3:Landroidx/compose/runtime/State;

    iget-object v5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$4:Landroidx/compose/runtime/State;

    iget-object v6, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda42;->f$5:Landroidx/compose/runtime/State;

    move-object v7, p1

    check-cast v7, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v8

    invoke-static/range {v0 .. v8}, Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen$lambda$199(IDLandroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
