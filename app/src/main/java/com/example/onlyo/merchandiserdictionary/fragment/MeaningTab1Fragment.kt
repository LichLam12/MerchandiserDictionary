package com.example.onlyo.merchandiserdictionary.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlyo.merchandiserdictionary.R

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
        return view
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