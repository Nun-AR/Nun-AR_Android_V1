package com.nunar.nun_ar_android_v1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.PopularPostAdapter
import com.nunar.nun_ar_android_v1.adapter.RecentPostAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentHomeBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner

        val recentPostAdapter = RecentPostAdapter()
        binding.rvRecentPost.adapter = recentPostAdapter

        val popularPostAdapter = PopularPostAdapter()
        binding.vpPopularPost.adapter = popularPostAdapter

        viewModel.popularPostResult.observe(viewLifecycleOwner, {
            when(it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(), "${it.throwable.message}", Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    popularPostAdapter.submitList(it.data)
                }
            }
        })

        viewModel.recentPostListResult.observe(viewLifecycleOwner, {
            when(it) {
                is NetworkStatus.Error -> {

                }
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    recentPostAdapter.submitList(it.data)
                }
            }
        })

        RecentPostAdapter.onClick.observe(this, {
            val action = HomeFragmentDirections.actionHomeFragmentToPostFragment(it)
            findNavController().navigate(action)
        })

        PopularPostAdapter.onClick.observe(this, {
            val action = HomeFragmentDirections.actionHomeFragmentToPostFragment(it)
            findNavController().navigate(action)
        })

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_search_fragment)
        }

        return binding.root
    }

}