package com.example.onlyo.merchandiserdictionary.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "yourwordData")
class YourWordEntity (@PrimaryKey @ColumnInfo(name = "vietword") var vietword : String,
                      @ColumnInfo(name = "engword") var engword: String) {
    @Ignore constructor():this("","")
}