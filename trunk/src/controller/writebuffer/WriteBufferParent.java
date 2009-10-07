/**
 * This is the parent class of all WriteBuffer classes
 * Three abstract methods must be implemented.
 */
package controller.writebuffer;

import controller.TaskObject;
import controller.TransmissionBuffer;
import property.Property;
import simulatorexception.SimulatorException;
import sourcehandler.SensorFileInputStream;
import sourcehandler.SensorPacket;

/**
 *
 * @author CZC
 */
public abstract class WriteBufferParent extends Thread {

    protected int bufferSize;
    protected int count;
    protected boolean stop;
    protected int maxSimultaneouslyPacketNo;
    protected int channelNumber;
    protected int packetSize; //the size of one wiTilt packet
    protected int maxSize;  // the maximum size of tempData
    protected int headerSize;
    protected int tailSize;
    protected int transFrequency;
    protected byte[] tempData;
    protected byte transMode; // transmission mode
    protected SensorFileInputStream fileInputStream;
    protected TransmissionBuffer buffer;

    protected WriteBufferParent(Property p, TransmissionBuffer buffer, SensorFileInputStream in) {
        count = 0;
        stop = false;
        this.buffer = buffer;
        fileInputStream = in;
        bufferSize = p.getBufferSize();
        channelNumber = p.getChannelNumber();
        transMode = p.getTransMode();
        if (transMode == Property.TransMode_TimeStamp) {
            //in time stamp mode, a limit is set for the number of packets would be transmit in one transmission
            maxSimultaneouslyPacketNo = p.getMaxSimultaneouslyPacketNo();
        } else if (transMode == Property.TransMode_Frequency) {
            transFrequency = p.getFrequency();
            //in frequency mode, the number of packets in one transmission is required by the calculation result from desired frequency
            maxSimultaneouslyPacketNo = p.getPacketsPerTrans();
        }
        // packetSize = channelNumber * 2 + 5;
        // maxSize = packetSize * maxSimultaneouslyPacketNo;
        // tempData = new byte[maxSize];
    }

    /**
     * initialize parameters about packet size
     */
    abstract protected void initPacketSize();

    /**
     * assemble packet header
     * @param offset, offset in byte array, tempData
     */
    abstract protected void fillPacketHeader(int offset);

    /**
     * assemble packet tail
     * @param offset, offset in byte array, tempData
     */
    abstract protected void fillPacketTail(int offset);

    @Override
    public void run() {
        initPacketSize();
        long currTime = -1, prevTime = -1;
        int repeatedPacketNo = 0;  // how many packet in one buffer element
        int offset = 0;  // offset in tempData
        boolean firstLoop = true; // after the first round to go through the whole buffer, switch to false       
        boolean rollOver = false;
        SensorPacket sp = null;
        System.out.println("Loading...");
        while (!stop) {
            while (!stop && count < bufferSize) {

                //packet header and sample number
                fillPacketHeader(offset);
                /* tempData[0 + offset] = (byte) 35; //"#"
                tempData[1 + offset] = (byte) 64; //"@"
                tempData[2 + offset] = (byte) 0x00;
                tempData[3 + offset] = (byte) 0x01;*/

                try {
                    try {
                        sp = fileInputStream.readLine();
                    } catch (SimulatorException ee) {
                        if (rollOver) {
                            throw new SimulatorException("Error 009: Source file is empty");
                        } else {
                            System.out.println("Playback");
                            rollOver = true;
                            fileInputStream.resetSensorFileInputStream();
                            sp = fileInputStream.readLine();
                        }
                    }
                    System.arraycopy(sp.getPacketData(), 0, tempData, headerSize + offset, sp.getDataLength());

                    fillPacketTail(offset);
                    //  tempData[packetSize - 1 + offset] = (byte) 36; //the end of the packet

                    if (transMode == Property.TransMode_TimeStamp) { //time stamp mode
                        currTime = sp.getTimeStamp();
                        if (currTime != prevTime) {
                            if (prevTime != -1) {  //current time stamp is different with previous time stamp, this is a new buffer element
                                //create a new TaskObject
                                TaskObject to = new TaskObject(tempData, 0, repeatedPacketNo * packetSize, rollOver ? 0 : currTime - prevTime);

                                rollOver = false;

                                //put it into the buffer
                                buffer.setBufferElementAt(count, to);

                                repeatedPacketNo = 0;  // reset buffer element size

                                //move the current packet to the beginning of tempData
                                for (int i = 0; i < packetSize; i++) {
                                    tempData[i] = tempData[i + offset];
                                }

                                count++;

                                //stop loading once half the buffer is filled
                                if (count == bufferSize / 2 && !firstLoop) {
                                    buffer.turnOffWriting();
                                }
                            }
                            prevTime = currTime;
                        }
                        repeatedPacketNo++;
                        offset = repeatedPacketNo * packetSize;
                        continue;
                    } else if (transMode == Property.TransMode_Frequency) { //frequency mode

                        if (repeatedPacketNo == maxSimultaneouslyPacketNo - 1) {
                            //create a new TaskObject
                            TaskObject to = new TaskObject(tempData, 0, maxSimultaneouslyPacketNo * packetSize, -1);

                            //put it into the buffer
                            buffer.setBufferElementAt(count, to);

                            repeatedPacketNo = 0;  // reset buffer element size
                            offset = 0;

                            count++;

                            //stop loading once half the buffer is filled
                            if (count == bufferSize / 2 && !firstLoop) {
                                buffer.turnOffWriting();
                            }
                        } else {
                            repeatedPacketNo++;
                            offset = repeatedPacketNo * packetSize;
                            continue;
                        }
                    } else {
                        throw new SimulatorException("Error 010: Unknown transmission mode");
                    }


                } catch (SimulatorException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Loading exit");
                    stop = true;
                    break;
                }
            }

            buffer.turnOffWriting();
            if (firstLoop) {
                firstLoop = false;
                buffer.turnOnReading();
            }
            count = 0;
        }
        fileInputStream.close();
        fileInputStream = null;
        return;
    }

    public void stopWriteToBuffer() {
        stop = true;
    }
}
