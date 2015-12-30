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
 * �ϳ�����ȡ
 */
public class DataOrg {
	HashSet<String> roadLines=new HashSet<>();
	HashSet<String> roadIds=new HashSet<String>();//���õ��г����
    static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void main(String[] args) {
		DataOrg dt=new DataOrg();
	    //dt.preLineInfo();     //������·�߷���Hashset
	    //dt.proLineInfo();         //��ȡ�м�ֵ��·��
	    //dt.preDrive();
	    //dt.proDrive();//��ȡ���õ��г�����
    }
	private void preDrive() {//��ȡ���õ���·��Ϣ
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����·��info.csv")));
			String[] recValues;
			//br.readLine();//ȥ����һ��
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
	private void proDrive() {//�������õ��г���¼��ֻ����·��������Ĳ��ܼ�����
		try {
			String[] recValues;
			String id; 
			BufferedReader  br2=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�г�20150401-16.csv")));
			BufferedWriter  bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�����г�20150401-16.csv"))));
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
	private void preLineInfo() {//����ˢ����������·�Ľ���
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\�д���·.csv")));
			String[] recValues;
			br.readLine();//ȥ����һ��
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
	private void proLineInfo() {//�õ����õ���·��Ϣ
		try {
			BufferedReader  br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\·��info.csv")));
			BufferedWriter  bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\����·��info.csv"))));
			String[] recValues;
			//br.readLine();//ȥ����һ��
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
