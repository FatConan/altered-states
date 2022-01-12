package de.themonstrouscavalca.alteredstates.abs;

import de.themonstrouscavalca.alteredstates.InternalTransition;
import de.themonstrouscavalca.alteredstates.Transition;
import de.themonstrouscavalca.alteredstates.helpers.EventCheckAndAction;
import de.themonstrouscavalca.alteredstates.helpers.InternalTransitionToCheckAndActionMap;
import de.themonstrouscavalca.alteredstates.helpers.TransitionToCheckAndActionMap;
import de.themonstrouscavalca.alteredstates.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
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
    protected TransitionToCheckAndActionMap<S, E, C, X> handlerMap = new TransitionToCheckAndActionMap<>();
    protected List<InternalTransition<S, E>> internalTransitions = new ArrayList<>();
    protected InternalTransitionToCheckAndActionMap<S, E, C, X> internalHandlerMap = new InternalTransitionToCheckAndActionMap<>();
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
                                                                    String label,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        Transition<S, E> transition = new Transition<>(from, to, onEvent, label);
        this.transitions.add(transition);
        this.handlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        return this.addTransition(from, to, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent, String label){
        return this.addTransition(from, to, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent){
        return this.addTransition(from, to, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    //Make a distinction for adding privileged transitions
    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent,
                                                                    String label,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        Transition<S, E> transition = new Transition<>(from, to, onEvent, label, true);
        this.transitions.add(transition);
        this.handlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        return this.addPrivilegedTransition(from, to, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent, String label){
        return this.addPrivilegedTransition(from, to, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent){
        return this.addPrivilegedTransition(from, to, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }


    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent,
                                                                            String label,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        InternalTransition<S, E> transition = new InternalTransition<>(state, onEvent, label);
        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        return this.addInternalTransition(state, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent, String label){
        return this.addInternalTransition(state, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent){
        return this.addInternalTransition(state, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    //Add privileged internal transitions
    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent,
                                                                            String label,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        InternalTransition<S, E> transition = new InternalTransition<>(state, onEvent, label, true);
        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        return this.addPrivilegedInternalTransition(state, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent, String label){
        return this.addPrivilegedInternalTransition(state, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent){
        return this.addPrivilegedInternalTransition(state, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
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

    public TransitionToCheckAndActionMap<S, E, C, X> getHandlerMap(){
        return handlerMap;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return internalTransitions;
    }

    public InternalTransitionToCheckAndActionMap<S, E, C, X> getInternalHandlerMap(){
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
