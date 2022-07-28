# altered-states
A library for representing state machines. Altered states is designed to represent states and transitions that match the 
following patterns:

* Simple state machines with fully defined states and state A to state B transitions
* Complex state machines with fully defined states and state A, B, C etc. to state D transitions
* Dynamic state machines with generated states A, B< C etc. to an instanced on the fly state D transitions


## Build
Build with `sbt clean compile` deploy to a local repository with `sbt clean publishLocal`

## Usage
**alteredstates** is designed to provide a framework and helpers to support the
construction of basic state machines. Primarily this is provided through
the StateMachine and StateMachineBuilder classes.


