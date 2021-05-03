package BNlearning.api;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ProcessSLL {
    static private String sll="C:\\Users\\79951\\Desktop\\for_eclipse\\srf_blip\\DataSet\\Alarm\\alarm5_data\\Alarm5_s5000_v10_sll_100s.jkl";

    static int size=0;
    static int addCardinalNumber() throws IOException {//������������
        String pre = sll.substring(0, sll.lastIndexOf("\\"));
        String after = sll.substring(sll.lastIndexOf("\\"));
        String ph_dat = pre +
                after.substring(0,after.indexOf("_"))
                +"_s5000_v10.dat";
        System.out.println(ph_dat);

        File file=new File(ph_dat);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            int j=0;
            String line;
            String[] contentStrings;
            ArrayList<ArrayList<Integer>> tree=new ArrayList<ArrayList<Integer>>();
            String rewriteString="";
            //�������
            line=reader.readLine();
            contentStrings=line.split(" ");
            for(String v:contentStrings) {
                //System.out.println(v);
                if(!v.equals("")) {
                    size++;
                }
            }
            reader.close();
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return size;
    }


    static <T> void getdescend() throws IOException {


        File file = new File(sll);
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String str;
        int count = 0;
        for (int i = 0; (str = bf.readLine()) != null; i++) {
            count++;
        }
        bf.close();
        String[][] m=new String[count][];
        bf = new BufferedReader(new FileReader(file));
        for (int i = 0; (str = bf.readLine()) != null; i++) {
            m[i]=str.split(" ");
        }
        String newPath=sll;
        File wfile=new File(newPath);
        BufferedWriter bw = new BufferedWriter(new FileWriter(wfile));
        bw.write(addCardinalNumber() +
                "\r\n"
        );
        for(int i=0;i<m.length;){
            for (int j = 0;i<m.length && j < m[i].length; j++) {
                if(Double.valueOf(m[i][0])>=0){
                    String[][] son=new String[Integer.valueOf(m[i][1])+1][];
                    for (int k = 0; k < son.length; k++) {
                        int len=m[i].length;
                        son[k]=new String[100];
                        son[k][0]=String.valueOf(len);
                        for (int l = 0; l < len; l++) {
                            son[k][l+1]=m[i][l];
                        }
                        i++;
                    }
//
                    bw.write(son[0][1]+" "+son[0][2]+"\n");
                    bw.flush();
//
                    Arrays.parallelSort(son, 1, son.length, new Comparator<String[]>() {
                        @Override
                        public int compare(String[] o1, String[] o2) {
                            return Double.valueOf(o1[1]) > Double.valueOf(o2[1]) ? -1 : 1;
                        }
                    });

                    for (int k = 1; k <son.length; k++) {

                        for (int l = 1; l < Integer.valueOf(son[k][0])+1; l++) {
                            System.out.print(k+" "+String.valueOf(l)+":");
                            System.out.println(son[k][l]);
                            bw.write(son[k][l]+" ");
                        }
                        bw.write("\n");
                        bw.flush();
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        getdescend();

    }

}
