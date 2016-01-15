import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

import com.sun.corba.se.impl.naming.pcosnaming.NameServer;


public class BusTest {
	HashSet<String>    roadLines   =new HashSet<String>();
	HashSet<String>    roadLines2  =new HashSet<String>();
	ArrayList<String>  existName   =new ArrayList<>();
	HashSet<String>    exist       =new HashSet<String>();
	ArrayList<String>  notExistName=new ArrayList<>();
	HashSet<String>    notExist    =new HashSet<String>();
    public static void main(String[] args) {
		BusTest bt=new BusTest();
		//bt.process();
	    //bt.process2();//ˢ����·������ͳ�ƣ�info��·������ͳ��
	    //bt.process3();//�����Ƿ���·�е�������ˢ�������ж��ҵõ����Լ��Ƿ�ˢ�������е���������·�����ж��ҵĵ�
		//bt.process4();//����г����豸��·�ߣ�վ��
		//bt.process5();//��·info�е���·���룬��·���ƣ��豸�������
        //bt.process7();//�г����ݺ���·��Ϣ�е��豸�Ƚ�
		//bt.process8();//�г���·�ź���·��Ϣ��·�űȽ�
		bt.process9();//���ˢ������������
    }
	private void process9() {
		HashSet<String> peoples=new HashSet<String>();
		//HashSet<String> devices=new HashSet<String>();
		HashSet<String> lineNames=new HashSet<String>(); 
		String name_before="D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\ˢ������\\SPTCC-201504";
		String name="";
		BufferedReader br = null ;
		for(int i=1;i<=30;i++){
			if(i<=9){
				name=name_before+"0"+i+".csv";
			}else {
				name=name_before+i+".csv";
			}
			try {
				br=new BufferedReader(new InputStreamReader(new FileInputStream(name),"gbk"));//ˢ��20150401.csv
				String[] recValues;
				String   cardID;
				String   lineName;
				String   type;
				for(String line=br.readLine();line!=null;line=br.readLine()){
					recValues=line.split(",");
                    if(recValues.length<6){
                    	System.out.println("����:"+name+"|"+line);
                    	continue;
                    }
					cardID  =recValues[0];
					lineName=recValues[3];
					type    =recValues[4];
					if(type.equals("����")){
						peoples.add(cardID);
						lineNames.add(lineName);
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\һ����ˢ��\\cards_30day_cardids.csv"),true),"utf-8"));//ˢ����
			Iterator<String> ids=peoples.iterator();
			while(ids.hasNext()){
				String id=ids.next();
				bw.append(id+"\r\n");
			}
			bw.close();
			BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\һ����ˢ��\\cards_30day_lineNames.csv"),true),"utf-8"));//ˢ����
			Iterator<String> lineName_ids=lineNames.iterator();
			while(lineName_ids.hasNext()){
				String id=lineName_ids.next();
				bw2.append(id+"\r\n");
			}
			bw2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		// TODO Auto-generated method stub
	}
	private void process8() {
		// TODO Auto-generated method stub
		HashSet<String> lineIDs1=new HashSet<String>();
		HashSet<String> lineIDs2=new HashSet<String>();
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г�20150401-16.csv")));
			BufferedReader br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\·��info.csv")));
			String[] recValues1;
			String   lineID1;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues1=line.split(",");
				lineID1   =recValues1[1];
				lineIDs1.add(lineID1);
			}
			br.close();
			String[] recValues2;
			String      lineID2;
			br2.readLine();//ȥ����һ��
			for(String line=br2.readLine();line!=null;line=br2.readLine()){
				recValues2=line.split("\t");
				lineID2   =recValues2[0];
				lineIDs2.add(lineID2);
			}
			br2.close();
			BufferedWriter bw1            =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·�űȽ�\\�г�in·��info.csv"))));
			Iterator<String> lineIDs1_iters=lineIDs1.iterator();
			while(lineIDs1_iters.hasNext()){
				String lineID1_iter=lineIDs1_iters.next();
				if(lineIDs2.contains(lineID1_iter)){
					bw1.append(lineID1_iter+"\r\n");
				}
			}
			bw1.close();
			BufferedWriter bw2           =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·�űȽ�\\·��infoIn�г�.csv"))));
			Iterator<String> lineIDs2_iters=lineIDs2.iterator();
			while(lineIDs2_iters.hasNext()){
				String lineID2_iter=lineIDs2_iters.next();
				if(lineIDs1.contains(lineID2_iter)){
					bw2.append(lineID2_iter+"\r\n");
				}
			}
			bw2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void process7() {
		// TODO Auto-generated method stub
		HashSet<String> machines1=new HashSet<String>();
		HashSet<String> machines2=new HashSet<String>();
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г����\\�豸.csv")));
			BufferedReader br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·info���\\��·�豸����.csv")));
			for(String line=br.readLine();line!=null;line=br.readLine()){
				machines1.add(line);
			}
			br.close();
			for(String line=br2.readLine();line!=null;line=br2.readLine()){
				machines2.add(line);
			}
			br2.close();
			BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�豸�Ƚ�\\ˢ���豸in��·�豸.csv"))));
			Iterator<String> machine1_iters=machines1.iterator();
			while(machine1_iters.hasNext()){
				String machine1=machine1_iters.next();
				if(machines2.contains(machine1)){
					bw1.append(machine1+"\r\n");
				}
			}
			bw1.close();
			BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�豸�Ƚ�\\��·�豸inˢ���豸.csv"))));
			Iterator<String> machine2_iters=machines2.iterator();
			while(machine2_iters.hasNext()){
				String machine2=machine2_iters.next();
				if(machines1.contains(machine2)){
					bw2.append(machine2+"\r\n");
				}
			}
			bw2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void process5() {
		// TODO Auto-generated method stub
		HashSet<String> lineIDs    =new HashSet<String>();
		HashSet<String> lineNames  =new HashSet<String>();
		HashSet<String> machineIDs =new HashSet<String>();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\·��info.csv")));
			String[] recValues;
			String   lineID;
			String   lineName;
			String   machineID;
			br.readLine();//��ȥ��һ��
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split("\t");
				lineID=recValues[0];
				lineIDs.add(lineID);
				lineName=recValues[1];
				lineNames.add(lineName);
				machineID=recValues[2];
				machineIDs.add(machineID);
			}
			br.close();
			BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·info���\\��·���.csv"))));
			Iterator<String> lineIDs_iters=lineIDs.iterator();
			while(lineIDs_iters.hasNext()){
				String lineID_iter=lineIDs_iters.next();
				bw1.append(lineID_iter+"\r\n");
			}
			bw1.close();
			BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·info���\\��·����.csv"))));
			Iterator<String> lineNames_iters=lineNames.iterator();
			while(lineNames_iters.hasNext()){
				String lineName_iter=lineNames_iters.next();
				bw2.append(lineName_iter+"\r\n");
			}
			bw2.close();
			BufferedWriter bw3=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·info���\\��·�豸����.csv"))));
			Iterator<String> machineIDs_iters=machineIDs.iterator();
			while(machineIDs_iters.hasNext()){
				String machineID_iter=machineIDs_iters.next();
				bw3.append(machineID_iter+"\r\n");
			}
			bw3.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void process4() {
		// TODO Auto-generated method stub
		HashSet<String> machines=new HashSet<String>();
		HashSet<String> lines   =new HashSet<String>();
		HashSet<String> stops   =new HashSet<String>();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г�20150401-16.csv")));
			String[] recValues;
			String   machine;
			String   lineID;
			String   stopID;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split(",");
				machine=recValues[0];
				machines.add(machine);
				lineID=recValues[1];
				lines.add(lineID);
				stopID=recValues[2];
				stops.add(stopID);
			}
			br.close();
			BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г����\\�豸.csv"))));
			Iterator<String> machines_iters=machines.iterator();
			while(machines_iters.hasNext()){
				String machine_iter=machines_iters.next();
				bw1.append(machine_iter+"\r\n");
			}
			bw1.close();
			BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г����\\��·.csv"))));
			Iterator<String> lines_iters=lines.iterator();
			while(lines_iters.hasNext()){
				String line_iter=lines_iters.next();
				bw2.append(line_iter+"\r\n");
			}
			bw2.close();
			BufferedWriter bw3=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г����\\վ��.csv"))));
			Iterator<String> stops_iters=stops.iterator();
			while(stops_iters.hasNext()){
				String stops_iter=stops_iters.next();
				bw3.append(stops_iter+"\r\n");
			}
			bw3.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void process3() {
		// TODO Auto-generated method stub
		HashSet<String> pos_names=new HashSet<String>();
		HashSet<String> line_names=new HashSet<String>();
		try {
			//..............................
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·\\ˢ��_��·��.csv")));
			String pos_name;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				pos_name=line;
				pos_names.add(pos_name);
			}
			br.close();
			BufferedReader br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·\\��·_��·��.csv")));
			String line_name;
			for(String line=br2.readLine();line!=null;line=br2.readLine()){
				line_name=line;
				line_names.add(line_name);
			}
		    br2.close();
		    //................................
		    BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·\\ˢ������in��·.csv"))));
		    Iterator<String> names1=pos_names.iterator();
		    while(names1.hasNext()){
		        String name1=names1.next();
		        if(line_names.contains(name1)){
		        	bw1.append(name1+"\r\n");
		        }
		    }
		    bw1.close();
		    BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·\\��·����inˢ��.csv"))));
		    Iterator<String> names2=line_names.iterator();
		    while(names2.hasNext()){
		        String name2=names2.next();
		        if(pos_names.contains(name2)){
		        	bw2.append(name2+"\r\n");
		        }
		    }
		    bw2.close();
		    //.................................
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void process2() {
		// TODO Auto-generated method stub
		try {
			//������·���ݵ���Ϣ
			//����1��ʼ.....................................
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\·��info.csv"),"gbk"));
			String[] recValues;
			br.readLine();//ȥ����һ��
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split("\t");
				String roadName=recValues[1];
				//System.out.println(roadName);
			    roadLines.add(roadName);
			}
			br.close();
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\��·_��·��.csv"))));
			Iterator<String> names=roadLines.iterator();
			while(names.hasNext()){
				String name=names.next();
				bw.append(name+"\r\n");
			}
			bw.close();
			//����1����.....................................
			//ˢ�����ݵ�����
			//����2��ʼ.....................................
			BufferedReader  br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\ˢ��20150401.csv"),"utf-8"));
			String[] recValues2;
			for(String line=br2.readLine();line!=null;line=br2.readLine()){
				//System.out.println(line);
				recValues2=line.split(",");
				String roadName=recValues2[2];
				//System.out.println(roadName);
			    roadLines2.add(roadName);
			}
			br2.close();
			BufferedWriter      bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\ˢ��_��·��.csv"))));
			Iterator<String> names2=roadLines2.iterator();
			while(names2.hasNext()){
				String name=names2.next();
				bw2.append(name+"\r\n");
			}
			bw2.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void process() {
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\info.csv")));
			String[] recValues;
			br.readLine();//ȥ����һ��
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split("\t");
				String roadName=recValues[1];
				System.out.println(roadName);
			    roadLines.add(roadName);
			}
			br.close();
			String[] recValues2;
			BufferedWriter bw1=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�д���·.csv"))));
			BufferedWriter bw2=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�޴���·.csv"))));
			BufferedReader  br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����ˢ������.csv"),"utf-8"));
			for(String line2=br2.readLine();line2!=null;line2=br2.readLine()){
				recValues2=line2.split(",");
				//System.out.println(recValues2.length);
				//System.out.println(line2);
				String roadName=recValues2[2];
				//
				if(roadLines.contains(roadName)){
					existName.add(roadName);
					exist.add(roadName);
					//bw1.append(roadName+"\r\n");
				}else{
					notExistName.add(roadName);
					notExist.add(roadName);
					//bw2.append(roadName+"\r\n");
				}
			}
			br2.close();
			Iterator<String> iters=exist.iterator();
			while(iters.hasNext()){
				String name=iters.next();
				bw1.append(name+"\r\n");
			}
			Iterator<String> iters2=notExist.iterator();
			while(iters2.hasNext()){
				String name=iters2.next();
				bw2.append(name+"\r\n");
			}
			bw1.close();
			bw2.close();
			System.out.println("����·�����ݼ�¼Ϊ��"+existName.size()+"������Ϊ��"+exist.size());//�н�������·����300��
			System.out.println("����·�����ݼ�¼Ϊ��"+notExistName.size()+"������Ϊ��"+notExist.size());//�޽�������·����1000��
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
