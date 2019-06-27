package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.*

@Dao
interface YourWordDAO {

    //@Query("SELECT * from yourwordData where id >= :start AND id <= :endd")
    //fun getAll_ItemBlock(start : Int, endd: Int) : List<YourWordEntity>


    @Query("SELECT * from yourwordData")
    fun getAll() : List<YourWordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllYourWord(yourwordList : ArrayList<YourWordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(yourwordEntity : YourWordEntity)

    @Delete
    fun delete(yourwordEntity : YourWordEntity)

}