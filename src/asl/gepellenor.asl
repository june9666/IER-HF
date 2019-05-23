// Agent sample_agent in project ierHF

/* Initial beliefs and rules */

pos(0,1).
last_dir(null). // the last movement I did
free.
/* Initial goals */
!pos(3,3).

/* plans for sending the initial position to leader */

+gsize(S,_,_) : true // S is the simulation Id 
  <- !send_init_pos(S).
+!send_init_pos(S) : pos(X,Y)
  <- .send(leader,tell,init_pos(S,X,Y)).
+!send_init_pos(S) : not pos(_,_) // if I do not know my position yet
  <- .wait("+pos(X,Y)", 500);     // wait for it and try again
     !!send_init_pos(S).
     
//!start.

/* Plans */

+pos(3,3).


+pos(X1,Y1) : 
	free 		//bool: ha a szenzor rendben van, igazat ad vissza
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
      do(D).
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