/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatordriver;

import communication.SimulatorConnection;
import controller.Controller;
import controller.StateListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import property.Property;
import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class SimulatorDriver implements StateListener {

    private SimulatorConnection simCon;
    private Controller controller;
    private Property wtPro;
    private DriverStateListener driverStateListner;

    public SimulatorDriver(DriverStateListener driverStateListner) throws SimulatorException {
        this.driverStateListner = driverStateListner;
        wtPro = new Property(this);

    }

    public void start() throws SimulatorException {
        wtPro.initFrequencyObj();
        simCon = new SimulatorConnection();
        simCon.startSppService();
        controller = new Controller(wtPro, simCon.getInputStream(), simCon.getOutputStream(), this);
        controller.open();
    }

    public void setGeneralProperties(String path, byte mode, byte timestampposition, int fre, String sensortype) throws SimulatorException {
        wtPro.setGeneralProperties(path, mode, timestampposition, fre, sensortype);

    }

    public void setAdvanceProperties(byte outputbyteorder, int dataunitformat, int channelnumber, double frePrecision, byte headerContent) throws SimulatorException {
        wtPro.setAdvanceProperties(outputbyteorder, dataunitformat, channelnumber, frePrecision, headerContent);
    }

    public Property getWtPro() {
        return wtPro;
    }

    public void transmitProgressEvent(long size) {
        driverStateListner.transmitProgressEvent((double) size / controller.getFileSize());
    }

    public void systemInforEvent(String message) {
        driverStateListner.systemInforEvent(message);
    }

    public void close() throws SimulatorException, IOException {
        if (controller != null) {
            controller.close();
            controller = null;
        }
        if (simCon != null) {
            simCon.close();
            simCon = null;
        }

    }

    public void stopCommandReceived() {
        try {
            close();
        } catch (SimulatorException ex) {
            driverStateListner.systemInforEvent(ex.getMessage());
        } catch (IOException ex) {
            driverStateListner.systemInforEvent(ex.getMessage());
        }
    }
}
