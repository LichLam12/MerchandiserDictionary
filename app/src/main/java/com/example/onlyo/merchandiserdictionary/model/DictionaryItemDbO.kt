package com.example.onlyo.merchandiserdictionary.model

import java.io.Serializable

data class DictionaryItemDbO(var word : String= ""
                       , var spelling : String=""
                       , var wordkind: String=""
                       , var imagelink: String=""
                       , var vietmean: String=""
                       , var engmean: String=""
                       , var favorite: String="") : Serializable