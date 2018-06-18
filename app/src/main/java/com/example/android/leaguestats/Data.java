package com.example.android.leaguestats;

public class Data {

    public static final String API_KEY = "api_key";
    public static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;

    public static final String REQUEST_METHOD_GET = "GET";

    public static final String ENTRY_URL_SUMMONER_EUNE = "https://eun1.api.riotgames.com/lol/summoner/v3/summoners/by-name";
    public static final String ENTRY_URL_SUMMONER_EUW = "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name";
    public static final String ENTRY_URL_SUMMONER_NA = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name";
    public static final String[] ENTRY_URL_SUMMONER_ARRAY = {ENTRY_URL_SUMMONER_EUNE, ENTRY_URL_SUMMONER_EUW, ENTRY_URL_SUMMONER_NA};

    public static final String ENTRY_URL_MASTERY_EUNE = "https://eun1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries";
    public static final String ENTRY_URL_MASTERY_EUW = "https://euw1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries";
    public static final String ENTRY_URL_MASTERY_NA = "https://na1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries";
    public static final String[] ENTRY_URL_MASTERY_ARRAY = {ENTRY_URL_MASTERY_EUNE, ENTRY_URL_MASTERY_EUW, ENTRY_URL_MASTERY_NA};

    public static final String[] CHAMPION_NAME_ARRAY = new String[]{"Jax", "Sona", "Tristana",
            "Varus", "Kaisa", "Fiora", "Singed", "TahmKench", "Leblanc", "Thresh", "Karma", "Jhin",
            "Rumble", "Udyr", "LeeSin", "Yorick", "Ornn", "Kayn", "Kassadin", "Sivir", "MissFortune",
            "Draven", "Yasuo", "Kayle", "Shaco", "Renekton", "Hecarim", "Fizz", "KogMaw", "Maokai",
            "Lissandra", "Jinx", "Urgot", "Fiddlesticks", "Galio", "Pantheon", "Talon", "Gangplank",
            "Ezreal", "Gnar", "Teemo", "Annie", "Mordekaiser", "Azir", "Kennen", "Riven", "Chogath",
            "Aatrox", "Poppy", "Taliyah", "Illaoi", "Pyke", "Heimerdinger", "Alistar", "XinZhao",
            "Lucian", "Volibear", "Sejuani", "Nidalee", "Garen", "Leona", "Zed", "Blitzcrank",
            "Rammus", "Velkoz", "Caitlyn", "Trundle", "Kindred", "Quinn", "Ekko", "Nami", "Swain",
            "Taric", "Syndra", "Rakan", "Skarner", "Braum", "Veigar", "Xerath", "Corki", "Nautilus",
            "Ahri", "Jayce", "Darius", "Tryndamere", "Janna", "Elise", "Vayne", "Brand", "Zoe",
            "Graves", "Soraka", "Xayah", "Karthus", "Vladimir", "Zilean", "Katarina", "Shyvana",
            "Warwick", "Ziggs", "Kled", "Khazix", "Olaf", "TwistedFate", "Nunu", "Rengar", "Bard",
            "Irelia", "Ivern", "MonkeyKing", "Ashe", "Kalista", "Akali", "Vi", "Amumu", "Lulu",
            "Morgana", "Nocturne", "Diana", "AurelionSol", "Zyra", "Viktor", "Cassiopeia", "Nasus",
            "Twitch", "DrMundo", "Orianna", "Evelynn", "RekSai", "Lux", "Sion", "Camille", "MasterYi",
            "Ryze", "Malphite", "Anivia", "Shen", "JarvanIV", "Malzahar", "Zac", "Gragas"};
}
