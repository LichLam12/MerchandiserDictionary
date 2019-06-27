package com.example.onlyo.merchandiserdictionary.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.activity.NavigationDrawerActivity
import com.example.onlyo.merchandiserdictionary.adapter.VietAnhListAdapter
import com.example.onlyo.merchandiserdictionary.adapter.YourWordAdapter
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import com.example.onlyo.merchandiserdictionary.database.VietAnhEntity
import com.example.onlyo.merchandiserdictionary.database.YourWordEntity
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.abc_screen_toolbar.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragmentchild_vietanh.view.*
import java.util.*

class YourWordFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var adapterYourWordList : YourWordAdapter
    var wordList = ArrayList<YourWordEntity>()
    var wordAll = ArrayList<YourWordEntity>()
    var yourword_size = 0

    var textToSpeech: TextToSpeech? = null
    //lateinit var imgv_volume: ImageView

    lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_favorite, container, false)

        //get data
        val getAllDictionary =
                MerchandiseDicDB.getInstance(this@YourWordFragment.context).yourwordDataDao().getAll()

        wordAll = ArrayList<YourWordEntity>(getAllDictionary)
        //how to refer list -> arraylist
        var wordList_temp = ArrayList<YourWordEntity>(getAllDictionary)

        /*yourword_size = wordAll.size
        if(wordAll.size >= 21) {
            val getAllDictionary2 =
                    MerchandiseDicDB.getInstance(this@YourWordFragment.context).yourwordDataDao().getAll_ItemBlock(1,21)
            wordList_temp = ArrayList<YourWordEntity>(getAllDictionary2)
        }
        wordList = wordList_temp*/
        wordList = wordAll
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

        adapterYourWordList = YourWordAdapter(wordAll,
                {
                    word_speak = it
                    word_speakOut(word_speak)
                    view.imgv_volume.setOnClickListener{word_speakOut(word_speak)}
                },{context,textview,engword,position ->
                    showPopup(context,textview,engword,position)
                }

        )


        //search (update with allword list)
        val activity_9 = (activity as NavigationDrawerActivity)
        searchView = activity_9.edt_yourwordsearch
        searchView.setOnQueryTextListener(this)


        //show add your word dialog when click "+"
        activity_9.imgbtn_addyourword.setOnClickListener(){
            showDialogAddYourWord()
        }

        //add layout
        val linearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,
                false).apply { isAutoMeasureEnabled = true }
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        //linearLayoutManager.setAutoMeasureEnabled(false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterYourWordList
        view.rv_favorite.isNestedScrollingEnabled = false
        //show per 21 items to avoid big data
        /*view.rv_favorite.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom()
            }
        })*/

        return view
    }

    private fun showDialogAddYourWord() {
        val alterDialog : AlertDialog.Builder = AlertDialog.Builder(this.context)
        alterDialog.setTitle("Thêm từ của bạn")

        val inflater : LayoutInflater = LayoutInflater.from(this.context)
        val dialogView =    inflater.inflate(R.layout.dialog_addyourword,null)
        alterDialog.setView(dialogView)
        val edt_engword  = dialogView.findViewById<View>(R.id.edt_engword) as MaterialEditText
        val edt_vietword = dialogView.findViewById<View>(R.id.edt_vietword) as MaterialEditText

        alterDialog.setPositiveButton("Thêm") { dialog, _ ->
            //add to Room
            if(!edt_engword.text.isEmpty() && !edt_vietword.text.isEmpty()){
               /* if(wordAll.isEmpty()) yourword_size = 0
                else yourword_size = wordAll.lastIndex+1*/
                wordAll.add(YourWordEntity(edt_engword.text.toString(),edt_vietword.text.toString()))
                insertYourWord().execute(YourWordEntity(edt_engword.text.toString(),edt_vietword.text.toString()))
                adapterYourWordList.notifyDataSetChanged()
                Toast.makeText(this.context,"Đã thêm vào từ của bạn" , Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this.context,"Vui lòng không để trống từ khi nhập" , Toast.LENGTH_SHORT).show()
        }

        alterDialog.setNegativeButton("Hủy", { dialog, whichButton ->
            dialog.cancel()
        })
        alterDialog.create().show()
    }

    //Click popup menu of any img
    private fun showPopup(context: Context, textView: TextView,engword: String,position: Int) {
        var popup: PopupMenu? = null
        popup = PopupMenu(context, textView)
        //Add only option (remove) of per img
        popup.menu.add(0, position, 0, "Xóa")
        popup.show()
        popup.setOnMenuItemClickListener({
            if(it.title == "Xóa"){
                wordAll.removeAt(position)
                deleteYourWord().execute(YourWordEntity(textView.text.toString(),engword))
                //adapterYourWordList.notifyItemRemoved(position)
                adapterYourWordList.notifyDataSetChanged()
                Toast.makeText(context,"Đã xóa khỏi từ của bạn", Toast.LENGTH_SHORT).show()
            }
            true
        })
    }

    inner class insertYourWord() : AsyncTask<YourWordEntity, Void, Void>() {
        override fun doInBackground(vararg params: YourWordEntity): Void? {
            MerchandiseDicDB.getInstance(this@YourWordFragment.context).yourwordDataDao().insert(params[0])
            return null
        }
    }

    inner class deleteYourWord() : AsyncTask<YourWordEntity, Void, Void>() {
        override fun doInBackground(vararg params: YourWordEntity): Void? {
            MerchandiseDicDB.getInstance(this@YourWordFragment.context).yourwordDataDao().delete(params[0])
            return null
        }
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
            for (i in x until y) {
                wordList.add(wordAll.get(i))
            }
            adapterYourWordList.notifyDataSetChanged()
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }
    var searchList = ArrayList<YourWordEntity>()
    override fun onQueryTextChange(newText: String): Boolean {
        Log.e("search list : ",newText.toString())

        if(newText == "" || newText.length == 0){
            wordList = wordAll
            adapterYourWordList.filter(wordList)
        }
        else {
            var charText = newText
            charText = charText.toLowerCase(Locale.getDefault())
            //wordList.clear()

            val searchList_temp = ArrayList<YourWordEntity>()
            for (wp in searchList) {
                //Log.e("search list2 : ",wp.toString())
                if (wp.vietword.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchList_temp.add(wp)
                    //wordList.add(wp)
                    //Log.e("search list2 : ",wp.word.toString())
                }
            }
            wordList = searchList_temp

            adapterYourWordList.filter(wordList)
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
}