package de.kevinfaust.heybeaches

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import de.kevinfaust.heybeaches.repository.IUserRepository
import de.kevinfaust.heybeaches.services.PersistenceService

class MainActivity : AppCompatActivity() {

    lateinit var userRepository: IUserRepository
    lateinit var persistenceService: PersistenceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Injector.inject(mainActivity = this)

        val email = "user4@testmail.com"
        val password = "password1234"

        val registeredUser = userRepository.signup(email, password)

        if (registeredUser?.email == persistenceService.userName) {
            Log.i("MAIN", "onCreate: Registered new user ${registeredUser.email} with token ${registeredUser.token}")
        }
    }
}
