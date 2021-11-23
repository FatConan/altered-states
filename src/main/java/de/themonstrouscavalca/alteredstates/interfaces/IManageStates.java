package de.themonstrouscavalca.alteredstates.interfaces;

public interface IManageStates<S extends INameStates, E extends INameEvents, C, X> extends INameStates{
    IMonitorStateChanges<S, E, C, X> onEvent(E event, X eventContext);
    IMonitorStateChanges<S, E, C, X> onEvent(E event);
    void setCurrentState(S state);
    S getCurrentState();
}
