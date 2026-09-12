.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Lcom/example/ui/screens/KitchenPriority;

.field public final synthetic f$1:J

.field public final synthetic f$2:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Lcom/example/ui/screens/KitchenPriority;JLjava/lang/String;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;->f$0:Lcom/example/ui/screens/KitchenPriority;

    iput-wide p2, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;->f$1:J

    iput-object p4, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;->f$2:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;->f$0:Lcom/example/ui/screens/KitchenPriority;

    iget-wide v1, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;->f$1:J

    iget-object v3, p0, Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda12;->f$2:Ljava/lang/String;

    move-object v4, p1

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/CocinaScreenKt;->KitchenTicketCard$lambda$204$lambda$203$lambda$164$lambda$163(Lcom/example/ui/screens/KitchenPriority;JLjava/lang/String;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
