package com.example.threadsclone.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.threadsclone.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchViewModel : ViewModel() {

    private val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    val users: DatabaseReference = db.getReference("users")


    private val _users = MutableLiveData<List<UserModel>>()
    val usersList: LiveData<List<UserModel>> = _users

    init {
        Log.i("***outsideinsideinit000", "insideinit${users}}")
        fetchUsers {
            Log.i("***insideinit", "insideinit")
            _users.value = it
        }

    }


    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        users.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (threadSnapShot in snapshot.children) {
                    val thread = threadSnapShot.getValue(UserModel::class.java)
                    result.add(thread!!)
                }
                onResult(result)
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