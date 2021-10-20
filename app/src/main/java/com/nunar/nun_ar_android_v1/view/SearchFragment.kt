package com.nunar.nun_ar_android_v1.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.RecentSearchWordAdapter
import com.nunar.nun_ar_android_v1.adapter.SuggestionTagAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentSearchBinding
import com.nunar.nun_ar_android_v1.repository.SearchRepository
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.SearchViewModel
import com.nunar.nun_ar_android_v1.viewmodel_factory.SearchViewModelFactory
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    val viewModel: SearchViewModel by viewModels { SearchViewModelFactory(SearchRepository(this.requireContext())) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSearchBinding>(inflater,
            R.layout.fragment_search,
            container,
            false)

        binding.vm = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        val suggestionTagAdapter = SuggestionTagAdapter()
        binding.rvSuggestionSearchWord.adapter = suggestionTagAdapter
        binding.rvSuggestionSearchWord.layoutManager =
            FlexboxLayoutManager(this.requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }

        val recentSearchWordAdapter = RecentSearchWordAdapter()
        binding.rvRecentlySearchWord.adapter = recentSearchWordAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getRecentSearchWord()
        }

        viewModel.suggestionTagResult.observe(this.viewLifecycleOwner, {
            when (it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this.requireContext(),
                        "${it.throwable.message}",
                        Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    suggestionTagAdapter.submitList(it.data)
                }
            }
        })

        viewModel.recentSearchWordResult.observe(this.viewLifecycleOwner, {
            when (it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this.requireContext(),
                        "${it.throwable.message}",
                        Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    recentSearchWordAdapter.submitList(it.data)
                }
            }
        })

        binding.btnSearch.setOnClickListener {
            binding.etSearchWord.requestFocus()
            (this.requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .apply {
                    toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY)
                }
        }

        binding.etSearchWord.setOnKeyListener { _, keyCode, _ ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    binding.etSearchWord.clearFocus()
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.addRecentSearchWord(binding.etSearchWord.text.toString())
                    }
                    val action =
                        SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(binding.etSearchWord.text.toString())
                    findNavController().navigate(action)
                }
                else -> return@setOnKeyListener false
            }

            return@setOnKeyListener true
        }

        SuggestionTagAdapter.onClick.observe(this.viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.addRecentSearchWord(it)
                val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(it)
                findNavController().navigate(action)
            }
        })

        RecentSearchWordAdapter.onClick.observe(viewLifecycleOwner, {
            lifecycleScope.launch {
                viewModel.addRecentSearchWord(it)
                val action = SearchFragmentDirections.actionSearchFragmentToSearchResultFragment(it)
                findNavController().navigate(action)
            }
        })

        binding.tvEraseAll.setOnClickListener {
            lifecycleScope.launch {
                viewModel.removeAllSearchWord()
                viewModel.getRecentSearchWord()
            }
        }

        return binding.root
    }
}