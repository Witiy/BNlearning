package BNlearning.core.utils.other;


import BNlearning.core.utils.BayesianNetwork;
import BNlearning.core.io.bn.BnNetWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static BNlearning.core.utils.RandomStuff.logExp;


public class BetterNets  {

    private static final Logger log = Logger.getLogger(
            BetterNets.class.getName());

    private static final Pattern ptrn = Pattern.compile(
            "node \\(([^\\\\)]+)\\) \\{([^\\}]+)\\}");

    public void go(BayesianNetwork bn, String s, int max_time) {


        getPositions(bn, s);

        BnNetWriter.ex(bn, s + "-new.net");
    }

    private void getPositions(BayesianNetwork bn, String s) {
        bn.positions = new HashMap<String, double[]>();
        try {
            BufferedReader r = new BufferedReader(new FileReader(s + ".plain"));
            String content;

            while (r.ready()) {
                content = r.readLine().trim();
                if (!content.startsWith("node")) {
                    continue;
                }
                String[] g = content.split(" ");
                double[] p = new double[2];

                p[0] = Double.valueOf(g[2]) * 100;
                p[1] = Double.valueOf(g[3]) * 100;
                bn.positions.put(g[1], p);
            }
        } catch (Exception e) {
            logExp(log, e);
        }
    }

}
