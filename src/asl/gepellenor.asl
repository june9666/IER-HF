/* Initial beliefs and rules */

pos(0,1).
last_dir(null). // the last movement I did
nemkelljavitas.

/* Initial goals */
!checkSensor1.

/* Plans */

+!checkSensor1:  nemkelljavitas <- ?sensor1(_,X,Y);
 !pos(X,Y);
 !checkThis(X,Y);
 !!checkSensor2.


+!checkSensor2:  nemkelljavitas <- ?sensor2(_,X,Y);
 !pos(X,Y);
 !checkThis(X,Y);
 !!checkSensor3.
 
 
+!checkSensor3:  nemkelljavitas <- ?sensor3(_,X,Y);
 !pos(X,Y);
 !checkThis(X,Y);
 !!checkSensor4.
 
 
 +!checkSensor4:  nemkelljavitas <- ?sensor4(_,X,Y);
 !pos(X,Y);
 !checkThis(X,Y);
 !!checkSensor1.
 
 
 +!checkSensor4:  true <- .print("waiting").
 +!checkSensor1: true <-.print("waiting").
 +!checkSensor2:  true <-.print("waiting").
 +!checkSensor3:  true <- .print("waiting").


/* az adott szenzor adatainak lekérése */
+!checkThis(X,Y) : jia.getPress(X,Y) <- true.
+!checkThis(X,Y) : true <- .print("Javításra szorul" , Y);
!pos(0,0);
.broadcast(tell, repair(X,Y));
-nemkelljavitas.

/*ha az ha a karbantartó végzett, akkor mehet az ellenõrzés */
+mehetTovabb[source(gepkarbant2)] : true <- .print("karbantarto sent done");
+nemkelljavitas;
-mehetTovabb[source(gepkarbant2)];
!checkSensor1.

/* Moving */

+!pos(X,Y) : pos(X,Y) <- true.

+!pos(X,Y) : pos(X,Y) <- .print("I've reached ",X,"x",Y).

+!pos(X,Y) : not pos(X,Y) & nemkelljavitas <- !next_step(X,Y);
!pos(X,Y).
+!next_step(X,Y)
:  pos(AgX,AgY)
<- jia.get_direction(AgX, AgY, X, Y, D);
-+last_dir(D);
do(D).
  
+!next_step(X,Y) : not pos(_,_) // I still do not know my position
<- !next_step(X,Y).
   
-!next_step(X,Y) : true  // failure handling -> start again!
<- .print("Failed next_step to ", X,"x",Y," fixing and trying again!");
-+last_dir(null);
!next_step(X,Y).

