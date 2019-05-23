package jia;

import java.util.logging.Logger;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import PhysicalModell.src.*;

/**
 * if the press sensor is good, return true -> no need to repair
 * if return false you need to repair
 * @author jeno96
 *
 */
public class getPress   extends DefaultInternalAction{
    private Logger          logger   = Logger.getLogger("jasonTeamSimLocal.mas2j." + getPress.class.getName());
	
	 @Override
	    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		 
		 	
		    
            int X = (int)((NumberTerm)terms[0]).solve();
            int Y = (int)((NumberTerm)terms[1]).solve();
            
            switch (Y) {
            	case 2: 
            		logger.info("level0 " + MiningPlanet.press.wearlevel.get(0).intValue());
            if(MiningPlanet.press.wearlevel.get(0).intValue() <90) {
 
			return false;
            }else {
            	return true;
            }
            	case 4: 	logger.info("level1 " + MiningPlanet.press.wearlevel.get(1).intValue());
            if(MiningPlanet.press.wearlevel.get(1).intValue() <70) {
        
			return false;
            }else {
            	return true;
            }
            	case 6: 	logger.info("level2 " + MiningPlanet.press.wearlevel.get(2).intValue());
            if(MiningPlanet.press.wearlevel.get(2).intValue() <70) {
			return false;
            }else {
            	return true;
            }
            	case 8: 	logger.info("level3 " + MiningPlanet.press.wearlevel.get(3).intValue());
            if(MiningPlanet.press.wearlevel.get(3).intValue() <70) {
			return false;
            }else {
            	return true;
            }
            
	    }
            return false;
	 }
	 
	
}
