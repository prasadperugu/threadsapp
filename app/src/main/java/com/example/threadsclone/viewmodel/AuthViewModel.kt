package com.example.threadsclone.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsclone.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    val userRef = db.getReference("users")
    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                } else {
                    _error.postValue("Something went wrong.")
                }
            }
    }

    fun register(
        email: String
        , password: String
        , name: String
        , bio: String
        , userName: String
        , imageUri: Uri) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(email,password,name,bio,userName,imageUri, auth.currentUser?.uid)
                } else {
                    _error.postValue("Something went wrong.")
                }
            }
    }

    private fun saveImage(
        email: String
        , password: String
        , name: String
        , bio: String
        , userName: String
        , imageUri: Uri
        , uid: String?) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {

            imageRef.downloadUrl.addOnCompleteListener {
                saveData(email, password,name,bio,userName,it.toString(), uid)
            }
        }

    }

    private fun saveData(
        email: String
        , password: String
        , name: String
        , bio: String
        , userName: String
        , toString: String
        , uid: String?
        ) {

        val userData  = UserModel(email,password,name,bio, userName, toString)

        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {



            }.addOnFailureListener {


            }



    }


}