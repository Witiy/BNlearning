package BNlearning.core.learn.solver.ps;


import BNlearning.core.utils.ParentSet;


public class SimpleProvider implements Provider {

    protected ParentSet[][] sc;

    public SimpleProvider(ParentSet[][] sc) {
        this.sc = sc;
    }

    @Override
    public ParentSet[][] getParentSets() {
        return sc;
    }
}
