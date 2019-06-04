package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Flowable


@Dao
interface DictionaryDAO {

    @Query("SELECT * from dictionaryData")
    fun getAll() : Flowable<List<DictionaryEntity>>

    @Query("SELECT * FROM dictionaryData WHERE id = :dictionaryID")
    fun selectByDictionaryId(dictionaryID : String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(placeEntity : DictionaryEntity)

    @Query("DELETE from dictionaryData")
    fun deleteAll()

    @Query("DELETE FROM dictionaryData WHERE id = :dictionaryID")
    fun deleteByDictionaryId(dictionaryID : String?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllDictionary(dictionaryList : ArrayList<DictionaryEntity>)

}