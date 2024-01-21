public class OctPoint {

    private Object x;
    private Object y;
    private Object z;

    private boolean nullify = false;
   

    public OctPoint(Object x, Object y, Object z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public OctPoint(){
        nullify = true;
    }

    public Object getX(){
        return x;
    }

    public Object getY(){
        return y;
    }

    public Object getZ(){
        return z;
    }

    public boolean isNullified(){
        return nullify;
    }
}
