.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/MenuItemEntity;

.field public final synthetic f$1:I

.field public final synthetic f$2:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$3:I


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/MenuItemEntity;ILkotlin/jvm/functions/Function0;I)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$0:Lcom/example/data/entity/MenuItemEntity;

    iput p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$1:I

    iput-object p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$2:Lkotlin/jvm/functions/Function0;

    iput p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$3:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$0:Lcom/example/data/entity/MenuItemEntity;

    iget v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$1:I

    iget-object v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$2:Lkotlin/jvm/functions/Function0;

    iget v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda9;->f$3:I

    move-object v4, p1

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/MeseroScreenKt;->MenuItemCard$lambda$217(Lcom/example/data/entity/MenuItemEntity;ILkotlin/jvm/functions/Function0;ILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
