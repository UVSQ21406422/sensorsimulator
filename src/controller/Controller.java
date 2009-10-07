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

    public Controller(Property p, InputStream is, OutputStream os) {
        this.os = os;
        this.is = is;
        wtPro = p;
        bufferSize = p.getBufferSize();
        transmissionBuffer = new TransmissionBuffer(bufferSize);
    }

    public void initFileInputStream() throws SimulatorException {
        sensorInStream = new SensorFileInputStream(wtPro);
    }

    public void open() throws SimulatorException {
        if (sensorInStream == null) {
            throw new SimulatorException("Error 005: Null SensorFileInputStream");
        }
        wtComreceiveThread = new WiTiltCommandReceiving(is, os);
        wtComreceiveThread.start();
        if (wtComreceiveThread.StartCommandReceived()) {
            wToBufferThread = new WriteToBufferWiTilt(wtPro, transmissionBuffer, sensorInStream);
            trsBufferThread = new TransmitBufferThread(wtPro, transmissionBuffer, os);
            wToBufferThread.start();
            trsBufferThread.start();
        }
        if (wtComreceiveThread.StopCommandReceived()) {
            close();
        }
    }

    public void close() throws SimulatorException {
        wToBufferThread.stopWriteToBuffer();
        trsBufferThread.stopTransmission();
        transmissionBuffer = null;
        sensorInStream = null;
        try {
            os.close();
            is.close();
        } catch (IOException ex) {
            throw new SimulatorException("Error 008: Unable to close connection");
        }
        is = null;
        os = null;
        System.out.println("Closed");
    }
}
