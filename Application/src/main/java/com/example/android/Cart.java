package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

    ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView1=findViewById(R.id.cart_list);



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


        MyAdapter cartAdapter=new MyAdapter();
        listView1.setAdapter(cartAdapter);

    }


    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return cartList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
//            view=getLayoutInflater().inflate(R.layout.device_card,null);
//
//            TextView name=view.findViewById(R.id.food_name);
//            TextView price=view.findViewById(R.id.price);
//            TextView des=view.findViewById(R.id.food_description);
//
//            name.setText(cartList.get(i).foodName);
//            des.setText(cartList.get(i).quantity);
            view=getLayoutInflater().inflate(R.layout.device_name,null);

            TextView name=view.findViewById(R.id.name_device);

            name.setText("Hii this is ");


            return view;

        }
    }
}
