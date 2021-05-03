package BNlearning.core.learn.solver;

import BNlearning.core.learn.solver.ps.Provider;
import BNlearning.core.learn.solver.ps.SimpleProvider;
import BNlearning.core.learn.solver.samp.Sampler;
import BNlearning.core.learn.solver.src.Searcher;
import BNlearning.core.learn.solver.src.pewobs.PermExtensibleWinSearcher;

import java.util.HashMap;

import static BNlearning.core.learn.solver.samp.SamplerUtils.getAdvSampler;

public class PermExtensibleWinSolver extends WinObsSolver{
    public int limit;

    @Override
    protected String name() {
        return "PERM EXTENSIBLE WIN";
    }


    @Override
    protected Searcher getSearcher() {

        return  new PermExtensibleWinSearcher(this);
    }

    @Override
    public void init(HashMap<String, String> options) {
        super.init(options);
        limit = gInt("limit",10);

    }

}
