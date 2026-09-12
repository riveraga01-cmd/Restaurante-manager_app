.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:I

.field public final synthetic f$1:I

.field public final synthetic f$2:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$3:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$4:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$5:Landroid/content/Context;

.field public final synthetic f$6:Landroidx/compose/runtime/MutableState;

.field public final synthetic f$7:I

.field public final synthetic f$8:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(IILkotlin/jvm/functions/Function0;Landroidx/compose/runtime/MutableState;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroid/content/Context;Landroidx/compose/runtime/MutableState;ILandroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$0:I

    iput p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$1:I

    iput-object p3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$2:Lkotlin/jvm/functions/Function0;

    iput-object p4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$3:Landroidx/compose/runtime/MutableState;

    iput-object p5, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$4:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p6, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$5:Landroid/content/Context;

    iput-object p7, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$6:Landroidx/compose/runtime/MutableState;

    iput p8, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$7:I

    iput-object p9, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$8:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 11

    .line 0
    iget v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$0:I

    iget v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$1:I

    iget-object v2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$2:Lkotlin/jvm/functions/Function0;

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$3:Landroidx/compose/runtime/MutableState;

    iget-object v4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$4:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v5, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$5:Landroid/content/Context;

    iget-object v6, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$6:Landroidx/compose/runtime/MutableState;

    iget v7, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$7:I

    iget-object v8, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda27;->f$8:Landroidx/compose/runtime/MutableState;

    move-object v9, p1

    check-cast v9, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v10

    invoke-static/range {v0 .. v10}, Lcom/example/ui/screens/CocinaScreenKt;->CocinaScreen$lambda$60(IILkotlin/jvm/functions/Function0;Landroidx/compose/runtime/MutableState;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroid/content/Context;Landroidx/compose/runtime/MutableState;ILandroidx/compose/runtime/MutableState;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
