package BNlearning.core.learn.solver.samp;


import BNlearning.core.utils.DataSet;
import BNlearning.core.utils.analyze.Entropy;

import java.util.Random;

import static BNlearning.core.utils.RandomStuff.getDataSet;


public class EntropySampler implements Sampler {

    protected int n;

    protected Random r;

    protected DataSet dat;

    protected double[] weight;

    protected Object lock = new Object();

    @Override
    public int[] sample() {
    
        return sampleWeighted(n, weight);
    }

    public EntropySampler(String ph_dat,int n, Random r) {
    	 
        dat = getDataSet(ph_dat);
        this.n = n;
        this.r = r;
    }

    @Override
    public void init() {

        weight = new double[n];
        Entropy e = new Entropy(dat);
        //System.out.println("aaaaa");
        for (int i = 0; i < n; i++) {
            weight[i] = e.computeH(i);
        }
    }

    public int[] sampleWeighted(int n, double[] weights) {

        int[] new_ord = new int[n];

        synchronized (lock) {
    //        System.out.println("Weights");
    //    	for(double x:weights)
     //   		System.out.println(String.format("%.2f", x));
            
            boolean[] selected = new boolean[n];

            for (int j = 0; j < n; j++) {

                double tot = 0;

                for (int i = 0; i < n; i++) {
                    if (!selected[i]) {
                        tot += weights[i];
                    }
                }
       //         System.out.println("tot="+String.format("%.2f", tot));
                double v = r.nextDouble() - Math.pow(2, -10);
                int sel = -1;
             //   System.out.println("v: "+v);
 //             if(j<5) {
                for (int i = 0; i < n && sel == -1; i++) {
                    if (!selected[i]) {
                        double s = weights[i] / tot;
                //        System.out.println("variable."+i+"  "+String.format("%.2f", s)+"v: "+v);
                        if (s <= 0 || v <= s) {
                    //    	System.out.println((s<=0)+" "+(v<=s));
                            sel = i;
                        }
                        v -= s;
                    }
                }
                selected[sel] = true;
                new_ord[j] = sel;
                
                
                
                
 /*               }
               else {
                	double min=1.1;
                	
                
                    for (int i = 0; i < n ; i++) {
                        if (!selected[i]) {
                            double s = weights[i] / tot;
                            if (s<min) {
                            	min=s;
                                sel = i;
                        //        System.out.println("sel: "+sel);
                            }
                           
                        }
                    }
					
				}
*/
                // p(sel);

               

                // Entropy e = new Entropy(dat);
                // pf("%.4f ", e.computeH(sel));
            }
        }
        // p("");
       
        return new_ord;
    }
}
