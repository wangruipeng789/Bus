import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DriveActivity implements Comparable{
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String machineID;
	public String lineID;
	public String stopID;
	public String driveTime;
	public DriveActivity(String machineID, String lineID, String stopID,String driveTime) {
		super();
		this.machineID = machineID;
		this.lineID = lineID;
		this.stopID = stopID;
		this.driveTime = driveTime;
	}
	@Override
	public int compareTo(Object o) {
		long t1 = 0;
		long t2 = 0;
		Date d1 = null;
		try {
			d1 = sdf.parse(this.driveTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		t1=d1.getTime();
		Date d2 = null;
		try {
			d2 = sdf.parse(((DriveActivity)o).driveTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		t2=d2.getTime();
		if(t1>t2){
			return 1;
		}else if(t1==t2){
			return 0;
		}else{
			return -1;
		}
	}

}
