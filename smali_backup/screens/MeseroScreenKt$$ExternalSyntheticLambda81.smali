.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:I

.field public final synthetic f$2:Z

.field public final synthetic f$3:I

.field public final synthetic f$4:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(DIZILandroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$0:D

    iput p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$1:I

    iput-boolean p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$2:Z

    iput p5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$3:I

    iput-object p6, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$4:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 8

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$0:D

    iget v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$1:I

    iget-boolean v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$2:Z

    iget v4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$3:I

    iget-object v5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda81;->f$4:Landroidx/compose/runtime/MutableState;

    move-object v6, p1

    check-cast v6, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v7

    invoke-static/range {v0 .. v7}, Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen$lambda$69$lambda$68(DIZILandroidx/compose/runtime/MutableState;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
