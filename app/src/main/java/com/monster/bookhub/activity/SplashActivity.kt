package com.monster.bookhub.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp
import com.monster.bookhub.databinding.ActivitySplashBinding
import com.monster.bookhub.model.User

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val bookType = binding.etBookType.text.toString().trim()

            // Initialize Firebase
            FirebaseApp.initializeApp(this)
            database = FirebaseDatabase.getInstance().reference.child("DataBase")

            if (username.isNotEmpty() && bookType.isNotEmpty()) {
                // Save data to Firebase
                saveUserDataToFirebase(username, bookType)
                // Proceed to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserDataToFirebase(username: String, bookType: String) {
        val user = User(username, bookType)


        database.child(username).setValue(user).addOnCompleteListener { task ->
            binding.etUsername.text.clear()
            binding.etBookType.text.clear()

            if (task.isSuccessful) {
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
