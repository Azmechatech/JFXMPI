/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mky.image;


/**
 *
 * @author PDI
 */
public class Line implements Cloneable{
//slope intercept form: y=mx+c
    public double y;
    public double m;
    public double x;
    public double c;
    public Double Distance=0.0;
    public double length;
    public boolean eqnSet=false;
    public boolean parallelToX=false;
    public double Y_const;
    public boolean parallelToY=false;
    public double X_const;
    public boolean m_infinite=false;
    public static Double NaN = new Double(Double.NaN);
    public boolean debug=false;
    
    @Override
protected Object clone() throws CloneNotSupportedException {
return super.clone();
}
/**
 *
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 */
public void setMXpC(double x1,double y1,double x2, double y2 ){
    m=(y1-y2)/(x1-x2);
    if(m==0){ if(debug)System.out.println("#parallelToX y= "+y1);parallelToX=true; Y_const=y1; }
    if((x1-x2)==0){if(debug)System.out.println("#parallelToY x= "+x1);parallelToY=true; X_const=x1;m_infinite=true;}
    c=y1-m*x1;
    eqnSet=true;
    length=Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    if(debug)System.out.println("# y= "+m+" x + "+c);
    if(debug)System.out.println("\t Len=" +length+"\t Slope Angle"+Math.toDegrees(Math.atan(m)));
}


public void setMXpC(Double[] x1y1,Double[] x2y2 ){
    setMXpC(x1y1[0],x1y1[1],x2y2[0],x2y2[1] );
    Distance=distanceBetween2Points(x1y1[0],x1y1[1],x2y2[0],x2y2[1]);
}
/**
 * 
 * @param x1
 * @param y1
 * @param m
 */
public void setMC(double x1,double y1,double m){
    this.m=m;this.x=x1;this.y=y1;
   // System.out.println("The Slope Angle"+Math.toDegrees(Math.atan(m)));
    if(Math.atan(m)==0){ System.out.println("#parallelToX y= "+y1);parallelToX=true; Y_const=y1; }
    if(Math.atan(m)==Math.PI/2){System.out.println("#parallelToY x= "+x1);parallelToY=true; X_const=x1;}
    c=y1-m*x1;
    eqnSet=true;
    //System.out.println("# y= "+this.m+" x + "+c);
}

public void setM(double m){this.m=m;}

public void setMC(double[] x1y1,double m){
    setMC(x1y1[0],x1y1[1], m);
}

/**
 * 
 * @param x1
 * @param y1
 * @param Theta  in rad
 */
public void setThetaC(double x1,double y1,double Theta){
    //this.m=m;
    //System.out.println("The Slope Angle"+Theta);
    if(Theta==0){ System.err.println("#parallelToX y= "+y1);parallelToX=true; Y_const=y1;  m=0;}else
    if(Theta==Math.PI/2){System.err.println("#parallelToY x= "+x1);parallelToY=true; X_const=x1;}else
        m=Math.tan(Theta);
    
    c=y1-m*x1;
    eqnSet=true;
    //System.out.println("# y= "+this.m+" x + "+c);
}

public void setThetaC(double[] x1y1,double m){
    setThetaC(x1y1[0],x1y1[1], m);
}
/**
 *
 * @param y
 * @return
 */
public double getX(double y){
    if(eqnSet){
        //if()
        
    if(parallelToY){ return X_const;}

        return (y-c)/m;
    }
    //System.out.println("Equation not set, call setMxpC()");
    return 0;
}
/**
 * 
 * @param x
 * @return
 */
public double getY(double x){
    if(eqnSet){
        if(parallelToX){return Y_const; }
        return m*x+c;
    }
    //System.out.println("Equation not set, call setMxpC()");
    return 0;
}
/**
 * Returns Y
 * @param m
 * @param x
 * @param c
 * @return
 */
public double getPointOnLine(double m,double x,double c){
    return m*x+c;
}
/**
 *
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @return
 */
public static double getLength(double x1,double y1,double x2, double y2){
        return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
}
/**
 * Gives 2 points, on both sides of the reference point.
 * @param x1
 * @param y1
 * @param atDistance
 * @return 
 */
public double[] getPointsOnLine(double x1,double y1,double atDistance){
    double result[]=new double[4];
    if(eqnSet){
        double a = (1 + m * m);
        double b = -2 * (x1 - c * m +y1 * m);
        double cons = x1*x1+(c - y1) * (c - y1) - atDistance* atDistance ;

        EqnSolver eqs = new EqnSolver();
        eqs.QuadSolv(a, b, cons);
        result[0] = eqs.bigRoot;
        result[1] = getY(result[0]);
        result[2] = eqs.smallRoot;
        result[3] = getY(result[2]);
        //System.out.println("Big Root Length"+getLength(x1,y1,result[0],result[1]));
        //System.out.println("Small Root Length"+getLength(x1,y1,result[2],result[3]));
    }
        return result;
}

public double[][] getPointsOnLine(double[] x1y1,double atDistance){
    double result[][]=new double[2][2];
    if(eqnSet & !parallelToX & !parallelToY){
        double a = (1 + m * m);
        double b = -2 * (x1y1[0] - c * m +x1y1[1] * m);
        double cons = x1y1[0]*x1y1[0]+(c - x1y1[1]) * (c - x1y1[1]) - atDistance* atDistance ;

        EqnSolver eqs = new EqnSolver();
        eqs.QuadSolv(a, b, cons);
        result[0][0] = eqs.bigRoot;
        result[0][1] = getY(result[0][0]);
        result[1][0] = eqs.smallRoot;
        result[1][1] = getY(result[1][0]);
       
    }
    
    if(parallelToX){
        result[0][0] = x1y1[0]+atDistance;
        result[0][1] = getY(x1y1[0]);
        result[1][0] = x1y1[0]-atDistance;
        result[1][1] = getY(x1y1[0]);
        
    }
    
    if(parallelToY){
        result[0][0] = getX(x1y1[1]); 
        result[0][1] = x1y1[1]+atDistance;
        result[1][0] = getX(x1y1[1]);
        result[1][1] = x1y1[1]-atDistance;
    }
    
        //System.out.println("result["+result[0][0]+","+result[0][1]+","+result[1][0]+","+result[1][1]+"]");
        //System.out.println("Big Root Length "+getLength(x1y1[0],x1y1[1],result[0][0],result[0][1]));
        //System.out.println("Small Root Length "+getLength(x1y1[0],x1y1[1],result[1][0],result[1][1]));
    
        return result;
}



public double[] getPointsOnLineBetween(double[] x1y1,double atDistance,double[] x2y2){
    double[][] points=getPointsOnLine(x1y1,atDistance);
    
    if(pointInBoundingBox2D(x1y1,x2y2,points[0])) return points[0]; //SPX Specific. Recheck. 
    else return points[1];
        
}
/**
 * if(parallelToX)
        return Math.abs(Y_const-y);
    else if(parallelToX)
        return Math.abs(X_const-x);
        else
    return Math.abs(y-m*x-c)/Math.sqrt(1+m*m);
 * @param x
 * @param y
 * @return 
 */
public double getShortestDistance(double x, double y){
    
    if(parallelToX)
        return Math.abs(Y_const-y);
    else if(parallelToX)
        return Math.abs(X_const-x);
        else
    return Math.abs(y-m*x-c)/Math.sqrt(1+m*m);
}

/**
 * Corrective function
 * @param corner1
 * @param corner2
 * @param point
 * @return 
 */
/**
 * For 2D Only
 * @param corner1
 * @param corner2
 * @param point
 * @return 
 */
public boolean pointInBoundingBox2D(double corner1[],double corner2[],double point[]){
    //if(corner1.length==2){
        double xi=point[0];
        double yi=point[1];
        setMXpC( corner1[0], corner1[1], corner2[0], corner2[1]);
       
        if((Math.abs(corner1[0]-xi)+Math.abs(corner2[0]-xi))==Math.abs(corner1[0]-corner2[0])
                &(Math.abs(corner1[1]-yi)+Math.abs(corner2[1]-yi))==Math.abs(corner1[1]-corner2[1])){
            return true;
        }
        
    return false;
}

/**
 * 
 * @param corner1
 * @param corner2
 * @param point
 * @return 
 */
public boolean pointInBoundingBox(double corner1[],double corner2[],double point[]){
    if(corner1.length==2){
        double xi=point[0];
        double zi=point[1];
        setMXpC( corner1[0], corner1[2], corner2[0], corner2[1]);
        if (m > 0) {
                   /**
                     * Result is between the points i.e. in the Bounding Box,
                     * conditions are sensitive and will respond to values in first Quadrant only
                     */
                    if (zi > corner1[1]
                            & zi < corner2[1]
                            & xi >corner1[0]
                            & xi <corner2[0]) {
                        return true;
                    } else {
                        return false;
                    }
                }
                if (m < 0 ) {
                   /**
                     * Result is between the points i.e. in the Bounding Box,
                     * conditions are sensitive and will respond to values in first Quadrant only
                     */
                    if(corner1[1]<corner2[1])
                    {
                        if (zi > corner1[1]
                            & zi < corner2[1]
                            & xi <corner1[0]
                            & xi >corner2[0]) {
                        return true;
                    } else {
                        return false;
                    }}

                    if(corner1[1]>corner2[1]);
                    if (    zi < corner1[1]
                            & zi > corner2[1]
                            & xi >corner1[0]
                            & xi <corner2[0]) {
                        return true;
                    } else {
                        return false;
                    }
                }
    }
    return false;
}
/**
 * To be Implemented
 * @param x
 * @param y
 * @return
 */
public int getQuadrant(double x,double y){
    return 0;
}

/**
 * 
 * @return 
 */
public double getSlopeAngle(){
    return Math.toDegrees(Math.atan(m));
}

/**
 * 
 * @param toLine
 * @return
 * @throws CloneNotSupportedException 
 */
public static Line getPerpendicularLine(Line toLine) throws CloneNotSupportedException{
   Line result=(Line) toLine.clone();
   result.setM(Math.tan(Math.PI*0.5+Math.atan(toLine.m)));
   return result;
}
/**
 * 
 * @param toLine
 * @param passingThrough_x
 * @param passingThrough_y
 * @return
 * @throws CloneNotSupportedException 
 */
public static Line getParallelLine(Line toLine,double passingThrough_x,double passingThrough_y) throws CloneNotSupportedException{
   Line result=new Line();
   result.setMC(passingThrough_x,passingThrough_y,toLine.m);
   return result;
}


public static double angleBeteenLines(Line line1,Line line2){
    /**
     * if m1=NaN, theta1=PI/2, return Math.atan(slope 2)-PI/2
     * if m2=NaN, theta2=PI/2  return PI/2-Math.atan(slope 1)
     */
    Double m1 = new Double(line1.m);
    if(m1.equals(NaN)){return Math.atan(line2.m)-Math.PI/2;}
    m1 = new Double(line2.m);
    if(m1.equals(NaN)){return Math.PI/2-Math.atan(line1.m);}
    
    return Math.atan((line2.m-line1.m)/(1+line1.m*line2.m));

}

public static double[] intersectionPointBeteenLines(Line line1,Line line2){
    double x,y;
    if(line1.parallelToY){ x=line1.getX(0); y=line2.getY(x);}else
    if(line2.parallelToY){x=line2.getX(0); y=line1.getY(x);}else{
    x=(line2.c-line1.c)/(line1.m-line2.m);
    y=line1.getY(x);}
    return new double[]{x,y};

}

public static double distanceBetween2Points(double x1,double y1,double x2,double y2){
    
    return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
}

public static double distanceBetween2Points(double[] Point1,double[] Point2){
    
    return Math.sqrt((Point1[0]-Point2[0])*(Point1[0]-Point2[0])+(Point1[1]-Point2[1])*(Point1[1]-Point2[1])+(Point1[2]-Point2[2])*(Point1[2]-Point2[2]));
}

public static double[] ScaleValues(double [] Points,double Scale){
    
    for(int i=0;i<Points.length;i++){
    Points[i]=Points[i]*Scale;
    }
    
    return Points;
}

public static double[] Transform(double [] Points,double[] TransformPoints){
    double results[]=new double[Points.length];
    for(int i=0;i<Points.length;i++){
    results[i]=Points[i]+TransformPoints[i];
    }
    return results;
}

public static double[] Transform(double [] Points,double TransformPoints){
    for(int i=0;i<Points.length;i++){
        //System.out.print("#Transform Pre="+Points[i]);
        Points[i]=Points[i]+TransformPoints;
        //System.out.println("\tPost="+Points[i]);
    }
    return Points;
}

public static double[][] Transform(double [][] Points,double[] TransformPoints){
    for(int i=0;i<Points.length;i++){
        for(int j=0;j<Points[0].length;j++){
           // System.out.print("#Transform Pre="+Points[i][j]);
            Points[i][j]=Points[i][j]+TransformPoints[j];
           // System.out.println("Post="+Points[i][j]);
        }
    }
    return Points;
}

}
