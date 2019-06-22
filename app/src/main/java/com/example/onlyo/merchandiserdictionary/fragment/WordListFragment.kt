package com.example.onlyo.merchandiserdictionary.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
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
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Created by onlyo on 4/10/2019.
 */
class WordListFragment : Fragment() {
    private lateinit var adapterWordList : WordListAdapter
    var wordList = ArrayList<DictionaryEntity>()
    var number_of_word = 0
    private val compositeDisposable = CompositeDisposable()

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

       /* readData_justgetsize()
        loaddatefromfile()
        readData()*/
        //getAllDictionary().execute()
        val getAllDictionary =
        MerchandiseDicDB.getInstance(this@WordListFragment.context).dictionaryDataDao().getAll()

        //how to refer list -> arraylist
        val wordList_temp = ArrayList<DictionaryEntity>(getAllDictionary)
        wordList = wordList_temp
        wordList.forEach { dsp ->
            Log.e("ket qua2 : ", dsp.toString())
        }

        adapterWordList = WordListAdapter(wordList) { context, word,  spelling, wordkind,meaning
                                                      ,vietmeaning,engmeaning,imagelink,favorite,id, i ->


            listener?.sendData(word.text.toString(),spelling, wordkind,meaning, vietmeaning, engmeaning, imagelink, favorite,id)
            Log.e("ket qua3 : ",DictionaryEntity(id,word.text.toString(),spelling,wordkind,meaning,vietmeaning,
                    engmeaning,imagelink,favorite).toString())


            val b = Bundle()
            b.putString("word", word.text.toString())
            b.putString("spelling",spelling)
            b.putString("wordkind",wordkind)
            b.putString("meaning",meaning)
            b.putString("vietmeaning",vietmeaning)
            b.putString("engmeaning",engmeaning)
            b.putString("imagelink",imagelink)
            b.putString("favorite",favorite)
            b.putString("id",id)

            var fragment: Fragment? = null

            var fragmentClass: Class<*>? = null
            fragmentClass = DictionaryFragment::class.java

            try {
                fragment = fragmentClass!!.newInstance() as Fragment
            } catch (e: Exception) {
                e.printStackTrace()
            }
            fragment!!.arguments = b
            val fragmentManager = fragmentManager
            fragmentManager?.beginTransaction()?.replace(R.id.flContent, fragment!!,"wordlistfragment")?.addToBackStack("wordlistfragment")?.commit()

        }
        adapterWordList.notifyDataSetChanged()


        //search (update with allword list)
        val activity_9 = (activity as NavigationDrawerActivity)
        activity_9.wordList = wordList
        activity_9.searchList = wordList
        activity_9.updateadaptersearch(wordList)

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
        fun sendData(word: String, spelling: String, wordkind: String, meaning:String, vietmeaning: String, engmeaning: String,
                     imagelink: String ,favorite: String,id:String)
    }


}