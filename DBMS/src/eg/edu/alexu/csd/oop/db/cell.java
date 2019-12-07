package eg.edu.alexu.csd.oop.db;

public class cell {
	private Object value;

	public cell() {
		value = new Object();
	}

	public void setcell(Object c) {
		value = c;
	}

	public Object getcell() {
		return value;

	}
}