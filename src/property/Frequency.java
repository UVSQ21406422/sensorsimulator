package property;

import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class Frequency {

    private final int minSleepUnit = 1;//the smallest sleep interval in this system in millisecond
    private final int millisecondsPerSecond = 1000;
    private double frePrecision = 0.07; // frequency precision
    private int sleepInterval;
    private int packetsPerTrans; // number of packets to be transmitted in one transmission
    private double[] freTable; // a table store all
    private double realFrequency;

    public Frequency() {
        constructTable();
    }

    /**
     * build table
     */
    private void constructTable() {
        freTable = null;
        freTable = new double[millisecondsPerSecond / minSleepUnit];
        int i = 0;
        int d = minSleepUnit;
        for (; i < freTable.length; i++) {
            //calculate every element, two digits precision after floating point
            freTable[i] = (double) Math.round((double) millisecondsPerSecond * 100 / d) / 100;
            d += minSleepUnit;
        }
        System.out.println("Table constructured with size = " + freTable.length);
    }

    /**
     * calculate sleep interval and number of packets per transmission
     * @param fre: desired frequency
     */
    protected void calculateFrequency(int fre) throws SimulatorException {
        int i = 0;
        double mod = 0;
        for (; i < freTable.length; i++) {
            mod = (double) fre % freTable[i];
            if (mod < (double) fre * frePrecision) {
                sleepInterval = (i + 1) * minSleepUnit;
                packetsPerTrans = (int) (fre / freTable[i]);
                realFrequency = packetsPerTrans * freTable[i];
                System.out.println("n = " + packetsPerTrans + " T = " + sleepInterval + " RF = " + realFrequency);
                return;
            } else if (Math.abs(freTable[i] - mod) < (double) fre * frePrecision) {
                sleepInterval = (i + 1) * minSleepUnit;
                packetsPerTrans = (int) (fre / freTable[i]) + 1;
                realFrequency = packetsPerTrans * freTable[i];
                System.out.println("n = " + packetsPerTrans + " T = " + sleepInterval + " RF = " + realFrequency);
                return;
            }
        }
        throw new SimulatorException("Error 011: Desired frequency can not be achieved");
    }

    protected int getPacketsPerTrans() {
        return packetsPerTrans;
    }

    protected int getSleepInterval() {
        return sleepInterval;
    }

    protected double getRealFrequency() {
        return realFrequency;
    }

    protected void setFrePrecision(double frePrecision) {
        this.frePrecision = frePrecision;
    }

    protected double getFrePrecision() {
        return frePrecision;
    }
}
