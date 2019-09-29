/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
 
public class PolynomialRegression {
    private static void polyRegression(int[] x, int[] y) {
        int n = x.length;
        int[] r = IntStream.range(0, n).toArray();
        double xm = Arrays.stream(x).average().orElse(Double.NaN);
        double ym = Arrays.stream(y).average().orElse(Double.NaN);
        double x2m = Arrays.stream(r).map(a -> a * a).average().orElse(Double.NaN);
        double x3m = Arrays.stream(r).map(a -> a * a * a).average().orElse(Double.NaN);
        double x4m = Arrays.stream(r).map(a -> a * a * a * a).average().orElse(Double.NaN);
        double xym = 0.0;
        for (int i = 0; i < x.length && i < y.length; ++i) {
            xym += x[i] * y[i];
        }
        xym /= Math.min(x.length, y.length);
        double x2ym = 0.0;
        for (int i = 0; i < x.length && i < y.length; ++i) {
            x2ym += x[i] * x[i] * y[i];
        }
        x2ym /= Math.min(x.length, y.length);
 
        double sxx = x2m - xm * xm;
        double sxy = xym - xm * ym;
        double sxx2 = x3m - xm * x2m;
        double sx2x2 = x4m - x2m * x2m;
        double sx2y = x2ym - x2m * ym;
 
        double b = (sxy * sx2x2 - sx2y * sxx2) / (sxx * sx2x2 - sxx2 * sxx2);
        double c = (sx2y * sxx - sxy * sxx2) / (sxx * sx2x2 - sxx2 * sxx2);
        double a = ym - b * xm - c * x2m;
 
        IntToDoubleFunction abc = (int xx) -> a + b * xx + c * xx * xx;
 
        System.out.println("y = " + a + " + " + b + "x + " + c + "x^2");
        System.out.println(" Input  Approximation");
        System.out.println(" x   y     y1");
        for (int i = 0; i < n; ++i) {
            System.out.printf("%2d %3d  %5.1f\n", x[i], y[i], abc.applyAsDouble(x[i]));
        }
    }
    
    public static OLSMultipleLinearRegression getMultipleLinearRegression(List<List<Double>> pointlist) {
    OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
    double y[] = new double[pointlist.size()];
    double x[][] = new double[pointlist.size()][2];
    int c = 0;
    for (List<Double> point : pointlist) {
        y[c] = point.get(0);
        x[c][0] = point.get(1);
        x[c][1] = Math.pow(point.get(1), 2);
        regression.newSampleData(y, x);
        c++;
    }
   // System.out.printf("\tR2 = %f", regression.calculateRSquared());
    return regression;
}
    
    
   
    
    
 
    public static void main(String[] args) {
        int[] x = IntStream.range(0, 11).toArray();
        int[] y = new int[]{1, 6, 17, 34, 57, 86, 121, 162, 209, 262, 321};
        polyRegression(x, y);
        Double[] xData={1.0,2.0,3.0,4.0,3.0,4.0};
        Double[] yData={1.0,2.0,3.0,4.0,3.0,4.0};
        List<Double> listX = Arrays.asList(xData);
        List<Double> listY = Arrays.asList(yData);
        
        List<List<Double>> pointlist=new ArrayList<>();
        pointlist.add(listX);
        pointlist.add(listY);
        
        getMultipleLinearRegression( pointlist);
        
        
    }
}