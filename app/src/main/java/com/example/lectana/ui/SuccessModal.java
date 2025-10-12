package com.example.lectana.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.lectana.R;

public class SuccessModal extends Dialog {

    private Button btnOk;
    private OnSuccessListener listener;

    public interface OnSuccessListener {
        void onSuccess();
    }

    public SuccessModal(Context context) {
        super(context);
    }

    public SuccessModal(Context context, OnSuccessListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modal_success);
        
        // Hacer el diálogo no cancelable tocando fuera
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        btnOk = findViewById(R.id.btnOkSuccess);
        
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onSuccess();
                }
            }
        });
    }

    public void showModal() {
        show();
    }
}
