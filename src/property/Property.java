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
    public static final int TransFrequency_MaxFrequency = 500;
    public static final int TransFrequency_MinFrequency = 1;
    /**
     * default settings
     */
    private final String defaultFilePath = "";
    private final byte defaultTransMode = TransMode_TimeStamp;
    private final byte defaultTimeStampPosition = sourcehandler.SensorFileInputStream.TimeStampPosition_End;
    private final int defaultTransFrequency = 100;
    private final String defaultSensorType = SensorType_WiTiltSensor;
    private final byte defaultOutPutByteOrder = sourcehandler.SensorFileInputStream.ByteOrder_HighLow;
    private final int defaultDataUnitFormat = sourcehandler.SensorFileInputStream.DataFormat_Short;
    private final int defaultBufferSize = 50;
    private final int defaultChannelNumber = 3;
    private final int defaultMaxSimultaneouslyPacketNo = 6;
    private final int defaultIntervalUnit = 1;
    private final double defaultFrePrecision = 0.12;
    /**
     * General Properties
     */
    private String filePath;  //location of source file
    private byte transMode; //transmission based on time stamp on source file or based on user specified frequency, default: time stamp
    private byte timeStampPosition; // indicate the position of time stamp in source file
    private int transFrequency; // transmit frequency, default 100 Hz
    private String sensorType;
    /**
     * Advanced properties
     */
    private byte outputByteOrder; // low-high or high-low, default: High_Low
    private int dataUnitFormat; //how many bytes to represent one data value, short(2), int(4) or long(8), default: short.
    private int bufferSize; // the size of transmit buffer, default: 50
    private int channelNumber; //number of active channels, default: 3
    private int maxSimultaneouslyPacketNo; // maximum number of packet in one time frame, default 6
    private int IntervalUnit; // the minimum interval between two transmission in milliseconds (the minimum time system can sleep)
    private double frePrecision;// frequency precision
    /**
     * frequencyPrecision, from class: Frequency
     */
    ////////////////////////////////hidden parameters
    private Frequency frequencyObj = null;

    /**
     * 
     * @param path, the location of source file
     * @param timestampposition, the position of time stamp in source file
     * @param sensortype, the type of sensor
     */
    public Property(String path, byte mode, byte timestampposition, int fre, String sensortype) throws SimulatorException {

        filePath = path;
        sensorType = sensortype;
        timeStampPosition = timestampposition;
        transMode = mode;
        if (transMode == TransMode_Frequency) {
            transFrequency = fre;
            if (transFrequency < TransFrequency_MinFrequency) {
                transFrequency = TransFrequency_MinFrequency;
                System.out.println("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
            }
            if (transFrequency > TransFrequency_MaxFrequency) {
                transFrequency = TransFrequency_MaxFrequency;
                System.out.println("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
            }
            IntervalUnit = defaultIntervalUnit;
            frePrecision = defaultFrePrecision;
            frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
            System.out.println("Transmit Mode: Frequency");
            System.out.println("Desired Frequency: " + fre);
            System.out.println("Real Frequency: " + getRealFrequency());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
        } else if (transMode == TransMode_TimeStamp) {
            System.out.println("Transmit Mode: Time Stamp");
        }
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = defaultOutPutByteOrder;
            dataUnitFormat = defaultDataUnitFormat;
            bufferSize = defaultBufferSize;
            channelNumber = defaultChannelNumber;
            maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        }
    }

    /**
     * 
     * @param path the location of source file
     */
    public Property(String path) throws SimulatorException {
        filePath = path;
        sensorType = defaultSensorType;
        timeStampPosition = defaultTimeStampPosition;
        transMode = defaultTransMode;
        if (transMode == TransMode_Frequency) {
            transFrequency = defaultTransFrequency;
            if (transFrequency < TransFrequency_MinFrequency) {
                transFrequency = TransFrequency_MinFrequency;
            }
            if (transFrequency > TransFrequency_MaxFrequency) {
                transFrequency = TransFrequency_MaxFrequency;
            }
            IntervalUnit = defaultIntervalUnit;
            frePrecision = defaultFrePrecision;
            frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
            System.out.println("Transmit Mode: Frequency");
            System.out.println("Desired Frequency: " + defaultTransFrequency);
            System.out.println("Real Frequency: " + getRealFrequency());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
        } else if (transMode == TransMode_TimeStamp) {
            System.out.println("Transmit Mode: Time Stamp");
        }
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = defaultOutPutByteOrder;
            dataUnitFormat = defaultDataUnitFormat;
            bufferSize = defaultBufferSize;
            channelNumber = defaultChannelNumber;
            maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        }
    }

    public Property() throws SimulatorException {

        filePath = defaultFilePath;
        sensorType = defaultSensorType;
        timeStampPosition = defaultTimeStampPosition;
        transMode = defaultTransMode;
        if (transMode == TransMode_Frequency) {

            transFrequency = defaultTransFrequency;
            if (transFrequency < TransFrequency_MinFrequency) {
                transFrequency = TransFrequency_MinFrequency;
            }
            if (transFrequency > TransFrequency_MaxFrequency) {
                transFrequency = TransFrequency_MaxFrequency;
            }
            IntervalUnit = defaultIntervalUnit;
            frePrecision = defaultFrePrecision;
            frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
            System.out.println("Transmit Mode: Frequency");
            System.out.println("Desired Frequency: " + defaultTransFrequency);
            System.out.println("Real Frequency: " + getRealFrequency());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
        } else if (transMode == TransMode_TimeStamp) {
            System.out.println("Transmit Mode: Time Stamp");
        }
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = defaultOutPutByteOrder;
            dataUnitFormat = defaultDataUnitFormat;
            bufferSize = defaultBufferSize;
            channelNumber = defaultChannelNumber;
            maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        }
    }

    public void setAdvanceProperties(byte outputbyteorder, int dataunitformat, int buffersize, int channelnumber, int maxsimpacno, int minSleepUnit, int frePrecision) throws SimulatorException {

        if (transMode == TransMode_Frequency) {
            if (minSleepUnit != IntervalUnit || frePrecision != this.frePrecision) {
                IntervalUnit = minSleepUnit;
                this.frePrecision = frePrecision;
                frequencyObj = new Frequency(transFrequency, IntervalUnit, this.frePrecision);
            }
            System.out.println("Real Frequency: " + getRealFrequency());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
        } else if (transMode == TransMode_TimeStamp) {
            System.out.println("Transmit Mode: Time Stamp");
        }
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
        if (transMode == TransMode_Frequency) {
            transFrequency = fre;
            if (transFrequency < TransFrequency_MinFrequency) {
                transFrequency = TransFrequency_MinFrequency;
                System.out.println("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
            }
            if (transFrequency > TransFrequency_MaxFrequency) {
                transFrequency = TransFrequency_MaxFrequency;
                System.out.println("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
            }
            IntervalUnit = defaultIntervalUnit;
            frePrecision = defaultFrePrecision;
            frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
            System.out.println("Transmit Mode: Frequency");
            System.out.println("Desired Frequency: " + fre);
            System.out.println("Real Frequency: " + getRealFrequency());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
        } else if (transMode == TransMode_TimeStamp) {
            System.out.println("Transmit Mode: Time Stamp");
        }

        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = defaultOutPutByteOrder;
            dataUnitFormat = defaultDataUnitFormat;
            bufferSize = defaultBufferSize;
            channelNumber = defaultChannelNumber;
            maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        }
    }

    /**
     * reset properties to default
     */
    public void resetProperties() throws SimulatorException {
        filePath = defaultFilePath;
        sensorType = defaultSensorType;
        timeStampPosition = defaultTimeStampPosition;
        transMode = defaultTransMode;
        transFrequency = defaultTransFrequency;
        IntervalUnit = defaultIntervalUnit;
        frePrecision = defaultFrePrecision;
        frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = defaultOutPutByteOrder;
            dataUnitFormat = defaultDataUnitFormat;
            bufferSize = defaultBufferSize;
            channelNumber = defaultChannelNumber;
            maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        }
    }

    public int getFrequency() {
        return transFrequency;
    }

    public void setFerquency(int fre) throws SimulatorException {
        transFrequency = fre;
        if (transFrequency < TransFrequency_MinFrequency) {
            transFrequency = TransFrequency_MinFrequency;
            System.out.println("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
        }
        if (transFrequency > TransFrequency_MaxFrequency) {
            transFrequency = TransFrequency_MaxFrequency;
            System.out.println("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
        }
        if (transMode == TransMode_Frequency) {
            frequencyObj.calculateFrequency(transFrequency);
            System.out.println("Transmit Mode: Frequency");
            System.out.println("Desired Frequency: " + fre);
            System.out.println("Real Frequency: " + getRealFrequency());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
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

    public int getPacketsPerTrans() {
        return frequencyObj.getPacketsPerTrans();
    }

    public int getSleepInterval() {
        return frequencyObj.getSleepInterval();
    }

    public double getRealFrequency() {
        return frequencyObj.getRealFrequency();
    }

    public int getTransFrequency() {
        return transFrequency;
    }

    public double getFrePrecision() {
        return frePrecision;
    }

    public int getIntervalUnit() {
        return IntervalUnit;
    }

//////////////////////////////////Get default settings////////////////////
    public int getDefaultBufferSize() {
        return defaultBufferSize;
    }

    public int getDefaultChannelNumber() {
        return defaultChannelNumber;
    }

    public int getDefaultDataUnitFormat() {
        return defaultDataUnitFormat;
    }

    public String getDefaultFilePath() {
        return defaultFilePath;
    }

    public double getDefaultFrePrecision() {
        return defaultFrePrecision;
    }

    public int getDefaultIntervalUnit() {
        return defaultIntervalUnit;
    }

    public int getDefaultMaxSimultaneouslyPacketNo() {
        return defaultMaxSimultaneouslyPacketNo;
    }

    public byte getDefaultOutPutByteOrder() {
        return defaultOutPutByteOrder;
    }

    public String getDefaultSensorType() {
        return defaultSensorType;
    }

    public byte getDefaultTimeStampPosition() {
        return defaultTimeStampPosition;
    }

    public int getDefaultTransFrequency() {
        return defaultTransFrequency;
    }

    public byte getDefaultTransMode() {
        return defaultTransMode;
    }
}
