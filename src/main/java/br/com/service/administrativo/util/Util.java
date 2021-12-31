package br.com.service.administrativo.util;

import java.text.SimpleDateFormat;
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
	
	public int getMesInt2(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int mes = c.get(Calendar.MONTH)+1;
		return mes;
	}

	
	public static String getDataFimMesString(int mes, int ano) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String dataFormatada = formato.format(getDataFimMes(mes, ano));
		return dataFormatada;
	}

	public static String getDataInicioMesString(int mes, int ano) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String dataFormatada = formato.format(getDataInicioMes(mes, ano));
		return dataFormatada;
	}
	
	/*public static long diferencaEntreDatas(Date data1, Date data2) {
		DateTime dt1 = new DateTime(data1.getTime());
		DateTime dt2 = new DateTime(data2.getTime());

		Days d = Days.daysBetween(dt2, dt1);
		int days = d.getDays();
		return days;
	}*/

	public static String formatarDouble2Decimais(double valor) {
		String sb = String.format("%.2f", valor);

		return sb;
	}

	public static Date getDataInicioMes(int mes, int ano) {
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(new Date());
		dataInicio.set(Calendar.MONTH, mes - 1);
		dataInicio.set(Calendar.DAY_OF_MONTH, 1);
		dataInicio.set(Calendar.YEAR, ano);

		return dataInicio.getTime();
	}

	public static Date getDataFimMes(int mes, int ano) {
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(new Date());
		dataInicio.set(Calendar.YEAR, ano);
		dataInicio.set(Calendar.MONTH, mes - 1);

		int ultimo = dataInicio.getActualMaximum(Calendar.DAY_OF_MONTH);
		dataInicio.set(Calendar.DAY_OF_MONTH, ultimo);
		dataInicio.set(Calendar.HOUR_OF_DAY, 23);

		return dataInicio.getTime();
	}

	public static boolean nullOrTrue(Boolean value) {
		if (value == null) {
			return false;
		}
		return value;

	}
}
