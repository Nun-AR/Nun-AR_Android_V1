package com.nunar.nun_ar_android_v1.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentWritePostBinding>(inflater,
            R.layout.fragment_write_post,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner

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

        viewModel.uploadFileResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(requireContext(),
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    binding.imageUploadView.isInvisible = false
                    binding.imageView.isInvisible = true
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
                    binding.imageUploadView.isVisible = false
                    binding.imageView.isVisible = true
                }
            }
        })

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var file: File? = null

        if (resultCode == RESULT_OK) {
            Log.e("fadofnaof","${requestCode} ${resultCode}")
            if (requestCode == CODE_GET_FILE) {
                if (data?.data != null) {

                }
            } else if (requestCode == CODE_GET_IMAGE) {
                Log.e("fadofnaof", data?.data.toString())
                if (data?.data != null) {
                    Log.e("fadofnaof", "daf")
                    //val uri: Uri =
                    file = File(createCopyAndReturnRealPath(data.data!!))

                    Log.e("fadofnaof", "aaaaa: ${file.name}")
                    Log.e("fadofnaof", "aaa : ${file.path}")

                    val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull
                        (), file)
                    val filePart = MultipartBody.Part.createFormData("file",
                        file.name,
                        fileBody)

                    viewModel.uploadImage(filePart)
                }
            }
        } else {
            Toast.makeText(requireContext(), "사진 및 파일을 선택하지 않았습니다...", Toast.LENGTH_SHORT).show()
        }
    }

    fun createCopyAndReturnRealPath(uri: Uri) :String? {
        val context = requireContext()
        val contentResolver = context.contentResolver ?: return null

        // Create file path inside app's data dir
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
            return file.getAbsolutePath()
        } catch (e: Exception) {
           return null
        }
        return null
    }

    fun checkPermission(){
        val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        val permissionResult = ContextCompat.checkSelfPermission(requireContext(), permission)

        when(permissionResult){
            PackageManager.PERMISSION_DENIED->{
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 100)
            }
            PackageManager.PERMISSION_GRANTED->{
                Toast.makeText(requireContext(), "권한 존재함", Toast.LENGTH_SHORT).show()
            }
        }

    }

}