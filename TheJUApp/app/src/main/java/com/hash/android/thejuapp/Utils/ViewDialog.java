package com.hash.android.srijan;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.hash.android.srijan.functions.QRHelper;

public class ViewDialog {

    public void showDialog(Activity activity, User authUser){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.qr_dialog);

        ImageView qrImageView = (ImageView) dialog.findViewById(R.id.a);

        QRHelper qrHelper = new QRHelper();
        try {
            Bitmap qr = qrHelper.encodeAsBitmap(authUser.getId(), 800, 800);
            qrImageView.setImageBitmap(qr);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
