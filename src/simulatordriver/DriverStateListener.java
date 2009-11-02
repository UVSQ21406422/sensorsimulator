/**
 *
 * @author Cao
 */
package simulatordriver;

public interface DriverStateListener {

    public void transmitProgressEvent(double percent);

    public void systemInforEvent(String message);
}
