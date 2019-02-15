package com.spaceo.cardviewtutorial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spaceo.cardviewtutorial.R;
import com.spaceo.cardviewtutorial.bean.Detail;

import java.util.ArrayList;

/**
 * Created by sotsys-219 on 23/8/16.
 */
public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.CustomViewHolder> {

    private ArrayList<Detail> arrayDetails;

    public DetailAdapter(Context context, ArrayList<Detail> arrayDetails) {
        this.arrayDetails = arrayDetails;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycleview, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        customViewHolder.tvName.setText(arrayDetails.get(i).getName());
        customViewHolder.tvEmailId.setText(arrayDetails.get(i).getEmailId());

    }

    @Override
    public int getItemCount() {
        return (null != arrayDetails ? arrayDetails.size() : 0);
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvEmailId;

        public CustomViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
        }
    }

}