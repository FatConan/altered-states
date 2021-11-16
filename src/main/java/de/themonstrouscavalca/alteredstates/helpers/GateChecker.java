package de.themonstrouscavalca.alteredstates.helpers;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class GateChecker{
    public static boolean checkGate(Boolean ... checks){
        Set<Boolean> checkSet = Arrays.stream(checks).collect(Collectors.toSet());
        return !checkSet.contains(false);
    }
}
