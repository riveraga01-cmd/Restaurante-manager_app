.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/MenuItemEntity;

.field public final synthetic f$1:Landroidx/compose/runtime/MutableIntState;

.field public final synthetic f$2:Ljava/util/List;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/MenuItemEntity;Landroidx/compose/runtime/MutableIntState;Ljava/util/List;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$0:Lcom/example/data/entity/MenuItemEntity;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$1:Landroidx/compose/runtime/MutableIntState;

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$2:Ljava/util/List;

    iput-object p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$3:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$0:Lcom/example/data/entity/MenuItemEntity;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$1:Landroidx/compose/runtime/MutableIntState;

    iget-object v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$2:Ljava/util/List;

    iget-object v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda108;->f$3:Landroidx/compose/runtime/MutableState;

    move-object v4, p1

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/MeseroScreenKt;->AddItemCustomizationDialog$lambda$241(Lcom/example/data/entity/MenuItemEntity;Landroidx/compose/runtime/MutableIntState;Ljava/util/List;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
