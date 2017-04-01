package com.broadsense.jtt808.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.broadsense.jtt808.R;
import com.broadsense.jtt808.connection.NettyConnectionManager;
import com.broadsense.jtt808.data.Constants;
import com.broadsense.jtt808.handler.MessageDispatchHandler;
import com.broadsense.jtt808.package_data.PacketData;
import com.broadsense.jtt808.package_data.TerminalRegisterMsg;

import io.netty.channel.Channel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Channel mChannel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_connect).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_isAlive).setOnClickListener(this);
        findViewById(R.id.btn_heartBeat).setOnClickListener(this);

        try {
            NettyConnectionManager.getInstance().connect();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        ConnectManager.getInstance().connect();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_connect:
                try {
                    NettyConnectionManager.getInstance().connect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_register:
                try {
//                    String result = "7e0100002c0200000000150025002c0133373039363054372d54383038000000000000000000000000003033323931373001d4c142383838387b7e";
//                    ConnectManager.getInstance().send(result.getBytes());
                    mChannel = NettyConnectionManager.getInstance().getChannel();
//                    MessageDispatchHandler.getInstance().send2Service(mChannel, result.getBytes());
                    MessageDispatchHandler.getInstance().send2Service(mChannel, fakeRegisterPackageData());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_isAlive:
                mChannel = NettyConnectionManager.getInstance().getChannel();
                Log.e(TAG, "onClick_isActive: " + mChannel.isActive() + ", isOpen: " + mChannel.isOpen() + ", isRegistered: " + mChannel.isRegistered() + ", isWritable: " + mChannel.isWritable());
//                Log.e(TAG, "onClick_isConnected: "+ConnectManager.getInstance().mSocket.isConnected() );
                break;
            case R.id.btn_heartBeat:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NettyConnectionManager.getInstance().disconnect();
    }

    private PacketData fakeRegisterPackageData() {
        TerminalRegisterMsg msg = new TerminalRegisterMsg();
        //body
        TerminalRegisterMsg.TerminalRegInfo terminalRegInfo = new TerminalRegisterMsg.TerminalRegInfo();
        terminalRegInfo.setProvinceId(0x00);
        terminalRegInfo.setCityId(0x00);
        terminalRegInfo.setManufacturerId("12345");
        terminalRegInfo.setTerminalType("12345678901234567890");
        terminalRegInfo.setTerminalId("ABCD123");
        terminalRegInfo.setLicensePlateColor(0x01);
        terminalRegInfo.setLicensePlate("粤A:66666");
        msg.setTerminalRegInfo(terminalRegInfo);
        Log.e(TAG, "getBodyLength: "+msg.getBodyLength() );
        //header
        PacketData.MsgHeader header = new PacketData.MsgHeader();
        header.setMsgId(Constants.TERMINAL_REGISTER);
        header.setMsgBodyLength(msg.getBodyLength());
        header.setEncryptionType(0);
        header.setHasSubPackage(false);
        header.setReservedBit(0);
        //设置终端号
        header.setTerminalPhone("861234567890");
        //流水号在发送时添加
        //消息包封装项-无

        msg.setMsgHeader(header);

        return msg;
    }
}
