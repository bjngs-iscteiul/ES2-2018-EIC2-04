package org.uma.jmetal.runner.multiobjective;

import interfaces.jMetalDinamicValues;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.moead.AbstractMOEAD;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.crossover.DifferentialEvolutionCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

/**
 * Class for configuring and running the MOEA/D algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class MOEADRunner extends AbstractAlgorithmRunner implements jMetalDinamicValues {

  private static HashMap<String, Double> doubleHmapProperty = new HashMap<String, Double>();
  private static HashMap<String, Integer> intHmapProperty = new HashMap<String, Integer>();


  public MOEADRunner(){
    doubleHmapProperty.put("mutationDistributionIndex",20.0);
    doubleHmapProperty.put("NeighborhoodSelectionProbability",0.9);
    intHmapProperty.put("MaxEvaluations",150000);
    intHmapProperty.put("PopulationSize",100);
    intHmapProperty.put("ResultPopulationSize",100);
    intHmapProperty.put("MaximumNumberOfReplacedSolutions",2);
    intHmapProperty.put("NeighborSize",20);
  }

  @Override
  public void setDoubleHmapProperty(HashMap<String, Double> hmapProperty) {
    if (hmapProperty.size() == hmapProperty.size()) {
      this.doubleHmapProperty = hmapProperty;
    } else {
      throw new IllegalArgumentException;
    }
  }

  @Override
  public void setIntHmapProperty(HashMap<String, Integer> hmapProperty) {
    if (hmapProperty.size() == hmapProperty.size()) {
      this.intHmapProperty = hmapProperty;
    } else {
      throw new IllegalArgumentException;
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
  java org.uma.jmetal.runner.multiobjective.MOEADRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws FileNotFoundException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    MutationOperator<DoubleSolution> mutation;
    DifferentialEvolutionCrossover crossover;

    String problemName ;
    String referenceParetoFront = "";
    if (args.length == 1) {
      problemName = args[0];
    } else if (args.length == 2) {
      problemName = args[0] ;
      referenceParetoFront = args[1] ;
    } else {
      problemName = "org.uma.jmetal.problem.multiobjective.UF.UF1";
      referenceParetoFront = "jmetal-problem/src/test/resources/pareto_fronts/UF1.pf";
    }

    problem = (DoubleProblem)ProblemUtils.<DoubleSolution> loadProblem(problemName);

    double cr = 1.0 ;
    double f = 0.5 ;
    crossover = new DifferentialEvolutionCrossover(cr, f, "rand/1/bin");

    double mutationProbability = 1.0 / problem.getNumberOfVariables();
    double mutationDistributionIndex = doubleHmapProperty.get("mutationDistributionIndex");


    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

    algorithm = new MOEADBuilder(problem, MOEADBuilder.Variant.MOEAD)
            .setCrossover(crossover)
            .setMutation(mutation)
            .setMaxEvaluations(intHmapProperty.get("MaxEvaluations"))
            .setPopulationSize(intHmapProperty.get("PopulationSize"))
            .setResultPopulationSize(intHmapProperty.get("ResultPopulationSize"))
            .setNeighborhoodSelectionProbability(0.9)
            .setMaximumNumberOfReplacedSolutions(intHmapProperty.get("MaximumNumberOfReplacedSolutions"))
            .setNeighborSize(intHmapProperty.get("NeighborSize"))
            .setFunctionType(AbstractMOEAD.FunctionType.TCHE)
            .setDataDirectory("MOEAD_Weights")
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
