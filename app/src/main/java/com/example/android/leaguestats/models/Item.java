package com.example.android.leaguestats.models;

public class Item {

    private int mId;
    private String mName;

    private String mPlainText;
    private String mDescription;
    private int mDepth;

    private String mImage;

    private String mInto;
    private String mFrom;

    private int mBaseGold;
    private int mTotalGold;
    private int mSellGold;
    private String mPurchasable;

    private double mFlatArmorMod;
    private double mFlatSpellBlockMod;
    private double mFlatHPPoolMod;
    private double mFlatMPPoolMod;
    private double mFlatHPRegenMod;
    private double mFlatCritChanceMod;
    private double mFlatMagicDamageMod;
    private double mFlatPhysicalDamageMod;
    private double mFlatMovementSpeedMod;
    private double mPercentMovementSpeedMod;
    private double mPercentAttackSpeedMod;
    private double mPercentLifeStealMod;

    public Item(int id, String name, String plainText, String description, int depth, String image,
                String into, String from, int baseGold, int totalGold, int sellGold,
                String purchasable, double flatArmorMod, double flatSpellBlockMod,
                double flatHPPoolMod, double flatMPPoolMod, double flatHPRegenMod,
                double flatCritChanceMod, double flatMagicDamageMod, double flatPhysicalDamageMod,
                double flatMovementSpeedMod, double percentMovementSpeedMod,
                double percentAttackSpeedMod, double percentLifeStealMod) {
        this.mId = id;
        this.mName = name;
        this.mPlainText = plainText;
        this.mDescription = description;
        this.mDepth = depth;
        this.mImage = image;
        this.mInto = into;
        this.mFrom = from;
        this.mBaseGold = baseGold;
        this.mTotalGold = totalGold;
        this.mSellGold = sellGold;
        this.mPurchasable = purchasable;
        this.mFlatArmorMod = flatArmorMod;
        this.mFlatSpellBlockMod = flatSpellBlockMod;
        this.mFlatHPPoolMod = flatHPPoolMod;
        this.mFlatMPPoolMod = flatMPPoolMod;
        this.mFlatHPRegenMod = flatHPRegenMod;
        this.mFlatCritChanceMod = flatCritChanceMod;
        this.mFlatMagicDamageMod = flatMagicDamageMod;
        this.mFlatPhysicalDamageMod = flatPhysicalDamageMod;
        this.mFlatMovementSpeedMod = flatMovementSpeedMod;
        this.mPercentMovementSpeedMod = percentMovementSpeedMod;
        this.mPercentAttackSpeedMod = percentAttackSpeedMod;
        this.mPercentLifeStealMod = percentLifeStealMod;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPlainText() {
        return mPlainText;
    }

    public void setPlainText(String plainText) {
        this.mPlainText = plainText;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public int getDepth() {
        return mDepth;
    }

    public void setDepth(int depth) {
        this.mDepth = depth;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getInto() {
        return mInto;
    }

    public void setInto(String into) {
        this.mInto = into;
    }

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        this.mFrom = from;
    }

    public int getBaseGold() {
        return mBaseGold;
    }

    public void setBaseGold(int baseGold) {
        this.mBaseGold = baseGold;
    }

    public int getTotalGold() {
        return mTotalGold;
    }

    public void setTotalGold(int totalGold) {
        this.mTotalGold = totalGold;
    }

    public int getSellGold() {
        return mSellGold;
    }

    public void setSellGold(int sellGold) {
        this.mSellGold = sellGold;
    }

    public String getPurchasable() {
        return mPurchasable;
    }

    public void setPurchasable(String purchasable) {
        this.mPurchasable = purchasable;
    }

    public double getFlatArmorMod() {
        return mFlatArmorMod;
    }

    public void setFlatArmorMod(double flatArmorMod) {
        this.mFlatArmorMod = flatArmorMod;
    }

    public double getFlatSpellBlockMod() {
        return mFlatSpellBlockMod;
    }

    public void setFlatSpellBlockMod(double flatSpellBlockMod) {
        this.mFlatSpellBlockMod = flatSpellBlockMod;
    }

    public double getFlatHPPoolMod() {
        return mFlatHPPoolMod;
    }

    public void setFlatHPPoolMod(double flatHPPoolMod) {
        this.mFlatHPPoolMod = flatHPPoolMod;
    }

    public double getFlatMPPoolMod() {
        return mFlatMPPoolMod;
    }

    public void setFlatMPPoolMod(double flatMPPoolMod) {
        this.mFlatMPPoolMod = flatMPPoolMod;
    }

    public double getFlatHPRegenMod() {
        return mFlatHPRegenMod;
    }

    public void setFlatHPRegenMod(double flatHPRegenMod) {
        this.mFlatHPRegenMod = flatHPRegenMod;
    }

    public double getFlatCritChanceMod() {
        return mFlatCritChanceMod;
    }

    public void setFlatCritChanceMod(double flatCritChanceMod) {
        this.mFlatCritChanceMod = flatCritChanceMod;
    }

    public double getFlatMagicDamageMod() {
        return mFlatMagicDamageMod;
    }

    public void setFlatMagicDamageMod(double flatMagicDamageMod) {
        this.mFlatMagicDamageMod = flatMagicDamageMod;
    }

    public double getFlatPhysicalDamageMod() {
        return mFlatPhysicalDamageMod;
    }

    public void setFlatPhysicalDamageMod(double flatPhysicalDamageMod) {
        this.mFlatPhysicalDamageMod = flatPhysicalDamageMod;
    }

    public double getFlatMovementSpeedMod() {
        return mFlatMovementSpeedMod;
    }

    public void setFlatMovementSpeedMod(double flatMovementSpeedMod) {
        this.mFlatMovementSpeedMod = flatMovementSpeedMod;
    }

    public double getmPercentMovementSpeedMod() {
        return mPercentMovementSpeedMod;
    }

    public void setmPercentMovementSpeedMod(double mPercentMovementSpeedMod) {
        this.mPercentMovementSpeedMod = mPercentMovementSpeedMod;
    }

    public double getPercentAttackSpeedMod() {
        return mPercentAttackSpeedMod;
    }

    public void setPercentAttackSpeedMod(double percentAttackSpeedMod) {
        this.mPercentAttackSpeedMod = percentAttackSpeedMod;
    }

    public double getPercentLifeStealMod() {
        return mPercentLifeStealMod;
    }

    public void setPercentLifeStealMod(double percentLifeStealMod) {
        this.mPercentLifeStealMod = percentLifeStealMod;
    }
}
