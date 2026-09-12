.class final Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;
.super Ljava/lang/Object;
.source "MeseroScreen.kt"

# interfaces
.implements Lkotlin/jvm/functions/Function0;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/example/ui/screens/MeseroScreenKt;->TableMapTabContent(Ljava/util/List;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lcom/example/ui/viewmodel/RestaurantViewModel;Landroidx/compose/runtime/Composer;I)V
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
.field final synthetic $selectedTableForDetail$delegate:Landroidx/compose/runtime/MutableState;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Landroidx/compose/runtime/MutableState<",
            "Lcom/example/ui/screens/RestaurantTableInfo;",
            ">;"
        }
    .end annotation
.end field

.field final synthetic $table:Lcom/example/ui/screens/RestaurantTableInfo;


# direct methods
.method constructor <init>(Lcom/example/ui/screens/RestaurantTableInfo;Landroidx/compose/runtime/MutableState;)V
    .locals 0
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Lcom/example/ui/screens/RestaurantTableInfo;",
            "Landroidx/compose/runtime/MutableState<",
            "Lcom/example/ui/screens/RestaurantTableInfo;",
            ">;)V"
        }
    .end annotation

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;->$table:Lcom/example/ui/screens/RestaurantTableInfo;

    iput-object p2, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;->$selectedTableForDetail$delegate:Landroidx/compose/runtime/MutableState;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public bridge synthetic invoke()Ljava/lang/Object;
    .locals 1

    .line 1646
    invoke-virtual {p0}, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;->invoke()V

    sget-object v0, Lkotlin/Unit;->INSTANCE:Lkotlin/Unit;

    return-object v0
.end method

.method public final invoke()V
    .locals 2

    .line 1647
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;->$selectedTableForDetail$delegate:Landroidx/compose/runtime/MutableState;

    iget-object v1, p0, Lcom/example/ui/screens/MeseroScreenKt$TableMapTabContent$1$3$1$1$1$1;->$table:Lcom/example/ui/screens/RestaurantTableInfo;

    invoke-static {v0, v1}, Lcom/example/ui/screens/MeseroScreenKt;->access$TableMapTabContent$lambda$267(Landroidx/compose/runtime/MutableState;Lcom/example/ui/screens/RestaurantTableInfo;)V

    .line 1648
    return-void
.end method
