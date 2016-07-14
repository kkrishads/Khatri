package com.myinfos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myinfos.adapter.ContactListAdapter;
import com.myinfos.db.MyDb;
import com.myinfos.models.InfoDetails;
import com.myinfos.supports.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 14/07/2016.
 */
public class SearchActivity extends AppCompatActivity {
    private MyDb db;
    private ProgressBar progressBar;
    private TextView txtMsg;
    private RecyclerView contactListView;
    private EditText edtSearch;
    private List<InfoDetails> infoDetailsList;
    private ContactListAdapter listAdapter;
    private static String[] PERMISSIONS = {Manifest.permission.CALL_PHONE};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        db = new MyDb(this);
        contactListView = (RecyclerView) findViewById(R.id.contact_list);
        edtSearch=(EditText) findViewById(R.id.edt_name);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtMsg = (TextView) findViewById(R.id.txt_msg);
        contactListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        infoDetailsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grantRuntimePermission();
        }

        loadAllList(0);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                loadAllList(1);
            }
        });
    }





    void loadAllList(int i){
        if(i==0) {
            infoDetailsList = db.getAllContacts();
        } else {
            infoDetailsList=db.getAllContacts(edtSearch.getText().toString());
        }
        if (infoDetailsList.size() > 0) {
            listAdapter = new ContactListAdapter(SearchActivity.this, infoDetailsList);
            listAdapter.SetOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    callPhoneIntent(position);
                }
            });
            contactListView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            contactListView.setVisibility(View.VISIBLE);
            txtMsg.setVisibility(View.GONE);
        } else {
            txtMsg.setVisibility(View.VISIBLE);
            contactListView.setVisibility(View.GONE);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
