package com.hyper.ubertransport.driver.orders.usable;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.my_account.usable.CustomClickListener;
import com.hyper.ubertransport.order.usable.OrderInfo;

import java.util.List;

public class DriverOrdersAdapter extends RecyclerView.Adapter<DriverOrdersAdapter.MyViewHolder> {

    private final List<OrderInfo> orderList;
    private final Context mContext;
    private final CustomClickListener clickListener;
    private final String to;


    public DriverOrdersAdapter(List<OrderInfo> orderList, Context context, CustomClickListener clickListner, String to) {
        this.orderList = orderList;
        this.mContext = context;
        this.clickListener = clickListner;
        this.to = to;
    }

    public void add(OrderInfo s,int position) {
        position = position == -1 ? getItemCount()  : position;
        orderList.add(position,s);
        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            orderList.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_my_shipment_adapter, parent, false);
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

        OrderInfo orderInfo = orderList.get(position);

        holder.iconImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delivery_icon));
        String stringBuilder = orderInfo.getFromName() +
                " " +
                to +
                " " +
                orderInfo.getDestinationName();
        holder.value.setText(stringBuilder);
        holder.statusValueTextView.setText(orderInfo.getPackageStatus().toString());
        holder.priceTextView.setText(orderInfo.getPrice());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        final ImageView iconImageView;
        final TextView value;
        final TextView statusValueTextView;
        final TextView priceTextView;

        MyViewHolder(View view) {
            super(view);
            iconImageView = view.findViewById(R.id.icon);
            value = view.findViewById(R.id.my_shipment_value);
            statusValueTextView = view.findViewById(R.id.my_shipment_status_value);
            priceTextView = view.findViewById(R.id.price_shipment);
        }

    }
}




