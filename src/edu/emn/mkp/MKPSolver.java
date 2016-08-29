package edu.emn.mkp;
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

/**
 * 
 * This class is the place where you should enter your code and from which you can create your own objects.
 * 
 * The method you must implement is solve(). This method is called by the programmer after loading the data.
 * 
 * The MKPSolver object is created by the Main class.
 * The other objects that are created in Main can be accessed through the following MKPSolver attributes: 
 * 	- m_instance :  the Instance object which contains the problem data
 * 	- m_solution : the Solution object to modify. This object will store the result of the program.
 * 	- m_time : the maximum time limit (in seconds) given to the program.
 *  
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2016
 * 
 */
public class MKPSolver {

    // ---------------------------------------------
    // --------------- ATTRIBUTES ------------------
    // ---------------------------------------------

    /**
     * The MKP Solution that will be returned by the program
     */
    private Solution m_solution;

    /** The MKP data. */
    private Instance m_instance;

    /** Time given to solve the problem. */
    private long m_time;

    // --------------------------------------------
    // ------------ GETTERS AND SETTERS -----------
    // --------------------------------------------

    /** @return the problem Solution */
    public Solution getSolution() {
	return m_solution;
    }

    /** @return problem data */
    public Instance getInstance() {
	return m_instance;
    }

    /** @return Time given to solve the problem */
    public long getTime() {
	return m_time;
    }

    /**
     * Initializes the problem solution with a new Solution object (the old one
     * will be deleted).
     * 
     * @param sol : new solution
     */
    public void setSolution(Solution sol) {
	this.m_solution = sol;
    }

    /**
     * Sets the problem data
     * 
     * @param inst : the Instance object which contains the data.
     */
    public void setInstance(Instance inst) {
	this.m_instance = inst;
    }

    /**
     * Sets the time limit (in seconds).
     * 
     * @param time : time given to solve the problem
     */
    public void setTime(long time) {
	this.m_time = time;
    }

    // -------------------------------------
    // -------------- METHODS --------------
    // -------------------------------------

    /**
     * **TODO** Modify this method to solve the problem.
     * 
     * Do not print text on the standard output (eg. using <code>System.out.print()</code> or <code>System.out.println()</code>).
     * This output is dedicated to the result analyzer that will be used to evaluate your code on multiple instances.
     * 
     * You can print using the error output (<code>System.err.print()</code> or <code>System.err.println()</code>).
     * 
     * When your algorithm terminates, make sure the attribute m_solution in this class points to the solution you want to return.
     * 
     * You have to make sure that your algorithm does not take more time than the time limit m_time.
     * 
     * @param time Time allowed (in seconds) to solve the problem.
     * @throws Exception
     *             May return some error, in particular if some vertices index
     *             are wrong.
     */
    public void solve(long time) throws Exception {
	m_time = time;
	long t = System.currentTimeMillis();
	long timeElapsed = 0;

	// Example of initial solution :
	// add objects by natural order if possible
	for (int j=0;j<m_instance.getNbObjects();j++) {
	    if (m_solution.addingPossible(j)) {
		m_solution.addObject(j);
	    }
	}

	timeElapsed = System.currentTimeMillis() - t;

	// Loop for improvements of the solution.
	// Keep 500ms free

	//	long maxTime=  m_time * 1000 - 500;
	//	while (timeElapsed < maxTime) {
	//	    // Your code here			
	//	    timeElapsed = System.currentTimeMillis() - t;
	//	}

	System.err.println("Solving time: " + timeElapsed + " ms");
	// Visualization functions
	m_solution.print(System.err);
	m_solution.printConstraints(System.err);
    }

}
