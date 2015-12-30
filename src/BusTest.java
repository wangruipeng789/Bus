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
import java.util.HashSet;
import java.util.Iterator;

import com.sun.corba.se.impl.naming.pcosnaming.NameServer;


public class BusTest {
	HashSet<String>    roadLines   =new HashSet<String>();
	ArrayList<String>  existName   =new ArrayList<>();
	HashSet<String>    exist       =new HashSet<String>();
	ArrayList<String>  notExistName=new ArrayList<>();
	HashSet<String>    notExist    =new HashSet<String>();
    public static void main(String[] args) {
		BusTest bt=new BusTest();
		bt.process();
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
