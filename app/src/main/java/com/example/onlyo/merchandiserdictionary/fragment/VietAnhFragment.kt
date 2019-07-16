package com.example.onlyo.merchandiserdictionary.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.activity.NavigationDrawerActivity
import com.example.onlyo.merchandiserdictionary.adapter.VietAnhListAdapter
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import com.example.onlyo.merchandiserdictionary.database.VietAnhEntity
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_mearningtab1.view.*
import kotlinx.android.synthetic.main.fragmentchild_vietanh.view.*
import java.util.*

/**
 * Created by onlyo on 4/7/2019.
 */
class VietAnhFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var adapterVietAnhList : VietAnhListAdapter
    var wordList = ArrayList<VietAnhEntity>()
    var wordAll = ArrayList<VietAnhEntity>()

    var textToSpeech: TextToSpeech? = null
    //lateinit var imgv_volume: ImageView

    lateinit var searchView: SearchView

    lateinit var imgbtn_volume: ImageButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_favorite, container, false)

        //get data
        val getAllDictionary =
                MerchandiseDicDB.getInstance(this@VietAnhFragment.context).vietanhDataDao().getAll()
        wordAll = ArrayList<VietAnhEntity>(getAllDictionary)
        //how to refer list -> arraylist
        val getAllDictionary2 =
                MerchandiseDicDB.getInstance(this@VietAnhFragment.context).vietanhDataDao().getAll_ItemBlock(1,21)
        val wordList_temp = ArrayList<VietAnhEntity>(getAllDictionary.subList(1,21))
        wordList = wordList_temp
        searchList = wordAll

        //text to speech
        var word_speak = "a"
        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                textToSpeech!!.language = Locale.US
            }
        })
        //imgv_volume = view.findViewById<View>(R.id.imgv_volume) as ImageView

        adapterVietAnhList = VietAnhListAdapter(wordList,
                {
                    word_speak = it
                    word_speakOut(word_speak)
                    //view.imgv_volume.setOnClickListener{word_speakOut(word_speak)}
                })


        //search (update with allword list)
        val activity_9 = (activity as NavigationDrawerActivity)
        searchView = activity_9.edt_vietanhsearch
        searchView.setOnQueryTextListener(this)
        //activity_9.wordList = wordList
        //activity_9.searchList = wordList
        //activity_9.updateadaptersearch(wordList)

        /*for (index in 0..(number_of_word-1)){
            println(">  " + wordList[index].word)
        }*/

        //volume icon - speech to text
        imgbtn_volume = activity_9.imgbtn_vietanh_volume
        imgbtn_volume.setOnClickListener{getSpeechInput()}


        val linearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,
                false).apply { isAutoMeasureEnabled = true }
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //linearLayoutManager.setAutoMeasureEnabled(false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterVietAnhList
        view.rv_favorite.isNestedScrollingEnabled = false
        //show per 21 items to avoid big data
        view.rv_favorite.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView:RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom()
            }
        })

        return view
    }
    //show per 21 items to avoid big data
    private fun onScrolledToBottom() {
        if (wordList.size < wordAll.size) {
            val x: Int
            val y: Int
            if (wordAll.size - wordList.size >= 21) {
                x = wordList.size
                y = x + 21
            } else {
                x = wordList.size
                y = x + wordAll.size - wordList.size
            }
            for (i in x+1 until y) {
                wordList.add(wordAll.get(i))
            }
            adapterVietAnhList.notifyDataSetChanged()
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }
    var searchList = ArrayList<VietAnhEntity>()
    override fun onQueryTextChange(newText: String): Boolean {
        Log.e("search list : ",newText.toString())

        if(newText == "" || newText.length == 0){
        }
        else {
            var charText = newText
            charText = charText.toLowerCase(Locale.getDefault())
            //wordList.clear()

            val searchList_temp = ArrayList<VietAnhEntity>()
            for (wp in searchList) {
                //Log.e("search list2 : ",wp.toString())
                if (wp.engword.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchList_temp.add(wp)
                    //wordList.add(wp)
                    //Log.e("search list2 : ",wp.word.toString())
                }
            }
            wordList = searchList_temp

            adapterVietAnhList.filter(wordList)
        }
        return false
    }

    private fun word_speakOut (word_speak:String) {
        val text = word_speak
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        }
    }

    override fun onDestroy() {
        // Shutdown TTS
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }


    fun getSpeechInput() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())

        if (intent.resolveActivity(this.context.packageManager) != null) {
            startActivityForResult(intent, 10)
        } else {
            Toast.makeText(this.context, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && data != null)
            {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                //searchView.setText(result[0])
                searchView.setQuery(result[0],false)
            }
        }
    }
}
