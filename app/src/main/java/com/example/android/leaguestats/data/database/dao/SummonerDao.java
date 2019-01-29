package com.example.android.leaguestats.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.android.leaguestats.models.Champion;
import com.example.android.leaguestats.models.Item;
import com.example.android.leaguestats.models.SummonerSpell;

import java.util.List;

@Dao
public interface SummonerDao {

    String SUMMONER_SPELL_COLUMNS = "summoner_spell_id, summoner_spell_key, summoner_spell_name, summoner_spell_description, summoner_spell_image_id, summoner_spell_cooldown";

    @Query("SELECT champion_id, champion_key, champion_name, champion_title, champion_image_id, " +
            "champion_splash_art_id, champion_splash_art_name  FROM champion WHERE champion_id IN (:id)")
    LiveData<List<Champion>> getChampions(List<Integer> id);

    @Query("SELECT item_id, item_key, item_name, item_description, item_plain_text, item_image_id, item_total_gold FROM item WHERE item_id IN (:id)")
    LiveData<List<Item>> getItems(List<Integer> id);

    @Query("SELECT " + SUMMONER_SPELL_COLUMNS + " FROM summoner_spell WHERE summoner_spell_id IN (:id)")
    LiveData<List<SummonerSpell>> getSummonerSpells(List<Integer> id);
}
