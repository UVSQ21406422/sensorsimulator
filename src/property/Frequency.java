package property;

import simulatorexception.SimulatorException;

/**
 *
 * @author CZC
 */
public class Frequency {

    private int minSleepUnit;//the smallest sleep interval in this system in millisecond
    private final int millisecondsPerSecond = 1000;
    private final int exception = 10; // those sleepInterval which are multiple of 10 must be eliminated
    private double frePrecision; // frequency precision
    private int sleepInterval;
    private int packetsPerTrans; // number of packets to be transmitted in one transmission
    private int gcd;
    private int lcm;
    private double[] freTable; // a table store all
    private double realFrequency;

    public Frequency(int fre, int sleepUnit, double precision) throws SimulatorException {
        minSleepUnit = sleepUnit;
        frePrecision = precision;
        gcd = getGCD(minSleepUnit, exception);
        lcm = minSleepUnit * exception / gcd;
        constructTable();
        calculateFrequency(fre);
    }

    /**
     * build table
     */
    private void constructTable() {
        freTable = null;
        
        //calculate the size of the array, the multiple of gcd must be eliminated
        int size = millisecondsPerSecond / minSleepUnit - millisecondsPerSecond / lcm;

        freTable = new double[size];
        int i = 0;
        int d = minSleepUnit;
        for (; d < millisecondsPerSecond; d += minSleepUnit) {
            //calculate every element, two digits precision after floating point
            if (d % lcm == 0) {
                continue;
            }
            freTable[i] = (double) Math.round((double) millisecondsPerSecond * 100 / d) / 100;          
            i++;
        }
    }

    /**
     * calculate the greatest common divisor
     * @param a
     * @param b
     * @return the graetest common divisor
     */
    private int getGCD(int a, int b) {
        int x = a, y = b, temp;
        while (b != 0) {
            temp = a % b;
            a = b;
            b = temp;
        }
        return a;
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
                sleepInterval = (i + 1) * minSleepUnit % lcm == 0 ? (i+2)*minSleepUnit:(i + 1) * minSleepUnit;
                packetsPerTrans = (int) (fre / freTable[i]);
                realFrequency = packetsPerTrans * freTable[i];
                return;
            } else if (Math.abs(freTable[i] - mod) < (double) fre * frePrecision) {
                sleepInterval = (i + 1) * minSleepUnit % lcm == 0 ? (i+2)*minSleepUnit:(i + 1) * minSleepUnit;
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
