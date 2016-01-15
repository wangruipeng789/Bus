import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;


public class AbordCal {
    public static void main(String[] args) {
		AbordCal ac=new AbordCal();
		ac.process();
	}
	private void process() {
		// TODO Auto-generated method stub
		HashSet<String> users=new HashSet<>();
		HashSet<String> stopIDs=new HashSet<String>();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\���ϳ�վ��.csv")));
			String user;
			String stop;
			String[] recValues;
			for(String line=br.readLine();line!=null;line=br.readLine()){
				recValues=line.split(",");
				user=recValues[0];
				stop=recValues[2];
				users.add(user);
				stopIDs.add(stop);
			}
			br.close();//611624����  //110��5366����¼
			System.out.println("�û�������"+users.size());
			System.out.println("վ�����:"+stopIDs.size());//5664��վ��
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
