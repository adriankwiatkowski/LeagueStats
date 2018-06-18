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

    private ArrayList<Stats> mStats;

    private ArrayList<Spell> mSpell;

    public Champion(long id, String name, String key, String title, String lore, String thumbnail,
                    ArrayList<String> splashArt, ArrayList<String> splashArtName, int difficulty,
                    int attack, int defense, int magic, ArrayList<String> enemyTips,
                    ArrayList<String> allyTips, ArrayList<Stats> stats, ArrayList<Spell> spell) {

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
        mStats = stats;
        mSpell = spell;
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

    public ArrayList<Stats> getStats() {
        return mStats;
    }

    public void setStats(ArrayList<Stats> stats) {
        this.mStats = stats;
    }

    public ArrayList<Spell> getSpell() {
        return mSpell;
    }

    public void setSpell(ArrayList<Spell> spell) {
        this.mSpell = spell;
    }
}
