package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(
        DictionaryEntity::class, VietAnhEntity::class, YourWordEntity::class), version = 3, exportSchema = false)
abstract class MerchandiseDicDB : RoomDatabase() {

    abstract fun dictionaryDataDao(): DictionaryDAO
    abstract fun vietanhDataDao(): VietAnhDAO
    abstract fun yourwordDataDao(): YourWordDAO

    companion object {
        private var INSTANCE: MerchandiseDicDB? = null

        @Synchronized
        fun getInstance(context: Context): MerchandiseDicDB {
            if (INSTANCE == null) {
                synchronized(MerchandiseDicDB::class) {
                    INSTANCE = create(context)
                }
            }
            return INSTANCE as MerchandiseDicDB
        }

        fun create(context : Context) : MerchandiseDicDB{
            return Room.databaseBuilder(context,
                    MerchandiseDicDB::class.java, "Dictionary_horus")
                    .fallbackToDestructiveMigration()
                   .allowMainThreadQueries()
                    .build()
        }
    }
}