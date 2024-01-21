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
public class Page implements Serializable {

	String tableName;
	Object minKey;
	Object maxKey;
	String clustringKey;
	int pageNum;
	int currentRowNumber;
	int maximumRowsCountinPage;
	Vector<Record> records;
	public Page(String tableName, String clustringKey,Integer pageNum,Vector<Record> records) {
		this.tableName = tableName;
		this.clustringKey = clustringKey;
		this.maximumRowsCountinPage = readConfig();
		this.pageNum = pageNum;
		this.records = records;
		this.updateMinMaxCurrent();
	}
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public static int readConfig() {
		Properties prop = new Properties();
		String fileName = "src/resources/DBApp.config";
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
	}

	public Vector<Object> getClustringKeys() {
		Vector<Object> r = new Vector<Object>();
		for (Record element : records) {
			r.add(element.getClustringKey());
		}
		return r;
	}

	public boolean isEmpty() {
		if (this.currentRowNumber == 0)
			return true;
		return false;
	}

	public boolean isFull() {
		if (this.currentRowNumber == this.maximumRowsCountinPage)
			return true;
		return false;
	}

	public void updateMinMaxCurrent() {
		this.minKey = this.records.get(0).getClustringKey();
		this.maxKey = this.records.get(this.records.size() - 1).getClustringKey();
		this.currentRowNumber = this.records.size();
	}

	public void write() {
		String fileName = "src/main/resources/data/" + this.getTableName() + "Page" + pageNum + ".bin";
		try {
			FileOutputStream fileOs = new FileOutputStream(fileName);
			ObjectOutputStream os = new ObjectOutputStream(fileOs);
			os.writeObject(this.records);
			fileOs.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Hashtable<String, Object> getMinMaxCountN() {
		Hashtable<String, Object> v = new Hashtable<String, Object>();
		v.put("min", this.minKey);
		v.put("max", this.maxKey);
		v.put("count", this.currentRowNumber);
		v.put("pageN", this.pageNum);
		return v;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Object getMinKey() {
		return minKey;
	}

	public void setMinKey(Object minKey) {
		this.minKey = minKey;
	}

	public Object getMaxKey() {
		return maxKey;
	}

	public void setMaxKey(Object maxKey) {
		this.maxKey = maxKey;
	}

	public String getClustringKey() {
		return clustringKey;
	}

	public void setClustringKey(String clustringKey) {
		this.clustringKey = clustringKey;
	}

	public int getCurrentRowNumber() {
		return currentRowNumber;
	}

	public void setCurrentRowNumber(int currentRowNumber) {
		this.currentRowNumber = currentRowNumber;
	}

	public int getMaximumRowsCountinPage() {
		return maximumRowsCountinPage;
	}

	public void setMaximumRowsCountinPage(int maximumRowsCountinPage) {
		this.maximumRowsCountinPage = maximumRowsCountinPage;
	}

	public Vector<Record> getRecords() {
		return records;
	}

	public void setRecords(Vector<Record> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return "Page [tableName=" + tableName + ", minKey=" + minKey + ", maxKey=" + maxKey + ", clustringKey="
				+ clustringKey + ", currentRowNumber=" + currentRowNumber + ", maximumRowsCountinPage="
				+ maximumRowsCountinPage + ", records=" + records + "]";
	}
}
