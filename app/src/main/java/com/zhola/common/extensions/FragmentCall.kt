package com.zhola.common.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.zhola.R

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
//        setCustomAnimations(
//            R.anim.enter_from_left,
//            R.anim.exit_to_right,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left
//        )
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        replace(frameId, fragment).addToBackStack(fragment.javaClass.name)
    }
}

fun AppCompatActivity.replaceFragWithArgs(
    fragment: Fragment,
    frameId: Int,
    args: Bundle
) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
//        setCustomAnimations(
//            R.anim.enter_from_left,
//            R.anim.exit_to_right,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left
//        )
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        replace(frameId, fragment).addToBackStack(fragment.javaClass.name)
    }
}

fun AppCompatActivity.replaceFragWithArgsWithoutStack(
    fragment: Fragment,
    frameId: Int,
    args: Bundle
) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
//        setCustomAnimations(
//            R.anim.enter_from_left,
//            R.anim.exit_to_right,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left
//        )
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragWithoutStack(
    fragment: Fragment,
    frameId: Int
) {
    supportFragmentManager.inTransaction {
//        setCustomAnimations(
//            R.anim.enter_from_left,
//            R.anim.exit_to_right,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left
//        )
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragWithoutTransition(
    fragment: Fragment,
    frameId: Int
) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
    }
}

fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    frameId: Int,
    backStackTag: String? = null
) {
    supportFragmentManager.inTransaction {
//        setCustomAnimations(
//            R.anim.enter_from_left,
//            R.anim.exit_to_right,
//            R.anim.enter_from_right,
//            R.anim.exit_to_left
//        )
        setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        add(frameId, fragment)
        backStackTag?.let {
            addToBackStack(fragment.javaClass.name)
        }!!
    }
}