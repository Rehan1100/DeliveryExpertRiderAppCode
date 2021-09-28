/*
package com.expertorider.ProjectBackend.DataBaseObject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.expertorider.Communications.response.Model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TestDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(ArrayList<OrderDetail> listofTest);

        @Query("Select * from Orders")
        LiveData<List<OrderDetail>> GetAllOrders();

        @Query("DELETE FROM Orders")
        void DeleteOrders();

}
*/
