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


public class AboardAll_improve1 {
	public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
	Hashtable<String, String> name_id=new Hashtable<String, String>();
	Hashtable<String, ArrayList<String>> lineID_acts=new Hashtable<String, ArrayList<String>>();//��·�Ÿ��г���¼
	Hashtable<String, ArrayList<UserActivity>> passenger_bas=new Hashtable<String, ArrayList<UserActivity>>();//���Ÿ�ˢ��
	ArrayList<String> recs=new ArrayList<>();            //һ��ļ�¼���Աȶ�
	ArrayList<String> acts_alike=new ArrayList<String>();//�����ڵ����Ƽ�¼���ڲ���
	Hashtable<String,HashSet<String>> date_stops=new Hashtable<>();//ÿ�����ڶ�Ӧ��վ������
	Hashtable<String, Integer> stop_counts=new Hashtable<String, Integer>();//��ÿ��վ���¼���ٴ�
    public static void main(String[] args) {
		AboardAll_improve1 aa=new AboardAll_improve1();
		aa.proNameHash();//��·���Ƹ���·IDӳ��
		aa.readPos();//��ȡһ���е�ˢ������
		aa.readDrive();//�����г�����
		aa.readPosAll();//����ˢ������
		aa.match();//��Ϣ��ƥ��
	}
    private void match() {//ѭ��ƥ��
		String userID;
		String roadName;
		String date;
		String time;
		String recValues[];
		for(int i=0;i<recs.size();i++){
			acts_alike.clear();//��ÿ��������Ϊ���Ƶ��жϱ�׼
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
				ArrayList<UserActivity> bas=passenger_bas.get(userID);
				UserActivity  ba;
				for(int j=0,len=bas.size();j<len;j++){
					ba=bas.get(j);
					 userIDTemp  =ba.actUser;
					 roadNameTemp=ba.actLineName;
					 dateTemp    =ba.actTime.substring(0,10);
					 timeTemp    =ba.actTime.substring(11);
					 long timeDiff=sdf2.parse(time).getTime()-sdf2.parse(timeTemp).getTime();//ֻ��ʱ�����ʱ���
					 if(userID.equals(userIDTemp)&&roadName.equals(roadNameTemp)&&Math.abs(timeDiff)<=3600000){
					     acts_alike.add(userIDTemp+","+dateTemp+","+timeTemp+","+roadNameTemp);
					 }
				}
				this.proZDID();
				this.count();
				String stopAndCount=this.getMaxCountStop();
				//System.out.println(stopAndCount);
				String stopMax=stopAndCount.split(",")[0];
				String countMax=stopAndCount.split(",")[1];
				BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\���ϳ�վ��.csv"),true),"utf-8"));
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
	private void proZDID() {//վ��ŷ�������-��̬����ӳ��
		date_stops.clear();//�����
		// TODO Auto-generated method stub
		String passengerID;
	    String dateTime;
	    String lineName;
	    String lineID;
	    String recValues[];
		try {
			for(int i=0,len=acts_alike.size();i<len;i++){//�Ƚϵõ��Ľ��ƻ
		         recValues=acts_alike.get(i).split(",");
		         passengerID=recValues[0];
		         dateTime=recValues[1]+" "+recValues[2];
		         lineName=recValues[3];
		         lineID  =name_id.get(lineName);
		         String passengerIDTemp;
		 	     String dateTimeTemp;
		 	     String lineIDTemp;
		 	     String stopID;
                 HashSet<String> stops=new HashSet();
                 //System.out.println(lineID);
         	     ArrayList<String> acts=lineID_acts.get(lineID);
         	     if(acts==null)continue;
         	     //System.out.println("ssss"+acts.size());
         	     String recValuesTemp[]; 
         	     for(int j=0,n=acts.size();j<n;j++){//�Ӷ�Ӧ�г���������
         	    	 recValuesTemp=acts.get(j).split(",");
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
		    }
		} catch (ParseException e) {
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
			ArrayList<UserActivity> bas;
			UserActivity ba;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	//System.out.println(line);
			    recValues    =line.split(",");
			    passengerID  =recValues[0];//
			    dateAndTime  =recValues[1];
			    lineName     =recValues[2];
			    if(passenger_bas.get(passengerID)==null){
			    	bas=new ArrayList<UserActivity>();
			    	ba=new UserActivity(passengerID, dateAndTime,lineName);
			    	bas.add(ba);
			    	passenger_bas.put(passengerID, bas);
			    }else{
			    	bas=passenger_bas.get(passengerID);
			    	ba=new UserActivity(passengerID, dateAndTime,lineName);
			    	bas.add(ba);
			    	passenger_bas.put(passengerID, bas);
			    }
			}
			//System.out.println("��ѽѽ��"+lineID_acts.get("10065").size());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void readDrive() {
		try {
			BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�����г�20150401-16.csv")));
			String[] recValues;
			String   lineID;
			String   currID="";
			ArrayList<String> acts;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	//System.out.println(line);
			    recValues=line.split(",");
			    lineID   =recValues[1];//
			    if(lineID_acts.get(lineID)==null){
			    	acts=new ArrayList<String>();
			    	acts.add(line);
			    	lineID_acts.put(lineID, acts);
			    }else{
			    	acts=lineID_acts.get(lineID);
			    	acts.add(line);
			    	lineID_acts.put(lineID, acts);
			    }
			}
			System.out.println("��ѽѽ��"+lineID_acts.size());
			System.out.println("����3����..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readPos() {//��ȡһ��ļ�¼���Աȶ�
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����ˢ��20150401-16.csv")));
		    String recValues[];
		    String userId;
		    String roadName;//��·����
		    String date;
		    String time;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	recValues=line.split(",");
		    	userId=recValues[0];
		    	roadName=recValues[3];
		    	date=recValues[1];
		    	time=recValues[2];
		    	if(date.substring(0,10).equals("2015-04-01")){
		    		recs.add(userId+","+date+","+time+","+roadName);
		    	}
 			}
			br.close();
			System.out.println("����2��ʼ..................................");
			/*for(int i=0,len=recs.size();i<len;i++){
				System.out.println(recs.get(i));
			}*/
			//System.out.println(recs.size());//1218627
			System.out.println("����2����..................................");
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
			System.out.println("����1��ʼ..................................");
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
