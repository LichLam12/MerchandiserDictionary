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






/**
 * Created by onlyo on 4/7/2019.
 */
class MeaningTab1Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_mearningtab1, container, false)

        /*view.btn_add_location.setOnClickListener {
            val intent = Intent(this.context, AddLocationActivity::class.java)
            //after successlly addlocation -> refer to HomeAcrivity (code: 1234)
            startActivityForResult(intent, 1234)
        }*/
        val word = "accessory"
        val space = " "
        val spelling = "[əkˈsesəri]"
        val sourceString = "<b>$word</b>$space$spelling"
        view.tv_word.text = Html.fromHtml(sourceString)

        val url2 = "https://www.upsieutoc.com/images/2019/04/15/DL.jpg"
        val url = "https://www.upsieutoc.com/images/2019/04/15/14fc782b7c47bc73c.jpg"
        val url3 = "https://www.upsieutoc.com/images/2019/04/15/678f4e578f07cb394.png"
        Picasso.with(view.context).load(url).into(view.img_image)
        val p = readData()
        Log.e("gia tri: ",p)
        return view
    }

    /**
     * Hàm đọc tập tin trong Android
     * Dùng openFileInput trong Android để đọc
     * ra FileInputStream
     */
    @Throws(IOException::class)
    private fun readData(): String {
        var data = ""
        //val rd = context.resources.assets.open("dictionary.txt")

        /*val bufferedReader = file.bufferedReader()
        val text:List<String> = bufferedReader.readLines()
        for(line in text){
            println(line)
        }*/
        //var iStream: InputStream = File("dictionary.txt").inputStream()

        //var rd = BufferedReader(FileReader(File("dictionary.txt")))
        val path = Environment.getDataDirectory()

        val file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/dictionary.txt")
        //Log.e("aaaa : ",path.toString())

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs()
            val bufferedReader = file.bufferedReader()

            val lineList = mutableListOf<String>()

            bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }
            var count = 0
            lineList.forEach {
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