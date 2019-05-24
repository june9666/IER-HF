

/* Initial beliefs and rules */

last_dir(null). // the last movement I did
tuzoltasVege.

/* Initial goals */
!wait.
/* Plans */
+!wait : true <- !!wait.

/* tûz esemény érkezett, tûzoltásra van szükség */
+tuzoltas(X,Y)[source(gyarfelugy4)] : tuzoltasVege
  <-.print("tuzoltas");
  -tuzoltasVege;
  !pos(X,Y);
  do(pick);
  !pos(6,1);
  -tuzoltas(X,Y)[source(gyarfelugy4)];
  +tuzoltasVege.
     
     
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
   <- !next_step(X,Y).
   
-!next_step(X,Y) : true  // failure handling -> start again!
   <- .print("Failed next_step to ", X,"x",Y," fixing and trying again!");
      -+last_dir(null);
      !next_step(X,Y).
