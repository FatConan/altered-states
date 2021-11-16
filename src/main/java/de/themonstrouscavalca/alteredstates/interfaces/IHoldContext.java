package de.themonstrouscavalca.alteredstates.interfaces;

public interface IHoldContext<E extends INameEvents, C, X>{
    E getEvent();
    C getContext();
    X getEventContext();
    void setEvent(E event);
    void setContext(C context);
    void setEventContext(X eventContext);
}
