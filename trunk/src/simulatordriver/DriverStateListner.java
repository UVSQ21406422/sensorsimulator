/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatordriver;

/**
 *
 * @author CZC
 */
public interface DriverStateListner {

    public void transmitProgressEvent(double percent);

    public void systemInforEvent(String message);
}
