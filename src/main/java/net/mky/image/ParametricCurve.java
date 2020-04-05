/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.image;

/**
 *
 * @author PDI
 */
public class ParametricCurve {
   public static boolean debug=false;
   public final int NumberOfCurves=7;
   
   /**
    * 
    * @param t
    * @param Radius
    * @return 
    */
   public static double[] Circle(double t,double Radius){
        double x = (double) (Radius*Math.cos(t*2*Math.PI));
        double y = (double) (Radius*Math.sin(t*2*Math.PI));
        double z = 0;
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new double[]{x,y,z}; 
   }
   
   public static double[] Sphere(double t){

        double x = (double) (1*Math.cos(t*2*Math.PI)*Math.sin(t*2*Math.PI));// rCos(phi)Sin(theta)
        double y = (double) (1*Math.sin(t*2*Math.PI)*Math.cos(t*2*Math.PI));// rSin(phi)Cos(theta)
        double z = (double) (1*Math.cos(t*2*Math.PI));//rCos(theta)
        
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new double[]{x,y,z}; 
   }
   
   public static int[] Bowl(double t, double Radius, int NumberOfRings){

       double[] result=Helix( t,NumberOfRings);
       return new int[]{(int)(result[0]*Radius*t),(int)(result[1]*Radius*t),(int)(result[2]*Radius*t)}; 
   }
   
   public static double[] Helix(double t, int Turns){
        double x = (double) (1*Math.cos(t*2*Math.PI*Turns));
        double y = (double) (1*Math.sin(t*2*Math.PI*Turns));
        double z = t*1;
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new double[]{x,y,z}; 
   }
   
   public static int[] Helix(double t, double Radius, double height, int Turns){
        int x = (int) (Radius*Math.cos(t*2*Math.PI*Turns));
        int y = (int) (Radius*Math.sin(t*2*Math.PI*Turns));
        int z = (int) (t*height);
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new int[]{x,y,z}; 
   }

   public static int[] Spiral(double t, double StartRadius, double EndRadius2, int Turns){
        int x = (int) (((EndRadius2-StartRadius)*t*Turns+StartRadius)*Math.cos(t*2*Math.PI*Turns));
        int y = (int) (((EndRadius2-StartRadius)*t*Turns+StartRadius)*Math.sin(t*2*Math.PI*Turns));
        int z =(int)((EndRadius2-StartRadius)*t*Turns+StartRadius);
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        return new int[]{x,y,z}; 
   }
   public static int[] TransformingSpiral(double t, double StartRadius, double EndRadius2, int Turns){
        int x = (int) (((EndRadius2-StartRadius)*t*Turns+StartRadius)*Math.cos(t*2*Math.PI*Turns));
        int y = (int) ((EndRadius2-StartRadius)*t*Turns+StartRadius*Math.sin(t*2*Math.PI*Turns));
        int z = (int) ((EndRadius2-StartRadius)*t*Turns);
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        return new int[]{x,y,z}; 
   }

   
   public static int[] RandomCurveEample(double t, int MajorValue, int MinorValue,int Type){   
       if(Type==1)
        return RandomCurve1(t, MajorValue, MinorValue);  
       if(Type==2)
        return RandomCurve2(t, MajorValue, MinorValue);
       if(Type==3)
        return RandomCurve3(t, MajorValue, MinorValue);
       if(Type==4)
        return Bowl(t, MajorValue, 100);
       if(Type==5)
        return Helix(t, MajorValue, MinorValue, 10);
       if(Type==6)
        return Spiral(t, MajorValue, MinorValue, 10);
       if(Type==7)
        return TransformingSpiral(t, MajorValue, MinorValue, 10);//TransformingSpiral
       if(Type==8)
        return ButterflyCurve(t,MajorValue,10);
       
       return new int[]{1,1,1};
    }

    public static int[] RandomCurve1(double t, int MajorValue, int MinorValue){ 
        
        int x = (int) (MinorValue*t + MajorValue*Math.cos(t*4*Math.PI*17));
        int y = (int) (MinorValue*t + MajorValue*Math.sin(t*4*Math.PI*17));
        int z = (int) (x*Math.cos(t*Math.PI));
        
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new int[]{x,y,z};  
    }
    
    public static int[] RandomCurve2(double t, int MajorValue, int MinorValue){ 
        
        int x = (int) (MinorValue*t + MajorValue*Math.cos(t*4*Math.PI*17));
        int y = (int) (MinorValue*t + MajorValue*Math.sin(t*4*Math.PI*17));
        int z = (int) (y*Math.cos(t*Math.PI));
        
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new int[]{x,y,z};  
    }

    public static int[] RandomCurve3(double t, int MajorValue, int MinorValue){ 
        
        int x = (int) (MinorValue*t + MajorValue*Math.cos(t*4*Math.PI*50));
        int y = (int) (MinorValue*t + MajorValue*Math.sin(t*4*Math.PI*50));
        int z = (int) (y*Math.cos(t*Math.PI*2));
        
        if(debug)
        System.out.println(x+ " "+y+" "+z);
        
        return new int[]{x,y,z};  
    }

    /**
     * Just any shape.
     * @param NumOfPoints
     * @return 
     */
    
    public static Double[][] RandomShape(int NumOfPoints){
        Double[][] result=new Double[NumOfPoints][2];
        //Find points in Boundary/NumOfPoints region else reject
        result[0][0]=Math.random();
        result[0][1]=Math.random();
         for(int i=0;i<NumOfPoints;i++){
             while(i>0){
                  double x=Math.random();
                  double y=Math.random();
                  double distance=Line.distanceBetween2Points(result[i-1][0], result[i-1][1],x,y);
                 if((distance>= ((double)1/(double)NumOfPoints)) ){ 
                        result[i][0]=x;
                        result[i][1]=y;
                        if(debug)System.out.println("#Accepted "+ i);
                        break;
                 }else {
                    // System.out.println("#Rejecting "+ Line.distanceBetween2Points(result[i-1][0], result[i-1][1],x,y));
                 }
             }
             
            
        }
        return result;
   }
    
    
    public static int[][] RandomClosedLoopSmoothCurve(int NumOfPoints, int Boundary){
        int[][] result=new int[NumOfPoints][2];
        //Find points in Boundary/NumOfPoints region else reject
        result[0][0]=(int) (Boundary*Math.random());
        result[0][1]=(int) (Boundary*Math.random());
         for(int i=0;i<NumOfPoints;i++){

             while(i>0){
                  int x=(int) (Boundary*Math.random());
                  int y=(int) (Boundary*Math.random());
                  double distance=Line.distanceBetween2Points(result[i-1][0], result[i-1][1],x,y);
                 if((distance<= ((double)Boundary/(double)NumOfPoints)) ){ 

                   result[i][0]=x;
                   result[i][1]=y;
                  // System.out.println("#Accepted "+ i);
                   break;
                 }else {
                    // System.out.println("#Rejecting "+ Line.distanceBetween2Points(result[i-1][0], result[i-1][1],x,y));
                 }
             }
             
            
        }
        return result;
   }
    
    public static int[][] RandomClosedLoopCurve(int NumOfPoints, int Boundary){
        int[][] result=new int[NumOfPoints][2];
         for(int i=0;i<NumOfPoints;i++){
            result[i][0]=(int) (Boundary*Math.random());
            result[i][1]=(int) (Boundary*Math.random());
        }
        return result;
   }
    
     public static int[] ButterflyCurve(double t, double Scale,int turns){
    
         double FullAngle=2*Math.PI*turns;
        double x=Math.sin(t*FullAngle)*(Math.pow(Math.E,Math.cos(t*FullAngle))-2*Math.cos(4*t*FullAngle)-Math.pow(Math.sin(t*FullAngle/12),5)); 
        double y=Math.cos(t*FullAngle)*(Math.pow(Math.E,Math.cos(t*FullAngle))-2*Math.cos(4*t*FullAngle)-Math.pow(Math.sin(t*FullAngle/12),5)); 
        double z=0.0;
        
        return new int[]{(int)(x*Scale),(int)(y*Scale),(int)(z*Scale)};
    }
    
     public static int[] ButterflyCurve(double t, double Scale){
    
        double x=Math.sin(t*2*Math.PI)*(Math.pow(Math.E,Math.cos(t*2*Math.PI))-2*Math.cos(4*t*Math.PI*2)-Math.pow(Math.sin(t*2*Math.PI/12),5)); 
        double y=Math.cos(t*2*Math.PI)*(Math.pow(Math.E,Math.cos(t*2*Math.PI))-2*Math.cos(4*t*Math.PI*2)-Math.pow(Math.sin(t*2*Math.PI/12),5)); 
        double z=0.0;
        
        return new int[]{(int)(x*Scale),(int)(y*Scale),(int)(z*Scale)};
    }
    
    public static Double[] ButterflyCurve(double t){
    
        double x=Math.sin(t*2*Math.PI)*(Math.pow(Math.E,Math.cos(t*2*Math.PI))-2*Math.cos(4*t*Math.PI*2)-Math.pow(Math.sin(t*2*Math.PI/12),5)); 
        double y=Math.cos(t*2*Math.PI)*(Math.pow(Math.E,Math.cos(t*2*Math.PI))-2*Math.cos(4*t*Math.PI*2)-Math.pow(Math.sin(t*2*Math.PI/12),5)); 
        double z=0.0;
        
        return new Double[]{x,y,z};
    }

}
