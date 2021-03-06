package com.nunar.nun_ar_android_v1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.PopularPostAdapter
import com.nunar.nun_ar_android_v1.adapter.PostAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentHomeBinding
import com.nunar.nun_ar_android_v1.model.Server.DOMAIN
import com.nunar.nun_ar_android_v1.model.Server.DOMAIN_FILE
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

        val recentPostAdapter = PostAdapter()
        binding.rvRecentPost.adapter = recentPostAdapter

        val popularPostAdapter = PopularPostAdapter()
        binding.vpPopularPost.adapter = popularPostAdapter

        viewModel.popularPostResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    popularPostAdapter.submitList(it.data)
                }
            }
        }

        viewModel.recentPostListResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Error -> {

                }
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    recentPostAdapter.submitList(it.data)
                }
            }
        }

        viewModel.userResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Error -> {

                }
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    Glide.with(requireActivity()).load("${DOMAIN_FILE}image/${it.data.profileUrl}")
                        .placeholder(R.mipmap.ic_launcher)
                        .into(binding.btnUserInfo)
                }
            }
        }

        PostAdapter.onClick.observe(this) {
            val action = HomeFragmentDirections.actionHomeFragmentToPostFragment(it)
            findNavController().navigate(action)
        }

        PopularPostAdapter.onClick.observe(this) {
            val action = HomeFragmentDirections.actionHomeFragmentToPostFragment(it)
            findNavController().navigate(action)
        }

        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_search_fragment)
        }

        binding.btnWrite.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_write_post_fragment)
        }

        binding.btnUserInfo.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_user_info_fragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}