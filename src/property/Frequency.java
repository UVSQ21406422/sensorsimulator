package property;

import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class Frequency {

    private int minSleepUnit;//the smallest sleep interval in this system in millisecond
    private final int millisecondsPerSecond = 1000;
    private double frePrecision; // frequency precision
    private int sleepInterval;
    private int packetsPerTrans; // number of packets to be transmitted in one transmission
    private double[] freTable; // a table store all
    private double realFrequency;

    public Frequency(int fre, int sleepUnit, double precision) throws SimulatorException {
        minSleepUnit = sleepUnit;
        frePrecision = precision;
        constructTable();
        calculateFrequency(fre);
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
                return;
            } else if (Math.abs(freTable[i] - mod) < (double) fre * frePrecision) {
                sleepInterval = (i + 1) * minSleepUnit;
                packetsPerTrans = (int) (fre / freTable[i]) + 1;
                realFrequency = packetsPerTrans * freTable[i];
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
}
