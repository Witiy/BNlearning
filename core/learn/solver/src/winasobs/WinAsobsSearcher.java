package BNlearning.core.learn.solver.src.winasobs;


import java.util.Random;

import BNlearning.api.learn.solver.win.WinAsobsSolverApi;
import BNlearning.core.learn.solver.WinAsobsSolver;
import BNlearning.core.learn.solver.samp.EntropySampler;
import BNlearning.core.learn.solver.src.WinObsSearcher;
import BNlearning.core.learn.solver.src.asobs.AsobsSearcher;
import BNlearning.core.utils.ParentSet;


/**
 * Hybrid greedy hill exploration
 */
public class WinAsobsSearcher extends WinObsSearcher {

    private int[][] l_parent_var;

    private AsobsSearcher s;
    
    private String entPath;


    public WinAsobsSearcher(WinAsobsSolver solver) {
        super(solver);
        this.max_windows = solver.max_windows;
        this.entPath = solver.entPath;
    }

    public void init(ParentSet[][] scores, int thread) {
        super.init(scores, thread);

        l_parent_var = new int[n_var][];

        s = new AsobsSearcher(solver);
        s.init(m_scores, thread);
     //   t=0;
    }

    public ParentSet[] search() {



    	EntropySampler sampler=new EntropySampler(entPath, n_var, new Random());

    	sampler.init();
    	vars=sampler.sample();
        System.out.println(max_windows);
        asobsOpt();

        winasobs();

        return str;
    }
  


    public void asobsOpt() {
        s.vars = vars;
        s.asobsGain();
        this.vars = s.vars;

    }
}
