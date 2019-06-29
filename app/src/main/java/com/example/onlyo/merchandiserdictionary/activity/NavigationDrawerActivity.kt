package com.example.onlyo.merchandiserdictionary.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.example.onlyo.merchandiserdictionary.R
import com.example.onlyo.merchandiserdictionary.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.navigationdrawer.*
import android.view.inputmethod.InputMethodManager
import android.app.Activity
//import android.app.Fragment
import android.content.Intent
import android.graphics.Rect
import android.view.*
import com.example.onlyo.merchandiserdictionary.fragment.*
import android.support.v4.app.Fragment
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.speech.RecognizerIntent
import android.support.annotation.RequiresApi
import android.support.v7.widget.AppCompatRatingBar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.example.onlyo.merchandiserdictionary.Class.Suggestion
import com.example.onlyo.merchandiserdictionary.adapter.SearchListAdapter
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import com.example.onlyo.merchandiserdictionary.database.VietAnhEntity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.app_bar_navigation_drawer.*

import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class NavigationDrawerActivity : AppCompatActivity(), WordListFragment.SendDataToFragmentInterface
        ,NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    lateinit var toolbar: Toolbar
    //lateinit var rv_Favorite: RecyclerView
    lateinit var searchView: SearchView
    lateinit var imgbtn_volume: ImageButton

    private val compositeDisposable = CompositeDisposable()

    val dictionaryList = ArrayList<DictionaryEntity>()
    var wordList = ArrayList<DictionaryEntity>() //per 30 items
    var wordAll = ArrayList<DictionaryEntity>()
    var searchList = ArrayList<DictionaryEntity>()
    lateinit var adapterSearchList : SearchListAdapter

    //vietenglist
    val vietanhList = ArrayList<VietAnhEntity>()

    var number_of_word = 0 //dictionaryList
    var number_of_word2 = 0 //vietenglist

    //to set rating = 0 when refer to any fragment
    lateinit var ratebar_addtofav: AppCompatRatingBar

    private var listener: WordListFragment.SendDataToFragmentInterface? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigationdrawer)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Giao dien 9: DictionaryFragment
        var fragment: Fragment? = null
        var fragmentClass: Class<*>? = null
        fragmentClass = DictionaryFragment::class.java
        try {
            fragment = fragmentClass!!.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()


        //volume icon - speech to text
        imgbtn_volume = findViewById(R.id.imgbtn_volume)
        imgbtn_volume.setOnClickListener{getSpeechInput()}

        //to set rating = 0 when refer to any fragment
        ratebar_addtofav = this.findViewById<View>(R.id.ratebar_addtofav) as AppCompatRatingBar

        /*//Just run once
        //dictionary
        readData_justgetsize()
        loaddatefromfile()
        //viet anh
        readData_justgetsize2()
        loaddatefromfile2()
        readfile()*/

        //Search data is all words (show per 30 items)
        val getAllDictionary =
                MerchandiseDicDB.getInstance(this).dictionaryDataDao().getAll()
        wordAll = ArrayList<DictionaryEntity>(getAllDictionary)
        //get 21 first words to show in advance
        val getAllDictionary2 =
                MerchandiseDicDB.getInstance(this).dictionaryDataDao().getAll_ItemBlock(1,30)
        //how to refer list -> arraylist
        val wordList_temp = ArrayList<DictionaryEntity>(getAllDictionary2)
        wordList = wordList_temp
        wordList.forEach { dsp ->
            mSuggestions.add(Suggestion(dsp.word))
        }
        searchList = wordList
        adapterSearchList = SearchListAdapter(wordList) { context, word, spelling, wordkind, meaning
                                                             , vietmeaning, engmeaning, imagelink, favorite, id, i ->


            listener?.sendData(word.text.toString(),spelling, wordkind,meaning, vietmeaning, engmeaning, imagelink, favorite,id)
            Log.e("ket qua3 : ",DictionaryEntity(id,word.text.toString(),spelling,wordkind,meaning,vietmeaning,
                    engmeaning,imagelink,favorite).toString())

            //save to history list
            var favorite_temp = "0"
            if(favorite == "0"){
                favorite_temp = "2" //add to history
            } else if(favorite == "1"){ //exists in favorite list
                favorite_temp = "3" //favorite word + searched word
            } else if(favorite == "2"){
                favorite_temp = "2"
            } else if (favorite == "3"){
                favorite_temp = "3"
            }
            val entity_word = DictionaryEntity(id,word.text.toString(),spelling,wordkind,meaning,imagelink,vietmeaning,engmeaning,favorite_temp)
            this.updateFavWord().execute(entity_word)

            //send to dictionaryFragment
            val b = Bundle()
            b.putString("word", word.text.toString())
            b.putString("spelling",spelling)
            b.putString("wordkind",wordkind)
            b.putString("meaning",meaning)
            b.putString("vietmeaning",vietmeaning)
            b.putString("engmeaning",engmeaning)
            b.putString("imagelink",imagelink)
            b.putString("favorite",favorite_temp)
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
            fragmentManager?.beginTransaction()?.replace(R.id.flContent, fragment!!,"searchfragment")?.addToBackStack("searchfragment")?.commit()

            flContent.visibility = View.VISIBLE
            rv_search.visibility = View.GONE
        }
        adapterSearchList.notifyDataSetChanged()

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        //val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_search.layoutManager = linearLayoutManager
        rv_search.adapter = adapterSearchList
        rv_search.isNestedScrollingEnabled = false
        //show per 21 items to avoid big data
        rv_search.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx:Int, dy:Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1))
                    onScrolledToBottom()
            }
        })

        searchView = findViewById<SearchView>(R.id.edt_search)
        searchView.setOnQueryTextListener(this)

        //add navigationdrawer
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
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
            for (i in x until y) {
                wordList.add(wordAll.get(i))
            }
            adapterSearchList.notifyDataSetChanged()
        }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }
    //when search tab's data has any change
    override fun onQueryTextChange(newText: String): Boolean {
        Log.e("search list : ",newText.toString())

        if(newText == "" || newText.length == 0){
            flContent.visibility = View.VISIBLE
            rv_search.visibility = View.GONE
        }
        else {
            rv_search.visibility = View.VISIBLE
            flContent.visibility = View.GONE

            var charText = newText
            charText = charText.toLowerCase(Locale.getDefault())
            //wordList.clear()

            val searchList_temp = ArrayList<DictionaryEntity>()
            for (wp in searchList) {
                //Log.e("search list2 : ",wp.toString())
                if (wp.word.toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchList_temp.add(wp)
                    //wordList.add(wp)
                    //Log.e("search list2 : ",wp.word.toString())
                }
            }
            wordList = searchList_temp
            /*for (wp in wordList) {
                Log.e("search list2 : ",wp.word.toString())
            }*/
            /*if (charText.length == 0) {
                Log.e("search list2 : ",1.toString())
                wordList.addAll(searchList)
                adapterSearchList.notifyDataSetChanged()
            } else {
                for (wp in searchList) {
                    Log.e("search list2 : ",wp.toString())
                    if (wp.word.toLowerCase(Locale.getDefault()).contains(charText)) {
                        wordList.add(wp)
                        Log.e("search list2 : ",wp.toString())
                    }
                }
            }*/
            adapterSearchList.filter(wordList)
        }
        return false
    }

    fun updateadaptersearch(wordList:ArrayList<DictionaryEntity>){
        adapterSearchList.filter(wordList)
    }


    private val mSuggestions = ArrayList<Suggestion>()
    //Search
    private fun getSuggestion(query:String):List<Suggestion> {
        val suggestions = ArrayList<Suggestion>()
        for (suggestion in mSuggestions)
        {
            if (suggestion.body.toLowerCase().contains(query.toLowerCase()))
            {
                suggestions.add(suggestion)
            }
        }
        return suggestions
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    //Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    //Send data from WordList -> Dictionary Fragment
    override fun sendData(word: String, spelling: String, wordkind: String,meaning:String, vietmeaning: String, engmeaning: String,
                          imagelink: String ,favorite: String, id: String) {

        val fragment = DictionaryFragment()
        (fragment as DictionaryFragment).showInfor(word,spelling,wordkind,vietmeaning,engmeaning,imagelink,favorite)
    }

    fun getSpeechInput() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US)

        if (intent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(intent, 10)
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            10 -> if (resultCode == Activity.RESULT_OK && data != null)
            {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                //searchView.setText(result[0])
                searchView.setQuery(result[0],false)
 /*               val fragment = (fragmentManager.findFragmentByTag("vietanhfragment").activity)
                val fragment2 = (fragmentManager.findFragmentByTag("yourwordfragment").activity)

                if(fragment.edt_vietanhsearch.visibility == View.VISIBLE){
                    fragment.edt_vietanhsearch.setQuery(result[0],false)
                } else if(fragment2.edt_yourwordsearch.visibility == View.VISIBLE){
                    fragment2.edt_yourwordsearch.setQuery(result[0],false)
                }*/


            }
        }
    }



    private lateinit var adapter: ViewPagerAdapter
    /*private fun setupViewPager(fragment:  Fragment) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        //adapter.addFragment(HelloFragment(this), "Hello")
        adapter.addFragmentforother(0,fragment, "")

        //adapter.addFragmentforother(1,SynonymTab2Fragment(), "Đồng Nghĩa")
        //adapter.addFragmentforother(1, ExampleTab2Fragment(), "Ví dụ")
        //adapter.addFragmentforother(2, ImageTab3Fragment(), "Hình ảnh")
        viewPager.adapter = adapter
    }*/


    private fun loaddatefromfile(){
        for (index in 1..number_of_word){
            dictionaryList.add(DictionaryEntity(index.toString(),"","","","","","","","0"))
        }
    }

    private fun loaddatefromfile2(){
        for (index in 1..number_of_word2){
            vietanhList.add(VietAnhEntity(index,"",""))
        }
    }
    /**
     * Hàm đọc tập tin trong Android
     * Dùng openFileInput trong Android để đọc
     * ra FileInputStream
     */
    @Throws(IOException::class)
    private fun readfile() {
        var data = ""

        val path = Environment.getDataDirectory()
        val file = File(path,"/data/com.example.onlyo.merchandiserdictionary/files/word.txt")

        //var file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
        /*var file = File(path, "Desktop/files/word.txt")
        var file2 = File(path, "/local/tmp/files/spelling.txt")
        var file3 = File(path, "/local/tmp/files/wordkind.txt")
        var file4 = File(path, "/local/tmp/files/imagelink.txt")
        var file5 = File(path, "/local/tmp/files/vietmeaning.txt")
        var file6 = File(path, "/local/tmp/files/engmeaning.txt")
        var file7 = File(path, "/local/tmp/files/favorite.txt")*/
        var file2 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/spelling.txt")
        var file3 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/wordkind.txt")
        var file4 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/imagelink.txt")
        var file5 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/vietmeaning.txt")
        var file6 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/engmeaning.txt")
        var file7 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/favorite.txt")
        var file8 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/meaning.txt")

        //viet eng
        var file9 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/vviet.txt")
        var file10 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/veng.txt")

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs()
            val bufferedReader = file.bufferedReader()

            //val lineList = mutableListOf<String>()
            var count = 0

            /*bufferedReader.useLines { lines ->
                if(lines.contains(lines.indexOf(stt))){
                    lineList.add(0, lines.index(stt))}
            }*/
            bufferedReader.useLines { lines -> lines.forEach {
                dictionaryList[count++].word = it
                    //lineList.add(it)
                }
            }

            /////////////////////////////////////////

            val bufferedReader2 = file2.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader2.useLines { lines -> lines.forEach {
                dictionaryList[count++].spelling = it
                }
            }

            /////////////////////////////////////////

            val bufferedReader3 = file3.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader3.useLines { lines -> lines.forEach {
                dictionaryList[count++].wordkind = it
                }
            }

            /////////////////////////////////////////

            val bufferedReader4 = file4.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader4.useLines { lines -> lines.forEach {
                dictionaryList[count++].imagelink = it
                }
            }

            /////////////////////////////////////////

            val bufferedReader5 = file5.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader5.useLines { lines -> lines.forEach {
                dictionaryList[count++].vietmean = it
                }
            }

            /////////////////////////////////////////

            val bufferedReader6 = file6.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader6.useLines { lines -> lines.forEach {
                dictionaryList[count++].engmean = it
                }
            }

            /////////////////////////////////////////

            val bufferedReader7 = file7.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader7.useLines { lines -> lines.forEach {
                dictionaryList[count++].favorite = it
                }
            }

            val bufferedReader8 = file8.bufferedReader()
            count = 0
            //bufferedReader.useLines { lines ->lineList.add(1, lines.elementAt(stt))}
            bufferedReader8.useLines { lines -> lines.forEach {
                dictionaryList[count++].meaning = it
            }
            }

            //print all ditionary of dictionaryList
            /*dictionaryList.forEach{
                Log.e("ket qua : ",it.toString())
               // MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).dictionaryDataDao().insert(it)
            }*/

            //read 2 files of vietanhlist
            val bufferedReader9 = file9.bufferedReader()
            count = 0
            bufferedReader9.useLines { lines -> lines.forEach {
                vietanhList[count++].vietword = it
            }
            }

            val bufferedReader10 = file10.bufferedReader()
            count = 0
            bufferedReader10.useLines { lines -> lines.forEach {
                vietanhList[count++].engword = it
            }
            }

            //print all viet anh files of vietanhlist
            /*vietanhList.forEach{
                Log.e("ket qua : ",it.toString())
                // MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).dictionaryDataDao().insert(it)
            }*/

            insertAllDictionary().execute(dictionaryList)
            insertAllVietAnh().execute(vietanhList)
            //file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //return data
    }

    private fun getDataFromLocal()
    {
        // RxJava là thư viện mã nguồn mở implement ReactiveX trên Java. Có 2 lớp chính là Observable và Subscriber:
        // Observable là một lớp đưa ra dòng dữ liệu hoặc sự kiện (event). Flow của Observable là đưa ra một
        // hoặc nhiều các items, sau đó gọi kết thúc thành công hoặc lỗi.
        // Subscriber lắng nghe flow, thực thi các hành động trên dòng dữ liệu hoặc sự kiện được đưa ra bởi Observable

        //get all places from database room
        val getAllDictionary = MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).dictionaryDataDao().getAll()
        /*getAllDictionary.forEach { dsp ->
            Log.e("ket qua2 : ", dsp.toString())
        }*/

        // Probably, you already know that all UI code is done on Android Main thread.
        // RxJava is java library and it does not know about Android Main thread. That is the reason why we use RxAndroid.
        // RxAndroid gives us the possibility to choose Android Main thread as the thread where our code will be executed.
        // Obviously, our Observer should operate on Android Main thread.
        /*compositeDisposable.add(getAllDictionary.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ //excute event
                    //val adapter = ViewPagerAdapter(childFragmentManager,it)
                    //view_pager.adapter = adapter
                    Log.e("ket qua2 : ", it.toString())
                }, {
                    Log.e("error: ", "" + it.message)
                }))*/
    }
    inner class insertAllDictionary() : AsyncTask<ArrayList<DictionaryEntity>, Void, Void>() {
        override fun doInBackground(vararg params : ArrayList<DictionaryEntity>): Void? {
            MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).dictionaryDataDao().insertAllDictionary(params[0])
            //getDataFromLocal()
            return null
        }
    }

    inner class updateFavWord() : AsyncTask<DictionaryEntity, Void, Void>() {
        override fun doInBackground(vararg word_stt: DictionaryEntity): Void? {

            MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).dictionaryDataDao().updateDictionary(word_stt[0])
            //getDataFromLocal()
            return null
        }
    }

    inner class updateWordList() : AsyncTask<ArrayList<DictionaryEntity>, Void, Void>() {
        override fun doInBackground(vararg word_list: ArrayList<DictionaryEntity>): Void? {

            MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).dictionaryDataDao().updateDictionaryList(word_list[0])
            //getDataFromLocal()
            return null
        }
    }

    inner class insertAllVietAnh() : AsyncTask<ArrayList<VietAnhEntity>, Void, Void>() {
        override fun doInBackground(vararg params : ArrayList<VietAnhEntity>): Void? {
            MerchandiseDicDB.getInstance(this@NavigationDrawerActivity).vietanhDataDao().insertAllVietAnh(params[0])
            //getDataFromLocal()
            return null
        }
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
    }

    @Throws(IOException::class)
    private fun readData_justgetsize2() {
        var data = ""

        val path = Environment.getDataDirectory()
        val file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/vviet.txt")

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs()
            val bufferedReader = file.bufferedReader()

            val lineList = mutableListOf<String>()

            bufferedReader.useLines { lines -> lines.forEach { lineList.add(it) } }
            number_of_word2 = lineList.size

            //file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    //Hide ban phim
    fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation_drawer, menu)
        return true
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }*/


    @SuppressLint("InflateParams")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        var fragment: Fragment? = null
        var fragmentClass: Class<*>? = null
        var fragmentTag = ""
        //tv_word = this.findViewById<View>(R.id.tv_word) as TextView


        when (item.itemId) {
            R.id.nav_search -> {
                fragmentTag = "dictionaryfragment"
                fragmentClass = DictionaryFragment::class.java
                ratebar_addtofav.rating = 0.0F
                //vi tri cuoi - 4 icon thay phien nhau
                ratebar_addtofav.visibility = View.VISIBLE
                imgbtn_deleteall.visibility = View.GONE //history
                imgbtn_deleteall2.visibility = View.GONE //favorite
                imgbtn_addyourword.visibility = View.GONE

                edt_search.visibility = View.VISIBLE
                edt_vietanhsearch.visibility = View.GONE
                edt_yourwordsearch.visibility = View.GONE

                imgbtn_volume.visibility = View.VISIBLE
                imgbtn_vietanh_volume.visibility = View.GONE
                imgbtn_yourword_volume.visibility = View.GONE
                // Handle the camera action
                //val homeIntent = Intent(this@NavigationDrawerActivity, NavigationDrawerActivity::class.java)
                //startActivity(homeIntent)
                //adapter.removeFragment()
                //adapter.notifyDataSetChanged()
                //setupViewPager()
                //viewPager.setCurrentItem(0,true
            }
            R.id.nav_favorite -> {
                fragmentTag = "favlistfragment"
                fragmentClass = FavoriteFagment::class.java
                ratebar_addtofav.rating = 0.0F
                //vi tri cuoi - 4 icon thay phien nhau
                ratebar_addtofav.visibility = View.GONE
                imgbtn_deleteall.visibility = View.GONE //history
                imgbtn_deleteall2.visibility = View.VISIBLE //favorite
                imgbtn_addyourword.visibility = View.GONE

                edt_search.visibility = View.VISIBLE
                edt_vietanhsearch.visibility = View.GONE
                edt_yourwordsearch.visibility = View.GONE

                imgbtn_volume.visibility = View.VISIBLE
                imgbtn_vietanh_volume.visibility = View.GONE
                imgbtn_yourword_volume.visibility = View.GONE
                //adapter.addFragmentforother(1,FavoriteFagment(),"")
                //adapter.addFragmentforother(2,DictionaryListFragment(),"")
                //adapter.addFragmentforother(3,ActivityLogFragment(),"")
                //adapter.notifyDataSetChanged()
            }
            R.id.nav_yourword -> {
                fragmentTag = "yourwordfragment"
                fragmentClass = YourWordFragment::class.java
                ratebar_addtofav.rating = 0.0F
                //vi tri cuoi - 4 icon thay phien nhau
                ratebar_addtofav.visibility = View.GONE
                imgbtn_deleteall.visibility = View.GONE //history
                imgbtn_deleteall2.visibility = View.GONE //favorite
                imgbtn_addyourword.visibility = View.VISIBLE

                edt_search.visibility = View.GONE
                edt_vietanhsearch.visibility = View.GONE
                edt_yourwordsearch.visibility = View.VISIBLE

                imgbtn_volume.visibility = View.GONE
                imgbtn_vietanh_volume.visibility = View.GONE
                imgbtn_yourword_volume.visibility = View.VISIBLE
                //adapter.addFragmentforother(1,FavoriteFagment(),"")
                //adapter.addFragmentforother(2,DictionaryListFragment(),"")
                //adapter.addFragmentforother(3,ActivityLogFragment(),"")
                //adapter.notifyDataSetChanged()
            }
            R.id.nav_dictlist -> {
                fragmentTag = "wordlistfragment"
                fragmentClass = WordListFragment::class.java
                ratebar_addtofav.rating = 0.0F
                //vi tri cuoi - 4 icon thay phien nhau
                ratebar_addtofav.visibility = View.VISIBLE
                imgbtn_deleteall.visibility = View.GONE //history
                imgbtn_deleteall2.visibility = View.GONE //favorite
                imgbtn_addyourword.visibility = View.GONE

                edt_search.visibility = View.VISIBLE
                edt_vietanhsearch.visibility = View.GONE
                edt_yourwordsearch.visibility = View.GONE

                imgbtn_volume.visibility = View.VISIBLE
                imgbtn_vietanh_volume.visibility = View.GONE
                imgbtn_yourword_volume.visibility = View.GONE
            }
            R.id.nav_vietanh -> {
                fragmentTag = "vietanhfragment"
                fragmentClass = VietAnhFragment::class.java
                ratebar_addtofav.rating = 0.0F
                //vi tri cuoi - 4 icon thay phien nhau
                ratebar_addtofav.visibility = View.GONE
                imgbtn_deleteall.visibility = View.GONE //history
                imgbtn_deleteall2.visibility = View.GONE //favorite
                imgbtn_addyourword.visibility = View.GONE

                edt_search.visibility = View.GONE
                edt_vietanhsearch.visibility = View.VISIBLE
                edt_yourwordsearch.visibility = View.GONE

                imgbtn_volume.visibility = View.GONE
                imgbtn_vietanh_volume.visibility = View.VISIBLE
                imgbtn_yourword_volume.visibility = View.GONE
            }
            R.id.nav_activitylog -> {
                fragmentTag = "activitylogfragment"
                fragmentClass = ActivityLogFragment::class.java
                ratebar_addtofav.rating = 0.0F
                //vi tri cuoi - 4 icon thay phien nhau
                ratebar_addtofav.visibility = View.GONE
                imgbtn_deleteall.visibility = View.VISIBLE //history
                imgbtn_deleteall2.visibility = View.GONE //favorite
                imgbtn_addyourword.visibility = View.GONE
                //thanh search - co 3 loại thanh search
                edt_search.visibility = View.VISIBLE
                edt_vietanhsearch.visibility = View.GONE
                edt_yourwordsearch.visibility = View.GONE

                imgbtn_volume.visibility = View.VISIBLE
                imgbtn_vietanh_volume.visibility = View.GONE
                imgbtn_yourword_volume.visibility = View.GONE
            }
        }

        try {
            fragment = fragmentClass!!.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        searchView.setQuery("",true)
        flContent.visibility = View.VISIBLE
        rv_search.visibility = View.GONE

        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment,fragmentTag).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
