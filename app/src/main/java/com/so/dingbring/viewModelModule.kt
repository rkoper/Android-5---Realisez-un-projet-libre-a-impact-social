package com.so.dingbring

import com.so.dingbring.data.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MyEventViewModel(mEventRepository = MyEventRepository()) }
    viewModel { MyUserViewModel(mUserRepository = MyUserRepository() )}
    viewModel { MyItemViewModel(mItemRepository = MyItemRepository()) }
}