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
import com.nunar.nun_ar_android_v1.adapter.SearchResultAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentSearchResultBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.SearchResultViewModel

class SearchResultFragment : Fragment() {

    val viewModel: SearchResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentSearchResultBinding>(inflater, R.layout.fragment_search_result, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val searchResultAdapter = SearchResultAdapter()
        binding.resultRvList.adapter = searchResultAdapter

        

        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkStatus.Error -> Toast.makeText(requireContext(), "${it.throwable.message}", Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    searchResultAdapter.submitList(it.data)
                }
            }
        })



        return binding.root
    }

}