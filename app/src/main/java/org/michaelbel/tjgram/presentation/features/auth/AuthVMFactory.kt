package org.michaelbel.tjgram.presentation.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.michaelbel.tjgram.data.api.remote.TjApi
import org.michaelbel.tjgram.data.db.dao.UserDao

@Suppress("unchecked_cast")
class AuthVMFactory(
        private val service: TjApi, private val dataSource: UserDao
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthVM(service, dataSource) as T
    }
}