package BNlearning.api.learn.solver.win;


import BNlearning.api.learn.solver.AsobsSolverApi;
import BNlearning.core.learn.solver.ScoreSolver;
import BNlearning.core.learn.solver.WinAsobsSolver;
import org.kohsuke.args4j.Option;


public class WinAsobsSolverApi extends AsobsSolverApi {

    @Option(name = "-win", usage = "Maximum window size")
    protected int win = 5;

    @Option(name = "-ep", usage = "DAT input file for ent")
    protected String entPath;

    public static void main(String[] args)  {
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
                "-ep",input.substring(0,input.length()-3)+"dat",

        };
        defaultMain(strings, new WinAsobsSolverApi());

    }

    @Override
    public void run(String input, String output,String check,int core){
        String[] strings={"", "-j", input,
                "-r", output,
                "-b", String.valueOf(core),
                "-ep",input.substring(0,input.length()-3)+"dat",
                "-check", check

        };
        defaultMain(strings, this);

    }

    @Override
    protected ScoreSolver getSolver() {
        return new WinAsobsSolver();
    }

}
