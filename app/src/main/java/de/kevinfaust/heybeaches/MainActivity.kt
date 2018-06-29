package de.kevinfaust.heybeaches

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import de.kevinfaust.heybeaches.repository.IImageRepository
import de.kevinfaust.heybeaches.repository.IUserRepository
import de.kevinfaust.heybeaches.services.PersistenceService

class MainActivity : AppCompatActivity() {

    lateinit var userRepository: IUserRepository
    lateinit var imageRepository: IImageRepository
    lateinit var persistenceService: PersistenceService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Injector.inject(mainActivity = this)

        val email = "user6@testmail.com"
        val password = "password1234"

        val registeredUser = userRepository.signup(email, password)

        if (registeredUser?.email == persistenceService.userName) {
            Log.i("MAIN", "onCreate: Registered new user ${registeredUser.email} with token ${registeredUser.token}")
        }

        if(registeredUser != null) {
            userRepository.logout(registeredUser.token)
        }

        if (persistenceService.jwtToken == "") {
            Log.i("MAIN", "onCreate: Logged out new user ${registeredUser?.email} with token ${registeredUser?.token}")
        }

        val beachList = imageRepository.getBeaches()
        val beachImage = imageRepository.getBeachImage(beachList[0])

        if (beachImage != null) {
            Log.i("MAIN", "onCreate: Image Loaded $beachImage")
        }
    }
}
