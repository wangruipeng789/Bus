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

//��ȡ�ϳ���
//���һ����ÿ�����ͬһ��վ���ϳ�����ô�ۺϿ�10������˵�ÿ��ĳ˳���·�Ĳ�ͬ�豸���ϳ���Ӧվ����ۺ�ͳ�������Ŀ����Ǵ��˵��ϳ�վ��
public class Aboard {
    //��һ����Ϊ����ȡ�ϳ���
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ArrayList<String>  acts=new ArrayList<String>();//�����Ļ
	Hashtable<String, String> name_id=new Hashtable<String, String>();
 	Hashtable<String,HashSet<String>> date_stops=new Hashtable<>();//ÿ�����ڶ�Ӧ��վ������
 	Hashtable<String, Integer> stop_counts=new Hashtable<String, Integer>();//��ÿ��վ���¼���ٴ�
	public static void main(String[] args) {
	 	Aboard ab=new Aboard();
	 	ab.proActs();
	 	ab.proNameHash();
	 	ab.proZDID();//�õ���Ӧ��վ���
	 	ab.print();
	 	ab.count();
	 	ab.reprint();
	}
	private void reprint() {//��ӡ������ڸ�������վ�������
		Enumeration<String> stops=stop_counts.keys();
		while(stops.hasMoreElements()){
			String stop=stops.nextElement();
			int count=stop_counts.get(stop);
			System.out.println("վ�㣺"+stop+":"+count+"��");
		}
	}
	private void count() {//ͳ��ÿ��վ�������
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
	private void print() {//��ӡ�����ڶ�Ӧ��վ����Ϣ
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
			System.out.println("print����................................");
		}
	}
	private void proZDID() {//վ��ŷ�������-��̬����ӳ��
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
         	     BufferedReader  br = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�����г�20150401-16.csv")));
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
			System.out.println("����2��ʼ..................................");
			Enumeration<String> nas=name_id.keys();
			while(nas.hasMoreElements()){
				String na=nas.nextElement();
				System.out.println(na+"-"+name_id.get(na));
			}
			System.out.println("����2����..................................");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private void proActs() {//�ҵ������и��������������ƥ������ݼ�¼
		String id="3102426931";//�˿ͱ��
		String lineName="995·";
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����ˢ��20150401-16.csv")));
		    String recValues[];
		    String idTemp;
		    String nameTemp;//��·����
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
			System.out.println("����1����..................................");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
