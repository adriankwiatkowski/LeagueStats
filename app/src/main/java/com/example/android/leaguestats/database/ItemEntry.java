package com.example.android.leaguestats.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.RoomWarnings;
import android.support.annotation.Nullable;

import java.util.List;

@Entity(tableName = "item")
public class ItemEntry {

    @PrimaryKey
    private int id;
    private String name;

    @ColumnInfo(name = "plain_text")
    private String plainText;
    private String description;
    private int depth;

    private String image;

    @ColumnInfo(name = "item_into")
    private List<String> into;
    @ColumnInfo(name = "item_from")
    private List<String> from;

    @ColumnInfo(name = "base_gold")
    private int baseGold;
    @ColumnInfo(name = "total_gold")
    private int totalGold;
    @ColumnInfo(name = "sell_gold")
    private int sellGold;
    private String purchasable;

    @ColumnInfo(name = "flat_armor_mod")
    private double flatArmorMod;
    @ColumnInfo(name = "flat_spell_block_mod")
    private double flatSpellBlockMod;
    @ColumnInfo(name = "flat_hp_pool_mod")
    private double flatHPPoolMod;
    @ColumnInfo(name = "flat_mp_pool_mod")
    private double flatMPPoolMod;
    @ColumnInfo(name = "flat_hp_regen_mod")
    private double flatHPRegenMod;
    @ColumnInfo(name = "flat_crit_chance_mod")
    private double flatCritChanceMod;
    @ColumnInfo(name = "flat_magic_damage_mod")
    private double flatMagicDamageMod;
    @ColumnInfo(name = "flat_physical_damage_mod")
    private double flatPhysicalDamageMod;
    @ColumnInfo(name = "flat_movement_speed_mod")
    private double flatMovementSpeedMod;
    @ColumnInfo(name = "percent_movement_speed_mod")
    private double percentMovementSpeedMod;
    @ColumnInfo(name = "percent_attack_speed_mod")
    private double percentAttackSpeedMod;
    @ColumnInfo(name = "percent_life_steal_mod")
    private double percentLifeStealMod;

    public ItemEntry(int id, String name, String plainText, String description, int depth,
                     String image, List<String> into, List<String> from, int baseGold, int totalGold,
                     int sellGold, String purchasable, double flatArmorMod, double flatSpellBlockMod,
                     double flatHPPoolMod, double flatMPPoolMod, double flatHPRegenMod,
                     double flatCritChanceMod, double flatMagicDamageMod, double flatPhysicalDamageMod,
                     double flatMovementSpeedMod, double percentMovementSpeedMod,
                     double percentAttackSpeedMod, double percentLifeStealMod) {
        this.id = id;
        this.name = name;
        this.plainText = plainText;
        this.description = description;
        this.depth = depth;
        this.image = image;
        this.into = into;
        this.from = from;
        this.baseGold = baseGold;
        this.totalGold = totalGold;
        this.sellGold = sellGold;
        this.purchasable = purchasable;
        this.flatArmorMod = flatArmorMod;
        this.flatSpellBlockMod = flatSpellBlockMod;
        this.flatHPPoolMod = flatHPPoolMod;
        this.flatMPPoolMod = flatMPPoolMod;
        this.flatHPRegenMod = flatHPRegenMod;
        this.flatCritChanceMod = flatCritChanceMod;
        this.flatMagicDamageMod = flatMagicDamageMod;
        this.flatPhysicalDamageMod = flatPhysicalDamageMod;
        this.flatMovementSpeedMod = flatMovementSpeedMod;
        this.percentMovementSpeedMod = percentMovementSpeedMod;
        this.percentAttackSpeedMod = percentAttackSpeedMod;
        this.percentLifeStealMod = percentLifeStealMod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlainText() {
        return plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getInto() {
        return into;
    }

    public void setInto(List<String> into) {
        this.into = into;
    }

    public List<String> getFrom() {
        return from;
    }

    public void setFrom(List<String> from) {
        this.from = from;
    }

    public int getBaseGold() {
        return baseGold;
    }

    public void setBaseGold(int baseGold) {
        this.baseGold = baseGold;
    }

    public int getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(int totalGold) {
        this.totalGold = totalGold;
    }

    public int getSellGold() {
        return sellGold;
    }

    public void setSellGold(int sellGold) {
        this.sellGold = sellGold;
    }

    public String getPurchasable() {
        return purchasable;
    }

    public void setPurchasable(String purchasable) {
        this.purchasable = purchasable;
    }

    public double getFlatArmorMod() {
        return flatArmorMod;
    }

    public void setFlatArmorMod(double flatArmorMod) {
        this.flatArmorMod = flatArmorMod;
    }

    public double getFlatSpellBlockMod() {
        return flatSpellBlockMod;
    }

    public void setFlatSpellBlockMod(double flatSpellBlockMod) {
        this.flatSpellBlockMod = flatSpellBlockMod;
    }

    public double getFlatHPPoolMod() {
        return flatHPPoolMod;
    }

    public void setFlatHPPoolMod(double flatHPPoolMod) {
        this.flatHPPoolMod = flatHPPoolMod;
    }

    public double getFlatMPPoolMod() {
        return flatMPPoolMod;
    }

    public void setFlatMPPoolMod(double flatMPPoolMod) {
        this.flatMPPoolMod = flatMPPoolMod;
    }

    public double getFlatHPRegenMod() {
        return flatHPRegenMod;
    }

    public void setFlatHPRegenMod(double flatHPRegenMod) {
        this.flatHPRegenMod = flatHPRegenMod;
    }

    public double getFlatCritChanceMod() {
        return flatCritChanceMod;
    }

    public void setFlatCritChanceMod(double flatCritChanceMod) {
        this.flatCritChanceMod = flatCritChanceMod;
    }

    public double getFlatMagicDamageMod() {
        return flatMagicDamageMod;
    }

    public void setFlatMagicDamageMod(double flatMagicDamageMod) {
        this.flatMagicDamageMod = flatMagicDamageMod;
    }

    public double getFlatPhysicalDamageMod() {
        return flatPhysicalDamageMod;
    }

    public void setFlatPhysicalDamageMod(double flatPhysicalDamageMod) {
        this.flatPhysicalDamageMod = flatPhysicalDamageMod;
    }

    public double getFlatMovementSpeedMod() {
        return flatMovementSpeedMod;
    }

    public void setFlatMovementSpeedMod(double flatMovementSpeedMod) {
        this.flatMovementSpeedMod = flatMovementSpeedMod;
    }

    public double getPercentMovementSpeedMod() {
        return percentMovementSpeedMod;
    }

    public void setPercentMovementSpeedMod(double mPercentMovementSpeedMod) {
        this.percentMovementSpeedMod = mPercentMovementSpeedMod;
    }

    public double getPercentAttackSpeedMod() {
        return percentAttackSpeedMod;
    }

    public void setPercentAttackSpeedMod(double percentAttackSpeedMod) {
        this.percentAttackSpeedMod = percentAttackSpeedMod;
    }

    public double getPercentLifeStealMod() {
        return percentLifeStealMod;
    }

    public void setPercentLifeStealMod(double percentLifeStealMod) {
        this.percentLifeStealMod = percentLifeStealMod;
    }
}
