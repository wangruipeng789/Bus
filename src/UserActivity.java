import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserActivity<BusActivity> implements Comparable{
	static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String actUser;
	public String actTime;
	public String actLineName;
	public UserActivity(String actUser, String actTime, String actLineName) {
		super();
		this.actUser     = actUser;
		this.actTime     = actTime;
		this.actLineName = actLineName;
	}
	@Override
	public int compareTo(Object o) {
		long t1 = 0;
		long t2 = 0;
		Date d1 = null;
		try {
			d1 = sdf.parse(this.actTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		t1=d1.getTime();
		Date d2 = null;
		try {
			d2 = sdf.parse((( UserActivity)o).actTime);
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
