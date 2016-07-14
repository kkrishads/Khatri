package com.myinfos;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myinfos.db.MyDb;
import com.myinfos.models.InfoDetails;

import java.util.List;
import java.util.Locale;

public class SwipeFragment extends Fragment {

    private int position = 0;
    private TextView txtSno, txtFname, txtLname, txtCity, txtPinCode, txtAddress, txtArea, txtMobile, txtLandline;
    private ImageView imgLandCall, imgMobCall, imgMsg, imgLocate;
    private MyDb db;
    private InfoDetails details;
    private List<InfoDetails> infoDetailsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
        db = new MyDb(getActivity());
        txtSno = (TextView) swipeView.findViewById(R.id.txt_sno);
        txtFname = (TextView) swipeView.findViewById(R.id.txt_fname);
        txtLname = (TextView) swipeView.findViewById(R.id.txt_lname);
        txtCity = (TextView) swipeView.findViewById(R.id.txt_city);
        txtPinCode = (TextView) swipeView.findViewById(R.id.txt_pincode);
        txtAddress = (TextView) swipeView.findViewById(R.id.txt_address);
        txtArea = (TextView) swipeView.findViewById(R.id.txt_area);
        txtMobile = (TextView) swipeView.findViewById(R.id.txt_mobile);
        txtLandline = (TextView) swipeView.findViewById(R.id.txt_phone);
        imgLandCall = (ImageView) swipeView.findViewById(R.id.img_call);
        imgLocate = (ImageView) swipeView.findViewById(R.id.img_locate);
        imgMobCall = (ImageView) swipeView.findViewById(R.id.img_mob_call);
        imgMsg = (ImageView) swipeView.findViewById(R.id.img_mob_msg);
        Bundle args = getArguments();
        position = args.getInt("position");
        infoDetailsList = db.getAllContacts();
        setDefaults(position);
        return swipeView;
    }

    static SwipeFragment newInstance(int position) {
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        swipeFragment.setArguments(args);
        return swipeFragment;
    }

    void setDefaults(int position) {
        details = infoDetailsList.get(position);
        if (details != null) {
            setValues(txtSno, details.id);
            setValues(txtAddress, details.address);
            if (details.landline != null && !details.landline.isEmpty()) {
                txtLandline.setText("" + details.landline);
                imgLandCall.setVisibility(View.VISIBLE);
                imgLandCall.setOnClickListener(btnListener);
            } else {
                txtLandline.setText("-");
                imgLandCall.setVisibility(View.GONE);
            }
            if (details.mobile != null && !details.mobile.isEmpty()) {
                txtMobile.setText("" + details.mobile);
                imgMobCall.setOnClickListener(btnListener);
                imgMsg.setOnClickListener(btnListener);
                imgMsg.setVisibility(View.VISIBLE);
                imgMobCall.setVisibility(View.VISIBLE);
            } else {
                txtMobile.setText("-");
                imgMsg.setVisibility(View.GONE);
                imgMobCall.setVisibility(View.GONE);
            }
            setValues(txtPinCode, details.pincode);
            imgLocate.setOnClickListener(btnListener);
            setValues(txtArea, details.area);
            setValues(txtCity, details.city);
            setValues(txtFname, details.fname);
            setValues(txtLname, details.lname);
        }

    }

    private void setValues(TextView txtView, String value) {
        if (value != null && !value.isEmpty()) {
            txtView.setText(value);
        } else {
            txtView.setText("-");
        }
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (v == imgLandCall) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + details.landline));
                startActivity(phoneIntent);
            } else if (v == imgLocate) {
                if (details.latitude != null && !details.latitude.isEmpty() && details.longitude != null && !details.longitude.isEmpty()) {
//                    String uri = String.format(Locale.ENGLISH, "geo:<"+details.latitude+"> ,< "+ details.longitude+">");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + details.latitude  + ">,<" + details.longitude + ">?q=<" + details.latitude  + ">,<" + details.longitude + ">"));
                    startActivity(intent);
                } else {

                    String uri = "http://maps.google.com/maps?q=" + details.getArea().toString().trim().replaceAll("&", "and").replaceAll(" ", "+") + ", "
                            + details.getCity().toString().trim().replaceAll(" ", "+");
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    getActivity().startActivity(intent);

                }

            } else if (v == imgMobCall) {
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + details.mobile));
                startActivity(phoneIntent);
            } else if (v == imgMsg) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", details.mobile, null)));
            }
        }
    };


}