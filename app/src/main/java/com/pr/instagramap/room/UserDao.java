//package com.pr.instagramap.room;
//
//import androidx.room.Dao;
//import androidx.room.Insert;
//import androidx.room.Query;
//
//import java.util.List;
//
//@Dao
//public interface UserDao {
//
//	@Query("SELECT * FROM user")
//	List<UserEntity> getAll();
//
//	@Query("SELECT boss_post_id  FROM user WHERE insta_post_id = :instaPostId")
//	public String findBossLikeId(String instaPostId);
//
//
//	@Query("DELETE FROM user WHERE insta_post_id = :instaPostId")
//	public boolean deleteBossLikeId(String instaPostId);
//
//	@Insert
//	void insertAll(UserEntity... userEntities);
//
//
////	@Insert
////	void insertAll(UserEntity... users);
//
//
//}
