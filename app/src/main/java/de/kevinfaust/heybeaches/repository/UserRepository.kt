package de.kevinfaust.heybeaches.repository

import android.util.Log
import de.kevinfaust.heybeaches.api.user.UserApiClient
import de.kevinfaust.heybeaches.model.User
import de.kevinfaust.heybeaches.services.PersistenceService

object UserRepository : IUserRepository {
    lateinit var apiClient: UserApiClient
    lateinit var persistenceService: PersistenceService

    override fun signup(email: String, password: String): User? {
        var user: User? = null

        try {
            // 1. Make webservice call
            val result = apiClient.signup(email, password)

            // 2. Create user object
            user = User(result?.email!!, result.token)

            // 3. Save credentials locally
            persistenceService.userName = user.email
            persistenceService.jwtToken = user.token

        } catch (e: Exception) {
            Log.e("USER_REPO", "signup: Signup failed due to $e")
        }

        return user
    }

    override fun login(email: String, password: String): User? {
        var user: User? = null

        try {
            // 1. Make webservice call
            val result = apiClient.login(email, password)

            // 2. Create user object
            user = User(result?.email!!, result.token)

            // 3. Save credentials locally
            persistenceService.userName = user.email
            persistenceService.jwtToken = user.token

        } catch (e: Exception) {
            Log.e("USER_REPO", "login: Login failed due to $e")
        }

        return user
    }

    override fun logout(token: String) {
        try {
            // 1. Check and delete locally saved credentials
            if (token == persistenceService.jwtToken) {
                persistenceService.userName = ""
                persistenceService.jwtToken = ""

                // 2. Make webservice call
                apiClient.logout(token)
            }
        } catch (e: Exception) {
            Log.e("USER_REPO", "logout: Logout failed due to $e")
        }
    }

    override fun authenticatedUser(): User? {
        return if (persistenceService.jwtToken.isNotEmpty()) {
            User(persistenceService.userName, persistenceService.jwtToken)
        } else {
            null
        }
    }
}
