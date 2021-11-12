package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.interfaces.IConsumeEvents;
import de.themonstrouscavalca.alteredstates.interfaces.IBuildStateMachines;
import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @param <S> State class
 * @param <E> Event class
 * @param <C> Current context
 * @param <X> Event context
 */
public class StateMachineBuilder<S extends INameStates, E, C, X, T extends StateMachine<S, E, C, X>> implements IBuildStateMachines<T>{
    protected S initialState;
    protected List<Transition<S, E>> transitions = new ArrayList<>();
    protected Map<Transition<S, E>, IConsumeEvents<E, X, C>> handlerMap = new HashMap<>();
    protected List<InternalTransition<S, E>> internalTransitions = new ArrayList<>();
    protected Map<InternalTransition<S, E>, IConsumeEvents<E, X, C>> internalHandlerMap = new HashMap<>();
    protected C context;
    protected String name;

    protected List<S> finalStates;
    protected List<E> finalEvents;

    public StateMachineBuilder<S, E, C, X, T> setContext(C context){
        this.context = context;
        return this;
    }

    public StateMachineBuilder<S, E, C, X, T> setInitialState(S initialState){
        this.initialState = initialState;
        return this;
    }

    public StateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent, IConsumeEvents<E, X, C> updateHandler){
        Transition<S, E> transition = new Transition<>(from, to, onEvent);
        this.transitions.add(transition);
        this.handlerMap.put(transition, updateHandler);
        return this;
    }

    public StateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent){
        Transition<S, E> transition = new Transition<>(from, to, onEvent);
        this.transitions.add(transition);
        this.handlerMap.put(transition, (e, x, c) -> true);
        return this;
    }

    public StateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent, IConsumeEvents<E, X, C> updateHandler){
        InternalTransition<S, E> transition = new InternalTransition<>(state, onEvent);
        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, updateHandler);
        return this;
    }

    public StateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent){
        InternalTransition<S, E> transition = new InternalTransition<>(state, onEvent);
        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, (e, x, c) -> true);
        return this;
    }

    public StateMachineBuilder<S, E, C, X, T> setName(String name){
        this.name = name;
        return this;
    }

    private void finalizeStateValues(){
        List<S> finalFromStates = this.transitions.stream().map(Transition::getFromState).distinct().collect(Collectors.toList());
        List<S> finalToStates = this.transitions.stream().map(Transition::getToState).distinct().collect(Collectors.toList());
        finalFromStates.addAll(finalToStates);
        this.finalStates = finalFromStates.stream().distinct().collect(Collectors.toList());

        List<E> finalExternalTransitions = this.transitions.stream().map(Transition::getEvent).distinct().collect(Collectors.toList());
        List<E> finalInternalTransitions = this.internalTransitions.stream().map(InternalTransition::getEvent).distinct().collect(Collectors.toList());
        finalExternalTransitions.addAll(finalInternalTransitions);
        this.finalEvents = finalExternalTransitions.stream().distinct().collect(Collectors.toList());
    }

    protected T createInstance(){
        return (T) new StateMachine<S, E, C, X>(this.initialState, this.finalStates, this.finalEvents,
                this.transitions,
                this.internalTransitions,
                this.handlerMap,
                this.internalHandlerMap,
                this.context,
                this.name);
    }

    public T build(){
        this.finalizeStateValues();
        return this.createInstance();
    }
}
