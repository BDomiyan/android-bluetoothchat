package com.example.android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
    TextView totalTxt;

    String order="order";
    String delete="delete";

    Button buttonOrder,buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView1=findViewById(R.id.cart_list);
        totalTxt=findViewById(R.id.cart_total);
        buttonOrder=findViewById(R.id.button_order);
        buttonDelete=findViewById(R.id.button_delete);

        getData();
        MyAdapter cartAdapter=new MyAdapter();
        listView1.setAdapter(cartAdapter);

        calTotal();


        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("message",20);

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("message",21);

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

    }


    private void getData()
    {
        extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("CartList");


        //this part forchange to object

        // ArrayList<FoodData> foodlist=new ArrayList<FoodData>();

        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);

            cartList=(ArrayList<FoodData>) in.readObject();
            //Toast.makeText(getApplicationContext(),String.valueOf(cartList.size()),Toast.LENGTH_LONG).show();


        }
        catch (IOException ex)
        {

        }
        catch (ClassNotFoundException ex)
        {

        }

    }


    private void calTotal()
    {
        int total=0;
        String totalString;

        if(cartList.size()>0)
        {
            for(FoodData i:cartList)
            {
                total=total+i.price*i.quantity;
            }

        }
        totalString="$"+String.valueOf(total);
        totalTxt.setText(totalString);

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
            view=getLayoutInflater().inflate(R.layout.cart_card,null);

            TextView name=view.findViewById(R.id.cart_name);
            TextView quantity=view.findViewById(R.id.cart_quantity);
            TextView price=view.findViewById(R.id.cart_price);


            name.setText(cartList.get(i).foodName);
            quantity.setText(String.valueOf(cartList.get(i).quantity));
            price.setText(String.valueOf(cartList.get(i).quantity*cartList.get(i).price));


            return view;

        }
    }
}
