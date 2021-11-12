package de.themonstrouscavalca.alteredstates;

public class InternalTransition<S, E>{
    private S state;
    private E event;

    public InternalTransition(S state, E event){
        this.state = state;
        this.event = event;
    }

    public S getState(){
        return state;
    }

    public E getEvent(){
        return event;
    }
}
