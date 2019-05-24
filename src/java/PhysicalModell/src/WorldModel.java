package PhysicalModell.src;

import jason.asSyntax.Literal;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import PhysicalModell.src.IntelligentFactory.Move;

public class WorldModel extends GridWorldModel {

    public static final int   DEPOT = 32;
    public static final int   SENSOR = 128;
    public static final int   FIRE = 256;
    
    Location                  depot;
    Location				  sensor1;
    Location				  sensor2;
    Location				  sensor3;
    Location				  sensor4;
    int                       goldsInDepot   = 0;
    int                       initialNbGolds = 0;

    private Logger            logger   = Logger.getLogger("jasonTeamSimLocal.mas2j." + WorldModel.class.getName());

    private String            id = "WorldModel";
    
    // singleton pattern
    protected static WorldModel model = null;
    
    synchronized public static WorldModel create(int w, int h, int nbAgs) {
    	
    	
        if (model == null) {
            model = new WorldModel(w, h, nbAgs);
        }
        return model;
    }
    
    public static WorldModel get() {
        return model;
    }
    
    public static void destroy() {
        model = null;
    }

    private WorldModel(int w, int h, int nbAgs) {
        super(w, h, nbAgs);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String toString() {
        return id;
    }
    
    public Location getDepot() {
        return depot;
    }



    public void setDepot(int x, int y) {
        depot = new Location(x, y);
        data[x][y] = DEPOT;
    }

  
    public void setSensors() {
    	sensor1 = new Location(1,2);
    	sensor2 = new Location(1,4);
    	sensor3 = new Location(1,6);
    	sensor4 = new Location(1,8);
    	data[1][2] = SENSOR;
   	data[1][4] = SENSOR;
    	data[1][6] = SENSOR;
   	data[1][8] = SENSOR;
    }
    public Location getSensor(int i) {
    	logger.info("called");
    	switch (i) {
    	case 1:    	logger.info("called1" + sensor1.x); return sensor1; 
    	case 2: return sensor2;
    	case 3: return sensor3;
    	case 4: return sensor4;
    	}
    	return null;
    	
    }
    /** Actions **/

    boolean move(Move dir, int ag) throws Exception {
        Location l = getAgPos(ag);
        switch (dir) {
        case UP:
            if (isFree(l.x, l.y - 1)) {
                setAgPos(ag, l.x, l.y - 1);
            }
            break;
        case DOWN:
            if (isFree(l.x, l.y + 1)) {
                setAgPos(ag, l.x, l.y + 1);
            }
            break;
        case RIGHT:
            if (isFree(l.x + 1, l.y)) {
                setAgPos(ag, l.x + 1, l.y);
            }
            break;
        case LEFT:
            if (isFree(l.x - 1, l.y)) {
                setAgPos(ag, l.x - 1, l.y);
            }
            break;
        }
        return true;
    }

    boolean pick(int ag) {
        Location l = getAgPos(ag);
        if (hasObject(WorldModel.FIRE, l.x, l.y)) {
        	IntelligentFactory.removeFire = new Location(l.x,l.y);
                remove(WorldModel.FIRE, l.x, l.y);
               
                return true;
        }
        return false;
    }
  
    
    /**
     * default world
     * @return
     * @throws Exception
     */
    static WorldModel world1() throws Exception {
        WorldModel model = WorldModel.create(10, 10, 3);
        model.setDepot(0, 0);
        model.setDepot(2, 5);
        model.setDepot(6, 1);
        model.setAgPos(0, 1, 0);
        model.add(WorldModel.SENSOR, 1,2 );
        model.add(WorldModel.SENSOR, 1,4 );
        model.add(WorldModel.SENSOR, 1,6 );
        model.add(WorldModel.SENSOR, 1,8 );
        model.setAgPos(1, 2, 5);
        model.setAgPos(2, 6,1);
        model.setSensors();
        return model;
    }
}
