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


public class AboardAll_improve2 {
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
	Hashtable<String, String> name_id=new Hashtable<String, String>();
	Hashtable<String, ArrayList<DriveActivity>>    lineID_drives=new Hashtable<String, ArrayList<DriveActivity>>();//线路号跟行车记录
	Hashtable<String, ArrayList<ArrayList<DriveActivity>>> lineIDTime_drives=new Hashtable<>();//线路号-24小时划分,第一个ArraList是24个划分
	Hashtable<String, ArrayList<UserActivity>>  passenger_uas=new Hashtable<String, ArrayList<UserActivity>>();//卡号跟刷卡
	ArrayList<String> recs=new ArrayList<>();            //一天的记录用以比对
	ArrayList<String> acts_alike=new ArrayList<String>();//多日期的相似记录用于查找
	Hashtable<String,HashSet<String>> date_stops=new Hashtable<>();//每个日期对应的站点序列
	Hashtable<String, Integer> stop_counts=new Hashtable<String, Integer>();//在每个站点记录多少次
    public static void main(String[] args) {
		AboardAll_improve2 aa=new AboardAll_improve2();
		aa.proNameHash();//线路名称跟线路ID映射
		aa.readPosAll();//整理刷卡数据按人读取
		aa.readDrive();//整理行车数据按路线编号读取
		aa.orgDrive();//重新整理行车数据，按照编号和小时划分hash<编号，ArrayList<DriveActivity>[24]>
		aa.match();//信息的匹配
	}
	private void match() {//循环匹配
    	try{
    		Enumeration<String> passengers=passenger_uas.keys();
    		System.out.println("共有:"+passenger_uas.size()+"个人.......");
    		int count=0;
    		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\各上车站点.csv"),true),"utf-8"));
        	while(passengers.hasMoreElements()){//同一个人
        		if(count%2000==0){
        			System.out.println("第"+count+"个人正在进行中......");
        		}
        		count++;
        		String passenger=passengers.nextElement();
        		ArrayList<UserActivity> uas=passenger_uas.get(passenger);
        		HashSet<Integer> rows=new HashSet<>();//用来记录该用户下哪一行的已经标记过了
        		String time;
        		String lineName;
        		UserActivity ua;
        		for(int i=0,len=uas.size();i<len;i++){//依次进行到这一行（作为标杆）
        			if(rows.contains(i)){//已经被标记了就不再判断
        				continue;
        			}
        			ua=uas.get(i);
        			time=ua.actTime;
        			lineName=ua.actLineName;
        			ArrayList<UserActivity> recActs=new ArrayList<UserActivity>();
        			for(int j=(i+1);j<len;j++){
        			   if(rows.contains(j)){//已经被标记了就不再判断
        				   continue;
        			   }
        			   UserActivity  uaTemp=uas.get(j);
        			   String timeTemp=uaTemp.actTime;
        			   String lineNameTemp=uaTemp.actLineName;
        			   double timeDiff=sdf2.parse(timeTemp.substring(11)).getTime()-sdf2.parse(time.substring(11)).getTime();//只比较时分秒
        			   if(lineName.equals(lineNameTemp)&&Math.abs(timeDiff)<3600000){//能近似匹配的
            			   recActs.add(uaTemp);
            			   rows.add(i);//该序号也标记为已经处理过
            			   rows.add(j);//把该序号也标记为已经处理过
        			   }
        			}
        			if(recActs.size()>=2&&recActs!=null){//至少要有两个重复的地方才算
        				this.proZDID(recActs);
        				this.count();
        				String stopAndCount=this.getMaxCountStop();
        				String stopMax=stopAndCount.split(",")[0];
        				String countMax=stopAndCount.split(",")[1];
        				if(Integer.valueOf(countMax)>=2){//最小也得2个重复的
            			    bw.append(passenger+","+time+","+stopMax+","+countMax+"\r\n");
        				}
        			}
        		}//end of for
        		bw.flush();
        	}//end of while
        	bw.close();
    	}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getMaxCountStop() {
		String  stopMax="";
		int    maxCount=0;
		//System.out.println(stop_counts.size());
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
					stop_counts.put(stop,++count);
				}
			}
		}
	}
	private void proZDID(ArrayList<UserActivity> recActs) {
		date_stops.clear();//先清空
		String passengerID;
	    String dateTime;
	    int    hour;
	    String lineName;
	    String lineID;
	    UserActivity ua;
		try {
			for(int i=0,len=recActs.size();i<len;i++){//比较得到的近似活动
		         ua=recActs.get(i);
		         passengerID=ua.actUser;
		         dateTime=ua.actTime;
		         hour    =sdf.parse(dateTime).getHours();
		         lineName=ua.actLineName;
		         lineID  =name_id.get(lineName);
		         if(lineID==null)continue;//线路号为空，直接下一条
		         String passengerIDTemp;
		 	     String dateTimeTemp;
		 	     String lineIDTemp;
		 	     String stopID;
                 HashSet<String> stops=new HashSet();
                 ArrayList<ArrayList<DriveActivity>>   dasTimeArrays=lineIDTime_drives.get(lineID);//该线路所有小时段
                 ArrayList<DriveActivity>  das=new ArrayList<>();
                 DriveActivity da; 
                 try{
                	 if(hour==0){//不等于0就一定有前一小时
                    	 das=dasTimeArrays.get(hour);
                    	 if(das!=null){
                    		 for(int j=0,n=das.size();j<n;j++){//从对应行车数据中找
                     	    	 da=das.get(j);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                    	 das=dasTimeArrays.get(hour+1);
                    	 if(das!=null){
                    		 for(int k=0,m=das.size();k<m;k++){//从对应行车数据中找
                     	    	 da=das.get(k);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                     }else if(hour==23){
                    	 das=dasTimeArrays.get(hour-1);
                    	 if(das!=null){
                    		 for(int j=0,n=das.size();j<n;j++){//从对应行车数据中找
                     	    	 da=das.get(j);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                    	 das=dasTimeArrays.get(hour);
                    	 if(das!=null){
                    		 for(int k=0,m=das.size();k<m;k++){//从对应行车数据中找
                     	    	 da=das.get(k);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                     }else{
                    	 das=dasTimeArrays.get(hour-1);
                    	 if(das!=null){
                    		 for(int j=0,n=das.size();j<n;j++){//从对应行车数据中找
                     	    	 da=das.get(j);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                    	 das=dasTimeArrays.get(hour);
                    	 if(das!=null){
                    		 for(int k=0,m=das.size();k<m;k++){//从对应行车数据中找
                     	    	 da=das.get(k);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                    	 das=dasTimeArrays.get(hour+1);
                    	 if(das!=null){
                    		 for(int k=0,m=das.size();k<m;k++){//从对应行车数据中找
                     	    	 da=das.get(k);
                     	    	 lineIDTemp=da.lineID;
            		        	 stopID=da.stopID;
            		        	 dateTimeTemp=da.driveTime;
            		        	 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        	 if(lineIDTemp.equals(lineID)&&(Math.abs(sdf.parse(dateTimeTemp).getTime()-sdf.parse(dateTime).getTime()))<30*1000){
            		        		 //System.out.println("LineID:"+lineID+"LineIDTemp"+lineIDTemp+"stopID"+stopID);
            		        		 stops.add(stopID);
            		        	 }
                     	     }
                    	 }
                     }
                 }catch (Exception e) {
					System.out.println(lineID);// TODO: handle exception
					System.out.println(das);
				 }
		         date_stops.put(dateTime.substring(0,10), stops);
		    }
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
    private void orgDrive() {
		// TODO Auto-generated method stub
		Enumeration<String> lineIDs=lineID_drives.keys();
		while(lineIDs.hasMoreElements()){
			String lineID=lineIDs.nextElement();
			ArrayList<DriveActivity> das    =lineID_drives.get(lineID);//从原始组装数据中获取
			ArrayList<ArrayList<DriveActivity>> daTimeArrays=new ArrayList<ArrayList<DriveActivity>>();//用于装填新的数据
			for(int i=0;i<24;i++){//先把各时间段的位置沾满
				daTimeArrays.add(i, null);
			}
			ArrayList<DriveActivity> dasTemp;//用于存储某个时间段内的数据
			DriveActivity da;
			String driveTime;
			int hour;
			for(int i=0,len=das.size();i<len;i++){
				da=das.get(i);
				driveTime=da.driveTime;
				try {
					hour=sdf.parse(driveTime).getHours();
					if(daTimeArrays.get(hour)==null){//这个时间段的为空就需要新建
						dasTemp=new ArrayList<>();
						dasTemp.add(da);
						daTimeArrays.set(hour, dasTemp);
					}else{//说明这个时间段的List已经有了
						dasTemp=daTimeArrays.get(hour);
						dasTemp.add(da);
						daTimeArrays.set(hour, dasTemp);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			lineIDTime_drives.put(lineID, daTimeArrays);
		}
		System.out.println("测试4结束..................................");
	}
	private void readDrive() {
		try {
			BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用行车20150401-16.csv")));
			String[] recValues;
			String   machineID;
			String   lineID;
			String   stopID;
			String   driveTime;
			ArrayList<DriveActivity> drives;
			DriveActivity  da;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	//System.out.println(line);
			    recValues=line.split(",");
			    machineID=recValues[0];
			    lineID   =recValues[1];
			    stopID   =recValues[2];
			    driveTime=recValues[6];
			    if(lineID_drives.get(lineID)==null){
			    	drives=new ArrayList<DriveActivity>();
			    	da=new DriveActivity(machineID, lineID, stopID, driveTime);
			    	drives.add(da);
			    	lineID_drives.put(lineID, drives);
			    }else{
			    	drives=lineID_drives.get(lineID);
			    	da=new DriveActivity(machineID, lineID, stopID, driveTime);
			    	drives.add(da);
			    	lineID_drives.put(lineID, drives);
			    }
			}
			System.out.println("测试3结束..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readPosAll() {
		try {
			BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用刷卡20150401-16_sorted.csv")));
			String[] recValues;
			String   passengerID;
			String   dateAndTime;
			String   lineName;
			ArrayList<UserActivity> uas;
			UserActivity ua;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	//System.out.println(line);
			    recValues    =line.split(",");
			    passengerID  =recValues[0];//
			    dateAndTime  =recValues[1];
			    lineName     =recValues[2];
			    if(passenger_uas.get(passengerID)==null){
			    	uas=new ArrayList<UserActivity>();
			    	ua=new UserActivity(passengerID, dateAndTime,lineName);
			    	uas.add(ua);
			    	passenger_uas.put(passengerID, uas);
			    }else{
			    	uas=passenger_uas.get(passengerID);
			    	ua=new UserActivity(passengerID, dateAndTime,lineName);
			    	uas.add(ua);
			    	passenger_uas.put(passengerID, uas);
			    }
			}
			System.out.println("测试2结束..................................");
			br.close();
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
			/*Enumeration<String> nas=name_id.keys();
			while(nas.hasMoreElements()){
				String na=nas.nextElement();
				System.out.println(na+"-"+name_id.get(na));
			}*/
			System.out.println("测试1结束..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
