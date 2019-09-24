/*
* Name: Justin Senia
* E-Number: E00851822
* Date: 11/11/2017
* Class: COSC 461
* Project: #3
*/

import java.io.*;
import java.util.*;

public class TravelGenTester
{

	//Main method for testing
	public static void main(String[] args) throws IOException
	{
		//creating buffered reader for getting user input
		java.io.BufferedReader keyIn = new BufferedReader(new InputStreamReader(System.in));

		//message welcoming to the program/giving instructions
		System.out.println("*****************************************");
		System.out.println("*        TSP Genetic Algo program       *");
		System.out.println("*****************************************");
		System.out.println("*  Please enter in a filename to start  *");
		System.out.println("* or type quit to terminate the program *");
		System.out.println("*****************************************");

		//start a loop that continues querying for input as long as user
		//does not enter "quit" (without the quotes)
		while (true)
		{

			String userIn = "";			//used for file entry or quitting

			System.out.print("Please enter a filename for data input: ");
			userIn = keyIn.readLine(); //reading user input

			

			if (userIn.equalsIgnoreCase("quit")) //if user typed quit, stops program
				break;
			else
			{
				try
				{
					
					//creating parameters to be passed to genetic algorithm
					int popSize = 0;			//population size parameter to be passed
					int strLength = 0;			//string length parameter to be passed
					int numIterations = 0;		//number of iterations parameter to be passed
					double crssoverRate = 0;	//crossover rate parameter to be passed
					double mutRate = 0;			//mutation rate parameter to be passed
					int seedVal = 0;			//seed value parameter to be passed
					
					
					//establishing working directory for file I/O
					String currentDir = System.getProperty("user.dir");
					File fIn = new File(currentDir + '\\' + userIn);

					//using scanner with new input file based on user input
					Scanner scanIn = new Scanner(fIn);

					//creating printwriter for file output
					File fOut = new File("output_" + userIn);
					PrintWriter PWOut = new PrintWriter(fOut, "UTF-8");

					//scanning external file for number of verticies in graph
					int numOfVerts = scanIn.nextInt();
					//scanning external file for number of edges in graph
					int numOfEdges = scanIn.nextInt();
					
					
					//reading in parameters to be passed into genetic algorithm
					System.out.print("Please enter an int value for population size (for genetic parameter passing): ");
					popSize = Integer.parseInt(keyIn.readLine()); //reading user input

					//assigning scanned in number of verts as string length, as string length
					//will be reliant on number of Verts to choose from
					strLength = numOfVerts;

					System.out.print("Please enter an int value to indicate number of iterations (for genetic parameter passing): ");
					numIterations = Integer.parseInt(keyIn.readLine()); //reading user input

					System.out.print("Please enter a double value between 0 and 1 for crossover rate (for genetic parameter passing): ");
					crssoverRate = Double.parseDouble(keyIn.readLine()); //reading user input

					System.out.print("Please enter a double value between 0 and 1 for mutation rate(for genetic parameter passing): ");
					mutRate = Double.parseDouble(keyIn.readLine()); //reading user input

					System.out.print("Please enter an int value for seedvalue (for genetic parameter passing): ");
					seedVal = Integer.parseInt(keyIn.readLine()); //reading user input

					//testing statement to see if data was parsed from file correctly
					//System.out.println("test: " + popSize + " " + strLength + " " + numIterations + " " + crssoverRate + " " + mutRate + " " + seedVal);



					//declaring multidimensional array based on number of verts designated by external file
					//this will serve as an adjacency matrix to pass to the genetic algorithm object
					int[][] adjaceMatrix = new int[numOfVerts][numOfVerts];
					
					int tempVertCoord1 = 0; //temporarily holds read in value for vert coord #1
					int tempVertCoord2 = 0; //temporarily holds read in value for vert coord #2
					
					//temporarily holds read in value for edge between tempVertCoord1 & tempVertCoord2
					int tempEdgeValue = 0; 

					//importing item information from external file
					for (int i = 0; i < numOfEdges; i++)
					{
						tempVertCoord1 = scanIn.nextInt();	//get 1st vert name
						tempVertCoord2 = scanIn.nextInt();	//gets 2nd vert name
						tempEdgeValue  = scanIn.nextInt();	//gets edge value

						//assigning values to matrix
						adjaceMatrix[tempVertCoord1-1][tempVertCoord2-1] = 
						adjaceMatrix[tempVertCoord2-1][tempVertCoord1-1] = tempEdgeValue;
					}

					//making sure all locations in adjacency matrix that lack a value
					//have one, in order to ensure complete graph, and accurate results
					//substituting the value of 9999999 for all zero fields in matrix
					//so calculations wont be affected by any lack of edges
					for (int i = 0; i < numOfVerts; i++)
					{
						for (int j = 0; j < numOfVerts; j++)
						{
							if (adjaceMatrix[i][j] == 0)
							{
								adjaceMatrix[i][j] = 9999999;
							}
						}
					}
					
					//for testing to see if adjacency matrix was created successfully
					//for(int i = 0; i < numOfVerts; i++)
					//{
					//	for(int j = 0; j < numOfVerts; j++)
					//	{
					//		System.out.print(adjaceMatrix[i][j] + " ");
					//	}
					//	System.out.println("");
					//}


					//printing related info to console
					System.out.println("***************************************************************");
					System.out.println("            TSP Parameters for File: " + userIn                 );
					System.out.println("***************************************************************");
					System.out.println("*                         Parameters                          *");
					System.out.println("***************************************************************");
					System.out.println(" Population Size:      " + popSize); 
					System.out.println(" String Length:        " + strLength);
					System.out.println(" Number of Iterations: " + numIterations); 
					System.out.println(" Crossover Rate:       " + crssoverRate);
					System.out.println(" Mutation Rate:        " + mutRate);
					System.out.println(" Seed Value:           " + seedVal);
					System.out.println(" Number of Verticies:  " + numOfVerts);
					System.out.println(" Number of Edges:      " + numOfEdges);
					System.out.println("***************************************************************");
					System.out.println("*                            Output                           *");
					System.out.println("***************************************************************");

					//printing related info to file
					PWOut.println("***************************************************************");
					PWOut.println("            TSP Parameters for File: " + userIn                 );
					PWOut.println("***************************************************************");
					PWOut.println("*                         Parameters                          *");
					PWOut.println("***************************************************************");
					PWOut.println(" Population Size:      " + popSize); 
					PWOut.println(" String Length:        " + strLength);
					PWOut.println(" Number of Iterations: " + numIterations); 
					PWOut.println(" Crossover Rate:       " + crssoverRate);
					PWOut.println(" Mutation Rate:        " + mutRate);
					PWOut.println(" Seed Value:           " + seedVal);
					PWOut.println(" Number of Verticies:  " + numOfVerts);
					PWOut.println(" Number of Edges:      " + numOfEdges);
					PWOut.println("***************************************************************");
					PWOut.println("*                            Output                           *");
					PWOut.println("***************************************************************");

					//create TravelGen solver
					TravelGen t = new TravelGen(adjaceMatrix, numOfVerts, PWOut);

					//set parameters of genetic algorithm
					t.setParameters(popSize, strLength, numIterations, crssoverRate, mutRate, seedVal);

					//find the optimal solution 
					t.solve();

					//closing printwriter and scanner objects to maintain file integrity
					scanIn.close();
					PWOut.close();
				}
				catch (IOException e) //catches if there were any fileIO exceptions
				{
					;
				}
			}
		}
	}
}
