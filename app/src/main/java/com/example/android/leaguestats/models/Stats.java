package com.example.android.leaguestats.models;

public class Stats {

    private double mAttackDamage;
    private double mAttackDamagePerLevel;
    private double mAttackRange;
    private double mArmor;
    private double mArmorPerLevel;
    private double mHealth;
    private double mHealthPerLevel;
    private double mHealthRegen;
    private double mHealthRegenPerLevel;
    private double mMana;
    private double mManaPerLevel;
    private double mManaRegen;
    private double mManaRegenPerLevel;
    private double mAttackSpeedOffset;
    private double mAttackSpeedPerLevel;
    private double mMoveSpeed;
    private double mCrit;
    private double mCritPerLevel;
    private double mMagicResist;
    private double mMagicResistPerLevel;

    public Stats(double attackDamage, double attackDamagePerLevel, double attackRange, double armor,
                 double armorPerLevel, double health, double healthPerLevel, double healthRegen,
                 double healthRegenPerLevel, double mana, double manaPerLevel, double manaRegen,
                 double manaRegenPerLevel, double attackSpeedOffset, double attackSpeedPerLevel,
                 double moveSpeed, double crit, double critPerLevel, double magicResist,
                 double magicResistPerLevel) {

        this.mAttackDamage = attackDamage;
        this.mAttackDamagePerLevel = attackDamagePerLevel;
        this.mAttackRange = attackRange;
        this.mArmor = armor;
        this.mArmorPerLevel = armorPerLevel;
        this.mHealth = health;
        this.mHealthPerLevel = healthPerLevel;
        this.mHealthRegen = healthRegen;
        this.mHealthRegenPerLevel = healthRegenPerLevel;
        this.mMana = mana;
        this.mManaPerLevel = manaPerLevel;
        this.mManaRegen = manaRegen;
        this.mManaRegenPerLevel = manaRegenPerLevel;
        this.mAttackSpeedOffset = attackSpeedOffset;
        this.mAttackSpeedPerLevel = attackSpeedPerLevel;
        this.mMoveSpeed = moveSpeed;
        this.mCrit = crit;
        this.mCritPerLevel = critPerLevel;
        this.mMagicResist = magicResist;
        this.mMagicResistPerLevel = magicResistPerLevel;
    }

    public double getAttackDamage() {
        return mAttackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.mAttackDamage = attackDamage;
    }

    public double getAttackDamagePerLevel() {
        return mAttackDamagePerLevel;
    }

    public void setAttackDamagePerLevel(double attackDamagePerLevel) {
        this.mAttackDamagePerLevel = attackDamagePerLevel;
    }

    public double getAttackRange() {
        return mAttackRange;
    }

    public void setAttackRange(double attackRange) {
        this.mAttackRange = attackRange;
    }

    public double getArmor() {
        return mArmor;
    }

    public void setArmor(double armor) {
        this.mArmor = armor;
    }

    public double getArmorPerLevel() {
        return mArmorPerLevel;
    }

    public void setArmorPerLevel(double armorPerLevel) {
        this.mArmorPerLevel = armorPerLevel;
    }

    public double getHealth() {
        return mHealth;
    }

    public void setHealth(double health) {
        this.mHealth = health;
    }

    public double getHealthPerLevel() {
        return mHealthPerLevel;
    }

    public void setHealthPerLevel(double healthPerLevel) {
        this.mHealthPerLevel = healthPerLevel;
    }

    public double getHealthRegen() {
        return mHealthRegen;
    }

    public void setHealthRegen(double healthRegen) {
        this.mHealthRegen = healthRegen;
    }

    public double getHealthRegenPerLevel() {
        return mHealthRegenPerLevel;
    }

    public void setHealthRegenPerLevel(double healthRegenPerLevel) {
        this.mHealthRegenPerLevel = healthRegenPerLevel;
    }

    public double getMana() {
        return mMana;
    }

    public void setMana(double mana) {
        this.mMana = mana;
    }

    public double getManaPerLevel() {
        return mManaPerLevel;
    }

    public void setManaPerLevel(double manaPerLevel) {
        this.mManaPerLevel = manaPerLevel;
    }

    public double getManaRegen() {
        return mManaRegen;
    }

    public void setManaRegen(double manaRegen) {
        this.mManaRegen = manaRegen;
    }

    public double getManaRegenPerLevel() {
        return mManaRegenPerLevel;
    }

    public void setManaRegenPerLevel(double manaRegenPerLevel) {
        this.mManaRegenPerLevel = manaRegenPerLevel;
    }

    public double getAttackSpeedOffset() {
        return mAttackSpeedOffset;
    }

    public void setAttackSpeedOffset(double attackSpeedOffset) {
        this.mAttackSpeedOffset = attackSpeedOffset;
    }

    public double getAttackSpeedPerLevel() {
        return mAttackSpeedPerLevel;
    }

    public void setAttackSpeedPerLevel(double attackSpeedPerLevel) {
        this.mAttackSpeedPerLevel = attackSpeedPerLevel;
    }

    public double getMoveSpeed() {
        return mMoveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.mMoveSpeed = moveSpeed;
    }

    public double getCrit() {
        return mCrit;
    }

    public void setCrit(double crit) {
        this.mCrit = crit;
    }

    public double getCritPerLevel() {
        return mCritPerLevel;
    }

    public void setCritPerLevel(double critPerLevel) {
        this.mCritPerLevel = critPerLevel;
    }

    public double getMagicResistPerLevel() {
        return mMagicResistPerLevel;
    }

    public void setMagicResistPerLevel(double magicResistPerLevel) {
        this.mMagicResistPerLevel = magicResistPerLevel;
    }

    public double getMagicResist() {
        return mMagicResist;
    }

    public void setMagicResist(double magicResist) {
        this.mMagicResist = magicResist;
    }
}