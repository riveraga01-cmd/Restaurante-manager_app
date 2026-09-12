.class public final enum Lcom/example/ui/screens/KitchenPriority;
.super Ljava/lang/Enum;
.source "CocinaScreen.kt"


# annotations
.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Enum<",
        "Lcom/example/ui/screens/KitchenPriority;",
        ">;"
    }
.end annotation

.annotation runtime Lkotlin/Metadata;
    d1 = {
        "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u000f\u0008\u0086\u0081\u0002\u0018\u00002\u0008\u0012\u0004\u0012\u00020\u00000\u0001B4\u0008\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0011\u0010\u0007\u001a\r\u0012\u0004\u0012\u00020\t0\u0008\u00a2\u0006\u0002\u0008\n\u00a2\u0006\u0004\u0008\u000b\u0010\u000cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0008\n\u0000\u001a\u0004\u0008\r\u0010\u000eR\u0013\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\u0008\u000f\u0010\u0010R\u0013\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\u0008\u0012\u0010\u0010R\u001e\u0010\u0007\u001a\r\u0012\u0004\u0012\u00020\t0\u0008\u00a2\u0006\u0002\u0008\n\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\u0008\u0013\u0010\u0014j\u0002\u0008\u0016j\u0002\u0008\u0017j\u0002\u0008\u0018\u00a8\u0006\u0019"
    }
    d2 = {
        "Lcom/example/ui/screens/KitchenPriority;",
        "",
        "title",
        "",
        "primaryColor",
        "Landroidx/compose/ui/graphics/Color;",
        "containerColor",
        "icon",
        "Lkotlin/Function0;",
        "",
        "Landroidx/compose/runtime/Composable;",
        "<init>",
        "(Ljava/lang/String;ILjava/lang/String;JJLkotlin/jvm/functions/Function2;)V",
        "getTitle",
        "()Ljava/lang/String;",
        "getPrimaryColor-0d7_KjU",
        "()J",
        "J",
        "getContainerColor-0d7_KjU",
        "getIcon",
        "()Lkotlin/jvm/functions/Function2;",
        "Lkotlin/jvm/functions/Function2;",
        "CRITICA",
        "ALTA",
        "NORMAL",
        "app"
    }
    k = 0x1
    mv = {
        0x2,
        0x2,
        0x0
    }
    xi = 0x30
.end annotation


# static fields
.field private static final synthetic $ENTRIES:Lkotlin/enums/EnumEntries;

.field private static final synthetic $VALUES:[Lcom/example/ui/screens/KitchenPriority;

.field public static final enum ALTA:Lcom/example/ui/screens/KitchenPriority;

.field public static final enum CRITICA:Lcom/example/ui/screens/KitchenPriority;

.field public static final enum NORMAL:Lcom/example/ui/screens/KitchenPriority;


# instance fields
.field private final containerColor:J

.field private final icon:Lkotlin/jvm/functions/Function2;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "Lkotlin/jvm/functions/Function2<",
            "Landroidx/compose/runtime/Composer;",
            "Ljava/lang/Integer;",
            "Lkotlin/Unit;",
            ">;"
        }
    .end annotation
.end field

.field private final primaryColor:J

.field private final title:Ljava/lang/String;


# direct methods
.method private static final synthetic $values()[Lcom/example/ui/screens/KitchenPriority;
    .locals 3

    sget-object v0, Lcom/example/ui/screens/KitchenPriority;->CRITICA:Lcom/example/ui/screens/KitchenPriority;

    sget-object v1, Lcom/example/ui/screens/KitchenPriority;->ALTA:Lcom/example/ui/screens/KitchenPriority;

    sget-object v2, Lcom/example/ui/screens/KitchenPriority;->NORMAL:Lcom/example/ui/screens/KitchenPriority;

    filled-new-array {v0, v1, v2}, [Lcom/example/ui/screens/KitchenPriority;

    move-result-object v0

    return-object v0
.end method

.method static constructor <clinit>()V
    .locals 11

    .line 66
    new-instance v0, Lcom/example/ui/screens/KitchenPriority;

    .line 67
    nop

    .line 68
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCanceladoBadge()J

    move-result-wide v4

    .line 69
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCanceladoContainer()J

    move-result-wide v6

    sget-object v1, Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;->INSTANCE:Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;

    invoke-virtual {v1}, Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;->getLambda$85472844$app()Lkotlin/jvm/functions/Function2;

    move-result-object v8

    .line 66
    const-string v1, "CRITICA"

    const/4 v2, 0x0

    const-string v3, "CR\u00cdTICA"

    invoke-direct/range {v0 .. v8}, Lcom/example/ui/screens/KitchenPriority;-><init>(Ljava/lang/String;ILjava/lang/String;JJLkotlin/jvm/functions/Function2;)V

    sput-object v0, Lcom/example/ui/screens/KitchenPriority;->CRITICA:Lcom/example/ui/screens/KitchenPriority;

    .line 72
    new-instance v1, Lcom/example/ui/screens/KitchenPriority;

    .line 73
    nop

    .line 74
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCocinaBadge()J

    move-result-wide v5

    .line 75
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCocinaContainer()J

    move-result-wide v7

    sget-object v0, Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;->INSTANCE:Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;

    invoke-virtual {v0}, Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;->getLambda$-2043811027$app()Lkotlin/jvm/functions/Function2;

    move-result-object v9

    .line 72
    const-string v2, "ALTA"

    const/4 v3, 0x1

    const-string v4, "ALTA"

    invoke-direct/range {v1 .. v9}, Lcom/example/ui/screens/KitchenPriority;-><init>(Ljava/lang/String;ILjava/lang/String;JJLkotlin/jvm/functions/Function2;)V

    sput-object v1, Lcom/example/ui/screens/KitchenPriority;->ALTA:Lcom/example/ui/screens/KitchenPriority;

    .line 78
    new-instance v2, Lcom/example/ui/screens/KitchenPriority;

    .line 79
    nop

    .line 80
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusLibreBadge()J

    move-result-wide v6

    .line 81
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusLibreContainer()J

    move-result-wide v8

    sget-object v0, Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;->INSTANCE:Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;

    invoke-virtual {v0}, Lcom/example/ui/screens/ComposableSingletons$CocinaScreenKt;->getLambda$804675132$app()Lkotlin/jvm/functions/Function2;

    move-result-object v10

    .line 78
    const-string v3, "NORMAL"

    const/4 v4, 0x2

    const-string v5, "NORMAL"

    invoke-direct/range {v2 .. v10}, Lcom/example/ui/screens/KitchenPriority;-><init>(Ljava/lang/String;ILjava/lang/String;JJLkotlin/jvm/functions/Function2;)V

    sput-object v2, Lcom/example/ui/screens/KitchenPriority;->NORMAL:Lcom/example/ui/screens/KitchenPriority;

    invoke-static {}, Lcom/example/ui/screens/KitchenPriority;->$values()[Lcom/example/ui/screens/KitchenPriority;

    move-result-object v0

    sput-object v0, Lcom/example/ui/screens/KitchenPriority;->$VALUES:[Lcom/example/ui/screens/KitchenPriority;

    sget-object v0, Lcom/example/ui/screens/KitchenPriority;->$VALUES:[Lcom/example/ui/screens/KitchenPriority;

    check-cast v0, [Ljava/lang/Enum;

    invoke-static {v0}, Lkotlin/enums/EnumEntriesKt;->enumEntries([Ljava/lang/Enum;)Lkotlin/enums/EnumEntries;

    move-result-object v0

    sput-object v0, Lcom/example/ui/screens/KitchenPriority;->$ENTRIES:Lkotlin/enums/EnumEntries;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;ILjava/lang/String;JJLkotlin/jvm/functions/Function2;)V
    .locals 0
    .param p1, "$enum$name"    # Ljava/lang/String;
    .param p2, "$enum$ordinal"    # I
    .param p3, "title"    # Ljava/lang/String;
    .param p4, "primaryColor"    # J
    .param p6, "containerColor"    # J
    .param p8, "icon"    # Lkotlin/jvm/functions/Function2;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            "JJ",
            "Lkotlin/jvm/functions/Function2<",
            "-",
            "Landroidx/compose/runtime/Composer;",
            "-",
            "Ljava/lang/Integer;",
            "Lkotlin/Unit;",
            ">;)V"
        }
    .end annotation

    .line 60
    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    .line 61
    iput-object p3, p0, Lcom/example/ui/screens/KitchenPriority;->title:Ljava/lang/String;

    .line 62
    iput-wide p4, p0, Lcom/example/ui/screens/KitchenPriority;->primaryColor:J

    .line 63
    iput-wide p6, p0, Lcom/example/ui/screens/KitchenPriority;->containerColor:J

    .line 64
    iput-object p8, p0, Lcom/example/ui/screens/KitchenPriority;->icon:Lkotlin/jvm/functions/Function2;

    .line 60
    return-void
.end method

.method public static getEntries()Lkotlin/enums/EnumEntries;
    .locals 1
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "()",
            "Lkotlin/enums/EnumEntries<",
            "Lcom/example/ui/screens/KitchenPriority;",
            ">;"
        }
    .end annotation

    sget-object v0, Lcom/example/ui/screens/KitchenPriority;->$ENTRIES:Lkotlin/enums/EnumEntries;

    return-object v0
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/example/ui/screens/KitchenPriority;
    .locals 1

    const-class v0, Lcom/example/ui/screens/KitchenPriority;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object v0

    check-cast v0, Lcom/example/ui/screens/KitchenPriority;

    return-object v0
.end method

.method public static values()[Lcom/example/ui/screens/KitchenPriority;
    .locals 1

    sget-object v0, Lcom/example/ui/screens/KitchenPriority;->$VALUES:[Lcom/example/ui/screens/KitchenPriority;

    invoke-virtual {v0}, Ljava/lang/Object;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/example/ui/screens/KitchenPriority;

    return-object v0
.end method


# virtual methods
.method public final getContainerColor-0d7_KjU()J
    .locals 2

    .line 63
    iget-wide v0, p0, Lcom/example/ui/screens/KitchenPriority;->containerColor:J

    return-wide v0
.end method

.method public final getIcon()Lkotlin/jvm/functions/Function2;
    .locals 1
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "()",
            "Lkotlin/jvm/functions/Function2<",
            "Landroidx/compose/runtime/Composer;",
            "Ljava/lang/Integer;",
            "Lkotlin/Unit;",
            ">;"
        }
    .end annotation

    .line 64
    iget-object v0, p0, Lcom/example/ui/screens/KitchenPriority;->icon:Lkotlin/jvm/functions/Function2;

    return-object v0
.end method

.method public final getPrimaryColor-0d7_KjU()J
    .locals 2

    .line 62
    iget-wide v0, p0, Lcom/example/ui/screens/KitchenPriority;->primaryColor:J

    return-wide v0
.end method

.method public final getTitle()Ljava/lang/String;
    .locals 1

    .line 61
    iget-object v0, p0, Lcom/example/ui/screens/KitchenPriority;->title:Ljava/lang/String;

    return-object v0
.end method
