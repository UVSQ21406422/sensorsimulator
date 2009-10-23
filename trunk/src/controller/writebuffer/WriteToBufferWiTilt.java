/**
 * write WiTilt packet to buffer
 */
package controller.writebuffer;

import controller.*;
import property.Property;
import sourcehandler.SensorFileInputStream;

/**
 *
 * @author CZC
 */
public class WriteToBufferWiTilt extends WriteBufferParent {

    public WriteToBufferWiTilt(Property p, TransmissionBuffer buffer, SensorFileInputStream in, StateListner stateListner) {
        super(p, buffer, in,stateListner);
    }

    @Override
    protected void initPacketSize() {
        headerSize = 4;
        tailSize = 1;
        packetSize = wtPro.getChannelNumber() * 2 + headerSize + tailSize;
        maxSize = packetSize * maxSimultaneouslyPacketNo;
        tempData = new byte[maxSize];
    }

    @Override
    protected void fillPacketHeader(int offset) {
        tempData[0 + offset] = (byte) 35; //"#"
        tempData[1 + offset] = (byte) 64; //"@"
        tempData[2 + offset] = (byte) 0x00;
        tempData[3 + offset] = (byte) 0x01;
    }

    @Override
    protected void fillPacketTail(int offset) {
        tempData[packetSize - 1 + offset] = (byte) 36; //the end of the packet
    }
}
