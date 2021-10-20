package com.nunar.nun_ar_android_v1.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.SuggestPostAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentPostBinding
import com.nunar.nun_ar_android_v1.model.response.PostResponse
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
                    val fileUrl = it.data.fileUrl

                    binding.postTvTitle.text = it.data.title
                    binding.postTvName.text = it.data.writer
                    binding.postTvTag.text = it.data.tag
                    binding.postBookmark.isSelected = it.data.isBookmarks
                    binding.postBookmark.setOnClickListener {

                    }
                    binding.postBookmarkCount.text = it.data.bookmarks.toString()

                    binding.postDownloadBtn.setOnClickListener {

                    }
                    binding.postArRunBtn.setOnClickListener {
                        var sceneViewIntent: Intent = Intent(Intent.ACTION_VIEW)
                        val url = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                            .appendQueryParameter("file",
                                "http://3.37.250.4:8080/model/${fileUrl}")
                            .appendQueryParameter("mode", "3d_preferred")
                            .build()
                        sceneViewIntent.data = url
                        sceneViewIntent.setPackage("com.google.android.googlequicksearchbox")
                        startActivity(sceneViewIntent)
                    }

                    Glide.with(this)
                        .load("http://3.37.250.4:8080/image/${it.data.thumbnail}")
                        .into(binding.postImageView)
                }
            }
        })

        viewModel.popularPostResult.observe(viewLifecycleOwner, Observer {
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