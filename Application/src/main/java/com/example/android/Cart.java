package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.bluetoothchat.R;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Cart extends Activity {
    Bundle extras;
    ArrayList<FoodData> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);



        extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("CartList");


        //this part forchange to object

        // ArrayList<FoodData> foodlist=new ArrayList<FoodData>();

        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);

            cartList=(ArrayList<FoodData>) in.readObject();
            Toast.makeText(getApplicationContext(),String.valueOf(cartList.size()),Toast.LENGTH_LONG).show();


        }
        catch (IOException ex)
        {

        }
        catch (ClassNotFoundException ex)
        {

        }
    }
}
