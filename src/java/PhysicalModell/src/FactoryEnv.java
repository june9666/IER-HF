package PhysicalModell.src;

public class FactoryEnv extends jason.environment.Environment {

	@Override
	public void init(String[] args) {
		
    }
	/*
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
            } else if (action.equals(drop)) {
                result = model.drop(agId);
                view.udpateCollectedGolds();
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
	//*/
}
