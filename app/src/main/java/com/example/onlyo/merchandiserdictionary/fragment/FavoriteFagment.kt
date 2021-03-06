package com.example.onlyo.merchandiserdictionary.fragment

import android.app.AlertDialog
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
    var wordList = ArrayList<DictionaryEntity>() //show per 30 items
    var wordAll = ArrayList<DictionaryEntity>()
    lateinit var searchView: SearchView

    var number_of_word = 0
    private var listener: WordListFragment.SendDataToFragmentInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)


        val getAllDictionary =
                MerchandiseDicDB.getInstance(this@FavoriteFagment.context).dictionaryDataDao().getFavAll()
        wordAll = ArrayList<DictionaryEntity>(getAllDictionary)
        var wordList_temp = ArrayList<DictionaryEntity>(getAllDictionary)
        if(wordAll.size >= 30) {
            val getAllDictionary2 =
                    MerchandiseDicDB.getInstance(this@FavoriteFagment.context).dictionaryDataDao().getFav_ItemBlock(1,30)
            wordList_temp = ArrayList<DictionaryEntity>(getAllDictionary.subList(1,30))
        }
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
        activity_9.wordAll = wordAll
        activity_9.searchList = wordAll
        activity_9.updateadaptersearch(wordList)

        //remove all when click deleteallicon
        activity_9.imgbtn_deleteall2.setOnClickListener(){
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Xóa từ yêu thích")
            alertDialogBuilder
                    .setMessage("Bạn có muốn xóa tất cả từ yêu thích hay không?")
                    .setCancelable(false)
                    .setPositiveButton("Có") { dialog, id ->

                        var update_wordList = ArrayList<DictionaryEntity>()

                        wordList.forEach {
                            var favorite_temp = "0"
                            if(it.favorite == "1") favorite_temp = "0"
                            else if(it.favorite == "3") favorite_temp = "2"
                            val entity_word = DictionaryEntity(it.id,it.word,it.spelling,it.wordkind,it.meaning,it.imagelink,it.vietmean,it.engmean,favorite_temp)
                            update_wordList.add(entity_word)
                        }
                        activity_9.updateWordList().execute(update_wordList)
                        wordList.removeAll(wordList)
                        adapterFavoriteList.notifyDataSetChanged()

                        Toast.makeText(context, "Đã xóa tất cả lịch sử tìm kiếm", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Không") { dialog, id ->
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel()
                    }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        val linearLayoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterFavoriteList
        view.rv_favorite.isNestedScrollingEnabled = false
        //show per 30 items to avoid big data
        view.rv_favorite.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom()
            }
        })
        return view
    }

    //show per 30 items to avoid big data
    private fun onScrolledToBottom() {
        if (wordList.size < wordAll.size) {
            val x: Int
            val y: Int
            if (wordAll.size - wordList.size >= 30) {
                x = wordList.size
                y = x + 30
            } else {
                x = wordList.size
                y = x + wordAll.size - wordList.size
            }
            for (i in x+1 until y) {
                wordList.add(wordAll.get(i))
            }
            adapterFavoriteList.notifyDataSetChanged()
        }
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