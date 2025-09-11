package com.test.schedulerapp.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.test.schedulerapp.R

object Navigator {

    //Activity Navigation
    fun <T : Activity> startActivity(context: Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        context.startActivity(intent)
    }

    //Replace fragment
    fun startSecondLVL(activity: AppCompatActivity, fragment: Fragment, addToBackStack: Boolean = true) {
        val transaction = activity.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment::class.java.simpleName)
        }

        transaction.commit()
    }

    //Pop backstack
    fun back(activity: AppCompatActivity) {
        activity.supportFragmentManager.popBackStack()
    }
}
