package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.InternalTransition;
import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.List;

public class Transitions<S extends INameStates, E extends INameEvents>{
    private List<Transition<S, E>> externalTransitions;
    private List<InternalTransition<S, E>> internalTransitions;

    public Transitions(List<Transition<S, E>> externalTransitions,
                       List<InternalTransition<S, E>> internalTransitions){
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
