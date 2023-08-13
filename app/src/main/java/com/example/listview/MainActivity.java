package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView itemListView;
    ArrayList<String> itemList;
    ArrayAdapter<String> itemAdapter;
    Handler handler;
    Connection con;
    Statement stmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemListView = findViewById(R.id.itemListView);
        itemList = new ArrayList<>();
        itemAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        itemListView.setAdapter(itemAdapter);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateListView();
                handler.postDelayed(this, 5000); // Refresh every 5 seconds
            }
        }, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void updateListView() {
        try {
            con = connectionClass();
            stmt = con.createStatement();
            String query = "SELECT * FROM Checkout_Product WHERE QR_ID ='2334'";
            ResultSet rs = stmt.executeQuery(query);

            itemList.clear();
            while (rs.next()) {
                String itemPrice = rs.getString("Item_Price");
                String itemCategory = rs.getString("Item_Category");

                itemList.add("Item: " + itemCategory + ", Price: " + itemPrice);
            }

            con.close();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    itemAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("NewApi")
    public Connection connectionClass() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://sqlserverrcloset.database.windows.net:1433;DatabaseName=DbRcloset;user=adminRcloset@sqlserverrcloset;password=Password_Rcloset;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            connection = DriverManager.getConnection(connectionURL);
        } catch (Exception e) {
            Log.e("SQL Connection Error: ", e.getMessage());
        }

        return connection;
    }
}