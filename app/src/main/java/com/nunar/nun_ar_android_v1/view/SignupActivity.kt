package com.nunar.nun_ar_android_v1.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.databinding.ActivitySignupBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.SignupViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class SignupActivity : AppCompatActivity() {

    private val CODE_GET_IMAGE = 1002

    private val viewModel: SignupViewModel by viewModels()
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.vm = viewModel
        binding.activity = this
        binding.lifecycleOwner = this

        checkPermission()

        binding.profileUploadView.setOnClickListener {
            val intent = Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(intent, CODE_GET_IMAGE)
        }

        binding.profileDeleteImageBtn.setOnClickListener {
            binding.profileUploadView.isVisible = true
            binding.profileImage.isVisible = false
            viewModel.profileFile.value = null
        }

        binding.signupSubmitbtn.setOnClickListener {
            with(viewModel) {
                if (profileFile.value != null) {
                    imageUpload(getProfileImageFilePart(profileFile.value!!))
                } else {
                    Toast.makeText(this@SignupActivity, "프로필 사진이 선택되지 않았습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        viewModel.imageUpload.observe(this, Observer {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(this,
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    with(viewModel) {
                        if (!id.value.isNullOrEmpty() && !password.value.isNullOrEmpty() && !name.value.isNullOrEmpty() && !profileUrl.value.isNullOrEmpty()) {
                            register()
                        } else {
                            Toast.makeText(this@SignupActivity,
                                "아이디, 비밀번호, 이름을 모두 입력해주세요",
                                Toast.LENGTH_SHORT).show()
                            Log.d("check", "${!id.value.isNullOrEmpty()} ${!password.value.isNullOrEmpty()} ${!name.value.isNullOrEmpty()} ${!profileUrl.value.isNullOrEmpty()}")
                        }
                    }
                }
            }
        })

        viewModel.registerResult.observe(this, Observer {
            when (it) {
                is NetworkStatus.Error -> Toast.makeText(this,
                    "${it.throwable.message}",
                    Toast.LENGTH_SHORT).show()
                is NetworkStatus.Loading -> {

                }
                is NetworkStatus.Success -> {
                    Toast.makeText(this@SignupActivity.applicationContext, "회원가입 성공!", Toast.LENGTH_SHORT)
                    finish()

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var file: File? = null

        if (resultCode == RESULT_OK) {
            Log.e("fadofnaof", "${requestCode} ${resultCode}")
            if (requestCode == CODE_GET_IMAGE) {
                if (data?.data != null) {
                    file = File(createCopyAndReturnRealPath(data.data!!))

                    Log.e("adsf", data.data!!.toString())

                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap =
                            MediaStore.Images.Media.getBitmap(this?.contentResolver,
                                data.data!!)
                        binding.profileThumbnailImage.setImageBitmap(bitmap)
                    } else {
                        val source =
                            ImageDecoder.createSource(this?.contentResolver!!, data.data!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        binding.profileThumbnailImage.setImageBitmap(bitmap)
                    }

                    binding.profileUploadView.isVisible = false
                    binding.profileImage.isVisible = true
                    viewModel.profileFile.value = file
                }
            }
        } else {
            Toast.makeText(this, "사진 및 파일을 선택하지 않았습니다...", Toast.LENGTH_SHORT).show()
        }
    }


    fun createCopyAndReturnRealPath(uri: Uri): String? {
        val contentResolver = this.contentResolver ?: return null

        val filePath = (this.applicationInfo.dataDir + File.separator
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

        when (ContextCompat.checkSelfPermission(this, permission)) {
            PackageManager.PERMISSION_DENIED -> {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 100)
            }
        }
    }

}