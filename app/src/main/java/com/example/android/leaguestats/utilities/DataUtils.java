package com.example.android.leaguestats.utilities;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.android.leaguestats.BuildConfig;

import java.util.List;

public class DataUtils {

    private static final String ENTRY_URL_SUMMONER_EUNE = "https://eun1.api.riotgames.com";
    private static final String ENTRY_URL_SUMMONER_EUW = "https://euw1.api.riotgames.com";
    private static final String ENTRY_URL_SUMMONER_NA = "https://na1.api.riotgames.com";
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
}
