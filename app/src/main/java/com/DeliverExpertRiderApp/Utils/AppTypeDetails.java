package com.DeliverExpertRiderApp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppTypeDetails {

    private SharedPreferences sh;

    private AppTypeDetails(Context mContext) {
        sh = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    private static AppTypeDetails instance = null;

    public synchronized static AppTypeDetails getInstance(Context mContext) {
        if (instance == null) {
            instance = new AppTypeDetails(mContext);
        }
        return instance;
    }

    /*************************User Detail Status********************************/

    public String getUserID() {
        return sh.getString("user_id", "");
    }

    public void setUserID(String user) {
        sh.edit().putString("user_id", user).commit();
    }

    public String getUserName() {
        return sh.getString("user_name", "");
    }

    public void setUserName(String user) {
        sh.edit().putString("user_name", user).commit();
    }


    public String getUserEmail() {
        return sh.getString("user_email", "");
    }

    public void setUserEmail(String user) {
        sh.edit().putString("user_email", user).commit();
    }

    public String getUserPhoneNumber() {
        return sh.getString("user_number", "");
    }

    public void setUserPhoneNumber(String user) {
        sh.edit().putString("user_number", user).commit();
    }


    public String getUserImage() {
        return sh.getString("userimage", "");
    }

    public void setUserImage(String user) {
        sh.edit().putString("userimage", user).commit();
    }

    public void setUserPassword(String user) {
        sh.edit().putString("userpassword", user).commit();
    }

    public String getUserPassword() {
        return sh.getString("userpassword", "");
    }


    public void setUserAddress(String Address) {
        sh.edit().putString("Address", Address).commit();
    }

    public String getUserAddress() {
        return sh.getString("Address", "");
    }

    public void setCashCollected(String Address) {
        sh.edit().putString("cash_collected", Address).commit();
    }

    public String getCashCollected() {
        return sh.getString("cash_collected", "");
    }


    public void setIsOnline(boolean user) {
        sh.edit().putBoolean("isOnline", user).commit();
    }

    public boolean getIsOnline() {
        return sh.getBoolean("isOnline", false);
    }


    public boolean isLoggedIn() {
        return sh.getString("user_id", null) != null || sh.getString("user", "") != "";
    }


    public void Logout() {
        SharedPreferences.Editor editor = sh.edit();
        editor.remove("user_name");
        editor.remove("user_id");
        editor.remove("user_email");
        editor.remove("user_number");
        editor.remove("cash_collected");
        editor.remove("userpassword");
        editor.remove("Address");
        editor.remove("isOnline");
        editor.remove("userimage");
        editor.remove("login_date");

        editor.commit();

    }

    // Clear All Data
    public void clear() {
        sh.edit().clear().commit();
    }

    /****************************Set Code *************************/

    public void setCode(String code) {
        sh.edit().putString("code", code).commit();
    }

    public String getCode() {
        return sh.getString("code", "");
    }

    /*******************************************************************************/
    /*************************Social Media Link Status****************[Start]***************/

    public String getFaceBookShareLink() {
        return sh.getString("fb_link", "");
    }

    public void setFaceBookShareLink(String user) {
        sh.edit().putString("fb_link", user).commit();
    }

    public String getTwitterShareLink() {
        return sh.getString("tt_link", "");
    }

    public void setTwitterShareLink(String user) {
        sh.edit().putString("tt_link", user).commit();
    }

    public String getInstaShareLink() {
        return sh.getString("insta_link", "");
    }

    public void setInstaShareLink(String user) {
        sh.edit().putString("insta_link", user).commit();
    }

    public String getLinkedInShareLink() {
        return sh.getString("lindedin_link", "");
    }

    public void setLinkedInShareLink(String user) {
        sh.edit().putString("lindedin_link", user).commit();
    }

    public String getYoutubeShareLink() {
        return sh.getString("youtube_link", "");
    }

    public void setYoutubeShareLink(String user) {
        sh.edit().putString("youtube_link", user).commit();
    }

    public String getGoogleShareLink() {
        return sh.getString("google_link", "");
    }

    public void setGoogleShareLink(String user) {
        sh.edit().putString("google_link", user).commit();
    }

    public String getVemoShareLink() {
        return sh.getString("vemo_link", "");
    }

    public void setVemoShareLink(String user) {
        sh.edit().putString("vemo_link", user).commit();
    }

    public String getPintrestShareLink() {
        return sh.getString("pintrest_link", "");
    }

    public void setPintrestShareLink(String user) {
        sh.edit().putString("pintrest_link", user).commit();
    }

    public String getMapLink() {
        return sh.getString("map_link", "");
    }

    public void setMapLink(String user) {
        sh.edit().putString("map_link", user).commit();
    }

    public String getPrivacyPolicy() {
        return sh.getString("privacy_policy", "");
    }

    public void setPrivacyPolicy(String user) {
        sh.edit().putString("privacy_policy", user).commit();
    }

    public String getAboutUs() {
        return sh.getString("about_us", "");
    }

    public void setAboutUs(String user) {
        sh.edit().putString("about_us", user).commit();
    }

    public String getCompany_Email() {
        return sh.getString("sdpi_email", "");
    }

    public void setCompany_Email(String user) {
        sh.edit().putString("sdpi_email", user).commit();
    }

    public String getCompany_City() {
        return sh.getString("sdpi_city", "");
    }

    public void setCompany_City(String user) {
        sh.edit().putString("sdpi_city", user).commit();
    }

    public String getCompany_PhoneNumber() {
        return sh.getString("sdpi_phone", "");
    }

    public void setCompany_PhoneNumber(String user) {
        sh.edit().putString("sdpi_phone", user).commit();
    }

    public String getCompany_icon() {
        return sh.getString("sdpi_icon", "");
    }

    public void setCompany_icon(String user) {
        sh.edit().putString("sdpi_icon", user).commit();
    }

    public String getCompany_address() {
        return sh.getString("address", "");
    }

    public void setCompany_address(String user) {
        sh.edit().putString("address", user).commit();
    }

    public String getCompany_Help() {
        return sh.getString("help", "");
    }

    public void setCompany_Help(String user) {
        sh.edit().putString("help", user).commit();
    }


    /*******************************************************************************/

    public String getLastLogin_Time() {
        return sh.getString("login_date", "");
    }

    public void setLastLogin_Time(String user) {
        sh.edit().putString("login_date", user).commit();
    }

//
//    public static void saveFriend(Context context, ArrayList<Friends> list) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor ed = sharedPreferences.edit();
//        Log.d("Fav Saved: ", new Gson().toJson(list));
//        ed.putString("favourites", new Gson().toJson(list));
//        ed.apply();
//    }
//
//    public static ArrayList<Friends> getFriendsList(Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        TypeToken<ArrayList<Friends>> token = new TypeToken<ArrayList<Friends>>() {
//        };
//        ArrayList<Friends> favourites = new Gson()
//                .fromJson(sharedPreferences.getString("favourites", ""), token.getType());
//
//        if (null == favourites) {
//            favourites = new ArrayList<>();
//            return favourites;
//        } else {
//            return favourites;
//        }
//    }
//
//    public static void removeFriend(Context context, String adId) {
//        ArrayList<Friends> favorites = getFriendsList(context);
//        for (int i = 0; i < favorites.size(); i++) {
//            if (favorites.get(i).getId().equalsIgnoreCase(adId))
//                favorites.remove(i);
//        }
//        saveFriend(context, favorites);
//    }
//
//    public static boolean isAllreadyFriend(Context context, String adId) {
//        ArrayList<Friends> favorites = getFriendsList(context);
//        for (int i = 0; i < favorites.size(); i++) {
//            if (favorites.get(i).getPhoneNumber().equalsIgnoreCase(adId)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static void clearFriendList(Context context) {
//        ArrayList<Friends> notifications = new ArrayList<>();
//        saveFriend(context, notifications);
//    }

    /*************************User Detail Status****************[End]***************/


}