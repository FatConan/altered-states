package de.themonstrouscavalca.alteredStates.interfaces;

@FunctionalInterface
public interface EventConsumer<E, X, C>{
    boolean process(E event, X eventContext, C context);
}
