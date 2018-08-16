package com.example.android.leaguestats.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "champion")
public class ChampionEntry {

    @PrimaryKey
    private long id;
    private String name;
    @ColumnInfo(name = "champion_key")
    private String key;
    private String title;
    private String lore;

    private String thumbnail;
    @ColumnInfo(name = "splash_art")
    private List<String> splashArt;
    @ColumnInfo(name = "splash_art_name")
    private List<String> splashArtName;

    private int difficulty;
    private int attack;
    private int defense;
    private int magic;

    @ColumnInfo(name = "vs_tips")
    private List<String> vsTips;
    @ColumnInfo(name = "as_tips")
    private List<String> asTips;

    @ColumnInfo(name = "attack_damage")
    private double attackDamage;
    @ColumnInfo(name = "attack_damage_per_level")
    private double attackDamagePerLevel;
    @ColumnInfo(name = "attack_range")
    private double attackRange;
    private double armor;
    @ColumnInfo(name = "armor_per_level")
    private double armorPerLevel;
    private double health;
    @ColumnInfo(name = "health_per_level")
    private double healthPerLevel;
    @ColumnInfo(name = "health_regen")
    private double healthRegen;
    @ColumnInfo(name = "health_regen_per_level")
    private double healthRegenPerLevel;
    private double mana;
    @ColumnInfo(name = "mana_per_level")
    private double manaPerLevel;
    @ColumnInfo(name = "mana_regen")
    private double manaRegen;
    @ColumnInfo(name = "mana_regen_per_level")
    private double manaRegenPerLevel;
    @ColumnInfo(name = "attack_speed_offset")
    private double attackSpeedOffset;
    @ColumnInfo(name = "attack_speed_per_level")
    private double attackSpeedPerLevel;
    @ColumnInfo(name = "move_speed")
    private double moveSpeed;
    private double crit;
    @ColumnInfo(name = "crit_per_level")
    private double critPerLevel;
    @ColumnInfo(name = "magic_resist")
    private double magicResist;
    @ColumnInfo(name = "magic_resist_per_level")
    private double magicResistPerLevel;

    @ColumnInfo(name = "spell_name")
    private List<String> spellName;
    @ColumnInfo(name = "spell_description")
    private List<String> spellDescription;
    @ColumnInfo(name = "spell_image")
    private List<String> spellImage;
    @ColumnInfo(name = "spell_resource")
    private List<String> spellResource;
    @ColumnInfo(name = "spell_cooldown_array")
    private List<Double> spellCooldown;
    @ColumnInfo(name = "spell_cost_array")
    private List<Double> spellCost;
    @ColumnInfo(name = "spell_max_rank")
    private List<Integer> spellMaxRank;

    public ChampionEntry(long id, String name, String key, String title, String lore, String thumbnail,
                         List<String> splashArt, List<String> splashArtName, int difficulty, int attack,
                         int defense, int magic, List<String> vsTips, List<String> asTips, double attackDamage,
                         double attackDamagePerLevel, double attackRange, double armor,
                         double armorPerLevel, double health, double healthPerLevel, double healthRegen,
                         double healthRegenPerLevel, double mana, double manaPerLevel, double manaRegen,
                         double manaRegenPerLevel, double attackSpeedOffset, double attackSpeedPerLevel,
                         double moveSpeed, double crit, double critPerLevel, double magicResist,
                         double magicResistPerLevel, List<String> spellName, List<String> spellDescription,
                         List<String> spellImage, List<String> spellResource, List<Double> spellCooldown,
                         List<Double> spellCost, List<Integer> spellMaxRank) {

        this.id = id;
        this.name = name;
        this.key = key;
        this.title = title;
        this.lore = lore;
        this.thumbnail = thumbnail;
        this.splashArt = splashArt;
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

        this.spellName = spellName;
        this.spellDescription = spellDescription;
        this.spellImage = spellImage;
        this.spellResource = spellResource;
        this.spellCooldown = spellCooldown;
        this.spellCost = spellCost;
        this.spellMaxRank = spellMaxRank;
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

    public long getId() {
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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getSplashArt() {
        return splashArt;
    }

    public void setSplashArt(List<String> splashArt) {
        this.splashArt = splashArt;
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

    public List<String> getSpellName() {
        return spellName;
    }

    public void setSpellName(List<String> name) {
        this.spellName = name;
    }

    public List<String> getSpellDescription() {
        return spellDescription;
    }

    public void setSpellDescription(List<String> description) {
        this.spellDescription = description;
    }

    public List<String> getSpellImage() {
        return spellImage;
    }

    public void setSpellImage(List<String> image) {
        this.spellImage = image;
    }

    public List<Double> getSpellCooldown() {
        return spellCooldown;
    }

    public void setSpellCooldown(List<Double> cooldown) {
        this.spellCooldown = cooldown;
    }

    public List<Double> getSpellCost() {
        return spellCost;
    }

    public void setSpellCost(List<Double> cost) {
        this.spellCost = cost;
    }

    public List<String> getSpellResource() {
        return spellResource;
    }

    public void setSpellResource(List<String> resource) {
        this.spellResource = resource;
    }

    public List<Integer> getSpellMaxRank() {
        return spellMaxRank;
    }

    public void setSpellMaxRank(List<Integer> spellMaxRank) {
        this.spellMaxRank = spellMaxRank;
    }
}
