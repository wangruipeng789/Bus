import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;


public class Cal {
   public static void main(String[] args) {
	  Cal cal=new Cal();
	  cal.process();
   }
   private void process() {
	// TODO Auto-generated method stub
	  HashSet<String> ZDIDs=new HashSet<String>(); 
	  try {
		BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\���ݻ���\\һ��ͨ�˿�ˢ������1\\SPTCC-20150401\\����\\վ��Info.csv")));
		String recValues[];
		String ZDID;
		for(String line=br.readLine();line!=null;line=br.readLine()){
			recValues=line.split(",");
			ZDID=recValues[2];
			ZDIDs.add(ZDID);
		}
		br.close();
		System.out.println("ԭʼվ���У�"+ZDIDs.size());//6636��վ�����
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	  
   } 
   
}
