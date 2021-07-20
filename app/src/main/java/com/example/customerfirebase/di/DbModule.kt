package com.example.customerfirebase.di

import android.content.Context
import androidx.room.Room
import com.example.customerfirebase.db.CustomerDatabase
import com.example.customerfirebase.utils.Constant.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideCustomerDb(@ApplicationContext context: Context): CustomerDatabase {
        return Room.databaseBuilder(context, CustomerDatabase::class.java,
            DB_NAME).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideCustomerDao(db: CustomerDatabase) = db.getCustomerDao()
}