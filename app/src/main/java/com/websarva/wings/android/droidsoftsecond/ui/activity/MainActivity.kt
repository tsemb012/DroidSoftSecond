package com.websarva.wings.android.droidsoftsecond.ui.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.websarva.wings.android.droidsoftsecond.R
import com.websarva.wings.android.droidsoftsecond.databinding.A001ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<A001ActivityMainBinding>(this, R.layout.a001_activity_main)
    }
}