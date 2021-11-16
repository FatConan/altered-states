package de.themonstrouscavalca.alteredstates.helpers;

import de.themonstrouscavalca.alteredstates.interfaces.IHoldContext;
import de.themonstrouscavalca.alteredstates.interfaces.INameEvents;

public class ContextHolder<E extends INameEvents, C, X> implements IHoldContext<E, C, X>{
    private E event;
    private C context;
    private X eventContext;

    public ContextHolder(E event, C context, X eventContext){
        this.event = event;
        this.context = context;
        this.eventContext = eventContext;
    }

    @Override
    public E getEvent(){
        return this.event;
    }

    @Override
    public C getContext(){
        return this.context;
    }

    @Override
    public X getEventContext(){
        return this.eventContext;
    }

    @Override
    public void setEvent(E event){
        this.event = event;
    }

    @Override
    public void setContext(C context){
        this.context = context;
    }

    @Override
    public void setEventContext(X eventContext){
        this.eventContext = eventContext;
    }
}
