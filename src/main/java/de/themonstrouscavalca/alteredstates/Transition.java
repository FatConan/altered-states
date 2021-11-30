package de.themonstrouscavalca.alteredstates;

public class Transition<S, E>{
    private String label;
    private S fromState;
    private S toState;
    private E event;

    public Transition(S fromState, S toState, E event, String label){
        this.label = label;
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
    }

    public Transition(S fromState, S toState, E event){
        this.label = "";
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
    }

    public String getLabel(){
        return label;
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
