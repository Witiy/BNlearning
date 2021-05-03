package BNlearning.api.learn.scorer;


import BNlearning.api.Api;
import BNlearning.core.utils.DataSet;
import BNlearning.core.learn.scorer.BaseScorer;
import BNlearning.core.utils.other.IncorrectCallException;
import org.kohsuke.args4j.Option;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

import static BNlearning.core.utils.RandomStuff.getDataSet;
import static BNlearning.core.utils.RandomStuff.getWriter;
import static BNlearning.core.utils.RandomStuff.p;


abstract class ScorerApi extends Api {

    private static final Logger log = Logger.getLogger(ScorerApi.class.getName());

    @Option(name = "-d", required = true, usage = "Datafile path (.dat format)")
    public static String ph_dat;
//    public String ph_dat= "C:\\Users\\Witiy\\Desktop\\for_eclipse\\srf_blip\\test.dat";
    @Option(name = "-j", required = true, usage = "Parent set scores output file (jkl format)")
    public String ph_scores;
 //   public String ph_scores="C:\\Users\\Witiy\\Desktop\\for_eclipse\\srf_blip\\test.jkl";
    @Option(name = "-n", usage = "Maximum learned in-degree")
    public int max_pset_size = 6;

    @Option(name = "-t", usage = "Maximum time (if 0, default 60 seconds for variable)")
    public int max_exec_time = 100;

    @Option(name = "-c", usage = "Chosen score function. Possible choices: BIC, BDeu")
    public String scoreNm = "bdeu";

    @Option(name = "-a", usage = "(if BDeu is chosen) equivalent sample size parameter")
    public Double alpha = 1.0;

    @Option(name = "-u", usage = "Search only the selected variable (ex: '3' or '1-10')")
    public String choice_variables = "";

    private BaseScorer scorer;

    public ScorerApi() {
        scorer = getScorer();
    }

    @Override
    public void exec() throws Exception {
        DataSet d = getDataSet(ph_dat);

        scorer.init(options());
        scorer.go(d);
    }

    @Override
    protected void check() throws IncorrectCallException {
        if ( ! new File(ph_dat).exists()) {
            throw new IncorrectCallException( "Data input file  ("+ph_dat +") does not exists.");
        }

        if ( getWriter(ph_scores, true) == null) {
            throw new IncorrectCallException( "Can't write to score result file ("+ph_scores +").");
        }
    }

    protected abstract BaseScorer getScorer();
    


}

