.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$1:Landroidx/compose/runtime/State;

.field public final synthetic f$2:I

.field public final synthetic f$3:I

.field public final synthetic f$4:I


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/State;III)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$0:Landroidx/compose/runtime/MutableState;

    iput-object p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$1:Landroidx/compose/runtime/State;

    iput p3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$2:I

    iput p4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$3:I

    iput p5, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$4:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$0:Landroidx/compose/runtime/MutableState;

    iget-object v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$1:Landroidx/compose/runtime/State;

    iget v2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$2:I

    iget v3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$3:I

    iget v4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda45;->f$4:I

    move-object v5, p1

    check-cast v5, Landroidx/compose/foundation/lazy/LazyListScope;

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/CocinaScreenKt;->CocinaScreen$lambda$136$lambda$135$lambda$130$lambda$129(Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/State;IIILandroidx/compose/foundation/lazy/LazyListScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
