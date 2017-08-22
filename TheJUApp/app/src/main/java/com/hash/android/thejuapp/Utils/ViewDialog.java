package com.hash.android.thejuapp.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.hash.android.thejuapp.R;

public class ViewDialog extends Dialog {

    public ViewDialog(@NonNull Context context) {
        super(context);
    }

    public void showDialog(Activity activity, String uid) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.qr_dialog);

        ImageView qrImageView = dialog.findViewById(R.id.a);

        QRUtils qrHelper = new QRUtils();
        try {
            Bitmap qr = qrHelper.encodeAsBitmap(uid, 800, 800);
            qrImageView.setImageBitmap(qr);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        Button dialogButton = dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
