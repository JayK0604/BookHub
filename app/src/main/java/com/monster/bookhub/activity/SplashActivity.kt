package com.monster.bookhub.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
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

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance().reference.child("DataBase")

        // Set up the Enter key action for EditText fields
        setupEditTextListeners()

        binding.btnStart.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val bookType = binding.etBookType.text.toString().trim()
            val bookName = binding.etBookName.text.toString().trim()
            val authorName = binding.etAuthorName.text.toString().trim()

            if (username.isNotEmpty() && bookType.isNotEmpty() && bookName.isNotEmpty() && authorName.isNotEmpty()) {
                // Save data to Firebase
                saveUserDataToFirebase(username, bookType, bookName, authorName)
                // Proceed to MainActivity
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupEditTextListeners() {
        binding.etUsername.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etBookType.requestFocus()
                true
            } else false
        }

        binding.etBookType.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etBookName.requestFocus()
                true
            } else false
        }

        binding.etBookName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etAuthorName.requestFocus()
                true
            } else false
        }

        binding.etAuthorName.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Handle the "Done" action if needed
                binding.btnStart.performClick() // Simulate button click
                true
            } else false
        }
    }

    private fun saveUserDataToFirebase(username: String, bookType: String, bookName: String, authorName: String) {
        val user = User(username, bookType, bookName, authorName)

        database.child(username).setValue(user).addOnCompleteListener { task ->
            binding.etUsername.text.clear()
            binding.etBookType.text.clear()
            binding.etBookName.text.clear()
            binding.etAuthorName.text.clear()

            if (task.isSuccessful) {
                Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
