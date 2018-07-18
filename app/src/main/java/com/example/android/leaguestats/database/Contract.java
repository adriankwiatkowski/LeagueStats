package com.example.android.leaguestats.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public static final String CONTENT_AUTHORITY = "com.example.android.leaguestats";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CHAMPION = "champions";
    public static final String PATH_ICON = "icon";
    public static final String PATH_ITEM = "item";
    public static final String PATH_SUMMONER_SPELL = "summonerSpell";

    public Contract() {}

    public static final class ChampionEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHAMPION).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAMPION;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHAMPION;

        public static final String TABLE_NAME = "champions";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_CHAMPION_NAME = "name";
        public static final String COLUMN_KEY = "championKey";
        public static final String COLUMN_CHAMPION_TITLE = "title";
        public static final String COLUMN_CHAMPION_LORE = "lore";

        public static final String COLUMN_CHAMPION_THUMBNAIL = "thumbnail";

        public static final String COLUMN_SPLASH_ART = "splashArt";
        public static final String COLUMN_SPLASH_ART_NAME = "splashArtName";

        public static final String COLUMN_ALLY_TIPS = "asTips";
        public static final String COLUMN_ENEMY_TIPS = "enemytips";

        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_ATTACK = "attack";
        public static final String COLUMN_DEFENSE = "defense";
        public static final String COLUMN_MAGIC = "magic";

        public static final String COLUMN_ARMOR_PER_LEVEL = "armorperlevel";
        public static final String COLUMN_ATTACK_DAMAGE = "attackdamage";
        public static final String COLUMN_MANA_PER_LEVEL = "mpperlevel";
        public static final String COLUMN_ATTACK_SPEED_OFFSET = "attackspeedoffset";
        public static final String COLUMN_MANA = "mp";
        public static final String COLUMN_ARMOR = "armor";
        public static final String COLUMN_HEALTH = "hp";
        public static final String COLUMN_HEALTH_REGEN_PER_LEVEL = "hpregenperlevel";
        public static final String COLUMN_ATTACK_SPEED_PER_LEVEL = "attackspeedperlevel";
        public static final String COLUMN_ATTACK_RANGE = "attackrange";
        public static final String COLUMN_MOVE_SPEED = "movespeed";
        public static final String COLUMN_ATTACK_DAMAGE_PER_LEVEL = "attackdamageperlevel";
        public static final String COLUMN_MANA_REGEN_PER_LEVEL = "mpregenperlevel";
        public static final String COLUMN_CRIT_PER_LEVEL = "critperlevel";
        public static final String COLUMN_MAGIC_RESIST_PER_LEVEL = "spellblockperlevel";
        public static final String COLUMN_CRIT = "crit";
        public static final String COLUMN_MANA_REGEN = "mpregen";
        public static final String COLUMN_MAGIC_RESIST = "spellblock";
        public static final String COLUMN_HEALTH_REGEN = "hpregen";
        public static final String COLUMN_HEALTH_PER_LEVEL = "hpperlevel";

        public static final String COLUMN_SPELL_NAME = "spellName";
        public static final String COLUMN_SPELL_DESCRIPTION = "spellDescription";
        public static final String COLUMN_SPELL_IMAGE = "spellImage";
        public static final String COLUMN_SPELL_RESOURCE = "spellResource";
        public static final String COLUMN_SPELL_COOLDOWN = "spellCooldown";
        public static final String COLUMN_SPELL_COST = "spellCost";

        public static Uri buildChampionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class IconEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ICON).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ICON;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ICON;

        public static final String TABLE_NAME = "icons";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_ICON_ID = "iconId";

        public static Uri buildIconUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        public static final String TABLE_NAME = "items";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SANITIZED_DESCRIPTION = "sanitizedDescription";
        public static final String COLUMN_PLAIN_TEXT = "plainText";
        public static final String COLUMN_DEPTH = "itemDepth";
        public static final String COLUMN_FROM = "itemFrom";
        public static final String COLUMN_INTO = "itemInto";

        public static final String COLUMN_IMAGE = "image";

        public static final String COLUMN_BASE_GOLD = "baseGold";
        public static final String COLUMN_TOTAL_GOLD = "totalGold";
        public static final String COLUMN_SELL_GOLD = "sellGold";
        public static final String COLUMN_PURCHASABLE = "purchasable";

        public static final String COLUMN_FLAT_ARMOR = "FlatArmorMod";
        public static final String COLUMN_FLAT_MAGIC_RESIST = "FlatSpellBlockMod";
        public static final String COLUMN_FLAT_HP_POOL = "FlatHPPoolMod";
        public static final String COLUMN_FLAT_MP_POOL = "FlatMPPoolMod";
        public static final String COLUMN_FLAT_HP_REGEN = "FlatHPRegenMod";
        public static final String COLUMN_FLAT_CRIT_CHANCE = "FlatCritChanceMod";
        public static final String COLUMN_FLAT_PHYSICAL_DAMAGE = "FlatPhysicalDamageMod";
        public static final String COLUMN_FLAT_MAGIC_DAMAGE = "FlatMagicDamageMod";
        public static final String COLUMN_FLAT_MOVEMENT_SPEED = "FlatMovementSpeedMod";
        public static final String COLUMN_PERCENT_MOVEMENT_SPEED = "PercentMovementSpeedMod";
        public static final String COLUMN_PERCENT_LIFE_STEAL = "PercentLifeStealMod";
        public static final String COLUMN_PERCENT_ATTACK_SPEED = "PercentAttackSpeedMod";

        public static Uri buildItemUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SummonerSpellEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SUMMONER_SPELL).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUMMONER_SPELL;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SUMMONER_SPELL;

        public static final String TABLE_NAME = "summonerSpell";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_KEY = "summonerSpellKey";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_COST = "cost";
        public static final String COLUMN_COOLDOWN = "cooldown";
        public static final String COLUMN_RANGE = "range";
        public static final String COLUMN_MODES = "modes";

        public static Uri buildSummonerSpellUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
