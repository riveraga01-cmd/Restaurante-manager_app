.class final Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;
.super Ljava/lang/Object;
.source "RepartidorScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function0;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/RepartidorScreenKt;->ReportDeliveryProblemDialog(Lcom/example/data/entity/WebOrderEntity;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Landroidx/compose/runtime/Composer;I)V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x18
    name = null
.end annotation

.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Object;",
        "Lkotlin/jvm/functions/Function0<",
        "Lkotlin/Unit;",
        ">;"
    }
.end annotation

.annotation runtime Lkotlin/Metadata;
    k = 0x3
    mv = {
        0x2,
        0x2,
        0x0
    }
    xi = 0x30
.end annotation


# instance fields
.field final synthetic $incidentText$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $preset:Ljava/lang/String;

.field final synthetic $selectedPreset$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;"
        }
    .end annotation
.end field


# direct methods
.method constructor <init>(Ljava/lang/String;Landroidx/compose/runtime/MutableState;Landroidx/compose/runtime/MutableState;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;",
            "Landroidx/compose/runtime/MutableState<",
            "Ljava/lang/String;",
            ">;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$preset:Ljava/lang/String;

    iput-object p2, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$selectedPreset$delegate:Landroidx/compose/runtime/MutableState;

    iput-object p3, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$incidentText$delegate:Landroidx/compose/runtime/MutableState;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke()Ljava/lang/Object;
    .locals 1

    .line 1067
    invoke-virtual {p0}, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->invoke()V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke()V
    .locals 2

    .line 1068
    iget-object v0, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$selectedPreset$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v1, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$preset:Ljava/lang/String;

    invoke-static {v0, v1}, Lcom/example/ui/screens/RepartidorScreenKt;->access$ReportDeliveryProblemDialog$lambda$158(Landroidx/compose/runtime/MutableState;Ljava/lang/String;)V

    .line 1069
    iget-object v0, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$incidentText$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v1, p0, Lcom/example/ui/screens/RepartidorScreenKt$ReportDeliveryProblemDialog$3$1$1$1$1$1$1;->$preset:Ljava/lang/String;

    invoke-static {v0, v1}, Lcom/example/ui/screens/RepartidorScreenKt;->access$ReportDeliveryProblemDialog$lambda$155(Landroidx/compose/runtime/MutableState;Ljava/lang/String;)V

    .line 1070
    return-void
.end method
