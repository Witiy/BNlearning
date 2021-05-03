package BNlearning.api;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.TreeSet;
import java.util.logging.Logger;

import static BNlearning.core.utils.RandomStuff.closeIt;
import static BNlearning.core.utils.RandomStuff.f;
import static BNlearning.core.utils.RandomStuff.logExp;


/**
 * Main point of execution
 */
class Blip {

    private static final Logger log = Logger.getLogger(Blip.class.getName());

    private static String version = "0.9";

    /**
     * Command line invocation
     *
     * @param args parameter for the requested methods
     */
    public static void main(String[] args) {

        Blip b = new Blip();
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        TreeSet<Api> m = b.getClazzApis();
        System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        if (args.length == 0) {
            printListApis(m);
            return;
        }

        String nm = args[0].toLowerCase().trim();

        if ("help".equals(nm)) {
            printListApis(m);
            return;
        }

        Api found = null;

        for (Api k : m) {
            if (nm.equals(k.nm)) {
                found = k;
                break;
            }
        }

        if (found == null) {
            System.out.printf(
                    "Sorry, I didn't understand %s. Can you repeat? \n \n", nm);
            printListApis(m);
            return;
        }

        try {
            Class<?> clazz = Class.forName(found.c.className);
            Method meth = clazz.getMethod("main", String[].class);

            meth.invoke(null, (Object) args);
        } catch (Exception exp) {
            logExp(log, exp);
        }
    }

    private static void printListApis(TreeSet<Api> m) {

        String av = f("Welcome to BLiP (Bayesian Learning Package). Version: %s. Available tasks: \n\n", version);
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrr");
        // String exp = "Work in progress commands (use at your own risk): \n\n";

        for (Api k : m) {

            if (k.nm.startsWith("#")) {
                continue;
            }

            String x = String.format("    # %20s -> %s\n", k.nm, k.c.description);

            av += x;

        }

        System.out.println(av);
    }

    private TreeSet<Api> getClazzApis() {
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        TreeSet<Api> m = new TreeSet<Api>();

        BufferedReader br = null;

        try {
            System.out.println("000000000000000000000000000000000000");
//            br = new BufferedReader(
//                    new InputStreamReader(
//                            getClass().getClassLoader().getResourceAsStream(
//                                    "clazzes")));

             br  = new BufferedReader(new  InputStreamReader(new FileInputStream("C:\\Users\\Witiy\\Desktop\\for_eclipse\\srf_blip\\clazzes")));

            String line = br.readLine();

            System.out.println("1111111111111111111111111111111111111111111111");

            while (line != null) {
                System.out.println("222222222222222222222222222222222");
                Clazz c = new Clazz();

                String nm = line.substring(0, line.indexOf(" "));

                line = line.substring(line.indexOf(" ")).trim();

                c.className = line.substring(0, line.indexOf(" "));
                c.description = line.substring(line.indexOf(" ")).trim();

                m.add(new Api(nm, c));

                line = br.readLine();
            }

        } catch (Exception e) {
            logExp(log, e);
        } finally {
            closeIt(log, br);
        }

        return m;
    }

    private static class Clazz {
        String description;
        String className;
    }


    private static class Api implements Comparable<Api> {
        final Clazz c;
        final String nm;

        public Api(String nm, Clazz c) {
            this.nm = nm;
            this.c = c;
        }

        public int compareTo(Api other) {
            return nm.compareTo(other.nm);
        }
    }
}
