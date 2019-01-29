package com.example.android.leaguestats.data.network.api.utils;

import android.util.Log;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.Spell;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class OpenDataJsonParser {

    private static final String LOG_TAG = OpenDataJsonParser.class.getSimpleName();
    private static final String UNKNOWN_MOD = "Unknown mod for: ";

    public String[] parseChampions(String jsonChampionResponse) {
        try {
            return getJsonChampions(jsonChampionResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON champions parsing finished");
        }

        return null;
    }

    private String[] getJsonChampions(String jsonChampionResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonChampionResponse);
        JSONObject data = root.getJSONObject("data");

        String[] idArray = new String[data.length()];

        int i = 0;
        Iterator<String> iterator = data.keys();
        while (iterator.hasNext()) {
            String jsonItemString = iterator.next();
            JSONObject championObject = data.getJSONObject(jsonItemString);

            String championId = championObject.getString("id");
            idArray[i] = championId;
            i++;
        }
        return idArray;
    }

    public ChampionEntry parseChampionResponse(String jsonChampionResponse) {
        try {
            return getJsonChampionData(jsonChampionResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON champion parsing finished");
        }

        return null;
    }

    /**
     * Called to parse single champion JSON response
     * @param jsonChampionResponse Single champion json response.
     * @return Return ChampionEntry (Note id and key are swapped.)
     * @throws JSONException
     */
    private ChampionEntry getJsonChampionData(String jsonChampionResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonChampionResponse);
        JSONObject data = root.getJSONObject("data");

        Iterator<String> iterator = data.keys();

        if (iterator.hasNext()) {

            String jsonItemString = iterator.next();
            JSONObject championObject = data.getJSONObject(jsonItemString);

            // Get Champion Name, title...
            String championKey = championObject.getString("id");
            String championName = championObject.getString("name");
            int championId = Integer.parseInt(championObject.getString("key"));
            String championTitle = championObject.getString("title");
            String championLore = championObject.getString("lore");

            JSONArray tagsArray = championObject.getJSONArray("tags");
            List<String> championTags = JsonUtils.getStringListFromJSONArray(tagsArray);

            String partype = championObject.getString("partype");

            // Get Champion Passive.
            JSONObject passiveObject = championObject.getJSONObject("passive");
            String passiveName = passiveObject.getString("name");
            String passiveDescription = passiveObject.getString("description");
            JSONObject passiveImageObject = passiveObject.getJSONObject("image");
            String passiveId = passiveImageObject.getString("full");

            // Get Champion Spells.
            List<Spell> spells = new ArrayList<>();
            JSONArray spellsArray = championObject.getJSONArray("spells");
            for (int i = 0; i < spellsArray.length(); i++) {
                spells.add(new Spell(spellsArray.getJSONObject(i)));
            }

            // Get Champion Thumbnail.
            JSONObject image = championObject.getJSONObject("image");
            String championImage = image.getString("full");

            // Get Champion allyTips.
            JSONArray allyTipsArray = championObject.getJSONArray("allytips");
            List<String> asTips = JsonUtils.getStringListFromJSONArray(allyTipsArray);

            // Get Champion enemyTips.
            JSONArray enemyTipsArray = championObject.getJSONArray("enemytips");
            List<String> vsTips = JsonUtils.getStringListFromJSONArray(enemyTipsArray);

            JSONArray skins = championObject.getJSONArray("skins");
            List<Integer> splashArtIdList = JsonUtils.getListIntegerFromJSONObjectFromJSONArray(skins, "num");
            List<String> splashArtName = JsonUtils.getListStringFromJSONObjectFromJSONArray(skins, "name");

            // Get Champion info.
            JSONObject info = championObject.getJSONObject("info");
            int championDifficulty = info.optInt("difficulty");
            int championAttack = info.optInt("attack");
            int championDefense = info.optInt("defense");
            int championMagic = info.optInt("magic");

            // Get Champion stats. data type double.
            JSONObject statsObject = championObject.getJSONObject("stats");
            double armorPerLevel = statsObject.optDouble("armorperlevel", 0.0);
            double attackDamage = statsObject.optDouble("attackdamage", 0.0);
            double manaPerLevel = statsObject.optDouble("mpperlevel", 0.0);
            double attackSpeedOffset = statsObject.optDouble("attackspeedoffset", 0.0);
            double mana = statsObject.optDouble("mp", 0.0);
            double armor = statsObject.optDouble("armor", 0.0);
            double health = statsObject.optDouble("hp", 0.0);
            double healthRegenPerLevel = statsObject.optDouble("hpregenperlevel", 0.0);
            double attackSpeedPerLevel = statsObject.optDouble("attackspeedperlevel", 0.0);
            double attackRange = statsObject.optDouble("attackrange", 0.0);
            double moveSpeed = statsObject.optDouble("movespeed", 0.0);
            double attackDamagePerLevel = statsObject.optDouble("attackdamageperlevel", 0.0);
            double manaRegenPerLevel = statsObject.optDouble("mpregenperlevel", 0.0);
            double critPerLevel = statsObject.optDouble("critperlevel", 0.0);
            double magicResistPerLevel = statsObject.optDouble("spellblockperlevel", 0.0);
            double crit = statsObject.optDouble("crit", 0.0);
            double manaRegen = statsObject.optDouble("mpregen", 0.0);
            double magicResist = statsObject.optDouble("spellblock", 0.0);
            double healthRegen = statsObject.optDouble("hpregen", 0.0);
            double healthPerLevel = statsObject.optDouble("hpperlevel", 0.0);

            // Add Champion to List.
            return new ChampionEntry(championId, championName, championKey, championTitle,
                    championLore, championTags, championImage, splashArtIdList, splashArtName,
                    championDifficulty, championAttack, championDefense, championMagic, vsTips,
                    asTips, attackDamage, attackDamagePerLevel, attackRange, armor, armorPerLevel,
                    health, healthPerLevel, healthRegen, healthRegenPerLevel, mana, manaPerLevel,
                    manaRegen, manaRegenPerLevel, attackSpeedOffset, attackSpeedPerLevel, moveSpeed,
                    crit, critPerLevel, magicResist, magicResistPerLevel, partype, passiveName,
                    passiveDescription, passiveId, spells);
        }

        return null;
    }

    public List<ItemEntry> parseItemResponse(String jsonItemResponse) {
        try {
            return getJsonItemData(jsonItemResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON item parsing finished");
        }

        return null;
    }

    private List<ItemEntry> getJsonItemData(String jsonItemResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonItemResponse);
        JSONObject data = root.getJSONObject("data");

        List<ItemEntry> itemEntryList = new ArrayList<>();

        Iterator<String> itemIterator = data.keys();
        while (itemIterator.hasNext()) {

            String jsonItemString = itemIterator.next();
            JSONObject itemJson = data.getJSONObject(jsonItemString);

            int id = Integer.parseInt(jsonItemString);
            String key = jsonItemString;
            String name = itemJson.optString("name");
            String description = itemJson.optString("sanitizedDescription");
            String plainText = itemJson.optString("plaintext");
            int depth = itemJson.optInt("depth");

            JSONArray fromArray = itemJson.optJSONArray("from");
            List<String> fromList = new ArrayList<>();
            if (fromArray != null) {
                fromList = JsonUtils.getStringListFromJSONArray(fromArray);
            }

            JSONArray intoArray = itemJson.optJSONArray("into");
            List<String> intoList = new ArrayList<>();
            if (intoArray != null) {
                intoList = JsonUtils.getStringListFromJSONArray(intoArray);
            }

            double flatArmor = 0;
            double flatSpellBlock = 0;
            double flatHPPool = 0;
            double flatMPPool = 0;
            double flatHPRegen = 0;
            double flatCritChance = 0;
            double flatMagicDamage = 0;
            double flatPhysicalDamage = 0;
            double flatMovementSpeed = 0;
            double percentMovementSpeed = 0;
            double percentAttackSpeed = 0;
            double percentLifeSteal = 0;

            if (itemJson.has("stats")) {
                JSONObject statsJson = itemJson.getJSONObject("stats");
                Iterator<String> statsIterator = statsJson.keys();
                while (statsIterator.hasNext()) {
                    String jsonStatKeys = statsIterator.next();
                    double stat = statsJson.getDouble(jsonStatKeys);
                    switch (jsonStatKeys) {
                        case "FlatArmorMod":
                            flatArmor = stat;
                            break;
                        case "FlatSpellBlockMod":
                            flatSpellBlock = stat;
                            break;
                        case "FlatHPPoolMod":
                            flatHPPool = stat;
                            break;
                        case "FlatMPPoolMod":
                            flatMPPool = stat;
                            break;
                        case "FlatHPRegenMod":
                            flatHPRegen = stat;
                            break;
                        case "FlatCritChanceMod":
                            flatCritChance = stat;
                            break;
                        case "FlatMagicDamageMod":
                            flatMagicDamage = stat;
                            break;
                        case "FlatPhysicalDamageMod":
                            flatPhysicalDamage = stat;
                            break;
                        case "FlatMovementSpeedMod":
                            flatMovementSpeed = stat;
                            break;
                        case "PercentMovementSpeedMod":
                            percentMovementSpeed = stat;
                            break;
                        case "PercentAttackSpeedMod":
                            percentAttackSpeed = stat;
                            break;
                        case "PercentLifeStealMod":
                            percentLifeSteal = stat;
                            break;
                        default:
                            Log.w(LOG_TAG, UNKNOWN_MOD + jsonStatKeys);
                    }
                }
            }

            JSONObject imageJson = itemJson.getJSONObject("image");
            String imageId = imageJson.optString("full");

            int baseGold = 0;
            int totalGold = 0;
            int sellGold = 0;
            String purchasable = "";
            if (itemJson.has("gold")) {
                JSONObject goldJson = itemJson.getJSONObject("gold");
                baseGold = goldJson.optInt("base");
                totalGold = goldJson.optInt("total");
                sellGold = goldJson.optInt("sell");
                // "true", "false"
                purchasable = goldJson.optString("purchasable");
            }

            ItemEntry itemEntry = new ItemEntry(id, key, name, plainText, description, depth, imageId, intoList,
                    fromList, baseGold, totalGold, sellGold, purchasable, flatArmor, flatSpellBlock,
                    flatHPPool, flatMPPool, flatHPRegen, flatCritChance, flatMagicDamage, flatPhysicalDamage,
                    flatMovementSpeed, percentMovementSpeed, percentAttackSpeed, percentLifeSteal);
            itemEntryList.add(itemEntry);
        }

        return itemEntryList;
    }

    public List<IconEntry> parseIconResponse(String jsonIconResponse) {
        try {
            return getJsonIconData(jsonIconResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON icon parsing finished");
        }

        return null;
    }

    private List<IconEntry> getJsonIconData(String jsonIconResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonIconResponse);
        JSONObject data = root.getJSONObject("data");

        List<IconEntry> iconEntries = new ArrayList<>();

        Iterator<String> iterator = data.keys();

        while (iterator.hasNext()) {
            JSONObject iconObject = data.getJSONObject(iterator.next());

            int iconId = iconObject.getInt("id");

            IconEntry iconEntry = new IconEntry(iconId);
            iconEntries.add(iconEntry);
        }

        return iconEntries;
    }

    public List<SummonerSpellEntry> parseSummonerSpellResponse(String jsonSummonerSpellResponse) {
        try {
            return getJsonSummonerSpellData(jsonSummonerSpellResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON summonerSpell parsing finished");
        }

        return null;
    }

    private List<SummonerSpellEntry> getJsonSummonerSpellData(String jsonSummonerSpellResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonSummonerSpellResponse);
        JSONObject data = root.getJSONObject("data");

        List<SummonerSpellEntry> summonerSpellEntryList = new ArrayList<>();

        Iterator<String> summonerSpellIterator = data.keys();
        int i = 0;
        while (summonerSpellIterator.hasNext()) {

            String jsonSummonerSpellString = summonerSpellIterator.next();
            JSONObject summonerSpellJson = data.getJSONObject(jsonSummonerSpellString);

            String key = summonerSpellJson.optString("id");
            String name = summonerSpellJson.optString("name");
            String description = summonerSpellJson.optString("description");
            int id = Integer.parseInt(summonerSpellJson.optString("key"));

            JSONObject imageJson = summonerSpellJson.getJSONObject("image");
            String imageId = imageJson.optString("full");

            JSONArray costArray = summonerSpellJson.getJSONArray("cost");
            int cost = costArray.getInt(0);

            JSONArray cooldownArray = summonerSpellJson.getJSONArray("cooldown");
            int cooldown = cooldownArray.getInt(0);

            JSONArray rangeArray = summonerSpellJson.getJSONArray("range");
            int range = rangeArray.getInt(0);

            JSONArray modesArray = summonerSpellJson.optJSONArray("modes");
            List<String> modesList = JsonUtils.getStringListFromJSONArray(modesArray);

            SummonerSpellEntry summonerSpellEntry = new SummonerSpellEntry(id, key, name,
                    description, imageId, cost, cooldown, range, modesList);
            summonerSpellEntryList.add(summonerSpellEntry);
        }
        return summonerSpellEntryList;
    }

    public List<String> parsePatchResponse(String jsonPatchResponse) {
        try {
            return getJsonPatchData(jsonPatchResponse);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON patch parsing finished");
        }

        return null;
    }

    private List<String> getJsonPatchData(String jsonPatchResponse) throws JSONException {
        JSONArray array = new JSONArray(jsonPatchResponse);
        List<String> patchList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            patchList.add(array.getString(i));
        }
        return patchList;
    }
}
