package com.broadsense.jtt808.handler;

import android.util.Log;

import com.broadsense.jtt808.package_data.PacketData;
import com.broadsense.jtt808.utils.MsgTransformer;
import com.broadsense.jtt808.utils.BitOperator;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * * Created by Kent_Lee on 2017/3/31.
 */

public class TCPServiceHandler extends ChannelInboundHandlerAdapter {
    private static final String TAG = "TCPServiceHandler";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf buf = (ByteBuf) msg;
            if (buf.readableBytes() <= 0) {
                // ReferenceCountUtil.safeRelease(msg);
                return;
            }

            byte[] bs = new byte[buf.readableBytes()];
            buf.readBytes(bs);
            Log.e(TAG, "channelRead_bs.length: " + bs.length);
            String result = "";
            BitOperator bitOperator = BitOperator.getInstance();
            for (byte b : bs) {
                result = result + Integer.toHexString(bitOperator.oneByteToInteger(b));
            }
            Log.e(TAG, "channelRead_result: " + result);
            MsgTransformer msgTransformer = new MsgTransformer();
            PacketData packetData = msgTransformer.packageByte2Data(bs);
            Log.e(TAG, "channelRead: " + packetData);
            // 字节数据转换为针对于808消息结构的实体类
            //PacketData pkg = this.decoder.bytes2PackageData(bs);
            // 引用channel,以便回送数据给硬件
            //pkg.setChannel(ctx.channel());
            //this.processPackageData(pkg);
        } finally {
            release(msg);
        }
    }

    private void release(Object msg) {
        try {
            ReferenceCountUtil.release(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        Log.e(TAG, "channelRegistered: ");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        Log.e(TAG, "channelUnregistered: ");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.e(TAG, "channelActive: ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.e(TAG, "channelInactive: ");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        Log.e(TAG, "channelReadComplete: ");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        Log.e(TAG, "userEventTriggered: ");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
        Log.e(TAG, "channelWritabilityChanged: ");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Log.e(TAG, "exceptionCaught: ", cause);
    }
}
