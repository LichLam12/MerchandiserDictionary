package com.example.onlyo.merchandiserdictionary.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.activity.NavigationDrawerActivity
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.adapter.WordListAdapter
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.io.File
import java.io.IOException

/**
 * Created by onlyo on 4/10/2019.
 */
class WordListFragment : Fragment() {
    private lateinit var adapterWordList : WordListAdapter
    var wordList = ArrayList<DictionaryItemDbO>()
    var number_of_word = 0

    private var listener: SendDataToFragmentInterface? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is NavigationDrawerActivity)
            this.listener = context as SendDataToFragmentInterface? // gan listener vao MainActivity
        else
            throw RuntimeException(context!!.toString() + " must implement onViewSelected!")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_favorite, container, false)

        readData_justgetsize()
        loaddatefromfile()
        readData()
        adapterWordList = WordListAdapter(wordList) { context, textview, i ->


            listener?.sendData(i, textview.text.toString())
            //startActivity(intent)

            var fragment: Fragment? = null
            var fragmentClass: Class<*>? = null
            fragmentClass = DictionaryFragment::class.java

            try {
                fragment = fragmentClass!!.newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()
            //showPopup(context,textview, i)
            //adapterFavoriteList.notifyDataSetChanged()
        }
        //adapterWordList.notifyDataSetChanged()

        /*for (index in 0..(number_of_word-1)){
            println(">  " + wordList[index].word)
        }*/

        val linearLayoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterWordList
        view.rv_favorite.isNestedScrollingEnabled = false

        return view
    }

    interface SendDataToFragmentInterface {
        fun sendData(stt: Int, word: String)
    }

    private fun loaddatefromfile(){
        for (index in 1..number_of_word){
            wordList.add(DictionaryItemDbO("","","","","","","0"))
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
    private fun readData() {
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
                wordList[count].word = it
                count++
            }

            //file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //return data
    }



    @Throws(IOException::class)
    private fun readData_justgetsize() {
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

        //return data
    }
}