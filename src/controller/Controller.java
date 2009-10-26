package controller;

import controller.writebuffer.WriteToBufferWiTilt;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import property.Property;
import simulatorexception.SimulatorException;
import sourcehandler.SensorFileInputStream;

/**
 *
 * @author CZC
 */
public class Controller {

    private OutputStream os;
    private InputStream is;
    private int bufferSize;
    private TransmissionBuffer transmissionBuffer;
    private SensorFileInputStream sensorInStream = null;
    private WriteToBufferWiTilt wToBufferThread;
    private TransmitBufferThread trsBufferThread;
    private WiTiltCommandReceiving wtComreceiveThread;
    private Property wtPro;
    private StateListener stateListner;

    public Controller(Property p, InputStream is, OutputStream os, StateListener stateListner) throws SimulatorException {
        this.os = os;
        this.is = is;
        wtPro = p;
        bufferSize = p.getBufferSize();
        transmissionBuffer = new TransmissionBuffer(bufferSize);
        this.stateListner = stateListner;
        initFileInputStream();
    }

    private void initFileInputStream() throws SimulatorException {
        sensorInStream = new SensorFileInputStream(wtPro, stateListner);
    }

    public void open() throws SimulatorException {
        if (sensorInStream == null) {
            throw new SimulatorException("Error 005: Null SensorFileInputStream");
        }
        wtComreceiveThread = new WiTiltCommandReceiving(is, os, stateListner);
        wtComreceiveThread.start();
        if (wtComreceiveThread.StartCommandReceived()) {
            wToBufferThread = new WriteToBufferWiTilt(wtPro, transmissionBuffer, sensorInStream, stateListner);
            trsBufferThread = new TransmitBufferThread(wtPro, transmissionBuffer, os, stateListner);
            wToBufferThread.start();
            trsBufferThread.start();
        }
        if (wtComreceiveThread.StopCommandReceived()) {
            stateListner.stopCommandReceived();
        }
    }

    public void close() throws SimulatorException {
        if (wToBufferThread != null) {
            wToBufferThread.stopWriteToBuffer();
        }
        if (trsBufferThread != null) {
            trsBufferThread.stopTransmission();
        }
        transmissionBuffer = null;
        if (sensorInStream != null) {
            sensorInStream.close();
            sensorInStream = null;
        }
        wtComreceiveThread = null;
        wToBufferThread = null;
        trsBufferThread = null;
        try {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
            throw new SimulatorException("Error 008: Unable to close connection");
        }

        stateListner.systemInforEvent("Closed");
        System.out.println("Closed");
    }
}
