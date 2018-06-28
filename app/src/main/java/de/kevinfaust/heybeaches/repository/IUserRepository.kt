package de.kevinfaust.heybeaches.repository

import de.kevinfaust.heybeaches.model.User

interface IUserRepository {
    fun signup(email: String, password: String): User?
    fun login(email: String, password: String): User?
    fun logout(token: String)
    fun authenticatedUser(): User?
}
