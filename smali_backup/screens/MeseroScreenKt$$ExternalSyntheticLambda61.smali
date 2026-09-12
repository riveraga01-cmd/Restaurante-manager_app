.class public final synthetic Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Ljava/lang/String;

.field public final synthetic f$1:I

.field public final synthetic f$2:J

.field public final synthetic f$3:J

.field public final synthetic f$4:Landroidx/compose/ui/graphics/vector/ImageVector;

.field public final synthetic f$5:Landroidx/compose/ui/Modifier;

.field public final synthetic f$6:I

.field public final synthetic f$7:I


# direct methods
.method public synthetic constructor <init>(Ljava/lang/String;IJJLandroidx/compose/ui/graphics/vector/ImageVector;Landroidx/compose/ui/Modifier;II)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$0:Ljava/lang/String;

    iput p2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$1:I

    iput-wide p3, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$2:J

    iput-wide p5, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$3:J

    iput-object p7, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$4:Landroidx/compose/ui/graphics/vector/ImageVector;

    iput-object p8, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$5:Landroidx/compose/ui/Modifier;

    iput p9, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$6:I

    iput p10, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$7:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 12

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$0:Ljava/lang/String;

    iget v1, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$1:I

    iget-wide v2, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$2:J

    iget-wide v4, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$3:J

    iget-object v6, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$4:Landroidx/compose/ui/graphics/vector/ImageVector;

    iget-object v7, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$5:Landroidx/compose/ui/Modifier;

    iget v8, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$6:I

    iget v9, p0, Lcom/example/ui/screens/MeseroScreenKt$$ExternalSyntheticLambda61;->f$7:I

    move-object v10, p1

    check-cast v10, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v11

    invoke-static/range {v0 .. v11}, Lcom/example/ui/screens/MeseroScreenKt;->MapStatCard_Y0xEhic$lambda$333(Ljava/lang/String;IJJLandroidx/compose/ui/graphics/vector/ImageVector;Landroidx/compose/ui/Modifier;IILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
