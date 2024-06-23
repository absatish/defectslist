package com.defectlist.inwarranty;


import smile.data.DataFrame;
import smile.data.vector.DoubleVector;
import smile.io.Read;
import smile.math.kernel.GaussianKernel;
import smile.math.kernel.MercerKernel;
import smile.regression.GaussianProcessRegression;
import smile.regression.Regression;
import smile.regression.RidgeRegression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {

        test();
    }


    private static void test() {

        //return 0.0; //TODO RAJESH
    }

    private static Double[] load(List<List<Double>> records, int index) {
        return records.stream()
                .map(record -> record.get(index))
                .collect(Collectors.toList()).toArray(Double[]::new);
    }

    private static void mm() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> Crate = new ArrayList<>();
        ArrayList<ArrayList<Double>> DoD = new ArrayList<>();
        int whileloop = 1;
        int nocycles = 0;

        while (whileloop > 0) {
            System.out.print("Please enter number of times the C-rate is varying during the operation: ");
            nocycles = scanner.nextInt();

            for (int count = 1; count <= nocycles; count++) {
                ArrayList<Double> dod = new ArrayList<>();
                if (count == 1) {
                    System.out.print("Please enter C-rate of cycle - " + count + " / " + nocycles + " (between 0.5 to 5) : ");
                    double crate = scanner.nextDouble();
                    Crate.add(crate);

                    System.out.print("Please enter initial charge of cell for Cycle - " + count + " / " + nocycles + " (between 100 % - 5 %) : ");
                    double initialCharge = scanner.nextDouble();
                    dod.add(initialCharge);

                    System.out.print("Please enter final charge of cell after cycle - " + count + " / " + nocycles + " (must be less than initial charge) (between 100 % - 5 %) : ");
                    double finalCharge = scanner.nextDouble();
                    dod.add(finalCharge);
                } else {
                    System.out.print("Please enter C-rate of cycle - " + count + " / " + nocycles + "(between 0.5 to 5) : ");
                    double crate = scanner.nextDouble();
                    Crate.add(crate);

                    System.out.println("Initial charge of cell for Cycle - " + count + "/ " + nocycles + " : " + DoD.get(count - 2).get(1));
                    dod.add(DoD.get(count - 2).get(1));

                    System.out.print("Please enter final charge of cell for Cycle - " + count + " / " + nocycles + " (between " + DoD.get(count - 2).get(1) + " % - 5 %) : ");
                    double finalCharge = scanner.nextDouble();
                    dod.add(finalCharge);
                }
                DoD.add(dod);
            }

            ArrayList<ArrayList<Double>> forploting = new ArrayList<>();
            for (int count1 = 0; count1 < nocycles; count1++) {
                ArrayList<Double> row1 = new ArrayList<>();
                row1.add(DoD.get(count1).get(0));
                row1.add(Crate.get(count1));
                forploting.add(row1);

                ArrayList<Double> row2 = new ArrayList<>();
                row2.add(DoD.get(count1).get(1));
                row2.add(Crate.get(count1));
                forploting.add(row2);
            }

            // Plotting code here

            System.out.print("Would you like to change the profile of C-rate variation? (Yes/No/Cancel): ");
            String changingprofile = scanner.next();
            if (changingprofile.equalsIgnoreCase("Yes")) {
                whileloop = 1;
            } else {
                whileloop = 0;
            }
        }

        System.out.print("Please enter initial temperature of cell between 15 to 35 (°C): ");
        double InitialTemp1 = scanner.nextDouble();
        double InitialTemp2 = InitialTemp1;
        double InitialTemp3 = InitialTemp2;

        System.out.print("Please enter inlet temperature of the fluid between 15 to 35 (°C) (Must be less than or equal to initial temperature): ");
        double InletTemp = scanner.nextDouble();

        System.out.print("Please enter set point temperature between 18 to 40 (°C): ");
        double SPT = scanner.nextDouble();

        double MaxDiff = SPT - InitialTemp1;
        double GridSize = 0.03;
        int NoOfGrids = (int) Math.ceil(0.5 / GridSize) + 1;
        int NoOfElementsInBigMatrix = (int) Math.pow(NoOfGrids, nocycles);
        double[][] BigMatrix = new double[NoOfElementsInBigMatrix][nocycles];
        double[] UnitCell = new double[NoOfGrids];

        int count4 = 1;
        for (double subcount4 = GridSize; subcount4 <= 0.5; subcount4 += GridSize) {
            UnitCell[count4 - 1] = subcount4;
            count4++;
        }
        UnitCell[0] = 0.01;
        if (GridSize % 0.05 != 0) {
            UnitCell[NoOfGrids - 1] = 0.5;
        }

        for (int count6 = 0; count6 < NoOfElementsInBigMatrix / NoOfGrids; count6++) {
            for (int i = 0; i < NoOfGrids; i++) {
                BigMatrix[NoOfGrids * count6 + i][nocycles - 1] = UnitCell[i];
            }
        }

        for (int count7 = nocycles - 2; count7 >= 0; count7--) {
            int count10 = nocycles - count7 - 1;
            for (int count8 = 0; count8 < NoOfElementsInBigMatrix / Math.pow(NoOfGrids, count10); count8++) {
                int index = (int) (NoOfGrids * count10 * count8);
                if (count8 % NoOfGrids == 0) {
                    BigMatrix[index][count7] = UnitCell[NoOfGrids - 1];
                    for (int i = 1; i < NoOfGrids; i++) {
                        BigMatrix[index + i][count7] = UnitCell[NoOfGrids - 1];
                    }
                } else {
                    BigMatrix[index][count7] = UnitCell[(int) (count8 % NoOfGrids)];
                    for (int i = 1; i < NoOfGrids; i++) {
                        BigMatrix[index + i][count7] = UnitCell[(int) (count8 % NoOfGrids)];
                    }
                }
            }
        }

        ArrayList<ArrayList<Double>> CollectionOfVelocities = new ArrayList<>();
        ArrayList<ArrayList<Double>> CollectionOfTemp = new ArrayList<>();
        int subcount5 = 0;

        for (int count11 = 0; count11 < NoOfElementsInBigMatrix; count11++) {
            InitialTemp3 = InitialTemp1;
            int count13 = 0;
            ArrayList<Double> tempList = new ArrayList<>();
            for (int count12 = 0; count12 < nocycles; count12++) {
                double DoDforcomputation = 1 - DoD.get(count12).get(0) / 100;
                double DoDforcomputation2 = 1 - DoD.get(count12).get(1) / 100;
                double temp = predict(Crate.get(count12), DoDforcomputation, InitialTemp3, InletTemp, BigMatrix[count11][count12], DoDforcomputation2);
                tempList.add(temp);
                InitialTemp3 = temp;
                if (temp < SPT) {
                    count13++;
                }
            }
            if (count13 == nocycles) {
                subcount5++;
                ArrayList<Double> velocities = new ArrayList<>();
                velocities.add((double) count11);
                for (int i = 0; i < nocycles; i++) {
                    velocities.add(BigMatrix[count11][i]);
                }
                CollectionOfVelocities.add(velocities);
                CollectionOfTemp.add(tempList);
            }
        }

        int count15 = CollectionOfVelocities.size();
        if (count15 > 0) {
            for (int count16 = 0; count16 < count15; count16++) {
                double sum = 0;
                for (int i = 1; i <= nocycles; i++) {
                    sum += CollectionOfVelocities.get(count16).get(i);
                }
                CollectionOfVelocities.get(count16).add(sum);
            }

            int finalNocycles = nocycles;
            CollectionOfVelocities.sort(Comparator.comparingDouble(a -> a.get(finalNocycles + 1)));

            ArrayList<Double> MinVelocity = new ArrayList<>(CollectionOfVelocities.get(0).subList(1, nocycles + 1));
            ArrayList<Double> TempForMinVelocity = CollectionOfTemp.get(CollectionOfVelocities.get(0).get(0).intValue() - 1);

            ArrayList<Double> ConVelTemp = new ArrayList<>();
            double InitialTemp4 = InitialTemp1;
            for (int count20 = 0; count20 < nocycles; count20++) {
                double DoDforcomputation = 1 - DoD.get(count20).get(0) / 100;
                double DoDforcomputation2 = 1 - DoD.get(count20).get(1) / 100;
                double temp = predict(Crate.get(count20), DoDforcomputation, InitialTemp4, InletTemp, 0.01, DoDforcomputation2);
                ConVelTemp.add(temp);
                InitialTemp4 = temp;
            }

            ArrayList<ArrayList<Double>> MinConVelCollection = new ArrayList<>();
            ArrayList<ArrayList<Double>> MinConVelTemp = new ArrayList<>();
            int subcount7 = 0;
            for (double count17 = GridSize; count17 <= 0.5; count17 += GridSize) {
                ArrayList<Double> tempList = new ArrayList<>();
                int subcount6 = 0;
                double InitialTemp5 = InitialTemp2;
                for (int count18 = 0; count18 < nocycles; count18++) {
                    double DoDforcomputation = 1 - DoD.get(count18).get(0) / 100;
                    double DoDforcomputation2 = 1 - DoD.get(count18).get(1) / 100;
                    double temp = predict(Crate.get(count18), DoDforcomputation, InitialTemp5, InletTemp, count17, DoDforcomputation2);
                    tempList.add(temp);
                    InitialTemp5 = temp;
                    if (temp < SPT) {
                        subcount6++;
                    }
                }
                if (subcount6 == nocycles) {
                    subcount7++;
                    ArrayList<Double> velocities = new ArrayList<>();
                    velocities.add(count17);
                    MinConVelCollection.add(velocities);
                    MinConVelTemp.add(tempList);
                }
            }

            double MinConVel = MinConVelCollection.get(0).get(0);
            ArrayList<Double> PercentEnergySaving = new ArrayList<>(nocycles + 1);
            ArrayList<Double> DiffInDoD = new ArrayList<>(nocycles);
            for (int subcount8 = 0; subcount8 < nocycles; subcount8++) {
                DiffInDoD.add(DoD.get(subcount8).get(0) - DoD.get(subcount8).get(1));
            }

            for (int count22 = 0; count22 < nocycles; count22++) {
                double diffInDod = DiffInDoD.get(count22);
                double initialDod = DoD.get(0).get(0);
                double finalDod = DoD.get(nocycles - 1).get(1);
                double minVelocity = MinVelocity.get(count22);
                double percentEnergySaving = (diffInDod / (initialDod - finalDod)) * ((MinConVel - minVelocity) / MinConVel) * 100;
                PercentEnergySaving.add(percentEnergySaving);
            }

            double sum = 0;
            for (double value : PercentEnergySaving) {
                sum += value;
            }
            PercentEnergySaving.add(sum / nocycles);

            ArrayList<ArrayList<Double>> forploting = new ArrayList<>();
            for (int count21 = 0; count21 < nocycles; count21++) {
                ArrayList<Double> row1 = new ArrayList<>();
                row1.add(DoD.get(count21).get(0));
                row1.add(Crate.get(count21));
                row1.add(MinVelocity.get(count21));
                row1.add(TempForMinVelocity.get(count21));
                row1.add(MinConVel);
                row1.add(MinConVelTemp.get(0).get(count21));
                row1.add(SPT);
                forploting.add(row1);

                ArrayList<Double> row2 = new ArrayList<>();
                row2.add(DoD.get(count21).get(1));
                row2.add(Crate.get(count21));
                row2.add(MinVelocity.get(count21));
                row2.add(TempForMinVelocity.get(count21));
                row2.add(MinConVel);
                row2.add(MinConVelTemp.get(0).get(count21));
                row2.add(SPT);
                forploting.add(row2);
            }

            for(ArrayList<Double> row : forploting) {
                System.out.println(row.get(0) + "," + row.get(1));
            }

            System.out.println("Stop here");
            // Plotting code here

        } else {
            System.out.println("With the given conditions, it is not possible to confine the maximum temperature of the cell below given SPT");
        }

        scanner.close();
    }

    private static double predict(double crate, double doDforcomputation, double initialTemp3, double inletTemp, double velocity2, double doDforcomputation2) {
        // Code to predict temperature using the GPR model

        try {

//            DataFrame dataFrame = Read.csv("/Users/satishjajula/Downloads/data.csv");
            List<List<Double>> records = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("/Users/satishjajula/Downloads/Input_and_Output_noscaling (2).csv"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains("C-rate")) {
                        continue;
                    }
                    String[] values = line.split(",");
                    records.add(Arrays.stream(values).map(Double::valueOf).collect(Collectors.toList()));
                }
            }
            Double[] cRate = load(records, 0);
            //dataFrame.column("C-rate").toDoubleArray();
            Double[] iDoD = load(records, 1); //dataFrame.column("Initial - DoD").toDoubleArray();
            Double[] iTemp = load(records, 2);//dataFrame.column("Initial-Temp").toDoubleArray();
            Double[] cTemp = load(records, 3);//dataFrame.column("Coolant-Temp").toDoubleArray();
            Double[] velocity = load(records, 4);//records.get(4).toArray(Double[]::new);//dataFrame.column("Velocity").toDoubleArray();
            Double[] fDoD = load(records, 5);//records.get(5).toArray(Double[]::new);//dataFrame.column("Final-DoD").toDoubleArray();
            Double[] outcome = load(records, 6);//records.get(6).toArray(Double[]::new);//dataFrame.column("Final-DoD").toDoubleArray();

            // Combine features into a double[][] array
            int n = records.size();
            double[][] X = new double[n][6]; // Assuming 3 features
            for (int i = 0; i < n; i++) {
                X[i][0] = cRate[i];
                X[i][1] = iDoD[i];
                X[i][2] = iTemp[i];
                X[i][3] = cTemp[i];
                X[i][4] = velocity[i];
                X[i][5] = fDoD[i];
            }

            //            List<String> featureColumns = Arrays.asList();
            // Split data into training and testing sets (example, adjust as needed)
            int trainSize = (int) (n * 1.0);  // 80% training, 20% testing
            double[][] X_train = new double[trainSize][];
            double[] y_train = new double[trainSize];
            double[][] X_test = new double[n - trainSize][];
            double[] y_test = new double[n - trainSize];

            for (int i = 0; i < trainSize; i++) {
                X_train[i] = X[i];
                y_train[i] = outcome[i];
            }

//            for (int i = trainSize; i < n; i++) {
//                X_test[i - trainSize] = X[i];
//                y_test[i - trainSize] = outcome[i];
//            }

            Regression<double[]> gpr = GaussianProcessRegression.fit(X_train, y_train, new GaussianKernel(1e5), new Properties());

            // Initialize Ridge Regression trainer with the Gaussian Kernel
            double[] testPoint = {crate, doDforcomputation, initialTemp3, inletTemp, velocity2, doDforcomputation2};
            System.out.println(testPoint[0] + ", " + testPoint[1] + ", " + testPoint[2] + ", " + testPoint[3] +  ", " + testPoint[4] + ", " + testPoint[5]);

            double prediction = gpr.predict(testPoint);

            System.out.println("Prediction for test: " + prediction );
            return prediction;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

