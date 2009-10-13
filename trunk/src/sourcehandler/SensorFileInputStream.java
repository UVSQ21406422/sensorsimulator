/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sourcehandler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import property.Property;
import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class SensorFileInputStream {

    private String filePath;
    private byte timeStampPosition; // indicate the position of time stamp in source file
    private byte outputByteOrder; // low-high or high-low
    private int dataUnitFormat; //how many bytes to represent one data value, short(2), int(4) or long(8), default is short.
    private BufferedReader bufferIn;
    private String lineString = null;
    private int tokenCounts;
    private int toCount; //count of each token in one line string
    private int byteCount; //count of each data byte in one line string
    private SensorPacket sensorPacket;

    public SensorFileInputStream(Property p) throws SimulatorException {
        filePath = p.getFilePath();
        timeStampPosition = p.getTimeStampPosition();
        outputByteOrder = p.getOutputByteOrder();
        dataUnitFormat = p.getDataUnitFormat();
        try {
            bufferIn = new BufferedReader(new InputStreamReader(new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)))));
        } catch (FileNotFoundException ex) {
            throw new SimulatorException("Error 003: Source file not found");
        }
    }

    /**
     * read a line of data from source.txt file
     * @return an object of SensorPacket which contains binary data and time stamp, return null if the end of the file is reached
     */
    public SensorPacket readLine() throws SimulatorException {
        try {
            lineString = bufferIn.readLine();
            if (lineString == null) {  // return null if the end of stream is reached
                throw new SimulatorException("Error 001: End of file");
                //return null;
            } else {
                StringTokenizer st = new StringTokenizer(lineString);
                tokenCounts = st.countTokens();
                toCount = 0;
                byteCount = (timeStampPosition == Property.TimeStampPosition_Begin ? toCount - 1 : toCount);
                sensorPacket = new SensorPacket();
                byte[] temp = new byte[(tokenCounts - 1) * dataUnitFormat];
                while (st.hasMoreTokens()) {
                    if (toCount == 0 && timeStampPosition == Property.TimeStampPosition_Begin) {
                        sensorPacket.setTimeStamp(Long.parseLong(st.nextToken()));
                    } else if (toCount == tokenCounts - 1 && timeStampPosition == Property.TimeStampPosition_End) {
                        sensorPacket.setTimeStamp(Long.parseLong(st.nextToken()));
                    } else {
                        switch (dataUnitFormat) {
                            case Property.DataFormat_Short:
                                try {
                                    System.arraycopy(shortToBinary(Short.parseShort(st.nextToken()), outputByteOrder), 0, temp, byteCount * dataUnitFormat, dataUnitFormat);
                                } catch (NumberFormatException e) {
                                    System.out.println(e.getMessage());
                                    System.arraycopy(shortToBinary(Short.parseShort("0"), outputByteOrder), 0, temp, byteCount * dataUnitFormat, dataUnitFormat);
                                }
                                break;
                            case Property.DataFormat_Integer:
                                try {
                                    System.arraycopy(intToBinary(Integer.parseInt(st.nextToken()), outputByteOrder), 0, temp, byteCount * dataUnitFormat, dataUnitFormat);
                                } catch (NumberFormatException e) {
                                    System.out.println(e.getMessage());
                                    System.arraycopy(intToBinary(Integer.parseInt("0"), outputByteOrder), 0, temp, byteCount * dataUnitFormat, dataUnitFormat);
                                }

                                break;
                            case Property.DataFormat_Long:
                                try {
                                    System.arraycopy(longToBinary(Long.parseLong(st.nextToken()), outputByteOrder), 0, temp, byteCount * dataUnitFormat, dataUnitFormat);
                                } catch (NumberFormatException e) {
                                    System.out.println(e.getMessage());
                                    System.arraycopy(longToBinary(Long.parseLong("0"), outputByteOrder), 0, temp, byteCount * dataUnitFormat, dataUnitFormat);
                                }

                                break;
                            default:
                                break;
                        }
                    }
                    toCount++;
                    byteCount++;
                }
                sensorPacket.setPacketData(temp, 0, temp.length);
            }
        } catch (NumberFormatException e) {
            throw new SimulatorException("Error 012: Wrong time stamp position");
        } catch (IOException ex) {
            throw new SimulatorException("Error 002: File Reading Exception");
            // return null;
        }
        return sensorPacket;
    }

    public void resetSensorFileInputStream() throws SimulatorException {
        try {
            bufferIn.close();
            bufferIn = null;
            bufferIn = new BufferedReader(new InputStreamReader(new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)))));
        } catch (FileNotFoundException ex) {
            throw new SimulatorException("Error 003: Source file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * change the default data size
     * @param s, new data size
     */
    public void setDataUnitFormat(int dataunitformat) {
        dataUnitFormat = dataunitformat;
    }

    /**
     * Skips characters.
     * @param n, The number of characters to skip 
     * @return The number of characters actually skipped
     */
    public long skip(long n) {
        try {
            return bufferIn.skip(n);
        } catch (IOException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * close SensorFileInputStream
     */
    public void close() {
        try {
            bufferIn.close();
            bufferIn = null;
            sensorPacket = null;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     *get the size of source file
     * @return the size of file in bytes
     */
    public long getFileSize() {
        return new File(filePath).length();
    }

    /**
     * 
     * @param value
     * @param byteorder, which type of byte order
     * @return byte array or null if no byte order matched
     */
    private byte[] shortToBinary(short value, int byteorder) {
        switch (byteorder) {
            case Property.ByteOrder_HighLow:
                return new byte[]{
                            (byte) (value >>> 8), (byte) (value & 0xFF)
                        };
            case Property.ByteOrder_LowHigh:
                return new byte[]{
                            (byte) (value & 0xFF), (byte) (value >>> 8)
                        };
            default:
                return null;
        }

    }

    /**
     * 
     * @param value
     * @param byteorder
     * @return byte array or null if no byte order matched
     */
    private byte[] intToBinary(int value, int byteorder) {
        switch (byteorder) {
            case Property.ByteOrder_HighLow:
                return new byte[]{
                            (byte) (value >>> 24), (byte) (value >> 16 & 0xFF), (byte) (value >> 8 & 0xff), (byte) (value & 0xff)
                        };
            case Property.ByteOrder_LowHigh:
                return new byte[]{
                            (byte) (value & 0xff), (byte) (value >> 8 & 0xff), (byte) (value >> 16 & 0xFF), (byte) (value >>> 24)
                        };
            default:
                return null;
        }
    }

    /**
     * 
     * @param value
     * @param byteorder
     * @return byte array or null if no byte order matched
     */
    private byte[] longToBinary(long value, int byteorder) {

        switch (byteorder) {
            case Property.ByteOrder_HighLow:
                return new byte[]{
                            (byte) (value >>> 56), (byte) (value >> 48 & 0xFF), (byte) (value >> 40 & 0xff), (byte) (value >> 32 & 0xff),
                            (byte) (value >> 24 & 0xff), (byte) (value >> 16 & 0xFF), (byte) (value >> 8 & 0xff), (byte) (value & 0xff)
                        };
            case Property.ByteOrder_LowHigh:
                return new byte[]{
                            (byte) (value & 0xff), (byte) (value >> 8 & 0xff), (byte) (value >> 16 & 0xFF), (byte) (value >> 24 & 0xff),
                            (byte) (value >> 32 & 0xff), (byte) (value >> 40 & 0xff), (byte) (value >> 48 & 0xFF), (byte) (value >>> 56)
                        };
            default:
                return null;
        }
    }
}
