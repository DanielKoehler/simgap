/*
 ****************************************************************************************
 * Copyright © Giovanni Novelli                                             
 * All Rights Reserved.                                                                 
 ****************************************************************************************
 *
 * Title:        QAGESA Simulator 
 * Description:  QAGESA (QoS Aware Grid Enabled Streaming Architecture) Simulator
 *               of a QoS-Aware Architecture for Multimedia Content Provisioning in a GRID Environment
 * License:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * MoviesSet.java
 *
 * Created on 14 March 2007, 14.08 by Giovanni Novelli
 *
 * $Id: MoviesSet.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.multimedia;

import java.util.HashMap;

import net.sf.gap.distributions.Uniform_int;

import com.csvreader.CsvReader;

/**
 * This class is responsible to provide random and filtered Movie instances for
 * their dissemination in the Grid
 * 
 * <p>
 * 
 * 
 * @author Giovanni Novelli
 */
public class MoviesSet extends HashMap<String, Movie> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4650754191631223322L;

	private Uniform_int random;

	public MoviesSet() {
		super();
		this.random = new Uniform_int("random_ms");
	}

	public MoviesSet(String csvname) {
		this();
		this.csvRead(csvname);
	}

	public int getStorageSize() {
		int storageSize = 0;
		int movies = this.values().size();
		for (int j = 0; j < movies; j++) {
			Movie movie = (Movie) this.values().toArray()[j];
			storageSize += movie.getTotalSize();
		}
		return storageSize;
	}

	public String toString() {
		String msg = "";
		int movies = this.values().size();
		msg += "MOVIESET{";
		for (int j = 0; j < movies; j++) {
			Movie movie = (Movie) this.values().toArray()[j];
			if ((j + 1) < movies) {
				msg += movie + ", ";
			} else {
				msg += movie + "}";
			}
		}
		return msg;
	}

	public void csvRead(String csvname) {
		try {
			CsvReader reader = new CsvReader(csvname, ';');

			reader.readHeaders();

			while (reader.readRecord()) {
				String strName = reader.get("Name");
				String strFormat = reader.get("Format");
				String strTotalDuration = reader.get("TotalDuration");
				String strTotalSize = reader.get("TotalSize");

				int iTotalDuration = Integer.parseInt(strTotalDuration);
				int iTotalSize = Integer.parseInt(strTotalSize);
				Movie movie = new Movie(strName, strFormat, iTotalDuration,
						iTotalSize);
				this.put(strName, movie);
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Movie selectRandomMovie() {
		Movie movie = (Movie) (this.values().toArray()[this.random.sample(this
				.size())]);
		return movie;
	}
}
