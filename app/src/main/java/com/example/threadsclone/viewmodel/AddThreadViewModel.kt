package com.example.threadsclone.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsclone.model.UserModel
import com.example.threadsclone.utils.SharedPref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.util.UUID

class AddThreadViewModel : ViewModel() {

    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    val userRef: DatabaseReference = db.getReference("threads")


    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted


    fun saveImage(
        thread: String,
        userId: String,
        imageUri: Uri,


        ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {

            imageRef.downloadUrl.addOnCompleteListener {
                saveData(thread, userId, it.toString())
            }
        }

    }

    fun saveData(
        thread: String,
        userId: String,
        image: String
    ) {

        val threadData = ThreadModel(thread, image, userId, System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(threadData).addOnSuccessListener {

            _isPosted.postValue(true)


        }.addOnFailureListener {

            _isPosted.postValue(false)
        }


    }


}