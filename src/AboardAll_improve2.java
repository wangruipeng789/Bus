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
	Hashtable<String, ArrayList<DriveActivity>>    lineID_drives=new Hashtable<String, ArrayList<DriveActivity>>();//��·�Ÿ��г���¼
	Hashtable<String, ArrayList<ArrayList<DriveActivity>>> lineIDTime_drives=new Hashtable<>();//��·��-24Сʱ����,��һ��ArraList��24������
	Hashtable<String, ArrayList<UserActivity>>  passenger_uas=new Hashtable<String, ArrayList<UserActivity>>();//���Ÿ�ˢ��
	ArrayList<String> recs=new ArrayList<>();            //һ��ļ�¼���Աȶ�
	ArrayList<String> acts_alike=new ArrayList<String>();//�����ڵ����Ƽ�¼���ڲ���
	Hashtable<String,HashSet<String>> date_stops=new Hashtable<>();//ÿ�����ڶ�Ӧ��վ������
	Hashtable<String, Integer> stop_counts=new Hashtable<String, Integer>();//��ÿ��վ���¼���ٴ�
    public static void main(String[] args) {
		AboardAll_improve2 aa=new AboardAll_improve2();
		aa.proNameHash();//��·���Ƹ���·IDӳ��
		aa.readPosAll();//����ˢ�����ݰ��˶�ȡ
		aa.readDrive();//�����г����ݰ�·�߱�Ŷ�ȡ
		aa.orgDrive();//���������г����ݣ����ձ�ź�Сʱ����hash<��ţ�ArrayList<DriveActivity>[24]>
		aa.match();//��Ϣ��ƥ��
	}
	private void match() {//ѭ��ƥ��
    	try{
    		Enumeration<String> passengers=passenger_uas.keys();
    		System.out.println("����:"+passenger_uas.size()+"����.......");
    		int count=0;
    		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\���ϳ�վ��.csv"),true),"utf-8"));
        	while(passengers.hasMoreElements()){//ͬһ����
        		if(count%2000==0){
        			System.out.println("��"+count+"�������ڽ�����......");
        		}
        		count++;
        		String passenger=passengers.nextElement();
        		ArrayList<UserActivity> uas=passenger_uas.get(passenger);
        		HashSet<Integer> rows=new HashSet<>();//������¼���û�����һ�е��Ѿ���ǹ���
        		String time;
        		String lineName;
        		UserActivity ua;
        		for(int i=0,len=uas.size();i<len;i++){//���ν��е���һ�У���Ϊ��ˣ�
        			if(rows.contains(i)){//�Ѿ�������˾Ͳ����ж�
        				continue;
        			}
        			ua=uas.get(i);
        			time=ua.actTime;
        			lineName=ua.actLineName;
        			ArrayList<UserActivity> recActs=new ArrayList<UserActivity>();
        			for(int j=(i+1);j<len;j++){
        			   if(rows.contains(j)){//�Ѿ�������˾Ͳ����ж�
        				   continue;
        			   }
        			   UserActivity  uaTemp=uas.get(j);
        			   String timeTemp=uaTemp.actTime;
        			   String lineNameTemp=uaTemp.actLineName;
        			   double timeDiff=sdf2.parse(timeTemp.substring(11)).getTime()-sdf2.parse(time.substring(11)).getTime();//ֻ�Ƚ�ʱ����
        			   if(lineName.equals(lineNameTemp)&&Math.abs(timeDiff)<3600000){//�ܽ���ƥ���
            			   recActs.add(uaTemp);
            			   rows.add(i);//�����Ҳ���Ϊ�Ѿ������
            			   rows.add(j);//�Ѹ����Ҳ���Ϊ�Ѿ������
        			   }
        			}
        			if(recActs.size()>=2&&recActs!=null){//����Ҫ�������ظ��ĵط�����
        				this.proZDID(recActs);
        				this.count();
        				String stopAndCount=this.getMaxCountStop();
        				String stopMax=stopAndCount.split(",")[0];
        				String countMax=stopAndCount.split(",")[1];
        				if(Integer.valueOf(countMax)>=2){//��СҲ��2���ظ���
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
	
	private void count() {//ͳ��ÿ��վ�������
		stop_counts.clear();//�����
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
		date_stops.clear();//�����
		String passengerID;
	    String dateTime;
	    int    hour;
	    String lineName;
	    String lineID;
	    UserActivity ua;
		try {
			for(int i=0,len=recActs.size();i<len;i++){//�Ƚϵõ��Ľ��ƻ
		         ua=recActs.get(i);
		         passengerID=ua.actUser;
		         dateTime=ua.actTime;
		         hour    =sdf.parse(dateTime).getHours();
		         lineName=ua.actLineName;
		         lineID  =name_id.get(lineName);
		         if(lineID==null)continue;//��·��Ϊ�գ�ֱ����һ��
		         String passengerIDTemp;
		 	     String dateTimeTemp;
		 	     String lineIDTemp;
		 	     String stopID;
                 HashSet<String> stops=new HashSet();
                 ArrayList<ArrayList<DriveActivity>>   dasTimeArrays=lineIDTime_drives.get(lineID);//����·����Сʱ��
                 ArrayList<DriveActivity>  das=new ArrayList<>();
                 DriveActivity da; 
                 try{
                	 if(hour==0){//������0��һ����ǰһСʱ
                    	 das=dasTimeArrays.get(hour);
                    	 if(das!=null){
                    		 for(int j=0,n=das.size();j<n;j++){//�Ӷ�Ӧ�г���������
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
                    		 for(int k=0,m=das.size();k<m;k++){//�Ӷ�Ӧ�г���������
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
                    		 for(int j=0,n=das.size();j<n;j++){//�Ӷ�Ӧ�г���������
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
                    		 for(int k=0,m=das.size();k<m;k++){//�Ӷ�Ӧ�г���������
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
                    		 for(int j=0,n=das.size();j<n;j++){//�Ӷ�Ӧ�г���������
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
                    		 for(int k=0,m=das.size();k<m;k++){//�Ӷ�Ӧ�г���������
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
                    		 for(int k=0,m=das.size();k<m;k++){//�Ӷ�Ӧ�г���������
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
			ArrayList<DriveActivity> das    =lineID_drives.get(lineID);//��ԭʼ��װ�����л�ȡ
			ArrayList<ArrayList<DriveActivity>> daTimeArrays=new ArrayList<ArrayList<DriveActivity>>();//����װ���µ�����
			for(int i=0;i<24;i++){//�ȰѸ�ʱ��ε�λ��մ��
				daTimeArrays.add(i, null);
			}
			ArrayList<DriveActivity> dasTemp;//���ڴ洢ĳ��ʱ����ڵ�����
			DriveActivity da;
			String driveTime;
			int hour;
			for(int i=0,len=das.size();i<len;i++){
				da=das.get(i);
				driveTime=da.driveTime;
				try {
					hour=sdf.parse(driveTime).getHours();
					if(daTimeArrays.get(hour)==null){//���ʱ��ε�Ϊ�վ���Ҫ�½�
						dasTemp=new ArrayList<>();
						dasTemp.add(da);
						daTimeArrays.set(hour, dasTemp);
					}else{//˵�����ʱ��ε�List�Ѿ�����
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
		System.out.println("����4����..................................");
	}
	private void readDrive() {
		try {
			BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�����г�20150401-16.csv")));
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
			System.out.println("����3����..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void readPosAll() {
		try {
			BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����ˢ��20150401-16_sorted.csv")));
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
			System.out.println("����2����..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void proNameHash() {//��·���Ƹ���·��ӳ��
		String lineName="995·";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����·��info.csv"),"gbk"));
		    String recValues[];
		    String id;
		    String name;//��·����
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
			System.out.println("����1����..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
