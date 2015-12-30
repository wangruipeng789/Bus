import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;


public class AboardAll {
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
	Hashtable<String, String> name_id=new Hashtable<String, String>();
	ArrayList<String> recs=new ArrayList<>();            //一天的记录用以比对
	ArrayList<String> acts_alike=new ArrayList<String>();//多日期的相似记录用于查找
	Hashtable<String,HashSet<String>> date_stops=new Hashtable<>();//每个日期对应的站点序列
	Hashtable<String, Integer> stop_counts=new Hashtable<String, Integer>();//在每个站点记录多少次
    public static void main(String[] args) {
		AboardAll aa=new AboardAll();
		aa.proNameHash();//线路名称跟线路ID映射
		aa.read();//读取一天中的数据
		aa.match();//信息的匹配
	}
	private void match() {//循环匹配
		String userID;
		String roadName;
		String date;
		String time;
		String recValues[];
		for(int i=0;i<recs.size();i++){
			acts_alike.clear();//以每行数据作为相似的判断标准
			recValues=recs.get(i).split(",");
			userID=recValues[0];
			roadName=recValues[3];
			date=recValues[1];
			time=recValues[2];
			try {
				String userIDTemp;
				String roadNameTemp;
				String dateTemp;
				String timeTemp;
				String recValuesTemp[];
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用刷卡20150401-16.csv")));
				for(String line=br.readLine();line!=null;line=br.readLine()){
				    recValuesTemp=line.split(",");
				    userIDTemp  =recValuesTemp[0];
				    roadNameTemp=recValuesTemp[3];
				    dateTemp    =recValuesTemp[1];
				    timeTemp    =recValuesTemp[2];
				    long timeDiff=sdf2.parse(time).getTime()-sdf2.parse(timeTemp).getTime();//只算时分秒的时间差
				    if(userID.equals(userIDTemp)&&roadName.equals(roadNameTemp)&&Math.abs(timeDiff)<=3600000){
				    	acts_alike.add(line);
				    }
				}
				br.close();
				this.proZDID();
				this.count();
				String stopAndCount=this.getMaxCountStop();
				String stopMax=stopAndCount.split(",")[0];
				String countMax=stopAndCount.split(",")[1];
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\各上车站点.csv"),true),"utf-8"));
			    bw.append(userID+","+time+","+stopMax+","+countMax+"\r\n");
			    bw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	private String getMaxCountStop() {
		String  stopMax = "";
		int    maxCount=0;
		Enumeration<String> stops=stop_counts.keys();
		while (stops.hasMoreElements()) {
			String  stop = (String) stops.nextElement();
			int     count=stop_counts.get(stop);
			if(count>=maxCount){
				maxCount=count;
				stopMax=stop;
			}
		}
		return stopMax+","+maxCount;
	}
	private void proZDID() {//站点号放入日期-动态数组映射
		date_stops.clear();//先清空
		// TODO Auto-generated method stub
		String passengerID;
	    String dateTime;
	    String lineName;
	    String lineID;
	    String recValues[];
		try {
			for(int i=0,len=acts_alike.size();i<len;i++){
		         recValues=acts_alike.get(i).split(",");
		         passengerID=recValues[0];
		         dateTime=recValues[1]+" "+recValues[2];
		         lineName=recValues[3];
		         lineID  =name_id.get(lineName);
		         String passengerIDTemp;
		 	     String dateTimeTemp;
		 	     String lineIDTemp;
		 	     String stopID;
		 	     String recValuesTemp[]; 
                 HashSet<String> stops=new HashSet();
         	     BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用行车20150401-16.csv")));
         	     for(String line=br.readLine();line!=null;line=br.readLine()){
		        	 recValuesTemp=line.split(",");
		        	 lineIDTemp=recValuesTemp[1];
		        	 stopID=recValuesTemp[2];
		        	 dateTimeTemp=recValuesTemp[6];
		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
		        		 stops.add(stopID);
		        	 }
		         }
		         date_stops.put(recValues[1], stops);
		         br.close();
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
	}
	private void count() {//统计每个站点的数量
		stop_counts.clear();//先清空
		Enumeration<String> dates=date_stops.keys();
		while(dates.hasMoreElements()){
			String date=dates.nextElement();
			HashSet<String> stops=date_stops.get(date);
			Iterator<String> iters=stops.iterator();
			String outLine ="";
			while(iters.hasNext()){
				String stop=iters.next();
				if(stop_counts.get(stop)==null){
					stop_counts.put(stop, 1);
				}else{
					int count=stop_counts.get(stop).intValue();
					stop_counts.put(stop,++(count));
				}
			}
		}
	}
	private void read() {//读取一天的记录用以比对
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用刷卡20150401-16.csv")));
		    String recValues[];
		    String userId;
		    String roadName;//线路名称
		    String date;
		    String time;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	recValues=line.split(",");
		    	userId=recValues[0];
		    	roadName=recValues[3];
		    	date=recValues[1];
		    	time=recValues[2];
		    	if(date.substring(8,10).equals("01")){
		    		recs.add(userId+","+date+","+time+","+roadName);
		    	}
 			}
			br.close();
			System.out.println("测试1开始..................................");
			/*for(int i=0,len=recs.size();i<len;i++){
				System.out.println(recs.get(i));
			}*/
			System.out.println(recs.size());//1218627
			System.out.println("测试1结束..................................");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private void proNameHash() {//线路名称跟线路号映射
		String lineName="995路";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用路线info.csv"),"gbk"));
		    String recValues[];
		    String id;
		    String name;//线路名称
		    int hour;   
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	recValues=line.split("\t");
		    	id=recValues[0];
		    	name=recValues[1];
		    	name_id.put(name, id);
		    }
			System.out.println("测试2开始..................................");
			/*Enumeration<String> nas=name_id.keys();
			while(nas.hasMoreElements()){
				String na=nas.nextElement();
				System.out.println(na+"-"+name_id.get(na));
			}*/
			System.out.println("测试2结束..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
