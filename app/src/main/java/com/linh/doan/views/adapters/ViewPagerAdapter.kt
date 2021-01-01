package com.linh.doan.views.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.linh.doan.views.uis.friend.FriendFragment
import com.linh.doan.views.uis.message.MessageFragment
import com.linh.doan.views.uis.profile.ProfileFragment

class ViewPagerAdapter(
    private val myContext: Context,
    fm: FragmentManager,
    internal var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> {
                return MessageFragment()
            }
            1 -> {
                return FriendFragment()
            }
            else -> return ProfileFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}