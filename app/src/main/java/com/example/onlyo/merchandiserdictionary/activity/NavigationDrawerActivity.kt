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
import android.widget.EditText
import android.app.Activity
//import android.app.Fragment
import android.content.Intent
import android.graphics.Rect
import android.view.*
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.fragment.*
import android.support.v4.app.Fragment
import com.example.onlyo.merchandiserdictionary.model.DictionaryItemDbO
import android.widget.Toast
import android.R.attr.name
import android.os.AsyncTask
import android.os.Environment
import android.support.v4.view.ViewCompat.isInLayout
import android.util.Log
import com.example.onlyo.merchandiserdictionary.database.DictionaryEntity
import com.example.onlyo.merchandiserdictionary.database.MerchandiseDicDB
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import java.io.File
import java.io.IOException


class NavigationDrawerActivity : AppCompatActivity(), WordListFragment.SendDataToFragmentInterface
        ,NavigationView.OnNavigationItemSelectedListener {


    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var toolbar: Toolbar
    lateinit var edittext: EditText
    //lateinit var rv_Favorite: RecyclerView

    private val compositeDisposable = CompositeDisposable()

    private lateinit var adapterFavoriteList : FavoriteListAdapter
    var favList = ArrayList<DictionaryItemDbO>()
    val dictionaryList = ArrayList<DictionaryEntity>()

    var number_of_word = 0

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigationdrawer)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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

        edittext = findViewById(R.id.edt_search)
        edittext.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            } else {
                //Log.d("focus", "focused")
            }

        }

        readData_justgetsize()
        loaddatefromfile()
        readfile()

        //viewPager = findViewById(R.id.viewpager)
        //tabLayout = findViewById(R.id.tabs)

        //setupViewPager()
        //tabLayout.setupWithViewPager(viewPager)
        //setIcon()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
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
    override fun sendData(stt: Int, word: String) {

        val fragment = DictionaryFragment()
        (fragment as DictionaryFragment).showInfor(stt,word)

        /*val dictionaryFragment = supportFragmentManager.findFragmentById(R.id.nav_search) as DictionaryFragment

        if (dictionaryFragment != null || dictionaryFragment!!.isInLayout()) { // kiem tra fragment can truyen data den co thuc su ton tai va dang hien
            dictionaryFragment!!.showInfor(stt, word)
        } else {
            Toast.makeText(applicationContext, "Fragment is not exist", Toast.LENGTH_LONG).show()
        }*/
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

    private fun setIcon() {
        //tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_tab1_24dp)
        //tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_image_24dp)
        //tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_note_24dp)
        //tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_tab1_24dp)
    }

    private fun loaddatefromfile(){
        for (index in 1..number_of_word){
            //favList.add(DictionaryItemDbO("","","","","","","0"))
            dictionaryList.add(DictionaryEntity(index.toString(),"","","","","","","0"))
        }
        //favList.add(FavoriteDbO("love","[lʌv]","danh từ",""
        //      ,"","","0"))
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

        var file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
        var file2 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/spelling.txt")
        var file3 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/wordkind.txt")
        var file4 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/imagelink.txt")
        var file5 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/vietmeaning.txt")
        var file6 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/engmeaning.txt")
        var file7 = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/favorite.txt")

        /*when (feildkind) {
            1 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
            2 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/spelling.txt")
            3 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/wordkind.txt")
            4 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/image.txt")
            5 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/vietmean.txt")
            6 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/engmean.txt")
            7 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/favorite.txt")
        }*/

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

            /*dictionaryList.forEach{
                Log.e("ket qua : ",it.toString())
            }*/

            //insertAllDictionary().execute(dictionaryList)
            //getDataFromLocal()
            /*when (feildkind) {
                1 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].word = it
                        count++
                    }
                2 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].spelling = it
                        count++
                    }
                3 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].wordkind = it
                        count++
                    }
                4 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].image = it
                        count++
                    }
                5 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].vietmean = it
                        count++
                    }
                6 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].engmean = it
                        count++
                    }
                7 ->
                    lineList.forEach {
                        //println("> " + it)
                        favList[count].favorite = it
                        count++
                    }
                *//*else -> { // Note the block
                    print("x is neither 1 nor 2")
                }*//*
            }*/
            //file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //return data
    }

    /*private fun getDataFromLocal()
    {
        // RxJava là thư viện mã nguồn mở implement ReactiveX trên Java. Có 2 lớp chính là Observable và Subscriber:
        // Observable là một lớp đưa ra dòng dữ liệu hoặc sự kiện (event). Flow của Observable là đưa ra một
        // hoặc nhiều các items, sau đó gọi kết thúc thành công hoặc lỗi.
        // Subscriber lắng nghe flow, thực thi các hành động trên dòng dữ liệu hoặc sự kiện được đưa ra bởi Observable

        //get all places from database room
        val getAllDictionary = MerchandiseDicDB.getInstance(context = this@NavigationDrawerActivity).dictionaryDataDao()

        // Probably, you already know that all UI code is done on Android Main thread.
        // RxJava is java library and it does not know about Android Main thread. That is the reason why we use RxAndroid.
        // RxAndroid gives us the possibility to choose Android Main thread as the thread where our code will be executed.
        // Obviously, our Observer should operate on Android Main thread.
        compositeDisposable.add(getAllDictionary.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ //excute event
                    //val adapter = ViewPagerAdapter(childFragmentManager,it)
                    //view_pager.adapter = adapter
                    Log.e("ket qua : ",it.toString())
                }, {
                    Log.e("", "" + it.message)
                }))
    }*/
   /* inner class insertAllDictionary() : AsyncTask<ArrayList<DictionaryEntity>, Void, Void>() {
        override fun doInBackground(vararg params : ArrayList<DictionaryEntity>): Void? {
            MerchandiseDicDB.getInstance(context = this@NavigationDrawerActivity).dictionaryDataDao().insertAllDictionary(params[0])
            //getDataFromLocal()
            return null
        }
    }*/

    @Throws(IOException::class)
    private fun readData_justgetsize() {
        var data = ""

        val path = Environment.getDataDirectory()
        val file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
        /*when (feildkind) {
            1 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/word.txt")
            2 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/spelling.txt")
            3 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/wordkind.txt")
            4 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/image.txt")
            5 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/vietmean.txt")
            6 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/engmean.txt")
            7 ->
                file = File(path, "/data/com.example.onlyo.merchandiserdictionary/files/favorite.txt")
        *//*else -> { // Note the block
            print("x is neither 1 nor 2")
        }*//*
        }*/
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

        // return data
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

        when (item.itemId) {
            R.id.nav_search -> {
                fragmentClass = DictionaryFragment::class.java
                // Handle the camera action
                //val homeIntent = Intent(this@NavigationDrawerActivity, NavigationDrawerActivity::class.java)
                //startActivity(homeIntent)
                //adapter.removeFragment()
                //adapter.notifyDataSetChanged()
                //setupViewPager()
                //viewPager.setCurrentItem(0,true)
            }
            R.id.nav_favorite -> {
                fragmentClass = FavoriteFagment::class.java
                //adapter.addFragmentforother(1,FavoriteFagment(),"")
                //adapter.addFragmentforother(2,DictionaryListFragment(),"")
                //adapter.addFragmentforother(3,ActivityLogFragment(),"")
                //adapter.notifyDataSetChanged()

            }
            R.id.nav_dictlist -> {
                fragmentClass = WordListFragment::class.java
            }
            R.id.nav_activitylog -> {
                fragmentClass = ActivityLogFragment::class.java
            }



        }
        try {
            fragment = fragmentClass!!.newInstance() as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }


        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
