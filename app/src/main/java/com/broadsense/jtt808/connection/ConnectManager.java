package com.broadsense.jtt808.connection;

import android.util.Log;

import com.broadsense.jtt808.data.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * * Created by Kent_Lee on 2017/3/31.
 */

public class ConnectManager {
    private static final String TAG = "ConnectManager";
    public Socket mSocket;
    private BufferedInputStream bis;
    private BufferedOutputStream bos;

    private ConnectManager() {
        //no instance
//        try {
//            mSocket = new Socket(Constants.HOSTS, Constants.PORT);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        buffer = new byte[1024 * 2];
        bufTmp = new byte[1024];
    }

    private static final class HOLDER {
        private static final ConnectManager INSTANCE = new ConnectManager();
    }

    public static ConnectManager getInstance() {
        return HOLDER.INSTANCE;
    }

    public void connect() {
        new Thread(){
            @Override
            public void run() {
                try {
                    mSocket = new Socket();
                    mSocket.setSoTimeout(Constants.CONNECT_TIMEOUT);
                    mSocket.connect(new InetSocketAddress(Constants.HOSTS, Constants.PORT));
                    InputStream inputStream = mSocket.getInputStream();
                    OutputStream outputStream = mSocket.getOutputStream();
                    bis = new BufferedInputStream(inputStream);
                    bos = new BufferedOutputStream(outputStream);
                    new SocketThread().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void disconnect() {
        try {
            bis.close();
            bos.close();
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return mSocket.isConnected();
    }

    private byte[] bufTmp;
    private byte[] buffer;

    private class SocketThread extends Thread {
        @Override
        public void run() {
            while (mSocket.isConnected()) {
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                byte[] buffer = new byte[1024];
                try {
                    int length = bis.read(buffer);
                    if (length > 0) {
                        Log.e(TAG, "run: " + new String(buffer));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(byte[] arr) {
        try {
            bos.write(arr);
            bos.flush();
            Log.e(TAG, "send: " );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

