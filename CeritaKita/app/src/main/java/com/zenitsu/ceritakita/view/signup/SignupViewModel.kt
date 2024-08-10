package com.zenitsu.ceritakita.view.signup

import androidx.lifecycle.ViewModel
import com.zenitsu.ceritakita.data.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) = repository.register(name, email, password)

    companion object {
        private const val TAG = "SignupViewModel"
    }
}