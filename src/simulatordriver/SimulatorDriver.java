/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatordriver;

import communication.SimulatorConnection;
import controller.Controller;
import property.Property;
import simulatorexception.SimulatorException;
import sourcehandler.SensorFileInputStream;

/**
 *
 * @author CZC
 */
public class SimulatorDriver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //String filepath = "C:/Documents and Settings/chen/Desktop/tempPhoto/rawSeat.txt";
        String filepath = "D:/Study/WiTilt Simulator Project/testdata/short.txt";
        SimulatorConnection simCon = new SimulatorConnection();
        Controller controller = null;
        Property wtPro = new Property(filepath, SensorFileInputStream.TimeStampPosition_End, Property.TransMode_TimeStamp, Property.SensorType_WiTiltSensor);
        try {
            simCon.startSppService();
            controller = new Controller(wtPro, simCon.getInputStream(), simCon.getOutputStream());
            controller.initFileInputStream();
            controller.open();
        } catch (SimulatorException ex) {
            System.out.println(ex.getMessage());
            return;
        }
    }
}
