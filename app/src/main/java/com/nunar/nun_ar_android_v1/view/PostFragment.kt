package com.nunar.nun_ar_android_v1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.SuggestPostAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentPostBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.PostViewModel

class PostFragment : Fragment() {

    val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPostBinding>(inflater, R.layout.fragment_post, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val suggestPostAdapter = SuggestPostAdapter()
        binding.postRecycler.adapter = suggestPostAdapter

        viewModel.indexPostResult.observe(viewLifecycleOwner, Observer {
            when(it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(), "${it.throwable.message}", Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    suggestPostAdapter.submitList(it.data)
                }
            }
        })

        return binding.root
    }
}