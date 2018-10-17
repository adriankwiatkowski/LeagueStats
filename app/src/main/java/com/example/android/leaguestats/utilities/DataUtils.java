package com.example.android.leaguestats.utilities;

public class DataUtils {

    private static final String ENTRY_URL_SUMMONER_EUNE = "https://eun1.api.riotgames.com";
    private static final String ENTRY_URL_SUMMONER_EUW = "https://euw1.api.riotgames.com";
    private static final String ENTRY_URL_SUMMONER_NA = "https://na1.api.riotgames.com";
    public static final String[] ENTRY_URL_SUMMONER_ARRAY = {ENTRY_URL_SUMMONER_EUNE, ENTRY_URL_SUMMONER_EUW, ENTRY_URL_SUMMONER_NA};

    public static String getEntryUrl(String platformId) {
        String entryUrlString = "https://";
        switch (platformId) {
            case "BR1":
                entryUrlString += "br1.api.riotgames.com";
                break;
            case "EUN1":
                entryUrlString += "eun1.api.riotgames.com";
                break;
            case "EUW1":
                entryUrlString += "euw1.api.riotgames.com";
                break;
            case "JP1":
                entryUrlString += "jp1.api.riotgames.com";
                break;
            case "KR":
                entryUrlString += "kr.api.riotgames.com";
                break;
            case "LA1":
                entryUrlString += "la1.api.riotgames.com";
                break;
            case "LA2":
                entryUrlString += "la2.api.riotgames.com";
                break;
            case "NA1":
                entryUrlString += "na1.api.riotgames.com";
                break;
            case "OC1":
                entryUrlString += "oc1.api.riotgames.com";
                break;
            case "TR1":
                entryUrlString += "tr1.api.riotgames.com";
                break;
            case "RU":
                entryUrlString += "ru.api.riotgames.com";
                break;
            case "PBE1":
                entryUrlString += "pbe1.api.riotgames.com";
                break;
            default:
                throw new IllegalArgumentException("Unknown platformId: " + platformId);
        }
        return entryUrlString;
    }
}
