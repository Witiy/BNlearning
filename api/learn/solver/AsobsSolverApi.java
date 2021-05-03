package BNlearning.api.learn.solver;


import BNlearning.api.learn.solver.win.WinAsobsSolverApi;
import BNlearning.core.learn.solver.AsobsSolver;
import BNlearning.core.learn.solver.ScoreSolver;

import java.util.logging.Logger;


public class AsobsSolverApi extends ScoreSolverApi {

    public static void main(String[] args) {
    	String[] strings = {"","-j", "E:/DataSet/Alarm/alarm5_data/Alarm5_s5000_v10.jkl",
                "-r", "E:/DataSet/Alarm/alarm5_data/testAsobs2.res",
                "-t", "50", "-b", "0","-check","50"};
        defaultMain(strings, new AsobsSolverApi());
    }



    @Override
    protected ScoreSolver getSolver() {
        return new AsobsSolver();
    }

}
