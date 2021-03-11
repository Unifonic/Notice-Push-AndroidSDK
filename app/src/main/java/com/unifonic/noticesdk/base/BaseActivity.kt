package com.unifonic.noticesdk.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unifonic.noticesdk.AndroidApp
import com.unifonic.noticesdk.di.modules.ViewModule

abstract class BaseActivity : AppCompatActivity() {

    abstract fun inject()

    abstract fun getViewModel(): BaseViewModel?

    abstract fun layoutId() : Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        layoutId()?.let {
            setContentView(it)
        }
    }
    private val loadingFragment: LoadingFragment = LoadingFragment()


    val component by lazy {
        app.component.plus(ViewModule(this))
    }

    val app: AndroidApp
        get() = application as AndroidApp



    fun showLoading() {
        if (!loadingFragment.isAdded) {
            loadingFragment.show(supportFragmentManager, "loading")
        }
    }

    fun hideLoading() {
        if (loadingFragment.isAdded) {
            loadingFragment.dismiss()
        }
    }

}