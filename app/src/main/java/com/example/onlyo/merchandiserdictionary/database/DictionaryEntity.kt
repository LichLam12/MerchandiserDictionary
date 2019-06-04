package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "dictionaryData")
data class DictionaryEntity (@PrimaryKey var id : String,
                            @ColumnInfo(name = "word") var word : String,
                            @ColumnInfo(name = "spelling") var spelling: String,
                            @ColumnInfo(name = "wordkind") var wordkind: String,
                            @ColumnInfo(name = "imagelink") var imagelink: String,
                            @ColumnInfo(name = "vietmean") var vietmean: String,
                            @ColumnInfo(name = "engmean") var engmean: String,
                            @ColumnInfo(name = "favorite") var favorite: String) {
    @Ignore constructor():this("","","","","","","","0")
}