// Agent sample_agent in project ierHF

/* Initial beliefs and rules */

pos(1,0).
last_dir(null). // the last movement I did

/* Initial goals */

//!start.

/* Plans */

+pos(X1,Y1) : 
	check_sensor(X1,Y2) 		//bool: ha a szenzor rendben van, igazat ad vissza
	<- move_next(X1,Y1).

+pos(X1,Y1) : not check_sensor(X1,Y2) <- .broadcast(tell, machine_critical(X1,Y1)).

/* Moving */
+!pos(X,Y) : pos(X,Y) <- .print("I've reached ",X,"x",Y).
+!pos(X,Y) : not pos(X,Y)
  <- !next_step(X,Y);
     !pos(X,Y).

+!next_step(X,Y)
   :  pos(AgX,AgY)
   <- jia.get_direction(AgX, AgY, X, Y, D);
      //.print("from ",AgX,"x",AgY," to ", X,"x",Y," -> ",D);
      -+last_dir(D).
    //  do(D).
+!next_step(X,Y) : not pos(_,_) // I still do not know my position
   <- !next_step(X,Y).
-!next_step(X,Y) : true  // failure handling -> start again!
   <- .print("Failed next_step to ", X,"x",Y," fixing and trying again!");
      -+last_dir(null);
      !next_step(X,Y).




/*
+!start : true <- !start.

+!start : sensor(S)	<- !check_sensor(S).

+!check_sensor(S) : is_normal(S) 
		<- !next.
//*/