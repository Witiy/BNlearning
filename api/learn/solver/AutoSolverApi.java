package BNlearning.api.learn.solver;

import BNlearning.api.CountF1;
import BNlearning.api.learn.scorer.IndependenceScorerApi;
import BNlearning.api.learn.solver.win.WinAsobsSolverApi;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;

public class AutoSolverApi {
    private static PermExtensibleWinSolverApi pfwin = new PermExtensibleWinSolverApi();
    private static WinAsobsSolverApi win = new WinAsobsSolverApi();
    private static AsobsSolverApi asobs = new AsobsSolverApi();
    private static ObsSolverApi obs = new ObsSolverApi();

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {


        ScoreSolverApi[] Apis = {pfwin,win,asobs,obs};

        //File fileA = new File("e:/1hourLink.xls");
        String[] checks ={ "0.1 5.0 10.0 15.0 20.0 25.0 30.0 35.0 40.0 45.0 50.0 55.0 60.0 65.0 70.0 75.0 80.0 85.0 90.0 95.0 100.0 ",
                "180 360 540 720 900 1080 1260 1440 1620 1800 1980 2160 2340 2520 2700 2880 3060 3240 3420 3600"};


        int jkltime = 600;
        int jklcore = 0;
        int rescore = 0;
        int versionNum = 3;
        String []inputs = {//"E:\\DataSet\\Alarm\\alarm_data\\Alarm1_s5000",
                //"E:\\DataSet\\Alarm\\alarm3_data\\Alarm3_s5000",
                //"E:\\DataSet\\Alarm\\alarm5_data\\Alarm5_s5000",
                //"E:\\DataSet\\Alarm\\alarm10_data\\Alarm10_s5000",
                //"E:\\DataSet\\child\\child_data\\Child_s5000",
                //"C:\\Users\\79951\\Desktop\\for_eclipse\\srf_blip\\DataSet\\child\\child3_data\\Child3_s5000",
                //"E:\\DataSet\\child\\child5_data\\Child5_s5000",
                //"C:\\Users\\79951\\Desktop\\for_eclipse\\srf_blip\\DataSet\\child\\child10_data\\Child10_s5000",
                //        "C:\\Users\\79951\\Desktop\\for_eclipse\\srf_blip\\DataSet\\Insurance\\ins_data\\Insurance_s5000",
                //"C:\\Users\\79951\\Desktop\\for_eclipse\\srf_blip\\DataSet\\Insurance\\ins3_data\\Insurance3_s5000",
                //"E:\\DataSet\\Insurance\\ins5_data\\Insurance5_s5000",
                //"C:\\Users\\79951\\Desktop\\for_eclipse\\srf_blip\\DataSet\\Insurance\\ins10_data\\Insurance10_s5000",
                "E:\\DataSet\\Link\\link_data\\Link_s5000",
                "E:\\DataSet\\gene\\gene_data\\Gene_s5000"
        };

        try {


            String[] apis = {"PFW","WIN","ASOBS","OBS"};

            WritableWorkbook workbook = null;

            for(String input:inputs){
                //check
                String test = input+"_v"+String.valueOf(1)+".txt";
                File testfile = new File(test);
                if(!testfile.exists()){
                    System.out.println("Path Error :"+test);
                    return;
                }
                else {
                    System.out.println("Path Normal!");
                }
            }


            int t = 0;
            for(String input : inputs){
                String check = checks[1];

                t++;
                String times[] = check.split(" ");
                int row = 0, row1 = 0;
                File fileA = new File(input+".xls");
                System.out.println(input + ".xls");
                fileA.createNewFile();

                //����������
                workbook = Workbook.createWorkbook(fileA);

                //����sheet

                WritableSheet sheetA = workbook.createSheet("F1andSk", 0);
                WritableSheet sheetB = workbook.createSheet("Adv",1) ;
                Label labelA = null,labelB = null;


                String pre = input.substring(0, input.lastIndexOf("\\"));
                String after = input.substring(input.lastIndexOf("\\"));
                labelA = new Label(0,row++,after);
                sheetA.addCell(labelA);
                labelB = new Label(0,row1++,after);
                sheetB.addCell(labelB);

                for( int i = 1; i < 2; i++){
                    String className = Apis[i].getClass().getName();
                    System.out.println("Method: "+className);
                    labelA = new Label(0,row++,apis[i]);
                    sheetA.addCell(labelA);
                    labelB = new Label(0,row1++,apis[i]);
                    sheetB.addCell(labelB);

                    double advresult[][] = new double[times.length][5];
                    for(int j =3; j<=versionNum; j++){
                        //д�����ݼ��汾
                        String version = after+"_v"+String.valueOf(j);
                        labelA = new Label(0,row++,version);
                        sheetA.addCell(labelA);


                        System.out.println("++++++++++++++++++++++++");
                        System.out.println("+   "+version+"    +");
                        System.out.println("++++++++++++++++++++++++");
                        System.out.println();
                        System.out.println();

                        //ȷ���������·��
                        String realinput = input+"_v"+String.valueOf(j)+".jkl";
                        File inputfile = new File(realinput);
                        System.out.println(realinput);
                        if(!inputfile.exists())
                            IndependenceScorerApi.run(realinput.substring(0,realinput.length()-3)+"dat",realinput,jkltime,jklcore);
                        String output = pre +"\\"+ apis[i]+ after+"_v"+String.valueOf(j)+".res";

                            //create a instance
                        Class c = Class.forName(className);
                        ScoreSolverApi ssapi = (ScoreSolverApi) c.newInstance();
                        System.out.println(input);
                        //sll
                        /*
                        if( i == 0 && (
                                input.equals("E:\\DataSet\\Insurance\\ins5_data\\Insurance5_s5000"))){
                            String sllinput = realinput.substring(0,realinput.length()-4) + "_sll.jkl";
                            FMeasure.run(sllinput);

                            ssapi.run(sllinput,output,check,rescore);
                        }
                        */
                        //if( i == 0 )
                            ssapi.run(realinput,output,check,rescore);


                        double[][] scores = CountF1.computeAll(realinput,output,check);
                        for(int p = 0; p <times.length; p++){
                            labelA = new Label(0,row,times[p]);
                            sheetA.addCell(labelA);
                            for(int q = 1; q<= 5; q++){
                                labelA = new Label(q,row,String.valueOf(scores[p][q-1]));
                                advresult[p][q-1] += scores[p][q-1];
                                sheetA.addCell(labelA);
                            }

                            row++;
                        }
                        row++;


                    }

                    for(int j = 0; j < times.length; j++){
                        labelB = new Label(0,row1,times[j]);
                        sheetB.addCell(labelB);
                        for(int k = 0; k < 5; k++){
                            advresult[j][k] /= versionNum;
                            labelB = new Label(k+1,row1,String.valueOf(advresult[j][k]));
                            sheetB.addCell(labelB);
                        }
                        row1++;
                    }


                }
                workbook.write();    //д������
                workbook.close();  //�ر�����
            }


        } catch (WriteException writeException) {
            writeException.printStackTrace();
        }



    }

}

