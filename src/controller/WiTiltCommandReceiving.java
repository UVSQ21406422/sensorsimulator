/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author CZC
 */
public class WiTiltCommandReceiving extends Thread {

    private InputStream is;
    private OutputStream os;
    private boolean startCommandReceived;
    private boolean stopCommandReceived;
    private StateListner stateListner;

    public WiTiltCommandReceiving(InputStream is, OutputStream os, StateListner stateListner) {
        startCommandReceived = false;
        stopCommandReceived = false;
        this.stateListner = stateListner;
        this.is = is;
        this.os = os;
    }

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
    public void run() {
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
