package BNlearning.core.learn.solver.src.pewobs;


import BNlearning.core.learn.solver.PermExtensibleWinSolver;
import BNlearning.core.learn.solver.src.ScoreSearcher;
import BNlearning.core.utils.BayesianNetwork;
import BNlearning.core.learn.solver.samp.SimpleSampler;

import BNlearning.core.utils.data.ArrayUtils;
import BNlearning.core.utils.data.SIntSet;
import BNlearning.core.utils.data.common.TIntIterator;
import BNlearning.core.utils.data.set.TIntHashSet;
import BNlearning.core.utils.exp.CyclicGraphException;
import BNlearning.core.utils.other.Clique;
import BNlearning.core.utils.ParentSet;

import java.util.*;

import static BNlearning.core.utils.RandomStuff.f;
import static BNlearning.core.utils.RandomStuff.p;

public class PermExtensibleWinSearcher extends ScoreSearcher {
    private int max_windows;

    private TIntHashSet todo;

    private TreeSet<PermExtensibleWinSearcher.Result> cand;



    private PermExtensibleWinSearcher.Result[] bests;
    public BayesianNetwork new_bn;
    private double[] minSk;
    private double[] maxSk;

    private int[][] parents;

    private static int[] firstVars;

    private static int firstindex;

    private  int rt;


    public int[] vars;


    public boolean[]  joined;

    protected TreeSet<SIntSet> handles;

    private SimpleSampler simpleSampler;

    public List<Clique> junctTree;

    private int limit;


    public ParentSet[] new_str;



    private OptimizeSearcher otmSearcher;


    private int count;

    public PermExtensibleWinSearcher(PermExtensibleWinSolver solver){
        super(solver);
        max_windows = solver.max_windows;
        limit = solver.limit;
    }
    @Override
    public void init(ParentSet[][] scores, int thread) {
        super.init(scores, thread);
        simpleSampler = new SimpleSampler(n_var,new Random());
        firstVars = new int[n_var];
        firstindex = 0;

        for(int i = 0; i < n_var; i++){
            firstVars[i] = i;
        }
        ArrayUtils.shuffleArray(firstVars,new Random());

        vars=new int[n_var];

        minSk = new double[n_var];
        maxSk = new double[n_var];


        for (int i = 0; i < n_var; i++) {
            int j = m_scores[i].length - 1;

            minSk[i] = m_scores[i][j].sk;
            maxSk[i] = m_scores[i][0].sk;

        }

        parents = new int[n_var][];

        for (int i = 0; i < n_var; i++) {
            TIntHashSet l = new TIntHashSet();

            for (ParentSet ps : scores[i]) {
                for (int p : ps.parents) {
                    l.add(p);
                }
            }

            parents[i] = l.toArray();
            Arrays.sort(parents[i]);
        }
        this.otmSearcher = new OptimizeSearcher(this.solver);
        this.otmSearcher.init(m_scores, thread);
        this.otmSearcher.setMax_windows(max_windows);


    }
    private void check() {
        if(new_str == null)
            return;

        BayesianNetwork b = new BayesianNetwork(new_str);

        try {
            b.checkAcyclic();
        } catch (CyclicGraphException e) {
            solver.log("WHHHHHAAAAT");
            System.exit(0);
        }
    }

    protected void clear() {
        new_bn = new BayesianNetwork(n_var);
        new_str = new ParentSet[n_var];
        handles = new TreeSet<SIntSet>();

        junctTree = new ArrayList<Clique>();
        joined=new boolean[n_var];
        count = 0;

    }

    private void done(int v) {
        joined[v]=true;
        todo.remove(v);
        if (bests[v] == null) {
            p("cdfjds");
        }
        cand.remove(bests[v]);
        bests[v] = null;
    }


    private void initCand() {

        todo = new TIntHashSet();
        cand = new TreeSet<Result>();

        bests = new Result[n_var];

        for (int v = 0; v < n_var; v++) {
            todo.add(v);

            Result r = new Result(v, m_scores[v][m_scores[v].length - 1],
                    1.0);
            cand.add(r);
            bests[v] = r;

        }
        vars = new int[n_var];
    }

    private ParentSet bestParentSet(int v) {
        for (ParentSet p : m_scores[v]) {

            if(haveJoined(p.parents)||p.parents==null) {

                return p;
            }
        }
        return null;
    }

    public boolean haveJoined(int[] parents) {

        for(int i:parents) {
            if(joined[i]==false) {
                return false;
            }
        }
        return true;
    }

    private double FinalScore(Result res) {
        joined[res.v]=true;

        TIntIterator it = todo.iterator();
        double GlobalScore=0;

        while (it.hasNext()) {
            int v = it.next();

            ParentSet r = bestParentSet(v);

            if (r.sk == minSk[v]) {
                continue;
            }


            double todoScore= maxSk[v]==r.sk?0:(maxSk[v] - r.sk)/(maxSk[v]-minSk[v]);

            if (todoScore < bests[v].todoScore) {

                GlobalScore+=(bests[v].todoScore -todoScore)*(maxSk[v]-minSk[v]);

            }
        }

        joined[res.v]=false;

        return GlobalScore-(res.todoScore *(maxSk[res.v]-minSk[res.v]));

    }
    protected void update(int v, ParentSet ps) {
        new_bn.setParents(v, ps.parents);
        new_str[v] = ps;
    }
    private void updateBests() {

        TIntIterator it = todo.iterator();

        while (it.hasNext()) {
            int v = it.next();

            ParentSet r = bestParentSet(v);

            if (r == null) {
                continue;
            }

            double todoScore= maxSk[v]==r.sk?0:(maxSk[v] - r.sk)/(maxSk[v]-minSk[v]);

            if (todoScore < bests[v].todoScore) {

                cand.remove(bests[v]);
                Result c = new Result(v, r, todoScore);

                cand.add(c);
                bests[v] = c;

            }
        }
    }

    protected void finalize(Result res) {

        update(res.v, res.ps);
        updateBests();
    }

    private Result bestVar() {


        java.util.Iterator<Result> it = cand.iterator();
        int num=(todo.size() <= limit)?todo.size():limit;

        Result bestRes=it.next();

        double maxFinalScore= FinalScore(bestRes);
        Result res;

        if(todo.size()==1 || bestRes.todoScore ==0.0)
        {

            return bestRes;
        }



        for(int i = 0; i < num; i++) {
            if(it.hasNext())
                res=it.next();
            else
                break;
            double FinalScore= FinalScore(res);

            if(FinalScore > maxFinalScore) {

                bestRes=res;
                maxFinalScore=FinalScore;

            }

        }

        return bestRes;

    }

    public void QuicklyInitOrder(){

        clear();

        initCand();

        Result res;

        System.out.println(limit);
        synchronized (solver.lock){

            if(firstindex < n_var && rt > 0){
                randomOrder();
                rt--;
                return;
            }
            else if(firstindex < n_var){

                res=bests[firstVars[firstindex++]];
                rt = 2;
            }
            else{
                vars = simpleSampler.sample();
                return ;
            }
        }

        vars[count++]=res.v;


        done(res.v);
        finalize(res);

        while (!todo.isEmpty()) {

            res= bestVar();

            vars[count++]=res.v;

            done(res.v);

            finalize(res);

        }
    }

    private void randomOrder(){

        int rt = 4;
        int randomNum = n_var/rt;
        int last = 0;
        for(int i = 0; i < randomNum; i++){
            int index = randInt(last+rt-1, n_var - 1);
            ArrayUtils.swap(vars,randInt(last,last+rt-1) , index);
            last +=rt;
        }


    }

    @Override
    public ParentSet[] search() {


        QuicklyInitOrder();

        new_str= this.otmSearcher.optimizeOrder(vars,m_scores);

        check();

        return new_str;
    }




    protected class Result implements Comparable<PermExtensibleWinSearcher.Result> {

        public double todoScore;
        //   public SIntSet handle;
        public int v;
        public ParentSet ps;
        //   public Clique cl;

        public Result(int v, ParentSet ps, double todoScore) {
            this.ps = ps;
            this.v = v;
            //   this.handle = handle;
            this.todoScore = todoScore;
            //   this.cl = cl;

        }



        @Override
        public int compareTo(PermExtensibleWinSearcher.Result o) {
            if (todoScore > o.todoScore) {
                return 1;
            }
            if (todoScore < o.todoScore) {
                return -1;
            }

            if (v < o.v) {
                return 1;
            }
            if (v > o.v) {
                return -1;
            }

            if (equals(o)) {
                return 0;
            } else {
                return -1;
            }
        }

        @Override
        public String toString() {
            return f("%d %s  %.4f", v, ps.toString(),
                    todoScore);
        }
    }





}
