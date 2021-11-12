package de.themonstrouscavalca.alteredStates;

import de.themonstrouscavalca.alteredStates.interfaces.EventConsumer;
import de.themonstrouscavalca.alteredStates.interfaces.IBuildStateMachines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedStateMachineBuilder<S, E, C, X, T extends StateMachine<S, E, C, X>, V extends NestedStateMachine<S, E, C, X, T>>
        implements IBuildStateMachines<V>{
    protected T initialState;
    protected List<T> states = new ArrayList<>();
    protected List<E> events = new ArrayList<>();
    protected List<Transition<T, E>> transitions = new ArrayList<>();
    protected List<InternalTransition<T, E>> internalTransitions  = new ArrayList<>();
    protected Map<Transition<T, E>, EventConsumer<E, X, C>> handlerMap = new HashMap<>();
    protected Map<InternalTransition<T,E>, EventConsumer<E, X, C>> internalHandlerMap = new HashMap<>();
    protected C context;

    public NestedStateMachineBuilder<S, E, C, X, T, V> setContext(C context){
        this.context = context;
        return this;
    }

    public NestedStateMachineBuilder<S, E, C, X, T, V> setInitialState(T initialState){
        this.initialState = initialState;
        return this;
    }

    public NestedStateMachineBuilder<S, E, C, X, T, V> addTransition(T from, T to, E onEvent, EventConsumer<E, X, C> updateHandler){
        Transition<T, E> transition = new Transition<>(from, to, onEvent);
        this.transitions.add(transition);
        this.handlerMap.put(transition, updateHandler);
        return this;
    }

    public NestedStateMachineBuilder<S, E, C, X, T, V> addTransition(T from, T to, E onEvent){
        Transition<T, E> transition = new Transition<>(from, to, onEvent);
        this.transitions.add(transition);
        this.handlerMap.put(transition, (e, x, c) -> true);
        return this;
    }

    protected V createInstance(){
        return (V) new NestedStateMachine<>(this.initialState, this.states, this.events,
                this.transitions,
                this.handlerMap,
                this.context);
    }

    public  V build(){
        return this.createInstance();
    }
}
