package com.myinfos.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.myinfos.R;
import com.myinfos.SwipeActivity;
import com.myinfos.models.InfoDetails;
import com.myinfos.supports.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 14/07/2016.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ItemRowHolder> {
    private List<InfoDetails> dataList;
    private Context mContext;
    private OnItemClickListener mItemClickListener;
    public ContactListAdapter(Context context, List<InfoDetails> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder holder, final int i) {
        holder.txtName.setText(dataList.get(i).getFname());
        if (i % 2 == 0) {
            holder.rootView.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        } else {
            holder.rootView.setBackgroundColor(Color.WHITE);
        }

        holder.itemImgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, i);
                }

            }
        });

        holder.itemImgMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", dataList.get(i).getMobile(), null)));
            }
        });

    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public ImageView itemImgCall,itemImgMsg;
        public RelativeLayout rootView;
        public ItemRowHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            itemImgCall=(ImageView)view.findViewById(R.id.img_call);
            itemImgMsg=(ImageView)view.findViewById(R.id.img_msg);
            rootView=(RelativeLayout) view.findViewById(R.id.root_view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(mContext, SwipeActivity.class);
                    intent.putExtra("Id",dataList.get(getAdapterPosition()).id);
                    intent.putExtra("position",getAdapterPosition());
                    intent.putExtra("count",dataList.size());
                    mContext.startActivity(intent);

                }
            });



        }

    }
}
