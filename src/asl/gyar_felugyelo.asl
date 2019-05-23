// Agent sample_agent in project ierHF

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+pos(X1,Y1) : 
	check_sector(X1,Y2) 		//bool: ha a szenzor rendben van, igazat ad vissza
	<- move_next(X1,Y1).

+pos(X1,Y1) : not check_sector(X1,Y2) <- .broadcast(tell, fire(X1,Y1)).