package com.nunar.nun_ar_android_v1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nunar.nun_ar_android_v1.R

class WritePostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
<<<<<<< Updated upstream
        return inflater.inflate(R.layout.fragment_write_post, container, false)
=======
        val binding = DataBindingUtil.inflate<FragmentWritePostBinding>(inflater,
            R.layout.fragment_write_post,
            container,
            false)
        binding.lifecycleOwner = viewLifecycleOwner

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
            when(it){
                is NetworkStatus.Error -> Toast.makeText(requireContext(), "${it.throwable.message}", Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    binding.imageUploadView.isInvisible = false
                    binding.imageView.isInvisible = true
                }
            }
        })

        viewModel.uploadImageResult.observe(viewLifecycleOwner, Observer {
            when(it){
                is NetworkStatus.Error -> Toast.makeText(requireContext(), "${it.throwable.message}", Toast.LENGTH_SHORT).show()
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
            if (requestCode == CODE_GET_FILE) {
                if (data?.data != null) {
                    val uri: Uri = getRealPathFromUri(data.data!!)
                    file = File(getRealPathFromUri(uri).path)
                    MultipartBody.Part.createFormData("file", file.path, RequestBody.create(
                        "image/jpeg".toMediaTypeOrNull(), file))
                }
            } else if (requestCode == CODE_GET_IMAGE) {
                if (data?.data != null) {
                    val uri: Uri = getRealPathFromUri(data.data!!)
                    file = File(getRealPathFromUri(uri).path)

                   val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file.path)
                    val filePart = MultipartBody.Part.createFormData("file",
                        file.path,
                        fileBody)

                    viewModel.uploadImage(filePart)
                }
            }
        } else {
            Toast.makeText(requireContext(), "사진 및 파일을 선택하지 않았습니다...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRealPathFromUri(uri: Uri): Uri {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()

        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = columnIndex?.let {
            cursor.getString(it)
        }
        cursor?.close()

        return Uri.fromFile(File(picturePath ?: ""))
>>>>>>> Stashed changes
    }
}