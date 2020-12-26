package com.rikkei.tranning.chatapp.views.uis

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rikkei.tranning.chatapp.R
import com.rikkei.tranning.chatapp.base.BaseFragment
import com.rikkei.tranning.chatapp.databinding.FragmentMainBinding
import com.rikkei.tranning.chatapp.views.adapters.ViewPagerAdapter
import com.rikkei.tranning.chatapp.views.uis.friend.SharedFriendViewModel
import com.rikkei.tranning.chatapp.views.uis.message.ChatViewModel

class MainFragment : BaseFragment<FragmentMainBinding, SharedFriendViewModel>() {

    private var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var currentTab = 0
    var chatViewModel: ChatViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager = view.findViewById(R.id.viewPager)

        tabLayout?.addTab(
            tabLayout!!.newTab().setCustomView(R.layout.custom_tablayout_main_message)
        )
        tabLayout?.addTab(tabLayout!!.newTab().setCustomView(R.layout.custom_tablayout_main_friend))
        tabLayout?.addTab(
            tabLayout!!.newTab().setCustomView(R.layout.custom_tablayout_main_profile)
        )

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = parentFragmentManager.let {
            context?.let { it1 ->
                ViewPagerAdapter(it1, it, tabLayout!!.tabCount)
            }
        }
        viewPager?.adapter = adapter

        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
                currentTab = tab.position
                val tabView = tab.customView
                tabView?.findViewById<ImageView>(R.id.iconTabMain)?.colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        ), PorterDuff.Mode.SRC_ATOP
                    )

                tabView?.findViewById<TextView>(R.id.textTabMain)
                    ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.blue))

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView = tab.customView

                tabView?.findViewById<ImageView>(R.id.iconTabMain)?.colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.iconMain
                        ), PorterDuff.Mode.SRC_ATOP
                    )

                tabView?.findViewById<TextView>(R.id.textTabMain)
                    ?.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel.updateStatus("status", "online")
        mViewModel.countNotifiRequest.observe(
            viewLifecycleOwner,
            Observer { s: String ->
                val viewRequest: View? =
                    mViewDataBinding.tabLayout.getTabAt(1)?.customView
                val count = viewRequest?.findViewById<View>(R.id.notifiMain) as TextView
                if (s == "0") count.visibility = View.GONE
                else {
                    count.visibility = View.VISIBLE
                    count.text = s
                }
            }
        )
        chatViewModel?.countUnReadMessage?.observe(viewLifecycleOwner, Observer { s: String ->
            val viewRequest: View? =
                mViewDataBinding.tabLayout.getTabAt(0)?.customView
            val count = viewRequest?.findViewById<View>(R.id.notifiMain) as TextView
            if (s == "0") count.visibility = View.GONE
            else {
                count.visibility = View.VISIBLE
                count.text = s
            }
        })
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mViewModel.updateStatus("status", "offline");
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mViewModel.updateStatus("status", "online");
//    }
//
//    override fun onStop() {
//        super.onStop()
//        mViewModel.updateStatus("status", "offline");
//
//    }

    override fun getBindingVariable(): Int {
        return BR.mainViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun getViewModel(): SharedFriendViewModel {
        chatViewModel =
            ViewModelProviders.of(requireActivity()).get(ChatViewModel::class.java)
        return ViewModelProviders.of(requireActivity()).get(SharedFriendViewModel::class.java)
    }
}