.class public final synthetic Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function3;


# instance fields
.field public final synthetic f$0:Ljava/util/List;

.field public final synthetic f$1:Ljava/lang/String;

.field public final synthetic f$2:J

.field public final synthetic f$3:Landroidx/compose/ui/graphics/vector/ImageVector;

.field public final synthetic f$4:Ljava/lang/String;

.field public final synthetic f$5:Ljava/lang/String;


# direct methods
.method public synthetic constructor <init>(Ljava/util/List;Ljava/lang/String;JLandroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;Ljava/lang/String;)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$0:Ljava/util/List;

    iput-object p2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$1:Ljava/lang/String;

    iput-wide p3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$2:J

    iput-object p5, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$3:Landroidx/compose/ui/graphics/vector/ImageVector;

    iput-object p6, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$4:Ljava/lang/String;

    iput-object p7, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$5:Ljava/lang/String;

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 10

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$0:Ljava/util/List;

    iget-object v1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$1:Ljava/lang/String;

    iget-wide v2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$2:J

    iget-object v4, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$3:Landroidx/compose/ui/graphics/vector/ImageVector;

    iget-object v5, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$4:Ljava/lang/String;

    iget-object v6, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda20;->f$5:Ljava/lang/String;

    move-object v7, p1

    check-cast v7, Landroidx/compose/foundation/layout/ColumnScope;

    move-object v8, p2

    check-cast v8, Landroidx/compose/runtime/Composer;

    check-cast p3, Ljava/lang/Integer;

    invoke-virtual {p3}, Ljava/lang/Integer;->intValue()I

    move-result v9

    invoke-static/range {v0 .. v9}, Lcom/example/ui/screens/InicioScreenKt;->BentoRoleCard_3f6hBDE$lambda$108(Ljava/util/List;Ljava/lang/String;JLandroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;Ljava/lang/String;Landroidx/compose/foundation/layout/ColumnScope;Landroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
