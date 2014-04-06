package timefly.adapters;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelper {

	String[] Months = new String[] { "Январь", "Февраль", "Март", "Апрель",
			"Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь",
			"Декабрь" };
	String[] Weeks = new String[] { "Суббота", "Понедельник", "Вторник",
			"Среда", "Четверг", "Пятница", "Воскресение" };

	SimpleDateFormat fullFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm"); // 0
																			// -
																			// type
																			// format
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // 1
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm"); // 2

	public DateHelper() {
	}

	public String getWeek(int numberOfWeek) {
		return Weeks[numberOfWeek - 1];
	}

	public String getMonth(int numberOfMonth) {
		return Months[numberOfMonth];
	}

	public String getWeek(String EnDay) {
		int numberOfWeek = 0;
		if (EnDay.equals("Mon")) {
			numberOfWeek = 1;
		}
		if (EnDay.equals("Tue")) {
			numberOfWeek = 2;
		}
		if (EnDay.equals("Wed")) {
			numberOfWeek = 3;
		}
		if (EnDay.equals("Thu")) {
			numberOfWeek = 4;
		}
		if (EnDay.equals("Fri")) {
			numberOfWeek = 5;
		}
		if (EnDay.equals("Sat")) {
			numberOfWeek = 0;
		}
		if (EnDay.equals("Sun")) {
			numberOfWeek = 6;
		}
		return Weeks[numberOfWeek];
	}

	public long getLongTime(String date1, String date2) {
		if (!(date1.equals("")) || !(date2.equals(""))) {
			Date dateInMillSec = null;
			int typeFormat = 0;
			if (!(date1.equals("")) && (date2.equals(""))) {
				typeFormat = 1;
			}
			try {
				if (typeFormat == 0) {
					dateInMillSec = (Date) fullFormat.parse(date1 + date2);
				} else if (typeFormat == 1) {
					dateInMillSec = (Date) dateFormat.parse(date1);
				} else {
					dateInMillSec = (Date) timeFormat.parse(date2);
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}
			return dateInMillSec.getTime();
		} else {
			return 0;
		}
	}

	public String getStringDate(long time) {
		if (time == 0) {
			return "Дата выполнения";
		} else {
			return dateFormat.format(time);
		}
	}

	public String getStringTime(long time) {
		if ((time == 0) || (timeFormat.format(time).toString().equals("00-00"))) {
			return "Время выполнения";
		} else {
			return timeFormat.format(time);
		}
	}
}
