package com.example.threadsclone.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel : ViewModel() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()

    val userRef: DatabaseReference = db.getReference("users")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: MutableLiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error



    private val storageRef: StorageReference = Firebase.storage.reference
    private val imageRef: StorageReference = storageRef.child("users/${UUID.randomUUID()}.jpg")


    init {
        _firebaseUser.value = auth.currentUser
    }


    fun login(email: String, password: String, context: Context) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            Log.i("outsideinsuccessful", "outsideissuccessful")
            if (it.isSuccessful) {


                Log.i("insideinsuccessful", "insideissuccessful")

                _firebaseUser.postValue(auth.currentUser)
                getData(auth.currentUser!!.uid, context)


            } else {
                _error.postValue(it.exception?.message)
            }
        }
    }

    private fun getData(uid: String, context: Context) {

        val fireStoreDb = Firebase.firestore
        val followersRef = fireStoreDb.collection("followers").document(uid)
        val followingRef = fireStoreDb.collection("following").document(uid)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followerIds" to listOf<String>()))

        userRef.child(uid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.getValue(UserModel::class.java)
                    SharedPref.storeData(
                        userData!!.name,
                        userData!!.email,
                        userData!!.bio,
                        userData!!.userName,
                        userData!!.imageUrl,
                        context
                    )


                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // User creation is successful
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
                // User creation failed
                val errorMessage = task.exception?.message
                Log.i("errorText","${errorMessage.toString()}")
                if (errorMessage != null) {
                    // Display the error message as a toast
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                } else {
                    // Handle other types of errors
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
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
        Log.i("***0", "${imageUri}")
        val uploadTask = imageRef.putFile(imageUri)
        Log.i("***1", "${uploadTask}")
        uploadTask.addOnSuccessListener {
            Log.i("***2", "${uploadTask.toString()}")
            Log.i("***2", "${imageUri.toString()}")
            imageRef.downloadUrl.addOnSuccessListener {
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

        val fireStoreDb: FirebaseFirestore = Firebase.firestore
        val followersRef: DocumentReference = fireStoreDb.collection("followers").document(uid!!)
        val followingRef: DocumentReference = fireStoreDb.collection("following").document(uid!!)


        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followerIds" to listOf<String>()))

        val userData = UserModel(email, password, name, bio, userName, toString, uid!!)
        userRef.child(uid!!).setValue(userData).addOnSuccessListener {

            //for login success this sharedPref has been hitted for login
            SharedPref.storeData(name, email, bio, userName, toString, context)


        }.addOnFailureListener {


        }


    }

    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }


}