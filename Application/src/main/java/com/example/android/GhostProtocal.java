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
    private  static final int LEN=10240;        //this is length of GP Packet
    private static final int HEADER_LEN=4;
    private static final int BODY_LEN=10236;

    private static final int SEND_MENU=1001;
    private  static final int THIS_IS_MENU=1002;
    private static final int MY_ORDER=1003;

    private static final int CONFORM_ORDER=1110;
    private static final int READY_ORDER=1111;
    private static final int CLOSE_ORDER=1112;

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


    //send full package to this package
    public ArrayList<FoodData> converteToFoodData(byte[] byArr)
    {
        ArrayList<FoodData> arr =new ArrayList<>();
        byte[] body=new byte[BODY_LEN];

        for(int i=HEADER_LEN;i<LEN;i++)
        {
            body[i]=byArr[i];
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(body);
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


}
