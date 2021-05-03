package BNlearning.core.learn.solver.samp;


import java.util.Random;


public class SamplerUtils {

    static public Sampler getAdvSampler(String sampler, String dat_path, int n_var, Random rand) {
       if ("ent".equals(sampler)) {
            return new EntropySampler(dat_path, n_var, rand);
        }
        if ("ent_b".equals(sampler)) {
            return new EntropyBSampler(dat_path, n_var, rand);
        }
        else {
            // return new SimpleSampler(sc.n_var);
        	System.out.println(n_var+"  "+rand);
            return new SimpleSampler(n_var, rand);
        }
    }

}
