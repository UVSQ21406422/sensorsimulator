/**
 *This class is the driver for different simulator components and provides interfaces for GUI package
 */
/**
 *
 * @author Cao & Chen
 */
package simulatordriver;

import communication.SimulatorConnection;
import controller.Controller;
import controller.StateListener;
import java.io.IOException;
import property.Property;
import simulatorexception.SimulatorException;

public class SimulatorDriver implements StateListener {

    private SimulatorConnection simCon;                                         
    private Controller controller;
    private Property wtPro;
    private DriverStateListener driverStateListner;

    public SimulatorDriver(DriverStateListener driverStateListner) throws SimulatorException {
        this.driverStateListner = driverStateListner;
        wtPro = new Property(this);
    }

    public void start() throws SimulatorException {                            //start simulation service
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

    /**
     * implementation of StateListener interface
     * define the actions when transmitProgressEvent happens
     * @param percent
     */
    public void transmitProgressEvent(double percent) {
        driverStateListner.transmitProgressEvent(percent);
    }

    /**
     * implementation of StateListener interface
     * define the actions when systemInforEvent happens
     * @param message
     */
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

    /**
     * implementation of StateListener interface
     * define the actions when a stop command from receiver is received
     */
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
