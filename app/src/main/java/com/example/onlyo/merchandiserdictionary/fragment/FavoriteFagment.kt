package com.example.onlyo.merchandiserdictionary.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.io.File
import java.io.IOException

/**
 * Created by onlyo on 4/10/2019.
 */
class FavoriteFagment : Fragment() {
    private lateinit var adapterFavoriteList : FavoriteListAdapter
    var favList = ArrayList<DictionaryItemDbO>()
    var number_of_word = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        /*view.btn_add_location.setOnClickListener {
            val intent = Intent(this.context, AddLocationActivity::class.java)
            //after successlly addlocation -> refer to HomeAcrivity (code: 1234)
            startActivityForResult(intent, 1234)
        }*/
        //rv_favorite = findViewById(R.id.rv_favorite)
        readData_justgetsize()
        loaddatefromfile()
        readData()
        adapterFavoriteList = FavoriteListAdapter(favList,{context,textview, i ->

            showPopup(context,textview, i)
            //adapterFavoriteList.notifyDataSetChanged()
        })
        adapterFavoriteList.notifyDataSetChanged()

        for (index in 0..(number_of_word-1)){
            println(">  " + favList[index].word)
        }

        val linearLayoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterFavoriteList
        view.rv_favorite.isNestedScrollingEnabled = false
        return view
    }
    private fun loaddatefromfile(){
        for (index in 1..number_of_word){
            favList.add(DictionaryItemDbO("","","","","","","0"))
        }
        //favList.add(FavoriteDbO("love","[lʌv]","danh từ",""
          //      ,"","","0"))
    }
    /**
     * Hàm đọc tập tin trong Android
     * Dùng openFileInput trong Android để đọc
     * ra FileInputStream
     */
    @Throws(IOException::class)
    private fun readData(): String {
        var data = ""

        val path = Environment.getDataDirectory()

        val file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
        try {
            // Make sure the Pictures directory exists.
            path.mkdirs()
            val bufferedReader = file.bufferedReader()

            val lineList = mutableListOf<String>()

            bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }
            var count = 0
            lineList.forEach {
                //println("> " + it)
                favList[count].word = it
                count++
                /*it.forEach {
                    if (count == 2 && (element == "“" || element == "”")) {
                        println(">  " + element.subSequence())
                    }
                    count++
                }*/
            }
            /*val text:List<String> = bufferedReader.readLines()
            for(line in text){
                Log.e("aaaa : ", line.subSequence(6,line.length-1).toString())
            }*/


            //file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return data
    }


    //Click popup menu of any img
    private fun showPopup(context: Context, textView: TextView, position: Int) {
        var popup: PopupMenu? = null
        popup = PopupMenu(context, textView)
        //Add only option (remove) of per img
        popup.menu.add(0, position, 0, "Xóa")
        popup.show()
        popup.setOnMenuItemClickListener({
            Log.e("key : ",(position.toString()))
           //deleteOneHistory(FavoriteListA
           //        .getRef(position).key!!,position) //get position id of rv_my_places
            //runagain++
            true
        })
    }


    @Throws(IOException::class)
    private fun readData_justgetsize(): String {
        var data = ""

        val path = Environment.getDataDirectory()

        val file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
        try {
            // Make sure the Pictures directory exists.
            path.mkdirs()
            val bufferedReader = file.bufferedReader()

            val lineList = mutableListOf<String>()

            bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }
            number_of_word = lineList.size

            //file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return data
    }
}