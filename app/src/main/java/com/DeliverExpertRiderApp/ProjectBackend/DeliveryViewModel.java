/*
package com.expertorider.ProjectBackend;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.expertorider.Communications.response.Model.OrderDetail;
import com.expertorider.ProjectBackend.Repositry.DeliveryRiderRepositry;

import java.util.ArrayList;
import java.util.List;

public class DeliveryViewModel extends AndroidViewModel {
    private DeliveryRiderRepositry categoryRepositry;
    private LiveData<List<OrderDetail>> getAllCategory;

    public DeliveryViewModel(@NonNull Application application) {
        super(application);
        categoryRepositry= new DeliveryRiderRepositry(application);
        getAllCategory= categoryRepositry.getGetAlltest();
    }
    public void Insert(ArrayList<OrderDetail> list)
    {
        categoryRepositry.insert(list);
    }
    public LiveData<List<OrderDetail>> getALLCategry()
    {
        return getAllCategory;
    }


}
*/
