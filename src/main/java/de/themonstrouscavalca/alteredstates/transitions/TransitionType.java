package de.themonstrouscavalca.alteredstates.transitions;

public enum TransitionType{
    SIMPLE(false, false), //Simple single from and single defined to state
    MULTIPLE_ORIGIN(true, false), //Complex from (including multiple states or wildcards) singe defined to
    DYNAMIC_DESTINATION(false, true), //Simple single from state, dynamic generator used for toState
    MULTIPLE_ORIGIN_DYNAMIC_DESTINATION(true, true); //Complex from and dynamic to state generator

    private final boolean multipleOrigins;
    private final boolean toGenerator;

    TransitionType(boolean multipleOrigins, boolean toGenerator){
        this.multipleOrigins = multipleOrigins;
        this.toGenerator = toGenerator;
    }

    public boolean isMultipleOrigins(){
        return multipleOrigins;
    }

    public boolean isToGenerator(){
        return toGenerator;
    }

    public static TransitionType of(boolean multipleOrigins, boolean toGenerator){
        for(TransitionType t: TransitionType.values()){
            if(t.multipleOrigins == multipleOrigins && t.toGenerator == toGenerator){
                return t;
            }
        }
        return null;
    }
}
