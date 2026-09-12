.class public final synthetic Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Ljava/lang/String;

.field public final synthetic f$1:Lkotlin/jvm/functions/Function0;

.field public final synthetic f$2:Landroidx/compose/runtime/State;

.field public final synthetic f$3:Lcom/example/ui/viewmodel/RestaurantViewModel;

.field public final synthetic f$4:Landroidx/compose/runtime/State;

.field public final synthetic f$5:Landroidx/compose/runtime/MutableState;


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/State;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$0:Ljava/lang/String;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$1:Lkotlin/jvm/functions/Function0;

    iput-object p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$2:Landroidx/compose/runtime/State;

    iput-object p4, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$3:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p5, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$4:Landroidx/compose/runtime/State;

    iput-object p6, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$5:Landroidx/compose/runtime/MutableState;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 8

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$0:Ljava/lang/String;

    iget-object v1, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$1:Lkotlin/jvm/functions/Function0;

    iget-object v2, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$2:Landroidx/compose/runtime/State;

    iget-object v3, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$3:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v4, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$4:Landroidx/compose/runtime/State;

    iget-object v5, p0, Lcom/example/ui/screens/RepartidorScreenKt$$ExternalSyntheticLambda28;->f$5:Landroidx/compose/runtime/MutableState;

    move-object v6, p1

    check-cast v6, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v7

    invoke-static/range {v0 .. v7}, Lcom/example/ui/screens/RepartidorScreenKt;->RepartidorScreen$lambda$27(Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/State;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/State;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
