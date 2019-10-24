package com.example.tfm.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tfm.model.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM User")
    fun getAll() : LiveData<MutableList<User>>

    @Query("SELECT * FROM User WHERE email = :email")
    fun getByEmail( email: String) : User

    @Query("SELECT * FROM User WHERE id = :id")
    fun getById( id: String) : User

    @Query("SELECT name FROM User WHERE email = :email")
    fun getNameByEmail(email: String): String

    @Query("SELECT COUNT(*) FROM user")
    fun getSize(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM User")
    fun deleteAll()
}