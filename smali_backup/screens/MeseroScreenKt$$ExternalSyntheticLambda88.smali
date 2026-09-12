.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda88;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Z

.field public final synthetic f$1:Z


# direct methods
.method public synthetic constructor <init>(ZZ)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-boolean p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda88;->f$0:Z

    iput-boolean p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda88;->f$1:Z

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 2

    .line 0
    iget-boolean v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda88;->f$0:Z

    iget-boolean v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda88;->f$1:Z

    check-cast p1, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result p2

    invoke-static {v0, v1, p1, p2}, Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen$lambda$184$lambda$183$lambda$172$lambda$171$lambda$160(ZZLandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
