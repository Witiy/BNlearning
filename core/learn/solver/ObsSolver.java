package BNlearning.core.learn.solver;


import BNlearning.core.learn.solver.samp.Sampler;
import BNlearning.core.learn.solver.src.Searcher;
import BNlearning.core.learn.solver.src.obs.ObsSearcher;

import java.util.HashMap;

import static BNlearning.core.learn.solver.samp.SamplerUtils.getAdvSampler;


/**
 * (given an order, for each variable select the best parent set that doesn't contain any preceding variable)
 */
public class ObsSolver extends ScoreSolver {

    public String sampler;

    public String searcher;

    public ObsSolver(String search) {
        this.searcher = search;
    }

    public ObsSolver() {}

    @Override
    public void prepare() {
        super.prepare();

        if (verbose > 0) {
            log("sampler: " + sampler + "\n");
            log("sercher: " + searcher + "\n");
        }
    }

    @Override
    public void init(HashMap<String, String> options) {
        super.init(options);
        sampler = gStr("sampler", null);
        searcher = gStr("sercher", null);
    }

    @Override
    public Sampler getSampler() {
        return getAdvSampler(sampler, dat_path, sc.length, this.rand);
    }

    @Override
    protected String name() {
        return "OBS";
    }

    @Override
    protected Searcher getSearcher() {
        
        return new ObsSearcher(this);
    }

    public void initAdv(String searcher) {
        this.searcher = searcher;
    }

    public void initAdv(String dat_path, String sampler, String searcher) {
        this.dat_path = dat_path;
        this.sampler = sampler;
        initAdv(searcher);
    }

}
