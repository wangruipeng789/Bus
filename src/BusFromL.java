import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;


public class BusFromL {
    public static void main(String[] args) {
	    BusFromL bfl=new BusFromL();
	    bfl.process();
	}
	private void process() {
		HashSet<String> busRotes=new HashSet<String>();
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\公交线路数据From\\shangHaiBusStation.csv")));
		    String[] recValues;
		    String   busRote;
		    String   lastChar;
		    br.readLine();//去掉第一行
			for(String line=br.readLine();line!=null;line=br.readLine()){
		    	recValues=line.split(",");
		    	busRote  =recValues[0];
		    	lastChar =busRote.substring(busRote.length()-1);
		    	if(lastChar.equals("A")||lastChar.equals("B")){
		    		busRote=(String) busRote.subSequence(0,busRote.indexOf(lastChar));
		    	}
		    	busRotes.add(busRote);
		    }
			br.close();
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\数据汇总\\一卡通乘客刷卡数据1\\SPTCC-20150401\\公交\\公交线路数据From\\busRotes.csv"))));
			Iterator<String> rotes_iters=busRotes.iterator();
			while(rotes_iters.hasNext()){
				String rote_iter=rotes_iters.next();
				bw.append(rote_iter+"\r\n");
			}
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
