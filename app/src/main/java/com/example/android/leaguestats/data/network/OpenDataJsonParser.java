package com.example.android.leaguestats.data.network;

import android.util.Log;

import com.example.android.leaguestats.data.database.entity.ChampionEntry;
import com.example.android.leaguestats.data.database.entity.IconEntry;
import com.example.android.leaguestats.data.database.entity.ItemEntry;
import com.example.android.leaguestats.data.database.entity.SummonerSpellEntry;
import com.example.android.leaguestats.models.Match;

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
     * @param jsonChampionResponse String object, simple single champion json response.
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

            // Get Champion Spells.
            JSONArray spellsArray = championObject.getJSONArray("spells");
            List<String> spellName = JSONUtils.getListStringFromJSONObjectFromJSONArray(spellsArray, "name");
            List<String> spellDescription = JSONUtils.getListStringFromJSONObjectFromJSONArray(spellsArray, "description");
            List<String> spellImage = JSONUtils.getSpellImage(spellsArray, "image", "full");
            List<String> spellResource = JSONUtils.getListStringFromJSONObjectFromJSONArray(spellsArray, "resource");
            List<Double> spellCooldown = JSONUtils.getListFromJSONArrayFromJSONObjectFromJSONArray(spellsArray, "cooldown");
            List<Double> spellCost = JSONUtils.getListFromJSONArrayFromJSONObjectFromJSONArray(spellsArray, "cost");
            List<Integer> spellMaxRank = JSONUtils.getListIntegerFromJSONObjectFromJSONArray(spellsArray, "maxrank");

            // Get Champion Thumbnail.
            JSONObject image = championObject.getJSONObject("image");
            String championImage = image.getString("full");

            // Get Champion allyTips.
            JSONArray allyTipsArray = championObject.getJSONArray("allytips");
            List<String> asTips = JSONUtils.getStringListFromJSONArray(allyTipsArray);

            // Get Champion enemyTips.
            JSONArray enemyTipsArray = championObject.getJSONArray("enemytips");
            List<String> vsTips = JSONUtils.getStringListFromJSONArray(enemyTipsArray);

            // Get Champion SplashArt.
            // Example http://ddragon.leagueoflegends.com/cdn/img/champion/splash/Aatrox_0.jpg
            JSONArray skins = championObject.getJSONArray("skins");

            List<String> splashArtList = JSONUtils.getSplashArtPathFromJSONObjectFromJSONArray(skins, "num", championKey);
            List<String> splashArtName = JSONUtils.getListStringFromJSONObjectFromJSONArray(skins, "name");

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

            // Add champion to List.
            return new ChampionEntry(championId, championName, championKey, championTitle,
                    championLore, championImage, splashArtList, splashArtName,
                    championDifficulty, championAttack, championDefense, championMagic, vsTips,
                    asTips, attackDamage, attackDamagePerLevel, attackRange, armor, armorPerLevel,
                    health, healthPerLevel, healthRegen, healthRegenPerLevel, mana, manaPerLevel,
                    manaRegen, manaRegenPerLevel, attackSpeedOffset, attackSpeedPerLevel, moveSpeed,
                    crit, critPerLevel, magicResist, magicResistPerLevel, spellName,
                    spellDescription, spellImage, spellResource, spellCooldown, spellCost, spellMaxRank);
        }

        return null;
    }

    public ItemEntry[] parseItemResponse(String jsonItemResponse) {
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

    private ItemEntry[] getJsonItemData(String jsonItemResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonItemResponse);
        JSONObject data = root.getJSONObject("data");

        ItemEntry[] itemEntries = new ItemEntry[data.length()];

        int i = 0;
        Iterator<String> itemIterator = data.keys();
        while (itemIterator.hasNext()) {

            String jsonItemString = itemIterator.next();
            JSONObject itemJson = data.getJSONObject(jsonItemString);

            int id = Integer.parseInt(jsonItemString);
            String name = itemJson.optString("name");
            String description = itemJson.optString("sanitizedDescription");
            String plainText = itemJson.optString("plaintext");
            int depth = itemJson.optInt("depth");

            JSONArray fromArray = itemJson.optJSONArray("from");
            List<String> fromList = new ArrayList<>();
            if (fromArray != null) {
                fromList = JSONUtils.getStringListFromJSONArray(fromArray);
            }

            JSONArray intoArray = itemJson.optJSONArray("into");
            List<String> intoList = new ArrayList<>();
            if (intoArray != null) {
                intoList = JSONUtils.getStringListFromJSONArray(intoArray);
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
            String image = imageJson.optString("full");

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

            ItemEntry item = new ItemEntry(id, name, plainText, description, depth, image, intoList,
                    fromList, baseGold, totalGold, sellGold, purchasable, flatArmor, flatSpellBlock,
                    flatHPPool, flatMPPool, flatHPRegen, flatCritChance, flatMagicDamage, flatPhysicalDamage,
                    flatMovementSpeed, percentMovementSpeed, percentAttackSpeed, percentLifeSteal);

            itemEntries[i] = item;
            i++;
        }

        return itemEntries;
    }

    public IconEntry[] parseIconResponse(String jsonIconResponse) {
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

    private IconEntry[] getJsonIconData(String jsonIconResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonIconResponse);
        JSONObject data = root.getJSONObject("data");

        IconEntry[] iconEntries = new IconEntry[data.length()];

        int i = 0;
        Iterator<String> iterator = data.keys();

        while (iterator.hasNext()) {
            JSONObject iconObject = data.getJSONObject(iterator.next());

            JSONObject iconImage = iconObject.getJSONObject("image");
            String groupString = iconImage.getString("group");
            String fullString = iconImage.getString("full");
            int iconId = iconObject.getInt("id");

            IconEntry iconEntry = new IconEntry(iconId, groupString + "/" + fullString);
            iconEntries[i] = iconEntry;
            i++;
        }

        return iconEntries;
    }

    public SummonerSpellEntry[] parseSummonerSpellResponse(String jsonSummonerSpellResponse) {
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

    private SummonerSpellEntry[] getJsonSummonerSpellData(String jsonSummonerSpellResponse) throws JSONException {
        JSONObject root = new JSONObject(jsonSummonerSpellResponse);
        JSONObject data = root.getJSONObject("data");

        SummonerSpellEntry[] summonerSpellEntries = new SummonerSpellEntry[data.length()];

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
            String image = imageJson.optString("full");

            JSONArray costArray = summonerSpellJson.getJSONArray("cost");
            int cost = costArray.getInt(0);

            JSONArray cooldownArray = summonerSpellJson.getJSONArray("cooldown");
            int cooldown = cooldownArray.getInt(0);

            JSONArray rangeArray = summonerSpellJson.getJSONArray("range");
            int range = rangeArray.getInt(0);

            JSONArray modesArray = summonerSpellJson.optJSONArray("modes");
            List<String> modesList = JSONUtils.getStringListFromJSONArray(modesArray);

            SummonerSpellEntry summonerSpell = new SummonerSpellEntry(id, key, name, description,
                    image, cost, cooldown, range, modesList);
            summonerSpellEntries[i] = summonerSpell;
            i++;
        }
        return summonerSpellEntries;
    }

    public String parsePatchResponse(String jsonPatchResponse) {
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

    private String getJsonPatchData(String jsonPatchResponse) throws JSONException {
        JSONArray array = new JSONArray(jsonPatchResponse);
        String lastPatchVersion = array.getString(0);
        return lastPatchVersion;
    }

    public Match parseMatchResponse(String matchResponse, long summonerId) {
        try {
            return getJsonMatchData(matchResponse, summonerId);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            Log.d(LOG_TAG, "JSON match parsing finished");
        }

        return null;
    }

    private Match getJsonMatchData(String matchResponse, long summonerId) throws JSONException {
        JSONObject root = new JSONObject(matchResponse);

        List<Integer> participantIdList = new ArrayList<>();
        List<Long> accountIdList = new ArrayList<>();
        List<Long> summonerIdList = new ArrayList<>();
        List<String> summonerNameList = new ArrayList<>();
        List<Integer> teamIdList = new ArrayList<>();
        List<Integer> championIdList = new ArrayList<>();
        List<Integer> spell1IdList = new ArrayList<>();
        List<Integer> spell2IdList = new ArrayList<>();
        List<Boolean> winList = new ArrayList<>();
        List<Integer> itemList = new ArrayList<>();
        List<Integer> killList = new ArrayList<>();
        List<Integer> deathList = new ArrayList<>();
        List<Integer> assistList = new ArrayList<>();
        List<Long> totalDamageToChampions = new ArrayList<>();

        JSONArray participants = root.getJSONArray("participants");
        for (int i = 0; i < participants.length(); i++) {
            JSONObject object = participants.getJSONObject(i);

            participantIdList.add(object.getInt("participantId"));
            teamIdList.add(object.getInt("teamId"));
            championIdList.add(object.getInt("championId"));
            spell1IdList.add(object.getInt("spell1Id"));
            spell2IdList.add(object.getInt("spell2Id"));

            JSONObject statsObject = object.getJSONObject("stats");

            winList.add(statsObject.getBoolean("win"));

            killList.add(statsObject.getInt("kills"));
            deathList.add(statsObject.getInt("deaths"));
            assistList.add(statsObject.getInt("assists"));

            totalDamageToChampions.add(statsObject.getLong("totalDamageDealtToChampions"));

            for (int j = 0; j < 7; j++) {
                String key = "item" + j;
                itemList.add(statsObject.getInt(key));
            }
        }

        JSONArray participantIdentities = root.getJSONArray("participantIdentities");
        for (int i = 0; i < participantIdentities.length(); i++) {
            JSONObject object = participantIdentities.getJSONObject(i);
            JSONObject player = object.getJSONObject("player");

            accountIdList.add(player.getLong("accountId"));
            summonerIdList.add(player.getLong("summonerId"));
            summonerNameList.add(player.getString("summonerName"));
        }

        long gameDuration = root.getLong("gameDuration");
        long gameCreation = root.getLong("gameCreation");

        /*
        return new Match(participantIdList, accountIdList, summonerIdList, summonerNameList,
                teamIdList, championIdList, spell1IdList, spell2IdList, gameDuration, gameCreation,
                winList, itemList, killList, deathList, assistList, totalDamageToChampions, summonerId);
                */
        return null;
    }
}
