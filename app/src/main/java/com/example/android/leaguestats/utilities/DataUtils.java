package com.example.android.leaguestats.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.android.leaguestats.BuildConfig;
import com.example.android.leaguestats.database.Contract;
import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Icon;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.models.SummonerSpell;

import java.util.List;

public class DataUtils {

    private static final String LOG_TAG = DataUtils.class.getSimpleName();

    public static final String STRING_DIVIDER = "_,_";

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

        QueryHandler queryHandler = new QueryHandler(context.getContentResolver());
        for (int i = 0; i < contentValues.length; i++) {
            queryHandler.startInsert(i, null, Contract.IconEntry.CONTENT_URI, contentValues[i]);
        }
    }

    public static void saveChampionData(Context context, List<Champion> champions) {
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

            // Save Champion Thumbnail.
            values.put(Contract.ChampionEntry.COLUMN_CHAMPION_THUMBNAIL, champions.get(i).getThumbnail());

            // Save Splash Art Path.
            values.put(Contract.ChampionEntry.COLUMN_SPLASH_ART, champions.get(i).getSplashArt());
            // Save Splash Art Name.
            values.put(Contract.ChampionEntry.COLUMN_SPLASH_ART_NAME, champions.get(i).getSplashArtName());

            // Save Champion Info.
            values.put(Contract.ChampionEntry.COLUMN_DIFFICULTY, champions.get(i).getDifficulty());
            values.put(Contract.ChampionEntry.COLUMN_ATTACK, champions.get(i).getAttack());
            values.put(Contract.ChampionEntry.COLUMN_DEFENSE, champions.get(i).getDefense());
            values.put(Contract.ChampionEntry.COLUMN_MAGIC, champions.get(i).getMagic());

            // Save Enemy Tips.
            values.put(Contract.ChampionEntry.COLUMN_ENEMY_TIPS, champions.get(i).getEnemyTips());

            // Save Ally Tips.
            values.put(Contract.ChampionEntry.COLUMN_ALLY_TIPS, champions.get(i).getAllyTips());

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

            values.put(Contract.ChampionEntry.COLUMN_SPELL_NAME, champions.get(i).getSpellName());
            values.put(Contract.ChampionEntry.COLUMN_SPELL_DESCRIPTION, champions.get(i).getSpellDescription());
            values.put(Contract.ChampionEntry.COLUMN_SPELL_IMAGE, champions.get(i).getSpellImage());
            values.put(Contract.ChampionEntry.COLUMN_SPELL_RESOURCE, champions.get(i).getSpellResource());
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COOLDOWN, champions.get(i).getSpellCooldownList());
            values.put(Contract.ChampionEntry.COLUMN_SPELL_COST, champions.get(i).getSpellCostList());

            championContentValues[i] = values;
        }

        QueryHandler queryHandler = new QueryHandler(context.getContentResolver());
        for (int i = 0; i < championContentValues.length; i++) {
            queryHandler.startInsert(i, null, Contract.ChampionEntry.CONTENT_URI, championContentValues[i]);
        }
    }

    public static void saveItemData(Context context, List<Item> itemList) {
        ContentValues[] itemValues = new ContentValues[itemList.size()];

        for (int i = 0; i < itemList.size(); i++) {
            ContentValues values = new ContentValues();

            Item item = itemList.get(i);

            values.put(Contract.ItemEntry._ID, item.getId());
            values.put(Contract.ItemEntry.COLUMN_NAME, item.getName());
            values.put(Contract.ItemEntry.COLUMN_PLAIN_TEXT, item.getPlainText());
            values.put(Contract.ItemEntry.COLUMN_SANITIZED_DESCRIPTION, item.getDescription());
            values.put(Contract.ItemEntry.COLUMN_DEPTH, item.getDepth());

            values.put(Contract.ItemEntry.COLUMN_IMAGE, item.getImage());

            values.put(Contract.ItemEntry.COLUMN_INTO, item.getInto());
            values.put(Contract.ItemEntry.COLUMN_FROM, item.getFrom());

            values.put(Contract.ItemEntry.COLUMN_BASE_GOLD, item.getBaseGold());
            values.put(Contract.ItemEntry.COLUMN_TOTAL_GOLD, item.getTotalGold());
            values.put(Contract.ItemEntry.COLUMN_SELL_GOLD, item.getSellGold());
            values.put(Contract.ItemEntry.COLUMN_PURCHASABLE, item.getPurchasable());

            values.put(Contract.ItemEntry.COLUMN_FLAT_ARMOR, item.getFlatArmorMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_MAGIC_RESIST, item.getFlatMagicDamageMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_HP_POOL, item.getFlatHPPoolMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_MP_POOL, item.getFlatMPPoolMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_HP_REGEN, item.getFlatHPRegenMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_CRIT_CHANCE, item.getFlatCritChanceMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_MAGIC_DAMAGE, item.getFlatMagicDamageMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_PHYSICAL_DAMAGE, item.getFlatPhysicalDamageMod());
            values.put(Contract.ItemEntry.COLUMN_FLAT_MOVEMENT_SPEED, item.getFlatMovementSpeedMod());
            values.put(Contract.ItemEntry.COLUMN_PERCENT_MOVEMENT_SPEED, item.getmPercentMovementSpeedMod());
            values.put(Contract.ItemEntry.COLUMN_PERCENT_ATTACK_SPEED, item.getPercentAttackSpeedMod());
            values.put(Contract.ItemEntry.COLUMN_PERCENT_LIFE_STEAL, item.getPercentLifeStealMod());

            itemValues[i] = values;
        }

        QueryHandler queryHandler = new QueryHandler(context.getContentResolver());
        for (int i = 0; i < itemValues.length; i++) {
            queryHandler.startInsert(i, null, Contract.ItemEntry.CONTENT_URI, itemValues[i]);
        }
    }

    public static void saveSummonerSpellData(Context context, List<SummonerSpell> summonerSpells) {
        ContentValues[] summonerSpellValues = new ContentValues[summonerSpells.size()];

        for (int i = 0; i < summonerSpells.size(); i++) {
            ContentValues values = new ContentValues();

             SummonerSpell summonerSpell = summonerSpells.get(i);

             values.put(Contract.SummonerSpellEntry._ID, summonerSpell.getId());
             values.put(Contract.SummonerSpellEntry.COLUMN_KEY, summonerSpell.getKey());
             values.put(Contract.SummonerSpellEntry.COLUMN_NAME, summonerSpell.getName());
             values.put(Contract.SummonerSpellEntry.COLUMN_DESCRIPTION, summonerSpell.getDescription());
             values.put(Contract.SummonerSpellEntry.COLUMN_IMAGE, summonerSpell.getImage());
             values.put(Contract.SummonerSpellEntry.COLUMN_COST, summonerSpell.getCost());
             values.put(Contract.SummonerSpellEntry.COLUMN_COOLDOWN, summonerSpell.getCooldown());
             values.put(Contract.SummonerSpellEntry.COLUMN_RANGE, summonerSpell.getRange());
             values.put(Contract.SummonerSpellEntry.COLUMN_MODES, summonerSpell.getModes());

             summonerSpellValues [i] = values;
        }

        QueryHandler queryHandler = new QueryHandler(context.getContentResolver());
        for (int i = 0; i < summonerSpellValues.length; i++) {
            queryHandler.startInsert(i, null, Contract.SummonerSpellEntry.CONTENT_URI, summonerSpellValues[i]);
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

    public static String[] stringToStringArray(String string) {
        if (!TextUtils.isEmpty(string)) {
            return string.split(DataUtils.STRING_DIVIDER);
        } else {
            return new String[0];
        }
    }

    public static String buildInClause(String[] strings) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i == 0) {
                builder.append(" in(");
                builder.append("? ");
            } else {
                builder.append(",? ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    public static void setTextWithLabel(String string, TextView textView, TextView textViewLabel) {
        if (TextUtils.isEmpty(string)) {
            textViewLabel.setVisibility(View.GONE);
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textView.setText(string);
        }
    }

    public static void setTextWithLabel(int value, TextView textView, TextView textViewLabel) {
        if (value == 0) {
            textViewLabel.setVisibility(View.GONE);
        } else {
            textViewLabel.setVisibility(View.VISIBLE);
            textView.setText(String.valueOf(value));
        }
    }

    public static void setTextWithLabel(String labelString, double value, TextView textView) {
        if (!(value == 0)) {
            textView.append(labelString + " " + String.valueOf(value) + "\n");
        }
    }
}
