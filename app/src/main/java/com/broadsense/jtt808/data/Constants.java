package com.broadsense.jtt808.data;

import java.nio.charset.Charset;

/**
 * Created by Kent_Lee on 2017/3/31.
 */

public class Constants {

    public static final String HOSTS = "119.88.237.11";
    public static final int PORT = 11001;
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    /**
     * GBK编码格式
     */
    private static final String string_encoding = "GBK";

    // 标识位
    public static final int PKG_DELIMITER = 0x7e;

    public static final Charset string_charset = Charset.forName(string_encoding);

    //平台通用应答
    public static final int SERVER_COMMOM_RSP = 0x8001;
    //终端注册应答
    public static final int SERVER_REGISTER_RSP = 0x8100;
    //位置信息查询
    public static final int SERVER_LOCATION_REQ = 0x8100;
    //临时位置跟踪控制
    public static final int SERVER_LOCATION_TMP_REQ = 0x8202;

    //终端通用应答
    public static final int TERMINAL_CONMOM_RSP = 0x0001;
    //终端心跳
    public static final int TERMINAL_HEART_BEAT = 0x0002;
    //终端注册
    public static final int TERMINAL_REGISTER = 0x0100;
    //终端注销
    public static final int TERMINAL_UNREGISTER = 0x0003;
    //终端鉴权
    public static final int TERMINAL_AUTHEN = 0x0102;
    //位置信息汇报
    public static final int TERMINAL_LOCATION_UPLOAD = 0x0200;
    //位置信息查询应答
    public static final int TERMINAL_LOCATION_RSP = 0x0201;
    //定位数据批量上传
    public static final int TERMINAL_LOCATION_BATCH_UPLOAD = 0x0704;


    public static int tcp_client_idle_minutes = 30;

    public static final int HEART_BEAT_INTERVAL = 15 * 1000;
}
