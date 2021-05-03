package BNlearning.api.learn.solver;

import BNlearning.api.learn.solver.win.WinObsSolverApi;
import BNlearning.core.learn.solver.PermExtensibleWinSolver;
import BNlearning.core.learn.solver.ScoreSolver;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.logging.Logger;


public class PermExtensibleWinSolverApi extends WinObsSolverApi {

    @Option(name = "-lm", usage = "Limit the number of Vars computing FinalScore")
    protected int limit = 10;

    private static final Logger log = Logger.getLogger(
            PermExtensibleWinSolverApi.class.getName());



    public static void main(String[] args) throws IOException {
        String input = "E:\\DataSet\\Link\\link_data\\Link_s5000_v1.jkl";
        String output = input.substring(0,input.lastIndexOf("."))+"test2.res";
        System.out.println(input);
        System.out.println(output);
        int time = 180;
        int coreNum = 0;
        String[] strings={"", "-j", input,
                "-r", output,
                "-t", String.valueOf(time),
                "-b", String.valueOf(coreNum),


        };


        defaultMain(strings, new PermExtensibleWinSolverApi());

    }


    @Override
    protected ScoreSolver getSolver() {
        return new PermExtensibleWinSolver();
    }

}
