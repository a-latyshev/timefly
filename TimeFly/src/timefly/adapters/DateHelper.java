package timefly.adapters;

public class DateHelper {

	String[] Months = new String[] { "������", "�������", "����", "������",
			"���", "����", "����", "������", "��������", "�������", "������",
			"�������" };
	String[] Weeks = new String[] { "�����������", "�������", "�����",
			"�������", "�������", "�������", "�����������" };

	public DateHelper() {
	}

	public String getWeek(int numberOfWeek) {
		return Weeks[numberOfWeek - 1];
	}
	
	public String getMonth(int numberOfMonth) {
		return Months[numberOfMonth];
	}

}
