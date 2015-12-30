import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;


public class Sort {
   Hashtable<String, ArrayList<UserActivity>> passenger_acts=new Hashtable<String, ArrayList<UserActivity>>();
   public static void main(String[] args) {
	   Sort s=new Sort();
	   s.process();
	   s.export();
   }
   private void export() {//10:09
	   try {
		   BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用刷卡20150401-16_sorted.csv"))));
		   Enumeration<String> keys=passenger_acts.keys();
		   while(keys.hasMoreElements()){
			   String key=keys.nextElement();
			   ArrayList<UserActivity> bas=passenger_acts.get(key);
			   java.util.Collections.sort(bas);
			   String passenger;
			   String dateAndTime;
			   String lineName;
			   for(int i=0;i<bas.size();i++){
				   UserActivity  ba=bas.get(i);
				   passenger=ba.actUser;
				   dateAndTime=ba.actTime;
				   lineName=ba.actLineName;
				   bw.append(passenger+","+dateAndTime+","+lineName+"\r\n");
			   }
			   bw.flush();
		   }
		   bw.close();
 	   } catch (FileNotFoundException e) {
		   e.printStackTrace();
	   } catch (IOException e) {
		  e.printStackTrace();
	   }
   }
   private void process() {
	// TODO Auto-generated method stub
	   try {
			BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\有用刷卡20150401-16.csv")));
			String[] recValues;
			String   passengerID;
			String   actTime;
			String   actLineName;
			ArrayList<UserActivity> acts;
			UserActivity            ba;
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	//System.out.println(line);
			    recValues    =line.split(",");
			    passengerID  =recValues[0];//
			    actTime      =recValues[1]+" "+recValues[2];
			    actLineName  =recValues[3];
			    if(passenger_acts.get(passengerID)==null){
			    	ba=new UserActivity(passengerID, actTime, actLineName);
			    	acts=new ArrayList<>();
			    	acts.add(ba);
			    	passenger_acts.put(passengerID, acts);
			    }else{
			    	acts=passenger_acts.get(passengerID);
			    	ba=new UserActivity(passengerID, actTime, actLineName);
			    	acts.add(ba);
			    	passenger_acts.put(passengerID, acts);
			    }
			}
			//System.out.println("哇呀呀："+lineID_acts.get("10065").size());
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
}
