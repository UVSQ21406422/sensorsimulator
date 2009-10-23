package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class SimulatorConnection {

    private InputStream is = null;
    private OutputStream os = null;
    private StreamConnectionNotifier service = null;
    private StreamConnection con = null;
    private UUID uuid = new UUID(0x1101);
    private final String serviceURL = "btspp://localhost:" + uuid + ";name=CZC-PC";

    public SimulatorConnection() {
    }

    synchronized public void startSppService() throws SimulatorException {
        try {
            service = (StreamConnectionNotifier) Connector.open(serviceURL);
            System.out.println("Service opened");
            con = (StreamConnection) service.acceptAndOpen();
            is = con.openInputStream();
            os = con.openOutputStream();
            System.out.println("Client request received");
        } catch (IOException ex) {
            throw new SimulatorException("Error 006: Unable to start SPP service");
        }
    }

    public OutputStream getOutputStream() throws SimulatorException {
        if (os == null) {
            throw new SimulatorException("Error 007: Null connection");
        }
        return os;
    }

    public InputStream getInputStream() throws SimulatorException {
        if (is == null) {
            throw new SimulatorException("Error 007: Null connection");
        }
        return is;
    }

    public void close() throws IOException {
        if (is != null) {
            is.close();
            is = null;
        }
        if (os != null) {
            os.close();
            os = null;
        }
        if (service != null) {
            service.close();
            service = null;
        }
        if (con != null) {
            con.close();
            con = null;
        }
    }
}
