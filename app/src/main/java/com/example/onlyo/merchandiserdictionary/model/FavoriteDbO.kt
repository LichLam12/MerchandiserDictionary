package com.example.onlyo.merchandiserdictionary.model

import java.io.Serializable

data class FavoriteDbO(var word : String= ""
                       , var spelling : String=""
                       , var wordkind: String=""
                       , var image: String=""
                       , var imagedescribe: String=""
                       , var vietmean: String=""
                       , var engmean: String=""
                       , var favorite: String="") : Serializable