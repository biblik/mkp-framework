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
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * The Instance class allows to create an object that contains the data stored
 * in a mkp file. <br>
 * 
 * <br>
 * The class is created through its constructor that takes the data file as
 * parameter. The data file is read and the data are stored in the Instance
 * object. The data can then be access calling the object methods.
 * 
 * @warning Do not modify this class.
 * @author Fabien Lehuede / Damien Prot / Axel Grimault 2016
 * 
 */
public class Instance {

    // ---------------------------------------------
    // --------------- ATTRIBUTES ------------------
    // ---------------------------------------------

    /** Number of items */
    private int m_nbItems;

    /** Number of constraints */
    private int m_nbConstraints;

    /** Profit of each item */
    private Integer[] m_profit;

    /** Capacity of each constraint */
    private Integer[] m_capacity;

    /** Matrix of weight of object j in constraint i */
    private Integer[][] m_weight_ji;

    /** Name of the file corresponding to the instance */
    private String m_fileName;

    // --------------------------------------------
    // ------------ GETTERS AND SETTERS -----------
    // --------------------------------------------

    /** 
     * @return The number of items in the problem
     */
    public int getNbItems() {
	return m_nbItems;
    }

    /** 
     * @return The number of constraints in the problem 
     */
    public int getNbConstraints() {
	return m_nbConstraints;
    }

    /**
     * @param j Index of the item (index must be between 0 and the number of items -1).
     * @return The profit of item j.
     * @throws Exception Throw an exception if index j is not a valid object.
     */
    public int getProfit(int j) throws Exception {
	if ((j < 0) || (j >= m_nbItems))
	    throw new Exception("Error: " + j + " n\'is not an index of item between 0 and " + (m_nbItems - 1));
	return m_profit[j];
    }

    /**
     * @param i Index of the constraint (index must be between 0 and the number of constraints -1).
     * @return The capacity of constraint i.
     * @throws Exception Throw an exception if index i is not a valid constraint.
     */
    public int getCapacity(int i) throws Exception {
	if ((i < 0) || (i >= m_nbConstraints))
	    throw new Exception("Error: " + i + " n\'is not an index of constraint between 0 and " + (m_nbConstraints - 1));
	return m_capacity[i];
    }

    /**
     * @param j Index of the item (index must be between 0 and the number of items -1).
     * @param i Index of the object (index must be between 0 and the number of constraints -1).
     * @return The weight of object j in constraint i.
     * @throws Exception Throw an exception if indices i and j are not valid.
     **/
    public Integer getWeight(int j, int i) throws Exception {
	if ((j < 0) || (j >= m_nbItems))
	    throw new Exception("Error: " + j + " n\'is not an index of item between 0 and " + (m_nbItems - 1));
	if ((i < 0) || (i >= m_nbConstraints))
	    throw new Exception("Error: " + i + " n\'is not an index of constraint between 0 and " + (m_nbConstraints - 1));
	return m_weight_ji[j][i];
    }

    /**
     * @return The matrix of weights.
     */
    public Integer[][] getWeights() {
	return m_weight_ji;
    }

    /**
     * @return The name of the file.
     */
    public String getFileName() {
	return m_fileName;
    }

    // -------------------------------------
    // ------------ CONSTRUCTOR ------------
    // -------------------------------------

    /**
     * Constructor: this method creates an object of class Instance. It calls
     * the read method to load the data file given as parameter.
     * 
     * @param fileName
     *            instance file
     * @throws IOException
     *             Returns an error when a problem is met reading the data file.
     */
    public Instance(String fileName) throws IOException {
	m_fileName = fileName;
	read();
    }

    // -------------------------------------
    // -------------- METHODS --------------
    // -------------------------------------

    /** 
     * Read the instance file
     */
    private void read() throws IOException {

	File mfile = new File(m_fileName);
	if (!mfile.exists()) {
	    throw new IOException("The instance file : " + m_fileName + " does not exist.");
	}
	Scanner sc = new Scanner(mfile);

	String line = sc.nextLine();
	Scanner lineSc = new Scanner(line);
	m_nbItems = lineSc.nextInt();
	m_nbConstraints = lineSc.nextInt();

	lineSc.close();

	line = sc.nextLine();
	lineSc = new Scanner(line);

	// Create profit for each object
	m_profit = new Integer[m_nbItems];
	for (int j = 0; j < m_nbItems; j++) {
	    if (!lineSc.hasNextInt()) {
		line = sc.nextLine();
		lineSc = new Scanner(line);
	    }
	    m_profit[j] = lineSc.nextInt();
	}

	// Create weights matrix
	m_weight_ji= new Integer[m_nbItems][m_nbConstraints];
	// Read the matrix
	for (int i = 0; i < m_nbConstraints; i++) {
	    for (int j = 0; j < m_nbItems; j++) {
		if (!lineSc.hasNextInt()) {
		    line = sc.nextLine();
		    lineSc = new Scanner(line);
		}
		m_weight_ji[j][i] = lineSc.nextInt();
	    }
	}

	// Create capacity for constraints
	m_capacity = new Integer[m_nbConstraints];
	for (int i = 0; i < m_nbConstraints; i++) {
	    if (!lineSc.hasNextInt()) {
		line = sc.nextLine();
		lineSc = new Scanner(line);
	    }
	    m_capacity[i] = lineSc.nextInt();
	}
	sc.close();
	lineSc.close();

    }

    /**
     * Print weights matrix on the output given as a parameter.
     * 
     * @param out : output stream
     */
    public void print(PrintStream out) {

	out.println("Weights matrix :");
	for (int i = 0; i < m_nbConstraints; i++) {
	    for (int j = 0; j < m_nbItems; j++) {
		out.print(m_weight_ji[j][i] + ";");
	    }
	    out.println();
	}
	out.println();
    }

}
