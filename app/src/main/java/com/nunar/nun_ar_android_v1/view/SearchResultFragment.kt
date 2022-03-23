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
import com.nunar.nun_ar_android_v1.adapter.PostAdapter
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

        val searchResultAdapter = PostAdapter()
        binding.resultRvList.adapter = searchResultAdapter

        val searchKeyword = arguments?.getString("searchWord")
        viewModel.getSearchList(searchKeyword?: "")
        binding.resultTvResult.text = "${searchKeyword}의 검색 결과"

        viewModel.searchResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    searchResultAdapter.submitList(it.data)
                }
            }
        }

        binding.resultBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        PostAdapter.onClick.observe(this) {
            val action = SearchResultFragmentDirections.actionSearchResultFragmentToPostFragment(it)
            findNavController().navigate(action)
        }

        return binding.root
    }

}