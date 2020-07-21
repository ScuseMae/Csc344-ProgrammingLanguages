%% ==================================
%% Farmer, Wolf, Goat, Cabbage... 
%% ==================================

%% Travel E -> W

	travel(e,w).

%%Travel W -> E

	travel(w,e).

%% Possible Moves
	
	move([X,X,Goat,Cabbage],wolf,[Y,Y,Goat,Cabbage]) :- travel(X,Y).
	move([X,Wolf,X,Cabbage],goat,[Y,Wolf,Y,Cabbage]) :- travel(X,Y).
	move([X,Wolf,Goat,X],cabbage,[Y,Wolf,Goat,Y]) :- travel(X,Y).
	move([X,Wolf,Goat,Cabbage],nothing,[Y,Wolf,Goat,Cabbage]) :- travel(X,Y).

%% Safe Conditions

	safe([X,_,X,_]). % Goat is on the same bank as farmer.
	safe([X,X,_,X]). % Wolf and cabbage are on the same bank as farmer

	solve([e,e,e,e],[]).
	solve(State,[FirstMove|OtherMoves]) :- move(State, FirstMove, NextState), safe(NextState), solve(NextState, OtherMoves).

%% Version 2 Additions

	not_member(_, []) :- !.

	not_member(X, [Head|Tail]) :- X \= Head, not_member(X, Tail).

	solve2(AnsPath) :- solve2([[w,w,w,w]], AnsPath). 

	solve2([S|_], [S]) :- S = [e,e,e,e], !.

	solve2([S|Path], [S|AnsPath]) :- 
			move(S, _, NextState), safe(NextState), 
    		not_member(NextState,[S|Path]), solve2([NextState, S | Path], AnsPath).


%% ==================================
%% Ancestor Examples... 
%% ==================================

male(tom).
male(jim).
male(alex).

female(marry).
female(betty).
female(dorothy).
female(alice).

parent(tom, jim).
parent(betty, jim).
parent(tom, alex).
parent(mary, alex).
parent(alice, marry).
parent(dorothy, tom).

child(X, Y) :- parent(Y, X).

mother(X, Y) :- parent(X, Y), female(X).

% trace (traces calls)

sibling(X, Y) :- parent(P, X), parent(P, Y), X \= Y.

grandparent(X, Y) :- parent(X, P), parent(P, Y).

cousin(X, Y) :- grandparent(G, X), grandparent(G, Y), X \= Y, \+sibling(X, Y).

ancestor(X, Y, Level) :- parent(X, Y), Level is 1. % base-case
ancestor(X, Y, Level) :- parent(P, Y), ancestor(X, P, L), Level is L + 1.


%% ==================================
%% Assign 4 (lasers) trials... 
%% ==================================

grid(X,Y) :- X >= 1, 
			 X =< 12,
			 Y >= 1,
			 Y =< 10.


door(X,Y) :- X > 4,
			 X < 7,
			 Y >= 1,
			 Y =< 6.

obstacle_1(X,Y) :- X > 2,
				   X < 5,
				   Y >= 1,
				   Y =< 3.

obstacle_2(X,Y) :- X > 9,
				   X < 12,
				   Y >= 1,
				   Y =< 4.

safe(X,Y) :- grid(X,Y), \+door(X,Y), \+obstacle_1, \+obstacle_2.

move(X, Y, X2, Y) :- 
    X2 is X + 1, 
    safe(X2, Y).
move(X, Y, X, Y2) :- 
    Y2 is Y + 1, 
    safe(X, Y2).

path(X, Y, [(X,Y)|Ps]) :- 
    move(X, Y, X2, Y2), 
    path(X1, Y1, Ps).


%% ==================================
%% Assign 4 (lasers) ... 
%% ==================================

grid_position([X, Y]) :- 
    X >= 1,
    X =< 12,
    Y >= 1,
    Y =< 10.

blocked([5,1]).           
blocked([5,2]).
blocked([5,3]).
blocked([5,4]).
blocked([5,5]).
blocked([5,6]).
blocked([6,1]).
blocked([6,2]).
blocked([6,3]).
blocked([6,4]).
blocked([6,5]).
blocked([6,6]).

obstacles([],_,_).
obstacles([H|T],A,B) :- \+obstacle(H,A,B), obstacles(T,A,B).

obstacle([X,Y,Z],A, B) :-
    A > X,
    A =< X+Y,
    B > 10-Z.

allowed_position(Obstacles, [X,Y]) :-
    grid_position([X, Y]),
    \+blocked([X, Y]),
    obstacles(Obstacles,X, Y).

%Keep going right.
next_State(CX, CY, right, CM, NX, CY, right, CM):-
    NX is CX+1.
    
%Keep going up.
next_State (CX, CY,    up, CM, CX, NY, up,   CM):-
    NY is CY+1.

%keep going down.
next_State( CX,  CY,  down, CM, CX, NY, down, CM):-
    NY is CY-1.

%Now go up.
next_State( CX,  CY,  right, CM, CX, NY, up,  [[CX,CY,/]|CM]):-
    NY is CY+1.

%Now go right.
next_State(  CX,  CY,    up,  CM, NX, CY, right, [[CX,CY,/]|CM]):-
    NX is CX+1.

%Now go down.
next_State(   CX,  CY, right,  CM, CX, NY,  down, [[CX,CY,\]|CM]):-
    NY is CY-1.

%Now go right.
next_State(   CX,  CY,   down,  CM, NX, CY, right, [[CX,CY,\]|CM]):-
    NX is CX+1.

place_mirrors(H, Obstacles, X) :-
    place_mirrors(1, H, H, Obstacles, [], right,X).

place_mirrors(12, Y, H, _, CM, right, CM):-
    Y = H,
    length(CM,L),
    L < 9.

place_mirrors(CX, CY, H, Obstacles, CM, Dir, X) :- 
  next_State(CX, CY, Dir, CM, NX, NY, NewDir, NM), 
  allowed_position(Obstacles, [NX, NY]),
  place_mirrors(NX,NY,H,Obstacles,NM,NewDir, X).



