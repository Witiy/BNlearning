package BNlearning.core.learn.solver.src.pewobs;



import BNlearning.core.learn.solver.BaseSolver;
import BNlearning.core.learn.solver.src.obs.ObsSearcher;
import BNlearning.core.utils.data.ArrayUtils;
import BNlearning.core.utils.ParentSet;
import BNlearning.core.utils.RandomStuff;

import java.util.ArrayList;
import java.util.Random;

import static BNlearning.core.utils.data.ArrayUtils.swap;


/**
 * Hybrid greedy hill exploration
 */
public class OptimizeSearcher extends ObsSearcher {

    protected int max_windows;

    private int[] todo;

    protected int window = 1;


    private boolean[] b_forbidden;

    private boolean[] f_forbidden;

    private ParentSet[] emptyS;

    protected int[] work_vars;

    protected ParentSet[] work_str;

    protected double work_sk;

    protected boolean gain;

    protected ParentSet[] best_str;

    public int[] best_vars;

    protected double best_sk;



    public static int s=0;
    public static int f=0;


    ArrayList<Integer> promoteList;

    protected Random r = new Random(System.currentTimeMillis());

    public OptimizeSearcher(BaseSolver solver) {

        super(solver);

    }

    public void setMax_windows(int max_windows) {
        this.max_windows = max_windows;
    }

    public ParentSet[] optimizeOrder(int[] new_vars, ParentSet[][] new_score) {

        System.out.println(max_windows);

        int maxPerm= max_windows;

        vars=new_vars;

        initStr();
        sk=checkSk();

        m_scores=new_score;


        updateTodo();
        int cnt = 0;

        window = 1;

        int Perm_index=0;

        while(Perm_index <= n_var-maxPerm) {

            while (window <= max_windows) {
                solver.checkTime();

                if (!solver.still_time) {
                    return null;
                }
                if (cnt < n_var - 1) {
                    int index = randInt(cnt + 1, n_var - 1);

                    ArrayUtils.swap(todo, cnt, index);

                }
                if (extensible_greedy(todo[cnt])) {
                    cnt = 0;

                    updateTodo();

                    window = 1;

                } else {
                    cnt++;

                }

                if (cnt > n_var-1) {

                    window += 1;

                    cnt = 0;


                }

            }

            for(Perm_index=0; Perm_index <= n_var-maxPerm; Perm_index++) {
                if(PermSearch(Perm_index, Perm_index + maxPerm-1, maxPerm)) {

                    RandomStuff.cloneStr(best_str, str);
                    sk = best_sk;
                    ArrayUtils.cloneArray(best_vars, vars);
                    window=1;
                    cnt=0;

                    break;
                }

            }
        }


        if(best_sk < solver.best_sk)
            return null;
        else
            return str;

    }
    public ParentSet[] search() {

        vars = smp.sample();

        optimizeOrder(vars,m_scores);


        return str;
    }

    public void init(ParentSet[][] scores, int thread) {
        super.init(scores, thread);

        todo = new int[n_var];
        for (int i = 0; i < n_var; i++) {
            todo[i] = i;
        }
        f_forbidden = new boolean[n_var];
        b_forbidden = new boolean[n_var];

        emptyS = new ParentSet[n_var];
        for (int i = 0; i < n_var; i++) {
            emptyS[i] = m_scores[i][(m_scores[i].length - 1)];
        }
        work_vars = new int[n_var];

        work_str = new ParentSet[n_var];

        best_str = new ParentSet[n_var];

        best_vars = new int[n_var];
    }
    public boolean extensible_greedy(int pivot) {

        gain = false;
        best_sk = sk;



        int ex = 0;
        while(window>1&&promoteList.contains(pivot+window+ex)) {

            ex++;


        }

        window += ex;


        prepareForbidden(pivot,true);

        prepareSearch();
        forbidden = f_forbidden;
        if (window == 1) {
            greedy1Forward(pivot);
        } else if (window == 2) {
            greedy2Forward(pivot);
        } else {
            greedyNForward(pivot);
        }

        window -= ex;

        prepareSearch();
        forbidden = b_forbidden;
        if (window == 1) {
            greedy1Backward(pivot);
        } else if (window == 2) {
            greedy2Backward(pivot);
        } else {
            greedyNBackward(pivot);
        }

        if (!gain) {


            return false;
        }

        RandomStuff.cloneStr(best_str, str);
        sk = best_sk;
        ArrayUtils.cloneArray(best_vars, vars);


        return true;
    }


    private void greedyNBackward(int pivot) {
        for (int ix = pivot; ix >= window; ix--) {
            varSwitchNBackward(ix);
            checkBest();
        }
    }

    private void greedy2Backward(int pivot) {
        for (int ix = pivot; ix >= window; ix--) {
            varSwitch2Backward(ix);
            checkBest();
        }
    }

    private void greedy1Backward(int pivot) {
        for (int ix = pivot; ix >= window; ix--) {
            varSwitch1(ix, false);
            checkBest();
        }
    }


    private void greedyNForward(int pivot) {
        for (int ix = pivot; ix < n_var - window; ix++) {
            varSwitchNForward(ix);
            checkBest();
        }
    }

    private void greedy2Forward(int pivot) {
        for (int ix = pivot; ix < n_var - window; ix++) {
            varSwitch2Forward(ix);
            checkBest();
        }
    }

    private void greedy1Forward(int pivot) {
        for (int ix = pivot; ix < n_var - window; ix++) {
            varSwitch1(ix, true);
            checkBest();
        }
    }

    private void prepareForbidden(int pivot,boolean flag) {

        if (pivot > n_var / 2) {
            ArrayUtils.cloneArray(voidB, f_forbidden);
            for (int i = n_var - 1; i > pivot; i--) {
                if(flag)
                    f_forbidden[vars[i]] = true;
                else
                    f_forbidden[work_vars[i]] = true;
            }
        } else {
            ArrayUtils.cloneArray(fullB, f_forbidden);
            for (int i = 0; i <= pivot; i++) {
                if(flag)
                    f_forbidden[vars[i]] = false;
                else
                    f_forbidden[work_vars[i]] = false;
            }
        }
        ArrayUtils.cloneArray(f_forbidden, b_forbidden);

    }

    private void varSwitchNBackward(int ix) {
        int x = work_vars[(ix - window)];
        boolean improve_x = false;

        for (int i = 0; i < window; i++) {
            int a = work_vars[(ix - i)];

            forbidden[a] = false;
            if (cand[x][a]) {
                improve_x = true;
            }
        }
        if (improve_x) {
            bests(x);
        }
        forbidden[x] = true;
        for (int i = 0; i < window; i++) {
            int a = work_vars[(ix - i)];

            if (find(x, work_str[a].parents)) {
                bests(a);
            }
            forbidden[a] = true;
        }
        int t = work_vars[(ix - window)];

        for (int i = window; i > 0; i--) {
            work_vars[(ix - i)] = work_vars[(ix - (i - 1))];
        }
        work_vars[ix] = t;

    }

    private void varSwitchNForward(int ix) {
        int x = work_vars[(ix + window)];
        boolean find_x = false;

        for (int i = 0; i < window; i++) {
            int a = work_vars[(ix + i)];

            forbidden[a] = true;
            if (find(a, work_str[x].parents)) {
                find_x = true;
            }
        }
        if (find_x) {
            bests(x);
        }
        forbidden[x] = false;
        for (int i = 0; i < window; i++) {
            int a = work_vars[(ix + i)];

            if (cand[a][x]) {
                bests(a);
            }
            forbidden[a] = false;
        }
        int t = work_vars[(ix + window)];

        for (int i = window; i > 0; i--) {
            work_vars[(ix + i)] = work_vars[(ix + (i - 1))];
        }
        work_vars[ix] = t;
    }

    private void varSwitch2Backward(int ix) {
        int a = work_vars[ix];
        int b = work_vars[(ix - 1)];
        int x = work_vars[(ix - 2)];

        forbidden[a] = false;
        forbidden[b] = false;
        if ((cand[x][a]) || (cand[x][b])) {
            bests(x);
        }
        forbidden[x] = true;
        if (find(x, work_str[a].parents)) {
            bests(a);
        }
        forbidden[a] = true;
        if (find(x, work_str[b].parents)) {
            bests(b);
        }
        int t = work_vars[(ix - 2)];

        work_vars[(ix - 2)] = work_vars[(ix - 1)];
        work_vars[(ix - 1)] = work_vars[ix];
        work_vars[ix] = t;
    }

    private void varSwitch2Forward(int ix) {
        int a = work_vars[ix];
        int b = work_vars[(ix + 1)];
        int x = work_vars[(ix + 2)];

        forbidden[a] = true;
        forbidden[b] = true;
        if ((find(a, work_str[x].parents))
                || (find(b, work_str[x].parents))) {
            bests(x);
        }
        forbidden[x] = false;
        if (cand[a][x]) {
            bests(a);
        }
        forbidden[a] = false;
        if (cand[b][x]) {
            bests(b);
        }
        int t = work_vars[(ix + 2)];

        work_vars[(ix + 2)] = work_vars[(ix + 1)];
        work_vars[(ix + 1)] = work_vars[ix];
        work_vars[ix] = t;
    }

    private void varSwitch1(int ix, boolean forward) {
        int x;
        int a;

        if (forward) {
            a = work_vars[ix];
            x = work_vars[(ix + 1)];
        } else {
            a = work_vars[(ix - 1)];
            x = work_vars[ix];
        }
        forbidden[a] = true;
        if (find(a, work_str[x].parents)) {
            bests(x);
        }
        forbidden[x] = false;
        if (cand[a][x]) {
            bests(a);
        }
        if (forward) {
            ArrayUtils.swapArray(work_vars, ix, ix + 1);

        } else {
            ArrayUtils.swapArray(work_vars, ix, ix - 1);
        }
    }

    protected void checkBest() {
        if (work_sk + eps > best_sk) {

            best_sk = work_sk;
            RandomStuff.cloneStr(work_str, best_str);
            ArrayUtils.cloneArray(work_vars, best_vars);
            gain = true;
        }
    }

    protected void prepareSearch() {
        RandomStuff.cloneStr(str, work_str);
        ArrayUtils.cloneArray(vars, work_vars);
        work_sk = sk;
    }

    public void initStr() {
        ArrayUtils.cloneArray(voidB, forbidden);
        RandomStuff.cloneStr(emptyS, work_str);

        for (int i = n_var - 1; i >= 0; i--) {

            forbidden[vars[i]] = true;
            bests(vars[i]);
        }

        RandomStuff.cloneStr(work_str, str);
        sk = checkSk();
    }



    protected void bests(int a) {
        old_sk = work_str[a].sk;


        check:
        for (ParentSet pSet : m_scores[a]) {

            for (int p : pSet.parents) {
                if (forbidden[p]) {
                    continue check;
                }
            }
            work_str[a] = pSet;
            work_sk = (work_sk + pSet.sk - old_sk);

            return;
        }
    }

    private void updateTodo() {
        promoteList = new ArrayList<Integer>();
        for(int i=0; i<n_var; i++) {

            if(str[vars[i]].sk == m_scores[vars[i]][0].sk) {

                promoteList.add(i);
            }
        }



    }

    private boolean PermSearch(int k,int m,int l) {
        gain = false;
        RandomStuff.cloneStr(str, best_str);
        best_sk = sk;
        ArrayUtils.cloneArray(vars, best_vars);

        prepareSearch();
        Perm(k, m, l);

        return gain;
    }
    private void Perm(int k ,int m,int l)
    {

        if(k==m)
        {

            prepareForbidden(k-l+1,false);
            forbidden = f_forbidden;


            for(int i=k-l+1;i<=k;i++) {

                forbidden[work_vars[i]] = false;
                bests(work_vars[i]);


            }
            checkBest();

        }
        else{
            for(int i=k;i<=m;i++)
            {
                swap(work_vars,i,k);

                Perm(k+1,m,l);
                swap(work_vars,i,k);
            }
        }

    }

}
