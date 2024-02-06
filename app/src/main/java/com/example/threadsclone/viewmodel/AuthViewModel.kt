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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.lang.Exception
import java.util.UUID

class AuthViewModel : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    val userRef: DatabaseReference = db.getReference("users")

    private val _firebaseUser = MutableLiveData<FirebaseUser>()
    val firebaseUser: LiveData<FirebaseUser> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")


    init {
        _firebaseUser.value = auth.currentUser
    }


    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                Log.i("outsideinsuccessful", "outsideissuccessful")
                if (it.isSuccessful) {
                    Log.i("insideinsuccessful", "insideissuccessful")

                    _firebaseUser.postValue(auth.currentUser)
                } else {
                    _error.postValue("Something went wrong.")
                }
            }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                Log.i("outsideissuccessful", "outsideissuccessful")
                //not hitting this firebase
                try {
                    if (it.isSuccessful) {
                        Log.i("insideissuccessful", "insideissuccessful")
                        _firebaseUser.postValue(auth.currentUser)
                        saveImage(
                            email,
                            password,
                            name,
                            bio,
                            userName,
                            imageUri,
                            auth.currentUser?.uid,
                            context
                        )
                    } else {
                        Log.i("Elseintry", "Elseintry${it.exception?.message}")
                    }
                } catch (e: Exception) {
                    Log.i("errorblock", "insideissuccessful")
                    _error.postValue("Something went wrong.")
                }
            }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {

            imageRef.downloadUrl.addOnCompleteListener {
                saveData(email, password, name, bio, userName, it.toString(), uid, context)
            }
        }

    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        toString: String,
        uid: String?,
        context: Context
    ) {

        val userData = UserModel(email, password, name, bio, userName, toString)

        userRef.child(uid!!).setValue(userData).addOnSuccessListener {

                SharedPref.storeData(name, email, bio, userName, toString, context)


            }.addOnFailureListener {


            }


    }


}