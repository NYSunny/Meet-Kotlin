package com.johnny.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.johnny.base.utils.fixNotchScreen
import com.johnny.base.utils.fixSystemUI

/**
 * @author Johnny
 */
open class BaseUIActivity : AppCompatActivity() {

    var useSystemActionBar = true
    var fixSystemUIEnabled = false
    var statusBarColor = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixNotchScreen(this)
        if (fixSystemUIEnabled) fixSystemUI(this, statusBarColor)

        if (useSystemActionBar) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.elevation = 0F
            actionBarTitle()?.let { supportActionBar?.title = it }
        }
    }

    open fun actionBarTitle(): String? = null

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}