.class public final synthetic Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:D

.field public final synthetic f$1:Landroidx/compose/runtime/State;

.field public final synthetic f$2:D

.field public final synthetic f$3:Landroidx/compose/runtime/State;

.field public final synthetic f$4:Landroidx/compose/runtime/State;


# direct methods
.method public synthetic constructor <init>(DLandroidx/compose/runtime/State;DLandroidx/compose/runtime/State;Landroidx/compose/runtime/State;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-wide p1, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$0:D

    iput-object p3, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$1:Landroidx/compose/runtime/State;

    iput-wide p4, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$2:D

    iput-object p6, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$3:Landroidx/compose/runtime/State;

    iput-object p7, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$4:Landroidx/compose/runtime/State;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 10

    .line 0
    iget-wide v0, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$0:D

    iget-object v2, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$1:Landroidx/compose/runtime/State;

    iget-wide v3, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$2:D

    iget-object v5, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$3:Landroidx/compose/runtime/State;

    iget-object v6, p0, Lcom/example/ui/screens/GerenteScreenKt$$ExternalSyntheticLambda89;->f$4:Landroidx/compose/runtime/State;

    move-object v7, p1

    check-cast v7, Landroidx/compose/foundation/lazy/LazyItemScope;

    move-object v8, p2

    check-cast v8, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v9

    invoke-static/range {v0 .. v9}, Lcom/example/ui/screens/GerenteScreenKt;->GerenteScreen$lambda$173$lambda$172$lambda$155$lambda$154$lambda$138(DLandroidx/compose/runtime/State;DLandroidx/compose/runtime/State;Landroidx/compose/runtime/State;Landroidx/compose/foundation/lazy/LazyItemScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
