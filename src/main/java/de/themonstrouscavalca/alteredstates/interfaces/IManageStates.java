package de.themonstrouscavalca.alteredstates.interfaces;

import de.themonstrouscavalca.alteredstates.StateChange;

public interface IManageStates<S extends INameStates, E extends INameEvents, C, X>{
    StateChange<S, E, C, X> onEvent(E event, X eventContext);
    StateChange<S, E, C, X> onEvent(E event);
    void setCurrentState(S state);
    S getCurrentState();
}
