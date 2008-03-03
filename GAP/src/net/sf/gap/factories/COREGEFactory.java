/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        GAP Simulator
 * Description:  GAP (Grid Agents Platform) Toolkit for Modeling and Simulation
 *               of Mobile Agents on Grids
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * COREGEFactory.java
 *
 * Created on 7 August 2006, 14.09 by Giovanni Novelli
 *
 * $Id$
 *
 */

package net.sf.gap.factories;

import gridsim.Machine;
import gridsim.MachineList;
import gridsim.PE;
import gridsim.PEList;
import gridsim.ResourceCalendar;
import gridsim.ResourceCharacteristics;
import gridsim.datagrid.SimpleReplicaManager;
import gridsim.datagrid.index.TopRegionalRC;
import gridsim.datagrid.storage.HarddriveStorage;
import gridsim.datagrid.storage.Storage;
import gridsim.net.Link;

import java.util.Calendar;
import java.util.LinkedList;

import net.sf.gap.distributions.Uniform_int;
import net.sf.gap.grid.components.GridElement;

/**
 * 
 * @author Giovanni Novelli
 */
public class COREGEFactory extends ResourceFactory {
	private TopRegionalRC topRegionalRC;

	private int GBMin;

	private int GBMax;

	private int MIPS; // MIPS rating of single processing elements

	private int PEmax; // Maximum number of processing elements per machine

	private int Mmin; // Minimum number of machines

	private int Mmax; // Maximum number of machines

	private Uniform_int rand; // Random numbers generator

	/** Creates a new instance of CEFactory */
	public COREGEFactory(TopRegionalRC rc) {
		this.setTopRegionalRC(rc);
		this.setMIPS(500);
		this.setPEmax(1);
		this.setMmin(1);
		this.setMmax(1);
		this.setGBMin(100);
		this.setGBMax(100);
		this.setRand(new Uniform_int("GEFactory"));
	}

	/** Creates a new instance of CEFactory */
	public COREGEFactory(TopRegionalRC rc, int MIPS, int PEMax, int MMin,
			int MMax, int GBMin, int GBMax) {
		this.setTopRegionalRC(rc);
		this.setMIPS(MIPS);
		this.setPEmax(PEMax);
		this.setMmin(MMin);
		this.setMmax(MMax);
		this.setGBMin(GBMin);
		this.setGBMax(GBMax);
		this.setRand(new Uniform_int("GEFactory"));
	}

	@Override
	@SuppressWarnings("unchecked")
	public GridElement create(boolean fixed, int geIndex, Link link,
			boolean isSE) {
		MachineList mList = new MachineList();
		Uniform_int r = this.getRand();
		int np = 0;
		int m = 0;
		if (!isSE) {
			int wm = (this.getMmax() - this.getMmin() + 1);
			m = r.sample(wm) + this.getMmin(); // Number of machines in the CE
			if (fixed) {
				m = this.getMmin();
			}
			for (int i = 0; i < m; i++) {
				PEList peList = new PEList();
				int MIPSRating = this.getMIPS();
                                /*
				int p = r.sample(this.getPEmax()) + 1;
				if (fixed) {
					p = this.getPEmax();
				}
                                 */
				int p = this.getPEmax();
				np = np + p;
				for (int j = 0; j < p; j++) {
					peList.add(new PE(j, MIPSRating)); // store PE id and MIPS
					// Rating
				}
				mList.add(new Machine(i, peList)); // Machine i
			}
		} else {
			@SuppressWarnings("unused")
			int wm = (this.getMmax() - this.getMmin() + 1);
			m = 1; // Number of machines in the SE
			for (int i = 0; i < m; i++) {
				PEList peList = new PEList();
				int MIPSRating = this.getMIPS();
				int p = 1;
				np = np + p;
				for (int j = 0; j < p; j++) {
					peList.add(new PE(j, MIPSRating)); // store PE id and MIPS
					// Rating
				}
				mList.add(new Machine(i, peList)); // Machine i
			}
		}

		String arch = "System Architecture"; // system architecture
		String os = "Operating System"; // operating system
		double time_zone = 12.0; // time zone this resource located
		double cost = 1.0; // the cost of using this resource

		ResourceCharacteristics resConfig = new ResourceCharacteristics(arch,
				os, mList, ResourceCharacteristics.SPACE_SHARED, time_zone,
				cost);

		// 7. Finally, we need to create a GridResource object.
		String name = null;
		if (isSE) {
			name = "SE_" + geIndex; // SE name
		} else {
			name = "CE_" + geIndex; // CE name
		}
		long seed = 11L * 13 * 17 * 19 * 23 + 1;
		double peakLoad = 0.0; // the resource load during peak hour
		double offPeakLoad = 0.0; // the resource load during off-peak hr
		double holidayLoad = 0.0; // the resource load during holiday
		// incorporates weekends so the grid resource is on 7 days a week
		LinkedList<Integer> Weekends = new LinkedList<Integer>();
		Weekends.add(new Integer(Calendar.SATURDAY));
		Weekends.add(new Integer(Calendar.SUNDAY));
		// incorporates holidays. However, no holidays are set in this example
		LinkedList Holidays = new LinkedList();
		GridElement ge = null;
		try {
			// create the resource calendar
			ResourceCalendar cal = new ResourceCalendar(time_zone, peakLoad,
					offPeakLoad, holidayLoad, Weekends, Holidays, seed);

			// create the replica manager

			SimpleReplicaManager rm = new SimpleReplicaManager("RM_" + name,
					name); // create a storage
			ge = new GridElement(name, link, resConfig, cal, rm);
			if (isSE) {
				ge.setSE(true);
				int ws = (this.getGBMax() - this.getGBMin() + 1);
				int s = r.sample(ws) + this.getGBMin(); // Size in GB
				Storage storage = new HarddriveStorage("HD_" + ge.get_name(),
						s * 1000);
				ge.addStorage(storage);
			} else {
				ge.setSE(false);
			}
			ge.createLocalRC();
			ge.setHigherReplicaCatalogue(TopRegionalRC.DEFAULT_NAME);
			ge.setNumPE(np);
			ge.setNumWN(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ge;
	}

	public int getGBMin() {
		return this.GBMin;
	}

	public void setGBMin(int GBMin) {
		this.GBMin = GBMin;
	}

	public int getGBMax() {
		return this.GBMax;
	}

	public void setGBMax(int GBMax) {
		this.GBMax = GBMax;
	}

	public int getMIPS() {
		return this.MIPS;
	}

	public void setMIPS(int MIPS) {
		this.MIPS = MIPS;
	}

	public int getPEmax() {
		return this.PEmax;
	}

	public void setPEmax(int PEmax) {
		this.PEmax = PEmax;
	}

	public int getMmin() {
		return this.Mmin;
	}

	public void setMmin(int Mmin) {
		this.Mmin = Mmin;
	}

	public int getMmax() {
		return this.Mmax;
	}

	public void setMmax(int Mmax) {
		this.Mmax = Mmax;
	}

	public Uniform_int getRand() {
		return this.rand;
	}

	public void setRand(Uniform_int rand) {
		this.rand = rand;
	}

	public TopRegionalRC getTopRegionalRC() {
		return this.topRegionalRC;
	}

	public void setTopRegionalRC(TopRegionalRC topRegionalRC) {
		this.topRegionalRC = topRegionalRC;
	}
}
