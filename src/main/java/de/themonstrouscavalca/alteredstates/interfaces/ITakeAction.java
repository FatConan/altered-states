package de.themonstrouscavalca.alteredstates.interfaces;

@FunctionalInterface
public interface ITakeAction<E extends INameEvents, C, X>{
    IHoldContext<E, C, X> act(IHoldContext<E, C, X> context);
}
