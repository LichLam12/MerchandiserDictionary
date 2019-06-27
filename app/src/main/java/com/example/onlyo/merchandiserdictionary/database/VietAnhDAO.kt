package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.*

@Dao
interface VietAnhDAO {

    @Query("SELECT * from vietanhData where id >= :start AND id <= :endd")
    fun getAll_ItemBlock(start : Int, endd: Int) : List<VietAnhEntity>


    @Query("SELECT * from vietanhData")
    fun getAll() : List<VietAnhEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVietAnh(vietanhList : ArrayList<VietAnhEntity>)

}