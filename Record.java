import java.io.Serializable;
import java.util.Hashtable;

@SuppressWarnings("serial")
public class Record implements Serializable {

	String tableName;
	Object clustringKey;
	Hashtable<String, Object> colNameValue;

	public Record(String tableName, Hashtable<String, Object> colNameValue, String clustringKey) throws DBAppException {

		this.tableName = tableName;
		this.colNameValue = colNameValue;
		this.clustringKey = colNameValue.get(clustringKey);
		
	}

	public Object getClustringKey() {
		return clustringKey;
	}

	public void setClustringKey(Object clustringKey) {
		this.clustringKey = clustringKey;
	}

	public Hashtable<String, Object> getColNameValue() {
		return colNameValue;
	}

	public void setColNameValue(Hashtable<String, Object> colNameValue) {
		this.colNameValue = colNameValue;
	}

	@Override
	public String toString() {
		return "Record [clustringKey=" + clustringKey + ", colNameValue=" + colNameValue + "]";
	}

}
