package com.dicoding.dicodingsubmission_aplikasigithubuserextended.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class UserFollowPagerAdapter(activity: FragmentActivity, private val username: String) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = UserFollowFragment()
        val typeFollow = when (position) {
            0 -> "following"
            1 -> "followers"
            else -> ""
        }
        fragment.arguments = Bundle().apply {
            putString(UserFollowFragment.FOLLOW_TYPE, typeFollow)
            putString(UserFollowFragment.USERNAME, username)
        }
        return fragment
    }
}