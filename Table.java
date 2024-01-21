import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

@SuppressWarnings("serial")
public class Table implements Serializable {

	String tableName;
	String clustringKey;
//	int maxCount;
	Hashtable<String, String> colNameType;
	Hashtable<String, String> colNameMin;
	Hashtable<String, String> colNameMax;
	Vector<String> pages;

	public Table(String tableName, String clustringKey, Hashtable<String, String> colNameType,
			Hashtable<String, String> colNameMin, Hashtable<String, String> colNameMax
			, Vector<String> pages ) {
		super();
		this.tableName = tableName;
		this.clustringKey = clustringKey;
	//	this.maxCount = readConfig();
		this.colNameType = colNameType;
		this.colNameMin = colNameMin;
		this.colNameMax = colNameMax;
		this.pages=pages;
	}

	public Vector<String> getPages() {
		return pages;
	}

	public void setPages(Vector<String> pages) {
		this.pages = pages;
	}

/*	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	} 

	public static int readConfig() {
		Properties prop = new Properties();
		String fileName = "resources/DBApp.config";
		InputStream is = null;
		try {
			is = new FileInputStream(fileName);
		} catch (FileNotFoundException ex) {
			System.out.println("Config file not found!");
		}
		try {
			prop.load(is);
		} catch (IOException ex) {
		}
		return Integer.parseInt(prop.getProperty("MaximumRowsCountinPage"));
	} */

	public void write() {
		String fileName = "src/main/resources/data/" + this.tableName + ".bin";
		try {
			FileOutputStream fileOs = new FileOutputStream(fileName);
			@SuppressWarnings("resource")
			ObjectOutputStream os = new ObjectOutputStream(fileOs);
			os.writeObject(this);
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClustringKey() {
		return clustringKey;
	}

	public void setClustringKey(String clustringKey) {
		this.clustringKey = clustringKey;
	}

	public Hashtable<String, String> getColNameType() {
		return colNameType;
	}

	public void setColNameType(Hashtable<String, String> colNameType) {
		this.colNameType = colNameType;
	}

	public Hashtable<String, String> getColNameMin() {
		return colNameMin;
	}

	public void setColNameMin(Hashtable<String, String> colNameMin) {
		this.colNameMin = colNameMin;
	}

	public Hashtable<String, String> getColNameMax() {
		return colNameMax;
	}

	public void setColNameMax(Hashtable<String, String> colNameMax) {
		this.colNameMax = colNameMax;
	}

	@Override
	public String toString() {
		return "Table [tableName=" + tableName + ", clustringKey=" + clustringKey
				+ ", colNameType=" + colNameType + ", colNameMin=" + colNameMin + ", colNameMax=" + colNameMax
				+  "]";
	}
}