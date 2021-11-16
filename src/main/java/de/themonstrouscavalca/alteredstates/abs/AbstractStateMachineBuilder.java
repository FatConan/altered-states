package de.themonstrouscavalca.alteredstates.abs;

import de.themonstrouscavalca.alteredstates.InternalTransition;
import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.helpers.EventCheckAndAction;
import de.themonstrouscavalca.alteredstates.helpers.InternalTransitionMap;
import de.themonstrouscavalca.alteredstates.helpers.TransitionMap;
import de.themonstrouscavalca.alteredstates.interfaces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @param <S> State class
 * @param <E> Event class
 * @param <C> Current context
 * @param <X> Event context
 */
public abstract class AbstractStateMachineBuilder<S extends INameStates,
        E extends INameEvents, C, X, T extends IManageStates<S, E, C, X>>
        implements IBuildStateMachines<S, E, C, X, T>, IExpressStateMachines<S, E, C, X>{
    protected S initialState;
    protected List<Transition<S, E>> transitions = new ArrayList<>();
    protected TransitionMap<S, E, C, X> handlerMap = new TransitionMap<>();
    protected List<InternalTransition<S, E>> internalTransitions = new ArrayList<>();
    protected InternalTransitionMap<S, E, C, X> internalHandlerMap = new InternalTransitionMap<>();
    protected C context;
    protected String name;

    protected List<S> finalStates;
    protected List<E> finalEvents;

    public AbstractStateMachineBuilder<S, E, C, X, T> setContext(C context){
        this.context = context;
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> setInitialState(S initialState){
        this.initialState = initialState;
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
                                                                    ){
        Transition<S, E> transition = new Transition<>(from, to, onEvent);
        this.transitions.add(transition);
        this.handlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent){
       return this.addTransition(from, to, onEvent, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        InternalTransition<S, E> transition = new InternalTransition<>(state, onEvent);
        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent){
        return this.addInternalTransition(state, onEvent, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> setName(String name){
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

    public S getInitialState(){
        return initialState;
    }

    public List<Transition<S, E>> getTransitions(){
        return transitions;
    }

    public TransitionMap<S, E, C, X> getHandlerMap(){
        return handlerMap;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return internalTransitions;
    }

    public InternalTransitionMap<S, E, C, X> getInternalHandlerMap(){
        return internalHandlerMap;
    }

    public C getContext(){
        return context;
    }

    public String getName(){
        return name;
    }

    public List<S> getFinalStates(){
        return finalStates;
    }

    public List<E> getFinalEvents(){
        return finalEvents;
    }

    protected abstract T createInstance();

    public T build(){
        this.finalizeStateValues();
        return this.createInstance();
    }
}