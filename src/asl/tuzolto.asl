// Agent sample_agent in project ierHF

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+fire(X1,Y1) : true <- !extinguish_fire(X1,Y1).

+!extinguish_fire(X1,Y1): true <- 
		!go(X1,Y1);
		!ext_fire;
		!go_back.
		
+!go(X1,Y1) : pos(Xl,Yl) & my_pos
	<- true.
+!go(X1,Y1) : true
	<- ?pos(Xl,Yl);
	moveTowards(Xl,Yl);
	!go(X1,Y1).
	
+!ext_fire : true <- extinguishing.
