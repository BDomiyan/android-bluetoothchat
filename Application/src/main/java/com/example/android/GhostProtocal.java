package com.example.android;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class GhostProtocal {
    public   static final int LEN=1024;        //this is length of GP Packet
    public static final int HEADER_LEN=4;
    public static final int BODY_LEN=1020;

    public static final int SEND_MENU=1001;
    public static final int THIS_IS_MENU=1002;
    public static final int MY_ORDER=1003;

    public static final int CONFORM_ORDER=1110;
    public static final int READY_ORDER=1111;
    public static final int CLOSE_ORDER=1112;


    //this is to store state of order
    public static final String CONFORM_STATE="Preparing your order";
    public static final String READY_STATE="Order is ready";
    public static final String CLOSE_STATE="Order Closed";


    public int getHeaderVal (byte[] byArr)
    {
        byte[] header=new byte[4];      //this is to get number
        int headerValue;

        //getting header byte value
        for(int i=0;i<4;i++)
        {
            header[i]=byArr[i];
        }

        headerValue= ByteBuffer.wrap(header).getInt();
        return headerValue;
    }


    //to this has to implement responce from shop
    public byte[] requestMenu()
    {
        byte[] arr=ByteBuffer.allocate(4).putInt(SEND_MENU).array();
        return arr;
    }


    //below three function for
    public byte[] sendConformOrder()
    {
        return ByteBuffer.allocate(4).putInt(CONFORM_ORDER).array();
    }

    public byte[] sendReadyOrder()
    {
        return ByteBuffer.allocate(4).putInt(READY_ORDER).array();
    }

    public byte[] sendCloseOrder()
    {
        return ByteBuffer.allocate(4).putInt(CLOSE_ORDER).array();
    }


    //send full package to this package
    public ArrayList<FoodData> converteToFoodData(byte[] byArr)
    {
        ArrayList<FoodData> arr =new ArrayList<>();
        byte[] body=new byte[BODY_LEN];

        for(int i=HEADER_LEN;i<LEN;i++)
        {
            body[i]=byArr[i];
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(byArr);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            arr=(ArrayList<FoodData>) in.readObject();
            //Toast.makeText(getApplicationContext(),String.valueOf(cartList.size()),Toast.LENGTH_LONG).show();
        }
        catch (IOException ex)
        {

        }
        catch (ClassNotFoundException ex)
        {

        }
        return arr;
    }


    //this method convert fooddata arraylist to bytearray
    public byte[] FoodDataToByte(ArrayList<FoodData> foodarr)
    {
        String filename="file.ser";
        ObjectOutputStream out;
        byte[] data = new byte[0];

        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(foodarr);
            oos.flush();
            data = bos.toByteArray();
        }
        catch (IOException ex)
        {

        }

        return data;
    }


    public String FoodDataToString(ArrayList<FoodData> food)
    {
        int count=food.size();
        String output="+";

        for (int i=0;i<count;i++)
        {
            String price=String.valueOf(food.get(i).price);
            String qua=String.valueOf(food.get(i).quantity);
            output=output+"!"+food.get(i).foodName+"@"+food.get(i).cat+"#"+price+"$"+qua;
        }
        output=output+"+";

        return output;
    }


    public String byteToString(byte[] arr)
    {
        int len=arr.length;
        byte[] finalarr=new byte[len-4];


        for (int i=0;i<finalarr.length;i++)
        {
            finalarr[i]=arr[i+4];
        }
        String str=new String(finalarr);

        return str;
    }


    public ArrayList<FoodData> stringToFoodData(String data)
    {
        ArrayList<FoodData> foodList=new ArrayList<>();

        String name="test";
        String cat="test";
        int price=1000;
        int qua=1000;
        char temp;
        int prev=0;

        if(data.charAt(0)=='+')
        {
            for (int i=1;i<data.length();i++)
            {
                temp=data.charAt(i);

                if(temp=='!')
                {
                    if(prev==0)
                    {
                        prev=i;
                    }
                    else
                    {
                        qua=Integer.parseInt(data.substring(prev+1,i));
                        foodList.add(new FoodData(name,cat,price,qua));
                        //arr.add(new FoodData(data.substring(prev+1,i),"Fast",100));
                        prev=i;

                    }
                }

                if(temp=='@')
                {
                    name=data.substring(prev+1,i);
                    //arr.add(new FoodData(data.substring(prev+1,i),"Fast",100));
                    prev=i;
                }

                if(temp=='#')
                {
                    cat=data.substring(prev+1,i);
                    //arr.add(new FoodData(data.substring(prev+1,i),"Fast",100));
                    prev=i;
                }

                if(temp=='$')
                {
                    price=Integer.parseInt(data.substring(prev+1,i));
                    //arr.add(new FoodData(data.substring(prev+1,i),"Fast",100));
                    prev=i;
                }

                if(temp=='+')
                {
                    qua=Integer.parseInt(data.substring(prev+1,i));
                    foodList.add(new FoodData(name,cat,price,qua));
                    //arr.add(tempObj);
                    //arr.add(new FoodData(data.substring(prev+1,i),"Fastx",100));
                    prev=i;
                }
            }
        }

        return foodList;

    }







}
