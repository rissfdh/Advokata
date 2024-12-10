//package com.example.projectadvocata.data.local.room
//
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.projectadvocata.data.loocal.entity.UserEntity
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface UserDao {
//    @Query("SELECT * FROM user_table WHERE email LIKE '%@lawyer.com%'")
//    fun getAllLawyerUsers(): Flow<List<UserEntity>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUser(user: UserEntity)
//
//    @Query("DELETE FROM user_table WHERE email = :email")
//    suspend fun deleteUser(email: String)
//}
