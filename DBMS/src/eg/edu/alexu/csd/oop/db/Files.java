package eg.edu.alexu.csd.oop.db;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Files {
	private File path;
	private String fs = System.getProperty("file.separator");

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
		path.mkdirs();
	}

	public Files() {
		path = new File("DD");
		path.mkdirs();
	}

	public String getpathof(String n) {
		return path.getAbsolutePath() + fs + n;
	}

	public boolean isDatabaseExist(String n) {
		File f = new File(path + fs + n);
		return f.exists();
	}

	public void createDatabase(String n) {
		File newDataBase = new File(path + fs + n);
		newDataBase.mkdirs();
	}

	public void DropDatabase(String n) {
		File p = new File(path + fs + n);
		if (!p.exists()) {
			return;
		}
		File[] f = p.listFiles();
		for (File c : f) {
			c.delete();
		}
		p.delete();
	}

	public void createtable(String tn, String dtn) {
		File xml = new File(path + fs + dtn + fs + tn + ".xml");
		File dtd = new File(path + fs + dtn + fs + tn + ".dtd");
		File xsd = new File(path + fs + dtn + fs + tn + ".xsd");
		try {
			xml.createNewFile();
			dtd.createNewFile();
			xsd.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public boolean isTableExist(String tn, String dtn) {
		File xml = new File(path + fs + dtn + fs + tn + ".xml");
		File dtd = new File(path + fs + dtn + fs + tn + ".dtd");
		File xsd = new File(path + fs + dtn + fs + tn + ".xsd");
		return xml.exists() && dtd.exists() && xsd.exists();
	}

	public void dropTable(String tn, String dtn) {
		File xml = new File(path + fs + dtn + fs + tn + ".xml");
		File dtd = new File(path + fs + dtn + fs + tn + ".dtd");
		xml.delete();
		dtd.delete();
	}

	public void writeData(table t) throws Exception {
		write_dtd(t);
		schemaWriter(t);
		String path = this.getpathof(t.getdatabaseName()) + fs + t.getname() + ".xml";
		File fl = new File(path);
		Element Databasename ;
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = f.newDocumentBuilder();
		Document doc = b.newDocument();
		int uuu = 0 ;
        if(t.getdatabaseName().contains(System.getProperty("file.separator"))){
            for( uuu = t.getdatabaseName().length()-1 ; uuu >= 0 ; uuu--){
                String x = t.getdatabaseName().substring(uuu,uuu+1);
                if(x.equals(System.getProperty("file.separator"))){
                    break;
                }
            }
            Databasename = doc.createElement(t.getdatabaseName().substring(uuu+1));
        }else{
            Databasename = doc.createElement(t.getdatabaseName());
        }
		doc.createTextNode("!DOCTYPE table SYSTEM " + "\"" + t.getname() + ".dtd" + "\"");
		Element column_num = doc.createElement("column_num");
		Element row_num = doc.createElement("row_num");
		Element tablename = doc.createElement("tablename");
		Element table_name = doc.createElement("table_name");
		int n = t.gettable().size();
		Element column_name[] = new Element[n];
		Element[] col_name = new Element[n];
		;

		int j = 0;
		for (int i = 0; i < n; i++) {
			column_name[i] = doc.createElement(t.gettable().elementAt(i).getname());
		}

		table_name.setTextContent(t.getname());

		Databasename.appendChild(column_num);
		Databasename.appendChild(row_num);
		Databasename.appendChild(table_name);
		for (int i = 0; i < n; i++) {
			col_name[i] = doc.createElement("col" + String.valueOf(i));
			col_name[i].setTextContent(t.gettable().elementAt(i).getname());
			Databasename.appendChild(col_name[i]);
		}

		Databasename.appendChild(tablename);
		for (int i = 0; i < n; i++)
			tablename.appendChild(column_name[i]);
		for (int i = 0; i < n; i++) {
			int a = t.gettable().elementAt(i).getcolumn().size();
			for (j = 0; j < a; j++) {
				Element e = doc.createElement("col" + String.valueOf(i) + "cell" + String.valueOf(j));
				e.setTextContent(String.valueOf(t.gettable().elementAt(i).getcolumn().elementAt(j).getcell()));
				column_name[i].appendChild(e);
			}
		}
		doc.appendChild(Databasename);
		column_num.setTextContent(String.valueOf(t.gettable().size()));
		row_num.setTextContent(String.valueOf(j));
		DOMSource s = new DOMSource(doc);
		Result r = new StreamResult(fl);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer th = tf.newTransformer();
		th.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		th.setOutputProperty(OutputKeys.INDENT, "no");
		th.transform(s, r);

	}

	public table read_xml_file(String path) {
		table t = new table();
		File fl = new File(path);
		if (!fl.exists()) {
			System.out.println("file doesn't exist");
			return null;
		}
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = null;
		try {
			b = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document doc = null;
		try {
			doc = b.parse(fl);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		NodeList col_num = doc.getElementsByTagName("column_num");
		int column_num = Integer.parseInt(col_num.item(0).getTextContent());
		NodeList row_num = doc.getElementsByTagName("row_num");
		int r_num = Integer.parseInt(row_num.item(0).getTextContent());
		NodeList table_name = doc.getElementsByTagName("table_name");
		String table__name = table_name.item(0).getTextContent();
		t.setname(table__name);
		NodeList[] ni = new NodeList[column_num];
		//columnname
		for (int i = 0; i < column_num; i++) {
			ni[i] = doc.getElementsByTagName("col" + String.valueOf(i));
			column c = new column();
			c.setname((ni[i].item(0).getTextContent()));
			t.addcolumn(c);

		} // cell creation
		ni = new NodeList[r_num];

		for (int i = 0; i < column_num; i++) {
			for (int j = 0; j < r_num; j++) {
				ni[j] = doc.getElementsByTagName("col" + String.valueOf(i) + "cell" + String.valueOf(j));
				cell cc = new cell();
				cc.setcell(ni[j].item(0).getTextContent());
				t.gettable().elementAt(i).getcolumn().add(cc);
			}
		}
		return t;

	}

	private void write_dtd(table t) {
		String path = this.getpathof(t.getdatabaseName()) + fs + t.getname() + ".dtd";
		File fl = new File(path);
		try {
			FileWriter f = new FileWriter(fl);
			BufferedWriter b = new BufferedWriter(f);
			b.write("XML Schema Generator\r\n" + "Generated Schema (.dtd):\r\n" + "<?xml encoding=\"UTF-8\"?>\n\n");
			String[] ss = new String[t.gettable().size()];
			String x = "";
			for (int i = 0; i < t.gettable().size(); i++) {
				ss[i] = "col" + String.valueOf(i);

				x += ss[i] + ",";
			}
			b.write("<!ELEMENT tess (column_num,row_num,table_name," + x + "tablename)>");
			b.write("\n<!ATTLIST tess\r\n" + "  xmlns CDATA #FIXED ''>\r\n" + "\r\n"
					+ "<!ELEMENT column_num (#PCDATA)>\r\n" + "<!ATTLIST column_num\r\n"
					+ "  xmlns CDATA #FIXED ''>\r\n" + "\r\n" + "<!ELEMENT row_num (#PCDATA)>\r\n"
					+ "<!ATTLIST row_num\r\n" + "  xmlns CDATA #FIXED ''>\r\n" + "\r\n"
					+ "<!ELEMENT table_name (#PCDATA)>\r\n" + "<!ATTLIST table_name\r\n" + " xmlns CDATA #FIXED ''>\r\n"
					+ "\r\n" + "");
			for (int i = 0; i < ss.length; i++) {
				b.write("\n<!ELEMENT " + ss[i] + " (#PCDATA)>\r\n" + "<!ATTLIST " + ss[i]
						+ "\n  xmlns CDATA #FIXED ''>\n");

			}
			x = "";
			String col[] = new String[t.gettable().size()];
			for (int i = 0; i < t.gettable().size(); i++) {
				col[i] = t.gettable().elementAt(i).getname();
				if (i == t.gettable().size() - 1) {
					x += col[i];
					break;
				}
				x += col[i] + ",";
			}
			b.write("\n<!ELEMENT tablename (" + x + ")>\n");
			x = "";
			for (int j = 0; j < col.length; j++) {
				x += "\n<!ELEMENT " + col[j] + "(";
				for (int i = 0; i < t.gettable().elementAt(0).getcolumn().size(); i++) {
					x += "col" + String.valueOf(j) + "cell" + String.valueOf(i) + ",";
				}
				x = x.substring(0, x.length() - 1);
				x += ")>\n<!ATTLIST " + col[j] + "\n xmlns CDATA #FIXED ''>\n";
			}
			b.write(x);
			x = "";
			for (int j = 0; j < col.length; j++) {
				for (int i = 0; i < t.gettable().elementAt(0).getcolumn().size(); i++) {
					x += "\n<!ELEMENT " + "col" + String.valueOf(j) + "cell" + String.valueOf(i) + " (#PCDATA)>\r\n"
							+ "<!ATTLIST " + "col" + String.valueOf(j) + "cell" + String.valueOf(i) + "\r\n"
							+ "  xmlns CDATA #FIXED ''>\n";
				}
			}
			b.write(x);
			b.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void schemaWriter(table t) throws ParserConfigurationException, TransformerException {
		String path = this.getpathof(t.getdatabaseName()) + fs + t.getname() + ".xsd";
		File fl = new File(path);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.newDocument();

		String attributes[][] = new String[t.gettable().size()][2];
		for (int i = 0; i < attributes.length; i++) {
			attributes[i][0] = t.gettable().elementAt(i).getname();
			attributes[i][1] = t.gettable().elementAt(i).gettype();
		}
		// root element
		Element rootName = doc.createElement("xs:schema");
		rootName.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
		doc.appendChild(rootName);

		// root element
		Element subElement = doc.createElement("xs:element");
		subElement.setAttribute("name", "Table");
		Element complexElement = doc.createElement("xs:complexType");
		Element sequence = doc.createElement("xs:sequence");
		Element rowComplexElement = doc.createElement("xs:complexType");
		Element rowSequence = doc.createElement("xs:all");
		Element row = doc.createElement("xs:element");
		row.setAttribute("name", "Row");
		row.setAttribute("maxOccurs", "unbounded");
		row.appendChild(rowComplexElement);
		rowComplexElement.appendChild(rowSequence);
		complexElement.appendChild(sequence);
		sequence.appendChild(row);
		subElement.appendChild(complexElement);
		rootName.appendChild(subElement);

		// table columns
		int i = 0;
		for (i = 0; i < attributes.length; i++) {
			Element column = doc.createElement("xs:element");
			column.setAttribute("minOccurs", "0");
			Attr Type = doc.createAttribute("type");
			Attr Name = doc.createAttribute("name");
			Name.setValue(attributes[i][0]);
			column.setAttributeNode(Name);
			if (attributes[i][1].equalsIgnoreCase("varchar"))
				Type.setValue("xs:string");
			else
				Type.setValue("xs:integer");
			column.setAttributeNode(Type);
			rowSequence.appendChild(column);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(fl);
			transformer.transform(source, result);

		}

	}
	
	public void deleteAndUpdate(table t) {
		try {
			writeData(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}