import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.crypto.Data;
import java.io.Serializable;

public class DBApp implements Serializable {
	public void init( ) {}
	
	public void createTable(String strTableName, 
			 String strClusteringKeyColumn, 
			Hashtable<String,String> htblColNameType, 
			Hashtable<String,String> htblColNameMin, 
			Hashtable<String,String> htblColNameMax ) throws DBAppException{
		 for (String col : htblColNameType.keySet())
	            if (!htblColNameMin.containsKey(col) || !htblColNameMax.containsKey(col))
	                throw new DBAppException();
		 for (String col : htblColNameMax.keySet())
	            if (!htblColNameType.containsKey(col) )
	                throw new DBAppException();

	        for (String col : htblColNameMin.keySet())
	            if (!htblColNameType.containsKey(col) )
	                throw new DBAppException();

	        for (String col : htblColNameType.keySet())
	            if (htblColNameType.get(col).equals("java.lang.Integer") || htblColNameType.get(col).equals("java.lang.String")|| htblColNameType.get(col).equals("java.lang.Double")|| htblColNameType.get(col).equals("java.util.Date")) {
	            	
	            }
	            else {
	                throw new DBAppException();
	            }
		File newTable= new File("src/resources/"+strTableName); //3aaaa
		if (newTable.exists())
			throw new DBAppException("Table already exists!");
		else
			newTable.mkdir();
		Vector <String> page= new Vector<String>();
		Table table =new Table(strTableName,strClusteringKeyColumn,htblColNameType,htblColNameMin,htblColNameMax,page);
		try {
		FileOutputStream fileOut = new FileOutputStream("src/resources/"+strTableName+"/"+strTableName+".ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(table);
		out.close();
		fileOut.close();}
		catch (IOException e) {
           e.printStackTrace();
       }
	
		try {
			FileReader currentFile = new FileReader("src/resources/metadata.csv");
			BufferedReader br = new BufferedReader(currentFile);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
           while ((line = br.readLine()) != null) {
           	stringBuilder.append(line).append('\n');
           	
                                                   }
           FileWriter metaDataFile = new FileWriter("src/resources/metadata.csv");
              for(String ColName :htblColNameType.keySet()) {
           	   stringBuilder.append(strTableName).append(",");
           	   stringBuilder.append(ColName).append(",");
           	   stringBuilder.append(htblColNameType.get(ColName)).append(",");
           	   if (ColName.equals(strClusteringKeyColumn))
           		   stringBuilder.append("TRUE").append(",");
           	   else 
           		   stringBuilder.append("FALSE").append(",");
           		   
           	   stringBuilder.append("null").append(",");
           	   stringBuilder.append("null").append(",");
           	   stringBuilder.append(htblColNameMin.get(ColName)).append(",");
                 stringBuilder.append(htblColNameMax.get(ColName));
                  stringBuilder.append("\n");
              }
              metaDataFile.write(stringBuilder.toString());
              metaDataFile.close();	               

		}
		catch (IOException ignored) {
       }
	}
		
	public void insertIntoTable(String strTableName,  //a single row insertion 
		 Hashtable<String,Object> htblColNameValue) 
		 throws DBAppException{
		try {
			String file= "src/resources/metadata.csv";
			FileReader reader=new FileReader(file);
			BufferedReader buff=new BufferedReader(reader);
			String line=buff.readLine();
			boolean flag1=false;
			while(!(line==null)) {
				String arr[]=line.split(",");
				if(strTableName.equals(arr[0])) {
					flag1=true;
					break;
				}
				line=buff.readLine();
			}
			if(flag1=false) {
				throw new DBAppException("Table "+strTableName+" does not exist");
			}
		    String clskey=null;
			for(String colname : htblColNameValue.keySet()) {
				FileReader reader2=new FileReader(file);
				BufferedReader buff2=new BufferedReader(reader2);
				String line2=buff2.readLine();
				boolean flag2=false;
		    	while(!(line2==null)) {
		    		String arr[]=line2.split(",");
		    		if(strTableName.equals(arr[0]) && colname.equals(arr[1])) {
		    			flag2=true;
		    			if(arr[3].equals("TRUE")) {
		    				clskey=colname;
		    			}
		    			if(arr[2].equals("java.lang.Integer")) {
		    				if(!(htblColNameValue.get(colname) instanceof Integer)) {
		    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type Integer");
		    				}	
		    				if(((int)htblColNameValue.get(colname))<Integer.parseInt(arr[6])
		    				|| ((int)htblColNameValue.get(colname))>Integer.parseInt(arr[7])) {
		    				    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
		    				}
		    			}
		    			else if(arr[2].equals("java.lang.String")){
		    				if(!(htblColNameValue.get(colname) instanceof String)) {
		    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type String");
		    				}	
                            if(((String)htblColNameValue.get(colname)).compareTo(arr[6])<0 
                            || ((String)htblColNameValue.get(colname)).compareTo(arr[7])>0)  {
                        	    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
		    				}
		    			}
		    			else if(arr[2].equals("java.lang.Double")) {
		    				if(!(htblColNameValue.get(colname) instanceof Double)) {
		    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type Double");
		    				}	
		    				if(((Double)htblColNameValue.get(colname))<Double.parseDouble(arr[6])
				    		|| ((Double)htblColNameValue.get(colname))>Double.parseDouble(arr[7])) {
		    				    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
				   			}
		    			}
		    			else if (arr[2].equals("java.util.Date")) {
		    				if(!(htblColNameValue.get(colname) instanceof Date)) {
		    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type Date");
		    				}
		    				Date min = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(arr[6]);
		    				Date max = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(arr[7]);
		    				if((((Date)htblColNameValue.get(colname)).compareTo(min)<0)
						   	|| (((Date)htblColNameValue.get(colname)).compareTo(max)>0)) {
				    		    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
							}
		    			} 
		    			break;
		    		}
		    		line2=buff2.readLine();
		    	}
		    	if(flag2==false) {
		    		throw new DBAppException("column "+colname+" does not exist in Table");
		    	}
		    }
			if(clskey==null) {
				throw new DBAppException("clustering key is not inserted in the record");
			}
			
			
			Record r=new Record(strTableName,htblColNameValue,clskey);
			Table table= null;
			FileInputStream filein = new FileInputStream("src/resources/"+strTableName+"/"+strTableName+".ser");
			ObjectInputStream in = new ObjectInputStream(filein);
			table = (Table) in.readObject();
			in.close();
			filein.close();
			if(table.pages.isEmpty()) {
				Vector <Record> records = new Vector<Record>();
				records.add(r);
				Page p=new Page(strTableName,clskey,1,records);
				table.pages.add(0,"src/resources/"+strTableName+"/"+p.tableName+"_0.ser");
				FileOutputStream tableOut = new FileOutputStream(table.pages.get(0));
			    ObjectOutputStream outable = new ObjectOutputStream(tableOut);
				outable.writeObject(p);
				outable.close();
				tableOut.close();
				}
			else {
				int pageindex=binarysearchpage(table.pages,r.getClustringKey()); //note
				if(pageindex==table.pages.size()) {
				//	System.out.println("well,hello there");
				     Vector <Record> records = new Vector<Record>();
				     records.add(r);
				     int pagenum=pageindex+1;
				     Page p=new Page(strTableName,clskey,pagenum,records);
				     table.pages.add(pageindex,"src/resources/"+strTableName+"/"+p.tableName+"_"+pageindex+".ser");
				     FileOutputStream fileOut = new FileOutputStream("src/resources/"+strTableName+"/"+p.tableName+"_"+pageindex+".ser");
				     ObjectOutputStream out = new ObjectOutputStream(fileOut);
				     out.writeObject(p);
				     out.close();
				     fileOut.close();
				}
				else {
					 Page page = getPage(table.pages.get(pageindex));
				     int recordindex;
				     if (binarySearch(page.getClustringKeys(), r.getClustringKey()) == -1) {
					     recordindex=binarySearchInsertion(page.getClustringKeys(),r.clustringKey);
				     } else { 
					     throw new DBAppException("The clustring Key Already exists!");
				     }
				     page.records.add(recordindex,r);
				     page.updateMinMaxCurrent();
				     if(page.records.size()>page.maximumRowsCountinPage) { //page was full
					     Record temporary= page.records.get(page.maximumRowsCountinPage);
					     page.records.remove(page.maximumRowsCountinPage);
					     page.updateMinMaxCurrent();
					     releasePage(table.pages.get(pageindex),page);
					     boolean emptyexists = false;
					     Page pagee;FileInputStream fil;ObjectInputStream obj;
					     while((++pageindex)<table.pages.size()) {
						     pagee=getPage(table.pages.get(pageindex));
						     int rr;
						     if (binarySearch(pagee.getClustringKeys(), r.getClustringKey()) == -1) {
							     rr=binarySearchInsertion(pagee.getClustringKeys(),r.clustringKey);
						     } else { 
							     throw new DBAppException("The clustring Key Already exists!");
						     }
						     if(!(pagee.isFull())) {
							     emptyexists=true;
							     pagee.records.add(rr,temporary);
							     pagee.updateMinMaxCurrent();
							     releasePage(table.pages.get(pageindex),pagee);
							     break;
						     }
						     pagee.records.add(rr,temporary);
						     temporary= pagee.records.get(pagee.maximumRowsCountinPage);
						     pagee.records.remove(pagee.maximumRowsCountinPage);
						     pagee.updateMinMaxCurrent();
						     releasePage(table.pages.get(pageindex),pagee);
					     }
					     if(emptyexists==false) {
						     Vector<Record> finalrecords=new Vector<Record>();
						     finalrecords.add(temporary);
						     Page finalone=new Page(strTableName,clskey,table.pages.size()+1,finalrecords);
						     table.pages.add(pageindex,"src/resources/"+strTableName+"/"+finalone.tableName+"_"+pageindex+".ser");
						     FileOutputStream tableOut = new FileOutputStream(table.pages.get(pageindex));
						     ObjectOutputStream outable = new ObjectOutputStream(tableOut);
							 outable.writeObject(finalone);
							 outable.close();
							 tableOut.close();
					     }
				     }
				     else {
				    	 releasePage(table.pages.get(pageindex),page);
				     }
	             }
			}
			File f=new File("src/resources/"+strTableName+"/"+strTableName+".ser"); 
			f.delete();
			FileOutputStream tableOut = new FileOutputStream("src/resources/"+strTableName+"/"+strTableName+".ser");
			ObjectOutputStream outable = new ObjectOutputStream(tableOut);
			outable.writeObject(table);
			outable.close();
			tableOut.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
		
	public static Page getPage(String filepath) throws IOException, ClassNotFoundException {
		 Page page=null;
	     FileInputStream filin = new FileInputStream(filepath);
	     ObjectInputStream objin = new ObjectInputStream(filin);
	     page = (Page) objin.readObject();
	     objin.close();
	     filin.close();
	     return page;
	}
	
	public static void releasePage(String filepath,Page page) throws IOException {
		File f=new File(filepath); 
		f.delete();
		FileOutputStream tableOut = new FileOutputStream(filepath);
		ObjectOutputStream outable = new ObjectOutputStream(tableOut);
		outable.writeObject(page);
		outable.close();
		tableOut.close();
	}	
		
	public static int binarySearch(Vector<Object> arr, Object x) {
		int l = 0, r = arr.size() - 1;
		// int m = 0;
		while (l <= r) {
			int m = l + (r - l) / 2;

			// Check if x is present at mid
			if ((check(x,arr.get(m))==0))
				return m;

			// If x greater, ignore left half
			if (check(x,arr.get(m)) > 0)
				l = m + 1;

			// If x is smaller, ignore right half
			else
				r = m - 1;
		}

		// if we reach here, then element was not present
		return -1;
	}
	
	public static int binarySearchInsertion(Vector<Object> arr, Object x) {
		int l = 0, r = arr.size() - 1;
		// int m = 0;
		while (l <= r) {
			int m = l + (r - l) / 2;

			// Check if x is present at mid
			if (check(x,arr.get(m))==0)
				return m;

			// If x greater, ignore left half
			if (check(x,arr.get(m)) > 0)
				l = m + 1;

			// If x is smaller, ignore right half
			else
				r = m - 1;
		}

		// if we reach here, then element was not present
		return l;
	}
	
	public static int binarysearchpage(Vector<String> pages,Object clusteringkey ) throws ClassNotFoundException, IOException {
		int l=0;
		int h=pages.size()-1;
		while(l<= h) {
			int sum=l+h;
			int middle= sum / 2;
			Page page= getPage(pages.get(middle));
			Object lc=page.records.get(0).getClustringKey();
			Object hc=page.records.get(page.records.size() - 1).getClustringKey();

			if( (check(clusteringkey,lc) >= 0) && (check(clusteringkey,hc) <= 0)) {
				return middle;
			}
			if(check(clusteringkey,hc) >= 0 
					&& (!(page.isFull()))) {
				return middle;
			}
			if(check(clusteringkey,lc) <= 0 
					&& (!(page.isFull()))) {
				return middle;
			} 
			if (check(clusteringkey,lc) <= 0 ) {
				h=middle-1;
			}
			if (check(clusteringkey,hc) >= 0 ) {
				l=middle+1;
			}
	}
		return l;
	}
	
	public static int check(Object o1,Object o2) {
		if(o1 instanceof Integer && o2 instanceof Integer ) {
			if((int)o1>(int)o2) {
				return 1;
			}
			else if ((int)o1<(int)o2) {
				return -1;
			}
			else {
				return 0;
			}
		}
		else if(o1 instanceof String && o2 instanceof String) {
			return ((String)o1).compareTo((String)o2);
		}
		else if(o1 instanceof Double && o2 instanceof Double) {
			if((Double)o1>(Double)o2) {
				return 1;
			}
			else if ((Double)o1<(Double)o2){
				return -1;
			}
			else {
				return 0;
			}
		}
		else if(o1 instanceof Date && o2 instanceof Date) {
			return ((Date)o1).compareTo((Date)o2);
		}  
		return 1;
	}
	
	public void updateTable (String strTableName, String strClusteringKeyValue,
            Hashtable < String, Object > htblColNameValue) throws DBAppException{
		try {
            validateUpdate(strTableName,htblColNameValue);
            Table table=null;
            String tablePath = "src/resources/"+strTableName+"/"+ strTableName + ".ser";
            FileInputStream filein= new FileInputStream(tablePath);
            ObjectInputStream in = new ObjectInputStream(filein);
            table = (Table) in.readObject();
            String cskeyname=checkName(strTableName,htblColNameValue);
            Object cskeyvalue=null;
            String type=checkType(strTableName,htblColNameValue);
            if(type.equals("java.lang.Integer")) {
            	cskeyvalue = Integer.parseInt(strClusteringKeyValue);
            }
            else if(type.equals("java.lang.String")) {
            	cskeyvalue = strClusteringKeyValue;
            }
            else if(type.equals("java.lang.Double")) {
            	cskeyvalue= Double.parseDouble(strClusteringKeyValue);
            }
            else {
            	Date date = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(strClusteringKeyValue);
            	cskeyvalue=date;
            }
            htblColNameValue.put(cskeyname, cskeyvalue);
            int pageindex=binarysearchpage(table.pages,cskeyvalue);
            if(pageindex==table.pages.size()) {
            //	System.out.print("3aaa");
            	throw new DBAppException("The clustring Key does not exist");
            }
            Page page = getPage(table.pages.get(pageindex));
            int recordindex=binarySearchRecords(page.getClustringKeys(),cskeyvalue);
            if(recordindex == -1) {
            //	System.out.print("3aaa");
            	throw new DBAppException("The clustring Key does not exist");
            }
            Record record=new Record(strTableName,htblColNameValue,cskeyname);
            page.records.setElementAt(record,recordindex);
            releasePage(table.pages.get(pageindex),page);
            File f=new File(tablePath); 
			f.delete();
			FileOutputStream tableOut = new FileOutputStream(tablePath);
			ObjectOutputStream outable = new ObjectOutputStream(tableOut);
			outable.writeObject(table);
			outable.close();
			tableOut.close();
            
		}
		catch(Exception e){
			e.printStackTrace();
		}
    }
		
	public String checkType (String TableName,Hashtable < String, Object > htblColNameValue) throws IOException {
		String type=null;
		FileReader reader=new FileReader("src/resources/metadata.csv");
		BufferedReader buff=new BufferedReader(reader);
		String line=buff.readLine();
	    while(!(line==null)) {
	    	String arr[]=line.split(",");
	    	if(arr[0].equals(TableName)) {
	    		if(htblColNameValue.containsKey(arr[1])) {	
	    		}
	    		else if(arr[3].equals("TRUE")){
	    			type=arr[2];
	    			break;
	    		}
	    	}
	    	line=buff.readLine();
	    }
		return type;
	} 
	
	public String checkName (String TableName,Hashtable < String, Object > htblColNameValue) throws IOException {
		String name=null;
		FileReader reader=new FileReader("src/resources/metadata.csv");
		BufferedReader buff=new BufferedReader(reader);
		String line=buff.readLine();
	    while(!(line==null)) {
	    	String arr[]=line.split(",");
	    	if(arr[0].equals(TableName)) {
	    		if(htblColNameValue.containsKey(arr[1])) {
	    		}
	    		else if(arr[3].equals("TRUE")){
	    			name=arr[1];
	    			break;
	    		}
	    	}
	    	line=buff.readLine();
	    }
		return name;
	} 
	
	public void validateUpdate(String strTableName, Hashtable<String,Object> htblColNameValue) throws IOException, DBAppException, ParseException {
		String file= "src/resources/metadata.csv";
		FileReader reader=new FileReader(file);
		BufferedReader buff=new BufferedReader(reader);
		String line=buff.readLine();
		boolean flag1=false;
		while(!(line==null)) {
			String arr[]=line.split(",");
			if(strTableName.equals(arr[0])) {
				flag1=true;
				break;
			}
			line=buff.readLine();
		}
		if(flag1==false) {
			throw new DBAppException("Table "+strTableName+" does not exist");
		}
		for(String colname : htblColNameValue.keySet()) {
			FileReader reader2=new FileReader(file);
			BufferedReader buff2=new BufferedReader(reader2);
			String line2=buff2.readLine();
			boolean flag2=false;
	    	while(!(line2==null)) {
	    		String arr[]=line2.split(",");
	    		if(strTableName.equals(arr[0]) && colname.equals(arr[1])) {
	    			flag2=true;
	    			if(arr[2].equals("java.lang.Integer")) {
	    				if(!(htblColNameValue.get(colname) instanceof Integer)) {
	    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type Integer");
	    				}	
	    				if(((int)htblColNameValue.get(colname))<Integer.parseInt(arr[6])
	    				|| ((int)htblColNameValue.get(colname))>Integer.parseInt(arr[7])) {
	    				    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
	    				}
	    			}
	    			else if(arr[2].equals("java.lang.String")){
	    				if(!(htblColNameValue.get(colname) instanceof String)) {
	    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type String");
	    				}	
                        if(((String)htblColNameValue.get(colname)).compareTo(arr[6])<0 
                        || ((String)htblColNameValue.get(colname)).compareTo(arr[7])>0)  {
                    	    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
	    				}
	    			}
	    			else if(arr[2].equals("java.lang.Double")) {
	    				if(!(htblColNameValue.get(colname) instanceof Double)) {
	    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type Double");
	    				}	
	    				if(((Double)htblColNameValue.get(colname))<Double.parseDouble(arr[6])
			    		|| ((Double)htblColNameValue.get(colname))>Double.parseDouble(arr[7])) {
	    				    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
			   			}
	    			}
	    			else if (arr[2].equals("java.util.Date")) {
	    				if(!(htblColNameValue.get(colname) instanceof Date)) {
	    					throw new DBAppException(htblColNameValue.get(colname)+ "is not of type Date");
	    				}
	    				Date min = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(arr[6]);
	    				Date max = (Date) new SimpleDateFormat("yyyy-MM-dd").parse(arr[7]);
	    				if((((Date)htblColNameValue.get(colname)).compareTo(min)<0)
					   	|| (((Date)htblColNameValue.get(colname)).compareTo(max)>0)) {
			    		    throw new DBAppException("Value "+htblColNameValue.get(colname)+" is out of bounds");
						}
	    			} 
	    			break;
	    		}
	    		line2=buff2.readLine();
	    	}
	    	if(flag2==false) {
	    		throw new DBAppException("column "+colname+" does not exist in Table");
	    	}
	    }
    }
	
	public static int binarySearchRecords(Vector<Object> arr, Object strClusteringKeyValue) {
        int last = arr.size() - 1;
        int first = 0;
        int mid = (last + first) / 2;
        Object ck = arr.get(mid);
        while (first <= last) {
            if (check(strClusteringKeyValue, ck) == 0) {
                return mid;
            } else if (check(strClusteringKeyValue, ck) < 0) {
                last = mid - 1;
            } else {
                first = mid + 1;
            }
            mid = (last + first) / 2;
            ck = arr.get(mid);
        }
        return -1;
    }

	public Boolean[] validateInput(String strTableName, Hashtable<String, Object> htblColNameValue)
			throws DBAppException {
		String type = "";
		String primaryKey = "";
		String max = "";
		String min = "";
		String key;
		String firstKey = null;
		Object firstKeyValue = null;
		Boolean[] result = new Boolean[2];
		result[0] = false;
		result[1] = false;
		// checking if tablename exists
		try {
			FileReader currentFile = new FileReader("src/resources/metadata.csv");
			BufferedReader br = new BufferedReader(currentFile);
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			Boolean Cluster;
			Iterator<String> iterator = htblColNameValue.keySet().iterator();
			if (iterator.hasNext()) {
				firstKey = iterator.next();
				firstKeyValue = htblColNameValue.get(firstKey);
			}

			while ((line = br.readLine()) != null) {
				String arr[] = line.split(",");
				if (arr[0].equals(strTableName)) {
					type = arr[2];
					max = arr[7];
					min = arr[6];
					if (arr[1].equals(firstKey) && arr[3].equals("TRUE")) {

						if (type.equals("java.lang.Integer") && firstKeyValue instanceof Integer) {

							if ((int) firstKeyValue < Integer.parseInt(min)
									|| (int) firstKeyValue > Integer.parseInt(max)) {
								result[0] = false;
								result[1] = false;
							} else {

								result[0] = true;
								result[1] = true;
							}

						} else if (type.equals("java.lang.Double") && firstKeyValue instanceof Double) {

							if ((double) firstKeyValue < Double.parseDouble(min)
									|| (double) firstKeyValue > Double.parseDouble(max)) {
								result[0] = false;
								result[1] = false;
							} else {

								result[0] = true;
								result[1] = true;
							}
						} else if (type.equals("java.lang.String") && firstKeyValue instanceof String) {

							if (((String) firstKeyValue).compareTo(min) < 0
									|| max.compareTo((String) firstKeyValue) < 0) {
								result[0] = false;
								result[1] = false;
							} else {

								result[0] = true;
								result[1] = true;
							}

						}

					} // validating cluster
					else if (arr[1].equals(firstKey) && arr[3].equals("FALSE")) {

						if (type.equals("java.lang.Integer") && firstKeyValue instanceof Integer) {

							if ((int) firstKeyValue < Integer.parseInt(min)
									|| (int) firstKeyValue > Integer.parseInt(max)) {
								result[0] = false;
								result[1] = false;
							} else {

								result[0] = false;
								result[1] = true;
							}
						} else if (type.equals("java.lang.Double") && firstKeyValue instanceof Double) {

							if ((double) firstKeyValue < Double.parseDouble(min)
									|| (double) firstKeyValue > Double.parseDouble(max)) {
								result[0] = false;
								result[1] = false;
							} else {

								result[0] = false;
								result[1] = true;
							}
						} else if (type.equals("java.lang.String") && firstKeyValue instanceof String) {

							if (((String) firstKeyValue).compareTo(min) < 0
									|| max.compareTo((String) firstKeyValue) < 0) {
								result[0] = false;
								result[1] = false;
							} else {

								result[0] = false;
								result[1] = true;
							}

						} else if (type.equals("java.lang.Date") && firstKeyValue instanceof Date)

						{
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							try {
								Date datemin = dateFormat.parse(min);
								Date datemax = dateFormat.parse(max);
								if (((Date) firstKeyValue).compareTo(datemin) < 0
										|| ((Date) firstKeyValue).compareTo(datemax) > 0) {
									result[0] = false;
									result[1] = false;
								} else {
									result[0] = false;
									result[1] = true;
								}
							} catch (Exception e) {

							}
						}

					}

				} // ending table found
			}
		} catch (IOException ignored) {
		}
		return result;
	}

	public void handellingPathNameAfterDeleting(int i, Vector<String> currentPages, String tableName) {
		
		for (int j = i + 1; j < currentPages.size(); j++) {
			int r = j - 1;
			String s = "src/resources/" + tableName +"/"+tableName+ "_" + r + ".ser";
			currentPages.set(j, s);
		}
	}

	public void deleteFromTable(String strTableName, Hashtable<String, Object> htblColNameValue)
			throws DBAppException, IOException {

		try {
			Hashtable<String, Object> onlyOne = new Hashtable<String, Object>();
			for (String ColName : htblColNameValue.keySet()) {
				Object x = htblColNameValue.get(ColName);
				onlyOne.put(ColName, x);
				Boolean[] myResult = validateInput(strTableName, onlyOne);
				Boolean firstKey = myResult[0];
				Boolean Value = myResult[1];
				System.out.println(firstKey);
				System.out.println(Value);
				if (!firstKey && !Value) {
					System.out.print("tuple with " + ColName + " and value " + x
							+ "  couldn't be deleted due to  invalid data entry");}
				 else if (!firstKey && Value) {
					System.out.println("linear");
					System.out.println("was here");
					Table t = null;
					FileInputStream filein = new FileInputStream("src/resources/" + strTableName +"/" +strTableName+".ser");
					ObjectInputStream in = new ObjectInputStream(filein);
					t = (Table) in.readObject();
					in.close();
					filein.close();
				
                   if(t.pages.size()==0)
                   {
                	   System.out.println("The table is already empty");
                	   break;
                   }
					for (int z = 0; z < t.pages.size(); z++) {
						String zz = t.pages.get(z);

					}

					for (int i = 0; i <t.pages.size(); i++) {

						Page p = getPage(t.pages.get(i));

						
						for (int j = 0; j <p.getRecords().size(); j++) {

							Hashtable<String, Object> currentRecord = p.getRecords().get(j).colNameValue;

							int checkValue = check(x, currentRecord);

							if (checkValue == 1) {
								p.getRecords().remove(j);
								if (p.records.size() == 0) {
									File f = new File(t.pages.get(i));
									f.delete();
									t.pages.remove(i);
									p.pageNum--;
									handellingPathNameAfterDeleting(i, t.pages, strTableName);
								} else {
									releasePage(t.pages.get(i), p);
								}
							}
							
						}
						File f=new File("src/resources/"+strTableName+"/"+strTableName+".ser"); 
						f.delete();
						FileOutputStream tableOut = new FileOutputStream("src/resources/"+strTableName+"/"+strTableName+".ser");
						ObjectOutputStream outable = new ObjectOutputStream(tableOut);
						outable.writeObject(t);
						outable.close();
						tableOut.close();
					}

				}

				else {
					System.out.println("binary isa");
					// binary search using clusterKey
					// reading table
					Table t = null;
					FileInputStream filein = new FileInputStream("src/resources/" + strTableName +"/"+ strTableName+".ser");
					ObjectInputStream in = new ObjectInputStream(filein);
					t = (Table) in.readObject();
					in.close();
					filein.close();
					
					 if(t.pages.size()==0)
	                   {
	                	   System.out.println("The table is already empty");
	                	   break;
	                   }
					int i = binarysearchpage(t.pages, x);
					System.out.println(i);
					
					Page p = getPage(t.pages.get(i));
					
					Vector<Object> allCluster = p.getClustringKeys();
					int j = binarySearch(allCluster, x);
                    System.out.println(j);
					p.getRecords().remove(j);
					if (p.records.size() == 0) {
						File f = new File(t.pages.get(i));
						f.delete();
						t.pages.remove(i);
						p.pageNum--;
						handellingPathNameAfterDeleting(i, t.pages, strTableName);
						
					} else {
						releasePage(t.pages.get(i), p);
					}
					File f=new File("src/resources/"+strTableName+"/"+strTableName+".ser"); 
					f.delete();
					FileOutputStream tableOut = new FileOutputStream("src/resources/"+strTableName+"/"+strTableName+".ser");
					ObjectOutputStream outable = new ObjectOutputStream(tableOut);
					outable.writeObject(t);
					outable.close();
					tableOut.close();

				}
				
			}
		} catch (Exception e) {
			System.out.println("An error occurred while deleting the record: ");
		}
	}
	

	}