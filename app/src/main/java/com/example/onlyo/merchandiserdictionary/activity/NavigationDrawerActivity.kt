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
import android.graphics.Rect
import android.view.*
import com.example.onlyo.merchandiserdictionary.adapter.FavoriteListAdapter
import com.example.onlyo.merchandiserdictionary.fragment.*
import com.example.onlyo.merchandiserdictionary.model.FavoriteDbO


class NavigationDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var toolbar: Toolbar
    lateinit var edittext: EditText
    //lateinit var rv_Favorite: RecyclerView

    private lateinit var adapterFavoriteList : FavoriteListAdapter
    var favList = ArrayList<FavoriteDbO>()

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navigationdrawer)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        //rv_Favorite = findViewById(R.id.rv_favorite)

        edittext = findViewById(R.id.edt_search)
        edittext.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            } else {
                //Log.d("focus", "focused")
            }

        }
        /*layout_content.setOnClickListener({
            hideKeyboard(this)
        })*/
        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/


        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tabs)

        setupViewPager()
        tabLayout.setupWithViewPager(viewPager)
        setIcon()

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
    private lateinit var adapter: ViewPagerAdapter
    private fun setupViewPager() {
        adapter = ViewPagerAdapter(supportFragmentManager)
        //adapter.addFragment(HelloFragment(this), "Hello")
        adapter.addFragmentforother(0,MeaningTab1Fragment(), "Ý nghĩa")
        //adapter.addFragmentforother(1,SynonymTab2Fragment(), "Đồng Nghĩa")
        adapter.addFragmentforother(1, ExampleTab2Fragment(), "Ví dụ")
        adapter.addFragmentforother(2, ImageTab3Fragment(), "Hình ảnh")
        viewPager.adapter = adapter
    }

    private fun setIcon() {
        tabLayout.getTabAt(0)!!.setIcon(R.drawable.ic_tab1_24dp)
        tabLayout.getTabAt(1)!!.setIcon(R.drawable.ic_image_24dp)
        tabLayout.getTabAt(2)!!.setIcon(R.drawable.ic_note_24dp)
        //tabLayout.getTabAt(3)!!.setIcon(R.drawable.ic_tab1_24dp)
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
        when (item.itemId) {
            R.id.nav_search -> {
                // Handle the camera action
                //val homeIntent = Intent(this@NavigationDrawerActivity, NavigationDrawerActivity::class.java)
                //startActivity(homeIntent)
                tabLayout.visibility = View.VISIBLE
                adapter.removeFragment()
                adapter.notifyDataSetChanged()
                setupViewPager()
                tabLayout.setupWithViewPager(viewPager)
                setIcon()
                viewPager.setCurrentItem(0,true)
            }
            R.id.nav_favorite -> {
                //adapter.addFragment(HelloFragment(this), "Hello")
                adapter.addFragmentforother(3,FavoriteFagment(),"")
                adapter.addFragmentforother(4,DictionaryListFragment(),"")
                adapter.addFragmentforother(5,ActivityLogFragment(),"")
                adapter.notifyDataSetChanged()
                viewPager.setCurrentItem(3,true)
                tabLayout.visibility = View.GONE
            }
            R.id.nav_dictlist -> {
                adapter.addFragmentforother(3,FavoriteFagment(),"")
                adapter.addFragmentforother(4,DictionaryListFragment(),"")
                adapter.addFragmentforother(5,ActivityLogFragment(),"")
                adapter.notifyDataSetChanged()
                viewPager.setCurrentItem(4,true)
                tabLayout.visibility = View.GONE
            }
            R.id.nav_activitylog -> {
                adapter.addFragmentforother(3,FavoriteFagment(),"")
                adapter.addFragmentforother(4,DictionaryListFragment(),"")
                adapter.addFragmentforother(5,ActivityLogFragment(),"")
                adapter.notifyDataSetChanged()
                viewPager.setCurrentItem(5,true)
                tabLayout.visibility = View.GONE
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
