package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bluetoothchat.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FoodDetails extends Activity {
    Bundle extras;
    FoodData obj;
    int q=0;
    int total;
    int pos;

    TextView textViewReduce,textViewIncrease,textViewQuantity,textTotal;
    Button buttonAddToCart,buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        getActionBar().hide();

        getData();
        uiAssign();
        //Toast.makeText(getApplicationContext(),obj.foodName,Toast.LENGTH_LONG).show();

        textViewIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                q=q+1;
                String temp="$"+String.valueOf(q*obj.price);
                textViewQuantity.setText(String.valueOf(q));
                textTotal.setText(temp);
            }
        });

        textViewReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(q<1)
                {

                }
                else {
                    q=q-1;
                    String temp="$"+String.valueOf(q*obj.price);
                    textViewQuantity.setText(String.valueOf(q));
                    textTotal.setText(temp);
                }
            }
        });



        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Quantity",q);
                intent.putExtra("Position",pos);

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void uiAssign()
    {
        textViewIncrease=findViewById(R.id.increse);
        textViewQuantity=findViewById(R.id.qua);
        textViewReduce=findViewById(R.id.redce);
        textTotal=findViewById(R.id.total);

        buttonAddToCart=findViewById(R.id.button_cart);
        buttonCancel=findViewById(R.id.button_cancel);
    }


    private void getData()
    {
        extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("FoodData");
        pos=extras.getInt("Position");


        //this part forchange to object

        // ArrayList<FoodData> foodlist=new ArrayList<FoodData>();

        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);

            obj=(FoodData) in.readObject();
            //Toast.makeText(getApplicationContext(),obj.price,Toast.LENGTH_LONG).show();


        }
        catch (IOException ex)
        {

        }
        catch (ClassNotFoundException ex)
        {

        }

    }
}
