.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$1:I

.field public final synthetic f$2:I


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/runtime/MutableState;II)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;->f$0:Landroidx/compose/runtime/MutableState;

    iput p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;->f$1:I

    iput p3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;->f$2:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;->f$0:Landroidx/compose/runtime/MutableState;

    iget v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;->f$1:I

    iget v2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda43;->f$2:I

    move-object v3, p1

    check-cast v3, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v4, p2

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/CocinaScreenKt;->CocinaScreen$lambda$136$lambda$135$lambda$112(Landroidx/compose/runtime/MutableState;IILandroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
