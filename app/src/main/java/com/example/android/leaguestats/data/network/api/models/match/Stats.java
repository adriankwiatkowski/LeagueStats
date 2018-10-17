package com.example.android.leaguestats.data.network.api.models.match;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stats {

    @SerializedName("participantId")
    @Expose
    private long participantId;
    @SerializedName("win")
    @Expose
    private boolean win;
    @SerializedName("item0")
    @Expose
    private int item0;
    @SerializedName("item1")
    @Expose
    private int item1;
    @SerializedName("item2")
    @Expose
    private int item2;
    @SerializedName("item3")
    @Expose
    private int item3;
    @SerializedName("item4")
    @Expose
    private int item4;
    @SerializedName("item5")
    @Expose
    private int item5;
    @SerializedName("item6")
    @Expose
    private int item6;
    @SerializedName("kills")
    @Expose
    private int kills;
    @SerializedName("deaths")
    @Expose
    private int deaths;
    @SerializedName("assists")
    @Expose
    private int assists;
    @SerializedName("largestKillingSpree")
    @Expose
    private int largestKillingSpree;
    @SerializedName("largestMultiKill")
    @Expose
    private int largestMultiKill;
    @SerializedName("killingSprees")
    @Expose
    private int killingSprees;
    @SerializedName("longestTimeSpentLiving")
    @Expose
    private int longestTimeSpentLiving;
    @SerializedName("doubleKills")
    @Expose
    private int doubleKills;
    @SerializedName("tripleKills")
    @Expose
    private int tripleKills;
    @SerializedName("quadraKills")
    @Expose
    private int quadraKills;
    @SerializedName("pentaKills")
    @Expose
    private int pentaKills;
    @SerializedName("unrealKills")
    @Expose
    private int unrealKills;
    @SerializedName("totalDamageDealt")
    @Expose
    private long totalDamageDealt;
    @SerializedName("magicDamageDealt")
    @Expose
    private long magicDamageDealt;
    @SerializedName("physicalDamageDealt")
    @Expose
    private long physicalDamageDealt;
    @SerializedName("trueDamageDealt")
    @Expose
    private long trueDamageDealt;
    @SerializedName("largestCriticalStrike")
    @Expose
    private int largestCriticalStrike;
    @SerializedName("totalDamageDealtToChampions")
    @Expose
    private long totalDamageDealtToChampions;
    @SerializedName("magicDamageDealtToChampions")
    @Expose
    private long magicDamageDealtToChampions;
    @SerializedName("physicalDamageDealtToChampions")
    @Expose
    private long physicalDamageDealtToChampions;
    @SerializedName("trueDamageDealtToChampions")
    @Expose
    private long trueDamageDealtToChampions;
    @SerializedName("totalHeal")
    @Expose
    private long totalHeal;
    @SerializedName("totalUnitsHealed")
    @Expose
    private int totalUnitsHealed;
    @SerializedName("damageSelfMitigated")
    @Expose
    private long damageSelfMitigated;
    @SerializedName("damageDealtToObjectives")
    @Expose
    private long damageDealtToObjectives;
    @SerializedName("damageDealtToTurrets")
    @Expose
    private long damageDealtToTurrets;
    @SerializedName("visionScore")
    @Expose
    private long visionScore;
    @SerializedName("timeCCingOthers")
    @Expose
    private long timeCCingOthers;
    @SerializedName("totalDamageTaken")
    @Expose
    private long totalDamageTaken;
    @SerializedName("magicalDamageTaken")
    @Expose
    private long magicalDamageTaken;
    @SerializedName("physicalDamageTaken")
    @Expose
    private long physicalDamageTaken;
    @SerializedName("trueDamageTaken")
    @Expose
    private long trueDamageTaken;
    @SerializedName("goldEarned")
    @Expose
    private int goldEarned;
    @SerializedName("goldSpent")
    @Expose
    private int goldSpent;
    @SerializedName("turretKills")
    @Expose
    private int turretKills;
    @SerializedName("inhibitorKills")
    @Expose
    private int inhibitorKills;
    @SerializedName("totalMinionsKilled")
    @Expose
    private int totalMinionsKilled;
    @SerializedName("neutralMinionsKilled")
    @Expose
    private int neutralMinionsKilled;
    @SerializedName("neutralMinionsKilledTeamJungle")
    @Expose
    private int neutralMinionsKilledTeamJungle;
    @SerializedName("neutralMinionsKilledEnemyJungle")
    @Expose
    private int neutralMinionsKilledEnemyJungle;
    @SerializedName("totalTimeCrowdControlDealt")
    @Expose
    private int totalTimeCrowdControlDealt;
    @SerializedName("champLevel")
    @Expose
    private int champLevel;
    @SerializedName("visionWardsBoughtInGame")
    @Expose
    private int visionWardsBoughtInGame;
    @SerializedName("sightWardsBoughtInGame")
    @Expose
    private int sightWardsBoughtInGame;
    @SerializedName("wardsPlaced")
    @Expose
    private int wardsPlaced;
    @SerializedName("wardsKilled")
    @Expose
    private int wardsKilled;
    @SerializedName("firstBloodKill")
    @Expose
    private boolean firstBloodKill;
    @SerializedName("firstBloodAssist")
    @Expose
    private boolean firstBloodAssist;
    @SerializedName("firstTowerKill")
    @Expose
    private boolean firstTowerKill;
    @SerializedName("firstTowerAssist")
    @Expose
    private boolean firstTowerAssist;
    @SerializedName("firstInhibitorKill")
    @Expose
    private boolean firstInhibitorKill;
    @SerializedName("firstInhibitorAssist")
    @Expose
    private boolean firstInhibitorAssist;
    @SerializedName("combatPlayerScore")
    @Expose
    private int combatPlayerScore;
    @SerializedName("objectivePlayerScore")
    @Expose
    private int objectivePlayerScore;
    @SerializedName("totalPlayerScore")
    @Expose
    private int totalPlayerScore;
    @SerializedName("totalScoreRank")
    @Expose
    private int totalScoreRank;
    @SerializedName("playerScore0")
    @Expose
    private int playerScore0;
    @SerializedName("playerScore1")
    @Expose
    private int playerScore1;
    @SerializedName("playerScore2")
    @Expose
    private int playerScore2;
    @SerializedName("playerScore3")
    @Expose
    private int playerScore3;
    @SerializedName("playerScore4")
    @Expose
    private int playerScore4;
    @SerializedName("playerScore5")
    @Expose
    private int playerScore5;
    @SerializedName("playerScore6")
    @Expose
    private int playerScore6;
    @SerializedName("playerScore7")
    @Expose
    private int playerScore7;
    @SerializedName("playerScore8")
    @Expose
    private int playerScore8;
    @SerializedName("playerScore9")
    @Expose
    private int playerScore9;
    @SerializedName("perk0")
    @Expose
    private int perk0;
    @SerializedName("perk0Var1")
    @Expose
    private int perk0Var1;
    @SerializedName("perk0Var2")
    @Expose
    private int perk0Var2;
    @SerializedName("perk0Var3")
    @Expose
    private int perk0Var3;
    @SerializedName("perk1")
    @Expose
    private int perk1;
    @SerializedName("perk1Var1")
    @Expose
    private int perk1Var1;
    @SerializedName("perk1Var2")
    @Expose
    private int perk1Var2;
    @SerializedName("perk1Var3")
    @Expose
    private int perk1Var3;
    @SerializedName("perk2")
    @Expose
    private int perk2;
    @SerializedName("perk2Var1")
    @Expose
    private int perk2Var1;
    @SerializedName("perk2Var2")
    @Expose
    private int perk2Var2;
    @SerializedName("perk2Var3")
    @Expose
    private int perk2Var3;
    @SerializedName("perk3")
    @Expose
    private int perk3;
    @SerializedName("perk3Var1")
    @Expose
    private int perk3Var1;
    @SerializedName("perk3Var2")
    @Expose
    private int perk3Var2;
    @SerializedName("perk3Var3")
    @Expose
    private int perk3Var3;
    @SerializedName("perk4")
    @Expose
    private int perk4;
    @SerializedName("perk4Var1")
    @Expose
    private int perk4Var1;
    @SerializedName("perk4Var2")
    @Expose
    private int perk4Var2;
    @SerializedName("perk4Var3")
    @Expose
    private int perk4Var3;
    @SerializedName("perk5")
    @Expose
    private int perk5;
    @SerializedName("perk5Var1")
    @Expose
    private int perk5Var1;
    @SerializedName("perk5Var2")
    @Expose
    private int perk5Var2;
    @SerializedName("perk5Var3")
    @Expose
    private int perk5Var3;
    @SerializedName("perkPrimaryStyle")
    @Expose
    private int perkPrimaryStyle;
    @SerializedName("perkSubStyle")
    @Expose
    private int perkSubStyle;

    /**
     * No args constructor for use in serialization
     *
     */
    public Stats() {
    }

    /**
     *
     * @param item2
     * @param unrealKills
     * @param item1
     * @param totalDamageTaken
     * @param item0
     * @param pentaKills
     * @param sightWardsBoughtInGame
     * @param perk1
     * @param perk0
     * @param wardsKilled
     * @param perk3
     * @param perk2
     * @param perk5
     * @param perk2Var3
     * @param perk4
     * @param perk2Var1
     * @param perk2Var2
     * @param trueDamageDealt
     * @param doubleKills
     * @param physicalDamageDealt
     * @param playerScore9
     * @param tripleKills
     * @param deaths
     * @param firstBloodAssist
     * @param playerScore5
     * @param playerScore6
     * @param playerScore7
     * @param playerScore8
     * @param playerScore1
     * @param playerScore2
     * @param playerScore3
     * @param magicDamageDealtToChampions
     * @param playerScore4
     * @param playerScore0
     * @param assists
     * @param perk3Var1
     * @param damageDealtToTurrets
     * @param inhibitorKills
     * @param perk3Var3
     * @param perk3Var2
     * @param timeCCingOthers
     * @param physicalDamageDealtToChampions
     * @param perk0Var3
     * @param perk0Var2
     * @param perk0Var1
     * @param perk5Var3
     * @param totalDamageDealtToChampions
     * @param perk5Var2
     * @param perk5Var1
     * @param firstBloodKill
     * @param wardsPlaced
     * @param killingSprees
     * @param magicalDamageTaken
     * @param totalScoreRank
     * @param totalUnitsHealed
     * @param kills
     * @param firstInhibitorAssist
     * @param totalPlayerScore
     * @param participantId
     * @param totalHeal
     * @param perk1Var1
     * @param perk1Var3
     * @param perk1Var2
     * @param trueDamageTaken
     * @param neutralMinionsKilled
     * @param magicDamageDealt
     * @param longestTimeSpentLiving
     * @param largestCriticalStrike
     * @param damageDealtToObjectives
     * @param perkPrimaryStyle
     * @param visionWardsBoughtInGame
     * @param totalTimeCrowdControlDealt
     * @param champLevel
     * @param physicalDamageTaken
     * @param totalDamageDealt
     * @param largestKillingSpree
     * @param perkSubStyle
     * @param quadraKills
     * @param goldSpent
     * @param goldEarned
     * @param neutralMinionsKilledTeamJungle
     * @param firstTowerKill
     * @param trueDamageDealtToChampions
     * @param firstInhibitorKill
     * @param perk4Var3
     * @param perk4Var1
     * @param perk4Var2
     * @param visionScore
     * @param neutralMinionsKilledEnemyJungle
     * @param turretKills
     * @param largestMultiKill
     * @param win
     * @param item4
     * @param item3
     * @param objectivePlayerScore
     * @param damageSelfMitigated
     * @param item6
     * @param firstTowerAssist
     * @param item5
     * @param combatPlayerScore
     * @param totalMinionsKilled
     */
    public Stats(long participantId, boolean win, int item0, int item1, int item2, int item3, int item4, int item5, int item6, int kills, int deaths, int assists, int largestKillingSpree, int largestMultiKill, int killingSprees, int longestTimeSpentLiving, int doubleKills, int tripleKills, int quadraKills, int pentaKills, int unrealKills, long totalDamageDealt, long magicDamageDealt, long physicalDamageDealt, long trueDamageDealt, int largestCriticalStrike, long totalDamageDealtToChampions, long magicDamageDealtToChampions, long physicalDamageDealtToChampions, long trueDamageDealtToChampions, long totalHeal, int totalUnitsHealed, long damageSelfMitigated, long damageDealtToObjectives, long damageDealtToTurrets, long visionScore, long timeCCingOthers, int totalDamageTaken, long magicalDamageTaken, long physicalDamageTaken, long trueDamageTaken, int goldEarned, int goldSpent, int turretKills, int inhibitorKills, int totalMinionsKilled, int neutralMinionsKilled, int neutralMinionsKilledTeamJungle, int neutralMinionsKilledEnemyJungle, int totalTimeCrowdControlDealt, int champLevel, int visionWardsBoughtInGame, int sightWardsBoughtInGame, int wardsPlaced, int wardsKilled, boolean firstBloodKill, boolean firstBloodAssist, boolean firstTowerKill, boolean firstTowerAssist, boolean firstInhibitorKill, boolean firstInhibitorAssist, int combatPlayerScore, int objectivePlayerScore, int totalPlayerScore, int totalScoreRank, int playerScore0, int playerScore1, int playerScore2, int playerScore3, int playerScore4, int playerScore5, int playerScore6, int playerScore7, int playerScore8, int playerScore9, int perk0, int perk0Var1, int perk0Var2, int perk0Var3, int perk1, int perk1Var1, int perk1Var2, int perk1Var3, int perk2, int perk2Var1, int perk2Var2, int perk2Var3, int perk3, int perk3Var1, int perk3Var2, int perk3Var3, int perk4, int perk4Var1, int perk4Var2, int perk4Var3, int perk5, int perk5Var1, int perk5Var2, int perk5Var3, int perkPrimaryStyle, int perkSubStyle) {
        super();
        this.participantId = participantId;
        this.win = win;
        this.item0 = item0;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.largestKillingSpree = largestKillingSpree;
        this.largestMultiKill = largestMultiKill;
        this.killingSprees = killingSprees;
        this.longestTimeSpentLiving = longestTimeSpentLiving;
        this.doubleKills = doubleKills;
        this.tripleKills = tripleKills;
        this.quadraKills = quadraKills;
        this.pentaKills = pentaKills;
        this.unrealKills = unrealKills;
        this.totalDamageDealt = totalDamageDealt;
        this.magicDamageDealt = magicDamageDealt;
        this.physicalDamageDealt = physicalDamageDealt;
        this.trueDamageDealt = trueDamageDealt;
        this.largestCriticalStrike = largestCriticalStrike;
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
        this.magicDamageDealtToChampions = magicDamageDealtToChampions;
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
        this.trueDamageDealtToChampions = trueDamageDealtToChampions;
        this.totalHeal = totalHeal;
        this.totalUnitsHealed = totalUnitsHealed;
        this.damageSelfMitigated = damageSelfMitigated;
        this.damageDealtToObjectives = damageDealtToObjectives;
        this.damageDealtToTurrets = damageDealtToTurrets;
        this.visionScore = visionScore;
        this.timeCCingOthers = timeCCingOthers;
        this.totalDamageTaken = totalDamageTaken;
        this.magicalDamageTaken = magicalDamageTaken;
        this.physicalDamageTaken = physicalDamageTaken;
        this.trueDamageTaken = trueDamageTaken;
        this.goldEarned = goldEarned;
        this.goldSpent = goldSpent;
        this.turretKills = turretKills;
        this.inhibitorKills = inhibitorKills;
        this.totalMinionsKilled = totalMinionsKilled;
        this.neutralMinionsKilled = neutralMinionsKilled;
        this.neutralMinionsKilledTeamJungle = neutralMinionsKilledTeamJungle;
        this.neutralMinionsKilledEnemyJungle = neutralMinionsKilledEnemyJungle;
        this.totalTimeCrowdControlDealt = totalTimeCrowdControlDealt;
        this.champLevel = champLevel;
        this.visionWardsBoughtInGame = visionWardsBoughtInGame;
        this.sightWardsBoughtInGame = sightWardsBoughtInGame;
        this.wardsPlaced = wardsPlaced;
        this.wardsKilled = wardsKilled;
        this.firstBloodKill = firstBloodKill;
        this.firstBloodAssist = firstBloodAssist;
        this.firstTowerKill = firstTowerKill;
        this.firstTowerAssist = firstTowerAssist;
        this.firstInhibitorKill = firstInhibitorKill;
        this.firstInhibitorAssist = firstInhibitorAssist;
        this.combatPlayerScore = combatPlayerScore;
        this.objectivePlayerScore = objectivePlayerScore;
        this.totalPlayerScore = totalPlayerScore;
        this.totalScoreRank = totalScoreRank;
        this.playerScore0 = playerScore0;
        this.playerScore1 = playerScore1;
        this.playerScore2 = playerScore2;
        this.playerScore3 = playerScore3;
        this.playerScore4 = playerScore4;
        this.playerScore5 = playerScore5;
        this.playerScore6 = playerScore6;
        this.playerScore7 = playerScore7;
        this.playerScore8 = playerScore8;
        this.playerScore9 = playerScore9;
        this.perk0 = perk0;
        this.perk0Var1 = perk0Var1;
        this.perk0Var2 = perk0Var2;
        this.perk0Var3 = perk0Var3;
        this.perk1 = perk1;
        this.perk1Var1 = perk1Var1;
        this.perk1Var2 = perk1Var2;
        this.perk1Var3 = perk1Var3;
        this.perk2 = perk2;
        this.perk2Var1 = perk2Var1;
        this.perk2Var2 = perk2Var2;
        this.perk2Var3 = perk2Var3;
        this.perk3 = perk3;
        this.perk3Var1 = perk3Var1;
        this.perk3Var2 = perk3Var2;
        this.perk3Var3 = perk3Var3;
        this.perk4 = perk4;
        this.perk4Var1 = perk4Var1;
        this.perk4Var2 = perk4Var2;
        this.perk4Var3 = perk4Var3;
        this.perk5 = perk5;
        this.perk5Var1 = perk5Var1;
        this.perk5Var2 = perk5Var2;
        this.perk5Var3 = perk5Var3;
        this.perkPrimaryStyle = perkPrimaryStyle;
        this.perkSubStyle = perkSubStyle;
    }

    public long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(long participantId) {
        this.participantId = participantId;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getItem0() {
        return item0;
    }

    public void setItem0(int item0) {
        this.item0 = item0;
    }

    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public int getItem4() {
        return item4;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }

    public int getItem5() {
        return item5;
    }

    public void setItem5(int item5) {
        this.item5 = item5;
    }

    public int getItem6() {
        return item6;
    }

    public void setItem6(int item6) {
        this.item6 = item6;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getLargestKillingSpree() {
        return largestKillingSpree;
    }

    public void setLargestKillingSpree(int largestKillingSpree) {
        this.largestKillingSpree = largestKillingSpree;
    }

    public int getLargestMultiKill() {
        return largestMultiKill;
    }

    public void setLargestMultiKill(int largestMultiKill) {
        this.largestMultiKill = largestMultiKill;
    }

    public int getKillingSprees() {
        return killingSprees;
    }

    public void setKillingSprees(int killingSprees) {
        this.killingSprees = killingSprees;
    }

    public int getLongestTimeSpentLiving() {
        return longestTimeSpentLiving;
    }

    public void setLongestTimeSpentLiving(int longestTimeSpentLiving) {
        this.longestTimeSpentLiving = longestTimeSpentLiving;
    }

    public int getDoubleKills() {
        return doubleKills;
    }

    public void setDoubleKills(int doubleKills) {
        this.doubleKills = doubleKills;
    }

    public int getTripleKills() {
        return tripleKills;
    }

    public void setTripleKills(int tripleKills) {
        this.tripleKills = tripleKills;
    }

    public int getQuadraKills() {
        return quadraKills;
    }

    public void setQuadraKills(int quadraKills) {
        this.quadraKills = quadraKills;
    }

    public int getPentaKills() {
        return pentaKills;
    }

    public void setPentaKills(int pentaKills) {
        this.pentaKills = pentaKills;
    }

    public int getUnrealKills() {
        return unrealKills;
    }

    public void setUnrealKills(int unrealKills) {
        this.unrealKills = unrealKills;
    }

    public long getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(long totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public long getMagicDamageDealt() {
        return magicDamageDealt;
    }

    public void setMagicDamageDealt(long magicDamageDealt) {
        this.magicDamageDealt = magicDamageDealt;
    }

    public long getPhysicalDamageDealt() {
        return physicalDamageDealt;
    }

    public void setPhysicalDamageDealt(long physicalDamageDealt) {
        this.physicalDamageDealt = physicalDamageDealt;
    }

    public long getTrueDamageDealt() {
        return trueDamageDealt;
    }

    public void setTrueDamageDealt(long trueDamageDealt) {
        this.trueDamageDealt = trueDamageDealt;
    }

    public int getLargestCriticalStrike() {
        return largestCriticalStrike;
    }

    public void setLargestCriticalStrike(int largestCriticalStrike) {
        this.largestCriticalStrike = largestCriticalStrike;
    }

    public long getTotalDamageDealtToChampions() {
        return totalDamageDealtToChampions;
    }

    public void setTotalDamageDealtToChampions(long totalDamageDealtToChampions) {
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
    }

    public long getMagicDamageDealtToChampions() {
        return magicDamageDealtToChampions;
    }

    public void setMagicDamageDealtToChampions(int magicDamageDealtToChampions) {
        this.magicDamageDealtToChampions = magicDamageDealtToChampions;
    }

    public long getPhysicalDamageDealtToChampions() {
        return physicalDamageDealtToChampions;
    }

    public void setPhysicalDamageDealtToChampions(long physicalDamageDealtToChampions) {
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
    }

    public long getTrueDamageDealtToChampions() {
        return trueDamageDealtToChampions;
    }

    public void setTrueDamageDealtToChampions(long trueDamageDealtToChampions) {
        this.trueDamageDealtToChampions = trueDamageDealtToChampions;
    }

    public long getTotalHeal() {
        return totalHeal;
    }

    public void setTotalHeal(long totalHeal) {
        this.totalHeal = totalHeal;
    }

    public int getTotalUnitsHealed() {
        return totalUnitsHealed;
    }

    public void setTotalUnitsHealed(int totalUnitsHealed) {
        this.totalUnitsHealed = totalUnitsHealed;
    }

    public long getDamageSelfMitigated() {
        return damageSelfMitigated;
    }

    public void setDamageSelfMitigated(long damageSelfMitigated) {
        this.damageSelfMitigated = damageSelfMitigated;
    }

    public long getDamageDealtToObjectives() {
        return damageDealtToObjectives;
    }

    public void setDamageDealtToObjectives(long damageDealtToObjectives) {
        this.damageDealtToObjectives = damageDealtToObjectives;
    }

    public long getDamageDealtToTurrets() {
        return damageDealtToTurrets;
    }

    public void setDamageDealtToTurrets(long damageDealtToTurrets) {
        this.damageDealtToTurrets = damageDealtToTurrets;
    }

    public long getVisionScore() {
        return visionScore;
    }

    public void setVisionScore(long visionScore) {
        this.visionScore = visionScore;
    }

    public long getTimeCCingOthers() {
        return timeCCingOthers;
    }

    public void setTimeCCingOthers(long timeCCingOthers) {
        this.timeCCingOthers = timeCCingOthers;
    }

    public long getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public void setTotalDamageTaken(long totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public long getMagicalDamageTaken() {
        return magicalDamageTaken;
    }

    public void setMagicalDamageTaken(long magicalDamageTaken) {
        this.magicalDamageTaken = magicalDamageTaken;
    }

    public long getPhysicalDamageTaken() {
        return physicalDamageTaken;
    }

    public void setPhysicalDamageTaken(long physicalDamageTaken) {
        this.physicalDamageTaken = physicalDamageTaken;
    }

    public long getTrueDamageTaken() {
        return trueDamageTaken;
    }

    public void setTrueDamageTaken(long trueDamageTaken) {
        this.trueDamageTaken = trueDamageTaken;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
    }

    public int getGoldSpent() {
        return goldSpent;
    }

    public void setGoldSpent(int goldSpent) {
        this.goldSpent = goldSpent;
    }

    public int getTurretKills() {
        return turretKills;
    }

    public void setTurretKills(int turretKills) {
        this.turretKills = turretKills;
    }

    public int getInhibitorKills() {
        return inhibitorKills;
    }

    public void setInhibitorKills(int inhibitorKills) {
        this.inhibitorKills = inhibitorKills;
    }

    public int getTotalMinionsKilled() {
        return totalMinionsKilled;
    }

    public void setTotalMinionsKilled(int totalMinionsKilled) {
        this.totalMinionsKilled = totalMinionsKilled;
    }

    public int getNeutralMinionsKilled() {
        return neutralMinionsKilled;
    }

    public void setNeutralMinionsKilled(int neutralMinionsKilled) {
        this.neutralMinionsKilled = neutralMinionsKilled;
    }

    public int getNeutralMinionsKilledTeamJungle() {
        return neutralMinionsKilledTeamJungle;
    }

    public void setNeutralMinionsKilledTeamJungle(int neutralMinionsKilledTeamJungle) {
        this.neutralMinionsKilledTeamJungle = neutralMinionsKilledTeamJungle;
    }

    public int getNeutralMinionsKilledEnemyJungle() {
        return neutralMinionsKilledEnemyJungle;
    }

    public void setNeutralMinionsKilledEnemyJungle(int neutralMinionsKilledEnemyJungle) {
        this.neutralMinionsKilledEnemyJungle = neutralMinionsKilledEnemyJungle;
    }

    public int getTotalTimeCrowdControlDealt() {
        return totalTimeCrowdControlDealt;
    }

    public void setTotalTimeCrowdControlDealt(int totalTimeCrowdControlDealt) {
        this.totalTimeCrowdControlDealt = totalTimeCrowdControlDealt;
    }

    public int getChampLevel() {
        return champLevel;
    }

    public void setChampLevel(int champLevel) {
        this.champLevel = champLevel;
    }

    public int getVisionWardsBoughtInGame() {
        return visionWardsBoughtInGame;
    }

    public void setVisionWardsBoughtInGame(int visionWardsBoughtInGame) {
        this.visionWardsBoughtInGame = visionWardsBoughtInGame;
    }

    public int getSightWardsBoughtInGame() {
        return sightWardsBoughtInGame;
    }

    public void setSightWardsBoughtInGame(int sightWardsBoughtInGame) {
        this.sightWardsBoughtInGame = sightWardsBoughtInGame;
    }

    public int getWardsPlaced() {
        return wardsPlaced;
    }

    public void setWardsPlaced(int wardsPlaced) {
        this.wardsPlaced = wardsPlaced;
    }

    public int getWardsKilled() {
        return wardsKilled;
    }

    public void setWardsKilled(int wardsKilled) {
        this.wardsKilled = wardsKilled;
    }

    public boolean isFirstBloodKill() {
        return firstBloodKill;
    }

    public void setFirstBloodKill(boolean firstBloodKill) {
        this.firstBloodKill = firstBloodKill;
    }

    public boolean isFirstBloodAssist() {
        return firstBloodAssist;
    }

    public void setFirstBloodAssist(boolean firstBloodAssist) {
        this.firstBloodAssist = firstBloodAssist;
    }

    public boolean isFirstTowerKill() {
        return firstTowerKill;
    }

    public void setFirstTowerKill(boolean firstTowerKill) {
        this.firstTowerKill = firstTowerKill;
    }

    public boolean isFirstTowerAssist() {
        return firstTowerAssist;
    }

    public void setFirstTowerAssist(boolean firstTowerAssist) {
        this.firstTowerAssist = firstTowerAssist;
    }

    public boolean isFirstInhibitorKill() {
        return firstInhibitorKill;
    }

    public void setFirstInhibitorKill(boolean firstInhibitorKill) {
        this.firstInhibitorKill = firstInhibitorKill;
    }

    public boolean isFirstInhibitorAssist() {
        return firstInhibitorAssist;
    }

    public void setFirstInhibitorAssist(boolean firstInhibitorAssist) {
        this.firstInhibitorAssist = firstInhibitorAssist;
    }

    public int getCombatPlayerScore() {
        return combatPlayerScore;
    }

    public void setCombatPlayerScore(int combatPlayerScore) {
        this.combatPlayerScore = combatPlayerScore;
    }

    public int getObjectivePlayerScore() {
        return objectivePlayerScore;
    }

    public void setObjectivePlayerScore(int objectivePlayerScore) {
        this.objectivePlayerScore = objectivePlayerScore;
    }

    public int getTotalPlayerScore() {
        return totalPlayerScore;
    }

    public void setTotalPlayerScore(int totalPlayerScore) {
        this.totalPlayerScore = totalPlayerScore;
    }

    public int getTotalScoreRank() {
        return totalScoreRank;
    }

    public void setTotalScoreRank(int totalScoreRank) {
        this.totalScoreRank = totalScoreRank;
    }

    public int getPlayerScore0() {
        return playerScore0;
    }

    public void setPlayerScore0(int playerScore0) {
        this.playerScore0 = playerScore0;
    }

    public int getPlayerScore1() {
        return playerScore1;
    }

    public void setPlayerScore1(int playerScore1) {
        this.playerScore1 = playerScore1;
    }

    public int getPlayerScore2() {
        return playerScore2;
    }

    public void setPlayerScore2(int playerScore2) {
        this.playerScore2 = playerScore2;
    }

    public int getPlayerScore3() {
        return playerScore3;
    }

    public void setPlayerScore3(int playerScore3) {
        this.playerScore3 = playerScore3;
    }

    public int getPlayerScore4() {
        return playerScore4;
    }

    public void setPlayerScore4(int playerScore4) {
        this.playerScore4 = playerScore4;
    }

    public int getPlayerScore5() {
        return playerScore5;
    }

    public void setPlayerScore5(int playerScore5) {
        this.playerScore5 = playerScore5;
    }

    public int getPlayerScore6() {
        return playerScore6;
    }

    public void setPlayerScore6(int playerScore6) {
        this.playerScore6 = playerScore6;
    }

    public int getPlayerScore7() {
        return playerScore7;
    }

    public void setPlayerScore7(int playerScore7) {
        this.playerScore7 = playerScore7;
    }

    public int getPlayerScore8() {
        return playerScore8;
    }

    public void setPlayerScore8(int playerScore8) {
        this.playerScore8 = playerScore8;
    }

    public int getPlayerScore9() {
        return playerScore9;
    }

    public void setPlayerScore9(int playerScore9) {
        this.playerScore9 = playerScore9;
    }

    public int getPerk0() {
        return perk0;
    }

    public void setPerk0(int perk0) {
        this.perk0 = perk0;
    }

    public int getPerk0Var1() {
        return perk0Var1;
    }

    public void setPerk0Var1(int perk0Var1) {
        this.perk0Var1 = perk0Var1;
    }

    public int getPerk0Var2() {
        return perk0Var2;
    }

    public void setPerk0Var2(int perk0Var2) {
        this.perk0Var2 = perk0Var2;
    }

    public int getPerk0Var3() {
        return perk0Var3;
    }

    public void setPerk0Var3(int perk0Var3) {
        this.perk0Var3 = perk0Var3;
    }

    public int getPerk1() {
        return perk1;
    }

    public void setPerk1(int perk1) {
        this.perk1 = perk1;
    }

    public int getPerk1Var1() {
        return perk1Var1;
    }

    public void setPerk1Var1(int perk1Var1) {
        this.perk1Var1 = perk1Var1;
    }

    public int getPerk1Var2() {
        return perk1Var2;
    }

    public void setPerk1Var2(int perk1Var2) {
        this.perk1Var2 = perk1Var2;
    }

    public int getPerk1Var3() {
        return perk1Var3;
    }

    public void setPerk1Var3(int perk1Var3) {
        this.perk1Var3 = perk1Var3;
    }

    public int getPerk2() {
        return perk2;
    }

    public void setPerk2(int perk2) {
        this.perk2 = perk2;
    }

    public int getPerk2Var1() {
        return perk2Var1;
    }

    public void setPerk2Var1(int perk2Var1) {
        this.perk2Var1 = perk2Var1;
    }

    public int getPerk2Var2() {
        return perk2Var2;
    }

    public void setPerk2Var2(int perk2Var2) {
        this.perk2Var2 = perk2Var2;
    }

    public int getPerk2Var3() {
        return perk2Var3;
    }

    public void setPerk2Var3(int perk2Var3) {
        this.perk2Var3 = perk2Var3;
    }

    public int getPerk3() {
        return perk3;
    }

    public void setPerk3(int perk3) {
        this.perk3 = perk3;
    }

    public int getPerk3Var1() {
        return perk3Var1;
    }

    public void setPerk3Var1(int perk3Var1) {
        this.perk3Var1 = perk3Var1;
    }

    public int getPerk3Var2() {
        return perk3Var2;
    }

    public void setPerk3Var2(int perk3Var2) {
        this.perk3Var2 = perk3Var2;
    }

    public int getPerk3Var3() {
        return perk3Var3;
    }

    public void setPerk3Var3(int perk3Var3) {
        this.perk3Var3 = perk3Var3;
    }

    public int getPerk4() {
        return perk4;
    }

    public void setPerk4(int perk4) {
        this.perk4 = perk4;
    }

    public int getPerk4Var1() {
        return perk4Var1;
    }

    public void setPerk4Var1(int perk4Var1) {
        this.perk4Var1 = perk4Var1;
    }

    public int getPerk4Var2() {
        return perk4Var2;
    }

    public void setPerk4Var2(int perk4Var2) {
        this.perk4Var2 = perk4Var2;
    }

    public int getPerk4Var3() {
        return perk4Var3;
    }

    public void setPerk4Var3(int perk4Var3) {
        this.perk4Var3 = perk4Var3;
    }

    public int getPerk5() {
        return perk5;
    }

    public void setPerk5(int perk5) {
        this.perk5 = perk5;
    }

    public int getPerk5Var1() {
        return perk5Var1;
    }

    public void setPerk5Var1(int perk5Var1) {
        this.perk5Var1 = perk5Var1;
    }

    public int getPerk5Var2() {
        return perk5Var2;
    }

    public void setPerk5Var2(int perk5Var2) {
        this.perk5Var2 = perk5Var2;
    }

    public int getPerk5Var3() {
        return perk5Var3;
    }

    public void setPerk5Var3(int perk5Var3) {
        this.perk5Var3 = perk5Var3;
    }

    public int getPerkPrimaryStyle() {
        return perkPrimaryStyle;
    }

    public void setPerkPrimaryStyle(int perkPrimaryStyle) {
        this.perkPrimaryStyle = perkPrimaryStyle;
    }

    public int getPerkSubStyle() {
        return perkSubStyle;
    }

    public void setPerkSubStyle(int perkSubStyle) {
        this.perkSubStyle = perkSubStyle;
    }

}
