////////////////////////////////////////////////////
// Silhouette.java
// Written by Jan Wigginton, Dec 3, 2017
////////////////////////////////////////////////////
package edu.umich.wld.clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Silhouette
        {
        private int nClustersToStart, nCurrentClusters;
        private List<Double> weightsGrid = Arrays.asList(new Double[] {0.0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0});
        private List<Double> maxAverageValuesByWeight;
        private List<Integer> ksForMaxAverage;

        
        public Silhouette(int nTotalPts)  
                {
                this(nTotalPts, null);
                }
        
        public Silhouette(int nTotalPts, List<Double> weightsGrid)  
                {
                nClustersToStart = nTotalPts;
                nCurrentClusters = nTotalPts;
                
                if (weightsGrid != null)
                        this.weightsGrid = weightsGrid;
                
                maxAverageValuesByWeight = new ArrayList<Double>(); 
                ksForMaxAverage = new ArrayList<Integer>();
                for (Double weight : this.weightsGrid)
                        {
                        maxAverageValuesByWeight.add(-1.0 * Double.MAX_VALUE);
                        ksForMaxAverage.add(-1);
                        }
                }
        
        
        public void updateForNewK(List<Double> featureWinClusterDistances, List<Map<Integer, Double>> featureByClusterAverages, 
                        int nClusters, Boolean reportingOn) throws Exception
                {
                nCurrentClusters = nClusters;
                
                if (nClusters > 500)
                        return;
                
        //      if (largeBinWeight > 4.0 || largeBinWeight < 0.0)
        //              throw new Exception("Illegal weight parameter during clustering" + largeBinWeight);
                
                if (reportingOn)
                        System.out.println("\nCalculating silhouette values for " + nClusters);
                
                Map<Integer, Double> silhouetteValues = new HashMap<Integer, Double>();
                int nFeatures = featureWinClusterDistances.size();
                
                List<Double> averageSilhouettes = new ArrayList<Double>();
                for (int j = 0; j < weightsGrid.size(); j++)
                        averageSilhouettes.add(0.0);
                
                double currValue, nFeatureDivisor = nFeatures * 1.0;
                for (Integer i = 0; i < nFeatures; i++)
                        {
                        double bi = calculateBi(featureByClusterAverages.get(i));
                        double ai = featureWinClusterDistances.get(i);
                        double si = 0.0;
                        if (Math.abs(ai) < Double.MIN_VALUE)
                                si = 0.0;
                        else if (ai > bi)
                                si =  bi/ai - 1.0;
                        else if (ai < bi)
                                si = 1.0 -ai/bi;
                
                        for (int j = 0; j < weightsGrid.size(); j++)
                                {
                                currValue = averageSilhouettes.get(j);
                                averageSilhouettes.set(j,  currValue + si * (1.0/(1.0 + weightsGrid.get(j) * ai)));
                                }
                        }
                
                
                for (int j = 0; j < averageSilhouettes.size(); j++)
                        {
                        currValue = averageSilhouettes.get(j);
                        averageSilhouettes.set(j, currValue/ nFeatureDivisor);
                        }
                
                
                Double avg, maxAvg;
                for (int j = 0; j < weightsGrid.size(); j++)
                        {
                        avg = averageSilhouettes.get(j);
                        maxAvg = maxAverageValuesByWeight.get(j);
                        if (nCurrentClusters > 1 && nCurrentClusters != nClustersToStart && maxAvg < avg)
                                {
                                this.maxAverageValuesByWeight.set(j, avg);
                                ksForMaxAverage.set(j, nCurrentClusters);
                                }
                        }
                }
        
        
        private Double calculateBi(Map<Integer, Double> mapOfDistances)
                {
                double minDistance = Double.MAX_VALUE;
                
                for (Double value : mapOfDistances.values())
                        minDistance = Math.min(minDistance, value);
                
                return minDistance;
                }

        public List<Integer> getksForMaxAverage()
                {
                return ksForMaxAverage;
                }

        public List<Double> getWeightsGrid()
                {
                return weightsGrid;
                }

        public List<Double> getMaxAverageValuesByWeight()
                {
                return maxAverageValuesByWeight;
                }

        public void setksForMaxAverage(List<Integer> ksForMaxAverage)
                {
                this.ksForMaxAverage = ksForMaxAverage;
                }

        public void setWeightsGrid(List<Double> weightsGrid)
                {
                this.weightsGrid = weightsGrid;
                }

        public void setMaxAverageValuesByWeight(List<Double> maxAverageValuesByWeight)
                {
                this.maxAverageValuesByWeight = maxAverageValuesByWeight;
                }
        }