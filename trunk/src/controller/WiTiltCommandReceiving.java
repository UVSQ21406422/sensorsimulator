/**
 * This class receives command from receiver
 */
/**
 * @author CZC
 */
package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WiTiltCommandReceiving extends Thread {

    private InputStream is;
    private OutputStream os;
    private boolean startCommandReceived;                                       //true if start command is received
    private boolean stopCommandReceived;                                        //true if stop command is received
    private StateListener stateListner;

    public WiTiltCommandReceiving(InputStream is, OutputStream os, StateListener stateListner) {
        startCommandReceived = false;
        stopCommandReceived = false;
        this.stateListner = stateListner;
        this.is = is;
        this.os = os;
    }

    /**
     * A method to check start command status
     * @return true if start command was received, else block the thread
     */
    synchronized protected boolean StartCommandReceived() {
        if (!startCommandReceived) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return startCommandReceived;
    }

    /**
     * A method to check stop command status
     * @return true if stop command was received, else block the thread
     */
    synchronized protected boolean StopCommandReceived() {
        if (!stopCommandReceived) {
            try {
                wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        return stopCommandReceived;
    }

    @Override
    public void run() {                                                         //recieving commands
        try {
            int ch;
            while ((ch = is.read()) != -1) {
                if (ch == 49) {
                    os.write("#R$".getBytes());
                }
                if (ch == 82) {                        //'R' is received
                    stateListner.systemInforEvent("Ready command received");
                    System.out.println("Ready command received");
                    os.write("#R$".getBytes());
                }
                if (ch == 65) {                         //'A' is received
                    synchronized (this) {
                        stopCommandReceived = true;
                        notifyAll();
                    }
                    stateListner.systemInforEvent("Stop command received");
                    System.out.println("Stop command received");
                    break;
                }
                if (ch == 83) {                         //'S' is received
                    synchronized (this) {
                        startCommandReceived = true;
                        notifyAll();
                    }
                    os.write("#R$".getBytes());
                    stateListner.systemInforEvent("Start command received");
                    System.out.println("Start command received");
                }
            }
        } catch (IOException e) {
            stateListner.systemInforEvent(e.getMessage());
            System.out.println(e.getMessage());
        }
    }
}
