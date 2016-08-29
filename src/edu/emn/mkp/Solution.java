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

import java.io.PrintStream;
import java.util.Arrays;

/**
 * 
 * This class models a MKP Solution.
 * 
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2016
 * 
 */
public class Solution {

    // ---------------------------------------------
    // --------------- ATTRIBUTES ------------------
    // ---------------------------------------------

    /**
     * Solution stored in an array. Element j in the array is:
     *  - <code>true</code> object j is in the solution;
     *  - <code>false</code> object j is not in the solution.
     *  
     * @warning Do not modify the behaviour of this attribute
     */
    protected boolean[] m_solution;

    /**
     * Value of the knapsack.
     * @warning Maintain this value correct while you modify the solution (by your own means or with {@link #evaluate()})/
     * <br> Be careful : full computation is time consuming.
     */
    protected long m_objective = 0;

    /** 
     * Sum of the weights of the objects in the knapsack following each dimension.
     * For a solution, the computation of each weight is performed in {@link #validate()}.
     * 
     * <b> For computational requirement, update this attribute iteratively with method {@link #setWeight(int, long)}.
     * For further examples, see {@link #addObject(int)} or {@link #removeObject(int))} </b>.  
     */
    protected long[] m_weights;

    /** Data of the problem associated with the solution */
    protected Instance m_instance;

    /** Number of objects in the problem. */
    protected int m_nbObjects;

    /** Number of constraints in the problem. */
    protected int m_nbConstraints;

    /** Error code returned by <code>validate</code> */
    protected String m_error = "";

    // --------------------------------------------
    // ------------ GETTERS AND SETTERS -----------
    // --------------------------------------------
    
    /**
     * @return Array of boolean representing the solution.
     */
    public boolean[] getSolution() {
	return m_solution;
    }

    /**
     * @return Value of the knapsack. 
     */
    public long getObjective() {
	return m_objective;
    }

    /**
     * Sets the cost of the solution to the value newVal.
     * @param newVal : new solution cost
     */
    public void setObjective(long newVal) {
	this.m_objective = newVal;
    }

    /**
     * @param i The dimension selected.
     * @return The weight of the objects in the knapsack for dimension i.
     */
    public long getWeight(int i) {
	return m_weights[i];
    }

    /**
     * Set the weight of the knapsack for a selected dimension.
     * @param i The dimension selected.
     * @param newWeight The new weight of the knapsack for dimension i.
     */
    public void setWeight(int i, long newWeight) {
	m_weights[i] = newWeight;
    }

    /**
     * @return Returns a pointer to the data of the problem associated with the
     *         solution.
     */
    public Instance getInstance() {
	return m_instance;
    }

    /**
     * @param j Object for whom one want to know if it is selected or not.
     * @return <code>true</code> if the object is selected <code>false</code> otherwise.
     * @throws Exception Throw an exception if index of object is wrong.
     */
    public boolean isSelected(int j) throws Exception {
	if ((j < 0) || (j > m_nbObjects))
	    throw new Exception("Error: " + j + " n\'is not an index of object between 0 and " + (m_nbObjects - 1));
	return m_solution[j];
    }

    /**
     * @return Error code returned by <code>validate</code>
     */
    public String getError() {
	return m_error;
    }

    // -------------------------------------
    // ------------ CONSTRUCTOR ------------
    // -------------------------------------

    /**
     * Creates an object of the class Solution for the problem data loaded in
     * <code>inst</code>.
     */
    public Solution(Instance inst) {
	m_instance = inst;
	m_nbObjects = inst.getNbObjects();
	m_nbConstraints = inst.getNbConstraints();
	m_solution = new boolean[m_nbObjects];
	Arrays.fill(m_solution, false);
	m_weights = new long[m_instance.getNbConstraints()];
	Arrays.fill(m_weights, 0);
    }

    // -------------------------------------
    // -------------- METHODS --------------
    // -------------------------------------


    /** 
     * Overload of method <code>clone</code> of class <code>Object</code>. 
     * @return A copy of the solution.
     */
    public Solution clone() {
	Solution solution = new Solution(m_instance);
	solution.m_nbObjects = m_instance.getNbObjects();
	solution.m_nbConstraints = m_instance.getNbConstraints();
	solution.m_objective = m_objective;
	solution.m_solution = Arrays.copyOf(m_solution, m_nbObjects);
	solution.m_weights = Arrays.copyOf(m_weights,m_instance.getNbConstraints());
	solution.m_error = new String(m_error); 
	return solution;	  
    }

    /**
     * Set the weight of object j to value.
     * 
     * @warning This method does not modify the weights of each dimension. Theses updates need to be perform with
     * the corresponding methods.
     * 
     * @param j index of object
     * @param value <code>true</code> or <code>false</code>
     * @throws Exception Throw an exception if index j is not a valid object.
     */
    public void setObjectWeight(int j, boolean value) throws Exception {
	if ((j < 0) || (j >= m_nbObjects))
	    throw new Exception("Error: " + j + " n\'is not an index of object between 0 and " + (m_nbObjects - 1));
	m_solution[j] = value;
    }

    /**
     * Add object j to the knapsack (do nothing if object is already in the knapsack).
     * Update the objective value and the weights on each dimension
     * 
     * @param j Index of object to add.
     * @throws Exception Throw an exception if index j is not a valid object.
     */
    public void addObject(int j) throws Exception {
	if ((j < 0) || (j >= m_nbObjects))
	    throw new Exception("Error: " + j + " n\'is not an index of object between 0 and " + (m_nbObjects - 1));

	if (m_solution[j] == false ) {
	    m_solution[j] = true;		

	    m_objective += m_instance.getValue(j);

	    for (int i = 0; i < m_instance.getNbConstraints(); i++) {
		m_weights[i] += m_instance.getWeight(j, i);
	    }
	}
    }

    /**
     * Remove object j to the knapsack (do nothing if object is already in the knapsack).
     * Update the objective value and the weights on each dimension
     * 
     * @param j Index of object to remove.
     * @throws Exception Throw an exception if index j is not a valid object. 
     */
    public void removeObject(int j) throws Exception {
	if ((j < 0) || (j > +m_nbObjects))
	    throw new Exception("Error: " + j + " n\'is not an index of object between 0 and " + (m_nbObjects - 1));

	if (m_solution[j] == true ) {
	    m_solution[j] = false;		

	    m_objective -= m_instance.getValue(j);

	    for (int i = 0; i < m_instance.getNbConstraints(); i++) {
		m_weights[i] -= m_instance.getWeight(j, i);
	    }
	}
    }

    /**
     * Check if adding object j violate or not the constraints.
     * 
     * @param j Index of object to remove.
     * @return <code>true</code>if adding is possible or <code>false</code>
     * @throws Exception Throw an exception if index j is not an object. 
     */
    public boolean addingPossible(int j) throws Exception {
	if ((j < 0) || (j > m_nbObjects))
	    throw new Exception("Error: " + j + " n\'is not an index of object between 0 and " + (m_nbObjects - 1));

	for (int i = 0; i < m_nbConstraints; i++) {
	    if ( m_weights[i] + m_instance.getWeight(j, i) > m_instance.getCapacity(i))
		return false;
	}

	return true;
    }

    /**
     * Check if solution is feasible or not (capacity constraints not violated).
     * @warning This method do not compute the weights.

     * @return <code>true</code> if feasible or <code>false</code>.
     */
    public boolean isFeasible() {

	try {
	    for (int i = 0; i < m_nbConstraints; i++) {
		if ( m_weights[i]  > m_instance.getCapacity(i))
		    return false;
	    }
	} catch (Exception e) {
	    // Do nothing, error impossible
	}
	return true;
    }

    /**
     * Compute the value of the knapsack.
     * Update attribute <code>m_objective</code>.
     * @return The value of the knapsack.
     * @throws Exception
     */ 
    public double evaluate() throws Exception {
	m_objective = 0;
	for (int j = 0; j < m_nbObjects; j++) {
	    if (m_solution[j]) {
		m_objective += m_instance.getValue(j);
	    }
	}
	return m_objective;
    }

    /**
     * Compute the value of constraint i base on actual solution
     * (i.e. the weight of object j is add to the value of the constraint if object j is in the actual solution).
     * 
     * Update attribute <code>m_weights</code>/
     * 
     * @param i Index of the constraint.
     * @throws Exception Throw an exception if index i is not a valid constraint.
     */
    public int computeValueConstraint(int i) throws Exception {
	if ((i < 0) || (i >= m_nbConstraints))
	    throw new Exception("Error: " + i + " n\'is not an index of constraint between 0 and " + (m_nbConstraints - 1));

	int valueConstraint = 0;
	for (int j = 0; j < m_nbObjects; j++) {
	    if (m_solution[j])
		valueConstraint += m_instance.getWeight(j, i);
	}

	m_weights[i] = valueConstraint;
	return valueConstraint;
    }

    /**
     * Check that the solution is feasible for MKP.
     * Tests are the following :
     *  - compute the value of the knapsack (<code>evaluate</code>);
     *  - check that each constraint is not violated.
     *  
     * Error messages are available with the method <code>getError()</code>.
     * 
     * @return Return <code>true</code> if solution if feasible, <code>false</code> otherwise.
     * @throws Exception
     */
    public boolean validate() throws Exception {
	boolean result = true;
	m_error = "";

	for (int i = 0; i < m_nbConstraints; i++) {
	    if (computeValueConstraint(i) > m_instance.getCapacity(i)) {
		m_error += "Error: constraint " + i + " is violated.\n";
		result = false;
	    }
	}

	if (result)
	    m_error += "Solution is feasible for MKP.";
	else
	    m_error += "Solution is unfeasible for MKP.";
	return result;
    }

    /**
     * Print the solution on output out.
     * 
     * @param out Output.
     */
    public void print(PrintStream out) {
	out.println("MKP objective value: " + m_objective + "\nObjects chosen: ");
	for (int j = 0; j < m_nbObjects; j++) {
	    if (m_solution[j])
		out.print(j + " / ");
	}
	out.println("");
    }

    /**
     * Print the value of constraints on output out.
     * 
     * @param out Output.
     * @throws Exception
     */
    public void printConstraints(PrintStream out) throws Exception {
	for (int i = 0; i < m_nbConstraints; i++) {
	    out.print("Constraint " + i + ": ");
	    long initialCapacity = m_instance.getCapacity(i);
	    long remainingCapacity = initialCapacity - m_weights[i];
	    out.println(m_weights[i] + " <= " + initialCapacity + " / Remaining = " + remainingCapacity);
	}
    }

}
