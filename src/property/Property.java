/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

/**
 *
 * @author CZC
 */
public class Property {

    public static final String SensorType_WiTiltSensor = "WiTilt V 3.0";
    public static final byte TransMode_TimeStamp = (byte) 1;
    public static final byte TransMode_Frequency = (byte) 2;
    /**
     * General Properties
     */
    private String sensorType;
    private String filePath;  //location of source file
    private byte timeStampPosition; // indicate the position of time stamp in source file
    /**
     * Advanced properties
     */
    private byte outputByteOrder; // low-high or high-low, default: High_Low
    private byte transMode; //transmission based on time stamp on source file or based on user specified frequency, default: time stamp
    private int dataUnitFormat; //how many bytes to represent one data value, short(2), int(4) or long(8), default: short.
    private int bufferSize; // the size of transmit buffer, default: 50
    private int channelNumber; //number of active channels, default: 3
    private int maxSimultaneouslyPacketNo; // maximum number of packet in one time frame, default 6

    /**
     * 
     * @param path, the location of source file
     * @param timestampposition, the position of time stamp in source file
     * @param sensortype, the type of sensor
     */
    public Property(String path, byte timestampposition, byte mode, String sensortype) {
        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    /**
     * 
     * @param path the location of source file
     */
    public Property(String path) {
        filePath = path;
        sensorType = SensorType_WiTiltSensor;
        timeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
        transMode = TransMode_TimeStamp;
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    public Property() {
        filePath = "";
        sensorType = SensorType_WiTiltSensor;
        timeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
        transMode = TransMode_TimeStamp;
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    public void setAllProperties(String path, String sensortype, byte timestampposition, byte outputbyteorder, byte mode, int dataunitformat, int buffersize, int channelnumber, int maxsimpacno) {
        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        outputByteOrder = outputbyteorder;
        dataUnitFormat = dataunitformat;
        bufferSize = buffersize;
        channelNumber = channelnumber;
        maxSimultaneouslyPacketNo = maxsimpacno;
    }

    public void setGeneralProperties(String path, byte timestampposition, byte mode, String sensortype) {
        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    /**
     * reset properties to default
     */
    public void resetProperties() {
        filePath = "";
        sensorType = SensorType_WiTiltSensor;
        timeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
        transMode = TransMode_TimeStamp;
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    public byte getTransMode() {
        return transMode;
    }

    public String getSensorType() {
        return sensorType;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public int getMaxSimultaneouslyPacketNo() {
        return maxSimultaneouslyPacketNo;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte getTimeStampPosition() {
        return timeStampPosition;
    }

    public byte getOutputByteOrder() {
        return outputByteOrder;
    }

    public int getDataUnitFormat() {
        return dataUnitFormat;
    }
}
