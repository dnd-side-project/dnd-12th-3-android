package com.dnd.safety.di

import com.dnd.safety.data.repository.AuthRepositoryImpl
import com.dnd.safety.data.repository.CameraRepositoryImpl
import com.dnd.safety.data.repository.CommentRepositoryImpl
import com.dnd.safety.data.repository.IncidentListRepositoryImpl
import com.dnd.safety.data.repository.IncidentRepositoryImpl
import com.dnd.safety.data.repository.LawDistrictRepositoryImpl
import com.dnd.safety.data.repository.LoginRepositoryImpl
import com.dnd.safety.data.repository.MediaRepositoryImpl
import com.dnd.safety.data.repository.MyReportRepositoryImpl
import com.dnd.safety.data.repository.MyTownRepositoryImpl
import com.dnd.safety.data.repository.UserInfoRepositoryImpl
import com.dnd.safety.domain.repository.AuthRepository
import com.dnd.safety.domain.repository.CameraRepository
import com.dnd.safety.domain.repository.CommentRepository
import com.dnd.safety.domain.repository.IncidentListRepository
import com.dnd.safety.domain.repository.IncidentRepository
import com.dnd.safety.domain.repository.LawDistrictRepository
import com.dnd.safety.domain.repository.LoginRepository
import com.dnd.safety.domain.repository.MediaRepository
import com.dnd.safety.domain.repository.MyReportRepository
import com.dnd.safety.domain.repository.MyTownRepository
import com.dnd.safety.domain.repository.UserInfoRepository
import com.dnd.safety.utils.AndroidFileManager
import com.dnd.safety.utils.FileManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindMediaRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository

    @Binds
    abstract fun bindIncidentRepository(
        impl: IncidentRepositoryImpl
    ): IncidentRepository

    @Binds
    abstract fun bindFileManager(
        impl: AndroidFileManager
    ): FileManager

    @Binds
    @Singleton
    abstract fun provideIncidentsRepository(
        repositoryImpl: IncidentListRepositoryImpl
    ): IncidentListRepository

    @Binds
    @Singleton
    abstract fun provideLawDistrictRepository(
        repositoryImpl: LawDistrictRepositoryImpl
    ): LawDistrictRepository

    @Binds
    @Singleton
    abstract fun provideCameraRepository(
        repositoryImpl: CameraRepositoryImpl
    ): CameraRepository

    @Binds
    @Singleton
    abstract fun provideTokenRepository(
        repositoryImpl: UserInfoRepositoryImpl
    ): UserInfoRepository

    @Binds
    @Singleton
    abstract fun provideMyTownRepository(
        repositoryImpl: MyTownRepositoryImpl
    ): MyTownRepository

    @Binds
    @Singleton
    abstract fun provideLoginRepository(
        repositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun provideMyReportRepository(
        repositoryImpl: MyReportRepositoryImpl
    ): MyReportRepository

    @Binds
    @Singleton
    abstract fun provideCommentRepository(
        repositoryImpl: CommentRepositoryImpl
    ): CommentRepository
}