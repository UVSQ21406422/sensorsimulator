/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class Property {

    public static final String SensorType_WiTiltSensor = "WiTilt V 3.0";
    public static final byte TransMode_TimeStamp = (byte) 1;
    public static final byte TransMode_Frequency = (byte) 2;
    public static final int TransFrequency_MaxFrequency = 250;
    public static final int TransFrequency_DefaultFrequency = 100;
    public static final int TransFrequency_MinFrequency = 1;
    public static final int IntervalUnit = 10; // the minimum interval between two transmission in milliseconds (the minimum time system can sleep)
    /**
     * General Properties
     */
    private String sensorType;
    private String filePath;  //location of source file
    private byte timeStampPosition; // indicate the position of time stamp in source file
    private byte transMode; //transmission based on time stamp on source file or based on user specified frequency, default: time stamp
    private int transFrequency; // transmit frequency, default 100 Hz
    /**
     * Advanced properties
     */
    private byte outputByteOrder; // low-high or high-low, default: High_Low
    private int dataUnitFormat; //how many bytes to represent one data value, short(2), int(4) or long(8), default: short.
    private int bufferSize; // the size of transmit buffer, default: 50
    private int channelNumber; //number of active channels, default: 3
    private int maxSimultaneouslyPacketNo; // maximum number of packet in one time frame, default 6
    /**
     * frequencyPrecision, from class: Frequency
     */
    ////////////////////////////////hidden parameters
    private Frequency frequencyObj;

    /**
     * 
     * @param path, the location of source file
     * @param timestampposition, the position of time stamp in source file
     * @param sensortype, the type of sensor
     */
    public Property(String path, byte mode, byte timestampposition, int fre, String sensortype) throws SimulatorException {
        frequencyObj = new Frequency();
        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        transFrequency = fre;
        if (transFrequency < TransFrequency_MinFrequency) {
            transFrequency = TransFrequency_MinFrequency;
        }
        if (transFrequency > TransFrequency_MaxFrequency) {
            transFrequency = TransFrequency_MaxFrequency;
        }
        frequencyObj.calculateFrequency(transFrequency);
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
    public Property(String path) throws SimulatorException {
        frequencyObj = new Frequency();
        filePath = path;
        sensorType = SensorType_WiTiltSensor;
        timeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
        transMode = TransMode_TimeStamp;
        transFrequency = TransFrequency_DefaultFrequency;
        frequencyObj.calculateFrequency(transFrequency);
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    public Property() throws SimulatorException {
        frequencyObj = new Frequency();
        filePath = "";
        sensorType = SensorType_WiTiltSensor;
        timeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
        transMode = TransMode_TimeStamp;
        transFrequency = TransFrequency_DefaultFrequency;
        frequencyObj.calculateFrequency(transFrequency);
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    public void setAllProperties(String path, String sensortype, byte mode, byte timestampposition, int fre, byte outputbyteorder, int dataunitformat, int buffersize, int channelnumber, int maxsimpacno) throws SimulatorException {
        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        transFrequency = fre;
        if (transFrequency < TransFrequency_MinFrequency) {
            transFrequency = TransFrequency_MinFrequency;
        }
        if (transFrequency > TransFrequency_MaxFrequency) {
            transFrequency = TransFrequency_MaxFrequency;
        }
        frequencyObj.calculateFrequency(transFrequency);
        outputByteOrder = outputbyteorder;
        dataUnitFormat = dataunitformat;
        bufferSize = buffersize;
        channelNumber = channelnumber;
        maxSimultaneouslyPacketNo = maxsimpacno;
    }

    public void setGeneralProperties(String path, byte mode, byte timestampposition, int fre, String sensortype) throws SimulatorException {
        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        transFrequency = fre;
        if (transFrequency < TransFrequency_MinFrequency) {
            transFrequency = TransFrequency_MinFrequency;
        }
        if (transFrequency > TransFrequency_MaxFrequency) {
            transFrequency = TransFrequency_MaxFrequency;
        }
        frequencyObj.calculateFrequency(transFrequency);
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
    public void resetProperties() throws SimulatorException {
        filePath = "";
        sensorType = SensorType_WiTiltSensor;
        timeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
        transMode = TransMode_TimeStamp;
        transFrequency = TransFrequency_DefaultFrequency;
        frequencyObj.calculateFrequency(transFrequency);
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
            dataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
            bufferSize = 50;
            channelNumber = 3;
            maxSimultaneouslyPacketNo = 6;
        }
    }

    public int getFrequency() {
        return transFrequency;
    }

    public void setFerquency(int fre) {
        transFrequency = fre;
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

    public int getPacketsPerTrans() {
        return frequencyObj.getPacketsPerTrans();
    }

    public int getSleepInterval() {
        return frequencyObj.getSleepInterval();
    }

    public double getRealFrequency() {
        return frequencyObj.getRealFrequency();
    }

    public void setFrequencyPrecision(double pre) throws SimulatorException {
        frequencyObj.setFrePrecision(pre);
        frequencyObj.calculateFrequency(transFrequency);
    }

    public double getFrequencyPrecision() {
        return frequencyObj.getFrePrecision();
    }
}
