//package com.pr.instagramap.util;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.pr.instagramap.room.AppDataBase;
//import com.pr.instagramap.room.UserEntity;
//
//import java.util.List;
//
//public class DatabaseInitializer {
//
//
//	public static void populateAsync(@NonNull final AppDataBase db) {
//		PopulateDbAsync task = new PopulateDbAsync(db);
//		task.execute();
//	}
//
//	public static void populateSync(@NonNull final AppDataBase db) {
//		populateWithTestData(db);
//	}
//
//	private static UserEntity addUser(final AppDataBase db, UserEntity user) {
//		db.userDao().insertAll(user);
//		return user;
//	}
//
//	private static void populateWithTestData(AppDataBase db) {
//		UserEntity userEntity = new UserEntity();
//		userEntity.setInstaPostId("testInstaPostId1");
//		userEntity.setBossPostId("testBossId1");
//
//		addUser(db, userEntity);
//
//		List<UserEntity> userList = db.userDao().getAll();
//		Log.d("database tag", "Rows Count: " + userList.size());
//	}
//
//	public static List<UserEntity> getAllList(AppDataBase db){
//		return db.userDao().getAll();
//	}
//
//	private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//		private final AppDataBase mDb;
//
//		PopulateDbAsync(AppDataBase db) {
//			mDb = db;
//		}
//
//		@Override
//		protected Void doInBackground(final Void... params) {
//			populateWithTestData(mDb);
//			return null;
//		}
//
//	}
//}
