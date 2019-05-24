package PhysicalModell.src;

import jason.asSyntax.DefaultTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.environment.grid.Location;

import java.util.logging.Level;
import java.util.logging.Logger;

public class IntelligentFactory extends jason.environment.Environment {

    private Logger          logger   = Logger.getLogger("jasonTeamSimLocal.mas2j." + IntelligentFactory.class.getName());
    
    public  WorldModel  model;
    WorldView   view;
    public static Press press;
    public static Location fire = null;
    public static int fireNumber =0;
    int     simId    = 3; // type of environment
    int     nbWorlds = 1;
    int     sleep    = 0;
    boolean running  = true;
    boolean hasGUI   = true;
    public static Location removeFire = null;
    
    public static final int SIM_TIME = 60;                                                                         // in
   
    Term                    up       = DefaultTerm.parse("do(up)");
    Term                    down     = DefaultTerm.parse("do(down)");
    Term                    right    = DefaultTerm.parse("do(right)");
    Term                    left     = DefaultTerm.parse("do(left)");
    Term                    skip     = DefaultTerm.parse("do(skip)");
    Term                    pick     = DefaultTerm.parse("do(pick)");

    /**irányok
     * 
     * @author jeno96
     *
     */
    public enum Move {
        UP, DOWN, RIGHT, LEFT
    };

    public IntelligentFactory getPlanet() {
    	return this;
    }
    @Override
	public void init(String[] args) {
    	  press = new Press();
        hasGUI = args[2].equals("yes"); 
        sleep  = Integer.parseInt(args[1]);
        initWorld(Integer.parseInt(args[0]));
    }
    
    public int getSimId() {
        return simId;
    }
    
    public void setSleep(int s) {
        sleep = s;
    }

    @Override
	public void stop() {
        running = false;
        super.stop();
    }

   
    /**
     * az érkezõ akciótól függõen hajtunk végre utasításokat
     */
    @Override
    public boolean executeAction(String ag, Structure action) {
        boolean result = false;
        try {
            if (sleep > 0) {
                Thread.sleep(sleep);
            }
            
            // get the agent id based on its name
            int agId = getAgIdBasedOnName(ag);

            if (action.equals(up)) {
                result = model.move(Move.UP, agId);
            } else if (action.equals(down)) {
                result = model.move(Move.DOWN, agId);
            } else if (action.equals(right)) {
                result = model.move(Move.RIGHT, agId);
            } else if (action.equals(left)) {
                result = model.move(Move.LEFT, agId);
            } else if (action.equals(skip)) {
                result = true;
            } else if (action.equals(pick)) {
                result = model.pick(agId);
            } else {
                logger.info("executing: " + action + ", but not implemented!");
            }
            if (result) {
                updateAgPercept(agId);
                return true;
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            logger.log(Level.SEVERE, "error executing " + action + " for " + ag, e);
        }
        return false;
    }

    /** lekérjük az ágens nevét az id alapján
     * 
     * @param agName
     * @return
     */
    private int getAgIdBasedOnName(String agName) {
        return (Integer.parseInt(agName.substring(10))) - 1;
    }
    
    /** világ létrehozása
     * 
     * @param w
     */
    public void initWorld(int w) {
    	try {
    	simId = w;
	        switch (w) {
	        case 1: try {
					model = WorldModel.world1();
				//	logger.info(model.getId() + " world1 id");
				} catch (Exception e1) {
					logger.warning("error creating world1" + e1.getMessage());
				} break;
	        default:
	            logger.info("Invalid index!");
	            return;
	        }
	        
	       // perceptek hozzáadása
           clearPercepts();
           addPercept(Literal.parseLiteral("gsize(" + simId + "," + model.getWidth() + "," + model.getHeight() + ")"));
           addPercept(Literal.parseLiteral("depot(" + simId + "," + model.getDepot().x + "," + model.getDepot().y + ")"));
           addPercept(Literal.parseLiteral("sensor1(" + simId + "," + model.getSensor(1).x + "," + model.getSensor(1).y + ")"));
           addPercept(Literal.parseLiteral("sensor2(" + simId + "," + model.getSensor(2).x + "," + model.getSensor(2).y + ")"));
           addPercept(Literal.parseLiteral("sensor3(" + simId + "," + model.getSensor(3).x + "," + model.getSensor(3).y + ")"));
           addPercept(Literal.parseLiteral("sensor4(" + simId + "," + model.getSensor(4).x + "," + model.getSensor(4).y + ")"));
       
            if (hasGUI) {
                view = new WorldView(model);
                view.setEnv(this);
            }
            updateAgsPercept();        
            
            	informAgsEnvironmentChanged();
            	} 
    	catch (Exception e) {
    		logger.warning("Error creating world "+e);
    	}
    }
    
    
    /**
     * szimuláció vége
     */
    public void endSimulation() {
        addPercept(Literal.parseLiteral("end_of_simulation(" + simId + ",0)"));
        informAgsEnvironmentChanged();
        if (view != null) view.setVisible(false);
        WorldModel.destroy();
    }

    private void updateAgsPercept() {
        for (int i = 0; i < model.getNbOfAgs(); i++) {
            updateAgPercept(i);
        }
    }

    private void updateAgPercept(int ag) {
       
    	if(ag == 0) updateAgPercept("gepellenor" + (ag + 1), ag);
    	if(ag == 1) updateAgPercept("gepkarbant" + (ag + 1), ag);
    	if(ag == 2) updateAgPercept("tuztuzolto" + (ag + 1), ag);
    	if(ag == 3) updateAgPercept("gyarfelugy" + (ag + 1), ag);
    	 if(model.getAgPos(2).x == 6 &&  model.getAgPos(2).y == 1 && fire == null) {
             IntelligentFactory.fireNumber = 0;
             }
    	
    	
    }
    /**
     * a fizikai modell alapján lekérjük a tûzeseteket
     */
    private void getFireErrors() {
    	
    	for(int i = 0; i<= 3; i++) {
    	if (press.errorCodes.get(i) == 1 && fireNumber == 0  && model.getAgPos(2).x == 6 &&  model.getAgPos(2).y == 1 && fire == null) {
            IntelligentFactory.fireNumber = 0;
    		fire = getFireRandomLocation();
    		model.add(WorldModel.FIRE, fire);
    		  IntelligentFactory.fireNumber++;
    		IntelligentFactory.press.zeroizeError();
            } 
    	} 
    }
    	
    private int getRndInteger(int min, int max) {
    	  return (int) (Math.floor(Math.random() * (max - min + 1) ) + min);
    	}
    /**
     * egy adott tartmományon belül gyullad ki
     * @return
     */
    private Location getFireRandomLocation(){
    	int y =  getRndInteger(5,9);
    	int x = getRndInteger(5,9);
    	
    	return new Location(x,y);
    }
    private void updateAgPercept(String agName, int ag) {
       clearPercepts(agName);
       getFireErrors();
    	   if(fire !=null) {
    		   addPercept(Literal.parseLiteral("tuz(" + simId + "," + fire.x + "," + fire.y + ")"));
    		   System.out.println("fireAdded");
    		   fire = null;
    	   }
    	   if(removeFire !=null) {
    		   removePercept(Literal.parseLiteral("tuz(" + simId + "," + removeFire.x + "," + removeFire.y + ")"));
    		   removeFire = null;
    	   }
        Location l = model.getAgPos(ag);
        addPercept(agName, Literal.parseLiteral("pos(" + l.x + "," + l.y + ")"));
       
        // what's around
        updateAgPercept(agName, l.x - 1, l.y - 1);
        updateAgPercept(agName, l.x - 1, l.y);
        updateAgPercept(agName, l.x - 1, l.y + 1);
        updateAgPercept(agName, l.x, l.y - 1);
        updateAgPercept(agName, l.x, l.y);
        updateAgPercept(agName, l.x, l.y + 1);
        updateAgPercept(agName, l.x + 1, l.y - 1);
        updateAgPercept(agName, l.x + 1, l.y);
        updateAgPercept(agName, l.x + 1, l.y + 1);
    }

    
    private void updateAgPercept(String agName, int x, int y) {
        if (model == null || !model.inGrid(x,y)) return;
            if (model.hasObject(WorldModel.AGENT, x, y)) {
                addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",ally)"));
            }
            if (model.hasObject(WorldModel.SENSOR, x, y)) {
                addPercept(agName, Literal.parseLiteral("cell(" + x + "," + y + ",sensor)"));
            }
        }
	public static void removePeercent(Literal parseLiteral) {
	}
}
