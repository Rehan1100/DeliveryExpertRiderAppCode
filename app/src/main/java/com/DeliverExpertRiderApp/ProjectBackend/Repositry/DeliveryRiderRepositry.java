/*
package com.expertorider.ProjectBackend.Repositry;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.expertorider.Communications.response.Model.OrderDetail;
import com.expertorider.ProjectBackend.DataBaseObject.TestDao;
import com.expertorider.ProjectBackend.RoomDataBase.DeliveryRiderDataBase;

import java.util.ArrayList;
import java.util.List;

public class DeliveryRiderRepositry {

    private DeliveryRiderDataBase dataBase;
   private LiveData<List<OrderDetail>> getAllOrder;

    public DeliveryRiderRepositry(Application application) {
        dataBase = DeliveryRiderDataBase.getINSTANCE(application);
        getAllOrder = dataBase.dao().GetAllOrders();
    }

    public void insert(List<OrderDetail> list) {
        new InsertAsynTask(dataBase).execute(list);
    }

    public LiveData<List<OrderDetail>> getGetAlltest() {
        return getAllOrder;
    }

    static class InsertAsynTask extends AsyncTask<List, Void, Void> {
        public TestDao dao;

        public InsertAsynTask(DeliveryRiderDataBase dao) {
            this.dao = dao.dao();

        }

        @Override
        protected Void doInBackground(List... lists) {
            dao.DeleteOrders();
            dao.insert((ArrayList<OrderDetail>) lists[0]);
            return null;
        }
    }

}
*/
