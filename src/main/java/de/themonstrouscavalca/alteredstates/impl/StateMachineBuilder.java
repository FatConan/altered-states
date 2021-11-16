package de.themonstrouscavalca.alteredstates.impl;

import de.themonstrouscavalca.alteredstates.abs.AbstractStateMachineBuilder;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class StateMachineBuilder<S extends INameStates, E extends INameEvents, C, X>
        extends AbstractStateMachineBuilder<S, E, C, X, StateMachine<S, E, C, X>>{
    @Override
    protected StateMachine<S, E, C, X> createInstance(){
        return new StateMachine<>(this);
    }
}
