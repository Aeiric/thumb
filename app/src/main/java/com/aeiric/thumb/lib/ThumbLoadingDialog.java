package com.aeiric.thumb.lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.aeiric.thumb.R;


/**
 * @author xujian
 * @desc ThumbLoadingDialog
 * @from v1.0.0
 */
public class ThumbLoadingDialog extends Dialog {

    private final Context mContext;

    ThumbLoadingDialog(Context context) {
        super(context, R.style.customerDialog);
        this.mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout_loading);
        sizeDialog();
        setCanceledOnTouchOutside(false);

    }

    private void sizeDialog() {
        RelativeLayout confirm_dialog = findViewById(R.id.rv_layout);
        FrameLayout.LayoutParams confirm_dialogParams = (FrameLayout.LayoutParams) confirm_dialog.getLayoutParams();
        confirm_dialogParams.width = (int) (ThumbDensityUtil.getDevicesWidthPixels(mContext) * 0.75);
        confirm_dialogParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        confirm_dialog.setLayoutParams(confirm_dialogParams);
    }

}
