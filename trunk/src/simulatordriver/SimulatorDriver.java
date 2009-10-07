/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatordriver;

import communication.SimulatorConnection;
import controller.Controller;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Property wtPro = null;
        try {
            wtPro = new Property(filepath, Property.TransMode_TimeStamp, SensorFileInputStream.TimeStampPosition_End, Property.TransFrequency_DefaultFrequency, Property.SensorType_WiTiltSensor);
        } catch (SimulatorException ex) {
            System.out.println(ex.getMessage());
            return;
        }
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
