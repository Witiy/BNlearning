package BNlearning.core.utils.exp;


import BNlearning.core.utils.BayesianNetwork;


public class CyclicGraphException extends Exception {

    private final BayesianNetwork bn;

    public CyclicGraphException(BayesianNetwork bn) {
        super("Found a cyclic bayesian network!");
        this.bn = bn;
    }
}
