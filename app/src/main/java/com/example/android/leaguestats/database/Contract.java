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

        public static final String COLUMN_THUMBNAIL = "thumbnail";

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
        public static final String COLUMN_SPELL_COOLDOWN = "spellClass";
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
}
