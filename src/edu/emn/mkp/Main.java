/*
	mkp-framework
	Copyright (C) 2013 Fabien Lehuede / Damien Prot / Axel Grimault

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License along
	with this program; if not, write to the Free Software Foundation, Inc.,
	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package edu.emn.mkp;

import java.io.IOException;

/**
 * This class contains the Main function, that is the function that is launched
 * when the program is run.
 * 
 * DO NOT WRITE OR MODIFY THIS CLASS.
 * 
 * Read the description of the Main method below, it describes how to set some
 * parameters for the program (such as the filename of the problem to solve).
 * 
 * When the program is run, the input data file is loaded in an Instance object.
 * A MKPSolver object is then created, given the Instance object, and its solve
 * method MKPSolver::solve is called. This is where you have to code your
 * algorithms.
 * 
 * The class to be modified is MKPSolver, from where you may also create your
 * own classes.
 * 
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2016
 * 
 */
public class Main {

    /**
     * Main method. DO NOT MODIFY THIS METHOD.
     * 
     * The parameters of the java program are described below: 
     * **command**: java Main [options] datafile
     * **Options**:
     *  - -help :prints this parameter description
     *  - -t (int) :maximum number of seconds given to the algorithm
     *  - -v :trace level (print the solution at the end if true) and the value of the constraints
     * 
     * **Program output**: 
     * fileName;routeLength;time;e
     * e is an error code:
     *  - e = 0 -> the solution is feasible and returned within the time limit
     *  - e = 1 -> unfeasible solution
     *  - e = 2 -> overtime.
     * 
     * @param arg 
     *            program parameters.
     */
    public static void main(String[] arg) {
	String filename = null;
	long max_time = 30;
	boolean verbose = false;
	boolean graphical = false;

	// Parse command line
	for (int i = 0; i < arg.length; i++) {
	    if (arg[i].compareTo("-help") == 0) {
		System.err.println("The Multidimensional Knapsack Problem");
		System.err.println("Program parameters:");
		System.err.println("command: java Main [options] dataFile");
		System.err.println("Options:");
		System.err.println("\t-help\t: prints this parameter description");
		System.err.println("\t-t\t\t: maximum number of seconds given to the algorithm (int)");
		System.err.println("\t-v\t\t: trace level,print the solution and the constraints");
		return;

	    } else if (arg[i].compareTo("-v") == 0) {
		verbose = true;
		graphical = true;
	    } else if (arg[i].compareTo("-t") == 0) {
		try {
		    max_time = Integer.parseInt(arg[++i]);
		} catch (Exception e) {
		    System.out
		    .println("Error: The time given for -t is not a valid integer value.");
		    System.exit(1);
		}
	    } else {
		if (filename != null) {
		    System.err.println("Error: There is a problem in the program parameters.");
		    System.err.println("Value " + arg[i] + " is not a valid parameter.");
		    System.exit(1);
		}
		filename = arg[i];
	    }
	}

	if ((filename == null) || (filename.equals("")) ) {
	    System.err.println("Error : you must specify a filename of an instance as parameter.");
	    System.exit(1);
	}

	// Create and solve problem
	try {

	    // Create a new problem
	    MKPSolver mkp = new MKPSolver();
	    // Read data
	    Instance prob = new Instance(filename);
	    mkp.setInstance(prob);
	    mkp.setSolution(new Solution(prob));

	    // Solve the problem
	    long t = System.currentTimeMillis();
	    mkp.solve(max_time);
	    t = System.currentTimeMillis() - t;

	    // Evaluate the solution (and check whether it is feasible)
	    boolean feasible = mkp.getSolution().validate();
	    mkp.getSolution().evaluate();

	    int e = 0;
	    if (!feasible) {
		e = 1;
	    } else {
		if (t > (max_time + 1) * 1000) {
		    e = 2;
		    System.err.println("Error: Time limit exeeced !!!");
		}
	    }
	    System.out.println(filename + ";"+ mkp.getSolution().getObjective() + ";" + t + ";" + e);

	    // If verbose, print the solution
	    if (verbose) {
		mkp.getSolution().print(System.err);
		if (e == 1)
		    System.err.println("Error: There is an error in the solution: "+ mkp.getSolution().getError());
	    }

	    // If graphical visualization, print the value of the constraints
	    if (graphical) {
		System.err.println("******* Value of constraints ********");
		mkp.getSolution().printConstraints(System.err);
	    }

	} catch (IOException e) {
	    System.err.println("Error: An error has been met when reading the input file: " + e.getMessage());
	    System.exit(1);
	} catch (Exception e) {
	    System.err.printf("Error: %s", e.getMessage());
	    System.err.println();
	    e.printStackTrace(System.err);
	    System.exit(1);
	}
	return;

    }
}
