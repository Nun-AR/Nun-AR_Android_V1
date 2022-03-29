package com.nunar.nun_ar_android_v1.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.databinding.FragmentWritePostBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.WritePostViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.jar.Manifest

class WritePostFragment : Fragment() {

    private val CODE_GET_FILE = 1001
    private val CODE_GET_IMAGE = 1002

    val viewModel: WritePostViewModel by viewModels()
    lateinit var binding: FragmentWritePostBinding
    val checkInput = IntArray(2)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate<FragmentWritePostBinding>(inflater,
            R.layout.fragment_write_post,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewModel

        checkPermission()

        binding.writeBackBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fileUploadBtn.setOnClickListener {
            val intent = Intent().setType("*/*").setAction(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)

            startActivityForResult(intent, CODE_GET_FILE)
        }

        binding.imageUploadView.setOnClickListener {
            val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(intent, CODE_GET_IMAGE)
        }

        binding.writeUploadBtn.setOnClickListener {
            checkUploadBtn()
        }

        viewModel.uploadFileResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                }
            }
        })

        viewModel.uploadImageResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                }
            }
        })

        viewModel.writePostResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                }
            }
        })

        viewModel.postIdx.observe(viewLifecycleOwner, Observer {
            val idx: Int = viewModel.postIdx.value ?: 0
            val action =
                WritePostFragmentDirections.actionWritePostFragmentToPostFragment(idx)
            findNavController().navigate(action)
        })

        binding.deleteImageBtn.setOnClickListener {
            binding.imageUploadView.isVisible = true
            binding.imageView.isVisible = false
            checkInput[0] = 0
        }

        binding.fileDeleteBtn.setOnClickListener {
            binding.fileUploadBtn.isVisible = true
            binding.modelView.isVisible = false
            checkInput[1] = 0
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var file: File? = null

        if (resultCode == RESULT_OK) {
            Log.e("fadofnaof", "${requestCode} ${resultCode}")
            if (requestCode == CODE_GET_FILE) {
                if (data?.data != null) {
                    file = File(createCopyAndReturnRealPath(data.data!!))

                    binding.fileUploadBtn.isVisible = false
                    binding.modelView.isVisible = true
                    binding.tvFileName.text = data.data?.path?.split("/")?.last().toString()

                    checkInput[1] = 1
                    viewModel.modelFile.value = file
                }
            } else if (requestCode == CODE_GET_IMAGE) {
                if (data?.data != null) {
                    file = File(createCopyAndReturnRealPath(data.data!!))

                    Log.e("adsf", data.data!!.toString())

                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(activity?.contentResolver,
                                data.data!!)
                        binding.thumbnailImage.setImageBitmap(bitmap)
                    } else {
                        val source =
                            ImageDecoder.createSource(activity?.contentResolver!!, data.data!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.thumbnailImage.setImageBitmap(bitmap)
                    }

                    checkInput[0] = 1
                    binding.imageUploadView.isVisible = false
                    binding.imageView.isVisible = true
                    viewModel.imageFile.value = file
                }
            }
        } else {
            Toast.makeText(requireContext(), "사진 및 파일을 선택하지 않았습니다...", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkUploadBtn() {
        if (!viewModel.title.value.equals("") && viewModel.title.value != null) {

            if (!viewModel.tag.value.equals("") && viewModel.tag.value != null) {

                if (checkInput[0] == 1 && checkInput[1] == 1) {
                    viewModel.startWritePost()
                } else {
                    Toast.makeText(requireContext(),
                        "3D 모델링 파일 및 썸네일 이미지를 추가해주세요.",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(requireContext(), "tag를 입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    fun createCopyAndReturnRealPath(uri: Uri): String? {
        val context = requireContext()
        val contentResolver = context.contentResolver ?: return null

        val filePath = (context.applicationInfo.dataDir + File.separator
                + System.currentTimeMillis())
        val file = File(filePath)
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
            return file.absolutePath
        } catch (e: Exception) {
            return null
        }
        return null
    }

    fun checkPermission() {
        val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

        when (ContextCompat.checkSelfPermission(requireContext(), permission)) {
            PackageManager.PERMISSION_DENIED -> {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 100)
            }
        }

    }

}