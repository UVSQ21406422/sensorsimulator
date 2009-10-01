/**
 * represents one packet toghther with its time stamp
 */
package sourcehandler;

/**
 *
 * @author CZC
 */
public class SensorPacket {

    private byte[] data;
    private int dataLength;
    private long timeStamp;

    public SensorPacket(byte[] b, int srcPos, int length, long time) {
        data = new byte[length];
        System.arraycopy(b, srcPos, data, 0, length);
        dataLength = length;
        timeStamp = time;
    }

    public SensorPacket() {
    }

    protected void setPacketData(byte[] b, int srcPos, int length) {
        data = new byte[length];
        System.arraycopy(b, srcPos, data, 0, length);
        dataLength = length;
    }

    protected void setTimeStamp(long time) {
        timeStamp = time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public byte[] getPacketData() {
        return data;
    }

    public int getDataLength() {
        return dataLength;
    }
}
