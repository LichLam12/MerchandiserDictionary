package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.*
import android.database.Observable
import io.reactivex.Flowable



@Dao
interface DictionaryDAO {

    @Query("SELECT * FROM dictionaryData WHERE id = :dictionaryID LIMIT 1")
    fun selectByDictionaryId(dictionaryID : String): DictionaryEntity

    @Query("SELECT * from dictionaryData WHERE favorite = '1' OR favorite = '3'")
    fun getFavAll() : List<DictionaryEntity>

    @Query("SELECT * from dictionaryData WHERE favorite = '2' OR favorite = '3'")
    fun getHistoryAll() : List<DictionaryEntity>

    @Query("SELECT * from dictionaryData")
    fun getAll() : List<DictionaryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(placeEntity : DictionaryEntity)

    @Query("DELETE from dictionaryData")
    fun deleteAll()

    @Query("DELETE FROM dictionaryData WHERE id = :dictionaryID")
    fun deleteByDictionaryId(dictionaryID : String?)

    @Query("Update dictionaryData SET favorite = '1' WHERE id = :dictionaryID")
    fun updatefavoriteByDictionaryId(dictionaryID : String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDictionary(dictionaryList : ArrayList<DictionaryEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDictionary(dictionaryList : DictionaryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateDictionaryList(dictionaryList : ArrayList<DictionaryEntity>)

    @Delete
    fun deleteDictionary(placeEntity : DictionaryEntity)

    //@Query("Update dictionaryData SET favorite = '0' WHERE ")
    //fun deleteAllHistory(placeEntity : DictionaryEntity)
}