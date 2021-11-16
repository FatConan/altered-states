package de.themonstrouscavalca.alteredstates.impl;

import de.themonstrouscavalca.alteredstates.*;
import de.themonstrouscavalca.alteredstates.helpers.ContextHolder;
import de.themonstrouscavalca.alteredstates.helpers.EventCheckAndAction;
import de.themonstrouscavalca.alteredstates.helpers.InternalTransitionMap;
import de.themonstrouscavalca.alteredstates.helpers.TransitionMap;
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
public class StateMachine<S extends INameStates, E extends INameEvents, C, X> implements INameStates, IManageStates<S, E, C, X>{
    private final S initialState;
    private S currentState;
    private final List<S> states;
    private final List<E> events;
    private final List<Transition<S, E>> transitions;
    private final Map<E, List<Transition<S, E>>> eventMap;
    private final List<InternalTransition<S, E>> internalTransitions;
    private final Map<E, List<InternalTransition<S, E>>> internalEventMap;
    private final TransitionMap<S, E, C, X> handlerMap;
    private final InternalTransitionMap<S, E, C, X> internalHandlerMap;
    private final C context;
    private final String name;


    public StateMachine(IExpressStateMachines<S, E, C, X> builder){
        this.initialState = builder.getInitialState();
        this.currentState = initialState;
        this.states = builder.getFinalStates();
        this.events = builder.getFinalEvents();
        this.name = builder.getName();

        this.handlerMap = builder.getHandlerMap();
        this.internalHandlerMap = builder.getInternalHandlerMap();

        this.transitions = builder.getTransitions();
        this.eventMap = this.processExternalTransitions();
        this.internalTransitions = builder.getInternalTransitions();
        this.internalEventMap = this.processInternalTransitions();

        this.context = builder.getContext();
    }

    private Map<E, List<Transition<S, E>>> processExternalTransitions(){
        Map<E, List<Transition<S, E>>> eventMap = new HashMap<>();
        for(Transition<S, E> transition : this.transitions){
            if(eventMap.containsKey(transition.getEvent())){
                eventMap.get(transition.getEvent()).add(transition);
            }else{
                List<Transition<S, E>> transitionList = new ArrayList<>();
                transitionList.add(transition);
                eventMap.put(transition.getEvent(), transitionList);
            }
        }
        return eventMap;
    }

    private Map<E, List<InternalTransition<S, E>>> processInternalTransitions(){
        Map<E, List<InternalTransition<S, E>>> eventMap = new HashMap<>();
        for(InternalTransition<S, E> transition : this.internalTransitions){
            if(eventMap.containsKey(transition.getEvent())){
                eventMap.get(transition.getEvent()).add(transition);
            }else{
                List<InternalTransition<S, E>> transitionList = new ArrayList<>();
                transitionList.add(transition);
                eventMap.put(transition.getEvent(), transitionList);
            }
        }
        return eventMap;
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

    public Transitions<S, E> getTransitionsForState(S state){
        List<Transition<S, E>> externalStates = this.getTransitions().stream()
                .filter(t -> t.getFromState().equals(state)).collect(Collectors.toList());
        List<InternalTransition<S, E>> internalStates = this.getInternalTransitions().stream()
                .filter(t -> t.getState().equals(state)).collect(Collectors.toList());
        return new Transitions<>(externalStates, internalStates);
    }

    public List<Transition<S, E>> getTransitions(){
        return this.transitions;
    }

    public Map<E, List<Transition<S, E>>> getEventMap(){
        return this.eventMap;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return this.internalTransitions;
    }

    public Map<E, List<InternalTransition<S, E>>> getInternalEventMap(){
        return this.internalEventMap;
    }

    public TransitionMap<S, E, C, X> getHandlerMap(){
        return this.handlerMap;
    }

    public InternalTransitionMap<S, E, C, X> getInternalHandlerMap(){
        return this.internalHandlerMap;
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

        List<Transition<S, E>> transitions = this.eventMap.getOrDefault(event, Collections.emptyList());
        Optional<Transition<S, E>> selectedOpt = transitions.stream()
                .filter(t -> t.getFromState().equals(this.currentState)).findFirst();

        if(selectedOpt.isPresent()){
            externalTransitionFound = true;
            EventCheckAndAction<E, C, X> checkAndAct = this.handlerMap.get(selectedOpt.get());
            boolean gate = checkAndAct.getChecker().check(contextHolder);
            if(gate){
                this.currentState = selectedOpt.get().getToState();;
                externalTransitionSuccessful = true;
                contextHolder = checkAndAct.getActor().act(contextHolder);
            }
        }

        List<InternalTransition<S, E>> internalTransitions = this.internalEventMap.getOrDefault(event, Collections.emptyList());
        Optional<InternalTransition<S, E>> selectedInternalOpt = internalTransitions.stream()
                .filter(t -> t.getState().equals(this.currentState)).findFirst();

        if(selectedInternalOpt.isPresent()){
            EventCheckAndAction<E, C, X> checkAndAct = this.internalHandlerMap.get(selectedInternalOpt.get());
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
        return this.transitions.stream()
                .filter(t -> t.getFromState().equals(this.currentState))
                .collect(Collectors.toList());
    }
}
