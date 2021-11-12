package de.themonstrouscavalca.alteredstates;

import de.themonstrouscavalca.alteredstates.interfaces.EventConsumer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A generic state machine class used to represent a series of states and transitions
 * @param <S> State class
 * @param <E> Event class
 * @param <C> State context class
 * @param <X> Event context class
 */
public class StateMachine<S, E, C, X>{
    private final S initialState;
    private S currentState;
    private final List<S> states;
    private final List<E> events;
    private final List<Transition<S, E>> transitions;
    private final Map<E, List<Transition<S, E>>> eventMap;
    private final List<InternalTransition<S, E>> internalTransitions;
    private final Map<E, List<InternalTransition<S, E>>> internalEventMap;
    private final Map<Transition<S, E>, EventConsumer<E, X, C>> handlerMap;
    private final Map<InternalTransition<S,E>, EventConsumer<E, X, C>> internalHandlerMap;
    private final C context;

    public StateMachine(S initialState, List<S> states, List<E> events,
                 List<Transition<S, E>> transitions,
                 List<InternalTransition<S, E>> internalTransitions,
                 Map<Transition<S, E>, EventConsumer<E, X, C>> handlerMap,
                 Map<InternalTransition<S, E>, EventConsumer<E, X, C>> internalHandlerMap,
                 C context){
        this.initialState = initialState;
        this.currentState = initialState;
        this.states = states;
        this.events = events;

        this.handlerMap = handlerMap;
        this.internalHandlerMap = internalHandlerMap;

        this.transitions = transitions;
        this.eventMap = this.processExternalTransitions();
        this.internalTransitions = internalTransitions;
        this.internalEventMap = this.processInternalTransitions();

        this.context = context;
    }

    public static boolean checkGate(Boolean ... checks){
        Set<Boolean> checkSet = Arrays.stream(checks).collect(Collectors.toSet());
        return !checkSet.contains(false);
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

    public List<S> getStates(){
        return states;
    }

    public List<E> getEvents(){
        return events;
    }

    public List<Transition<S, E>> getTransitions(){
        return transitions;
    }

    public Map<E, List<Transition<S, E>>> getEventMap(){
        return eventMap;
    }

    public List<InternalTransition<S, E>> getInternalTransitions(){
        return internalTransitions;
    }

    public Map<E, List<InternalTransition<S, E>>> getInternalEventMap(){
        return internalEventMap;
    }

    public Map<Transition<S, E>, EventConsumer<E, X, C>> getHandlerMap(){
        return handlerMap;
    }

    public Map<InternalTransition<S, E>, EventConsumer<E, X, C>> getInternalHandlerMap(){
        return internalHandlerMap;
    }

    public C getContext(){
        return context;
    }

    public StateChange<S, E, C, X> onEvent(E event, X eventContext){
        boolean externalTransitionFound = false;
        boolean externalTransitionSuccessful = false;
        boolean internalTransitionFound = false;
        boolean internalTransitionSuccessful = false;
        S initialState = this.currentState;

        List<Transition<S, E>> transitions = this.eventMap.getOrDefault(event, Collections.emptyList());
        Optional<Transition<S, E>> selectedOpt = transitions.stream()
                .filter(t -> t.getFromState().equals(this.currentState)).findFirst();

        if(selectedOpt.isPresent()){
            externalTransitionFound = true;
            boolean gate = this.handlerMap.get(selectedOpt.get()).process(event, eventContext, this.context);
            if(gate){
                this.currentState = selectedOpt.get().getToState();;
                externalTransitionSuccessful = true;
            }
        }

        List<InternalTransition<S, E>> internalTransitions = this.internalEventMap.getOrDefault(event, Collections.emptyList());
        Optional<InternalTransition<S, E>> selectedInternalOpt = internalTransitions.stream()
                .filter(t -> t.getState().equals(this.currentState)).findFirst();

        if(selectedInternalOpt.isPresent()){
            internalTransitionFound = true;
            boolean gate = this.internalHandlerMap.get(selectedInternalOpt.get()).process(event, eventContext, this.context);
            if(gate){
                internalTransitionSuccessful = true;
            }
        }

        return new StateChange<>(initialState, this.currentState, event, context, eventContext,
                externalTransitionFound, externalTransitionSuccessful,
                internalTransitionFound, internalTransitionSuccessful);
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
