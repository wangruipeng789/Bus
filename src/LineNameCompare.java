import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;


public class LineNameCompare {
    public static void main(String[] args) {
		LineNameCompare lnc=new LineNameCompare();
		lnc.process();//给数字类型的加上"路"字
		lnc.process2();
	}
	private void process() {
		// TODO Auto-generated method stub
		try {
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\公交线路数据From\\busRotes1.csv")));
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\线路名比较\\busRotes2.csv"))));
			String name;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				//recValues=line.split(",");
				name=line.trim();
				if(name.matches("\\d*")){
				    name=name+"路";	
				}//看是否是数值类型的
				bw.append(name+"\r\n");
			}
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void process2() {
		// TODO Auto-generated method stub
		HashSet<String> rotes_fromL=new HashSet<String>();
		HashSet<String> rotes_shuaka=new HashSet<String>();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\线路名比较\\busRotes2.csv")));
			//String[] recValues;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				//recValues=line.split(",");
				rotes_fromL.add(line);
			}
			br.close();
			BufferedReader br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\线路名比较\\刷卡_线路名.csv")));
			//String[] recValues;
			for(String line=br2.readLine();line!=null;line=br2.readLine()){
				//recValues=line.split(",");
				rotes_shuaka.add(line);
			}
			br2.close();
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\线路名比较\\刷卡inFromL.csv"))));
			Iterator<String> names=rotes_shuaka.iterator();
			while(names.hasNext()){
				String name=names.next();
				if(rotes_fromL.contains(name)||rotes_fromL.contains(name.trim())){
					bw.append(name+"\r\n");
				}
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
