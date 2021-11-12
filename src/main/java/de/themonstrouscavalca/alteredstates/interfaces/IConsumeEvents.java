package de.themonstrouscavalca.alteredstates.interfaces;

@FunctionalInterface
public interface IConsumeEvents<E, X, C>{
    boolean process(E event, X eventContext, C context);
}
