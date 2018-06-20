package com.example.android.leaguestats.models;

import java.util.ArrayList;
import java.util.List;

public class Champion {

    private long mId;

    private String mName;
    private String mKey;
    private String mTitle;
    private String mLore;

    private String mThumbnail;
    private ArrayList<String> mSplashArt;
    private ArrayList<String> mSplashArtName;

    private int mDifficulty;
    private int mAttack;
    private int mDefense;
    private int mMagic;

    private ArrayList<String> mEnemyTips;
    private ArrayList<String> mAllyTips;

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

    private List<String> mSpellName;
    private List<String> mSpellDescription;
    private List<String> mSpellImage;
    private List<String> mSpellResource;
    private List<String> mSpellCooldownArray;
    private List<String> mSpellCostArray;

    public Champion(long id, String name, String key, String title, String lore, String thumbnail,
                    ArrayList<String> splashArt, ArrayList<String> splashArtName, int difficulty,
                    int attack, int defense, int magic, ArrayList<String> enemyTips,
                    ArrayList<String> allyTips, double attackDamage, double attackDamagePerLevel,
                    double attackRange, double armor, double armorPerLevel, double health,
                    double healthPerLevel, double healthRegen, double healthRegenPerLevel,
                    double mana, double manaPerLevel, double manaRegen, double manaRegenPerLevel,
                    double attackSpeedOffset, double attackSpeedPerLevel, double moveSpeed,
                    double crit, double critPerLevel, double magicResist, double magicResistPerLevel,
                    List<String> spellName, List<String> spellDescription, List<String> spellImage,
                    List<String> spellResource, ArrayList<String> spellCooldown, ArrayList<String> spellCost) {

        mId = id;
        mName = name;
        mKey = key;
        mTitle = title;
        mLore = lore;
        mThumbnail = thumbnail;
        mSplashArt = splashArt;
        mSplashArtName = splashArtName;
        mDifficulty = difficulty;
        mAttack = attack;
        mDefense = defense;
        mMagic = magic;
        mEnemyTips = enemyTips;
        mAllyTips = allyTips;

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

        this.mSpellName = spellName;
        this.mSpellDescription = spellDescription;
        this.mSpellImage = spellImage;
        this.mSpellResource = spellResource;
        this.mSpellCooldownArray = spellCooldown;
        this.mSpellCostArray = spellCost;
    }

    public long getId() {
        return mId;
    }

    public String getChampionName() {
        return mName;
    }

    public String getChampionTitle() {
        return mTitle;
    }

    public String getChampionLore() {
        return mLore;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setLore(String lore) {
        this.mLore = lore;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public ArrayList<String> getSplashArt() {
        return mSplashArt;
    }

    public void setSplashArt(ArrayList<String> splashArt) {
        this.mSplashArt = splashArt;
    }

    public ArrayList<String> getSplashArtName() {
        return mSplashArtName;
    }

    public void setSplashArtName(ArrayList<String> splashArtName) {
        this.mSplashArtName = splashArtName;
    }

    public int getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(int difficulty) {
        this.mDifficulty = difficulty;
    }

    public int getAttack() {
        return mAttack;
    }

    public void setAttack(int attack) {
        this.mAttack = attack;
    }

    public int getDefense() {
        return mDefense;
    }

    public void setDefense(int defense) {
        this.mDefense = defense;
    }

    public int getMagic() {
        return mMagic;
    }

    public void setMagic(int magic) {
        this.mMagic = magic;
    }

    public ArrayList<String> getEnemyTips() {
        return mEnemyTips;
    }

    public void setEnemyTips(ArrayList<String> enemyTips) {
        this.mEnemyTips = enemyTips;
    }

    public ArrayList<String> getAllyTips() {
        return mAllyTips;
    }

    public void setAllyTips(ArrayList<String> allyTips) {
        this.mAllyTips = allyTips;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
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

    public List<String> getSpellName() {
        return mSpellName;
    }

    public void setSpellName(List<String> name) {
        this.mSpellName = name;
    }

    public List<String> getSpellDescription() {
        return mSpellDescription;
    }

    public void setSpellDescription(List<String> description) {
        this.mSpellDescription = description;
    }

    public List<String> getSpellImage() {
        return mSpellImage;
    }

    public void setSpellImage(List<String> image) {
        this.mSpellImage = image;
    }

    public List<String> getSpellCooldownList() {
        return mSpellCooldownArray;
    }

    public void setSpellCooldownList(List<String> cooldown) {
        this.mSpellCooldownArray = cooldown;
    }

    public List<String> getSpellCostList() {
        return mSpellCostArray;
    }

    public void setSpellCostList(List<String> cost) {
        this.mSpellCostArray = cost;
    }

    public List<String> getSpellResource() {
        return mSpellResource;
    }

    public void setSpellResource(List<String> resource) {
        this.mSpellResource = resource;
    }
}
