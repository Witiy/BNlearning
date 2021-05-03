package BNlearning.core.learn.solver.ps;


import BNlearning.core.utils.ParentSet;


/**
 * Provides the parent set guiding the exploration
 */
public interface Provider {

    public ParentSet[][] getParentSets();
}
