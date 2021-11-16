package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.interfaces.IHoldContext;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class StateChange<S extends INameStates, E extends INameEvents, C, X>{
    private final S fromState;
    private final S toState;
    private final E onEvent;
    private final boolean externalTransitionFound;
    private final boolean externalTransitionSuccessful;
    private final boolean internalTransitionFound;
    private final boolean internalTransitionSuccessful;
    private final IHoldContext<E, C, X> contextHolder;

    public StateChange(S fromState, S toState, E onEvent, IHoldContext<E, C, X> contextHolder,
                       boolean externalTransitionFound, boolean externalTransitionSuccessful,
                       boolean internalTransitionFound, boolean internalTransitionSuccessful){
        this.fromState = fromState;
        this.toState = toState;
        this.onEvent = onEvent;
        this.externalTransitionFound = externalTransitionFound;
        this.externalTransitionSuccessful = externalTransitionSuccessful;
        this.internalTransitionFound = internalTransitionFound;
        this.internalTransitionSuccessful = internalTransitionSuccessful;
        this.contextHolder = contextHolder;
    }

    public S getFromState(){
        return fromState;
    }

    public S getToState(){
        return toState;
    }

    public E getOnEvent(){
        return onEvent;
    }

    public boolean isExternalTransitionFound(){
        return externalTransitionFound;
    }

    public boolean isExternalTransitionSuccessful(){
        return externalTransitionSuccessful;
    }

    public boolean isInternalTransitionFound(){
        return internalTransitionFound;
    }

    public boolean isInternalTransitionSuccessful(){
        return internalTransitionSuccessful;
    }

    public boolean transitionFound(){
        return this.internalTransitionFound || this.externalTransitionFound;
    }

    public boolean transitionSuccessful(){
        return this.internalTransitionSuccessful || this.externalTransitionSuccessful;
    }

    public IHoldContext<E, C, X> getContextHolder(){
        return contextHolder;
    }
}
