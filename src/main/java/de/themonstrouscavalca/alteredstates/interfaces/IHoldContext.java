package de.themonstrouscavalca.alteredstates.interfaces;

import java.util.Optional;

public interface IHoldContext<E extends INameEvents, C, X>{
    Optional<E> getEvent();
    Optional<C> getContext();
    Optional<X> getEventContext();
    void setEvent(E event);
    void setContext(C context);
    void setEventContext(X eventContext);
}
