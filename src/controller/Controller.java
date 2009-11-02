/**
 * Controller controlls the progress of transmission.
 */
/**
 * @author CZC
 */
package controller;

import controller.writebuffer.WriteToBufferWiTilt;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import property.Property;
import simulatorexception.SimulatorException;
import sourcehandler.SensorFileInputStream;

public class Controller {

    private OutputStream os;                                                    //output bluetooth stream
    private InputStream is;                                                     //input bluetooth stream
    private int bufferSize;                                                     //the size of buffer between loading and transmission
    private TransmissionBuffer transmissionBuffer;                              //the buffer
    private SensorFileInputStream sensorInStream = null;                        //source file input stream
    private WriteToBufferWiTilt wToBufferThread;                                //a buffer writer
    private TransmitBufferThread trsBufferThread;                               //a thread to transmit content in buffer
    private WiTiltCommandReceiving wtComreceiveThread;                          //a command receiver to receive command from receiver
    private Property wtPro;
    private StateListener stateListner;                                         //acton listener

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

    public void open() throws SimulatorException {                              //open simulator service, waiting for client
        if (sensorInStream == null) {
            throw new SimulatorException("Error 005: Null SensorFileInputStream");
        }
        wtComreceiveThread = new WiTiltCommandReceiving(is, os, stateListner);
        wtComreceiveThread.start();
        if (wtComreceiveThread.StartCommandReceived()) {                        //start request received, start transmission
            wToBufferThread = new WriteToBufferWiTilt(wtPro, transmissionBuffer, sensorInStream, stateListner);
            trsBufferThread = new TransmitBufferThread(wtPro, transmissionBuffer, os, stateListner);
            wToBufferThread.start();
            trsBufferThread.start();
        }
        if (wtComreceiveThread.StopCommandReceived()) {                         //stop request received, stop transmission
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
