%% Lazer_Grid
grid([X,Y]):- X >= 1,
	          X =< 12,
	          Y >= 1,
	          Y =< 10. 

%% Permanent Obstacle
doorway([X,Y]):- X >= 5,
		         X =< 6,
		         Y >= 1,
		         Y =< 6.

%% Direction Moves
%% Travel East
%%   travel_East(X_2) is (X_1 + 1). 

%% Travel North
%%   travel_North(Y_2) is (Y_1 + 1).

%% Travel South
%%   travel_South(Y_2) is (Y_1 - 1).

%% Redirect East
%%   redirect_East().

%% Redirect North
%%   redirect_North().

%% Redirect South
%%   redirect_South().

move(X_1, Y_1, east, Mirror, X_2, Y_1, east, Mirror):- X_2 is X_1+1.
move(X_1, Y_1, north, Mirror, X_1, Y_2, north, Mirror):- Y_2 is Y_1+1.
move(X_1, Y_1, south, Mirror, X_1, Y_2, south, Mirror):- Y_2 is Y_1-1.

move(X_1, Y_1, east, Mirror, X_1, Y_2, north, [[X_1, Y_1, /] | Mirror]):- Y_2 is Y_1+1.
move(X_1, Y_1, north, Mirror, X_2, Y_1, east, [[X_1, Y_1, /] | Mirror]):- X_2 is X_1+1.
move(X_1, Y_1, east, Mirror, X_1, Y_2, south, [[X_1, Y_1, \] | Mirror]):- Y_2 is Y_1-1.
move(X_1, Y_1, south, Mirror, X_2, Y_1, east, [[X_1, Y_1, \] | Mirror]):- X_2 is X_1+1.

%% Test Obstacles (:- dynamic ?)
obstacle_1([X,Y]) :- X >= 2,
	 	   X =< 4,
		   Y >= 8,
		   Y =< 10.

obstacle_2([X,Y]) :- X >= 10,
		   X =< 11,
		   Y >= 7,
		   Y =< 10.

%% Into list
block([],_,_).

%% Unification: First bound to X, Second bound to Y 
block([First|Second], X, Y):- \+obstacles(First, X, Y),
			 	  			  block(Second, X, Y).

%% Begins = X, [Width,Height] = Y
obstacles([Begins, Width, Height], X, Y):- X > Begins,
			   		  				     X =< Begins + Width,
			    			 	     	 Y > 10 - Height.

safe_moves(Block, [X,Y]):- grid([X,Y]),
						   \+doorway([X,Y]),
						   block(Block, X, Y). 


%% Possible states (Current_Coordinates, Direction, Mirror_Placement, Resulting_Coordinates, Direction, Mirror_Placement) + Direction
%% transition_1(X_1, Y_1, east, Mirror, X_2, Y_1, east, Mirror) :- X_1+1.
%% transition_2(X_1, Y_1, north, Mirror, X_1, Y_2, north, Mirror) :- Y_1+1.
%% transition_3(X_1, Y_1, south, Mirror, X_1, Y_2, south, Mirror) :- Y_1-1.

%% transition_4(X_1, Y_1, east, Mirror, X_1, Y_2, north, [[X1, Y1, /] | Mirror]) :- Y_1+1.
%% transition_5(X_1, Y_1, north, Mirror, X_2, Y_1, east, [[X1, Y1, /] | Mirror]) :- X_1+1.
%% transition_6(X_1, Y_1, east, Mirror, X_1, Y_2, south, [[X1, Y1, \] | Mirror]) :-  Y_1-1.
%% transition_7(X_1, Y_1, south, Mirror, X_2, Y_1, east, [[X1, Y1, \] | Mirror]) :- X_1+1.



% laser_placement([X,Y]):- read([X,Y]),
%			  laser_placement is [X,Y].

safe([X,Y]) :- grid([X,Y]), 
	       \+doorway([X,Y]), 
	       \+obstacle_1([X,Y]), 
	       \+obstacle_2([X,Y]).

move(X, Y, X2, Y):- X2 is X + 1, 
    		        safe(X2, Y).

move(X, Y, X, Y2):- Y2 is Y + 1, 
    		        safe(X, Y2).

path(X, Y, [(X,Y)|Ps]):- move(X, Y, X2, Y2), 
  			  		     path(X1, Y1, Ps).

mirrors(First, Block, Begins):- mirrors(1, First, First, Block, [], east, Begins).

mirrors(12, Width, First, _, Mirror, east, Mirror):- Width = First,
							  						 length(Mirror, Length),
							  						 Length < 9.

mirrors(X_1, Y_1, First, Block, Mirror, Direction, Begins):-
	move(X_1, Y_1, Direction, Mirror, X_2, Y_2, Redirect, Mirrors_2),
	safe_moves(Block, [X_2,Y_2]),
	mirrors(X_2, Y_2, First, Block, Mirrors_2, Redirect, Begins).


%% mirrors(3, [[2,2,3],[9,2,4]], X).









