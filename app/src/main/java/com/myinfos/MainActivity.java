package com.myinfos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myinfos.adapter.ContactListAdapter;
import com.myinfos.db.MyDb;
import com.myinfos.models.InfoDetails;
import com.myinfos.supports.ConnectionDetector;
import com.myinfos.supports.OnItemClickListener;
import com.myinfos.supports.Services;
import com.myinfos.supports.WebServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyDb db;
    private ProgressBar progressBar;
    private TextView txtMsg;
    private RecyclerView contactListView;
    private List<InfoDetails> infoDetailsList;
    private ContactListAdapter listAdapter;
    private LoadContacts loadContacts;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new MyDb(this);
        contactListView = (RecyclerView) findViewById(R.id.contact_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtMsg = (TextView) findViewById(R.id.txt_msg);
        contactListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        infoDetailsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantRuntimePermission();
        }
        loadContacts();

    }

    private void loadContacts() {
        if (ConnectionDetector.isConnectingToInternet(this)) {
            txtMsg.setVisibility(View.GONE);
            contactListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            loadContacts = new LoadContacts();
            loadContacts.execute();
        } else {
            int count = db.getContactsCount();
            if (count > 0) {
                infoDetailsList = db.getAllContacts();
            } else {
                Toast.makeText(MainActivity.this, "Internet Connection must for first use", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class LoadContacts extends AsyncTask<Void, Void, Void> {
        String Response, strUrl;

        @Override
        protected Void doInBackground(Void... params) {
            strUrl = Services.url;
            try {
                Response = WebServiceHandler.executePost(strUrl, convertToJSONforDeviceRequest());
                Log.e("WebService Response: ", "" + Response);
                if (Response != null) {
                    db.deleteAll();
                    JSONObject object = new JSONObject(Response);
                    JSONArray jsonArray = object.getJSONArray("Contacts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objects = jsonArray.getJSONObject(i);
                        InfoDetails details = new InfoDetails();
                        details.id = objects.getString("Id");
                        details.fname = objects.getString("fname");
                        details.lname = objects.getString("lname");
                        details.email = objects.getString("email");
                        details.mobile = objects.getString("mobile");
                        details.landline = objects.getString("phone");
                        details.city = objects.getString("city");
                        details.area = objects.getString("area");
                        details.address = objects.getString("address");
                        details.latitude = objects.getString("latitude");
                        details.longitude = objects.getString("longitude");
                        details.pincode = objects.getString("pincode");
                        db.addContact(details);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            infoDetailsList = db.getAllContacts();
            if (infoDetailsList.size() > 0) {
                listAdapter = new ContactListAdapter(MainActivity.this, infoDetailsList);
                listAdapter.SetOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        callPhoneIntent(position);
                    }
                });
                contactListView.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                contactListView.setVisibility(View.VISIBLE);
                txtMsg.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                txtMsg.setVisibility(View.VISIBLE);
                contactListView.setVisibility(View.GONE);
            }
        }
    }


    private String convertToJSONforDeviceRequest() {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("deviceType", "Android");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonParam.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sync) {
            if (ConnectionDetector.isConnectingToInternet(this)) {
                loadContacts();
            } else {
                Toast.makeText(MainActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
        if(item.getItemId()==R.id.search){

        }
        return super.onOptionsItemSelected(item);
    }


    void callPhoneIntent(int i){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},200);
            return;
        } else {
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:" + infoDetailsList.get(i).getMobile()));
            startActivity(phoneIntent);
        }
    }

    private void grantRuntimePermission() {
        for (String PERMISSION : PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if ((checkSelfPermission(PERMISSION)) != 0) {
                    if (PERMISSION.equalsIgnoreCase(Manifest.permission.CALL_PHONE)) {
                        requestPermissions(PERMISSIONS, 1);
                    }
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "Success Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, "Fail Permission Denied", Toast.LENGTH_SHORT).show();
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        promptSettings();
                    } else {
                        promptSettings();
                    }
                }
        }
    }

    private void promptSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Open Permission Settings");
//        builder.setMessage(R.string.denied_never_ask_msg);
        builder.setPositiveButton("go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goToSettings();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setCancelable(false);
        builder.show();
    }

    private void goToSettings() {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + this.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(myAppSettings);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(loadContacts!=null){
            loadContacts.cancel(true);
            loadContacts=null;
        }
    }
}
