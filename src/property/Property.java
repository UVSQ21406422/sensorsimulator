/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package property;

import controller.StateListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class Property {

    public final String PropertyFile = "property";
    public static final String SensorType_WiTiltSensor = "WiTilt V 3.0";
    public static final byte TransMode_TimeStamp = (byte) 1;
    public static final byte TransMode_Frequency = (byte) 2;
    public static final int TransFrequency_MaxFrequency = 500;
    public static final int TransFrequency_MinFrequency = 1;
    public static final byte TimeStampPosition_Begin = (byte) 1;
    public static final byte TimeStampPosition_End = (byte) 2;
    public static final byte TimeStampPosition_None = (byte) 3;
    public static final byte ByteOrder_HighLow = (byte) 1;
    public static final byte ByteOrder_LowHigh = (byte) 2;
    public static final int DataFormat_Short = 2;
    public static final int DataFormat_Integer = 4;
    public static final int DataFormat_Long = 8;
    public static final byte HeaderContent_TimeStamp = (byte) 1;
    public static final byte HeaderContent_None = (byte) 0;
    //////////////////////////////////////////////
    private final byte FreObjReConstruct = (byte) 1;
    private final byte FreReCalculate = (byte) 2;
    private final byte FreObjNoAction = (byte) 3;
    /**
     * default settings
     */
    private final String defaultFilePath = "";
    private final byte defaultTransMode = TransMode_TimeStamp;
    private final byte defaultTimeStampPosition = TimeStampPosition_End;
    private final int defaultTransFrequency = 100;
    private final String defaultSensorType = SensorType_WiTiltSensor;
    private final byte defaultOutPutByteOrder = ByteOrder_HighLow;
    private final int defaultDataUnitFormat = DataFormat_Short;
    private final int defaultBufferSize = 50;
    private final int defaultChannelNumber = 3;
    private final int defaultMaxSimultaneouslyPacketNo = 6;
    private final int defaultIntervalUnit = 1;
    private final double defaultFrePrecision = 0.12;
    private final byte defaultPacketHeaderContent = HeaderContent_None;
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
    private int channelNumber; //number of active channels, default: 3
    private double frePrecision;// frequency precision
    private byte packetHeaderContent;  //time stamp can be added to be part of a packet header or no header is required.
    /**
     * Hidden properties
     */
    private int bufferSize; // the size of transmit buffer, default: 50
    private int maxSimultaneouslyPacketNo; // maximum number of packet in one time frame, default 6
    private int IntervalUnit; // the minimum interval between two transmission in milliseconds (the minimum time system can sleep)
    ///////////////////////////////////////////////////
    private Frequency frequencyObj = null;
    private int propertyCount = 10;
    private StateListener stateListner;

    public Property(StateListener stateListner) {
        this.stateListner = stateListner;
        load();

    }

    public void loadDefaultGeneral() {
        filePath = defaultFilePath;
        sensorType = defaultSensorType;
        timeStampPosition = defaultTimeStampPosition;
        transMode = defaultTransMode;

        transFrequency = defaultTransFrequency;
        if (transFrequency < TransFrequency_MinFrequency) {
            transFrequency = TransFrequency_MinFrequency;
        }
        if (transFrequency > TransFrequency_MaxFrequency) {
            transFrequency = TransFrequency_MaxFrequency;
        }
    }

    public void loadDefaultAdvance() {
        frePrecision = defaultFrePrecision;
        outputByteOrder = defaultOutPutByteOrder;
        dataUnitFormat = defaultDataUnitFormat;
        channelNumber = defaultChannelNumber;
        packetHeaderContent = defaultPacketHeaderContent;
    }

    public void loadDefaultHidden() {
        maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        IntervalUnit = defaultIntervalUnit;
        bufferSize = defaultBufferSize;
    }

    public void load() {
        String dataLine = "";
        try {
            File f = new File(PropertyFile);
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        new FileInputStream(f)));
                for (int i = 0; i < propertyCount; i++) {
                    dataLine = br.readLine();
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("FilePath")) {
                        filePath = dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length());
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("TransmitMode")) {
                        transMode = Byte.parseByte(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("TimeStampPosition")) {
                        timeStampPosition = Byte.parseByte(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("TransmitFrequency")) {
                        transFrequency = Integer.parseInt(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("SensorType")) {
                        sensorType = dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length());
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("OutputByteOrder")) {
                        outputByteOrder = Byte.parseByte(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("DataUnitFormat")) {
                        dataUnitFormat = Integer.parseInt(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("ChannelNumber")) {
                        channelNumber = Integer.parseInt(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("FrequencyPrecision")) {
                        frePrecision = Double.parseDouble(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                    if (dataLine.substring(0, dataLine.indexOf('=')).equals("PacketHeaderContent")) {
                        packetHeaderContent = Byte.parseByte(dataLine.substring(dataLine.indexOf('=') + 1, dataLine.length()));
                    }
                }
                loadDefaultHidden();
                br.close();
            } else {
                loadDefaultGeneral();
                loadDefaultAdvance();
                loadDefaultHidden();
            }

        } catch (FileNotFoundException ex) {
            stateListner.systemInforEvent("Properties file not found. "+ ex.getMessage());
            System.out.println("Properties file not found.");
            ex.printStackTrace();
        } catch (IOException ex) {
            stateListner.systemInforEvent("Load properties error. "+ex.getMessage());
            System.out.println("Load properties error.");
            ex.printStackTrace();
        }
    }

    public void saveToFile() {
        try {
            File f = new File(PropertyFile);
            if (!f.exists()) {
                f.createNewFile();
            }
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
            String str = "FilePath=" + filePath + "\n" + "TransmitMode=" + Byte.toString(transMode) + "\nTimeStampPosition=" + Byte.toString(timeStampPosition) + "\nTransmitFrequency=" + Integer.toString(transFrequency) + "\nSensorType=" + sensorType + "\nOutputByteOrder=" + Byte.toString(outputByteOrder) + "\nDataUnitFormat=" + Integer.toString(dataUnitFormat) + "\nChannelNumber=" + Integer.toString(channelNumber) + "\nFrequencyPrecision=" + Double.toString(frePrecision) + "\nPacketHeaderContent=" + Byte.toString(packetHeaderContent);
            dos.write(str.getBytes());

            dos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void setHiddenProperties(int buffersize, int maxsimpacno, int minSleepUnit) {
        if (minSleepUnit != IntervalUnit) {
            IntervalUnit = minSleepUnit;
            // frequencyObj = new Frequency(transFrequency, IntervalUnit, this.frePrecision);
        }
        bufferSize = buffersize;
        maxSimultaneouslyPacketNo = maxsimpacno;
    }

    public void setAdvanceProperties(byte outputbyteorder, int dataunitformat, int channelnumber, double frePrecision, byte headerContent) {
        if (frePrecision != this.frePrecision) {
            this.frePrecision = frePrecision;
        }
        outputByteOrder = outputbyteorder;
        dataUnitFormat = dataunitformat;
        channelNumber = channelnumber;
        packetHeaderContent = headerContent;
        stateListner.systemInforEvent("Advance properties have been set");
        System.out.println("Advance properties have been set");
    }

    public void setGeneralProperties(String path, byte mode, byte timestampposition, int fre, String sensortype) throws SimulatorException {
        filePath = path;
        sensorType = sensortype;
        transMode = mode;
        if (transMode == TransMode_Frequency) {
            transFrequency = fre;
            if (transFrequency < TransFrequency_MinFrequency) {
                transFrequency = TransFrequency_MinFrequency;
                stateListner.systemInforEvent("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
                System.out.println("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
            }
            if (transFrequency > TransFrequency_MaxFrequency) {
                transFrequency = TransFrequency_MaxFrequency;
                stateListner.systemInforEvent("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
                System.out.println("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
            }
            //frequencyObj.calculateFrequency(transFrequency);

        }
        timeStampPosition = timestampposition;
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
        // frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
        if (sensorType.equals(SensorType_WiTiltSensor)) {
            outputByteOrder = defaultOutPutByteOrder;
            dataUnitFormat = defaultDataUnitFormat;
            bufferSize = defaultBufferSize;
            channelNumber = defaultChannelNumber;
            maxSimultaneouslyPacketNo = defaultMaxSimultaneouslyPacketNo;
        }
    }

    public void initFrequencyObj() throws SimulatorException {
        switch (isFrequencyModified()) {
            case FreObjReConstruct:
                System.out.println(transFrequency + " " + IntervalUnit + " " + frePrecision);
                frequencyObj = new Frequency(transFrequency, IntervalUnit, frePrecision);
                break;
            case FreReCalculate:
                frequencyObj.calculateFrequency(transFrequency);

                break;
            default:
                break;
        }
        if (transMode == TransMode_Frequency) {
            stateListner.systemInforEvent("Transmit Mode: Frequency");
            System.out.println("Transmit Mode: Frequency");
             stateListner.systemInforEvent("Desired Frequency: " + transFrequency);
            System.out.println("Desired Frequency: " + transFrequency);
             stateListner.systemInforEvent("Real Frequency: " + getRealFrequency());
            System.out.println("Real Frequency: " + getRealFrequency());
             stateListner.systemInforEvent("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
            System.out.println("T = " + getSleepInterval() + " N = " + getPacketsPerTrans());
        } else if (transMode == TransMode_TimeStamp) {
             stateListner.systemInforEvent("Transmit Mode: Time Stamp");
            System.out.println("Transmit Mode: Time Stamp");
        }
    }

    public int getFrequency() {
        return transFrequency;
    }

    public void setFerquency(int fre) throws SimulatorException {
        transFrequency = fre;
        if (transFrequency < TransFrequency_MinFrequency) {
            transFrequency = TransFrequency_MinFrequency;
            stateListner.systemInforEvent("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
            System.out.println("Desired frequency too low, reset to minumu available = " + TransFrequency_MinFrequency);
        }
        if (transFrequency > TransFrequency_MaxFrequency) {
            transFrequency = TransFrequency_MaxFrequency;
            stateListner.systemInforEvent("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
            System.out.println("Desired frequency too high, reset to maximum available = " + TransFrequency_MaxFrequency);
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

    public byte getPacketHeaderContent() {
        return packetHeaderContent;
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

    public byte getDefaultPacketHeaderContent() {
        return defaultPacketHeaderContent;
    }

    ///////////////////////////other methods//////////////////////////////
    /**
     * decide if frequency object has to be rebuild or the frequency table has to be re-caculated
     * @return 
     */
    private byte isFrequencyModified() {
        if (frequencyObj == null) {
            return FreObjReConstruct;
        } else if (frequencyObj.getMinSleepUnit() != IntervalUnit) {
            return FreObjReConstruct;
        } else if (frequencyObj.getDesiredFrequency() != transFrequency || frequencyObj.getFrePrecision() != frePrecision) {
            return FreReCalculate;
        } else {
            return FreObjNoAction;
        }

    }
}
