/*
* Name: Justin Senia
* E-Number: E00851822
* Date: 11/11/2017
* Class: COSC 461
* Project: #3
*/

import java.io.*;
import java.util.*;

/***********************************************************************************/

//This program solves traveling salesman problem using genetic algorithm
public class TravelGen
{
	private int[][] matrix;				//adjacency matrix of graph
	private int numberVertices;			//number of vertices
	private int maximumEdge;			//max edge weight
	
	private PrintWriter pW;
	private int populationSize;			//population size
	private int stringLength;			//string length
	private int numberIterations;		//number of iterations
	private double crossoverRate;		//crossover rate
	private double mutationRate;		//mutation rate
	private Random random;				//random number generator
	
	private int[][] population;			//population of strings
	private double[] fitnessValues;		//fitness values of strings

	/***********************************************************************************/

	//constructor of TravelGen class
	public TravelGen(int[][] matrix, int numberVertices, PrintWriter pWriter)
	{
		//set adjacency matrix and vertices
		this.matrix = matrix;
		this.numberVertices = numberVertices;
		this.pW = pWriter;
		
		//find maximum edge weight in the graph
		maximumEdge = Integer.MIN_VALUE;
		for (int i = 0; i < numberVertices; i++)
		{
			for (int j = 0; j < numberVertices; j++)
			{
				if (matrix[i][j] > maximumEdge)
				{
					maximumEdge = matrix[i][j];
				}
			}
		}
	}

	/***********************************************************************************/

	//method sets the parameters of genetic algorithm
	public void setParameters(int populationSize, int stringLength,
	int numberIterations, double crossoverRate, double mutationRate, int seed)
	{
		//set genetic algorithm parameters
		this.populationSize = populationSize;
		this.stringLength = stringLength;
		this.numberIterations = numberIterations;
		this.crossoverRate = crossoverRate;
		this.mutationRate = mutationRate;
		this.random = new Random(seed);
		
		//create a population and fitness arrays
		this.population = new int[populationSize][stringLength];
		this.fitnessValues = new double[populationSize];
	}

	/***********************************************************************************/

	//method executes genetic algorithm
	public void solve()
	{
		//initialize population
		initialize();
		
		//create generations
		for (int i = 0; i < numberIterations; i++)
		{
			//crossover strings
			crossover();
			
			//mutate strings
			mutate();
			
			//reproduce strings
			reproduce();
		}
		
		//choose best string
		solution();
	}

	/***********************************************************************************/

	//method initializes population (app specific)
	private void initialize()
	{
		//initialize strings with a random permutation of vertices
		for (int i = 0; i < populationSize; i++)
		{
			randomPermutation(population[i]);
		}
		
		//intial fitness values are zero
		for (int i = 0; i < populationSize; i++)
		{		
			fitnessValues[i] = 0;
		}
	}

	/***********************************************************************************/
	
	//method fills a string with a random permutation of vertices
	private void randomPermutation(int[] string)
	{
		//fill string with integers from 1 to string length
		for (int i = 0; i < stringLength; i++)
		{
			string[i] = i + 1;
		}
		
		//created random permutation of integers
		for (int i = stringLength-1; i >= 0; i--)
		{
			int j = random.nextInt(i+1);
			int temp = string[i];
			string[i] = string[j];
			string[j] = temp;
		}
	}
	
	/***********************************************************************************/

	//Method performs crossover operation(app specific)
	private void crossover()
	{
		//go thru each string
		for (int i = 0; i < populationSize; i++)
		{
			//if crossover is performed
			if (random.nextDouble() < crossoverRate)
			{
				//choose another string
				int j = random.nextInt(populationSize);
				
				//choose crossover point
				int cut = random.nextInt(stringLength);
				
				//make copy of the ith string
				int[] copy = new int[stringLength];
				for (int k = 0; k < stringLength; k++)
				{
					copy[k] = population[i][k];
				}
				
				//cross the jth string with the ith string
				for (int k = cut; k < stringLength; k++)
				{
					swap(population[i][k], population[j][k], population[i]);
				}
				
				//cross ith string with jth string
				for (int k = cut; k < stringLength; k++)
				{
					swap(copy[k], population[j][k], population[j]);
				}
				
			}
		}
	}

	/***********************************************************************************/
	
	//method swaps two values in a string
	private void swap(int a, int b, int[] string)
	{
		//find location of one value
		int i;
		for (i = 0; i < stringLength; i++)
		{
			if (string[i] == a)
			{
				break;
			}
		}
		
		//find location of other value
		int j;
		for (j = 0; j < stringLength; j++)
		{
			if (string[j] == b)
			{
				break;
			}
		}
		
		//swap two values
		int temp = string[i];
		string[i] = string[j];
		string[j] = temp;
	}
	
	/***********************************************************************************/

	//method performs mutation operation (app specific)
	private void mutate()
	{
		//go through each value of each string
		for (int i = 0; i < populationSize; i++)
		{
			for (int j = 0; j < stringLength; j++)
			{
				//if mutation is performed
				if (random.nextDouble() < mutationRate)
				{
					//select another value in the string
					int k = random.nextInt(stringLength);
					
					//swap the two values
					int temp = population[i][j];
					population[i][j] = population[i][k];
					population[i][k] = temp;
				}
			}
		}
	}

	/***********************************************************************************/

	//method performs reproduction operation
	private void reproduce()
	{
		//find the fitness value of all strings
		computeFitnessValues();
		
		//create and array for next generation
		int [][] nextGeneration = new int[populationSize][stringLength];
		
		//repeat population number of times
		for (int i = 0; i < populationSize; i++)
		{
			//select a string from current generation based on fitness
			int j = select();
			
			//copy selected string to next generation
			for (int k = 0; k < stringLength; k++)
			{
				nextGeneration[i][k] = population[j][k];
			}
		}
		
		//next generation becomes current generation
		for (int i = 0; i < populationSize; i++)
		{
			for (int j = 0; j < stringLength; j++)
			{
				population[i][j] = nextGeneration[i][j];
			}
		}
	}

	/***********************************************************************************/

	//method computes fitness values of all strings
	private void computeFitnessValues()
	{
		//compute fitness values of strings
		for (int i = 0; i < populationSize; i++)
		{
			fitnessValues[i] = fitness(population[i]);
		}
		
		//accumulate fitness values
		for (int i = 1; i < populationSize; i++)
		{
			fitnessValues[i] = fitnessValues[i] + fitnessValues[i-1];
		}
		
		//normalize accumulated fitness values
		for (int i = 0; i < populationSize; i++)
		{
			fitnessValues[i] = fitnessValues[i]/fitnessValues[populationSize-1];
		}
	}

	/***********************************************************************************/

	//method selects a string based on fitness values
	private int select()
	{
		//create a random number between 1 and 0
		double value = random.nextDouble();
		
		//determine on which normalized accumulated fitness value it falls
		int i;
		for (i = 0; i < populationSize; i++)
		{
			if (value <= fitnessValues[i])
			{
				break;
			}
		}
		
		//return the string where the random number fell
		return i;
	}

	/***********************************************************************************/

	//Method find the best solution
	private void solution()
	{
		//compute fitness values of strings
		for (int i = 0; i < populationSize; i++)
		{
			fitnessValues[i] = fitness(population[i]);
		}
		
		//find the string with best fitness value
		int best = 0;
		for (int i = 0; i < populationSize; i++)
		{
			if (fitnessValues[i] > fitnessValues[best])
			{
				best = i;
			}
		}
		
		//display the best string
		display(population[best]);
	}

	/***********************************************************************************/

	//method computes the fitness value of a string (application specific)
	private double fitness(int[] string)
	{
		//compute cycle cost
		double sum = 0;
		for (int i = 0; i < stringLength; i++)
		{
			sum += matrix[string[i]-1][string[(i+1)%stringLength]-1];
		}
		
		//fitness value is number of verts times max edge weight minus cycle cost
		return numberVertices*maximumEdge - sum;
	}

	/***********************************************************************************/

	//method displays a string (application specific)
	private void display(int[] string)
	{
		System.out.print("Cycle: ");
		pW.print("Cycle: ");
		
		//print cycle
		for (int i = 0; i < stringLength; i++)
		{
			System.out.print(string[i] + " ");
			pW.print(string[i] + " ");
		}
		System.out.println(string[0] + " ");
		pW.println(string[0] + " ");
		
		//compute cycle cost
		double sum = 0;
		for (int i = 0; i < stringLength; i++)
		{
			sum += matrix[string[i]-1][string[(i+1)%stringLength]-1];
		}
		
		//print cycle cost
		System.out.println("Length: " + sum);
		pW.println("Length: " + sum);
		
		System.out.println("");
		pW.println("");
	}

	/***********************************************************************************/
}


