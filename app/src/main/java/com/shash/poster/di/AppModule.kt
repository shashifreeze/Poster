package com.shash.poster.di

import android.Manifest
import android.content.Context
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.shash.poster.network.RemoteDataSource
import com.shash.poster.network.api.HomeApi
import com.shash.poster.network.api.PosterApi
import com.shash.poster.utils.internetconnection.ConnectionLiveData
import com.shash.poster.views.main.MainRepository
import com.shash.poster.views.ui.home.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
@Author: Shashi
@Date: 19-03-2021
@Description: Provides Objects in the application scope
*/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * @param: NA
     * @return: RemoteDataSource
     * @author: Shashi
     * Creates and provides RemoteDataSource object reference */
    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource = RemoteDataSource()
    /**
     * @param: (remoteDataSource: RemoteDataSource)
     * @return: HomeApi
     * @author: Shashi
     * Creates and provides instance of HomeApi  */
    @Provides
    @Singleton
    fun provideHomeApi(remoteDataSource: RemoteDataSource): HomeApi = remoteDataSource.buildApi(
        HomeApi::class.java
    )

    /**
     * @param: (remoteDataSource: RemoteDataSource)
     * @return: HomeApi
     * @author: Shashi
     * Creates and provides instance of PosterApi  */
    @Provides
    @Singleton
    fun providePosterApi(remoteDataSource: RemoteDataSource): PosterApi = remoteDataSource.buildApi(
        PosterApi::class.java
    )

    /**
     * @param: (@ApplicationContext context: Context)
     * @return: MainRepository
     * @author: Shashi
     * Creates and provides MainRepository object reference */
    @Provides
    @Singleton
    fun provideMainRepository(@ApplicationContext context: Context): MainRepository =
        MainRepository(context)

    /**
     * @param: (@ApplicationContext context: Context)
     * @return: MainRepository
     * @author: Shashi
     * Creates and provides MainRepository object reference */
    @Provides
    @Singleton
    fun provideHomeRepository(@ApplicationContext context: Context, api: HomeApi): HomeRepository =
        HomeRepository(context,api)

    /**
     * @param: (@ApplicationContext context: Context)
     * @return: ConnectionLiveData
     * @author: Shashi
     * Creates and provides ConnectionLiveData object reference
     * Provides connection live data using which we will monitor internet connection in the application
     */
    @Provides
    @Singleton
    fun provideConnectionLiveData(@ApplicationContext context: Context): ConnectionLiveData =
        ConnectionLiveData(context)

    /**
     * @param: (@ApplicationContext context: Context)
     * @return: DexterBuilder.MultiPermissionListener
     * @author: Shashi
     * Creates and provides DexterBuilder.MultiPermissionListener object reference
     */
    @Provides
    @Singleton
    fun provideDexterBuilderMultiPermissionListener(@ApplicationContext context: Context): DexterBuilder.MultiPermissionListener =
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA
            )
}