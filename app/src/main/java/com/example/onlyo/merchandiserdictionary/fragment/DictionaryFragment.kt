package com.example.onlyo.merchandiserdictionary.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.onlyo.merchandiserdictionary.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mearningtab1.view.*
import kotlinx.android.synthetic.main.fragmentchild_favorite.view.*
import android.text.Html
import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import android.content.res.AssetManager
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.widget.Switch
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import android.content.Intent.getIntent
import kotlinx.android.synthetic.main.fragment_mearningtab1.*


/**
 * Created by onlyo on 4/7/2019.
 */
class DictionaryFragment : Fragment() {

    var favList = ArrayList<DictionaryItemDbO>()
    var number_of_word = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_mearningtab1, container, false)

        /*view.btn_add_location.setOnClickListener {
            val intent = Intent(this.context, AddLocationActivity::class.java)
            //after successlly addlocation -> refer to HomeAcrivity (code: 1234)
            startActivityForResult(intent, 1234)
        }*/

        favList.add(0,DictionaryItemDbO("","","","","","","0"))

        val word = "accessory"
        val space = " "
        val spelling = "[əkˈsesəri]"
        val sourceString = "<b>$word</b>$space$spelling"
        view.tv_word.text = Html.fromHtml(sourceString)

        //val url2 = "https://www.upsieutoc.com/images/2019/04/15/DL.jpg"
        val url = "https://www.upsieutoc.com/images/2019/04/15/14fc782b7c47bc73c.jpg"
        //val url3 = "https://www.upsieutoc.com/images/2019/04/15/678f4e578f07cb394.png"
        Picasso.with(view.context).load(url).into(view.img_image)
        //val p = readData()
        //Log.e("gia tri: ",p)

        //readData_justgetsize()
        //loaddatefromfile()
        //getwordbystt(1)

        return view
    }

    // set text cho TextView
    fun showInfor(stt: Int, word: String) {
        //tvName.setText(name)
        //tvAge.setText(age)
        val word = word
        val space = " "
        val spelling = "[əkˈsesəri]"
        val sourceString = "<b>$word</b>$space$spelling"
        Log.e("word : ",word + ", "+ stt)
        //tv_word.text = Html.fromHtml(sourceString)

        //getwordbystt(stt)
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1234){ //way to start Kotlin activity
            if (resultCode == Activity.RESULT_OK){
                val intent = Intent(context, BottomNavigation::class.java) //this activity will be this fragment's father
                //update fragments of HomeActivity
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                //Tao bỏ FLAG_ACTIVITY_CLEAR_TASK để tránh lỗi thêm vị trí xong về lại homeactivity đc 1 lúc bị out ra nha
                startActivity(intent)
            }
        }
    }*/
}