package br.com.service.util;

import java.util.Calendar;
import java.util.Date;

public class Util {

	public static String quebraLinhaTXT= "%n";
	
	public static int getMesInt(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int mes = c.get(Calendar.MONTH)+1;
		return mes;
	}

}
