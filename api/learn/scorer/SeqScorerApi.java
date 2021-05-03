package BNlearning.api.learn.scorer;


import BNlearning.core.learn.scorer.BaseScorer;
import BNlearning.core.learn.scorer.SeqScorer;

import java.util.logging.Logger;


public class SeqScorerApi extends ScorerApi {

    private static final Logger log = Logger.getLogger(
            SeqScorerApi.class.getName());

    public static void main(String[] args) {
        defaultMain(args, new SeqScorerApi());
    }

    @Override
    protected BaseScorer getScorer() {
        return new SeqScorer();
    }
}
