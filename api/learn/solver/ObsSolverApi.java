package BNlearning.api.learn.solver;


import BNlearning.api.learn.solver.win.WinAsobsSolverApi;
import BNlearning.core.learn.solver.ObsSolver;
import BNlearning.core.learn.solver.ScoreSolver;

import java.util.logging.Logger;


public class ObsSolverApi extends ScoreSolverApi {

    private static final Logger log = Logger.getLogger(
            ObsSolverApi.class.getName());

    public static void main(String[] args) {
    	String[] ssStrings = {"s","s"};
        defaultMain(ssStrings, new ObsSolverApi());
    }


    @Override
    protected ScoreSolver getSolver() {
        return new ObsSolver();
    }
}
