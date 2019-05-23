package PhysicalModell.src;

import java.util.ArrayList;

import static java.lang.Math.pow;

public class Press {

    ArrayList<Double> temp; //Temperature sensor in Celsius
    ArrayList<Double> pressure; //Pressure sensor in Bar
    ArrayList<Double> wearlevel; //Wear level 0-100


    ArrayList<Integer> errorCodes;
    /**
     * fire, explode, or component stop
     */
    static double ERROR_CODE_FIRE = 1;
    static double ERROR_CODE_EXPLODE = 2;
    static double ERROR_CODE_STOP = 3;

    double workingTime; // Time based values

    double hardwareErrorRate; // error rate in hardware components

    public void setPrintLevels(boolean printLevels) {
        this.printLevels = printLevels;
    }

    private boolean printLevels; //print to system out the levels

    /**
     * Init press, setting default values
     */
    public Press() {
        printLevels = true;
        workingTime = 0;
        wearlevel = new ArrayList<Double>(4);
        pressure = new ArrayList<Double>(4);
        temp = new ArrayList<Double>(4);
        errorCodes = new ArrayList<>(4);

        for (int i = 0; i < 4; i++) {
            wearlevel.add((double) 100);
            pressure.add((double) 0);
            temp.add((double) 0);
            errorCodes.add(0);
        }
        sethibaVal();
    }


    /**
     * setting failiure rate based on elapsedWorkingTime
     *
     * @return
     */
    private double sethibaVal() {
        double var = -0.0002 * pow(workingTime, 6) + 0.0074 * pow(workingTime, 5) - 0.0823 * pow(workingTime, 4)
                + 0.4208 * pow(workingTime, 3) - 0.923 * pow(workingTime, 2) + 0.4437 * workingTime + 0.8046;
        if (var > 0.9) //error rate max is 0.9
            return 0.9;
        return var;
    }

    /**
     * dynamic model needs time value, time should be increased external
     */
    public void increaseTime() {
        workingTime += 0.1;
        hardwareErrorRate = sethibaVal();
        generateError();
        generateHeatAndPressure();
        printLevels();
    }

    /**
     * Simulate a system failiure based on error rate
     */
    private void generateError() {
        double errorRand = Math.random() * hardwareErrorRate; //random number, to control if error occurs or not
        if (errorRand > 0.6) { //it's set to > 0.6 based on experimental tests
            int errorDrastical = (int) (errorRand * 10); // sets the error impact on system, more drastical, the more wear level decreases
            setWearLevel(errorDrastical, generateRandomSensorID());
        }
        generateErrorCode();
    }

    /**
     * gets random sensor ID 0-3
     *
     * @return
     */
    private int generateRandomSensorID() {
        return (int) ((Math.random() * 4) - 0.001);

    }

    /**
     * simulate system failiure
     */
    private void generateErrorCode() {
        double errorRand = Math.random();
        if (errorRand < 0.94) {
        } else if (errorRand < 0.96) {
            errorCodes.set(generateRandomSensorID(), (int) ERROR_CODE_STOP);
        } else if (errorRand < 0.98) {
            errorCodes.set(generateRandomSensorID(), (int) ERROR_CODE_EXPLODE);
        } else if (errorRand < 1) {
            errorCodes.set(generateRandomSensorID(), (int) ERROR_CODE_FIRE);
        }

    }

    /**
     * Based on damage, set the wear level for components
     *
     * @param drastical
     * @param id
     */
    public void setWearLevel(int drastical, int id) {
        double minus = drastical * Math.random();
        wearlevel.set(id, wearlevel.get(id) - minus);
    }

    /**
     * repair the current element
     *
     * @param id
     * @param drastical
     */
    public void repair(int id, double drastical) {
        double repairLevel = drastical * Math.random();
        double newLevel = wearlevel.get(id) + repairLevel;
        if (newLevel < 100)
            wearlevel.set(id, newLevel);
        else wearlevel.set(id, 100.0);
    }

    /**
     * generate heat and pressures based on working time
     */
    private void generateHeatAndPressure() {

        double heatBase = -0.0537 * pow(workingTime, 5) + 1.3859 * pow(workingTime, 4) - 12.244 * pow(workingTime, 3) + 37.827 * pow(workingTime, 2) + 16.869 * pow(workingTime, 1) + 30.813;
        double pressureBase = 0.0288 * pow(workingTime, 5) - 0.8144 * pow(workingTime, 4) + 8.5125 * pow(workingTime, 3) - 40.468 * pow(workingTime, 2) + 85.293 * pow(workingTime, 1) + 49.056;

        for (int i = 0; i < 4; i++) {
            temp.set(i, heatBase + Math.random() * 20);
            pressure.set(i, pressureBase + Math.random() * 20);
        }

    }


    /**
     * prints out to System.out the current values for the system
     */
    public void printLevels() {
        if (printLevels) {
            for (Double level : wearlevel) {
                System.out.print("Wear " + level.intValue() + " ");
            }
            for (Double temp : temp) {
                System.out.print("Temperature: " + temp.intValue() + " ");
            }
            for (Double pressure : pressure) {
                System.out.print("Pressure: " + pressure.intValue() + " ");
            }
            for (Integer errorCode: errorCodes) {
                System.out.print("error: " + errorCode + " ");
            }

            System.out.println();
        }
    }
}
