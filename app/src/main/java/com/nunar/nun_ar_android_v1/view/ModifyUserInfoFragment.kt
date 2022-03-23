package com.nunar.nun_ar_android_v1.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.databinding.FragmentModifyUserInfoBinding
import com.nunar.nun_ar_android_v1.model.Server.DOMAIN
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.ModifyUserInfoViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ModifyUserInfoFragment : Fragment() {

    val viewModel: ModifyUserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DataBindingUtil.inflate<FragmentModifyUserInfoBinding>(inflater,
            R.layout.fragment_modify_user_info,
            container,
            false)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        val register =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val data = result.data
                if (result.resultCode == RESULT_OK) {
                    data?.data?.let {
                        viewModel.file.value = File(createCopyAndReturnRealPath(it))
                    }
                }
            }

        navArgs<ModifyUserInfoFragmentArgs>().value.apply {
            viewModel.name.value = name
            viewModel.profileUrl.value = profileUrl
        }

        viewModel.profileUrl.observe(viewLifecycleOwner) {
            Glide.with(this.requireContext())
                .load("${DOMAIN}image/${it}")
                .into(binding.ivProfile)
        }

        viewModel.file.observe(viewLifecycleOwner) {
            val bitmap = BitmapFactory.decodeFile(it.path)

            Glide.with(this.requireContext())
                .load(bitmap)
                .into(binding.ivProfile)
        }


        viewModel.modifyUserInfoResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(requireContext(), it.throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }
                is NetworkStatus.Loading -> {
                }
                is NetworkStatus.Success -> {
                    findNavController().popBackStack()
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnUploadImage.setOnClickListener {
            val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)
            register.launch(intent)
        }

        binding.btnSave.setOnClickListener {
            try {
                viewModel.saveInfo()
            } catch (t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun createCopyAndReturnRealPath(uri: Uri): String? {
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
    }

}