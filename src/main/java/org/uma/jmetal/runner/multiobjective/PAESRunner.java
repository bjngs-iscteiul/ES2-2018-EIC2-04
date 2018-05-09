package org.uma.jmetal.runner.multiobjective;

import interfaces.jMetalDinamicValues;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.paes.PAESBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * Class for configuring and running the PAES algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class PAESRunner extends AbstractAlgorithmRunner implements jMetalDinamicValues {
  private static HashMap<String, Double> doubleHmapProperty = new HashMap<String, Double>();
  private static HashMap<String, Integer> intHmapProperty = new HashMap<String, Integer>();

  public PAESRunner() {
    doubleHmapProperty.put("mutationProbability", 1.0);
    doubleHmapProperty.put("distributionIndex", 20.0);

    intHmapProperty.put("MaxEvaluations", 25000);
    intHmapProperty.put("ArchiveSize", 100);
    intHmapProperty.put("BiSections", 5);
  }



  @Override
  public void setDoubleHmapProperty(HashMap<String, Double> hmapProperty) {
    if (hmapProperty.size() == hmapProperty.size()) {
      this.doubleHmapProperty = hmapProperty;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void setIntHmapProperty(HashMap<String, Integer> hmapProperty) {
    if (hmapProperty.size() == hmapProperty.size()) {
      this.intHmapProperty = hmapProperty;
    } else {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public HashMap<String, Integer> getIntHmapProperty() {
    return intHmapProperty;
  }

  @Override
  public HashMap<String, Double> getDoubleHmapProperty() {
    return doubleHmapProperty;
  }

  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.PAESRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    Problem<DoubleSolution> problem;
    Algorithm<List<DoubleSolution>> algorithm;
    MutationOperator<DoubleSolution> mutation;

    String referenceParetoFront = "" ;

    String problemName ;
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.Kursawe";
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/Kursawe.pf" ;
    }

    problem = ProblemUtils.loadProblem(problemName);

    mutation = new PolynomialMutation(doubleHmapProperty.get("mutationProbability")/ problem.getNumberOfVariables(), doubleHmapProperty.get("distributionIndex")) ;

    algorithm = new PAESBuilder<DoubleSolution>(problem)
            .setMutationOperator(mutation)
            .setMaxEvaluations(intHmapProperty.get("MaxEvaluations"))
            .setArchiveSize(intHmapProperty.get("ArchiveSize"))
            .setBiSections(intHmapProperty.get("BiSections"))
            .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    List<DoubleSolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);
    if (!referenceParetoFront.equals("")) {
      printQualityIndicators(population, referenceParetoFront) ;
    }
  }
}
