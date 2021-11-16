package de.themonstrouscavalca.alteredstates.interfaces;

@FunctionalInterface
public interface ICheckEvents<E extends INameEvents, C, X>{
    boolean check(IHoldContext<E, C, X> context);
}
