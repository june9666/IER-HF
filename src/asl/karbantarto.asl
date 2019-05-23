// Agent sample_agent in project ierHF

/* Initial beliefs and rules */

pos(rep_station,0,0).

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("hello world.").

+machine_critical(X1,Y1) : true <- !repair_machine(X1,Y1).

+!repair_machine(X1,Y1): true <- 
		!go(X1,Y1);
		!rep;
		!go_back.
		
+!go(X1,Y1) : pos(Xl,Yl) & my_pos
	<- true.
+!go(X1,Y1) : true
	<- ?pos(Xl,Yl);
	moveTowards(Xl,Yl);
	!go(X1,Y1).
	
+!rep : true <- repair.		//src/java/.../.repair

+!go_back : pos(rep_station,Xl,Yl) & pos(r1,X1,Y1)
	<- true.
+!go_back : true
	<- ?pos(rep_station,Xl,Yl);
	moveTowards(Xl,Yl);
	!go_back.

