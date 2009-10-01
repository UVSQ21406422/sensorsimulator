/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author CZC
 */
public class TransmitBufferThread extends Thread {

    
    private OutputStream os;
    private TransmissionBuffer buffer;
    private int index; // index in the taskBuffer
    private long prevTime;
    private boolean stop;
    private int bufferSize;

    public TransmitBufferThread(TransmissionBuffer buffer, OutputStream os) {
        this.os = os;
        this.buffer = buffer;
        index = 0;
        prevTime = -1;
        stop = false;
        bufferSize = buffer.getBufferSize();
    }

    @Override
    public void run() {
        long delay = -1; //delay between each transmission
        try {
            while (!stop) {
                try {

                    os.write(buffer.getBufferElementAt(index).getTaskData());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // System.out.println(buffer.getBufferElementAt(index).getExcuteTime());
                prevTime = buffer.getBufferElementAt(index).getExcuteTime();

                //System.out.println(System.currentTimeMillis());
                index++;

                //turn on loading once half of the buffer have been sent
                if (index == bufferSize) {
                    index = 0;
                    buffer.turnOnWriting();
                } else if (index == bufferSize / 2) {
                    buffer.turnOnWriting();
                }
                delay = buffer.getBufferElementAt(index).getExcuteTime() - prevTime;
                
                try {                  
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage() + ". " + "Probably caused by obsolute buffer or file end reached");
            return;
        }
    }

    public void stopTransmission() {
        stop = true;
    }
}
