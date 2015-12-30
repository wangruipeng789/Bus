import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashSet;

/**
 * 上车点提取
 */
public class DataOrg {
	HashSet<String> roadLines=new HashSet<>();
	HashSet<String> roadIds=new HashSet<String>();//有用的行车编号
    static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void main(String[] args) {
		DataOrg dt=new DataOrg();
	    //dt.preLineInfo();     //把有用路线放入Hashset
	    //dt.proLineInfo();         //提取有价值的路线
	    //dt.preDrive();
	    //dt.proDrive();//提取有用的行车数据
    }
	private void preDrive() {//读取有用的线路信息
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用路线info.csv")));
			String[] recValues;
			//br.readLine();//去掉第一行
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split("\t");
				String roadId=recValues[0];
				roadIds.add(roadId);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void proDrive() {//生成有用的行车记录（只有线路是有意义的才能继续）
		try {
			String[] recValues;
			String id; 
			BufferedReader  br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\行车20150401-16.csv")));
			BufferedWriter  bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用行车20150401-16.csv"))));
			for(String line=br2.readLine();line!=null;line=br2.readLine()){
				recValues=line.split(",");
				id=recValues[1];
				if(roadIds.contains(id)){
					bw.append(line+"\r\n");
				}
			}
			br2.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void preLineInfo() {//根据刷卡数据与线路的交集
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有此线路.csv")));
			String[] recValues;
			br.readLine();//去掉第一行
			for(String line=br.readLine();line!=null;line=br.readLine()){
			    roadLines.add(line);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void proLineInfo() {//得到有用的线路信息
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\路线info.csv")));
			BufferedWriter  bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用路线info.csv"))));
			String[] recValues;
			//br.readLine();//去掉第一行
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split("\t");
				String roadName=recValues[1];
				if(roadLines.contains(roadName)){
					bw.append(line+"\r\n");
				}
			}
			br.close();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
