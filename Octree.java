import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector; 
public class Octree<T> {

    private Bucket Bucket; 

    private Octree<T>[] children = new Octree[8];

    private T object;
    private String coloumn1 ;
    private String coloumn2 ;
    private String coloumn3 ;
    
    public Octree(Object x1,Object y1, Object z1, Object x2,Object y2,Object z2, String str1, String str2, String  str3) throws DBAppException{
        Bucket = new Bucket ( x1, y1,  z1,  x2, y2, z2) ; 
        this.coloumn1= str1 ;
        this.coloumn2= str2 ;
        this.coloumn3= str3 ;
   }
    public void insert(Object x, Object y, Object z, String s, int recnum) throws OutOfBoundsException, UnsupportedEncodingException, ParseException, DBAppException {
//        if(find(x, y, z)){
//            throw new PointExistsException("Point already exists in the tree. X: " + x + " Y: " + y + " Z: " + z + " Object Name: " + object.getClass().getName());
//        }

        if ( ((((Comparable)x).compareTo((Comparable)Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)Bucket.getMaxx())) > 0)
                ||  ((((Comparable)y).compareTo((Comparable)Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)Bucket.getMaxy())) > 0)
                ||  ((((Comparable)z).compareTo((Comparable)Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)Bucket.getMaxz())) > 0))
            throw new OutOfBoundsException("Insertion point is out of bounds! X: " + x + " Y: " + y + " Z: " + z + " Object Name: " + object.getClass().getName());
        Object midOfx ; 
        Object midOfy; 
        Object midOfz ; 
        switch (x.getClass().getName()) {
 
        case "java.lang.String" :
        	 midOfx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
             break;
        case "java.lang.Double" :
        	  midOfx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
        	  break;
        case "java.lang.Integer" :
        	 midOfx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
        	 break;
        default : 
        	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
        	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
            long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
            Date midwayDate = new Date(midwayTime);
        	midOfx = midwayDate ; 
        	 break;
        	
        }
        switch (y.getClass().getName()) {
        case "java.lang.String" :
        	 midOfy =  getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
        	 break;
        case "java.lang.Double" :
        	  midOfy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
        	  break;
        case "java.lang.Integer" :
        	 midOfy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
        	 break;
        default : 
        	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
        	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
            long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
            Date midwayDate = new Date(midwayTime);
        	midOfy = midwayDate ; 
        	 break;
        	
        }
        switch (z.getClass().getName()) {
        case "java.lang.String" :
        	 midOfz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
        	 break;
        case "java.lang.Double" :
        	  midOfz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
        	  break;
        case "java.lang.Integer" :
        	 midOfz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
        	 break;
        default : 
        	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
        	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
            long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
            Date midwayDate = new Date(midwayTime);
        	midOfz = midwayDate ; 
        	 break;
        }
        
        if (this.children[0] == null) {
        	if (Bucket.getCount()<=Bucket.getSize()) { 
       		 // reference to record
       		
       		Vector <OctPoint> v = this.Bucket.getLocations() ;
       		OctPoint t = new OctPoint (x,y,z) ;
       		if(v.contains(t)) 
       		{
       			int index = v.indexOf(t);
       		    Bucket.getDuplicates().get(index).add(s+"_"+recnum);
       		}
       		else {
       		this.Bucket.setCount(this.Bucket.getCount()+1); 
       		Bucket.getBucket().add(s) ;
       		Bucket.getRecnums().add(recnum) ;
       		v.add(t) ;
       		this.Bucket.setLocations(v) ;} 
       	}
       	else {
        
                  children[0] = new Octree((midOfx), (midOfy), (midOfz), this.Bucket.getMaxx(),this.Bucket.getMaxy(),this.Bucket.getMaxz(),this.coloumn1,this.coloumn2,this.coloumn3);
                  children[1] = new Octree((midOfx), (midOfy), Bucket.getMinz(), this.Bucket.getMaxx(),this.Bucket.getMaxy(),midOfz,this.coloumn1,this.coloumn2,this.coloumn3);
                  children[2] = new Octree((midOfx),Bucket.getMiny(),(midOfz),this.Bucket.getMaxx(),midOfy,this.Bucket.getMaxz(),this.coloumn1,this.coloumn2,this.coloumn3);
                  children[3] = new Octree((midOfx),Bucket.getMiny(),Bucket.getMinz(),this.Bucket.getMaxx(),midOfy,midOfz,this.coloumn1,this.coloumn2,this.coloumn3);
                  children[4] = new Octree(Bucket.getMinx(),(midOfy),(midOfz),midOfx, Bucket.getMaxy(),Bucket.getMaxz(),this.coloumn1,this.coloumn2,this.coloumn3);
                  children[5] = new Octree(Bucket.getMinx(),(midOfy),Bucket.getMinz(),midOfx,Bucket.getMaxy(),midOfz,this.coloumn1,this.coloumn2,this.coloumn3);
                  children[6] = new Octree(Bucket.getMinx(),Bucket.getMiny(),(midOfz),midOfx,midOfy,Bucket.getMaxz(),this.coloumn1,this.coloumn2,this.coloumn3);
                  children[7] = new Octree(Bucket.getMinx(),Bucket.getMiny(),Bucket.getMinz(),midOfx,midOfy,midOfz,this.coloumn1,this.coloumn2,this.coloumn3);
                  for (int i = 0  ; i< this.Bucket.getCount() ; i ++) {
               	   String s2 = this.Bucket.getBucket().get(i) ; 
               	   int i2= this.Bucket.getRecnums().get(i) ; 
               	   OctPoint t2 = this.Bucket.getLocations().get(i) ; 
               	   int octant = getOctant(t2,midOfx,midOfy,midOfz) ; 
               	   this.children[octant].insert(t2.getX(), t2.getY(), t2.getZ(), s2, i2);
                  }
                  this.Bucket = null ; 
                  
               
                  OctPoint t3 = new OctPoint (x,y,z) ; 
              	   int octant2 = getOctant(t3,midOfx,midOfy,midOfz) ; 
                  this.children[octant2].insert(x, y, z, s,recnum);
       	}
        }
        else {
        OctPoint t3 = new OctPoint (x,y,z) ; 
        int octant2 = getOctant(t3,midOfx,midOfy,midOfz) ; 
        this.children[octant2].insert(x, y, z, s,recnum); }
            
      
    }
    public Bucket getBucket() {
		return Bucket;
	}
	public void setBucket(Bucket bucket) {
		Bucket = bucket;
	}
	public Octree<T>[] getChildren() {
		return children;
	}
	public void setChildren(Octree<T>[] children) {
		this.children = children;
	}
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}
	public String getColoumn1() {
		return coloumn1;
	}
	public void setColoumn1(String coloumn1) {
		this.coloumn1 = coloumn1;
	}
	public String getColoumn2() {
		return coloumn2;
	}
	public void setColoumn2(String coloumn2) {
		this.coloumn2 = coloumn2;
	}
	public String getColoumn3() {
		return coloumn3;
	}
	public void setColoumn3(String coloumn3) {
		this.coloumn3 = coloumn3;
	}
	public boolean find(Object x, Object y, Object z) throws UnsupportedEncodingException, ParseException{
    	  if( ((((Comparable)x).compareTo((Comparable)this.Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)this.Bucket.getMaxx())) > 0)
    	          ||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMaxy())) > 0)
    	          ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMaxz())) > 0))
    	  		return false;
    	  Object midx ; 
    	  Object midy; 
    	  Object midz ; 

    	  switch (x.getClass().getName()) {
    	  
    	  case "java.lang.String" :
    		  midx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
    	       break;
    	  case "java.lang.Double" :
    	  	  midx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
    	  	  break;
    	  case "java.lang.Integer" :
    	  	 midx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
    	  	 break;
    	  default : 
    		  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
    	  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    	      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    	      Date midwayDate = new Date(midwayTime);
    	  	midx = midwayDate ; 
    	  	 break;
    	  	
    	  }
    	  switch (y.getClass().getName()) {
    	  case "java.lang.String" :
    		  midy = getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
    	  	 break;
    	  case "java.lang.Double" :
    	  	  midy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
    	  	  break;
    	  case "java.lang.Integer" :
    	  	 midy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
    	  	 break;
    	  default : 
    		  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
    	  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    	      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    	      Date midwayDate = new Date(midwayTime);
    	  	midy = midwayDate ; 
    	  	 break;
    	  	
    	  }
    	  switch (z.getClass().getName()) {
    	  case "java.lang.String" :
    		  midz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
    	  	 break;
    	  case "java.lang.Double" :
    	  	  midz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
    	  	  break;
    	  case "java.lang.Integer" :
    	  	 midz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
    	  	 break;
    	  default : 
    		  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
    	  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    	      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    	      Date midwayDate = new Date(midwayTime);
    	  	midz = midwayDate ; 
    	  	 break;
    	  }
    	  
    	  OctPoint oct = new OctPoint (x,y,z) ;  
    	  int pos = getOctant(oct,midx,midy,midz);
    	Boolean b = false;

    	  if(Bucket == null && children[0] != null) 
    	      return children[pos].find(x, y, z);
    	  if(children[pos].Bucket.getCount()==0)
    	      return false;
    	  for (int i = 0 ; i < children[pos].Bucket.getCount() ; i ++ ) {
    		  b = x == children[pos].Bucket.getLocations().get(i).getX()&& y == children[pos].Bucket.getLocations().get(i).getY() && z ==children[pos].Bucket.getLocations().get(i).getZ();
    		  if (b) 
    			  break ;
    	  }
    	  return b ;
    	}
	public boolean find2(Object x, Object y, Object z, String filepath, int recnum) throws UnsupportedEncodingException, ParseException{
  if( ((((Comparable)x).compareTo((Comparable)this.Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)this.Bucket.getMaxx())) > 0)
          ||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMaxy())) > 0)
          ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMaxz())) > 0))
  		return false;
  Object midx ; 
  Object midy; 
  Object midz ; 

  switch (x.getClass().getName()) {
  
  case "java.lang.String" :
	  midx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
       break;
  case "java.lang.Double" :
  	  midx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
  	  break;
  case "java.lang.Integer" :
  	 midx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midx = midwayDate ; 
  	 break;
  	
  }
  switch (y.getClass().getName()) {
  case "java.lang.String" :
	  midy = getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
  case "java.lang.Double" :
  	  midy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
  	  break;
  case "java.lang.Integer" :
  	 midy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midy = midwayDate ; 
  	 break;
  	
  }
  switch (z.getClass().getName()) {
  case "java.lang.String" :
	  midz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
  	 break;
  case "java.lang.Double" :
  	  midz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
  	  break;
  case "java.lang.Integer" :
  	 midz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midz = midwayDate ; 
  	 break;
  }
  
  OctPoint oct = new OctPoint (x,y,z) ;  
  int pos = getOctant(oct,midx,midy,midz);
Boolean b = false;

  if(Bucket == null && children[0] != null) 
      return children[pos].find2(x, y, z,  filepath, recnum);
  if(children[pos].Bucket.getCount()==0)
      return false;
  for (int i = 0 ; i < children[pos].Bucket.getCount() ; i ++ ) {
	  b = filepath.equals(children[pos].Bucket.getBucket().get(i)) && recnum==children[pos].Bucket.getRecnums().get(i) &&  x == children[pos].Bucket.getLocations().get(i).getX()&& y == children[pos].Bucket.getLocations().get(i).getY() && z ==children[pos].Bucket.getLocations().get(i).getZ();
	  if (b) 
		  break ;
  }
  return b ;
}

public String get(Object x, Object y, Object z) throws UnsupportedEncodingException, ParseException{
	if( ((((Comparable)x).compareTo((Comparable)this.Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)this.Bucket.getMaxx())) > 0)
	          ||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMaxy())) > 0)
	          ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMaxz())) > 0))
	  		return null;
  Object midx ; 
  Object midy; 
  Object midz ; 
  switch (x.getClass().getName()) {
	  
  case "java.lang.String" :
	  midx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
       break;
  case "java.lang.Double" :
  	  midx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
  	  break;
  case "java.lang.Integer" :
  	 midx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midx = midwayDate ; 
  	 break;
  	
  }
  switch (y.getClass().getName()) {
  case "java.lang.String" :
  	  midy = getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
  	 break;
  case "java.lang.Double" :
  	  midy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
  	  break;
  case "java.lang.Integer" :
  	 midy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midy = midwayDate ; 
  	 break;
  	
  }
  switch (z.getClass().getName()) {
  case "java.lang.String" :
	  midz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
  	 break;
  case "java.lang.Double" :
  	  midz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
  	  break;
  case "java.lang.Integer" :
  	 midz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midz = midwayDate ; 
  	 break;
  }
 
  OctPoint oct = new OctPoint (x,y,z) ;  
  int pos = getOctant(oct,midx,midy,midz);
  Boolean b = false;

  if(Bucket == null && children[0] != null)
      return children[pos].get(x, y, z);
  if(children[pos].Bucket.getCount()==0)
      return null;
for (int i = 0 ; i < children[pos].Bucket.getCount() ; i ++ ) {
  b = x == children[pos].Bucket.getLocations().get(i).getX()&& y == children[pos].Bucket.getLocations().get(i).getY() && z ==children[pos].Bucket.getLocations().get(i).getZ();
  if (b) 
	  return children[pos].Bucket.getBucket().get(i) + "" + children[pos].Bucket.getRecnums().get(i);
}
  return null;
}
public String get2(Object x, Object y, Object z, String filepath, int recnum) throws UnsupportedEncodingException, ParseException{
	if( ((((Comparable)x).compareTo((Comparable)this.Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)this.Bucket.getMaxx())) > 0)
	          ||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMaxy())) > 0)
	          ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMaxz())) > 0))
	  		return null;
  Object midx ; 
  Object midy; 
  Object midz ; 
  switch (x.getClass().getName()) {
	  
  case "java.lang.String" :
	  midx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
       break;
  case "java.lang.Double" :
  	  midx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
  	  break;
  case "java.lang.Integer" :
  	 midx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midx = midwayDate ; 
  	 break;
  	
  }
  switch (y.getClass().getName()) {
  case "java.lang.String" :
	  midy = getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
  	 break;
  case "java.lang.Double" :
  	  midy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
  	  break;
  case "java.lang.Integer" :
  	 midy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midy = midwayDate ; 
  	 break;
  	
  }
  switch (z.getClass().getName()) {
  case "java.lang.String" :
	  midz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
  	 break;
  case "java.lang.Double" :
  	  midz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
  	  break;
  case "java.lang.Integer" :
  	 midz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
  	 break;
  default : 
	  Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
  	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
      long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
      Date midwayDate = new Date(midwayTime);
  	midz = midwayDate ; 
  	 break;
  }
 
  OctPoint oct = new OctPoint (x,y,z) ;  
  int pos = getOctant(oct,midx,midy,midz);
  Boolean b = false;

  if(Bucket == null && children[0] != null)
      return children[pos].get(x, y, z);
  if(children[pos].Bucket.getCount()==0)
      return null;
for (int i = 0 ; i < children[pos].Bucket.getCount() ; i ++ ) {
  b = filepath.equals(children[pos].Bucket.getBucket().get(i)) && recnum==children[pos].Bucket.getRecnums().get(i) && x == children[pos].Bucket.getLocations().get(i).getX()&& y == children[pos].Bucket.getLocations().get(i).getY() && z ==children[pos].Bucket.getLocations().get(i).getZ();
  if (b) 
	  return children[pos].Bucket.getBucket().get(i) + "@" + children[pos].Bucket.getRecnums().get(i);
}
  return null;
}

public boolean remove(Object x, Object y, Object z) throws UnsupportedEncodingException, ParseException{
	if( ((((Comparable)x).compareTo((Comparable)this.Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)this.Bucket.getMaxx())) > 0)
	          ||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMaxy())) > 0)
	          ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMaxz())) > 0))
	  		return false;
Object midx ; 
Object midy; 
Object midz ; 
switch (x.getClass().getName()) {
	  
case "java.lang.String" :
	 midx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
     break;
case "java.lang.Double" :
	  midx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
	  break;
case "java.lang.Integer" :
	 midx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
	 break;
default : 
	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    Date midwayDate = new Date(midwayTime);
	midx = midwayDate ; 
	 break;
}
switch (y.getClass().getName()) {
case "java.lang.String" :
	 midy = getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
	 break;
case "java.lang.Double" :
	  midy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
	  break;
case "java.lang.Integer" :
	 midy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
	 break;
default : 
	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    Date midwayDate = new Date(midwayTime);
	midy = midwayDate ; 
	 break;
}
switch (z.getClass().getName()) {
case "java.lang.String" :
	 midz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
	 break;
case "java.lang.Double" :
	  midz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
	  break;
case "java.lang.Integer" :
	 midz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
	 break;
default : 
	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    Date midwayDate = new Date(midwayTime);
	midz = midwayDate ; 
	 break;
}

OctPoint oct = new OctPoint (x,y,z) ;  
int pos = getOctant(oct,midx,midy,midz);
Boolean b = false;
  if(Bucket == null && children[0] != null)
      return children[pos].remove(x, y, z);
  if(children[pos].Bucket.getCount()==0)
      return false;
for (int i = 0 ; i < children[pos].Bucket.getCount() ; i ++ ) {
b = x == children[pos].Bucket.getLocations().get(i).getX()&& y == children[pos].Bucket.getLocations().get(i).getY() && z ==children[pos].Bucket.getLocations().get(i).getZ();
if (b) {
	children[pos].Bucket.getLocations().remove(i) ; 
	children[pos].Bucket.getRecnums().remove(i) ; 
	children[pos].Bucket.getBucket().remove(i) ; 
	children[pos].Bucket.setCount(children[pos].Bucket.getCount()-1);
			} } 
  return false;
}
public boolean remove2(Object x, Object y, Object z, String filepath, int recnum) throws UnsupportedEncodingException, ParseException{
	if( ((((Comparable)x).compareTo((Comparable)this.Bucket.getMinx())) < 0) || ((((Comparable)x).compareTo((Comparable)this.Bucket.getMaxx())) > 0)
	          ||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMiny())) < 0)||  ((((Comparable)y).compareTo((Comparable)this.Bucket.getMaxy())) > 0)
	          ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMinz())) < 0) ||  ((((Comparable)z).compareTo((Comparable)this.Bucket.getMaxz())) > 0))
	  		return false;
Object midx ; 
Object midy; 
Object midz ; 
switch (x.getClass().getName()) {
	  
case "java.lang.String" :
	 midx = getMiddleString(Bucket.getMaxx().toString(), Bucket.getMinx().toString());
     break;
case "java.lang.Double" :
	  midx = ((double)Bucket.getMaxx() + (double)Bucket.getMinx())/2;
	  break;
case "java.lang.Integer" :
	 midx = ((Integer)Bucket.getMaxx() + (Integer)Bucket.getMinx())/2;
	 break;
default : 
	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    Date midwayDate = new Date(midwayTime);
	midx = midwayDate ; 
	 break;
}
switch (y.getClass().getName()) {
case "java.lang.String" :
	 midy = getMiddleString(Bucket.getMaxy().toString(), Bucket.getMiny().toString());
	 break;
case "java.lang.Double" :
	  midy = ((double)Bucket.getMaxy() + (double)Bucket.getMiny())/2;
	  break;
case "java.lang.Integer" :
	 midy = ((Integer)Bucket.getMaxy() + (Integer)Bucket.getMiny())/2;
	 break;
default : 
	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    Date midwayDate = new Date(midwayTime);
	midy = midwayDate ; 
	 break;
}
switch (z.getClass().getName()) {
case "java.lang.String" :
	 midz = getMiddleString(Bucket.getMaxz().toString(), Bucket.getMinz().toString());
	 break;
case "java.lang.Double" :
	  midz = ((double)Bucket.getMaxz() + (double)Bucket.getMinz())/2;
	  break;
case "java.lang.Integer" :
	 midz = ((Integer)Bucket.getMaxz() + (Integer)Bucket.getMinz())/2;
	 break;
default : 
	Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMaxx().toString()) ; 
	Date enddate =  new SimpleDateFormat("yyyy-MM-dd").parse(Bucket.getMinx().toString()) ;
    long midwayTime = (startdate.getTime() + enddate.getTime()) / 2;
    Date midwayDate = new Date(midwayTime);
	midz = midwayDate ; 
	 break;
}

OctPoint oct = new OctPoint (x,y,z) ;  
int pos = getOctant(oct,midx,midy,midz);
Boolean b = false;
  if(Bucket == null && children[0] != null)
      return children[pos].remove(x, y, z);
  if(children[pos].Bucket.getCount()==0)
      return false;
for (int i = 0 ; i < children[pos].Bucket.getCount() ; i ++ ) {
b = filepath.equals(children[pos].Bucket.getBucket().get(i)) && recnum==children[pos].Bucket.getRecnums().get(i) && x == children[pos].Bucket.getLocations().get(i).getX()&& y == children[pos].Bucket.getLocations().get(i).getY() && z ==children[pos].Bucket.getLocations().get(i).getZ();
if (b) {
	children[pos].Bucket.getLocations().remove(i) ; 
	children[pos].Bucket.getRecnums().remove(i) ; 
	children[pos].Bucket.getBucket().remove(i) ; 
	children[pos].Bucket.setCount(children[pos].Bucket.getCount()-1);
			} } 
  return false;
}

        private static  int  getOctant (OctPoint p , Object midx, Object midy , Object midz ) {
        	int pos;

            if(((Comparable)p.getX()).compareTo(((Comparable)midx))<0){
                if(((Comparable)p.getY()).compareTo(((Comparable)midy))<0){
                    if(((Comparable)p.getZ()).compareTo(((Comparable)midz))<0)

                        pos = 7;
                    else
                        pos = 6;
                }else{
                    if(((Comparable)p.getZ()).compareTo(((Comparable)midz))<0)
                        pos = 5;
                    else
                        pos = 4;
                }
            }else{
                if(((Comparable)p.getY()).compareTo(((Comparable)midy))<0){
                    if(((Comparable)p.getZ()).compareTo(((Comparable)midz))<0)
                        pos = 3;
                    else
                        pos = 2;
                }else {
                    if(((Comparable)p.getZ()).compareTo(((Comparable)midz))<0)
                        pos = 1;
                    else
                        pos = 0;
                }
            }
            return pos ; 
        }
        public static String getMiddleString(String s, String t)
        {
            return numberToAlphaString((alphaStringToNumber(s)+alphaStringToNumber(t))/2);
        }

        public static long alphaStringToNumber(String str) {
            long n = 0;
            for (char elem : str.toCharArray()) {
                n *= 26;
                n += 1 + (elem - 'a');
            }
            return n;
        }

        public static String numberToAlphaString(long n) {
            StringBuilder r = new StringBuilder();
            while (n > 0) {
                r.append((char)('a' + ( --n % 26) ));
                n /= 26;
            }
            return r.reverse().toString();
        }
}