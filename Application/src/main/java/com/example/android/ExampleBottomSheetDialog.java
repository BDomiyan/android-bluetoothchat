package com.example.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.android.bluetoothchat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class ExampleBottomSheetDialog extends BottomSheetDialogFragment {
    //private BottomSheetListener mListener;
    private int stat;
    TextView status,textOrderConfirm,textReady,textOrderRecived;
    Button buttonOrderConfirm,buttonReady,buttonOrderRecived;

    public ExampleBottomSheetDialog(int st)
    {
        stat=st;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        //this part is for inisialize ui
        status=v.findViewById(R.id.status);
        textOrderConfirm=v.findViewById(R.id.textOrderConfirm);
        textReady=v.findViewById(R.id.textReady);
        textOrderRecived=v.findViewById(R.id.textOrderRecived);

        buttonOrderConfirm=v.findViewById(R.id.buttonOrderConfirm);
        buttonReady=v.findViewById(R.id.buttonReady);
        buttonReady=v.findViewById(R.id.buttonOrderRecived);

        if(stat==GhostProtocal.CONFORM_ORDER)
        {
            setConfirmBlue();
            setReadyGrey();
            setRecivedGrey();
            status.setText("Order Placed");
        }

        if(stat==GhostProtocal.READY_ORDER)
        {
            setConfirmBlue();
            setReadyBlue();
            setRecivedGrey();
            textReady.setTextColor(this.getResources().getColor(R.color.button_blue));
            status.setText("Ready Pick");
        }

        if (stat==GhostProtocal.CLOSE_ORDER)
        {
            setConfirmBlue();
            setReadyBlue();
            setRecivedBlue();
            textOrderRecived.setTextColor(this.getResources().getColor(R.color.button_blue));
            status.setText("Order Closed");
        }

        if (stat==GhostProtocal.NO_ORDER)
        {
            setReadyGrey();
            setRecivedGrey();
            setConfirmGrey();
            textOrderRecived.setTextColor(this.getResources().getColor(R.color.darkgrey));
        }







//        Button button1 = v.findViewById(R.id.button1);
//        Button button2 = v.findViewById(R.id.button2);
//        button1.setText(txt);
//        button1.setTextColor(this.getResources().getColor(R.color.button_blue));
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //mListener.onButtonClicked("Button 1 clicked");
//                dismiss();
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //mListener.onButtonClicked("Button 2 clicked");
//                dismiss();
//            }
//        });

        return v;
    }



    private void setConfirmGrey()
    {
        //buttonOrderConfirm.setBackgroundColor();
        //buttonOrderConfirm.setBackground(this.getResources().getDrawable(R.drawable.greycircle));
        textOrderConfirm.setTextColor(this.getResources().getColor(R.color.darkgrey));
    }

    private void setReadyGrey()
    {
       // buttonReady.setBackground(this.getResources().getDrawable(R.drawable.greycircle));
        textReady.setTextColor(this.getResources().getColor(R.color.darkgrey));
    }

    private void setRecivedGrey()
    {
       // buttonReady.setBackground(this.getResources().getDrawable(R.drawable.greycircle));
        textReady.setTextColor(this.getResources().getColor(R.color.darkgrey));
    }


    private void setConfirmBlue()
    {
        //buttonOrderConfirm.setBackgroundColor();
        //buttonOrderConfirm.setBackground(this.getResources().getDrawable(R.drawable.circle));
        textOrderConfirm.setTextColor(this.getResources().getColor(R.color.button_blue));
    }

    private void setReadyBlue()
    {
       // buttonReady.setBackground(this.getResources().getDrawable(R.drawable.circle));
        textReady.setTextColor(this.getResources().getColor(R.color.button_blue));
    }

    private void setRecivedBlue()
    {
       // buttonReady.setBackground(this.getResources().getDrawable(R.drawable.circle));
        textReady.setTextColor(this.getResources().getColor(R.color.button_blue));
    }




    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
           // mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}