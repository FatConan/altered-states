package de.themonstrouscavalca.alteredstates;

public class InternalTransition<S, E>{
    private String label;
    private S state;
    private E event;
    private boolean privileged;

    public InternalTransition(S state, E event, String label){
        this.label = label;
        this.state = state;
        this.event = event;
        this.privileged = false;
    }

    public InternalTransition(S state, E event, String label, boolean privileged){
        this.label = label;
        this.state = state;
        this.event = event;
        this.privileged = privileged;
    }

    public InternalTransition(S state, E event, boolean privileged){
        this.label = "";
        this.state = state;
        this.event = event;
        this.privileged = privileged;
    }

    public InternalTransition(S state, E event){
        this.label = "";
        this.state = state;
        this.event = event;
        this.privileged = false;
    }

    public String getLabel(){
        return label;
    }

    public boolean isPrivileged(){
        return privileged;
    }

    public S getState(){
        return state;
    }
    public E getEvent(){
        return event;
    }
}
