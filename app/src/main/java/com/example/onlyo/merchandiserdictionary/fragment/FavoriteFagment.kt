package com.example.onlyo.merchandiserdictionary.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.model.FavoriteDbO
import kotlinx.android.synthetic.main.fragment_favorite.view.*

/**
 * Created by onlyo on 4/10/2019.
 */
class FavoriteFagment : Fragment() {
    private lateinit var adapterFavoriteList : FavoriteListAdapter
    var favList = ArrayList<FavoriteDbO>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        /*view.btn_add_location.setOnClickListener {
            val intent = Intent(this.context, AddLocationActivity::class.java)
            //after successlly addlocation -> refer to HomeAcrivity (code: 1234)
            startActivityForResult(intent, 1234)
        }*/
        //rv_favorite = findViewById(R.id.rv_favorite)
        loaddatefromfile()
        adapterFavoriteList = FavoriteListAdapter(favList,{


            //adapterFavoriteList.notifyDataSetChanged()
        })
        adapterFavoriteList.notifyDataSetChanged()

        val linearLayoutManager = LinearLayoutManager(this.activity, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        view.rv_favorite.layoutManager = linearLayoutManager
        view.rv_favorite.adapter = adapterFavoriteList
        view.rv_favorite.isNestedScrollingEnabled = false
        return view
    }
    private fun loaddatefromfile(){
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
        favList.add(FavoriteDbO("love","[lʌv]","danh từ","",
                "Mô tả hình ảnh: đây là từ thường được dùng trong công đoạn may bằng phương pháp tik tok.","","","0"))
    }
}