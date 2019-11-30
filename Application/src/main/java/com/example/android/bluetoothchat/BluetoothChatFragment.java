/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.bluetoothchat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.android.Cart;
import com.example.android.ExampleBottomSheetDialog;
import com.example.android.FoodData;
import com.example.android.FoodDetails;
import com.example.android.GhostProtocal;
import com.example.android.Tom;
import com.example.android.common.logger.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    private static final int FOOD_DETAILS=4;
    private static final int FOOD_CART=5;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    int stat=GhostProtocal.NO_ORDER;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    //private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;


    private ArrayList<FoodData> arr=new ArrayList<>();
    private ArrayList<FoodData> cart=new ArrayList<>();
    private GhostProtocal protocalObj;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        protocalObj=new GhostProtocal();

        // If the adapter is null, then Bluetooth is not supported
        FragmentActivity activity = getActivity();
        if (mBluetoothAdapter == null && activity != null) {
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBluetoothAdapter == null) {
            return;
        }
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = view.findViewById(R.id.in);
        //mOutEditText = view.findViewById(R.id.edit_text_out);
        mSendButton = view.findViewById(R.id.button_send);
    }


    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return arr.size();
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
            view=getLayoutInflater().inflate(R.layout.device_card,null);

            TextView name=view.findViewById(R.id.food_name);
            TextView price=view.findViewById(R.id.price);
            TextView des=view.findViewById(R.id.food_description);

            name.setText(arr.get(i).foodName);
            price.setText("$"+String.valueOf(arr.get(i).price));
            des.setText(arr.get(i).cat);

            return view;

        }
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
//        mConversationArrayAdapter = new ArrayAdapter<>(activity, R.layout.message);
//
//        mConversationView.setAdapter(mConversationArrayAdapter);


//        arr.add(new FoodData("Burger","Fast Food",5));
//        arr.add(new FoodData("Fries","Fast Food",10));
//        arr.add(new FoodData("Burger","Fast Food",15));
//        arr.add(new FoodData("Fries","Fast Food",11));
        MyAdapter arrad=new MyAdapter();
        mConversationView.setAdapter(arrad);



        // Initialize the compose field with a listener for the return key
       // mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    //TextView textView = view.findViewById(R.id.edit_text_out);
                    //String message = textView.getText().toString();
                    //sendMessage(message);
                    sendByte(protocalObj.requestMenu());
                }
            }
        });



        /**
         * Listview listener

         **/

        mConversationView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                //Toast.makeText(getContext(),String.valueOf(pos),Toast.LENGTH_LONG).show();

                FoodData obj=arr.get(pos);

                String filename="file.ser";
                ObjectOutputStream out;
                byte[] data = new byte[0];

                try {

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(obj);
                    oos.flush();
                    data = bos.toByteArray();
                }
                catch (IOException ex)
                {

                }

                Intent inta=new Intent(getActivity().getApplicationContext(), FoodDetails.class);
                inta.putExtra("FoodData",data);
                inta.putExtra("Position",pos);
                //startActivity(inta);
                startActivityForResult(inta,FOOD_DETAILS);


            }
        });








        // Initialize the BluetoothChatService to perform bluetooth connections
        /**
         * this is important part
         */

        mChatService = new BluetoothChatService( mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer();
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write

            /////this is
            test t=new test(message);





            String filename="file.ser";
            ObjectOutputStream out;
            byte[] data = new byte[0];

            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);
                oos.writeObject(t);
                oos.flush();
                data = bos.toByteArray();
            }
            catch (IOException ex)
            {

            }


            byte[] send = message.getBytes();
            Toast.makeText(getActivity().getApplicationContext(),String.valueOf(data.length),Toast.LENGTH_LONG).show();

            //mChatService.write(protocalObj.sendCloseOrder());
            //mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
           // mOutEditText.setText(mOutStringBuffer);
        }
    }


    private void sendByte(byte[] data)
    {
        mChatService.write(data);
    }



    private void handleOrder(String message)
    {
        if (message=="order")
        {
            Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();
        }
        if(message=="delete")
        {
            Toast.makeText(getActivity(),"Hobfhds",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**`
     * The Handler that gets information back from the BluetoothChatService
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:

                    try {

                        byte[] readBuf = (byte[]) msg.obj;
                        int i= protocalObj.getHeaderVal(readBuf);
                        // construct a string from the valid bytes in the buffer
                        String readMessage = new String(readBuf, 0, msg.arg1);
                        //met1(String.valueOf(readMessage.length()));

                        //arr=protocalObj.stringToFoodData(readMessage);
                        switch (i)
                        {
                            case GhostProtocal.THIS_IS_MENU:
                            {
                                met1("Menu refreshed");
                                //arr=protocalObj.stringToFoodData(protocalObj.byteToString(readBuf));
                                readMessage=protocalObj.byteToString(readBuf);
                                arr=protocalObj.stringToFoodData(readMessage);
                                MyAdapter arrad=new MyAdapter();
                                mConversationView.setAdapter(arrad);
                                break;

                            }

                            case GhostProtocal.CONFORM_ORDER:
                            {
                                stat=GhostProtocal.CONFORM_ORDER;
                                goTrack();
                                sendByte(protocalObj.sendDone());

                                //Toast.makeText(getActivity().getApplicationContext(),"order",Toast.LENGTH_LONG).show();
                                break;

                            }

                            case GhostProtocal.READY_ORDER:
                            {
                                stat=GhostProtocal.READY_ORDER;
                                goTrack();
                                break;

                            }
                            case GhostProtocal.CLOSE_ORDER:
                            {
                                stat=GhostProtocal.CLOSE_ORDER;
                                goTrack();
                                break;

                            }



                        }







//                        arr.add(new FoodData(readMessage,"Fast Food",5));
//                        MyAdapter arrad=new MyAdapter();
//                        mConversationView.setAdapter(arrad);
                    }catch (Exception e)
                    {
                        met1(e.toString());
                    }

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();

                    }
                    arr.clear();
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };


    private void met1(String str)
    {
        switch (str)
        {
            case "1":
                Toast.makeText(getActivity().getApplicationContext(),"this is 1",Toast.LENGTH_LONG).show();
                break;

            case "2":
                Toast.makeText(getActivity().getApplicationContext(),"this is 2",Toast.LENGTH_LONG).show();
                break;

                default:
                    Toast.makeText(getActivity().getApplicationContext(),str,Toast.LENGTH_LONG).show();




        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    FragmentActivity activity = getActivity();
                    if (activity != null) {
                        Toast.makeText(activity, R.string.bt_not_enabled_leaving,
                                Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                }

            case FOOD_DETAILS:
                if(resultCode==Activity.RESULT_OK)
                {
                    FoodData foodObj;
                    Bundle bun=data.getExtras();
                    int qua=bun.getInt("Quantity");
                    int pos=bun.getInt("Position");

                    if(qua>0)
                    {
                        foodObj=arr.get(pos);
                        foodObj.quantity=qua;
                        cart.add(foodObj);

                    }
                    //Toast.makeText(getActivity(),String.valueOf(cart.size()),Toast.LENGTH_LONG).show();

                }
            case FOOD_CART:
                if(resultCode==Activity.RESULT_OK)
                {
                    Bundle bun=data.getExtras();
                    int message=bun.getInt("message");
                    //handleOrder(message);

                    if (message==20)
                    {
                        //Toast.makeText(getActivity(),"Order daw",Toast.LENGTH_LONG).show();
                        //String food=protocalObj.FoodDataToString(cart);
                        sendByte(protocalObj.sendOrder(cart));
                        cart.clear();

                    }
                    if (message==21)
                    {
                        cart.clear();
                        Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_LONG).show();

                    }


                }



        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        String address = extras.getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.test:{
                Intent test=new Intent(getActivity(), Tom.class);
                startActivityForResult(test,REQUEST_CONNECT_DEVICE_INSECURE);
                //Toast.makeText(getActivity().getApplicationContext(),"i got it",Toast.LENGTH_LONG).show();
                return true;
            }

            case R.id.cart:{
                if(cart.size()==0)
                {
                    Toast.makeText(getActivity(),"Empty Cart",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //this is Food data
                    //FoodData obj=arr.get(pos);

                    String filename="file.ser";
                    ObjectOutputStream out;
                    byte[] data = new byte[0];

                    try {

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(cart);
                        oos.flush();
                        data = bos.toByteArray();
                    }
                    catch (IOException ex)
                    {


                    }

                    Intent cartInta=new Intent(getActivity(), Cart.class);
                    cartInta.putExtra("CartList",data);
                    //startActivity(cartInta);
                    startActivityForResult(cartInta,FOOD_CART);
                }

                return true;
            }

            case R.id.track:
            {
                try {
                    ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog(this.stat);
                    bottomSheet.show(getFragmentManager(), "exampleBottomSheet");
                }catch (Exception e)
                {
                    Toast.makeText(getActivity().getApplicationContext(),"So sad",Toast.LENGTH_LONG).show();
                }



                return true;

            }

        }
        return false;
    }


    private void goTrack()
    {
        try {
            ExampleBottomSheetDialog bottomSheet = new ExampleBottomSheetDialog(this.stat);
            bottomSheet.show(getFragmentManager(), "exampleBottomSheet");
        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(),"So sad",Toast.LENGTH_LONG).show();
        }
    }






    public void stringToFoodData(String data)
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
                        arr.add(new FoodData(name,cat,price,qua));
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
                    arr.add(new FoodData(name,cat,price,qua));
                    //arr.add(tempObj);
                    //arr.add(new FoodData(data.substring(prev+1,i),"Fastx",100));
                    prev=i;
                }
            }



        }

    }




}
