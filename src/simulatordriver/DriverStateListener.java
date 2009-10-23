/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatordriver;

/**
 *
 * @author CZC
 */
public interface DriverStateListener {

    public void transmitProgressEvent(double percent);

    public void systemInforEvent(String message);
}
