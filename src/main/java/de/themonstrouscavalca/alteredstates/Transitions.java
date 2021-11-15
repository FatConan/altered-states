package de.themonstrouscavalca.alteredstates;

import java.util.List;

public class Transitions<S, E>{
    private List<Transition<S,E>> externalTransitions;
    private List<InternalTransition<S, E>> internalTransitions;

    public Transitions(List<Transition<S, E>> externalTransitions, List<InternalTransition<S, E>> internalTransitions){
        this.externalTransitions = externalTransitions;
        this.internalTransitions = internalTransitions;
    }

    public List<Transition<S, E>> getExternalTransitions(){
        return externalTransitions;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return internalTransitions;
    }
}
