package de.themonstrouscavalca.alteredStates;

public class Transition<S, E>{
    private S fromState;
    private S toState;
    private E event;

    public Transition(S fromState, S toState, E event){
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
    }

    public S getFromState(){
        return fromState;
    }

    public S getToState(){
        return toState;
    }

    public E getEvent(){
        return event;
    }
}
