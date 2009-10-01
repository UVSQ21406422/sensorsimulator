/**
 * Exception class
 */
package simulatorexception;

/**
 *
 * @author CZC
 */
public class SimulatorException extends Exception {

    /**
     * Error 001: End of file
     * Error 002: File Reading Exception
     * Error 003: Source file not found
     * Error 004: Buffer array out of bounds
     * Error 005: Null SensorFileInputStream
     * Error 006: Unable to start SPP service
     * Error 007: Null connection
     * Error 008: Unable to close connection
     * @param msg
     */
    public SimulatorException(String msg) {
        super(msg);
    }
}
