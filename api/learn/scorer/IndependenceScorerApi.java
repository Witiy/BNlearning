package BNlearning.api.learn.scorer;

import BNlearning.core.learn.scorer.BaseScorer;
import BNlearning.core.learn.scorer.IndependenceScorer;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class IndependenceScorerApi extends ScorerApi {

	public static void main(String[] args) throws IOException {
		// String[] argStrings= {"falkfjsd","dfsd"};
		String ph_dat = "E:\\DataSet\\Link\\link_data\\Link_s5000_v3.dat";
		String ph_score = ph_dat.substring(0,ph_dat.length()-3)+"jkl";
		int time = 600;
		File dat = new File(ph_dat);
		if(!dat.exists())
			addCardinalNumber(ph_dat);
		String[] tempStrings = { "",
				"-d", ph_dat,
				"-j", ph_score,
				"-t", String.valueOf(time),
				"-c", "bdeu",
				"-b","0"
		};

		defaultMain(tempStrings, new IndependenceScorerApi());
	}

	public static void run(String input, String output, int time,int core) throws IOException {
		File dat = new File(input);
		if(!dat.exists())
			addCardinalNumber(input);
		String[] tempStrings = { "",
				"-d", input,
				"-j", output,
				"-t", String.valueOf(time),
				"-c", "bdeu",
				"-b", String.valueOf(core)
		};

		defaultMain(tempStrings, new IndependenceScorerApi());
	}

	static void addCardinalNumber(String ph_dat) throws IOException {//������������
		String ph_txt = ph_dat.substring(0,ph_dat.length()-3)+"txt";
		int size = 0;
		File file=new File(ph_txt);
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

			for(int a=0;a<size;a++)
			{
				tree.add(new ArrayList<Integer>());
			}
			while(line!=null)
			{
				//  String newLine=line.replaceAll("   ",",");

				//   System.out.println(newLine);
				rewriteString+=(line+"\r\n");
				contentStrings=line.split(" ");
				for(String v:contentStrings) {
					//System.out.println(v);
					if(!v.equals("")) {
						//	System.out.print(v+" ");
						int p=Integer.valueOf(v);

						if(!tree.get(j).contains(p)) {
							tree.get(j).add(p);
						}

						j=(j+1)%size;
					}
				}
				//   System.out.println();
				line=reader.readLine();

			}
			reader.close();
			file=new File(ph_dat);
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream writer=new FileOutputStream(file);

			String baseString="";




			for(ArrayList<Integer> a:tree) {
				baseString+=String.valueOf(a.size())+"  ";
			}
			baseString+="\r\n";
			byte[] buff= baseString.getBytes();

			writer.write(buff);
			writer.flush();
			buff = rewriteString.getBytes();
			writer.write(buff);
			writer.flush();
			writer.close();

		}
		catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	protected BaseScorer getScorer() {
		return new IndependenceScorer();
	}
}
