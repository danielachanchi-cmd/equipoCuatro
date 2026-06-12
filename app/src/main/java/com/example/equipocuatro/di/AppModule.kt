package com.example.equipocuatro.di

import android.content.Context
import androidx.room.Room
import com.example.equipocuatro.data.AuthDataSource
import com.example.equipocuatro.data.RetoDB
import com.example.equipocuatro.data.RetoDao
import com.example.equipocuatro.repository.AuthRepository
import com.example.equipocuatro.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetoDB(@ApplicationContext context: Context): RetoDB{
        return  Room.databaseBuilder(
            context,
            RetoDB::class.java,
            Constants.NAME_BD
        ).build()
    }

    @Singleton
    @Provides
    fun provideAuthDataSource(auth: FirebaseAuth, db: FirebaseFirestore): AuthDataSource{
        return AuthDataSource(auth,db)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideAuthRepository(dataSource: AuthDataSource): AuthRepository {
        return AuthRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
    @Singleton
    @Provides
    fun provideDaoReto(retoDB:RetoDB): RetoDao {
        return retoDB.retoDao()
    }
}