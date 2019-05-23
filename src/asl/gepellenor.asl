// Agent sample_agent in project ierHF

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+pos(X1,Y1) : 
	check_sensor(X1,Y2) 		//bool: ha a szenzor rendben van, igazat ad vissza
	<- move_next(X1,Y1).

+pos(X1,Y1) : not check_sensor(X1,Y2) <- .broadcast(tell, machine_critical(X1,Y1)).
/*
+!start : true <- !start.

+!start : sensor(S)	<- !check_sensor(S).

+!check_sensor(S) : is_normal(S) 
		<- !next.
//*/