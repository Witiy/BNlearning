package BNlearning.core.learn.solver;


import BNlearning.core.learn.solver.src.Searcher;
import BNlearning.core.learn.solver.src.winasobs.WinAsobsSearcher;

import java.util.HashMap;


/**
 * (given an order, for each variable select the best parent set compatible with the previous assignment).
 */
public class WinAsobsSolver extends WinObsSolver {
    public String entPath;
    @Override
    protected Searcher getSearcher() {
        return new WinAsobsSearcher(this);
    }

    @Override
    public void init(HashMap<String, String> options) {
        super.init(options);
        entPath = gStr("entPath");
    }

}
