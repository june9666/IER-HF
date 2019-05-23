


!wait.

 
+!wait: true <- !!wait.
/*.print("itt",X,Y);
.broadcast(tell,tuzoltas(X,Y)).
*/

+tuz(_,X,Y) : true <- .print("kaptam tuzet");
.broadcast(tell,tuzoltas(X,Y));.


