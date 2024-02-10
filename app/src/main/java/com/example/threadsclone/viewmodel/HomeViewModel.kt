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

class HomeViewModel : ViewModel() {

    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    val thread: DatabaseReference = db.getReference("threads")


    private val _threadsAndUsers = MutableLiveData<List<Pair<ThreadModel, UserModel>>>()
    val threadsAndUsers: LiveData<List<Pair<ThreadModel, UserModel>>> = _threadsAndUsers

    init {
        Log.i("***outsideinsideinit000", "insideinit${threadsAndUsers}}")
        fetchThreadsAndUsers {
            Log.i("***insideinit", "insideinit")
            _threadsAndUsers.value = it
        }

    }


    private fun fetchThreadsAndUsers(onResult: (List<Pair<ThreadModel, UserModel>>) -> Unit) {
        thread.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ThreadModel, UserModel>>()
                for (threadSnapShot in snapshot.children) {
                    val thread = threadSnapShot.getValue(ThreadModel::class.java)
                    thread.let {
                        fetchUserFromThread(it!!) { user ->
                            Log.i("***insideinit01", "insideinit${thread}")
                            result.add(0, it to user)
                            if (result.size == snapshot.childrenCount.toInt()) {
                                onResult(result)
                            }
                            Log.i("***insideinit02", "insideinit${onResult(result)}")
                        }
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    //continue from here
    fun fetchUserFromThread(thread: ThreadModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(thread.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.i("***insideinit111", "insideinit${snapshot}")
                    val user = snapshot.getValue(UserModel::class.java)
                    Log.i("***insideinit11tom", "insideinit00${user}")
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}