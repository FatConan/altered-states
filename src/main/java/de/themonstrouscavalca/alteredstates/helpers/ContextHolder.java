package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.interfaces.IHoldContext;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;

import java.util.Optional;

public class ContextHolder<E extends INameEvents, C, X> implements IHoldContext<E, C, X>{
    private Optional<E> event;
    private Optional<C> context;
    private Optional<X> eventContext;

    public ContextHolder(E event, C context, X eventContext){
        this.event = Optional.ofNullable(event);
        this.context = Optional.ofNullable(context);
        this.eventContext = Optional.ofNullable(eventContext);
    }

    public ContextHolder(){
        this.event = Optional.empty();
        this.context = Optional.empty();
        this.eventContext = Optional.empty();
    }

    @Override
    public Optional<E> getEvent(){
        return this.event;
    }

    @Override
    public Optional<C> getContext(){
        return this.context;
    }

    @Override
    public Optional<X> getEventContext(){
        return this.eventContext;
    }

    @Override
    public void setEvent(E event){
        this.event = Optional.ofNullable(event);
    }

    @Override
    public void setContext(C context){
        this.context = Optional.ofNullable(context);
    }

    @Override
    public void setEventContext(X eventContext){
        this.eventContext = Optional.ofNullable(eventContext);
    }
}
