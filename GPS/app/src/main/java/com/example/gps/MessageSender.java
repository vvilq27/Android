package com.example.gps;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;

public class MessageSender extends AsyncTask<String, Void, Void> {
    DatagramSocket s;
    DatagramPacket p;
    DataOutputStream dos;
    PrintWriter pw;
    InetAddress address;

    @Override
    protected Void doInBackground(String... voids){
        String message = voids[0];

        try{
            address = InetAddress.getByName("89.78.88.104");
            s = new DatagramSocket();
            p = new DatagramPacket(message.getBytes(), message.length(), address, 9999);

            s.send(p);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
