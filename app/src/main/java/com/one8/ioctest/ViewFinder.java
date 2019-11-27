package com.one8.ioctest;

import android.app.Activity;
import android.view.View;

public class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(Activity activity) {
        mActivity = activity;
    }

    public ViewFinder(View view) {
        mView = view;
    }

    public View findViewById(int viewId){
        return mActivity!=null?mActivity.findViewById(viewId):mView.findViewById(viewId);
    }
}
