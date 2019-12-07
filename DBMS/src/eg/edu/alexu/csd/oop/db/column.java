package eg.edu.alexu.csd.oop.db;

import java.util.Vector;

public class column {
    private String name;
    private Vector<cell>c;
    private String type;
    public column() {
        name=new String();
        c=new Vector<cell>();
    }

    public String getname() {
        return name;
    }

    public void setname(String n) {
        name=n;
    }

    public String gettype() {
        return type;
    }

    public void settype(String n) {
        type=n;
    }

    public Vector<cell>  getcolumn() {
        return c;
    }

    public void setcolumn(Vector<cell>c) {
        this.c=c;
    }

    public void addcell(cell x) {
        c.add(x);
    }

    public boolean isEmpty (){
        return this.c.isEmpty();
    }
}