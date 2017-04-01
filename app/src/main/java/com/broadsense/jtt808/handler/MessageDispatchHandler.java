package com.broadsense.jtt808.handler;

import android.os.Handler;
import android.util.Log;

import com.broadsense.jtt808.base.BaseApplication;
import com.broadsense.jtt808.connection.NettyConnectionManager;
import com.broadsense.jtt808.data.Constants;
import com.broadsense.jtt808.package_data.EmptyPacketData;
import com.broadsense.jtt808.package_data.PacketData;
import com.broadsense.jtt808.utils.MsgTransformer;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * Created by Kent_Lee on 2017/3/31.
 */

public class MessageDispatchHandler {
    private static final String TAG = "MessageDispatchHandler";

    private int flowId;

    private MessageDispatchHandler() {
        mHandler = new Handler(BaseApplication.mContext.getMainLooper());
    }

    private static class HOLDER {
        private static final MessageDispatchHandler INSTANCE = new MessageDispatchHandler();
    }

    public static MessageDispatchHandler getInstance() {
        return HOLDER.INSTANCE;
    }

    private Handler mHandler;

    public void startHeartBeat(){
        mHandler.removeCallbacks(sendHeartBeatMsg);
        mHandler.postDelayed(sendHeartBeatMsg, Constants.HEART_BEAT_INTERVAL);
    }
    public void stopHeartBeat(){
        mHandler.removeCallbacks(sendHeartBeatMsg);
    }
    public void send2Service(Channel channel, PacketData data) {
        flowId++;
        send2Service(channel, data, flowId);
    }

    public void resend2Service(Channel channel, PacketData data) {
        send2Service(channel, data, data.getMsgHeader().getFlowId());
    }

    private void sendHeartBeatDelay() {
        mHandler.removeCallbacks(sendHeartBeatMsg);
        mHandler.postDelayed(sendHeartBeatMsg, Constants.HEART_BEAT_INTERVAL);
    }

    private Runnable sendHeartBeatMsg = new Runnable() {
        @Override
        public void run() {
            Channel channel = NettyConnectionManager.getInstance().getChannel();
            if (channel.isActive()) {
                EmptyPacketData data = new EmptyPacketData();
                PacketData.MsgHeader msgHeader = new PacketData.MsgHeader();
                msgHeader.setMsgId(Constants.TERMINAL_HEART_BEAT);
                msgHeader.setMsgBodyLength(data.getBodyLength());
                msgHeader.setEncryptionType(0);
                msgHeader.setHasSubPackage(false);
                msgHeader.setReservedBit(0);
                msgHeader.setTerminalPhone("861234567890");
                data.setMsgHeader(msgHeader);
                send2Service(channel, data);
            }
        }
    };

    private void send2Service(Channel channel, PacketData data, int flowId) {
        MsgTransformer transFormer;
        data.getMsgHeader().setFlowId(flowId);
        transFormer = new MsgTransformer();
        byte[] bAry = transFormer.packageDataToByte(data);
        try {
            ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bAry)).sync();
            if (!future.isSuccess()) {
                Log.e(TAG, "发送数据出错: ", future.cause());
            } else {
                sendHeartBeatDelay();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "发送数据出错: ", e);
        }
    }

    public void send2Service(Channel channel, byte[] bAry) {
        try {
            ChannelFuture future = channel.writeAndFlush(Unpooled.copiedBuffer(bAry)).sync();
            if (!future.isSuccess()) {
                Log.e(TAG, "发送数据出错: ", future.cause());
            } else {
                Log.e(TAG, "发送数据成功: ", future.cause());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "发送数据出错: ", e);
        }
    }
}
