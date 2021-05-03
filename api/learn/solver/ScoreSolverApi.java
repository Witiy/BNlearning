package BNlearning.api.learn.solver;


import BNlearning.api.Api;
import BNlearning.core.learn.solver.ScoreSolver;
import BNlearning.core.utils.other.IncorrectCallException;
import BNlearning.core.utils.ParentSet;
import BNlearning.core.utils.RandomStuff;
import org.kohsuke.args4j.Option;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import static BNlearning.core.utils.RandomStuff.getWriter;
import static BNlearning.core.utils.RandomStuff.p;


public abstract class ScoreSolverApi extends Api {

    private static final Logger log = Logger.getLogger(
            ScoreSolverApi.class.getName());

    protected ScoreSolver solver;
    @Option(name = "-j", required = true, usage = "Scores input file (in jkl format)")
    protected static String ph_scores;

    @Option(name = "-r", required = true, usage = "result output file. If not supplied, the scores are printed on screen")
    protected static String ph_result;

    @Option(name = "-t", usage = "maximum time limit (seconds)")
    protected int max_exec_time = 1000;

    @Option(name = "-e", usage = "improvement delta")
    protected int delta = 0;

    @Option(name = "-p", usage = "max_parents")
    protected int max_parents = 0;

    @Option(name = "-o", usage = "number of solutions to output")
    protected int out_solutions = 1;

    @Option(name = "-l", usage = "log file (if not given, to output)")
    protected String logPath;


    public ScoreSolverApi() {
        solver = getSolver();
    }



    protected abstract ScoreSolver getSolver();


    @Override
    public void exec() throws Exception {

    	ParentSet[][] sc = RandomStuff.getScoreReader(ph_scores,
                verbose);


        solver.init(options());
        solver.init(sc);
        if (log != null) {
            solver.logWr = getWriter(logPath);
        }
        solver.go(ph_result);


    }

    public void run(String input,String output,String check,int core){
        String[] strings={"", "-j", input,
                "-r", output,
                "-b", String.valueOf(core),
                "-check", check

        };
        defaultMain(strings, this);
    }


    @Override
    protected void check() throws IncorrectCallException {
        if ( ! new File(ph_scores).exists()) {
            throw new IncorrectCallException("Score input file ("+ph_scores +") does not exists.");
        }

        if ( getWriter(ph_result, true) == null) {
            throw new IncorrectCallException("Can't write to result file ("+ph_result +").");
        }

    }


}

