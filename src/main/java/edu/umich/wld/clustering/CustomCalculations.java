////////////////////////////////////////////////////
// CustomCalculations.java
// Written by Jan Wigginton, Aug 1, 2017
////////////////////////////////////////////////////
package edu.umich.wld.clustering;

public class CustomCalculations
	{
	public static double calculateEuclidianDistance(double [] array1, double [] array2)
		{
		double distance = 0.0;
		
		for (int i = 0; i < array1.length; i++)
			{
			double val = array1[i] - array2[i];
			distance += val * val;
			}
		
		return Math.sqrt(distance);
		}
	}
