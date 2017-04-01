package com.broadsense.jtt808.connection;

import android.util.Log;

import com.broadsense.jtt808.handler.MessageDispatchHandler;
import com.broadsense.jtt808.handler.TCPServiceHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

/**
 * * Created by Kent_Lee on 2017/3/31.
 */

public class NettyConnectionManager {
    private static final String TAG = NettyConnectionManager.class.getSimpleName();

    private NettyConnectionManager() {
        //no instance
    }

    public Channel getChannel() {
        return mChannel;
    }

    private static final class HOLDER {
        private static final NettyConnectionManager INSTANCE = new NettyConnectionManager();
    }

    public static NettyConnectionManager getInstance() {
        return HOLDER.INSTANCE;
    }

    public void connect() throws InterruptedException {
        String host = "119.88.237.11";
        int port = 11001;
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(
                                new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[]{0x7e}),
                                        Unpooled.copiedBuffer(new byte[]{0x7e, 0x7e})));
                        ch.pipeline().addLast(new TCPServiceHandler());
                    }
                });
        bootstrap.connect(host, port).sync().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                connected(future);
            }
        });

    }

    private void connected(ChannelFuture future) {
        mChannel = future.channel();
        MessageDispatchHandler.getInstance().startHeartBeat();
        Log.e(TAG, "isActive: " + mChannel.isActive() + ", isOpen: " + mChannel.isOpen() + ", isRegistered: " + mChannel.isRegistered() + ", isWritable: " + mChannel.isWritable());
    }

    private Channel mChannel;

    public void disconnect() {
        if (mChannel != null) {
            mChannel.disconnect();
            MessageDispatchHandler.getInstance().stopHeartBeat();
        }
    }

}

