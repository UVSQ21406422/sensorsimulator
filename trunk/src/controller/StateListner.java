/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

/**
 *
 * @author CZC
 */
public interface StateListner {

    public void transmitProgressEvent(long size);

    public void systemInforEvent(String message);

}
