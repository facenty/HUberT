package com.hyper.ubertransport.my_account.usable;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyper.ubertransport.R;

import java.util.List;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyViewHolder> {

    private final List<UserData> userDataList;
    private final CustomClickListener clickListener;


    public MyAccountAdapter(List<UserData> userDataList, Context context, CustomClickListener clickListner) {
        this.userDataList = userDataList;
        this.clickListener = clickListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_myaccount_adapter, parent, false);
        final MyViewHolder mViewHolder = new MyViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(mViewHolder.getPosition());
            }
        });

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserData userData = userDataList.get(position);
        holder.description.setText(userData.getDescription());
        holder.value.setText(userData.getValue());

    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView description;
        final TextView value;

        MyViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.my_account_description);
            value = view.findViewById(R.id.my_account_value);

        }

    }
}




