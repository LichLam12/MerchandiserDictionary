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
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.activity.NavigationDrawerActivity
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.adapter.SearchListAdapter
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.io.File
import java.io.IOException
import java.util.*

/**
 * Created by onlyo on 4/10/2019.
 */
class FavoriteFagment : Fragment() {

    private lateinit var adapterFavoriteList : FavoriteListAdapter
    var wordList = ArrayList<DictionaryEntity>()
    lateinit var searchView: SearchView

    var number_of_word = 0
    private var listener: WordListFragment.SendDataToFragmentInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)


        val getAllDictionary =
                MerchandiseDicDB.getInstance(this@FavoriteFagment.context).dictionaryDataDao().getFavAll()

        val wordList_temp = ArrayList<DictionaryEntity>(getAllDictionary)
        wordList = wordList_temp

        adapterFavoriteList = FavoriteListAdapter(wordList,{context, word,  spelling, wordkind,meaning
                                                            ,vietmeaning,engmeaning,imagelink,favorite,id, i ->

            listener?.sendData(word.text.toString(),spelling, wordkind,meaning, vietmeaning, engmeaning, imagelink, favorite,id)
            Log.e("ket qua3 : ",DictionaryEntity(id,word.text.toString(),spelling,wordkind,meaning,vietmeaning,
                    engmeaning,imagelink,favorite).toString())

            showPopup(context,word,spelling, wordkind,meaning, vietmeaning, engmeaning, imagelink, favorite, id,i)
            //adapterFavoriteList.notifyDataSetChanged()
        })
        adapterFavoriteList.notifyDataSetChanged()

        //update search list by favorite list
        val activity_9 = (activity as NavigationDrawerActivity)
        activity_9.wordList = wordList
        activity_9.searchList = wordList
        activity_9.updateadaptersearch(wordList)
        /*val linearLayoutManager_search = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        activity_9.rv_search.layoutManager = linearLayoutManager_search
        activity_9.rv_search.adapter = adapterSearchList
        activity_9.rv_search.isNestedScrollingEnabled = false

        searchView = activity_9.findViewById(R.id.edt_search)
        searchView.setOnQueryTextListener(this)
*/

        val linearLayoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterFavoriteList
        view.rv_favorite.isNestedScrollingEnabled = false
        return view
    }

    //Click popup menu of any img
    private fun showPopup(context: Context, textView: TextView,spelling: String, wordkind: String,meaning:String, vietmeaning: String, engmeaning: String,
                          imagelink: String ,favorite: String, id:String,position: Int) {
        var popup: PopupMenu? = null
        popup = PopupMenu(context, textView)
        //Add only option (remove) of per img
        popup.menu.add(0, position, 0, "Xem")
        popup.menu.add(0, position, 0, "Xóa")
        popup.show()
        popup.setOnMenuItemClickListener({
            if(it.title == "Xem"){
                val b = Bundle()
                b.putString("word", textView.text.toString())
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
                fragmentManager?.beginTransaction()?.replace(R.id.flContent, fragment!!,"favlistfragment")?.addToBackStack("favlistfragment")?.commit()

            }
            else if(it.title == "Xóa"){

                var favorite_temp = "0"
                if(favorite == "1"){
                    favorite_temp = "0" //add to history
                } else if(favorite == "3"){ //exists in favorite list
                    favorite_temp = "2" //favorite word + searched word
                }
                val entity_word = DictionaryEntity(id,textView.text.toString(),spelling,wordkind,meaning,imagelink,vietmeaning,engmeaning,favorite_temp)
                val activity_9 = (activity as NavigationDrawerActivity)
                activity_9.updateFavWord().execute(entity_word)

                Toast.makeText(context,"Đã xóa khỏi danh sách từ vựng yêu thích", Toast.LENGTH_SHORT).show()
                wordList.removeAt(position)
                adapterFavoriteList.notifyItemRemoved(position)
                adapterFavoriteList.notifyDataSetChanged()


            }
            Log.e("key : ",(position.toString()))
           //deleteOneHistory(FavoriteListA
           //        .getRef(position).key!!,position) //get position id of rv_my_places
            //runagain++
            true
        })
    }



}