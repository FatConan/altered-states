package de.themonstrouscavalca.alteredstates;

public class InternalTransition<S, E>{
    private String label;
    private S state;
    private E event;

    public InternalTransition(S state, E event, String label){
        this.label = label;
        this.state = state;
        this.event = event;
    }

    public InternalTransition(S state, E event){
        this.label = "";
        this.state = state;
        this.event = event;
    }

    public String getLabel(){
        return label;
    }

    public S getState(){
        return state;
    }
    public E getEvent(){
        return event;
    }
}
