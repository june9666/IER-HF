package PhysicalModell.src;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.logging.Logger;

import busca.AEstrela;
import busca.Busca;
import busca.Estado;
import busca.Nodo;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Atom;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import jason.environment.grid.Location;
 
public class Press {

  public ArrayList<Double> temp; //Temperature sensor in Celsius
  public ArrayList<Double> pressure; //Pressure sensor in Bar
  public  ArrayList<Double> wearlevel; //Wear level 0-100
  public  ArrayList<Integer> errorCodes;
    /**
     * fire, explode, or component stop
     */
    static double ERROR_CODE_FIRE = 1;
    static double ERROR_CODE_EXPLODE = 2;
    static double ERROR_CODE_STOP = 3;

    double workingTime; // Time based values
    int errorTime;
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
        errorTime = 0;
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
        doTime();
        
    }

    public  void zeroizeError(){
    	for(int i = 0 ; i<=3; i++) {
    		errorCodes.set(i,0);
    	}
    }
    /**
     * increase the timer
     */
    private void doTime() {
    	Thread runit = new Thread() {
    		public void run() {
    	
    	 while(true) {
             try {
                 Thread.sleep(100);
                 increaseTime();
             //    System.out.print("Wear " + wearlevel.get(0).intValue() + " ");
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    		}
     };
     runit.start();
    }
    	
    

    /**
     * setting failiure rate based on elapsedWorkingTime
     *
     * @return
     */
    private double sethibaVal() {
   

    			
        double var = -0.000000007 * pow(workingTime, 6) + 0.000006 * pow(workingTime, 5) + 0.000006 * pow(workingTime, 4)
                - 0.0046 * pow(workingTime, 3) + 0.0784 * pow(workingTime, 2) + 0.4284* workingTime + 0.8846;
       
        if (var >0.9) //error rate max is 0.9
            return 0.9;
        return var;
    }

    /**
     * dynamic model needs time value
     */
    public void increaseTime() {
        workingTime += 0.1;
        errorTime +=1;
        hardwareErrorRate = sethibaVal();
        generateError();
        generateHeatAndPressure();
     //   printLevels();
    }

    /**
     * Simulate a system failiure based on error rate
     */
    private void generateError() {
        double errorRand = Math.random() * hardwareErrorRate; //random number, to control if error occurs or not
     //   System.out.println(hardwareErrorRate);
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
    	
    	double errorRand = Math.random() * hardwareErrorRate;
    	errorTime++;
    	if (errorRand > 0.88) {

    		if(errorTime > 100)
        errorCodes.set(generateRandomSensorID(), (int) ERROR_CODE_FIRE);
    	errorTime =0;
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
        if(wearlevel.get(id) - minus <3) {
        	wearlevel.set(id, wearlevel.get(id) - 4);
        }else {
        wearlevel.set(id, wearlevel.get(id) - minus);
        }
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
        System.out.println(wearlevel.get(id).intValue());
    }

    /**
     * generate heat and pressures based on working time
     */
    private void generateHeatAndPressure() {

        double heatBase = 0.0004 * pow(workingTime, 3) - 0.0924 * pow(workingTime, 2) + 7.829 * pow(workingTime, 1) + 21.224;

        double pressureBase =  0.0003 * pow(workingTime, 3) - 0.0646 * pow(workingTime, 2) +3.4848 * pow(workingTime, 1) + 59.231;

        for (int i = 0; i < 4; i++) {
        	
        	if (heatBase < 300 && heatBase > 15) {
        		temp.set(i, heatBase + Math.random() * 20);
        	}else {
        		temp.set(i, 300.0);
        	}
            
        	if (pressureBase < 300 && pressureBase > 15) {
        		pressure.set(i, pressureBase + Math.random() * 20);
        	}else {
        		pressure.set(i,300.0);
        	}
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
