package eg.edu.alexu.csd.oop.db;

import java.util.Vector;

public class table {
	private String name;
	private String database;
	private Vector<column> t;

	public table() {
		name = new String();
		t = new Vector<column>();
	}

	public void setdatabaseName(String database) {
		this.database = database;
	}

	public String getdatabaseName() {
		return database;
	}

	public String getname() {
		return name;
	}

	public void setname(String n) {
		name = n;
	}

	public void settable(Vector<column> t) {
		this.t = t;
	}

	public Vector<column> gettable() {
		return t;
	}

	public void addcolumn(column c) {
		t.add(c);
	}

	public void removecolumn(String n) {
		for (int i = 0; i < t.size(); i++) {
			if (t.elementAt(i).getname().equals(n)) {
				t.remove(i);
			}
		}
	}

	public column getColumn(String n) {
		for (int i = 0; i < t.size(); i++) {
			if (t.elementAt(i).getname().equals(n)) {
				return t.elementAt(i);
			}
		}
		return null;
	}

	public boolean ifColumnExists(String columnname) {
		for (int i = 0; i < t.size(); i++) {
			if (t.elementAt(i).getname().equals(columnname)) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmpty() {
		return this.t.isEmpty();
	}
}