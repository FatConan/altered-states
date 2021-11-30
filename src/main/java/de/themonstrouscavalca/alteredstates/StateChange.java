package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.interfaces.IHoldContext;
import de.themonstrouscavalca.alteredstates.interfaces.IMonitorStateChanges;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class StateChange<S extends INameStates, E extends INameEvents, C, X> implements IMonitorStateChanges<S, E, C, X>{
    private final S fromState;
    private final S toState;
    private final E onEvent;
    private final boolean externalTransitionFound;
    private final boolean externalTransitionPermitted;
    private final boolean externalTransitionSuccessful;
    private final boolean internalTransitionFound;
    private final boolean internalTransitionPermitted;
    private final boolean internalTransitionSuccessful;
    private final IHoldContext<E, C, X> contextHolder;

    public StateChange(S fromState, S toState, E onEvent, IHoldContext<E, C, X> contextHolder,
                       boolean externalTransitionFound, boolean externalTransitionPermitted, boolean externalTransitionSuccessful,
                       boolean internalTransitionFound, boolean internalTransitionPermitted, boolean internalTransitionSuccessful){
        this.fromState = fromState;
        this.toState = toState;
        this.onEvent = onEvent;
        this.externalTransitionFound = externalTransitionFound;
        this.externalTransitionPermitted = externalTransitionPermitted;
        this.externalTransitionSuccessful = externalTransitionSuccessful;
        this.internalTransitionFound = internalTransitionFound;
        this.internalTransitionPermitted = internalTransitionPermitted;
        this.internalTransitionSuccessful = internalTransitionSuccessful;
        this.contextHolder = contextHolder;
    }

    @Override
    public S getFromState(){
        return fromState;
    }

    @Override
    public S getToState(){
        return toState;
    }

    @Override
    public E getOnEvent(){
        return onEvent;
    }

    @Override
    public boolean isExternalTransitionFound(){
        return externalTransitionFound;
    }

    @Override
    public boolean isExternalTransitionPermitted(){
        return externalTransitionPermitted;
    }

    @Override
    public boolean isExternalTransitionSuccessful(){
        return externalTransitionSuccessful;
    }

    @Override
    public boolean isInternalTransitionFound(){
        return internalTransitionFound;
    }

    @Override
    public boolean isInternalTransitionPermitted(){
        return internalTransitionPermitted;
    }

    @Override
    public boolean isInternalTransitionSuccessful(){
        return internalTransitionSuccessful;
    }

    @Override
    public boolean transitionFound(){
        return this.internalTransitionFound || this.externalTransitionFound;
    }

    @Override
    public boolean transitionPermitted(){
        return (this.internalTransitionFound && this.internalTransitionPermitted) ||
                (this.externalTransitionFound && this.externalTransitionPermitted);
    }

    @Override
    public boolean transitionSuccessful(){
        return this.internalTransitionSuccessful || this.externalTransitionSuccessful;
    }
    
    @Override
    public IHoldContext<E, C, X> getContextHolder(){
        return contextHolder;
    }
}
