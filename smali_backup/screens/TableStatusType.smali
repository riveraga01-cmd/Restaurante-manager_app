.class public final enum Lcom/example/ui/screens/TableStatusType;
.super Ljava/lang/Enum;
.source "MeseroScreen.kt"


# annotations
.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Enum<",
        "Lcom/example/ui/screens/TableStatusType;",
        ">;"
    }
.end annotation

.annotation runtime Lkotlin/Metadata;
    d1 = {
        "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0012\u0008\u0086\u0081\u0002\u0018\u00002\u0008\u0012\u0004\u0012\u00020\u00000\u0001B9\u0008\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\u0008\u001a\u00020\u0005\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0004\u0008\u000b\u0010\u000cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0008\n\u0000\u001a\u0004\u0008\r\u0010\u000eR\u0013\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\u0008\u000f\u0010\u0010R\u0013\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\u0008\u0012\u0010\u0010R\u0013\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\u0008\u0013\u0010\u0010R\u0013\u0010\u0008\u001a\u00020\u0005\u00a2\u0006\n\n\u0002\u0010\u0011\u001a\u0004\u0008\u0014\u0010\u0010R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\u0008\n\u0000\u001a\u0004\u0008\u0015\u0010\u0016j\u0002\u0008\u0017j\u0002\u0008\u0018j\u0002\u0008\u0019j\u0002\u0008\u001aj\u0002\u0008\u001b\u00a8\u0006\u001c"
    }
    d2 = {
        "Lcom/example/ui/screens/TableStatusType;",
        "",
        "label",
        "",
        "badgeColor",
        "Landroidx/compose/ui/graphics/Color;",
        "containerColor",
        "borderColor",
        "contentColor",
        "icon",
        "Landroidx/compose/ui/graphics/vector/ImageVector;",
        "<init>",
        "(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V",
        "getLabel",
        "()Ljava/lang/String;",
        "getBadgeColor-0d7_KjU",
        "()J",
        "J",
        "getContainerColor-0d7_KjU",
        "getBorderColor-0d7_KjU",
        "getContentColor-0d7_KjU",
        "getIcon",
        "()Landroidx/compose/ui/graphics/vector/ImageVector;",
        "LIBRE",
        "RESERVADA",
        "ESPERANDO_COCINA",
        "LISTA_PARA_CUENTA",
        "OCUPADA",
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

.field private static final synthetic $VALUES:[Lcom/example/ui/screens/TableStatusType;

.field public static final enum ESPERANDO_COCINA:Lcom/example/ui/screens/TableStatusType;

.field public static final enum LIBRE:Lcom/example/ui/screens/TableStatusType;

.field public static final enum LISTA_PARA_CUENTA:Lcom/example/ui/screens/TableStatusType;

.field public static final enum OCUPADA:Lcom/example/ui/screens/TableStatusType;

.field public static final enum RESERVADA:Lcom/example/ui/screens/TableStatusType;


# instance fields
.field private final badgeColor:J

.field private final borderColor:J

.field private final containerColor:J

.field private final contentColor:J

.field private final icon:Landroidx/compose/ui/graphics/vector/ImageVector;

.field private final label:Ljava/lang/String;


# direct methods
.method private static final synthetic $values()[Lcom/example/ui/screens/TableStatusType;
    .locals 5

    sget-object v0, Lcom/example/ui/screens/TableStatusType;->LIBRE:Lcom/example/ui/screens/TableStatusType;

    sget-object v1, Lcom/example/ui/screens/TableStatusType;->RESERVADA:Lcom/example/ui/screens/TableStatusType;

    sget-object v2, Lcom/example/ui/screens/TableStatusType;->ESPERANDO_COCINA:Lcom/example/ui/screens/TableStatusType;

    sget-object v3, Lcom/example/ui/screens/TableStatusType;->LISTA_PARA_CUENTA:Lcom/example/ui/screens/TableStatusType;

    sget-object v4, Lcom/example/ui/screens/TableStatusType;->OCUPADA:Lcom/example/ui/screens/TableStatusType;

    filled-new-array {v0, v1, v2, v3, v4}, [Lcom/example/ui/screens/TableStatusType;

    move-result-object v0

    return-object v0
.end method

.method static constructor <clinit>()V
    .locals 17

    .line 1458
    new-instance v0, Lcom/example/ui/screens/TableStatusType;

    .line 1459
    nop

    .line 1460
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusLibreBadge()J

    move-result-wide v4

    .line 1461
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusLibreContainer()J

    move-result-wide v6

    .line 1462
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusLibreBorder()J

    move-result-wide v8

    .line 1463
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusLibreText()J

    move-result-wide v10

    .line 1464
    sget-object v1, Landroidx/compose/material/icons/Icons;->INSTANCE:Landroidx/compose/material/icons/Icons;

    invoke-virtual {v1}, Landroidx/compose/material/icons/Icons;->getDefault()Landroidx/compose/material/icons/Icons$Filled;

    move-result-object v1

    invoke-static {v1}, Landroidx/compose/material/icons/filled/CheckCircleKt;->getCheckCircle(Landroidx/compose/material/icons/Icons$Filled;)Landroidx/compose/ui/graphics/vector/ImageVector;

    move-result-object v12

    .line 1458
    const-string v1, "LIBRE"

    const/4 v2, 0x0

    const-string v3, "Libre"

    invoke-direct/range {v0 .. v12}, Lcom/example/ui/screens/TableStatusType;-><init>(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V

    sput-object v0, Lcom/example/ui/screens/TableStatusType;->LIBRE:Lcom/example/ui/screens/TableStatusType;

    .line 1466
    new-instance v1, Lcom/example/ui/screens/TableStatusType;

    .line 1467
    nop

    .line 1468
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusReservadaBadge()J

    move-result-wide v5

    .line 1469
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusReservadaContainer()J

    move-result-wide v7

    .line 1470
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusReservadaBorder()J

    move-result-wide v9

    .line 1471
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusReservadaText()J

    move-result-wide v11

    .line 1472
    sget-object v0, Landroidx/compose/material/icons/Icons;->INSTANCE:Landroidx/compose/material/icons/Icons;

    invoke-virtual {v0}, Landroidx/compose/material/icons/Icons;->getDefault()Landroidx/compose/material/icons/Icons$Filled;

    move-result-object v0

    invoke-static {v0}, Landroidx/compose/material/icons/filled/EventSeatKt;->getEventSeat(Landroidx/compose/material/icons/Icons$Filled;)Landroidx/compose/ui/graphics/vector/ImageVector;

    move-result-object v13

    .line 1466
    const-string v2, "RESERVADA"

    const/4 v3, 0x1

    const-string v4, "Reservada \ud83d\udcc5"

    invoke-direct/range {v1 .. v13}, Lcom/example/ui/screens/TableStatusType;-><init>(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V

    sput-object v1, Lcom/example/ui/screens/TableStatusType;->RESERVADA:Lcom/example/ui/screens/TableStatusType;

    .line 1474
    new-instance v2, Lcom/example/ui/screens/TableStatusType;

    .line 1475
    nop

    .line 1476
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCocinaBadge()J

    move-result-wide v6

    .line 1477
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCocinaContainer()J

    move-result-wide v8

    .line 1478
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCocinaBorder()J

    move-result-wide v10

    .line 1479
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCocinaText()J

    move-result-wide v12

    .line 1480
    sget-object v0, Landroidx/compose/material/icons/Icons;->INSTANCE:Landroidx/compose/material/icons/Icons;

    invoke-virtual {v0}, Landroidx/compose/material/icons/Icons;->getDefault()Landroidx/compose/material/icons/Icons$Filled;

    move-result-object v0

    invoke-static {v0}, Landroidx/compose/material/icons/filled/SoupKitchenKt;->getSoupKitchen(Landroidx/compose/material/icons/Icons$Filled;)Landroidx/compose/ui/graphics/vector/ImageVector;

    move-result-object v14

    .line 1474
    const-string v3, "ESPERANDO_COCINA"

    const/4 v4, 0x2

    const-string v5, "En cocina \u23f3"

    invoke-direct/range {v2 .. v14}, Lcom/example/ui/screens/TableStatusType;-><init>(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V

    sput-object v2, Lcom/example/ui/screens/TableStatusType;->ESPERANDO_COCINA:Lcom/example/ui/screens/TableStatusType;

    .line 1482
    new-instance v3, Lcom/example/ui/screens/TableStatusType;

    .line 1483
    nop

    .line 1484
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCobrarBadge()J

    move-result-wide v7

    .line 1485
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCobrarContainer()J

    move-result-wide v9

    .line 1486
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCobrarBorder()J

    move-result-wide v11

    .line 1487
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusCobrarText()J

    move-result-wide v13

    .line 1488
    sget-object v0, Landroidx/compose/material/icons/Icons;->INSTANCE:Landroidx/compose/material/icons/Icons;

    invoke-virtual {v0}, Landroidx/compose/material/icons/Icons;->getDefault()Landroidx/compose/material/icons/Icons$Filled;

    move-result-object v0

    invoke-static {v0}, Landroidx/compose/material/icons/filled/ReceiptLongKt;->getReceiptLong(Landroidx/compose/material/icons/Icons$Filled;)Landroidx/compose/ui/graphics/vector/ImageVector;

    move-result-object v15

    .line 1482
    const-string v4, "LISTA_PARA_CUENTA"

    const/4 v5, 0x3

    const-string v6, "Lista para cobrar \ud83d\udcb5"

    invoke-direct/range {v3 .. v15}, Lcom/example/ui/screens/TableStatusType;-><init>(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V

    sput-object v3, Lcom/example/ui/screens/TableStatusType;->LISTA_PARA_CUENTA:Lcom/example/ui/screens/TableStatusType;

    .line 1490
    new-instance v4, Lcom/example/ui/screens/TableStatusType;

    .line 1491
    nop

    .line 1492
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusOcupadaBadge()J

    move-result-wide v8

    .line 1493
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusOcupadaContainer()J

    move-result-wide v10

    .line 1494
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusOcupadaBorder()J

    move-result-wide v12

    .line 1495
    invoke-static {}, Lcom/example/ui/theme/ColorKt;->getStatusOcupadaText()J

    move-result-wide v14

    .line 1496
    sget-object v0, Landroidx/compose/material/icons/Icons;->INSTANCE:Landroidx/compose/material/icons/Icons;

    invoke-virtual {v0}, Landroidx/compose/material/icons/Icons;->getDefault()Landroidx/compose/material/icons/Icons$Filled;

    move-result-object v0

    invoke-static {v0}, Landroidx/compose/material/icons/filled/PeopleKt;->getPeople(Landroidx/compose/material/icons/Icons$Filled;)Landroidx/compose/ui/graphics/vector/ImageVector;

    move-result-object v16

    .line 1490
    const-string v5, "OCUPADA"

    const/4 v6, 0x4

    const-string v7, "Ocupada \ud83d\udc65"

    invoke-direct/range {v4 .. v16}, Lcom/example/ui/screens/TableStatusType;-><init>(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V

    sput-object v4, Lcom/example/ui/screens/TableStatusType;->OCUPADA:Lcom/example/ui/screens/TableStatusType;

    invoke-static {}, Lcom/example/ui/screens/TableStatusType;->$values()[Lcom/example/ui/screens/TableStatusType;

    move-result-object v0

    sput-object v0, Lcom/example/ui/screens/TableStatusType;->$VALUES:[Lcom/example/ui/screens/TableStatusType;

    sget-object v0, Lcom/example/ui/screens/TableStatusType;->$VALUES:[Lcom/example/ui/screens/TableStatusType;

    check-cast v0, [Ljava/lang/Enum;

    invoke-static {v0}, Lkotlin/enums/EnumEntriesKt;->enumEntries([Ljava/lang/Enum;)Lkotlin/enums/EnumEntries;

    move-result-object v0

    sput-object v0, Lcom/example/ui/screens/TableStatusType;->$ENTRIES:Lkotlin/enums/EnumEntries;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;ILjava/lang/String;JJJJLandroidx/compose/ui/graphics/vector/ImageVector;)V
    .locals 0
    .param p1, "$enum$name"    # Ljava/lang/String;
    .param p2, "$enum$ordinal"    # I
    .param p3, "label"    # Ljava/lang/String;
    .param p4, "badgeColor"    # J
    .param p6, "containerColor"    # J
    .param p8, "borderColor"    # J
    .param p10, "contentColor"    # J
    .param p12, "icon"    # Landroidx/compose/ui/graphics/vector/ImageVector;
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "(",
            "Ljava/lang/String;",
            "JJJJ",
            "Landroidx/compose/ui/graphics/vector/ImageVector;",
            ")V"
        }
    .end annotation

    .line 1450
    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    .line 1451
    iput-object p3, p0, Lcom/example/ui/screens/TableStatusType;->label:Ljava/lang/String;

    .line 1452
    iput-wide p4, p0, Lcom/example/ui/screens/TableStatusType;->badgeColor:J

    .line 1453
    iput-wide p6, p0, Lcom/example/ui/screens/TableStatusType;->containerColor:J

    .line 1454
    iput-wide p8, p0, Lcom/example/ui/screens/TableStatusType;->borderColor:J

    .line 1455
    iput-wide p10, p0, Lcom/example/ui/screens/TableStatusType;->contentColor:J

    .line 1456
    iput-object p12, p0, Lcom/example/ui/screens/TableStatusType;->icon:Landroidx/compose/ui/graphics/vector/ImageVector;

    .line 1450
    return-void
.end method

.method public static getEntries()Lkotlin/enums/EnumEntries;
    .locals 1
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "()",
            "Lkotlin/enums/EnumEntries<",
            "Lcom/example/ui/screens/TableStatusType;",
            ">;"
        }
    .end annotation

    sget-object v0, Lcom/example/ui/screens/TableStatusType;->$ENTRIES:Lkotlin/enums/EnumEntries;

    return-object v0
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/example/ui/screens/TableStatusType;
    .locals 1

    const-class v0, Lcom/example/ui/screens/TableStatusType;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object v0

    check-cast v0, Lcom/example/ui/screens/TableStatusType;

    return-object v0
.end method

.method public static values()[Lcom/example/ui/screens/TableStatusType;
    .locals 1

    sget-object v0, Lcom/example/ui/screens/TableStatusType;->$VALUES:[Lcom/example/ui/screens/TableStatusType;

    invoke-virtual {v0}, Ljava/lang/Object;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/example/ui/screens/TableStatusType;

    return-object v0
.end method


# virtual methods
.method public final getBadgeColor-0d7_KjU()J
    .locals 2

    .line 1452
    iget-wide v0, p0, Lcom/example/ui/screens/TableStatusType;->badgeColor:J

    return-wide v0
.end method

.method public final getBorderColor-0d7_KjU()J
    .locals 2

    .line 1454
    iget-wide v0, p0, Lcom/example/ui/screens/TableStatusType;->borderColor:J

    return-wide v0
.end method

.method public final getContainerColor-0d7_KjU()J
    .locals 2

    .line 1453
    iget-wide v0, p0, Lcom/example/ui/screens/TableStatusType;->containerColor:J

    return-wide v0
.end method

.method public final getContentColor-0d7_KjU()J
    .locals 2

    .line 1455
    iget-wide v0, p0, Lcom/example/ui/screens/TableStatusType;->contentColor:J

    return-wide v0
.end method

.method public final getIcon()Landroidx/compose/ui/graphics/vector/ImageVector;
    .locals 1

    .line 1456
    iget-object v0, p0, Lcom/example/ui/screens/TableStatusType;->icon:Landroidx/compose/ui/graphics/vector/ImageVector;

    return-object v0
.end method

.method public final getLabel()Ljava/lang/String;
    .locals 1

    .line 1451
    iget-object v0, p0, Lcom/example/ui/screens/TableStatusType;->label:Ljava/lang/String;

    return-object v0
.end method
