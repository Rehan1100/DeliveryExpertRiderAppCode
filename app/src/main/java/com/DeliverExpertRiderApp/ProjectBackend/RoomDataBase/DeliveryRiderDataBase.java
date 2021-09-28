/*
package com.expertorider.ProjectBackend.RoomDataBase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.expertorider.Communications.response.Model.OrderDetail;
import com.expertorider.ProjectBackend.DataBaseObject.TestDao;
import com.expertorider.ProjectBackend.VendorTypeConverter;

@Database(entities = {OrderDetail.class}, version = 2,exportSchema = false)
@TypeConverters({VendorTypeConverter.class})
public abstract class DeliveryRiderDataBase extends RoomDatabase {

    private static final String DatabaseName = "DeliveryRiderDataBase";

    public abstract TestDao dao();

    private static volatile DeliveryRiderDataBase INSTANCE;

    public static DeliveryRiderDataBase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            synchronized (DeliveryRiderDataBase.class) {
                INSTANCE = Room.databaseBuilder(context, DeliveryRiderDataBase.class
                        , DatabaseName)
                        .addCallback(callback)
                        .build();
            }
        }

        return INSTANCE;
    }

    static Callback callback= new Callback() {
        @Override
        public void onCreate(@NonNull  SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsync(INSTANCE);

        }
    };

    static class PopulateAsync extends AsyncTask<Void,Void,Void>
    {
        private TestDao dao;

        public PopulateAsync(DeliveryRiderDataBase dataBase) {
            this.dao =  dataBase.dao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            dao.DeleteOrders();
            return null;
        }
    }

}
*/
