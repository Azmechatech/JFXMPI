/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.image;

/**
 *
 * @author PDI
 */
public class EqnSolver {

    public double root1, root2, bigRoot, smallRoot;

    public double QuadSolv(double a, double b, double c) {

        double discr;
        root1 = 0;
        root2 = 0;

        // Apllying the quadratic formula
        // Obtain sides from user
        //System.out.println("#QuadSolve a="+a+"\t b="+b+"\t c="+c);
        //a = 1d;
        // b = 2d;
        //c = 3d;

        //System.out.println("b^2="+b*b);
        //System.out.println("4ac="+4*a*c*-1);

        // Solve the discriminant (SQRT (b^2 - 4ac)
        discr = Math.sqrt((b * b) - (4 * a * c));
        //System.out.print("Discr= " + discr);
        // Determine number of roots
        // if discr > 0 equation has 2 real roots
        // if discr == 0 equation has a repeated real root
        // if discr < 0 equation has imaginary roots
        // if discr is NaN equation has no roots

        // Test for NaN
        if (Double.isNaN(discr)) {
            System.out.println("Equation has no roots" + "Discr= " + discr);
        }

        if (discr > 0) {
            //System.out.print("Equation has 2 roots");
            root1 = (-b + discr) / (2 * a);
            root2 = (-b - discr) / (2 * a);
            System.out.print("Root1 = " + root1);
            System.out.print("Root2 = " + root2);
        }

        if (discr == 0) {
            //System.out.println("Equation has 1 root");
            root1 = (-b + discr) / (2 * a);
            System.out.print("Root = " + root1);
        }

        if (discr < 0) {
            System.out.println("Equation has imaginary roots");
        }

        if (root1 > root2) {
            bigRoot = root1;
            smallRoot = root2;
            return root1;

        }
        if (root2 > root1) {
            bigRoot = root2;
            smallRoot = root1;
            return root2;
        }

        return 0;
    }
}
