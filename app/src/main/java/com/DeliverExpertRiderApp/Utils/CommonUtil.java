package com.DeliverExpertRiderApp.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.DeliverExpertRiderApp.App;
import com.DeliverExpertRiderApp.Communications.ApiConstants;
import com.DeliverExpertRiderApp.ui.activities.MainActivity;
import com.expertorider.R;

import org.aviran.cookiebar2.CookieBar;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class CommonUtil {

    public static boolean isNumberValid(String number){
        //return password.length() >= 6;
        Pattern pattern;
        Matcher matcher;
        final String NUMBER_PATTERN = "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$";
        pattern = Pattern.compile(NUMBER_PATTERN);
        matcher = pattern.matcher(number);
        return matcher.matches();
    }
    public static String getCurrency() {
        if(ApiConstants.settings!=null)
            return ApiConstants.settings.getCurrency();
        else
        {
            return "";
        }
    }
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public static boolean IsInternetConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            //     Log.v(TAG, "Internet Connection Not Present");
            return false;
        }
    }


    public static void InternetConnectionErrorDialog(Context context){
        CookieBar.build((Activity) context)
                .setTitle("Internet Connection")
                .setTitleColor(R.color.white)
                .setIcon(R.drawable.crop_image_menu_crop)
                .setCookiePosition(CookieBar.TOP)
                .setEnableAutoDismiss(false)
                .setBackgroundColor(R.color.error_color)
                .setMessage("Internet Connection Error")
//                                .setAction("Go To Setting", new OnActionClickListener() {
//                                    @Override
//                                    public void onClick() {
//                                        // Do something
//                                    }
//                                })
                .setDuration(5000)
                .show();// 5 seconds
    }

    public static void PlayAudio(Context context){

        MediaPlayer mediaPlayer=MediaPlayer.create(context,Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sms_tone));
        mediaPlayer.start();
    }

    public static void MakePhoneCall(Context context, String phone_number){


        Uri uri = Uri.parse("tel:"+phone_number);
        Intent intentdial = new Intent(Intent.ACTION_DIAL, uri);
        intentdial.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentdial);


    }


    public static String getCurrentDateCalendar() {
        Calendar c = Calendar.getInstance();
//        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM hh:mm aa");
        //DATE_FORMAT_9 = h:mm a dd MMMM yyyy
        //The output will be -: 10:37 am 05 December 2018
        //  MMM = Month in words (Nov)  ||  MM = Month in number (11)
//        SimpleDateFormat dateformat = new SimpleDateFormat("ddMMM-yyyy h:mm:ss aa");

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = simpleDateFormat.format(c.getTime());
        System.out.println(datetime);
        //  String formattedtime=dateFormat.format(timeFormat);
        return datetime;

    }


    public static String getTomorrowDateCalendar() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String todayAsString = simpleDateFormat.format(today);
        String tomorrowAsString = simpleDateFormat.format(tomorrow);

        System.out.println(todayAsString);
        System.out.println(tomorrowAsString);
        return tomorrowAsString;

    }

    //    public static long Difference(Date startDate, Date endDate) {
    public static long getLoginSeconds(String startDate, String endDate) {
        //milliseconds

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        try {
            Date date1 = simpleDateFormat.parse(startDate);
            Date date2 = simpleDateFormat.parse(endDate);
            long different = date1.getTime() - date2.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;


            long elapsedSeconds = different / secondsInMilli;

            return elapsedSeconds;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //   long different = endDate.getTime() - startDate.getTime();

//        System.out.println("startDate : " + startDate);
//        System.out.println("endDate : "+ endDate);
        //System.out.println("different : " + different);

//        long secondsInMilli = 1000;
//        long minutesInMilli = secondsInMilli * 60;
//        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;
//
//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;
//
//        long elapsedHours = different / hoursInMilli;
//        different = different % hoursInMilli;
//
//        long elapsedMinutes = different / minutesInMilli;
//        different = different % minutesInMilli;
//
//        long elapsedSeconds = different / secondsInMilli;
//
        return 0;

//        System.out.printf(
//                "%d days, %d hours, %d minutes, %d seconds%n",
//                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }


    public static String getOTPCode() {
        int randomPin   =(int)(Math.random()*9000)+1000;
        String otp  =  String.valueOf(randomPin);
        return otp;

    }


    public static void OpenMap(Context context,double destinationLatitude,double destinationLongitude){
//        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", destinationLatitude, destinationLongitude);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

        String uri = "http://maps.google.com/maps?daddr=" + destinationLatitude + "," + destinationLongitude + " (" + "Where the Agent is at" + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void forceUpdate(Context context){
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion =  packageInfo.versionName;
        //  Toast.makeText(getApplicationContext(),currentVersion+packageInfo,Toast.LENGTH_LONG).show();
        new ForceUpdateAsync(currentVersion, context).execute();
        //      Toast.makeText(this, ""+this.getPackageName()+"\n"+currentVersion+"\n"+packageInfo, Toast.LENGTH_SHORT).show();
    }


    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static boolean checkPermissionForReadExtertalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }



    public static void ShowNotification(Context context,final String title, final String msg) {

        RemoteViews remoteCollapsedViews = new RemoteViews(context.getPackageName(), R.layout.custom_notification_expanded);
        remoteCollapsedViews.setTextViewText(R.id.title_notify, title);
        remoteCollapsedViews.setTextViewText(R.id.desp_notify, msg);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setCustomContentView(remoteCollapsedViews)
                        .setCustomBigContentView(remoteCollapsedViews)
                        .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sms_tone))
                        .setAutoCancel(true)
//                        .setBadgeIconType()
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(title))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg).setSummaryText("#hashtag"))
                        .setContentText(msg);

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,  PendingIntent.FLAG_ONE_SHOT);
        //startActivity(intent);

        builder.setContentIntent(pendingIntent);

        // Config.PlayAudio(this);


        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nChannel = new NotificationChannel(App.CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_HIGH);
            nChannel.enableLights(true);
            assert manager != null;
            builder.setChannelId(App.CHANNEL_ID);
            nChannel.setShowBadge(false);
            manager.createNotificationChannel(nChannel);
        }
        assert manager != null;
        manager.notify(0, builder.build());


    }
}
