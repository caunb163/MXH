package com.caunb163.mxh.di

import com.caunb163.mxh.ui.login.LoginViewModel
import com.caunb163.mxh.ui.login.phone.PhoneViewModel
import com.caunb163.mxh.ui.main.MainViewModel
import com.caunb163.mxh.ui.main.home.HomeViewModel
import com.caunb163.mxh.ui.main.home.comment.CommentViewModel
import com.caunb163.mxh.ui.main.home.create_post.CreatePostViewModel
import com.caunb163.mxh.ui.main.home.edit_post.EditPostViewModel
import com.caunb163.mxh.ui.main.messenger.GroupViewModel
import com.caunb163.mxh.ui.main.messenger.chat.ChatViewModel
import com.caunb163.mxh.ui.main.messenger.create_group.CreateGroupViewModel
import com.caunb163.mxh.ui.main.profile.ProfileViewModel
import com.caunb163.mxh.ui.main.profile.updateProfile.UpdateProfileViewModel
import com.caunb163.mxh.ui.main.profile.user.UserViewModel
import com.caunb163.mxh.ui.main.video.VideoViewModel
import com.caunb163.mxh.ui.onboarding.OnBoardingViewModel
import com.caunb163.mxh.ui.register.RegisterViewModel
import com.caunb163.mxh.ui.splash.SplashViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@InternalCoroutinesApi
val viewModelModule = module {
    viewModel<SplashViewModel> { SplashViewModel(get()) }
    viewModel<OnBoardingViewModel> { OnBoardingViewModel(get(), get()) }
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<RegisterViewModel> { RegisterViewModel(get()) }
    viewModel<HomeViewModel> { HomeViewModel(get()) }
    viewModel<CreatePostViewModel> { CreatePostViewModel(get(), get()) }
    viewModel<ProfileViewModel> { ProfileViewModel(get(), get(), get()) }
    viewModel<MainViewModel> { MainViewModel(get()) }
    viewModel<CommentViewModel> { CommentViewModel(get()) }
    viewModel<GroupViewModel> { GroupViewModel(get(), get()) }
    viewModel<ChatViewModel> { ChatViewModel(get()) }
    viewModel<CreateGroupViewModel> { CreateGroupViewModel(get()) }
    viewModel<EditPostViewModel> { EditPostViewModel(get()) }
    viewModel<PhoneViewModel> { PhoneViewModel(get()) }
    viewModel<UpdateProfileViewModel> { UpdateProfileViewModel(get()) }
    viewModel<VideoViewModel> { VideoViewModel(get()) }
    viewModel<UserViewModel> { UserViewModel(get(), get()) }
}