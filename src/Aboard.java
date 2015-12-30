import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

//提取上车点
//如果一个人每天会在同一个站点上车，那么综合看10多天此人的每天的乘车线路的不同设备在上车对应站点的综合统计中最多的可能是此人的上车站点
public class Aboard {
    //以一个人为例提取上车点
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ArrayList<String>  acts=new ArrayList<String>();//许多天的活动
	Hashtable<String, String> name_id=new Hashtable<String, String>();
 	Hashtable<String,HashSet<String>> date_stops=new Hashtable<>();//每个日期对应的站点序列
 	Hashtable<String, Integer> stop_counts=new Hashtable<String, Integer>();//在每个站点记录多少次
	public static void main(String[] args) {
	 	Aboard ab=new Aboard();
	 	ab.proActs();
	 	ab.proNameHash();
	 	ab.proZDID();//得到对应的站点号
	 	ab.print();
	 	ab.count();
	 	ab.reprint();
	}
	private void reprint() {//打印多个日期各个可能站点的数量
		Enumeration<String> stops=stop_counts.keys();
		while(stops.hasMoreElements()){
			String stop=stops.nextElement();
			int count=stop_counts.get(stop);
			System.out.println("站点："+stop+":"+count+"次");
		}
	}
	private void count() {//统计每个站点的数量
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
	private void print() {//打印各日期对应的站点信息
		// TODO Auto-generated method stub
		Enumeration<String> dates=date_stops.keys();
		while(dates.hasMoreElements()){
			String date=dates.nextElement();
			HashSet<String> stops=date_stops.get(date);
			Iterator<String> iters=stops.iterator();
			String outLine ="";
			while(iters.hasNext()){
				String stop=iters.next();
				outLine+=(stop+",");
			}
			outLine+="\r\n";
			System.out.println(date+":"+outLine);
			System.out.println("print结束................................");
		}
	}
	private void proZDID() {//站点号放入日期-动态数组映射
		// TODO Auto-generated method stub
		String passengerID;
	    String dateTime;
	    String lineName;
	    String lineID;
	    String recValues[];
		try {
			for(int i=0,len=acts.size();i<len;i++){
		         recValues=acts.get(i).split(",");
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
			Enumeration<String> nas=name_id.keys();
			while(nas.hasMoreElements()){
				String na=nas.nextElement();
				System.out.println(na+"-"+name_id.get(na));
			}
			System.out.println("测试2结束..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private void proActs() {//找到各天中跟下面具体条件相匹配的数据记录
		String id="3102426931";//乘客编号
		String lineName="995路";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用刷卡20150401-16.csv")));
		    String recValues[];
		    String idTemp;
		    String nameTemp;//线路名称
		    int hour;   
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	recValues=line.split(",");
		    	idTemp=recValues[0];
		    	nameTemp=recValues[3];
		    	hour=Integer.valueOf(recValues[2].substring(0,2));
		    	if(idTemp.equals(id)&&nameTemp.equals(lineName)&&hour<=10){
		    		acts.add(line);
		    	}
		    }
			br.close();
			for(int i=0,len=acts.size();i<len;i++){
				System.out.println(acts.get(i));
			}
			System.out.println("测试1结束..................................");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
