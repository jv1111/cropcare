package com.example.cropcare;

import android.content.Context;
import android.util.Log;

import com.example.cropcare.Database.CoFarmerDatabaseHelper;
import com.example.cropcare.Model.CoFarmerModel;

import java.util.List;

public class TestCoFarm {

    public static void listAllCoFarmers(Context context) {
        CoFarmerDatabaseHelper cfdh = new CoFarmerDatabaseHelper(context);
        List<CoFarmerModel> coFarmerList = cfdh.getAllCoFarmers();

        if (coFarmerList.isEmpty()) {
            Log.i("myTag", "No co-farmers found.");
        } else {
            for (CoFarmerModel coFarmer : coFarmerList) {
                Log.i("myTag", "Co-Farmer ID: " + coFarmer.getId() + ", Parent User ID: " + coFarmer.getParentUserId() +
                        ", Username: " + coFarmer.getUsername());
            }
        }
    }

    public static void deleteAllCoFarmers(Context context) {
        CoFarmerDatabaseHelper cfdh = new CoFarmerDatabaseHelper(context);
        cfdh.deleteAllCoFarmers();
        Log.i("myTag", "All co-farmers deleted successfully.");
    }
}
