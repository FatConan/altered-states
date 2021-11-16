package de.themonstrouscavalca.alteredstates.interfaces;

import de.themonstrouscavalca.alteredstates.InternalTransition;
import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.helpers.InternalTransitionToCheckAndActionMap;
import de.themonstrouscavalca.alteredstates.helpers.TransitionToCheckAndActionMap;

import java.util.List;

public interface IExpressStateMachines<S extends INameStates, E extends INameEvents, C, X>{
    S getInitialState();
    List<Transition<S, E>> getTransitions();
    TransitionToCheckAndActionMap<S, E, C, X> getHandlerMap();
    List<InternalTransition<S, E>> getInternalTransitions();
    InternalTransitionToCheckAndActionMap<S, E, C, X> getInternalHandlerMap();
    C getContext();
    String getName();
    List<S> getFinalStates();
    List<E> getFinalEvents();
}
