package de.themonstrouscavalca.alteredstates.helpers.transitions;

import de.themonstrouscavalca.alteredstates.transitions.InternalTransition;
import de.themonstrouscavalca.alteredstates.transitions.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.List;

public class Transitions<S extends INameStates, E extends INameEvents, C, X>{
    private List<Transition<S, E, C, X>> externalTransitions;
    private List<InternalTransition<S, E>> internalTransitions;

    public Transitions(List<Transition<S, E, C, X>> externalTransitions,
                       List<InternalTransition<S, E>> internalTransitions){
        this.externalTransitions = externalTransitions;
        this.internalTransitions = internalTransitions;
    }

    public List<Transition<S, E, C, X>> getExternalTransitions(){
        return externalTransitions;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return internalTransitions;
    }
}
