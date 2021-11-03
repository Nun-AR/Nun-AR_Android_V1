package com.nunar.nun_ar_android_v1.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import com.nunar.nun_ar_android_v1.adapter.PostAdapter
import com.nunar.nun_ar_android_v1.databinding.FragmentPostBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.PostViewModel
import java.io.File
import java.io.FileOutputStream
import java.lang.NumberFormatException
import java.net.URL

class PostFragment : Fragment() {

    val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DataBindingUtil.inflate<FragmentPostBinding>(inflater,
            R.layout.fragment_post,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        val suggestPostAdapter = PostAdapter()
        binding.postRecycler.adapter = suggestPostAdapter

        arguments?.getInt("postIdx")?.let {
            viewModel.getIdxPostResult(it)
        }

        viewModel.indexPostResult.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    val fileUrl = it.data.fileUrl

                    binding.postTvTitle.text = it.data.title
                    binding.postTvName.text = it.data.writer
                    binding.postTvTag.text = it.data.tag
                    binding.postBookmark.isSelected = it.data.isBookmarks
                    binding.postBookmarkCount.text = it.data.bookmarks.toString()

                    binding.postDownloadBtn.setOnClickListener { _ ->
                        Thread {
                            try{
                                val url = URL("https://nun-ar.com/model/${it.data.fileUrl}")
                                val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "")

                                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "${it.data.title}.gltf")

                                if(!folder.exists()) {
                                    folder.mkdirs()
                                }
                                if(file.exists()) {
                                    file.createNewFile()
                                }

                                url.openStream().use { input ->
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(requireContext(), "${it.data.title}.gltf 다운로드", Toast.LENGTH_SHORT).show()
                                    }
                                    FileOutputStream(file).use { outputStream ->
                                        input.copyTo(outputStream)
                                    }
                                }
                            } catch (e: Exception) {
                                requireActivity().runOnUiThread {
                                    e.printStackTrace()
                                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }.start()
                    }
                    binding.postArRunBtn.setOnClickListener {
                        val sceneViewIntent = Intent(Intent.ACTION_VIEW)
                        val url = Uri.parse("https://arvr.google.com/scene-viewer/1.0").buildUpon()
                            .appendQueryParameter("file",
                                "https://nun-ar.com/model/${fileUrl}")
                            .appendQueryParameter("mode", "3d_preferred")
                            .build()
                        sceneViewIntent.data = url
                        sceneViewIntent.setPackage("com.google.android.googlequicksearchbox")
                        startActivity(sceneViewIntent)
                    }

                    Glide.with(this)
                        .load("https://nun-ar.com/image/${it.data.thumbnail}")
                        .into(binding.postImageView)

                    Glide.with(this)
                        .load("https://nun-ar.com/image/${it.data.profileUrl}")
                        .into(binding.postUserImage)
                }
            }
        })

        viewModel.popularPostResult.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {
                }
                is NetworkStatus.Success -> {
                    suggestPostAdapter.submitList(it.data)
                }
            }
        })

        viewModel.bookmarkResult.observe(viewLifecycleOwner, {
            when (it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkStatus.Loading -> {
                }
                is NetworkStatus.Success -> {
                    try {
                        binding.postBookmarkCount.text =
                            binding.postBookmarkCount.text
                                .toString()
                                .toInt()
                                .plus(if (it.data) 1 else -1)
                                .toString()
                    } catch (e: NumberFormatException) {
                        Toast.makeText(requireContext(), "북마크 개수가 수가 아닙니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })

        binding.postBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        PostAdapter.onClick.observe(viewLifecycleOwner, {
            val action = PostFragmentDirections.actionPostFragmentSelf(it)
            findNavController().navigate(action)
        })

        return binding.root
    }
}