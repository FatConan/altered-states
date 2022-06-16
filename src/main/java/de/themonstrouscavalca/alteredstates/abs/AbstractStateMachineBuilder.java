package de.themonstrouscavalca.alteredstates.abs;

import de.themonstrouscavalca.alteredstates.transitions.InternalTransition;
import de.themonstrouscavalca.alteredstates.transitions.Transition;
import de.themonstrouscavalca.alteredstates.helpers.EventCheckAndAction;
import de.themonstrouscavalca.alteredstates.helpers.InternalTransitionToCheckAndActionMap;
import de.themonstrouscavalca.alteredstates.helpers.StateCollection;
import de.themonstrouscavalca.alteredstates.helpers.TransitionToCheckAndActionMap;
import de.themonstrouscavalca.alteredstates.interfaces.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <S> State class
 * @param <E> Event class
 * @param <C> Current context
 * @param <X> Event context
 * @param <T> The type of the StateMachine being created
 */
public abstract class AbstractStateMachineBuilder<S extends INameStates,
        E extends INameEvents,
        C, X, T extends IManageStates<S, E, C, X>>
        implements IBuildStateMachines<S, E, C, X, T>, IExpressStateMachines<S, E, C, X>{
    protected S initialState;
    protected List<Transition<S, E, C, X>> transitions = new ArrayList<>();
    protected TransitionToCheckAndActionMap<S, E, C, X> handlerMap = new TransitionToCheckAndActionMap<>();
    protected List<InternalTransition<S, E>> internalTransitions = new ArrayList<>();
    protected InternalTransitionToCheckAndActionMap<S, E, C, X> internalHandlerMap = new InternalTransitionToCheckAndActionMap<>();
    protected C context;
    protected String name;

    protected List<IGenerateState<S, E, C, X>> finalStates;
    protected List<E> finalEvents;

    public AbstractStateMachineBuilder<S, E, C, X, T> setContext(C context){
        this.context = context;
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> setInitialState(S initialState){
        this.initialState = initialState;
        return this;
    }

    /**
     * Add a new transition to the list of transition the machine can undergo. A transition is a
     * change from the machine in one or more viable states, to a second state when an event is triggered with the
     * required context and parameters. The multiple states a machine may transition from is provided by a StateCollection<S>
     * that is a collection of viable S states. A state collection may also be a wildcard (meaning that any state may be transitioned
     * from).
     *
     * @param fromStates
     * @param to
     * @param onEvent
     * @param label
     * @param updateCheck
     * @param actionTaker
     * @return
     */


    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(StateCollection<S> fromStates, S to, E onEvent,
                                                                    String label,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        Transition<S, E, C, X> transition = new Transition.Builder<S, E, C, X>()
                .from(fromStates)
                .to(to)
                .on(onEvent)
                .label(label)
                .build();
        this.transitions.add(transition);
        this.handlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(StateCollection<S> fromStates, S to, E onEvent,
                                                                              String label,
                                                                              ICheckEvents<E, C, X> updateCheck,
                                                                              ITakeAction<E, C, X> actionTaker
    ){
        Transition<S, E, C, X> transition = new Transition.Builder<S, E, C, X>()
                .from(fromStates)
                .to(to)
                .on(onEvent)
                .label(label)
                .privileged()
                .build();
        this.transitions.add(transition);
        this.handlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    //region AddTransition from single state
    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent,
                                                                    String label,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        StateCollection<S> fromStates = new StateCollection<>();
        fromStates.add(from);
        return this.addTransition(fromStates, to, onEvent, label, updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        StateCollection<S> fromStates = new StateCollection<>();
        fromStates.add(from);
        return this.addTransition(fromStates, to, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent, String label){
        StateCollection<S> fromStates = new StateCollection<>();
        fromStates.add(from);
        return this.addTransition(fromStates, to, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(S from, S to, E onEvent){
        StateCollection<S> fromStates = new StateCollection<>();
        fromStates.add(from);
        return this.addTransition(fromStates, to, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }
    //endregion

    //region AddTransition from state collection
    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(StateCollection<S> fromStates, S to, E onEvent,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        return this.addTransition(fromStates, to, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(StateCollection<S> fromStates, S to, E onEvent, String label){
        return this.addTransition(fromStates, to, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addTransition(StateCollection<S> fromStates, S to, E onEvent){
        return this.addTransition(fromStates, to, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }
    //endregion


    //Make a distinction for adding privileged transitions
    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent,
                                                                    String label,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
       return this.addPrivilegedTransition(StateCollection.of(from), to, onEvent, label, updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent,
                                                                              ICheckEvents<E, C, X> updateCheck,
                                                                              ITakeAction<E, C, X> actionTaker
    ){
        return this.addPrivilegedTransition(StateCollection.of(from), to, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent, String label){
        return this.addPrivilegedTransition(StateCollection.of(from), to, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(S from, S to, E onEvent){
        return this.addPrivilegedTransition(StateCollection.of(from), to, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(StateCollection<S> from, S to, E onEvent,
                                                                    ICheckEvents<E, C, X> updateCheck,
                                                                    ITakeAction<E, C, X> actionTaker
    ){
        return this.addPrivilegedTransition(from, to, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(StateCollection<S> from, S to, E onEvent, String label){
        return this.addPrivilegedTransition(from, to, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedTransition(StateCollection<S> from, S to, E onEvent){
        return this.addPrivilegedTransition(from, to, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }
    //endregion

    //region Add Internal transitions
    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(StateCollection<S> states, E onEvent,
                                                                            String label,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        InternalTransition<S, E> transition = new InternalTransition.Builder<S, E>()
                .states(states)
                .on(onEvent)
                .label(label)
                .build();

        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(StateCollection<S> states, E onEvent,
                                                                                      String label,
                                                                                      ICheckEvents<E, C, X> updateCheck,
                                                                                      ITakeAction<E, C, X> actionTaker){
        InternalTransition<S, E> transition = new InternalTransition.Builder<S, E>()
                .states(states)
                .on(onEvent)
                .label(label)
                .privileged()
                .build();
        this.internalTransitions.add(transition);
        this.internalHandlerMap.put(transition, new EventCheckAndAction<E, C, X>(updateCheck, actionTaker));
        return this;
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent,
                                                                            String label,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        return this.addInternalTransition(StateCollection.of(state), onEvent, label, updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        return this.addInternalTransition(StateCollection.of(state), onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent, String label){
        return this.addInternalTransition(StateCollection.of(state), onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(S state, E onEvent){
        return this.addInternalTransition(StateCollection.of(state), onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(StateCollection<S> states, E onEvent,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        return this.addInternalTransition(states, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(StateCollection<S> states, E onEvent, String label){
        return this.addInternalTransition(states, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addInternalTransition(StateCollection<S> states, E onEvent){
        return this.addInternalTransition(states, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }
    //endregion

    //region Add privileged internal transitions
    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent,
                                                                                      String label,
                                                                                      ICheckEvents<E, C, X> updateCheck,
                                                                                      ITakeAction<E, C, X> actionTaker){
        return this.addPrivilegedInternalTransition(StateCollection.of(state), onEvent, label, updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent,
                                                                            ICheckEvents<E, C, X> updateCheck,
                                                                            ITakeAction<E, C, X> actionTaker){
        return this.addPrivilegedInternalTransition(StateCollection.of(state), onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent, String label){
        return this.addPrivilegedInternalTransition(StateCollection.of(state), onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(S state, E onEvent){
        return this.addPrivilegedInternalTransition(StateCollection.of(state), onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(StateCollection<S> states, E onEvent,
                                                                                      ICheckEvents<E, C, X> updateCheck,
                                                                                      ITakeAction<E, C, X> actionTaker){
        return this.addPrivilegedInternalTransition(states, onEvent, "", updateCheck, actionTaker);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(StateCollection<S> states, E onEvent, String label){
        return this.addPrivilegedInternalTransition(states, onEvent, label, (contextHolder) -> true, (contextHolder) -> contextHolder);
    }

    public AbstractStateMachineBuilder<S, E, C, X, T> addPrivilegedInternalTransition(StateCollection<S> states, E onEvent){
        return this.addPrivilegedInternalTransition(states, onEvent, "", (contextHolder) -> true, (contextHolder) -> contextHolder);
    }
    //endregion

    public AbstractStateMachineBuilder<S, E, C, X, T> setName(String name){
        this.name = name;
        return this;
    }

    private void finalizeStateValues(){
        List<S> allFromStates = this.transitions.stream().map(Transition::getFromStates)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<IGenerateState<S, E, C, X>> finalFromStates = new ArrayList<>();
        for(S state: allFromStates){
            finalFromStates.add((e, c, x) -> state);
        }
        List<IGenerateState<S, E, C, X>> finalToStates = this.transitions.stream()
                .map(Transition::getToStateGenerator)
                .distinct().collect(Collectors.toList());
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

    public List<Transition<S, E, C, X>> getTransitions(){
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

    public List<IGenerateState<S, E, C, X>> getFinalStates(){
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
