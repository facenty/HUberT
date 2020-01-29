package com.hyper.ubertransport.order.usable;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

public class DistanceLoader extends AsyncTaskLoader<Long> {

    private final GoogMatrixRequest googMatrixRequest;

    public DistanceLoader(@NonNull Context context, String from, String destination) {
        super(context);
        googMatrixRequest = new GoogMatrixRequest();
        googMatrixRequest.setStr_from(from);
        googMatrixRequest.setStr_to(destination);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public Long loadInBackground() {
        return googMatrixRequest.transfer();
    }
}
