package com.so.dingbring

import com.so.dingbring.detail.DetailRepository
import com.so.dingbring.detail.DetailViewModel
import com.so.dingbring.home.HomeRepository
import com.so.dingbring.home.HomeViewModel
import com.so.dingbring.login.LoginRepository
import com.so.dingbring.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(repository = HomeRepository()) }
    viewModel { LoginViewModel(mLoginRepository = LoginRepository() )}
    viewModel { DetailViewModel(mDetailRepository = DetailRepository()) }
}