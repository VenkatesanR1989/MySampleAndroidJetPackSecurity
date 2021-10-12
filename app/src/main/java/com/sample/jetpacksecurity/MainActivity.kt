package com.sample.jetpacksecurity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.jetpacksecurity.databinding.ActivityMainBinding


/**
 * MainActivity to show a list of encrypted files as well as options to set a master password
 * to be required when opening files and the option to add new files by downloading and
 * encrypting them.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            lifecycleOwner = this@MainActivity
        }
    }
}
