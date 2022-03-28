package com.nunar.nun_ar_android_v1.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.databinding.ActivityLoginBinding
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.utils.tokenDataStore
import com.nunar.nun_ar_android_v1.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        binding.lifecycleOwner = this
        binding.vm = viewModel
        binding.activity = this

        viewModel.loginResult.observe(this) {
            when (it) {
                is NetworkStatus.Error -> {
                    Toast.makeText(this, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkStatus.Loading -> {
                }
                is NetworkStatus.Success -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        saveToken(it.data)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }


    }

    suspend fun saveToken(token: String) {
        val tokenKey = stringPreferencesKey("token")
        tokenDataStore.edit { pref ->
            pref[tokenKey] = token
        }
    }

    fun onClickRegister() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}