package com.nunar.nun_ar_android_v1.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.databinding.ActivityLoginBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        viewModel.loginResult.observe(this) {
            when(it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this, "${it.throwable.message}", Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {
                    Toast.makeText(this, "로딩 시작", Toast.LENGTH_SHORT).show()

                }
                is NetworkStatus.Success -> {
                    Toast.makeText(this, it.data, Toast.LENGTH_SHORT).show()

                }
            }
        }


    }
}