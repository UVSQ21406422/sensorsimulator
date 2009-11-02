/**
 * The buffer between loading and transmission
 */
/**
 * @author CZC
 */
package controller;

import simulatorexception.SimulatorException;

public class TransmissionBuffer {

    private TaskObject[] buffer;
    private int bufferSize;
    private boolean readReady; // true if allow to read from buffer, otherwise false
    private boolean writeReady; // true if allow to write to buffer, otherwise false

    public TransmissionBuffer(int bufferSize) {
        buffer = new TaskObject[bufferSize];
        readReady = false;
        writeReady = true;
        this.bufferSize = bufferSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * get an element from buffer, blocked if readReady is false
     * @param elementIndex
     * @return an element of buffer
     */
    synchronized public TaskObject getBufferElementAt(int elementIndex) {
        if (!readReady) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return buffer[elementIndex];
    }

    /**
     * set buffer element, blocked if writeReady is false
     * @param elementIndex
     * @param to
     * @throws simulatorexception.SimulatorException if array out of bounds
     */
    synchronized public void setBufferElementAt(int elementIndex, TaskObject to) throws SimulatorException {
        if (!writeReady) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        try {
            buffer[elementIndex] = (TaskObject) to.clone();
        } catch (Exception e) {
            throw new SimulatorException("Error 004: Buffer array out of bounds");
        }
    }

    synchronized public void turnOnReading() {
        readReady = true;
        notifyAll();
    }

    synchronized public void turnOffReading() {
        readReady = false;
        notifyAll();
    }

    synchronized public void turnOnWriting() {
        writeReady = true;
        notifyAll();
    }

    synchronized public void turnOffWriting() {
        writeReady = false;
        notifyAll();
    }
}
