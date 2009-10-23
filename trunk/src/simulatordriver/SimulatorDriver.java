/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatordriver;

import communication.SimulatorConnection;
import controller.Controller;
import controller.StateListner;
import property.Property;
import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class SimulatorDriver implements StateListner {

    private String filepath;
    private SimulatorConnection simCon;
    private Controller controller;
    private Property wtPro;
    private DriverStateListner driverStateListner;

    public SimulatorDriver(DriverStateListner driverStateListner) throws SimulatorException {
        //filepath = "D:/Study/WiTilt Simulator Project/testdata/short.txt";
        //filepath = "C:/Documents and Settings/chen/Desktop/tempPhoto/rawSeat.txt";
        this.driverStateListner = driverStateListner;
        wtPro = new Property();

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
        driverStateListner.transmitProgressEvent((double)size / controller.getFileSize());
    }

    public void systemInforEvent(String message) {
        driverStateListner.systemInforEvent(message);
    }

    public void close() throws SimulatorException{
        controller.close();
    }
}
