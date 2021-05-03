package BNlearning.api.learn.scorer;


import BNlearning.core.learn.scorer.AdvK2;
import BNlearning.core.learn.scorer.BaseScorer;

import java.util.logging.Logger;


public class AdvK2ScorerApi extends ScorerApi {

    public static void main(String[] args) {
        defaultMain(args, new AdvK2ScorerApi());
    }

    @Override

    protected BaseScorer getScorer() {
        return new AdvK2();
    }
}
