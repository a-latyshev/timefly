package timefly.adapters;

public class DateHelper {

	String[] Months = new String[] { "Январь", "Февраль", "Март", "Апрель",
			"Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь",
			"Декабрь" };
	String[] Weeks = new String[] { "Понедельник", "Вторник", "Среда",
			"Четверг", "Пятница", "Суббота", "Воскресение" };

	public DateHelper() {
	}

	public String getWeek(int numberOfWeek) {
		return Weeks[numberOfWeek - 1];
	}
	
	public String getMonth(int numberOfMonth) {
		return Months[numberOfMonth];
	}

}
