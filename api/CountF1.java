package BNlearning.api;

import java.io.*;
import java.util.ArrayList;

public class CountF1 {
    //��.res��ʽ�������ʽת�ɾ�����ʽ��Ϊʹ��Ӧ��ʦ���ı�׼���磩
    static String res_To_matrixPath ;
    //ʵ�������path�����鿴����������
    static String trueGraph;

    static String ph_result;

    static String ph_scores;

    static int size=0;
    static double getSk() throws IOException {
        File file = new File(ph_result);
        BufferedReader br = new BufferedReader(new FileReader(file));

        String str=null,next;

        while((next = br.readLine())!=null){
            str = next;
        }

        String sk[] = str.split(" ");
        return Double.valueOf(sk[1]);

    }
    static void changeToMatrix() throws IOException {//�Ӹ�����ʾ�������ʾ
        File rfile = new File(ph_result);
        File wfile = new File(res_To_matrixPath);
        BufferedReader bf = new BufferedReader(new FileReader(rfile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(wfile));
        String str;
        int count = -3;
        for (int i = 0; (str = bf.readLine()) != null; i++) {
            count++;
        }

        String[][] matrix = new String[count][count + 1];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length - 1; j++) {
                matrix[i][j] = "0  ";
            }
            matrix[i][matrix[i].length - 1] = "\n";
        }
        bf.close();
        bf = new BufferedReader(new FileReader(rfile));
        //����һ��
        str = bf.readLine();
        for (int i = 0; (str = bf.readLine()) != null; i++) {
            if (str.contains("(")) {
                String[] newStr = str.substring(str.indexOf("(") + 1, str.lastIndexOf(")")).split(",");
                for (int j = 0; j < newStr.length; j++) {
                    int r = Integer.parseInt(newStr[j]);
                    matrix[r][i] = "1  ";
                }
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                bw.write(matrix[i][j]);
                bw.flush();
            }
        }
        bw.close();
        bf.close();
    }

    static double[] countFMeasure() throws IOException {//����F-Measure
        File pfile = new File(res_To_matrixPath);
        BufferedReader pf = new BufferedReader(new FileReader(pfile));

        File tfile = new File(trueGraph);
        BufferedReader tf = new BufferedReader(new FileReader(tfile));


        String str;
        int count = 0;
        for (int i = 0; (str = pf.readLine()) != null; i++) {
            count++;
        }

        pf.close();
        pf=new BufferedReader(new FileReader(pfile));
        String[][] Pmatrix = new String[count][count];
        String[][] Tmatrix = new String[count][count];

        for (int i = 0; (str = pf.readLine()) != null; i++) {
            Pmatrix[i] = str.split("  ");
        }

        for (int i = 0; (str = tf.readLine()) != null; i++) {
            Tmatrix[i] = str.split("  ");
        }

        //int allPossibility = count * (count - 1);
        double TP = 0, FN = 0, FP = 0, TN = 0, TPandFN = 0, TPandFP = 0;
        for (int i = 0; i < Pmatrix.length; i++) {
            for (int j = 0; j < Pmatrix[i].length; j++) {
                if ("1".equals(Pmatrix[i][j]) && "1".equals(Tmatrix[i][j])) {
                    TP += 1;
                }
                if ("1".equals(Pmatrix[i][j])) {
                    TPandFP += 1;
                }
                if ("1".equals(Tmatrix[i][j])) {
                    TPandFN += 1;
                }
            }
        }
        double P = TP / TPandFP;
        double R = TP / TPandFN;
        double FMeasure = (2 * P * R) / (P + R);
        double SUM = count*(count-1);
        FN = TPandFN - TP;
        FP = TPandFP - TP;
        TN = SUM - TP - FN - FP;
        System.out.println("Accuracy\tPrecision\tRecall\tF1-scorer");
        double acc=(TP+TN)/(TP+TN+FP+FN);
        System.out.println(acc+"\t"+P+"\t"+R+"\t"+FMeasure);
        System.out.println();
        System.out.println("*******************************************************");
        System.out.println();
        double sk = getSk();
        return new double[]{acc,P,R,FMeasure,sk};

    }

    public static double[][] computeAll(String input,String output,String check) throws IOException {
        String[] timeStrings= check.split(" ");

        ArrayList<Double> times = new ArrayList<Double>();
        for(String ts: timeStrings ){

            times.add(Double.valueOf(ts));
        }

        double[][] scores = new double[times.size()+1][5];
        int i = 0;
        for (double time:times){

            System.out.println(output.substring(0,output.length()-4)
                    + "_"+String.valueOf(time)
                    + ".res");
            scores[i++]=compute( input,output.substring(0,output.length()-4)
                    + "_"+String.valueOf(time)
                    + ".res");
        }
        return scores;

    }

    public static double[][] computeAdv(String jklPath,String incomPath,String check,String api) throws IOException {
        String[] timeStrings= check.split(" ");

        String pre = incomPath.substring(0, incomPath.lastIndexOf("\\"));
        String after = incomPath.substring(incomPath.lastIndexOf("\\"));

        double[][] scores = new double[timeStrings.length][5];
        double[][] result = new double[timeStrings.length][5];
        for(int j = 1; j<= 2; j++){
            String output = pre +"\\"+ api+ after+"_v"+String.valueOf(j)+".res";
            scores = computeAll(jklPath,output,check);
            System.out.println();
            for(int k = 0; k <timeStrings.length; k++){
                for(int r = 0; r < 5; r++){
                    result[k][r]+=scores[k][r];
                }
            }
        }
        for(int k = 0; k <timeStrings.length; k++){
            for(int r = 0; r < 5; r++){
                result[k][r]/=10;
            }
        }

        return result;

    }

    public static double[] compute(String jkl,String res) throws IOException {
        ph_scores = jkl;
        ph_result = res;
        String pre = ph_scores.substring(0, ph_scores.lastIndexOf("\\"));
        String after = ph_scores.substring(ph_scores.lastIndexOf("\\"));
        String pre1 = ph_result.substring(0,ph_result.lastIndexOf("\\"));
        trueGraph = pre +
                after.substring(0,after.indexOf("_"))
                +"_graph.txt";

        res_To_matrixPath = pre1 + "\\matrix"+ after.substring(0,after.length()-3) + "txt";



        changeToMatrix();
        return countFMeasure();

    }

    public static void main(String[] args) throws IOException {
        CountF1 c = new CountF1();
        c.ph_scores = "E:\\DataSet\\Alarm\\alarm5_data\\Alarm5_s5000_v1.dat";
        c.ph_result = "E:\\DataSet\\Alarm\\alarm5_data\\WIN\\Alarm5_s5000_v1_100.0.res";
        String pre = ph_scores.substring(0, ph_scores.lastIndexOf("\\"));
        String after = ph_scores.substring(ph_scores.lastIndexOf("\\"));
        String pre1 = ph_result.substring(0,ph_result.lastIndexOf("\\"));
        trueGraph = pre +
                after.substring(0,after.indexOf("_"))
                +"_graph.txt";
        System.out.println(c.ph_scores);
        System.out.println(c.ph_result);
        System.out.println(trueGraph);
        res_To_matrixPath = pre1 + "\\matrix"+ after.substring(0,after.length()-3) + "txt";



        c.changeToMatrix();
        double[] scores = c.countFMeasure();
        for(double s:scores)
            System.out.printf(" ", s);
        System.out.println();
    }


}
