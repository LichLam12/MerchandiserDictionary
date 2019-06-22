package com.example.onlyo.merchandiserdictionary.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.onlyo.merchandiserdictionary.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mearningtab1.view.*
import android.text.Html
import android.util.Log
import com.example.onlyo.merchandiserdictionary.activity.NavigationDrawerActivity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Build
import android.view.*
import android.speech.tts.TextToSpeech
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.AppCompatRatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.view.*
import java.util.*


/**
 * Created by onlyo on 4/7/2019.
 */
class DictionaryFragment : Fragment(){

    var textToSpeech: TextToSpeech? = null
    //private var imgbtn_volume: ImageButton? = null
    lateinit var ratebar_addtofav: AppCompatRatingBar

    lateinit var tv_wordkind : TextView
    lateinit var tv_meaning : TextView
    lateinit var fab_directions : FloatingActionButton
    var wordList = ArrayList<DictionaryEntity>() //get all words

    //val vieww = getLayoutInflater()
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_mearningtab1, container, false)

        //        (getActivity() as AppCompatActivity).getSupportActionBar()



        textToSpeech = TextToSpeech(context, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR){
                //if there is no error then set language
                textToSpeech!!.language = Locale.US
            }
        })

        val activity_9 = (activity as NavigationDrawerActivity)
        ratebar_addtofav = activity_9.findViewById<View>(R.id.ratebar_addtofav) as AppCompatRatingBar

        var word_speak = ""
        var word_stt = "-1"
        var word_spelling = ""
        var word_wordkind = ""
        var word_eng = ""
        var word_viet =""
        var word_meaning = ""
        var word_link = ""
        var word_favorite = ""

        val bundle = this.arguments
        if (bundle != null) {
            //val myword = bundle.getString("word")
            word_stt = bundle.getString("id")
            val word = bundle.getString("word")
            word_speak = word //using to speakout
            val space = " ["
            val space2 = "]"
            var spelling = bundle.getString("spelling")
            word_spelling = spelling
            if(spelling == "0") spelling = ""
            else spelling = " ["+spelling+"]"
            val sourceString = "<b>$word</b>$spelling"
            view.tv_word.setText(Html.fromHtml(sourceString))

            var wordkind = bundle.getString("wordkind")
            if(wordkind != "0") {
                view.tv_wordkind.visibility = View.VISIBLE
                view.tv_wordkind.setText(bundle.getString("wordkind"))
            }
            else view.tv_wordkind.visibility = View.GONE

            var engmeaning = bundle.getString("engmeaning")
            var vietmeaning = bundle.getString("vietmeaning")
            if(engmeaning != "0" && vietmeaning != "0"){
                view.tv_engmeaning.visibility = View.VISIBLE
                view.tv_vietmeaning.visibility = View.VISIBLE
                view.tv_engmeaning.setText(engmeaning)
                view.tv_vietmeaning.setText(vietmeaning)
            } else {
                view.tv_engmeaning.visibility = View.GONE
                view.tv_vietmeaning.visibility = View.GONE
            }

            view.tv_meaning.visibility = View.VISIBLE
            view.tv_meaning.setText("=> "+bundle.getString("meaning"))

            val url = bundle.getString("imagelink")
            if(url != "0")
                Picasso.with(context).load(url).into(view.img_image)

            val favorite = bundle.getString("favorite")
            if(favorite == "1" || favorite == "3") {
                ratebar_addtofav.rating = 1.0F
            } else ratebar_addtofav.rating = 0.0F
            word_favorite = favorite

            view.fab_directions.visibility = View.VISIBLE

            //data to add to fav list
            word_wordkind = wordkind
            word_eng = engmeaning
            word_viet =vietmeaning
            word_meaning = bundle.getString("meaning")
            word_link = url

            //if(ratebar_addtofav.rating == 1.0F) word_favorite = "0"
            //else word_favorite = "1"
        } else {
            //to set Gone a few textview when open app
            tv_wordkind = view.findViewById<View>(R.id.tv_wordkind) as TextView
            tv_meaning = view.findViewById<View>(R.id.tv_meaning) as TextView
            fab_directions = view.findViewById<View>(R.id.fab_directions) as FloatingActionButton

            tv_wordkind.visibility = View.GONE
            tv_meaning.visibility = View.GONE
            fab_directions.visibility = View.GONE
        }
        view.fab_directions.setOnClickListener{word_speakOut(word_speak)}


        Log.e("WWord Stt : ",ratebar_addtofav.rating.toString())
        //fav icon - add to favlist
        //ratebar_addtofav = (getActivity() as AppCompatActivity).findViewById(R.id.ratebar_addtofav)
        //ratebar_addtofav.setOnClickListener{addtofavoritelist(word_stt)}

        ratebar_addtofav.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {

                if(word_stt != "-1"){

                    var favorite_temp = "0"

                    if(ratebar_addtofav.rating == 0.0F){
                        ratebar_addtofav.rating = 1.0F
                        if(word_favorite == "0"){
                            favorite_temp = "1" //add to favorite
                        } else if(word_favorite == "2"){ //searched
                            favorite_temp = "3" //favorite word + searched word
                        }
                        Toast.makeText(context,"Đã thêm vào danh sách từ vựng yêu thích",Toast.LENGTH_SHORT).show()
                    } else {
                        ratebar_addtofav.rating = 0.0F
                        if(word_favorite == "1"){
                            favorite_temp = "0"
                        } else if (word_favorite == "3"){
                            favorite_temp = "2"
                        }
                        Toast.makeText(context,"Đã xóa khỏi danh sách từ vựng yêu thích",Toast.LENGTH_SHORT).show()
                    }


                    Log.e("dictionary favorite9 : ",word_favorite)
                    val entity_word = DictionaryEntity((word_stt),word_speak,word_spelling,word_wordkind,word_meaning,word_link,word_viet,word_eng,favorite_temp)

                    activity_9.updateFavWord().execute(entity_word)


                }
                //activity_9.deleteWord().execute(60.toString())
            }
            true
        }


        //search
        val getAllDictionary =
                MerchandiseDicDB.getInstance(this@DictionaryFragment.context).dictionaryDataDao().getAll()

        val wordList_temp = ArrayList<DictionaryEntity>(getAllDictionary)
        wordList = wordList_temp
        //val activity_9 = (activity as NavigationDrawerActivity)
        activity_9.wordList = wordList
        activity_9.searchList = wordList
        activity_9.updateadaptersearch(wordList)
        activity_9.searchView.setQuery(word_speak,true)
        activity_9.flContent.visibility = View.VISIBLE
        activity_9.rv_search.visibility = View.GONE

        return view
    }



    /*override fun onBackPressed() {
        super.onBackPressed()
       *//* val intent = Intent(this, BottomNavigation::class.java) //this activity will be this fragment's father
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)*//*
    }*/
    // set text cho TextView
    fun showInfor(word: String, spelling: String, wordkind: String, vietmeaning: String, engmeaning: String,
                  imagelink: String ,favorite: String) {

        /*val tv_word = (this@DictionaryFragment as Activity).findViewById<View>(R.id.tv_word) as TextView
        val tv_wordkind = (this@DictionaryFragment as Activity).findViewById<View>(R.id.tv_wordkind) as TextView
        val tv_vietmeaning = (this@DictionaryFragment as Activity).findViewById<View>(R.id.tv_vietmeaning) as TextView
        val tv_engmeaning = (this@DictionaryFragment as Activity).findViewById<View>(R.id.tv_engmeaning) as TextView
        val img_image = (this@DictionaryFragment as Activity).findViewById<View>(R.id.img_image) as ImageView*/

        val word = word
        val space = " "
        val spelling = spelling
        val sourceString = "<b>$word</b>$space$spelling"
        //rootView!!.tv_word.setText(Html.fromHtml(sourceString))

        //tv_wordkind.setText(wordkind)
       // tv_engmeaning.setText(engmeaning)
        //tv_vietmeaning.setText(vietmeaning)

        val url = imagelink
        //val url3 = "https://www.upsieutoc.com/images/2019/04/15/678f4e578f07cb394.png"
       // Picasso.with(context).load(url).into(img_image)


    }


     /*@SuppressLint("StaticFieldLeak")
     inner class updateFavWord() : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg word_stt: String): Void? {
            val activity_9 = (getActivity() as NavigationDrawerActivity)
            ratebar_addtofav = activity_9.findViewById<View>(R.id.ratebar_addtofav) as AppCompatRatingBar

            var favorite = 0
            if(ratebar_addtofav.rating == 1.0F) {
                ratebar_addtofav.rating = 0.0F
                favorite = 0
            }
            else {
                ratebar_addtofav.rating = 1.0F
                favorite = 1
            }

            MerchandiseDicDB.getInstance(activity_9).dictionaryDataDao().updatefavoriteByDictionaryId(
                    word_stt.toString(),favorite.toString())
            //getDataFromLocal()
            return null
        }
    }*/

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


