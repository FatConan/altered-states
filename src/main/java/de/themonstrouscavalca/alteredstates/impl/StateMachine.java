package de.themonstrouscavalca.alteredstates.impl;

import de.themonstrouscavalca.alteredstates.*;
import de.themonstrouscavalca.alteredstates.helpers.*;
import de.themonstrouscavalca.alteredstates.interfaces.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A generic state machine class used to represent a series of states and transitions
 * @param <S> State class
 * @param <E> Event class
 * @param <C> State context class
 * @param <X> Event context class
 */
public class StateMachine<S extends INameStates, E extends INameEvents, C, X> implements IManageStates<S, E, C, X>{
    private final S initialState;
    private S currentState;
    private final List<S> states;
    private final List<E> events;
    private final TransitionsCheckAndActions<S, E, C, X> handlerChecksAndActions;
    private final C context;
    private final String name;

    public StateMachine(IExpressStateMachines<S, E, C, X> builder){
        this.initialState = builder.getInitialState();
        this.currentState = initialState;
        this.states = builder.getFinalStates();
        this.events = builder.getFinalEvents();
        this.name = builder.getName();

        TransitionsCheckAndActions<S, E, C, X> transitionsAndChecks = new TransitionsCheckAndActions<>();
        this.processExternalTransitions(builder, transitionsAndChecks);
        this.processInternalTransitions(builder, transitionsAndChecks);
        this.handlerChecksAndActions = transitionsAndChecks;

        this.context = builder.getContext();
    }

    private void processExternalTransitions(IExpressStateMachines<S, E, C, X> builder,
                                                                  TransitionsCheckAndActions<S, E, C, X> transitionsAndChecks){
        for(Transition<S, E> transition : builder.getTransitions()){
            transitionsAndChecks.addTransition(transition, builder.getHandlerMap().get(transition));
        }
    }

    private void processInternalTransitions(IExpressStateMachines<S, E, C, X> builder,
                                            TransitionsCheckAndActions<S, E, C, X> transitionsAndChecks){
        for(InternalTransition<S, E> transition : builder.getInternalTransitions()){
            transitionsAndChecks.addTransition(transition, builder.getInternalHandlerMap().get(transition));
        }
    }

    public S getInitialState(){
        return initialState;
    }

    @Override
    public String getName(){
        return this.name;
    }

    public List<S> getStates(){
        return this.states;
    }

    public List<E> getEvents(){
        return this.events;
    }

    public Events<E> getEventsForState(S state){
        List<E> externalEvents = this.getTransitions().stream()
                .filter(t -> t.getFromState().equals(state))
                .map(Transition::getEvent)
                .collect(Collectors.toList());
        List<E> internalEvents = this.getInternalTransitions().stream()
                .filter(t -> t.getState().equals(state))
                .map(InternalTransition::getEvent).collect(Collectors.toList());
        return new Events<>(externalEvents, internalEvents);
    }

    public TransitionsCheckAndActions<S, E, C, X> getTransitionsForState(S state){
        return this.handlerChecksAndActions.getForState(state);
    }

    public List<Transition<S, E>> getTransitions(){
        return this.handlerChecksAndActions.getTransitions();
    }

    public EventToTransitionMap<S, E> getEventMap(){
        return this.handlerChecksAndActions.getEventMap();
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return this.handlerChecksAndActions.getInternalTransitions();
    }

    public EventToInternalTransitionMap<S, E> getInternalEventMap(){
        return this.handlerChecksAndActions.getInternalEventMap();
    }

    public TransitionToCheckAndActionMap<S, E, C, X> getHandlerMap(){
        return this.handlerChecksAndActions.getTransitionsMap();
    }

    public InternalTransitionToCheckAndActionMap<S, E, C, X> getInternalHandlerMap(){
        return this.handlerChecksAndActions.getInternalTransitionsMap();
    }

    public C getContext(){
        return this.context;
    }

    protected StateChange<S, E, C, X> handleEvent(E event, X eventContext){
        boolean externalTransitionFound = false;
        boolean externalTransitionSuccessful = false;
        boolean internalTransitionFound = false;
        boolean internalTransitionSuccessful = false;
        S initialState = this.currentState;

        IHoldContext<E, C, X> contextHolder = new ContextHolder<>(event, this.context, eventContext);

        Optional<Transition<S, E>> selectedOpt = this.handlerChecksAndActions.getTransitionForEventAndState(event, this.getCurrentState());
        if(selectedOpt.isPresent()){
            externalTransitionFound = true;
            EventCheckAndAction<E, C, X> checkAndAct = this.handlerChecksAndActions.getCheckAndAction(selectedOpt.get());
            boolean gate = checkAndAct.getChecker().check(contextHolder);
            if(gate){
                this.currentState = selectedOpt.get().getToState();;
                externalTransitionSuccessful = true;
                contextHolder = checkAndAct.getActor().act(contextHolder);
            }
        }

        Optional<InternalTransition<S, E>> selectedInternalOpt = this.handlerChecksAndActions.getInternalTransitionForEventAndState(event, this.getCurrentState());
        if(selectedInternalOpt.isPresent()){
            EventCheckAndAction<E, C, X> checkAndAct = this.handlerChecksAndActions.getCheckAndAction(selectedInternalOpt.get());
            internalTransitionFound = true;
            boolean gate = checkAndAct.getChecker().check(contextHolder);
            if(gate){
                internalTransitionSuccessful = true;
                contextHolder = checkAndAct.getActor().act(contextHolder);
            }
        }

        return new StateChange<>(initialState, this.currentState, event, contextHolder,
                externalTransitionFound, externalTransitionSuccessful,
                internalTransitionFound, internalTransitionSuccessful);
    }
    public StateChange<S, E, C, X> onEvent(E event, X eventContext){
        return this.handleEvent(event, eventContext);
    }

    public StateChange<S, E, C, X> onEvent(E event){
        return this.onEvent(event, null);
    }

    public void setCurrentState(S state){
        this.currentState = state;
    }

    public S getCurrentState(){
        return this.currentState;
    }

    public List<Transition<S, E>> getAvailableTransitions(){
        return this.handlerChecksAndActions.getTransitions().stream()
                .filter(t -> t.getFromState().equals(this.currentState))
                .collect(Collectors.toList());
    }
}
