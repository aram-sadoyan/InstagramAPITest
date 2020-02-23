//package com.pr.instagramap.room;
//
//import androidx.room.TypeConverter;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.List;
//
//public class BossPostDataTypeConverters {
//
////	private static Gson gson = new Gson();
////	@TypeConverter
////	public static List<BossPostData> stringToSomeObjectList(String data) {
////		if (data == null) {
////			return Collections.emptyList();
////		}
////
////		Type listType = new TypeToken<List<BossPostData>>() {}.getType();
////
////		return gson.fromJson(data, listType);
////	}
////	@TypeConverter
////	public static String someObjectListToString(List<BossPostData> someObjects) {
////		return gson.toJson(someObjects);
////	}
//
//	@TypeConverter
//	public String fromCountryLangList(List<BossPostData> countryLang) {
//		if (countryLang == null) {
//			return (null);
//		}
//		Gson gson = new Gson();
//		Type type = new TypeToken<List<BossPostData>>() {}.getType();
//		String json = gson.toJson(countryLang, type);
//		return json;
//	}
//
//	@TypeConverter
//	public List<BossPostData> toCountryLangList(String countryLangString) {
//		if (countryLangString == null) {
//			return (null);
//		}
//		Gson gson = new Gson();
//		Type type = new TypeToken<List<BossPostData>>() {}.getType();
//		List<BossPostData> countryLangList = gson.fromJson(countryLangString, type);
//		return countryLangList;
//	}
//
//}
