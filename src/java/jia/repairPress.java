package jia;

import java.util.logging.Logger;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Term;
import PhysicalModell.src.*;

public class repairPress   extends DefaultInternalAction{
    private Logger          logger   = Logger.getLogger("jasonTeamSimLocal.mas2j." + repairPress.class.getName());
	
	 @Override
	    public Object execute(TransitionSystem ts, Unifier un, Term[] terms) throws Exception {
		 
		 	
		    
		  int X = (int)((NumberTerm)terms[0]).solve();
          int Y = (int)((NumberTerm)terms[1]).solve();
          
          switch (Y) {
          	case 2: 
          		IntelligentFactory.press.repair(0, 100000); 
          		break;
          
          	case 4: 
          		IntelligentFactory.press.repair(1, 200000); break;
          
          	case 6: 
          		IntelligentFactory.press.repair(2, 100000); break;
          
          	case 8: 
          		IntelligentFactory.press.repair(3, 300000); break;
	    }
          return true;
	
}
}
