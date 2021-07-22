package com.example.customerfirebase.di

import com.example.customerfirebase.db.CustomerDao
import com.example.customerfirebase.db.DbRepository
import com.example.customerfirebase.db.DefaultDbRepo
import com.example.customerfirebase.utils.Constant.PRODUCT
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFireStoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideCustomerIdCollectionRef(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("customer")
    }

    @Singleton
    @Provides
    fun provideCustomerDataCollectionRef(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("customerData")
    }

    @Singleton
    @Provides
    fun provideCustomerDetailsCollectionRef(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection("customerDetails")
    }


    @Singleton
    @Provides
    fun provideProductDetailsCollectionRef(firestore: FirebaseFirestore): CollectionReference {
        return firestore.collection(PRODUCT)
    }


    @Provides
    fun provideDefaultDbRepo(
        customerDao: CustomerDao,
    ): DefaultDbRepo {
        return DbRepository(customerDao)
    }


}