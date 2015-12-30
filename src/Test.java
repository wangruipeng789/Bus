import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class Test {
public static void main(String[] args) {
//	SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
//	try {
//	System.out.print(sdf2.parse("01:00:00").getTime());
//	System.out.print(sdf2.parse("02:00:00").getTime());
//	} catch (ParseException e) {
//		e.printStackTrace();
//	}
	ArrayList<Integer> ls=new ArrayList<Integer>();
	ls.add(null);
	ls.add(null);
	ls.add(2);
	ls.add(0, 100);
	ls.add(1,12);
	ls.set(2, 999);
	System.out.println(ls.size());
	System.out.println(ls.get(2));
}
}
