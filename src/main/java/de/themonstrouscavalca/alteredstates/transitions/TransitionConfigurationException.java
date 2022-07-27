package de.themonstrouscavalca.alteredstates.transitions;

import de.themonstrouscavalca.alteredstates.interfaces.INameStates;

public class TransitionConfigurationException extends RuntimeException{
    public static String errorString(String message, Transition<?, ?, ?, ?> transition){
        StringBuilder fullMessage = new StringBuilder().append(message)
                .append(": Transition of type [").append(transition.getTransitionType().name()).append("] for event [").append(transition.getEvent().getName()).append("] from state(s) [");
        if(transition.getTransitionType().isMultipleOrigins()){
            for(INameStates state: transition.getFromStates()){
                fullMessage.append(state.getName())
                        .append(",");
            }
        }else{
            fullMessage.append(transition.getFromState().getName());
        }
        fullMessage.append("]");
        return fullMessage.toString();
    }

    public static String errorString(String message, InternalTransition<?, ?> transition){
        StringBuilder fullMessage = new StringBuilder().append(message)
                .append(": Transition of type [").append(transition.getTransitionType().name()).append("] for event [").append(transition.getEvent().getName()).append("] from state(s) [");
        if(transition.getTransitionType().isMultipleStates()){
            for(INameStates state: transition.getStates()){
                fullMessage.append(state.getName())
                        .append(",");
            }
        }else{
            fullMessage.append(transition.getState().getName());
        }
        fullMessage.append("]");
        return fullMessage.toString();
    }

    public TransitionConfigurationException(String message){
        super(message);
    }
    public TransitionConfigurationException(String message, Transition<?, ?, ?, ?> transition){
        super(TransitionConfigurationException.errorString(message, transition));
    }
    public TransitionConfigurationException(String message, InternalTransition<?, ?> transition){
        super(TransitionConfigurationException.errorString(message, transition));
    }
}
