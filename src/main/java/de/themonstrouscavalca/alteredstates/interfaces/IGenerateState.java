package de.themonstrouscavalca.alteredstates.interfaces;

@FunctionalInterface
/**
 * This functional interface is designed to allow for states that are generated at runtime, still obeying the rules of the
 * state machine but created at runtime as the to state when transitions are required. In the base case the associated function
 * would just return a static state of type S
 * (e,c,x) -> S state
 *
 * S - State
 * E - Event
 * C - Context
 * X - EventContext
 */
public interface IGenerateState<S extends INameStates, E extends INameEvents, C, X>{
    S generate(E event, C context, X eventContext);
}
