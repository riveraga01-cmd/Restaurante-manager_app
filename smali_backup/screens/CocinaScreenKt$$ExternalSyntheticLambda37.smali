.class public final synthetic Lcom/example/ui/screens/CocinaScreenKt$$ExternalSyntheticLambda37;
.super Ljava/lang/Object;
.source "D8$$SyntheticClass"

# interfaces
.implements Lkotlin/jvm/functions/Function9;


# direct methods
.method public synthetic constructor <init>()V
    .locals 0

    .line 0
    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public final invoke(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    .locals 15

    .line 0
    move-object/from16 v0, p1

    check-cast v0, Ljava/lang/Long;

    invoke-virtual {v0}, Ljava/lang/Long;->longValue()J

    move-result-wide v1

    move-object/from16 v3, p2

    check-cast v3, Ljava/lang/String;

    move-object/from16 v0, p3

    check-cast v0, Ljava/lang/Double;

    invoke-virtual {v0}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v4

    move-object/from16 v0, p4

    check-cast v0, Ljava/lang/Double;

    invoke-virtual {v0}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v6

    move-object/from16 v0, p5

    check-cast v0, Ljava/lang/Double;

    invoke-virtual {v0}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v8

    move-object/from16 v0, p6

    check-cast v0, Ljava/lang/Double;

    invoke-virtual {v0}, Ljava/lang/Double;->doubleValue()D

    move-result-wide v10

    move-object/from16 v12, p7

    check-cast v12, Ljava/lang/String;

    move-object/from16 v13, p8

    check-cast v13, Ljava/lang/String;

    move-object/from16 v14, p9

    check-cast v14, Ljava/lang/Long;

    invoke-static/range {v1 .. v14}, Lcom/example/ui/screens/CocinaScreenKt;->CocinaScreen$lambda$136$lambda$135$lambda$81$lambda$80(JLjava/lang/String;DDDDLjava/lang/String;Ljava/lang/String;Ljava/lang/Long;)Lkotlin/Unit;

    move-result-object v0

    return-object v0
.end method
