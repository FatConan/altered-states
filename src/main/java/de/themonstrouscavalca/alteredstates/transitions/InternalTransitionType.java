package de.themonstrouscavalca.alteredstates.transitions;

public enum InternalTransitionType{
    SIMPLE(false),
    MULTIPLE_STATE(true);

    private final boolean multipleStates;

    InternalTransitionType(boolean multipleStates){
        this.multipleStates = multipleStates;
    }

    public boolean isMultipleStates(){
        return multipleStates;
    }

    public static InternalTransitionType of(boolean multipleStates){
        for(InternalTransitionType t: InternalTransitionType.values()){
            if(t.multipleStates == multipleStates){
                return t;
            }
        }
        return null;
    }
}
