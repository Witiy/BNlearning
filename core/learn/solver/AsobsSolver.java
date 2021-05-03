package BNlearning.core.learn.solver;


import BNlearning.core.learn.solver.src.Searcher;
import BNlearning.core.learn.solver.src.asobs.AsobsSearcher;


import static BNlearning.core.utils.RandomStuff.f;


/**
 * (given an order, for each variable select the best parent set compatible with the previous assignment).
 */
public class AsobsSolver extends ObsSolver {

    @Override
    protected Searcher getSearcher() {

        return new AsobsSearcher(this);
    }

    @Override
    protected String name() {
        return f("Asobs %s", searcher);
    }

}
