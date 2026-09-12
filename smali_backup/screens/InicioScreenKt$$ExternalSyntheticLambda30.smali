.class public final synthetic Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function2;


# instance fields
.field public final synthetic f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

.field public final synthetic f$1:Ljava/lang/String;

.field public final synthetic f$2:J


# direct methods
.method public synthetic constructor <init>(Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;J)V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    iput-object p1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;->f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

    iput-object p2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;->f$1:Ljava/lang/String;

    iput-wide p3, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;->f$2:J

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 6

    .line 0
    iget-object v0, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;->f$0:Landroidx/compose/ui/graphics/vector/ImageVector;

    iget-object v1, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;->f$1:Ljava/lang/String;

    iget-wide v2, p0, Lcom/example/ui/screens/InicioScreenKt$$ExternalSyntheticLambda30;->f$2:J

    move-object v4, p1

    check-cast v4, Landroidx/compose/runtime/Composer;

    check-cast p2, Ljava/lang/Integer;

    invoke-virtual {p2}, Ljava/lang/Integer;->intValue()I

    move-result v5

    invoke-static/range {v0 .. v5}, Lcom/example/ui/screens/InicioScreenKt;->BentoRoleCard_3f6hBDE$lambda$108$lambda$107$lambda$106$lambda$103$lambda$102(Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;JLandroidx/compose/runtime/Composer;I)Lkotlin/Unit;

    move-result-object p1

    return-object p1
.end method
