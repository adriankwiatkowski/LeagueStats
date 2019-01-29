package com.example.android.leaguestats.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "champion")
public class ChampionEntry {

    @PrimaryKey
    @ColumnInfo(name = "champion_id")
    private int id;
    @ColumnInfo(name = "champion_name")
    private String name;
    @ColumnInfo(name = "champion_key")
    private String key;
    @ColumnInfo(name = "champion_title")
    private String title;
    @ColumnInfo(name = "champion_lore")
    private String lore;

    @ColumnInfo(name = "champion_tags")
    private List<String> tags;

    @ColumnInfo(name = "champion_image_id")
    private String imageId;

    @ColumnInfo(name = "champion_splash_art_id")
    private List<Integer> splashArtId;
    @ColumnInfo(name = "champion_splash_art_name")
    private List<String> splashArtName;

    @ColumnInfo(name = "champion_difficulty")
    private int difficulty;
    @ColumnInfo(name = "champion_attack")
    private int attack;
    @ColumnInfo(name = "champion_defense")
    private int defense;
    @ColumnInfo(name = "champion_magic")
    private int magic;

    @ColumnInfo(name = "champion_vs_tips")
    private List<String> vsTips;
    @ColumnInfo(name = "champion_as_tips")
    private List<String> asTips;

    @ColumnInfo(name = "champion_attack_damage")
    private double attackDamage;
    @ColumnInfo(name = "champion_attack_damage_per_level")
    private double attackDamagePerLevel;
    @ColumnInfo(name = "champion_attack_range")
    private double attackRange;
    @ColumnInfo(name = "champion_armor")
    private double armor;
    @ColumnInfo(name = "champion_armor_per_level")
    private double armorPerLevel;
    @ColumnInfo(name = "champion_health")
    private double health;
    @ColumnInfo(name = "champion_health_per_level")
    private double healthPerLevel;
    @ColumnInfo(name = "champion_health_regen")
    private double healthRegen;
    @ColumnInfo(name = "champion_health_regen_per_level")
    private double healthRegenPerLevel;
    @ColumnInfo(name = "champion_mana")
    private double mana;
    @ColumnInfo(name = "champion_mana_per_level")
    private double manaPerLevel;
    @ColumnInfo(name = "champion_mana_regen")
    private double manaRegen;
    @ColumnInfo(name = "champion_mana_regen_per_level")
    private double manaRegenPerLevel;
    @ColumnInfo(name = "champion_attack_speed_offset")
    private double attackSpeedOffset;
    @ColumnInfo(name = "champion_attack_speed_per_level")
    private double attackSpeedPerLevel;
    @ColumnInfo(name = "champion_move_speed")
    private double moveSpeed;
    @ColumnInfo(name = "chmapion_crit")
    private double crit;
    @ColumnInfo(name = "champion_crit_per_level")
    private double critPerLevel;
    @ColumnInfo(name = "champion_magic_resist")
    private double magicResist;
    @ColumnInfo(name = "champion_magic_resist_per_level")
    private double magicResistPerLevel;

    @ColumnInfo(name = "champion_partype")
    private String partype;

    @ColumnInfo(name = "champion_passive_name")
    private String passiveName;
    @ColumnInfo(name = "champion_passive_description")
    private String passiveDescription;
    @ColumnInfo(name = "champion_passive_id")
    private String passiveId;

    private List<Spell> spells;

    public ChampionEntry(int id, String name, String key, String title, String lore, List<String> tags, String imageId,
                         List<Integer> splashArtId, List<String> splashArtName, int difficulty, int attack,
                         int defense, int magic, List<String> vsTips, List<String> asTips, double attackDamage,
                         double attackDamagePerLevel, double attackRange, double armor,
                         double armorPerLevel, double health, double healthPerLevel, double healthRegen,
                         double healthRegenPerLevel, double mana, double manaPerLevel, double manaRegen,
                         double manaRegenPerLevel, double attackSpeedOffset, double attackSpeedPerLevel,
                         double moveSpeed, double crit, double critPerLevel, double magicResist,
                         double magicResistPerLevel, String partype, String passiveName,
                         String passiveDescription, String passiveId, List<Spell> spells) {

        this.id = id;
        this.name = name;
        this.key = key;
        this.title = title;
        this.lore = lore;
        this.tags = tags;
        this.imageId = imageId;
        this.splashArtId = splashArtId;
        this.splashArtName = splashArtName;
        this.difficulty = difficulty;
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.vsTips = vsTips;
        this.asTips = asTips;

        this.attackDamage = attackDamage;
        this.attackDamagePerLevel = attackDamagePerLevel;
        this.attackRange = attackRange;
        this.armor = armor;
        this.armorPerLevel = armorPerLevel;
        this.health = health;
        this.healthPerLevel = healthPerLevel;
        this.healthRegen = healthRegen;
        this.healthRegenPerLevel = healthRegenPerLevel;
        this.mana = mana;
        this.manaPerLevel = manaPerLevel;
        this.manaRegen = manaRegen;
        this.manaRegenPerLevel = manaRegenPerLevel;
        this.attackSpeedOffset = attackSpeedOffset;
        this.attackSpeedPerLevel = attackSpeedPerLevel;
        this.moveSpeed = moveSpeed;
        this.crit = crit;
        this.critPerLevel = critPerLevel;
        this.magicResist = magicResist;
        this.magicResistPerLevel = magicResistPerLevel;

        this.partype = partype;
        this.passiveName = passiveName;
        this.passiveDescription = passiveDescription;
        this.passiveId = passiveId;
        this.spells = spells;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getLore() {
        return lore;
    }

    public int getId() {
        return id;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public List<Integer> getSplashArtId() {
        return splashArtId;
    }

    public void setSplashArtId(List<Integer> splashArt) {
        this.splashArtId = splashArt;
    }

    public List<String> getSplashArtName() {
        return splashArtName;
    }

    public void setSplashArtName(List<String> splashArtName) {
        this.splashArtName = splashArtName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public List<String> getVsTips() {
        return vsTips;
    }

    public void setVsTips(List<String> vsTips) {
        this.vsTips = vsTips;
    }

    public List<String> getAsTips() {
        return asTips;
    }

    public void setAsTips(List<String> asTips) {
        this.asTips = asTips;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public double getAttackDamagePerLevel() {
        return attackDamagePerLevel;
    }

    public void setAttackDamagePerLevel(double attackDamagePerLevel) {
        this.attackDamagePerLevel = attackDamagePerLevel;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(double attackRange) {
        this.attackRange = attackRange;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getArmorPerLevel() {
        return armorPerLevel;
    }

    public void setArmorPerLevel(double armorPerLevel) {
        this.armorPerLevel = armorPerLevel;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getHealthPerLevel() {
        return healthPerLevel;
    }

    public void setHealthPerLevel(double healthPerLevel) {
        this.healthPerLevel = healthPerLevel;
    }

    public double getHealthRegen() {
        return healthRegen;
    }

    public void setHealthRegen(double healthRegen) {
        this.healthRegen = healthRegen;
    }

    public double getHealthRegenPerLevel() {
        return healthRegenPerLevel;
    }

    public void setHealthRegenPerLevel(double healthRegenPerLevel) {
        this.healthRegenPerLevel = healthRegenPerLevel;
    }

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public double getManaPerLevel() {
        return manaPerLevel;
    }

    public void setManaPerLevel(double manaPerLevel) {
        this.manaPerLevel = manaPerLevel;
    }

    public double getManaRegen() {
        return manaRegen;
    }

    public void setManaRegen(double manaRegen) {
        this.manaRegen = manaRegen;
    }

    public double getManaRegenPerLevel() {
        return manaRegenPerLevel;
    }

    public void setManaRegenPerLevel(double manaRegenPerLevel) {
        this.manaRegenPerLevel = manaRegenPerLevel;
    }

    public double getAttackSpeedOffset() {
        return attackSpeedOffset;
    }

    public void setAttackSpeedOffset(double attackSpeedOffset) {
        this.attackSpeedOffset = attackSpeedOffset;
    }

    public double getAttackSpeedPerLevel() {
        return attackSpeedPerLevel;
    }

    public void setAttackSpeedPerLevel(double attackSpeedPerLevel) {
        this.attackSpeedPerLevel = attackSpeedPerLevel;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public double getCrit() {
        return crit;
    }

    public void setCrit(double crit) {
        this.crit = crit;
    }

    public double getCritPerLevel() {
        return critPerLevel;
    }

    public void setCritPerLevel(double critPerLevel) {
        this.critPerLevel = critPerLevel;
    }

    public double getMagicResistPerLevel() {
        return magicResistPerLevel;
    }

    public void setMagicResistPerLevel(double magicResistPerLevel) {
        this.magicResistPerLevel = magicResistPerLevel;
    }

    public double getMagicResist() {
        return magicResist;
    }

    public void setMagicResist(double magicResist) {
        this.magicResist = magicResist;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getPassiveName() {
        return passiveName;
    }

    public void setPassiveName(String passiveName) {
        this.passiveName = passiveName;
    }

    public String getPassiveId() {
        return passiveId;
    }

    public void setPassiveId(String passiveId) {
        this.passiveId = passiveId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPassiveDescription() {
        return passiveDescription;
    }

    public void setPassiveDescription(String passiveDescription) {
        this.passiveDescription = passiveDescription;
    }

    public String getPartype() {
        return partype;
    }

    public void setPartype(String partype) {
        this.partype = partype;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public void setSpells(List<Spell> spells) {
        this.spells = spells;
    }
}
