package de.themonstrouscavalca.alteredstates.interfaces;

public interface IMonitorStateChanges<S extends INameStates, E extends INameEvents, C, X>{
    S getFromState();
    S getToState();
    E getOnEvent();

    boolean isExternalTransitionFound();
    boolean isExternalTransitionPermitted();
    boolean isExternalTransitionSuccessful();
    boolean isInternalTransitionFound();
    boolean isInternalTransitionPermitted();
    boolean isInternalTransitionSuccessful();
    boolean transitionFound();
    boolean transitionPermitted();
    boolean transitionSuccessful();
    
    IHoldContext<E, C, X> getContextHolder();
}
