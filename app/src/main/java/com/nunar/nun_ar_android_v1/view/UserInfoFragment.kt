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
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.RecentPostAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentUserInfoBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.UserInfoViewModel

class UserInfoFragment : Fragment() {

    val viewModel: UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DataBindingUtil.inflate<FragmentUserInfoBinding>(layoutInflater, R.layout.fragment_user_info, container, false)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val bookmarkPostAdapter = RecentPostAdapter()
        val myPostAdapter = RecentPostAdapter()

        binding.rvBookmarkPost.adapter = bookmarkPostAdapter
        binding.rvUploadPost.adapter = myPostAdapter

        viewModel.getMyInfoResult.observe(viewLifecycleOwner, {
            when(it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this.requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {}
                is NetworkStatus.Success -> {
                    Glide.with(this.requireContext())
                        .load("https://nun-ar.com/image/${it.data.profileUrl}")
                        .into(binding.ivProfile)

                    binding.tvName.text = it.data.name

                    binding.btnModifyUserInfo.setOnClickListener { _ ->
                        val action = UserInfoFragmentDirections.actionUserInfoFragmentToModifyUserInfoFragment(it.data.name, it.data.profileUrl)
                        findNavController().navigate(action)
                    }
                }
            }
        })

        viewModel.getBookmarkPostResult.observe(viewLifecycleOwner, {
            when(it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this.requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {}
                is NetworkStatus.Success -> {
                    bookmarkPostAdapter.submitList(it.data)
                }
            }
        })

        viewModel.getMyPostResult.observe(viewLifecycleOwner, {
            when(it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this.requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {}
                is NetworkStatus.Success -> {
                    myPostAdapter.submitList(it.data)
                }
            }
        })

        RecentPostAdapter.onClick.observe(viewLifecycleOwner, {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToPostFragment(it)
            findNavController().navigate(action)
        })

        return binding.root
    }
}