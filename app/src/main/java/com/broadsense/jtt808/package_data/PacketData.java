package com.broadsense.jtt808.package_data;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;

import io.netty.channel.Channel;

public abstract class PacketData {

    /**
     * 16byte 消息头
     */
    protected MsgHeader msgHeader;

    // 消息体字节数组
    @JSONField(serialize = false)
    protected byte[] msgBodyBytes;

    /**
     * 校验码 1byte
     */
    protected int checkSum;

    @JSONField(serialize = false)
    protected Channel channel;

    public MsgHeader getMsgHeader() {
        return msgHeader;
    }

    public void setMsgHeader(MsgHeader msgHeader) {
        this.msgHeader = msgHeader;
    }

    public byte[] getMsgBodyBytes() {
        return msgBodyBytes;
    }

    public void setMsgBodyBytes(byte[] msgBodyBytes) {
        this.msgBodyBytes = msgBodyBytes;
    }

    public int getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    protected int answerFlowId;

    public int getAnswerFlowId() {
        return answerFlowId;
    }

    public void setAnswerFlowId(int answerFlowId) {
        this.answerFlowId = answerFlowId;
    }

    @Override
    public String toString() {
        return "PacketData [msgHeader=" + msgHeader + ", msgBodyBytes=" + Arrays.toString(msgBodyBytes) + ", checkSum="
                + checkSum + ", address=" + channel + "]";
    }

    public int getBodyLength() {
        return packageDataBody2Byte().length;
    }

    public abstract void inflatePackageBody(byte[] data);

    public abstract byte[] packageDataBody2Byte();

    public static class MsgHeader {
        // 消息ID WORD byte[0-1]
        protected int msgId;

        /////// ========消息体属性
        // byte[2-3]
        protected int msgBodyPropsField;
        // 消息体长度 bit[0-9]
        protected int msgBodyLength;
        // 数据加密方式 bit[10-12]
        protected int encryptionType;
        // 是否分包,true==>有消息包封装项 bit[13]
        protected boolean hasSubPackage;
        // 保留位 bit[14-15]
        protected int reservedBit;
        /////// ========消息体属性

        // 终端手机号 BCD[6] byte[4-9]
        protected String terminalPhone;
        // 流水号 byte[10-11]
        protected int flowId;

        //////// =====消息包封装项
        // byte[12-15]
        protected int packageInfoField;
        // 消息包总数(word(16))
        protected long totalSubPackage;
        // 包序号(word(16))这次发送的这个消息包是分包中的第几个消息包, 从 1 开始
        protected long subPackageSeq;
        //////// =====消息包封装项

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public int getMsgBodyLength() {
            return msgBodyLength;
        }

        public void setMsgBodyLength(int msgBodyLength) {
            this.msgBodyLength = msgBodyLength;
        }

        public int getEncryptionType() {
            return encryptionType;
        }

        public void setEncryptionType(int encryptionType) {
            this.encryptionType = encryptionType;
        }

        public String getTerminalPhone() {
            return terminalPhone;
        }

        public void setTerminalPhone(String terminalPhone) {
            this.terminalPhone = terminalPhone;
        }

        public int getFlowId() {
            return flowId;
        }

        public void setFlowId(int flowId) {
            this.flowId = flowId;
        }

        public boolean isHasSubPackage() {
            return hasSubPackage;
        }

        public void setHasSubPackage(boolean hasSubPackage) {
            this.hasSubPackage = hasSubPackage;
        }

        public int getReservedBit() {
            return reservedBit;
        }

        public void setReservedBit(int reservedBit) {
            this.reservedBit = reservedBit;
        }

        public long getTotalSubPackage() {
            return totalSubPackage;
        }

        public void setTotalSubPackage(long totalPackage) {
            this.totalSubPackage = totalPackage;
        }

        public long getSubPackageSeq() {
            return subPackageSeq;
        }

        public void setSubPackageSeq(long packageSeq) {
            this.subPackageSeq = packageSeq;
        }

        public int getMsgBodyPropsField() {
            return msgBodyLength + encryptionType & 0x1c00 + encryptionType & 0x2000;
            //return msgBodyPropsField;
        }

        public void setMsgBodyPropsField(int msgBodyPropsField) {
            this.msgBodyPropsField = msgBodyPropsField;
        }

        public void setPackageInfoField(int packageInfoField) {
            this.packageInfoField = packageInfoField;
        }

        public int getPackageInfoField() {
            return packageInfoField;
        }

        @Override
        public String toString() {
            return "MsgHeader [msgId=" + Integer.toHexString(msgId) + ", msgBodyPropsField=" + msgBodyPropsField + ", msgBodyLength="
                    + msgBodyLength + ", encryptionType=" + encryptionType + ", hasSubPackage=" + hasSubPackage
                    + ", reservedBit=" + reservedBit + ", terminalPhone=" + terminalPhone + ", flowId=" + Integer.toHexString(flowId)
                    + ", packageInfoField=" + packageInfoField + ", totalSubPackage=" + totalSubPackage
                    + ", subPackageSeq=" + subPackageSeq + "]";
        }

    }

}
