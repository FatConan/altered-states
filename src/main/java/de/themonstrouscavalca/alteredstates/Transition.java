package de.themonstrouscavalca.alteredstates;

public class Transition<S, E>{
    private String label;
    private S fromState;
    private S toState;
    private E event;
    private boolean privileged;

    public Transition(S fromState, S toState, E event, String label){
        this.label = label;
        this.privileged = false;
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
    }

    public Transition(S fromState, S toState, E event){
        this.label = "";
        this.privileged = false;
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
    }

    public Transition(S fromState, S toState, E event, boolean privileged){
        this.label = "";
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
        this.privileged = privileged;
    }

    public Transition(S fromState, S toState, E event, String label, boolean privileged){
        this.fromState = fromState;
        this.toState = toState;
        this.event = event;
        this.privileged = privileged;
        this.label = label;
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

    public boolean isPrivileged(){
        return privileged;
    }
}
