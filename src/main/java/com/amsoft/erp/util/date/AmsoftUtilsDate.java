package com.amsoft.erp.util.date;

import java.util.Calendar;
import java.util.Date;


/**
 * 
 * 
 * @author ITAUTEC
 */
public class AmsoftUtilsDate {

	
	/**
	 * 
	 * 
	 * 
	 * @param data
	 * @return
	 */
	public static Date getZeroHora(Date data) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();

	}

	
	/**
	 * 
	 * 
	 * 
	 * @param data
	 * @return
	 */
	public static Date getUltimaHora(Date data) {
		Calendar cal = Calendar.getInstance();

		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);

		return cal.getTime();
	}

}
