.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

.field public final synthetic f$1:J

.field public final synthetic f$2:I

.field public final synthetic f$3:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/ui/graphics/vector/ImageVector;JILjava/lang/String;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

    iput-wide p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$1:J

    iput p4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$2:I

    iput-object p5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$3:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 7

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

    iget-wide v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$1:J

    iget v3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$2:I

    iget-object v4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda60;->f$3:Ljava/lang/String;

    move-object v5, p1

    check-cast v5, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v6

    invoke-static/range {v0 .. v6}, Lcom/example/ui/screens/MeseroScreenKt;->MapStatCard_Y0xEhic$lambda$332(Landroidx/compose/ui/graphics/vector/ImageVector;JILjava/lang/String;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
