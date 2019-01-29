package com.example.android.leaguestats.data.glide;

public class GlideUtils {

    private static final String DDRAGON_ENTRY_URL = "http://ddragon.leagueoflegends.com/cdn/";
    private static final String CHAMPION_PATH = "/img/champion/";
    private static final String SPLASH_ART_PATH = "img/champion/splash/";
    private static final String ITEM_PATH = "/img/item/";
    private static final String SPELL_PATH = "/img/spell/";
    private static final String PROFILE_ICON_PATH = "/img/profileicon/";

    public static String getChampionUrl(String patchVersion, String championImageId) {
        return DDRAGON_ENTRY_URL + patchVersion + CHAMPION_PATH + championImageId;
    }

    public static String getSplashArtUrl(String championKey, int splashArtId) {
        return DDRAGON_ENTRY_URL + SPLASH_ART_PATH + championKey + "_" + splashArtId + ".jpg";
    }

    public static String getItemUrl(String patchVersion, String itemImageId) {
        return DDRAGON_ENTRY_URL + patchVersion + ITEM_PATH + itemImageId;
    }

    public static String getSpellUrl(String patchVersion, String spellImageId) {
        return DDRAGON_ENTRY_URL + patchVersion + SPELL_PATH + spellImageId;
    }

    public static String getProfileIconUrl(String patchVersion, int profileIconId) {
        return DDRAGON_ENTRY_URL + patchVersion + PROFILE_ICON_PATH + profileIconId + ".png";
    }
}
