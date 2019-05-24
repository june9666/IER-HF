

/* Initial beliefs and rules */

last_dir(null). // the last movement I did
~hazamegy.

/* Initial goals */
!wait.

/* Plans */
+!wait : true <- !!wait.

/* karbantartásra van szükség */
+repair(X,Y)[source(gepellenor1)] : ~hazamegy
  <-!pos(X,Y);
  .print("repair");
  jia.repairPress(X,Y);
  -repair(X,Y)[source(gepellenor1)];
  +hazamegy;
  !pos(2,5);
  -hazamegy;
   .print("sending done");
  .broadcast(tell,mehetTovabb).
     
     
/* Moving */
+!pos(X,Y) : pos(X,Y) <- .print("I've reached ",X,"x",Y).

+!pos(X,Y) : not pos(X,Y)
  <- !next_step(X,Y);
     !pos(X,Y).
    
+!next_step(X,Y)
   :  pos(AgX,AgY)
   <- jia.get_direction(AgX, AgY, X, Y, D);
      -+last_dir(D);   
      do(D).
  
+!next_step(X,Y) : not pos(_,_) // I still do not know my position
   <- !next_step(X,Y);
   .print("nextStepnotpos")
   .
   
-!next_step(X,Y) : true  // failure handling -> start again!
   <- .print("Failed next_step to ", X,"x",Y," fixing and trying again!");
      -+last_dir(null);
      !next_step(X,Y).

