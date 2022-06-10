package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class InternalTransition<S extends INameStates, E extends INameEvents>{
    private String label;
    private StateCollection<S> states;
    private E event;
    private boolean privileged;

    public InternalTransition(StateCollection<S> states, E event, String label){
        this.label = label;
        this.states = states;
        this.event = event;
        this.privileged = false;
    }

    public InternalTransition(StateCollection<S> states, E event, String label, boolean privileged){
        this.label = label;
        this.states = states;
        this.event = event;
        this.privileged = privileged;
    }

    public InternalTransition(StateCollection<S> states, E event, boolean privileged){
        this.label = "";
        this.states = states;
        this.event = event;
        this.privileged = privileged;
    }

    public InternalTransition(StateCollection<S> states, E event){
        this.label = "";
        this.states = states;
        this.event = event;
        this.privileged = false;
    }

    public String getLabel(){
        return label;
    }

    public boolean isPrivileged(){
        return privileged;
    }

    public StateCollection<S> getStates(){
        return states;
    }
    public E getEvent(){
        return event;
    }
}
