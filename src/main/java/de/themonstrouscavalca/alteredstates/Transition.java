package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class Transition<S extends INameStates, E extends INameEvents>{
    private String label;
    private StateCollection<S> fromStates;
    private S toState;
    private E event;
    private boolean privileged;

    public Transition(StateCollection<S> fromStates, S toState, E event, String label){
        this.label = label;
        this.privileged = false;
        this.fromStates = fromStates;
        this.toState = toState;
        this.event = event;
    }

    public Transition(StateCollection<S> fromStates, S toState, E event){
        this.label = "";
        this.privileged = false;
        this.fromStates = fromStates;
        this.toState = toState;
        this.event = event;
    }

    public Transition(StateCollection<S> fromStates, S toState, E event, boolean privileged){
        this.label = "";
        this.fromStates = fromStates;
        this.toState = toState;
        this.event = event;
        this.privileged = privileged;
    }

    public Transition(StateCollection<S> fromStates, S toState, E event, String label, boolean privileged){
        this.fromStates = fromStates;
        this.toState = toState;
        this.event = event;
        this.privileged = privileged;
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

    public StateCollection<S> getFromStates(){
        return fromStates;
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
