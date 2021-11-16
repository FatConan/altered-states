package de.themonstrouscavalca.alteredstates.interfaces;

public interface IBuildStateMachines<S extends INameStates, E extends INameEvents, C, X, T extends IManageStates<S, E, C, X>>{
    T build();
}
