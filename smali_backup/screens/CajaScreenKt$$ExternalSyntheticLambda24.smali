.class public final synthetic Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda24;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/data/entity/DailyCloseEntity;


# direct methods
.method public synthetic constructor <init>(Lcom/example/data/entity/DailyCloseEntity;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda24;->f$0:Lcom/example/data/entity/DailyCloseEntity;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 1

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CajaScreenKt$$ExternalSyntheticLambda24;->f$0:Lcom/example/data/entity/DailyCloseEntity;

    check-cast p1, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result p2

    invoke-static {v0, p1, p2}, Lcom/example/ui/screens/CajaScreenKt;->CajaScreen$lambda$134$lambda$133$lambda$132$lambda$131$lambda$130$lambda$129(Lcom/example/data/entity/DailyCloseEntity;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
