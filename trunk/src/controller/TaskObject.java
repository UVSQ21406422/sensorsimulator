/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

/**
 *
 * @author CZC
 */
public class TaskObject implements Cloneable{

    private byte[] data;
    private int length;  // the length of bytes to be sent
    private long excuteTime;

    public TaskObject() {
        data = null;
        length = -1;
        excuteTime = -1;
    }

    /**
     * Constructer
     * @param bytes: source array
     * @param os: the position in source array
     * @param len: the length to be copied
     */
    public TaskObject(byte[] bytes, int os, int len, long time) {
        data = new byte[len];
        System.arraycopy(bytes, os, data, 0, len);
        length = len;
        excuteTime = time;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }

    /**
     *
     * @return data field
     */
    public byte[] getTaskData() {
        return data;
    }

    /**
     *
     * @return the time to be excuted
     */
    public long getExcuteTime() {
        return excuteTime;
    }

    /**
     *
     * @return the length of data
     */
    public int getDataLength() {
        return length;
    }
}
