package com.example.onlyo.merchandiserdictionary.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.onlyo.merchandiserdictionary.R

/**
 * Created by onlyo on 4/7/2019.
 */
class ImageTab4Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view  = inflater.inflate(R.layout.fragment_imagetab4, container, false)

        /*view.btn_add_location.setOnClickListener {
            val intent = Intent(this.context, AddLocationActivity::class.java)
            //after successlly addlocation -> refer to HomeAcrivity (code: 1234)
            startActivityForResult(intent, 1234)
        }*/
        return view
    }
}