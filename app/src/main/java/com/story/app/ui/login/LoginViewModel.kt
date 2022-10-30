package com.story.app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.story.app.core.DataRepository

class LoginViewModel (private val dataRepository: DataRepository) : ViewModel() {
    private var email = ""
    private var password = ""

    fun setLoginParam(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun doLogin() = dataRepository.doLogin(email, password).asLiveData()
}