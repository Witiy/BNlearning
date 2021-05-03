package BNlearning.core.learn.solver.ps;


import BNlearning.core.utils.ParentSet;


public class NullProvider implements Provider {

    @Override
    public ParentSet[][] getParentSets() {
        return null;
    }
}
