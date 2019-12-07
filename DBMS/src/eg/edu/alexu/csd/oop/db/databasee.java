package eg.edu.alexu.csd.oop.db;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Vector;
    
    public class databasee implements Database {
        private boolean ppp ;
        private String dataname ;
        private databasetables db ;
        private int counter;
        private parse p ;
        private String[] columnsToPrint ;
        Files h= new Files();

        public Files getH() {
            return h;
        }

        public void setH(Files h) {
            this.h = h;
        }

        public String getDataname() {
            return dataname;
        }

        public void setDataname(String dataname) {
            this.dataname = dataname;
        }

        public databasee() {
            this.ppp = true;
            this.dataname = "kofta";
            this.db = new databasetables();
            this.counter = 0 ;
            this.p = new parse();
        }

        public databasetables getCurrentDB () {
        	return db;
        }

        public String[] getColumnsToPrint() {
        	return columnsToPrint;
        }

        public String removeSpaces(String s) {
            String temp = "";
            if (s.charAt(0) != ' ')
                temp += s.charAt(0);
            for (int i = 1; i < s.length(); i++) {
                if (s.charAt(i) == '\'') {
                    temp += s.charAt(i);
                    for (int r = i + 1; r < s.length(); r++) {
                        if (s.charAt(r) == '\'') {
                            temp += s.charAt(r);
                            i = r ;
                            break;
                        }
                        temp += s.charAt(r);
                    }
                }
                else if ((s.charAt(i) == ' ' && s.charAt(i - 1) == ' ') || s.charAt(i) == ';')
                    continue;
                else
                    temp += s.charAt(i);
            }
            while (true) {
                if (temp.charAt(temp.length() - 1) == ' ') {
                    temp = temp.substring(0, temp.length() - 1);
                } else
                    break;
            }
            if(counter == 5 || counter == 6 ){
                s = temp ;
                temp = "" ;
                for(int i = 0 ; i < s.length() ; i++){
                    if (s.charAt(i) == '\'') {
                        temp += s.charAt(i);
                        for (int r = i + 1; r < s.length(); r++) {
                            if (s.charAt(r) == '\'') {
                                temp += s.charAt(r);
                                i = r ;
                                break;
                            }
                            temp += s.charAt(r);
                        }
                    }
                    else if(s.charAt(i) == ','){
                        if(s.charAt(i+1) == ' '  && s.charAt(i-1) == ' '){
                            temp = temp.substring(0,temp.length()-1);
                            temp += "," ;
                            i++ ;
                        }
                        else if (s.charAt(i-1) == ' ' && s.charAt(i+1)!= ' '){
                            temp = temp.substring(0,temp.length()-1);
                            temp += ",";
                        }
                        else if (s.charAt(i+1) == ' ' && s.charAt(i-1) != ' '){
                            temp += ",";
                            i++ ;
                        }
                        else
                            temp += ",";
                    }
                    else
                        temp += s.charAt(i);
                }
            }
            return temp;
        }

        private String getname(String s) {
            s = removeSpaces(s);
            String[] arr = s.split(" ");

            String x = "";
            if (arr[2].contains("(")) {
                for (int i = 0; i < arr[2].indexOf('('); i++) {
                    x += arr[2].charAt(i);
                }
            } else {
                for (int i = 0; i < arr[2].length(); i++) {
                    x += arr[2].charAt(i);
                }
            }
            return x;
        }

        public String[] getNameSelect(String s) {
            s = removeSpaces(s);
            if (counter == 1 || counter == 2 || counter == 4) {
                String[] arr = s.split(" ");
                String[] ss = { "" };
                ss[0] = arr[2];
                return ss;
            } // create or drop database and drop table
            if (counter == 5 || counter == 6) {
                String[] arr = s.split(" ", 6);
                if (counter == 5) {
                    String[] ss = { "", "" };
                    ss[0] = arr[1];
                    ss[1] = arr[3];
                    return ss;
                } // select without condition when counter = 3
                else if (counter == 6) {
                    String[] ss = { "", "", "" };
                    ss[0] = arr[1];
                    ss[1] = arr[3];
                    ss[2] = arr[5];
                    return ss;
                } // select with condition when counter = 4
            } // select
            else if (counter == 10 || counter == 11) {
                String[] arr = s.split(" ", 5);
                if (counter == 10) {
                    String[] ss = { "", "" };
                    ss[0] = arr[2];
                    ss[1] = arr[4];
                    return ss;
                } else if (counter == 11) {
                    String[] ss = { "" };
                    ss[0] = arr[2];
                    return ss;
                }
            } // delete
            else if (counter == 8 || counter == 9) {
                String[] arr = s.split(" ");
                Vector<String> arr2 = new Vector<String>();
                for (int i = 0; i < arr.length; i++) {
                    int c = i + 1;
                    if (i != 0 && arr[i - 1].equals("WHERE")) {
                        while (c < arr.length) {
                            arr[i] += " " + arr[c];
                            c++;
                        }
                    }
                    arr2.add(arr[i]);
                    if (c == arr.length) {
                        break;
                    }
                }
                arr = new String[arr2.size()];
                arr2.copyInto(arr);
                return arr;
            }
            return null;
        }

        private String[] getCondition(String s) {
            String[] ss = { "", "", "" };
            String temp = "";
            if (s.contains("'")) {
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '"') {
                        temp += s.charAt(i);
                        for (int r = i + 1; r < s.length(); r++) {
                            if (s.charAt(r) == '"') {
                                temp += s.charAt(r);
                                i = r;
                                break;
                            }
                            temp += s.charAt(r);
                        }
                    } else {
                        if (s.charAt(i) != ' ') {
                            temp += s.charAt(i);
                        }
                    }
                }
            } else {
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) != ' ') {
                        temp += s.charAt(i);
                    }
                }
            }
            for (int i = 0; i < temp.length(); i++) {
                if (temp.charAt(i) == '=' || temp.charAt(i) == '<' || temp.charAt(i) == '>') {
                    ss[0] = temp.substring(0, i);
                    ss[1] = "" + temp.charAt(i);
                    ss[2] = temp.substring(i + 1);
                    break;
                }
            }
            if (ss[2].contains(",")) {
                ss[2] = ss[2].substring(0, ss[2].length() - 1);
            }
            return ss;
        } // btfsl elcondition elly bygely mn elmethod elly fo2

        @Override
        public String createDatabase(String databaseName, boolean dropIfExists) {
            databaseName = databaseName.toLowerCase();
            dataname = databaseName;
            try {
                if (h.isDatabaseExist(databaseName) && dropIfExists) {
                    counter = 2;
                    ppp = false;
                    executeStructureQuery("DROP database " + databaseName);
                    counter = 1;
                    ppp = false;
                    executeStructureQuery("CREATE database " + databaseName);
                } else if (h.isDatabaseExist(dataname) && !dropIfExists) {
                    return h.getpathof(dataname);
                } else {
                    counter = 1;
                    ppp = false;
                    executeStructureQuery("CREATE database " + dataname);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return h.getpathof(dataname);
        }

        @Override
        public boolean executeStructureQuery(String query) throws SQLException {
            query = query.toLowerCase();
            if (!dataname.contains(System.getProperty("file.separator")) || ppp) {
                counter = p.Check(query);
            }
            ppp = true;
            query = removeSpaces(query);
            if (counter == 0) {
                throw new SQLException("invalid");
            }
            String Name = getname(query);
            if (counter == 1) {
                dataname = Name;
                db.setname(Name);
                h.createDatabase(Name);
                return h.isDatabaseExist(Name);

            }
            if (counter == 3) {
                if (!h.isDatabaseExist(dataname)) {
                    throw new SQLException("invalid");
                }
                if (this.db.isTableExists(Name)) {
                    return false;
                }
                h.createtable(Name, dataname);
                for (int i = 1; i < query.length(); i++) {
                    if (query.charAt(i) == ',' && query.charAt(i - 1) == ' ') {
                        query = query.substring(0, i - 1) + query.substring(i);
                    }
                }
                String sub = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
                String[] arr = sub.split(" ");

                table t = new table();
                t.setname(Name);
                t.setdatabaseName(dataname);
                for (int i = 0; i < arr.length; i += 2) {
                    column c = new column();
                    c.setname(arr[i]);
                    if (i == arr.length - 2) {
                        c.settype(arr[i + 1]);
                    } else
                        c.settype(arr[i + 1].substring(0, arr[i + 1].length() - 1));
                    t.addcolumn(c);
                }
                db.addtable(t);
                if (query.contains("values")) {
                    String tmp = new String();
                    for (int i = 0; i < arr.length; i += 2) {
                        if (i == arr.length - 2) {
                            tmp += arr[i] + ")";
                        } else {
                            tmp += arr[i] + ", ";
                        }
                    }
                    String tmp2 = query.substring(query.lastIndexOf('(', query.length() - 1));
                    counter = 7;
                    executeUpdateQuery("insert into " + Name + "(" + tmp + " values" + tmp2);
                }
                return h.isTableExist(Name, dataname);

            }
            if (counter == 4) {
                if (this.db.isTableExists(Name)) {
                    h.dropTable(Name, dataname);
                    return true;
                } else
                    throw new SQLException("invalid");
            } else if
            (counter == 2) {
                h.DropDatabase(Name);
                return !h.isDatabaseExist(Name);
            }
            return false;
        }

        @Override
        public Object[][] executeQuery(String query) throws SQLException {
            query = query.toLowerCase();
            counter = p.Check(query);
            if (counter == 0) {
                throw new SQLException("invalid");
            }
            query = removeSpaces(query);
            String[] ss = getNameSelect(query);
            if (this.db.isTableExists(ss[1])) {
                if (db.getTable(ss[1]).isEmpty()) {
                    throw new SQLException("invalid");
                } // check if the table is empty
                if (counter == 5) {
                    if (ss[0].equals("*")) {
                        Vector<column> temp = db.getTable(ss[1]).gettable();
                        if(temp.elementAt(0).getcolumn().size() == 0){
                            Vector<column> tem = db.getTable(ss[1]).gettable();
                            columnsToPrint = new String[tem.size()];
                            for(int i = 0 ; i < tem.size(); i++){
                                columnsToPrint[i] = tem.elementAt(i).getname();
                            }
                            return new Object[1][1] ;
                        }
                        int rows = temp.elementAt(0).getcolumn().size();
                        int columns = temp.size();
                        Object[][] s = new Object[rows][columns];
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < columns; j++) {
                                s[i][j] = temp.elementAt(j).getcolumn().elementAt(i).getcell();
                            }
                        }
                        columnsToPrint = new String[temp.size()];
                        for(int i = 0 ; i < temp.size(); i++){
                            columnsToPrint[i] = temp.elementAt(i).getname();
                        }
                        return s;
                    } // select all columns
                    else{
                        String[] xx = ss[0].split(",");
                        for(int k = 0 ; k < xx.length ; k++){
                            if(db.getTable(ss[1]).ifColumnExists(xx[k])){
                                continue;
                            }
                            else
                                throw new SQLException("invalid");
                        }
                        if(db.getTable(ss[1]).getColumn(xx[0]).getcolumn().size() == 0){
                            columnsToPrint = new String[xx.length];
                            for(int i = 0 ; i < xx.length; i++){
                                columnsToPrint[i] = xx[i];
                            }
                            return new Object[1][1] ;
                        }
                        Object[][] z = new Object[db.getTable(ss[1]).getColumn(xx[0]).getcolumn().size()][xx.length];
                        for(int j = 0 ; j < xx.length ; j++){
                            column temp = db.getTable(ss[1]).getColumn(xx[j]);
                            for (int i = 0; i < temp.getcolumn().size(); i++) {
                                z[i][j] = temp.getcolumn().elementAt(i).getcell();
                            }
                        }
                        columnsToPrint = new String[xx.length];
                        for(int i = 0 ; i < xx.length; i++){
                            columnsToPrint[i] = xx[i];
                        }
                        return z;
                    } // check if specific column exists then retrieve it
                } // select without condition
                else if (counter == 6) {
                    String[] condition = getCondition(ss[2]);
                    if (this.db.getTable(ss[1]).ifColumnExists(condition[0])) {
                        column conditioned = this.db.getTable(ss[1]).getColumn(condition[0]);
                        if (ss[0].equals("*")) {
                            Vector<cell> z = conditioned.getcolumn();
                            int cc = 0;
                            int xx = 0;
                            if (condition[2].contains("'") && condition[1].equals("=")
                                    && conditioned.gettype().equals("varchar")) {
                                condition[2] = condition[2].substring(1, condition[2].length() - 1);
                                for (int i = 0; i < z.size(); i++) {
                                    if (z.elementAt(i).getcell().equals(condition[2])) {
                                        cc++;
                                    }
                                }
                                if (cc == 0) {
                                    Vector<column> temp = db.getTable(ss[1]).gettable();
                                    columnsToPrint = new String[temp.size()];
                                    for(int i = 0 ; i < temp.size(); i++){
                                        columnsToPrint[i] = temp.elementAt(i).getname();
                                    }
                                    return new Object[1][1];
                                }
                                Object[][] y = new Object[cc][this.db.getTable(ss[1]).gettable().size()];
                                for (int i = 0; i < z.size(); i++) {
                                    if (z.elementAt(i).getcell().equals(condition[2])) {
                                        for (int j = 0; j < this.db.getTable(ss[1]).gettable().size(); j++) {
                                            y[xx][j] = this.db.getTable(ss[1]).gettable().elementAt(j).getcolumn()
                                                    .elementAt(i).getcell();
                                        }
                                        xx++;
                                    }
                                }
                                Vector<column> temp = db.getTable(ss[1]).gettable();
                                columnsToPrint = new String[temp.size()];
                                for(int i = 0 ; i < temp.size(); i++){
                                    columnsToPrint[i] = temp.elementAt(i).getname();
                                }
                                return y;
                            }
                            else if (conditioned.gettype().equals("int") && !condition[2].contains("'")
                                    && !condition[2].contains(".")) {
                                int x = Integer.parseInt(condition[2]);
                                if (condition[1].equals("=")) {
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) == x) {
                                            cc++;
                                        }
                                    }
                                    if (cc == 0) {
                                        Vector<column> temp = db.getTable(ss[1]).gettable();
                                        columnsToPrint = new String[temp.size()];
                                        for(int i = 0 ; i < temp.size(); i++){
                                            columnsToPrint[i] = temp.elementAt(i).getname();
                                        }
                                        return new Object[1][1];
                                    }
                                    Object[][] y = new Object[cc][this.db.getTable(ss[1]).gettable().size()];
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) == x) {
                                            for (int j = 0; j < this.db.getTable(ss[1]).gettable().size(); j++) {
                                                y[xx][j] = this.db.getTable(ss[1]).gettable().elementAt(j).getcolumn()
                                                        .elementAt(i).getcell();
                                            }
                                            xx++;
                                        }
                                    }
                                    Vector<column> temp = db.getTable(ss[1]).gettable();
                                    columnsToPrint = new String[temp.size()];
                                    for(int i = 0 ; i < temp.size(); i++){
                                        columnsToPrint[i] = temp.elementAt(i).getname();
                                    }
                                    return y;
                                }
                                else if (condition[1].equals(">")) {
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) > x) {
                                            cc++;
                                        }
                                    }
                                    if (cc == 0) {
                                        Vector<column> temp = db.getTable(ss[1]).gettable();
                                        columnsToPrint = new String[temp.size()];
                                        for(int i = 0 ; i < temp.size(); i++){
                                            columnsToPrint[i] = temp.elementAt(i).getname();
                                        }
                                        return new Object[1][1];
                                    }
                                    Object[][] y = new Object[cc][this.db.getTable(ss[1]).gettable().size()];
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) > x) {
                                            for (int j = 0; j < this.db.getTable(ss[1]).gettable().size(); j++) {
                                                y[xx][j] = this.db.getTable(ss[1]).gettable().elementAt(j).getcolumn()
                                                        .elementAt(i).getcell();
                                            }
                                            xx++;
                                        }
                                    }
                                    Vector<column> temp = db.getTable(ss[1]).gettable();
                                    columnsToPrint = new String[temp.size()];
                                    for(int i = 0 ; i < temp.size(); i++){
                                        columnsToPrint[i] = temp.elementAt(i).getname();
                                    }
                                    return y;
                                }
                                else if (condition[1].equals("<")) {
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) < x) {
                                            cc++;
                                        }
                                    }
                                    if (cc == 0) {
                                        Vector<column> temp = db.getTable(ss[1]).gettable();
                                        columnsToPrint = new String[temp.size()];
                                        for(int i = 0 ; i < temp.size(); i++){
                                            columnsToPrint[i] = temp.elementAt(i).getname();
                                        }
                                        return new Object[1][1];
                                    }
                                    Object[][] y = new Object[cc][this.db.getTable(ss[1]).gettable().size()];
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) < x) {
                                            for (int j = 0; j < this.db.getTable(ss[1]).gettable().size(); j++) {
                                                y[xx][j] = this.db.getTable(ss[1]).gettable().elementAt(j).getcolumn()
                                                        .elementAt(i).getcell();
                                            }
                                            xx++;
                                        }
                                    }
                                    Vector<column> temp = db.getTable(ss[1]).gettable();
                                    columnsToPrint = new String[temp.size()];
                                    for(int i = 0 ; i < temp.size(); i++){
                                        columnsToPrint[i] = temp.elementAt(i).getname();
                                    }
                                    return y;
                                }
                            }
                            else {
                                throw new SQLException("invalid");
                            }
                        }
                        else  {
                            String[] xx = ss[0].split(",");
                            for(int k = 0 ; k < xx.length ; k++){
                                if(db.getTable(ss[1]).ifColumnExists(xx[k])){
                                    continue;
                                }
                                else
                                    throw new SQLException("invalid");
                            }
                            LinkedList<Object> temp = new LinkedList<>();
                            Vector<cell> z = conditioned.getcolumn();
                            if (condition[2].contains("'") && condition[1].equals("=")
                                    && conditioned.gettype().equals("varchar")) {
                                condition[2] = condition[2].substring(1, condition[2].length() - 1);
                                for (int i = 0; i < z.size(); i++) {
                                    if (z.elementAt(i).getcell().equals(condition[2])) {
                                        for(int l = 0 ; l < xx.length ; l++){
                                            temp.add(db.getTable(ss[1]).getColumn(xx[l]).getcolumn().elementAt(i).getcell());
                                        }
                                    }
                                }
                                if (temp.size() == 0) {
                                    columnsToPrint = new String[xx.length];
                                    for(int i = 0 ; i < xx.length; i++){
                                        columnsToPrint[i] = xx[i];
                                    }
                                    return new Object[1][1];
                                }
                                int yala = 0 ;
                                Object[][] y = new Object[temp.size()/xx.length][xx.length];
                                for(int i = 0 ; i < y.length ; i++){
                                    for(int j = 0 ; j < y[0].length ; j++){
                                        y[i][j] = temp.get(yala);
                                        yala++ ;
                                    }
                                }
                                columnsToPrint = new String[xx.length];
                                for(int i = 0 ; i < xx.length; i++){
                                    columnsToPrint[i] = xx[i];
                                }
                                return y;
                            }
                            else if (conditioned.gettype().equals("int") && !condition[2].contains("'")
                                    && !condition[2].contains(".")) {
                                int x = Integer.parseInt(condition[2]);
                                if (condition[1].equals("=")) {
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) == x) {
                                            for(int l = 0 ; l < xx.length ; l++){
                                                temp.add(db.getTable(ss[1]).getColumn(xx[l]).getcolumn().elementAt(i).getcell());
                                            }
                                        }
                                    }
                                } else if (condition[1].equals(">")) {
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) > x) {
                                            for(int l = 0 ; l < xx.length ; l++){
                                                temp.add(db.getTable(ss[1]).getColumn(xx[l]).getcolumn().elementAt(i).getcell());
                                            }
                                        }
                                    }
                                } else if (condition[1].equals("<")) {
                                    for (int i = 0; i < z.size(); i++) {
                                        if ((int) (z.elementAt(i).getcell()) < x) {
                                            for(int l = 0 ; l < xx.length ; l++){
                                                temp.add(db.getTable(ss[1]).getColumn(xx[l]).getcolumn().elementAt(i).getcell());
                                            }
                                        }
                                    }
                                }
                                if (temp.size() == 0) {
                                    columnsToPrint = new String[xx.length];
                                    for(int i = 0 ; i < xx.length; i++){
                                        columnsToPrint[i] = xx[i];
                                    }
                                    return new Object[1][1];
                                }
                                int yala = 0 ;
                                Object[][] y = new Object[temp.size()/xx.length][xx.length];
                                for(int i = 0 ; i < y.length ; i++){
                                    for(int j = 0 ; j < y[0].length ; j++){
                                        y[i][j] = temp.get(yala);
                                        yala++ ;
                                    }
                                }
                                columnsToPrint = new String[xx.length];
                                for(int i = 0 ; i < xx.length; i++){
                                    columnsToPrint[i] = xx[i];
                                }
                                return y;
                            }
                            else
                                throw new SQLException("invalid");
                        }
                    } else {
                        return new Object[1][1];
                    }
                }
            } else
                throw new SQLException("invavlid");
            return null;
        }

        @Override
        public int executeUpdateQuery(String query) throws SQLException {
            query = query.toLowerCase();
            counter = p.Check(query);
            query = removeSpaces(query);
            if (counter == 0) {
                throw new SQLException("invalid");
            }
            if (counter == 7) {
                String Name = getname(query);// name eltable
                if (!db.isTableExists(Name)) {
                    throw new SQLException("invalid");
                }
                for (int i = 0; i < query.length(); i++) {
                    if (i != query.length() - 1) {
                        if (query.charAt(i + 1) == ',' && query.charAt(i) == ' ') {
                            query = query.substring(0, i) + query.substring(i + 1);
                        }
                        if (query.charAt(i + 1) != ' ' && query.charAt(i) == ',') {
                            query = query.substring(0, i + 1) + " " +  query.substring(i + 1);
                        }
                    }
                }
                int o = 0;
                for (int j = 0; j < query.length(); j++) {
                    if (query.charAt(j) == '(') {
                        o++;
                    }
                }
                if (o != 2) {
                    String sub = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
                    String temp = "";
                    for(int i = 0 ; i < sub.length() ; i++){
                        if(sub.charAt(i) == '\''){
                            temp += sub.charAt(i);
                            for(int j = i+1 ; j < sub.length() ; j++){
                                temp += sub.charAt(j);
                                if(sub.charAt(j)=='\''){
                                    i = j ;
                                    break;
                                }
                            }
                        }
                        else if(sub.charAt(i)==' '){
                            continue;
                        }
                        else
                            temp += sub.charAt(i);
                    }
                    sub = temp ;
                    String[] arr = sub.split(",");
                    if (arr.length != db.getTable(Name).gettable().size()) {
                        throw new SQLException("invalid");
                    } else {
                        Object[] values = new Object[arr.length];
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].contains("'")) {
                                arr[i] = arr[i].substring(1, arr[i].length() - 1);
                                values[i] = arr[i];
                            } else {
                                values[i] = Integer.parseInt(arr[i]);
                            }
                        }
                        for (int i = 0; i < db.getTable(Name).gettable().size(); i++) {
                            cell c = new cell();
                            c.setcell(values[i]);
                            column col = db.getTable(Name).gettable().elementAt(i);
                            if (c.getcell() instanceof Integer) {
                                if (col.gettype().equals("int")) {
                                    col.addcell(c);
                                } else {
                                    throw new SQLException("invalid");
                                }
                            } else if (c.getcell() instanceof String) {
                                if (col.gettype().equals("varchar")) {

                                    col.addcell(c);
                                } else {
                                    throw new SQLException("invalid");
                                }
                            }

                        }
                    }
                }
                else {
                    String sub = query.substring(query.indexOf('(') + 1, query.indexOf(')'));
                    String[] arr = sub.split(", ");// col names
                    String sub2 = query.substring(query.lastIndexOf('(') + 1, query.lastIndexOf(')'));
                    String[] arr2 = sub2.split(", ");// values
                    if (arr.length != arr2.length || !db.isTableExists(Name)) {
                        return 0;
                    }
                    Object[] values = new Object[arr2.length];
                    for (int i = 0; i < arr2.length; i++) {
                        if (arr2[i].contains("'")) {
                            arr2[i] = arr2[i].substring(1, arr2[i].length() - 1);
                            values[i] = arr2[i];
                        } else {
                            values[i] = Integer.parseInt(arr2[i]);
                        }
                    }
                    for (int j = 0; j < arr.length; j++) {
                        cell c = new cell();
                        c.setcell(values[j]);
                        for (int i = 0; i < db.getTable(Name).gettable().size(); i++) {
                            column col = db.getTable(Name).gettable().elementAt(i);
                            if (arr[j].equals(col.getname())) {
                                if (c.getcell() instanceof Integer) {
                                    if (col.gettype().equals("int")) {
                                        col.addcell(c);
                                    } else {
                                        throw new SQLException("invalid");
                                    }
                                } else if (c.getcell() instanceof String) {
                                    if (col.gettype().equals("varchar")) {
                                        col.addcell(c);
                                    } else {
                                        throw new SQLException("invalid");
                                    }
                                }
                            }
                        }
                    }
                    try {
                        h.writeData(db.getTable(Name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return 1;
            }
            if (counter == 10 || counter == 11) {
                String[] ss = getNameSelect(query);
                if (this.db.isTableExists(ss[0])) {
                    if (db.getTable(ss[0]).isEmpty()) {
                        return 0;
                    } // check if the table has no columns
                    if (db.getTable(ss[0]).gettable().elementAt(0).isEmpty()) {
                        return 0;
                    } // check if the table columns are empty
                    if (counter == 10) {
                        String[] condition = getCondition(ss[1]);
                        if (this.db.getTable(ss[0]).ifColumnExists(condition[0])) {
                            column conditioned = this.db.getTable(ss[0]).getColumn(condition[0]);
                            Vector<column> deleting = this.db.getTable(ss[0]).gettable();
                            LinkedList<Integer> r = new LinkedList<>();
                            int counterr = 0;
                            if (condition[2].contains("'") && condition[1].equals("=")
                                    && conditioned.gettype().equals("varchar")) {
                                condition[2] = condition[2].substring(1, condition[2].length() - 1);
                                for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                    if (conditioned.getcolumn().elementAt(i).getcell().equals(condition[2])) {
                                        r.add(i);
                                        counterr++;
                                    }
                                }
                                for (int i = 0; i < counterr; i++) {
                                    int z = r.get(i);
                                    for (int j = 0; j < deleting.size(); j++) {
                                        deleting.elementAt(j).getcolumn().remove(z);
                                    }
                                }
                            } else if (conditioned.gettype().equals("int") && !condition[2].contains("'")
                                    && !condition[2].contains(".")) {
                                int x = Integer.parseInt(condition[2]);
                                if (condition[1].equals("=")) {
                                    for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                        if ((int) (conditioned.getcolumn().elementAt(i).getcell()) == x) {
                                            r.add(i);
                                            counterr++;
                                        }
                                    }
                                    for (int i = 0; i < counterr; i++) {
                                        int z = r.get(i);
                                        for (int j = 0; j < deleting.size(); j++) {
                                            deleting.elementAt(j).getcolumn().remove(z);
                                        }
                                    }
                                    return counterr;
                                } else if (condition[1].equals(">")) {
                                    for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                        if ((int) (conditioned.getcolumn().elementAt(i).getcell()) > x) {
                                            r.add(i);
                                            counterr++;
                                        }
                                    }
                                    for (int i = 0; i < counterr; i++) {
                                        int z = r.get(i);
                                        for (int j = 0; j < deleting.size(); j++) {
                                            deleting.elementAt(j).getcolumn().remove(z);
                                        }
                                    }
                                    return counterr;
                                } else if (condition[1].equals("<")) {
                                    for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                        if ((int) (conditioned.getcolumn().elementAt(i).getcell()) < x) {
                                            r.add(i);
                                            counterr++;
                                        }
                                    }
                                    for (int i = 0; i < counterr; i++) {
                                        int z = r.get(i);
                                        for (int j = 0; j < deleting.size(); j++) {
                                            deleting.elementAt(j).getcolumn().remove(z);
                                        }
                                    }
                                    h.deleteAndUpdate(db.getTable(ss[0]));
                                    return counterr;
                                } else
                                    return 0;
                            } else
                                throw new SQLException("invalid");
                            return r.size();
                        } else
                            throw new SQLException("invalid");
                    } // delete with condition
                    else if (counter == 11) {
                        Vector<column> deleting = this.db.getTable(ss[0]).gettable(); // columns of the table
                        int z = deleting.elementAt(0).getcolumn().size(); // number of cells in each column
                        for (int i = 0; i < deleting.size(); i++) {
                            deleting.elementAt(i).getcolumn().removeAllElements();
                        } // delete all cells in each column
                        h.deleteAndUpdate(db.getTable(ss[0]));
                        return z;
                    } // delete without condition
                } else
                    throw new SQLException("invalid");
            }
            else if (counter == 9 || counter == 8) {
                String[] ss = getNameSelect(query);
                if (this.db.isTableExists(ss[1])) {
                    if (db.getTable(ss[1]).isEmpty()) {
                        return 0;
                    } // check if the table has no columns
                    if (db.getTable(ss[1]).gettable().elementAt(0).isEmpty()) {
                        return 0;
                    }
                    if (counter == 9) {
                        for (int i = 3; i < ss.length; i++) {
                            String[] set = getCondition(ss[i]);
                            if (!db.getTable(ss[1]).ifColumnExists(set[0])) {
                                throw new SQLException("invalid");
                            }
                            column c = db.getTable(ss[1]).getColumn(set[0]);
                            if (c.gettype().equals("varchar") && !set[2].contains("'")) {
                                throw new SQLException("invalid");
                            }
                            if (c.gettype().equals("int") && set[2].contains("'")) {
                                throw new SQLException("invalid");
                            }
                            for (int j = 0; j < c.getcolumn().size(); j++) {
                                if (set[2].contains("'")) {
                                    set[2] = set[2].substring(1, set[2].length() - 1);
                                    cell ce = new cell();
                                    ce.setcell(set[2]);
                                    db.getTable(ss[1]).getColumn(set[0]).getcolumn().set(j, ce);
                                }
                                else{
                                    int y = Integer.parseInt(set[2]);
                                    cell ce = new cell();
                                    ce.setcell(y);
                                    db.getTable(ss[1]).getColumn(set[0]).getcolumn().set(j, ce);
                                }
                            }
                        }
                        h.deleteAndUpdate(db.getTable(ss[1]));
                        return db.getTable(ss[1]).gettable().size();
                    } else if (counter == 8) {
                        String[] condition = getCondition(ss[ss.length - 1]);
                        if (this.db.getTable(ss[1]).ifColumnExists(condition[0])) {
                            column conditioned = db.getTable(ss[1]).getColumn(condition[0]);
                            Vector<Integer> r = new Vector<Integer>();
                            int counterr = 0;
                            if (condition[2].contains("'") && condition[1].equals("=")
                                    && conditioned.gettype().equals("varchar")) {
                                condition[2] = condition[2].substring(1, condition[2].length() - 1);
                                for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                    if (conditioned.getcolumn().elementAt(i).getcell().equals(condition[2])) {
                                        r.add(i);
                                        counterr++;
                                    }
                                }
                            } else if (conditioned.gettype().equals("int") && !condition[2].contains("'")
                                    && !condition[2].contains(".")) {
                                int x = Integer.parseInt(condition[2]);
                                if (condition[1].equals("=")) {
                                    for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                        if ((int) (conditioned.getcolumn().elementAt(i).getcell()) == x) {
                                            r.add(i);
                                            counterr++;
                                        }
                                    }
                                } else if (condition[1].equals(">")) {
                                    for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                        if ((int) (conditioned.getcolumn().elementAt(i).getcell()) > x) {
                                            r.add(i);
                                            counterr++;
                                        }
                                    }
                                } else if (condition[1].equals("<")) {
                                    for (int i = 0; i < conditioned.getcolumn().size(); i++) {
                                        if ((int) (conditioned.getcolumn().elementAt(i).getcell()) < x) {
                                            r.add(i);
                                            counterr++;
                                        }
                                    }
                                }
                            } else
                                throw new SQLException("invalid");
                            if (counterr != 0) {
                                for (int i = 3; i < ss.length - 2; i++) {
                                    String[] set = getCondition(ss[i]);
                                    if (!db.getTable(ss[1]).ifColumnExists(set[0])) {
                                        throw new SQLException("invalid");
                                    }
                                    column c = db.getTable(ss[1]).getColumn(set[0]);
                                    if (c.gettype().equals("varchar") && !set[2].contains("'")) {
                                        throw new SQLException("invalid");
                                    }
                                    if (c.gettype().equals("int") && set[2].contains("'")) {
                                        throw new SQLException("invalid");
                                    }
                                    int z = 0;
                                    for (int j = 0; j < c.getcolumn().size(); j++) {
                                        if (set[2].contains("'")) {
                                            set[2] = set[2].substring(1, set[2].length() - 1);
                                        }
                                        if (z < r.size() && j == r.elementAt(z)) {
                                            cell ce = new cell();
                                            if (c.gettype().equals("int")) {
                                                int y = Integer.parseInt(set[2]);
                                                ce.setcell(y);
                                                db.getTable(ss[1]).getColumn(set[0]).getcolumn().set(j, ce);
                                            } else {
                                                ce.setcell(set[2]);
                                                db.getTable(ss[1]).getColumn(set[0]).getcolumn().set(j, ce);
                                            }
                                            z++;
                                        }
                                    }
                                }
                            }
                            h.deleteAndUpdate(db.getTable(ss[1]));
                            return counterr;
                        } else
                            throw new SQLException("invalid");
                    }

                } else
                    throw new SQLException("invalid");
            }

            return 0;
        }
    }