.class public final enum Lcom/example/ui/viewmodel/ManagerTab;
.super Ljava/lang/Enum;
.source "RestaurantViewModel.kt"


# annotations
.annotation system Ldalvik/annotation/Signature;
    value = {
        "Ljava/lang/Enum<",
        "Lcom/example/ui/viewmodel/ManagerTab;",
        ">;"
    }
.end annotation

.annotation runtime Lkotlin/Metadata;
    d1 = {
        "\u0000\u000c\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0010\u0008\u0086\u0081\u0002\u0018\u00002\u0008\u0012\u0004\u0012\u00020\u00000\u0001B\t\u0008\u0002\u00a2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006j\u0002\u0008\u0007j\u0002\u0008\u0008j\u0002\u0008\tj\u0002\u0008\nj\u0002\u0008\u000bj\u0002\u0008\u000cj\u0002\u0008\rj\u0002\u0008\u000ej\u0002\u0008\u000fj\u0002\u0008\u0010\u00a8\u0006\u0011"
    }
    d2 = {
        "Lcom/example/ui/viewmodel/ManagerTab;",
        "",
        "<init>",
        "(Ljava/lang/String;I)V",
        "MENU",
        "COMPRAS",
        "INVENTARIO",
        "TEMAS_WEB",
        "FACTURACION",
        "RESUMEN_FINANCIERO",
        "VENTAS",
        "HISTORIAL",
        "EMPLEADOS",
        "AUDIT_LOGS",
        "SEGURIDAD",
        "QR_MENU",
        "CONFIGURACION",
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

.field private static final synthetic $VALUES:[Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum AUDIT_LOGS:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum COMPRAS:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum CONFIGURACION:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum EMPLEADOS:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum FACTURACION:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum HISTORIAL:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum INVENTARIO:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum MENU:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum QR_MENU:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum RESUMEN_FINANCIERO:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum SEGURIDAD:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum TEMAS_WEB:Lcom/example/ui/viewmodel/ManagerTab;
.field public static final enum HORARIOS_ESTADO:Lcom/example/ui/viewmodel/ManagerTab;

.field public static final enum VENTAS:Lcom/example/ui/viewmodel/ManagerTab;


# direct methods
.method private static final synthetic $values()[Lcom/example/ui/viewmodel/ManagerTab;
    .locals 14

    sget-object v0, Lcom/example/ui/viewmodel/ManagerTab;->MENU:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v1, Lcom/example/ui/viewmodel/ManagerTab;->COMPRAS:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v2, Lcom/example/ui/viewmodel/ManagerTab;->INVENTARIO:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v3, Lcom/example/ui/viewmodel/ManagerTab;->TEMAS_WEB:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v4, Lcom/example/ui/viewmodel/ManagerTab;->HORARIOS_ESTADO:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v5, Lcom/example/ui/viewmodel/ManagerTab;->FACTURACION:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v6, Lcom/example/ui/viewmodel/ManagerTab;->RESUMEN_FINANCIERO:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v7, Lcom/example/ui/viewmodel/ManagerTab;->VENTAS:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v8, Lcom/example/ui/viewmodel/ManagerTab;->HISTORIAL:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v9, Lcom/example/ui/viewmodel/ManagerTab;->EMPLEADOS:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v10, Lcom/example/ui/viewmodel/ManagerTab;->AUDIT_LOGS:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v11, Lcom/example/ui/viewmodel/ManagerTab;->SEGURIDAD:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v12, Lcom/example/ui/viewmodel/ManagerTab;->QR_MENU:Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v13, Lcom/example/ui/viewmodel/ManagerTab;->CONFIGURACION:Lcom/example/ui/viewmodel/ManagerTab;

    filled-new-array/range {v0 .. v13}, [Lcom/example/ui/viewmodel/ManagerTab;

    move-result-object v0

    return-object v0
.end method

.method static constructor <clinit>()V
    .locals 3

    .line 21
    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "MENU"

    const/4 v2, 0x0

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->MENU:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "COMPRAS"

    const/4 v2, 0x1

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->COMPRAS:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "INVENTARIO"

    const/4 v2, 0x2

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->INVENTARIO:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "TEMAS_WEB"

    const/4 v2, 0x3

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->TEMAS_WEB:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "HORARIOS_ESTADO"

    const/4 v2, 0x4

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->HORARIOS_ESTADO:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "FACTURACION"

    const/4 v2, 0x5

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->FACTURACION:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "RESUMEN_FINANCIERO"

    const/4 v2, 0x6

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->RESUMEN_FINANCIERO:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "VENTAS"

    const/4 v2, 0x7

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->VENTAS:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "HISTORIAL"

    const/16 v2, 0x8

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->HISTORIAL:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "EMPLEADOS"

    const/16 v2, 0x9

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->EMPLEADOS:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "AUDIT_LOGS"

    const/16 v2, 0xa

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->AUDIT_LOGS:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "SEGURIDAD"

    const/16 v2, 0xb

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->SEGURIDAD:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "QR_MENU"

    const/16 v2, 0xc

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->QR_MENU:Lcom/example/ui/viewmodel/ManagerTab;

    new-instance v0, Lcom/example/ui/viewmodel/ManagerTab;

    const-string v1, "CONFIGURACION"

    const/16 v2, 0xd

    invoke-direct {v0, v1, v2}, Lcom/example/ui/viewmodel/ManagerTab;-><init>(Ljava/lang/String;I)V

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->CONFIGURACION:Lcom/example/ui/viewmodel/ManagerTab;

    invoke-static {}, Lcom/example/ui/viewmodel/ManagerTab;->$values()[Lcom/example/ui/viewmodel/ManagerTab;

    move-result-object v0

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->$VALUES:[Lcom/example/ui/viewmodel/ManagerTab;

    sget-object v0, Lcom/example/ui/viewmodel/ManagerTab;->$VALUES:[Lcom/example/ui/viewmodel/ManagerTab;

    check-cast v0, [Ljava/lang/Enum;

    invoke-static {v0}, Lkotlin/enums/EnumEntriesKt;->enumEntries([Ljava/lang/Enum;)Lkotlin/enums/EnumEntries;

    move-result-object v0

    sput-object v0, Lcom/example/ui/viewmodel/ManagerTab;->$ENTRIES:Lkotlin/enums/EnumEntries;

    return-void
.end method

.method private constructor <init>(Ljava/lang/String;I)V
    .locals 0
    .param p1, "$enum$name"    # Ljava/lang/String;
    .param p2, "$enum$ordinal"    # I
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "()V"
        }
    .end annotation

    .line 21
    invoke-direct {p0, p1, p2}, Ljava/lang/Enum;-><init>(Ljava/lang/String;I)V

    return-void
.end method

.method public static getEntries()Lkotlin/enums/EnumEntries;
    .locals 1
    .annotation system Ldalvik/annotation/Signature;
        value = {
            "()",
            "Lkotlin/enums/EnumEntries<",
            "Lcom/example/ui/viewmodel/ManagerTab;",
            ">;"
        }
    .end annotation

    sget-object v0, Lcom/example/ui/viewmodel/ManagerTab;->$ENTRIES:Lkotlin/enums/EnumEntries;

    return-object v0
.end method

.method public static valueOf(Ljava/lang/String;)Lcom/example/ui/viewmodel/ManagerTab;
    .locals 1

    const-class v0, Lcom/example/ui/viewmodel/ManagerTab;

    invoke-static {v0, p0}, Ljava/lang/Enum;->valueOf(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;

    move-result-object v0

    check-cast v0, Lcom/example/ui/viewmodel/ManagerTab;

    return-object v0
.end method

.method public static values()[Lcom/example/ui/viewmodel/ManagerTab;
    .locals 1

    sget-object v0, Lcom/example/ui/viewmodel/ManagerTab;->$VALUES:[Lcom/example/ui/viewmodel/ManagerTab;

    invoke-virtual {v0}, Ljava/lang/Object;->clone()Ljava/lang/Object;

    move-result-object v0

    check-cast v0, [Lcom/example/ui/viewmodel/ManagerTab;

    return-object v0
.end method
