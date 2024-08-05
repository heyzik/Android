package com.example.musicapplication

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.openFragment(fragment: Fragment) {
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.viewPager, fragment)
        .commit()
}