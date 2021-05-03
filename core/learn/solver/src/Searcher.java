package BNlearning.core.learn.solver.src;


import BNlearning.core.utils.ParentSet;


/**
 * New search following the given order
 */
public interface Searcher {

    ParentSet[] search();

    void init(ParentSet[][] scores, int thread);

}
