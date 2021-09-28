package com.DeliverExpertRiderApp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.DeliverExpertRiderApp.ui.activities.MainActivity;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;

public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

    private String latestVersion;
    private String currentVersion;
    private Context context;
    public ForceUpdateAsync(String currentVersion, Context context){
        this.currentVersion = currentVersion;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
//
//        try {
//     //                        https://play.google.com/store/apps/details?id=com.solutionsplayer.donateblood
////            Toast.makeText(context, "pkg name : "+context.getPackageName(), Toast.LENGTH_SHORT).show();
//            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+context.getPackageName()+"&hl=en")
//                    .timeout(30000)
//                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com")
//                    .get()
//                    .select("div[itemprop=softwareVersion]")
//                    .first()
//                    .ownText();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id="+context.getPackageName()+ "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
              //      .select("div[itemprop=softwareVersion]")
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
            Log.e("latestversion","---"+latestVersion);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if(latestVersion!=null){
            if(!currentVersion.equalsIgnoreCase(latestVersion)){
              //   Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();

                if((context instanceof MainActivity)) {
                    if(!((Activity)context).isFinishing()){
                        showForceUpdateDialog();
                        Toast.makeText(context,"update is available.", Toast.LENGTH_LONG).show();

                    }

                }
                else {
          //          Toast.makeText(context,"no update is available.",Toast.LENGTH_LONG).show();

                }
            }
        }
        super.onPostExecute(jsonObject);
    }

    public void showForceUpdateDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Update App");
        alertDialogBuilder.setMessage("New Version is avalible" + " " + latestVersion );
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
           //     context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                dialog.cancel();
            }
        });
        alertDialogBuilder.show();
    }


}