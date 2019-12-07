package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.Scanner;

public class mainn {
	private static databasee d = new databasee();
	private static parse p = new parse();
	private static int counter;

	public static void parse(String query) throws SQLException {
		query = query.toLowerCase();
		counter = p.Check(query);
		if (counter == 0) {
			throw new SQLException("invalid");
		}
		if (counter == 1 || counter == 2 || counter == 3 || counter == 4) {
			try {
				d.executeStructureQuery(query);
				System.out.println("Done!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if (counter == 5 || counter == 6) {
			try {
				Object[][] selected = d.executeQuery(query);
				String[] columns = d.getColumnsToPrint();
				String tmp = "";
				int c = columns[0].length();
				for (int i = 0; i < columns.length; i++) {
					if (columns[i].length() > c) {
						c = columns[i].length();
					}
				}
				for (int i = 0; i < selected.length; i++) {
					for (int j = 0; j < selected[0].length; j++) {
						if (selected[i][j].toString().length() > c) {
							c = selected[i][j].toString().length();
						}
					}
					}
				c += 4;
				for (int i = 0; i < columns.length; i++) {
					int tmpc = 0;
					int n = c;
					c = (c - columns[i].length())/2;
					while (tmpc < c) {
						tmp += " ";
						tmpc ++;
					}
					tmp += columns[i];
					tmpc = 0;
					while (tmpc < c) {
						tmp += " ";
						tmpc ++;
					}
					tmp += "|";
					c = n;
				}
				System.out.println(tmp);
				if (selected.length == 1 && selected[0].length == 1 && selected[0][0] == null) {
					return;
				} else {
					String tmp2 = "";
					for (int i = 0; i < selected.length; i++) {
						for (int j = 0; j < selected[0].length; j++) {
							int tmpc = 0;
							int n = c;
							c = (c - selected[i][j].toString().length())/2;
							while (tmpc < c) {
								tmp2 += " ";
								tmpc ++;
							}
							tmp2 += selected[i][j] ;
							tmpc = 0;
							while (tmpc < c) {
								tmp2 += " ";
								tmpc ++;
							}
							tmp2 += "|";
							c = n;
						}
						System.out.println(tmp2);
						tmp2 = "";
						
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			try {
				System.out.println("rows affected: " + d.executeUpdateQuery(query));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) throws SQLException {
		String s = "";
		Scanner in = new Scanner(System.in);
		do {
			System.out.println("Write your SQL statement: (exit if done)");
			s = in.nextLine();
			if (s.toLowerCase().contains("exit")) {
				break;
			}
			parse(s);
		} while (true);
		System.out.println("Thank You!");
		in.close();

	}
}
