package com.broadsense.jtt808.package_data;

import android.util.Log;

import com.broadsense.jtt808.utils.BitOperator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


/**
 * 终端注册消息
 *
 * @author hylexus
 */
public class TerminalRegisterMsg extends PacketData {
    private static final String TAG = "TerminalRegisterMsg";

    private TerminalRegInfo terminalRegInfo;


    public TerminalRegisterMsg() {

    }

    public void setTerminalRegInfo(TerminalRegInfo msgBody) {
        this.terminalRegInfo = msgBody;
    }

    public TerminalRegInfo getTerminalRegInfo() {
        return terminalRegInfo;
    }

    @Override
    public String toString() {
        return "TerminalRegisterMsg [terminalRegInfo=" + terminalRegInfo + ", msgHeader=" + msgHeader
                + ", msgBodyBytes=" + Arrays.toString(msgBodyBytes) + ", checkSum=" + checkSum + ", channel=" + channel
                + "]";
    }

    @Override
    public int getBodyLength() {
        byte[] bytes = packageDataBody2Byte();
        Log.e(TAG, "getBodyLength: " + bytes.length);
        return bytes.length;
    }

    @Override
    public void inflatePackageBody(byte[] data) {

    }

    public void byte2PackageDataBody(byte[] packageBytes) {

    }

    public byte[] packageDataBody2Byte() {
        BitOperator bitOperator = BitOperator.getInstance();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //拼接消息体
            // 1. 省域ID word(16)
            baos.write(bitOperator.integerTo2Bytes(terminalRegInfo.getProvinceId()));
            //市县域 ID
            baos.write(bitOperator.integerTo2Bytes(terminalRegInfo.getCityId()));
            //制造商 ID
            baos.write(terminalRegInfo.getManufacturerId().getBytes());
            //终端型号
            baos.write(terminalRegInfo.getTerminalType().getBytes());
            //终端 ID
            baos.write(terminalRegInfo.getTerminalId().getBytes());
            //车牌颜色
            baos.write(bitOperator.integerTo1Byte(terminalRegInfo.getLicensePlateColor()));
            //车牌标识
            baos.write(terminalRegInfo.getLicensePlate().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    public static class TerminalRegInfo {
        // 省域ID(WORD),设备安装车辆所在的省域，省域ID采用GB/T2260中规定的行政区划代码6位中前两位
        // 0保留，由平台取默认值
        private int provinceId;
        // 市县域ID(WORD) 设备安装车辆所在的市域或县域,市县域ID采用GB/T2260中规定的行 政区划代码6位中后四位
        // 0保留，由平台取默认值
        private int cityId;
        // 制造商ID(BYTE[5]) 5 个字节，终端制造商编码
        private String manufacturerId;
        // 终端型号(BYTE[8]) 八个字节， 此终端型号 由制造商自行定义 位数不足八位的，补空格。
        private String terminalType;
        // 终端ID(BYTE[7]) 七个字节， 由大写字母 和数字组成， 此终端 ID由制造 商自行定义
        private String terminalId;
        /**
         * 车牌颜色(BYTE) 车牌颜色，按照 JT/T415-2006 的 5.4.12 未上牌时，取值为0<br>
         * 0===未上车牌<br>
         * 1===蓝色<br>
         * 2===黄色<br>
         * 3===黑色<br>
         * 4===白色<br>
         * 9===其他
         */
        private int licensePlateColor;
        // 车牌(STRING) 公安交 通管理部门颁 发的机动车号牌
        private String licensePlate;

        public TerminalRegInfo() {
        }

        public int getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }

        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getManufacturerId() {
            return manufacturerId;
        }

        public void setManufacturerId(String manufacturerId) {
            this.manufacturerId = manufacturerId;
        }

        public String getTerminalType() {
            return terminalType;
        }

        public void setTerminalType(String terminalType) {
            this.terminalType = terminalType;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }

        public int getLicensePlateColor() {
            return licensePlateColor;
        }

        public void setLicensePlateColor(int licensePlate) {
            this.licensePlateColor = licensePlate;
        }

        public String getLicensePlate() {
            return licensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.licensePlate = licensePlate;
        }

        @Override
        public String toString() {
            return "TerminalRegInfo [provinceId=" + provinceId + ", cityId=" + cityId + ", manufacturerId="
                    + manufacturerId + ", terminalType=" + terminalType + ", terminalId=" + terminalId
                    + ", licensePlateColor=" + licensePlateColor + ", licensePlate=" + licensePlate + "]";
        }

    }
}
