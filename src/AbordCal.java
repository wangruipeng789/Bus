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
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\各上车站点.csv")));
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
			br.close();//611624个人  //110万5366条记录
			System.out.println("用户个数："+users.size());
			System.out.println("站点个数:"+stopIDs.size());//5664个站点
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
