.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function1;


# instance fields
.field public final synthetic f$0:Ljava/util/List;

.field public final synthetic f$1:Ljava/util/List;

.field public final synthetic f$2:Ljava/lang/String;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Ljava/util/List;Ljava/util/List;Ljava/lang/String;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$0:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$1:Ljava/util/List;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$2:Ljava/lang/String;

    iput-object p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$3:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;)Ljava/lang/Object;
    .locals 4

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$0:Ljava/util/List;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$1:Ljava/util/List;

    iget-object v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$2:Ljava/lang/String;

    iget-object v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda95;->f$3:Landroidx/compose/runtime/MutableState;

    check-cast p1, Landroidx/compose/foundation/lazy/grid/LazyGridScope;

    invoke-static {v0, v1, v2, v3, p1}, Lcom/example/ui/screens/MeseroScreenKt;->TableMapTabContent$lambda$283$lambda$282$lambda$281(Ljava/util/List;Ljava/util/List;Ljava/lang/String;Landroidx/compose/runtime/MutableState;Landroidx/compose/foundation/lazy/grid/LazyGridScope;)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
