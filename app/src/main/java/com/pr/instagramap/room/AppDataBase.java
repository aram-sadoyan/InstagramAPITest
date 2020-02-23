//package com.pr.instagramap.room;
//
//import android.content.Context;
//
//import androidx.annotation.NonNull;
//import androidx.room.Database;
//import androidx.room.DatabaseConfiguration;
//import androidx.room.InvalidationTracker;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//import androidx.sqlite.db.SupportSQLiteOpenHelper;
//
//@Database(entities = {UserEntity.class}, version = 3, exportSchema = false)
//public abstract class AppDataBase extends RoomDatabase {
//
//
//	private static AppDataBase INSTANCE;
//
//	public abstract UserDao userDao();
//
//	public static AppDataBase getAppDatabase(Context context) {
//		if (INSTANCE == null) {
//			INSTANCE =
//					Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "user")
//							// allow queries on the main thread.
//							// Don't do this on a real app! See PersistenceBasicSample for an example.
//							.allowMainThreadQueries()
//							.build();
//		}
//		return INSTANCE;
//	}
//
//
//
//
//	public static void destroyInstance() {
//		INSTANCE = null;
//	}
//
//}
//
//
