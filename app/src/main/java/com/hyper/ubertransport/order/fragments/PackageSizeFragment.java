package com.hyper.ubertransport.order.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyper.ubertransport.R;
import com.hyper.ubertransport.maps.MapsActivity;
import com.hyper.ubertransport.maps.usable.MapsInterface;
import com.hyper.ubertransport.order.usable.OrderInfo;

public class PackageSizeFragment extends Fragment /**/ {

    private OrderInfo orderInfo;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageView1:
                    orderInfo.setSmallPackagesQuantity(Integer.toString(Integer.parseInt(orderInfo.getSmallPackagesQuantity()) + 1));
                    break;
                case R.id.imageView2:
                    orderInfo.setMediumPackagesQuantity(Integer.toString(Integer.parseInt(orderInfo.getMediumPackagesQuantity()) + 1));
                    break;
                case R.id.imageView3:
                    orderInfo.setBigPackagesQuantity(Integer.toString(Integer.parseInt(orderInfo.getBigPackagesQuantity()) + 1));
                    break;
                default:
                    return;
            }

            openOrderSummaryFragment();
        }
    };


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        final View view = inflater.inflate(R.layout.fragment_package_size, container, false);


        orderInfo = (OrderInfo) getArguments().getSerializable("order_info");
        ImageView smallButton = view.findViewById(R.id.imageView1);
        ImageView mediumButton = view.findViewById(R.id.imageView2);
        ImageView bigButton = view.findViewById(R.id.imageView3);

        smallButton.setOnClickListener(clickListener);
        mediumButton.setOnClickListener(clickListener);
        bigButton.setOnClickListener(clickListener);


        return view;
    }

    private void openOrderSummaryFragment() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("order_info", orderInfo);
        Fragment fragment = new SummaryFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .remove(this)
                .replace(R.id.flContent, fragment)
                .addToBackStack(null)
                .commit();

        ((MapsActivity) getActivity()).changeViewOnOrderSummaryOpen();
    }


    public interface SummaryFragmentCallback extends MapsInterface {

        void changeNavigationButtonBehaviourOnOpenFragment();

        void changeNavigationButtonBehaviourOnCloseFragment();

        void changeViewOnOrderSummaryOpen();

    }
}
