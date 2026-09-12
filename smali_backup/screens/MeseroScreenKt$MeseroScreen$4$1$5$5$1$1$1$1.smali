.class final Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$5$1$1$1$1;
.super Ljava/lang/Object;
.source "MeseroScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function0;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/MeseroScreenKt;->MeseroScreen(Lcom/example/ui/viewmodel/RestaurantViewModel;Lkotlin/jvm/functions/Function0;Landroidx/compose/runtime/Composer;I)V
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
.field final synthetic $category:Ljava/lang/String;

.field final synthetic $viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;


# direct methods
.method constructor <init>(Lcom/example/ui/viewmodel/RestaurantViewModel;Ljava/lang/String;)V
    .locals 0

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$5$1$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$5$1$1$1$1;->$category:Ljava/lang/String;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke()Ljava/lang/Object;
    .locals 1

    .line 434
    invoke-virtual {p0}, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$5$1$1$1$1;->invoke()V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke()V
    .locals 2

    .line 434
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$5$1$1$1$1;->$viewModel:Lcom/example/ui/viewmodel/RestaurantViewModel;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$MeseroScreen$4$1$5$5$1$1$1$1;->$category:Ljava/lang/String;

    invoke-virtual {v0, v1}, Lcom/example/ui/viewmodel/RestaurantViewModel;->setMenuCategoryFilter(Ljava/lang/String;)V

    return-void
.end method
