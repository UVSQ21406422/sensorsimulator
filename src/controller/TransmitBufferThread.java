package controller;

import java.io.IOException;
import java.io.OutputStream;
import property.Property;
import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class TransmitBufferThread extends Thread {

    private OutputStream os;
    private TransmissionBuffer buffer;
    private int index; // index in the taskBuffer
    private boolean stop;
    private int bufferSize;
    private byte transMode; // transmission mode
    private int sleepInterval;

    public TransmitBufferThread(Property p, TransmissionBuffer buffer, OutputStream os) {
        this.os = os;
        this.buffer = buffer;
        index = 0;
        stop = false;
        bufferSize = buffer.getBufferSize();
        transMode = p.getTransMode();
        if (transMode == Property.TransMode_Frequency) {
            sleepInterval = p.getSleepInterval();
        }
    }

    @Override
    public void run() {
        long delay = transMode == Property.TransMode_Frequency ? sleepInterval : -1; //delay between each transmission
        try {
            switch (transMode) {
                case Property.TransMode_Frequency:
                    while (!stop) {
                        try {
                            os.write(buffer.getBufferElementAt(index).getTaskData());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        index++;

                        //turn on loading once half of the buffer have been sent
                        if (index == bufferSize) {
                            index = 0;
                            buffer.turnOnWriting();
                        } else if (index == bufferSize / 2) {
                            buffer.turnOnWriting();
                        }

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
                case Property.TransMode_TimeStamp:
                    while (!stop) {
                        try {
                            os.write(buffer.getBufferElementAt(index).getTaskData());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        delay = buffer.getBufferElementAt(index).getDelay();

                        index++;

                        //turn on loading once half of the buffer have been sent
                        if (index == bufferSize) {
                            index = 0;
                            buffer.turnOnWriting();
                        } else if (index == bufferSize / 2) {
                            buffer.turnOnWriting();
                        }

                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    break;
                default:
                    throw new SimulatorException("Error 010: Unknown transmission mode");
            }

        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage() + ". " + "Probably caused by obsolute buffer or file end reached");
            return;
        } catch (SimulatorException e) {
            System.out.println(e.getMessage() + " at TransmitBufferThread");
            return;
        }
    }

    public void stopTransmission() {
        stop = true;
    }
}
