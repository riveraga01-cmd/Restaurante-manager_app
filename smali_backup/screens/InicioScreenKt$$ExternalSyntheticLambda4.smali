.class public final synthetic Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

.field public final synthetic f$1:I

.field public final synthetic f$2:Ljava/lang/String;

.field public final synthetic f$3:J

.field public final synthetic f$4:J

.field public final synthetic f$5:I


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/ui/graphics/vector/ImageVector;ILjava/lang/String;JJI)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

    iput p2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$1:I

    iput-object p3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$2:Ljava/lang/String;

    iput-wide p4, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$3:J

    iput-wide p6, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$4:J

    iput p8, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$5:I

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 10

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

    iget v1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$1:I

    iget-object v2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$2:Ljava/lang/String;

    iget-wide v3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$3:J

    iget-wide v5, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$4:J

    iget v7, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda4;->f$5:I

    move-object v8, p1

    check-cast v8, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v9

    invoke-static/range {v0 .. v9}, Lcom/example/ui/screens/InicioScreenKt;->StatusSummaryChip_ZkgLGzA$lambda$97(Landroidx/compose/ui/graphics/vector/ImageVector;ILjava/lang/String;JJILandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
