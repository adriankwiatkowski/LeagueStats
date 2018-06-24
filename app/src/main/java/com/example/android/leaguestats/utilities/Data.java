package com.example.android.leaguestats.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Icon;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private static final String LOG_TAG = Data.class.getSimpleName();

    private static final String STRING_DIVIDER = "_,_";

    public static final String API_KEY = "api_key";
    public static final String PERSONAL_API_KEY = BuildConfig.HIDDEN_API_KEY;

    public static final String REQUEST_METHOD_GET = "GET";

    public static final String ENTRY_URL_SUMMONER_EUNE = "https://eun1.api.riotgames.com";
    public static final String ENTRY_URL_SUMMONER_EUW = "https://euw1.api.riotgames.com";
    public static final String ENTRY_URL_SUMMONER_NA = "https://na1.api.riotgames.com";
    public static final String[] ENTRY_URL_SUMMONER_ARRAY = {ENTRY_URL_SUMMONER_EUNE, ENTRY_URL_SUMMONER_EUW, ENTRY_URL_SUMMONER_NA};

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

    public static void saveIconData(Context context, List<Icon> icons) {
        ContentValues[] contentValues = new ContentValues[icons.size()];

        for (int i = 0; i < icons.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(Contract.IconEntry.COLUMN_ICON, icons.get(i).getIconPath());
            values.put(Contract.IconEntry.COLUMN_ICON_ID, icons.get(i).getIconId());

            contentValues[i] = values;
        }

        /*
        QueryHandler queryHandler = new QueryHandler(context.getContentResolver());
        for (int i = 0; i < contentValues.length; i++) {
            queryHandler.startInsert(i, null, Contract.IconEntry.CONTENT_URI, contentValues[i]);
        }
        */
        context.getContentResolver().bulkInsert(Contract.IconEntry.CONTENT_URI, contentValues);
    }

    public static void saveChampionData(Context context, ArrayList<Champion> champions) {
        ContentValues[] championContentValues = new ContentValues[champions.size()];

        for (int i = 0; i < champions.size(); i++) {
            ContentValues values = new ContentValues();

            // Save Champion ID and Key.
            values.put(Contract.ChampionEntry._ID, champions.get(i).getId());
            values.put(Contract.ChampionEntry.COLUMN_KEY, champions.get(i).getKey());

            // Save Champion Base Info.
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_NAME, champions.get(i).getChampionName());
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_TITLE, champions.get(i).getChampionTitle());
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_LORE, champions.get(i).getChampionLore());
            values.put(Contract.ChampionEntry.COLUMN_KEY, champions.get(i).getKey());

            // Save Thumbnail.
            values.put(Contract.ChampionEntry.COLUMN_THUMBNAIL, champions.get(i).getThumbnail());

            // Save Splash Art Path.
            List<String> splashArtList = champions.get(i).getSplashArt();
            String splashArtString = stringListToString(splashArtList);
            values.put(Contract.ChampionEntry.COLUMN_SPLASH_ART, splashArtString);

            // Save Splash Art Name.
            List<String> splashArtNameList = champions.get(i).getSplashArtName();
            String splashArtNameString = stringListToString(splashArtNameList);
            values.put(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME, splashArtNameString);

            // Save Champion Info.
            values.put(Contract.ChampionEntry.COLUMN_DIFFICULTY, champions.get(i).getDifficulty());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK, champions.get(i).getAttack());
            values.put(Contract.ChampionEntry.COLUMN_DEFENSE, champions.get(i).getDefense());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC, champions.get(i).getMagic());

            // Save Enemy Tips.
            List<String> enemyTipsList = champions.get(i).getEnemyTips();
            String enemyTipsString = stringListToString(enemyTipsList);
            values.put(Contract.ChampionEntry.COLUMN_ENEMY_TIPS, enemyTipsString);

            // Save Ally Tips.
            List<String> allyTipsList = champions.get(i).getAllyTips();
            String allyTipsString = stringListToString(allyTipsList);
            values.put(Contract.ChampionEntry.COLUMN_ALLY_TIPS, allyTipsString);

            values.put(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE, champions.get(i).getAttackDamage());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_DAMAGE_PER_LEVEL, champions.get(i).getAttackDamagePerLevel());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_RANGE, champions.get(i).getAttackRange());
            values.put(Contract.ChampionEntry.COLUMN_ARMOR, champions.get(i).getArmor());
            values.put(Contract.ChampionEntry.COLUMN_ARMOR_PER_LEVEL, champions.get(i).getArmorPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH, champions.get(i).getHealth());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH_PER_LEVEL, champions.get(i).getHealthPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH_REGEN, champions.get(i).getHealthRegen());
            values.put(Contract.ChampionEntry.COLUMN_HEALTH_REGEN_PER_LEVEL, champions.get(i).getHealthRegenPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MANA, champions.get(i).getMana());
            values.put(Contract.ChampionEntry.COLUMN_MANA_PER_LEVEL, champions.get(i).getManaPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MANA_REGEN, champions.get(i).getManaRegen());
            values.put(Contract.ChampionEntry.COLUMN_MANA_REGEN_PER_LEVEL, champions.get(i).getManaRegenPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_OFFSET, champions.get(i).getAttackSpeedOffset());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK_SPEED_PER_LEVEL, champions.get(i).getAttackSpeedPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MOVE_SPEED, champions.get(i).getMoveSpeed());
            values.put(Contract.ChampionEntry.COLUMN_CRIT, champions.get(i).getCrit());
            values.put(Contract.ChampionEntry.COLUMN_CRIT_PER_LEVEL, champions.get(i).getCritPerLevel());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC_RESIST, champions.get(i).getMagicResist());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC_RESIST_PER_LEVEL, champions.get(i).getMagicResistPerLevel());

            List<String> spellNameList = champions.get(i).getSpellName();
            String spellNameString = stringListToString(spellNameList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_NAME, spellNameString);

            List<String> spellDescriptionList = champions.get(i).getSpellDescription();
            String spellDescriptionString = stringListToString(spellDescriptionList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION, spellDescriptionString);

            List<String> spellImageList = champions.get(i).getSpellImage();
            String spellImageString = stringListToString(spellImageList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_IMAGE, spellImageString);

            List<String> spellResourceList = champions.get(i).getSpellResource();
            String spellResourceString = stringListToString(spellResourceList);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_RESOURCE, spellResourceString);

            List<String> spellCooldown = champions.get(i).getSpellCooldownList();
            String spellCooldownString = stringListToString(spellCooldown);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN, spellCooldownString);

            List<String> spellCost = champions.get(i).getSpellCostList();
            String spellCostString = stringListToString(spellCost);
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COST, spellCostString);

            championContentValues[i] = values;
        }

        QueryHandler queryHandler = new QueryHandler(context.getContentResolver());
        for (int i = 0; i < championContentValues.length; i++) {
            queryHandler.startInsert(i, null, Contract.ChampionEntry.CONTENT_URI, championContentValues[i]);
        }
    }

    private static String stringListToString(List<String> list) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i != list.size() - 1){
                builder.append(STRING_DIVIDER);
            }
        }

        return builder.toString();
    }
}
