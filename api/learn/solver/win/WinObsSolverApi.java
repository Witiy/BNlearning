package BNlearning.api.learn.solver.win;


import BNlearning.api.learn.solver.ScoreSolverApi;
import BNlearning.core.learn.solver.ScoreSolver;
import BNlearning.core.learn.solver.WinObsSolver;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.logging.Logger;


public class WinObsSolverApi extends ScoreSolverApi {

    @Option(name = "-win", usage = "Maximum window size")
    protected int win = 5;

    public static void main(String[] args) throws IOException {
    	String[] strings={"s","s"};
        defaultMain(strings, new WinObsSolverApi());
    }

    @Override
    protected ScoreSolver getSolver() {
        return new WinObsSolver();
    }

}
