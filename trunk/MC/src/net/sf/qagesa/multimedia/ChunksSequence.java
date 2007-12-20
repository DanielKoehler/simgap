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
 * ChunksSequence.java
 *
 * Created on 13 March 2007, 13.51 by Giovanni Novelli
 *
 * $Id: ChunksSequence.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.multimedia;

import java.util.Vector;

/**
 * This class is responsible to maintain a sequence of chunks related to a
 * specific transcoding configuration for a movie
 * <p>
 * Each chunk has its own transcoding operation and its information about
 * processing parameters
 * 
 * @author Giovanni Novelli
 * @see net.p2pgrid.qagesa.multimedia.Chunk
 */
public class ChunksSequence extends Vector<Chunk> {
	private Movie movie; // Movie

	private String operation; // Transcoding operation

	private String operationParameters; // Transcoding operation parameters

	private String messageDigest; // MD5 Message Digest used to identify any
									// chunk sequence

	/**
	 * 
	 */
	private static final long serialVersionUID = -6003775323301832113L;

	public ChunksSequence(Movie movie, String operation,
			String operationParameters) {
		this.setMovie(movie);
		this.setOperation(operation);
		this.setOperationParameters(operationParameters);
	}

	/*
	 * 
	 * @return a sequence of transcoded chunks
	 */
	public ChunksSequence transcode() {
		ChunksSequence sequence = new ChunksSequence(this.getMovie(), this
				.getOperation(), this.getOperationParameters());
		int chunks = this.size();
		for (int j = 0; j < chunks; j++) {
			Chunk chunk = this.get(j).transcode();
			sequence.add(chunk);
		}
		return sequence;
	}

        public int getInputSize() {
            int inputSize = 0;
		int chunks = this.size();
		for (int j = 0; j < chunks; j++) {
			Chunk chunk = this.get(j);
			inputSize += chunk.getInputSize();
		}
                return inputSize;
        }
        
        public boolean isTranscoded() {
            boolean size = true;
            boolean transcoded = true;
            boolean result = size && transcoded;
		int chunks = this.size();
		for (int j = 0; j < chunks; j++) {
			Chunk chunk = this.get(j);
                        result = result && (chunk.getInputSize()==chunk.getOutputSize()) && (chunk.getMIPS()==0);
		}
                return result;
        }
        
        public int getOutputSize() {
            int outputSize = 0;
		int chunks = this.size();
		for (int j = 0; j < chunks; j++) {
			Chunk chunk = this.get(j);
			outputSize += chunk.getOutputSize();
		}
                return outputSize;
        }
        
        public int getTotalMIPS() {
            int totalMIPS = 0;
		int chunks = this.size();
		for (int j = 0; j < chunks; j++) {
			Chunk chunk = this.get(j);
			totalMIPS += chunk.getMIPS();
		}
                return totalMIPS;
        }
        
	@Override
	public boolean add(Chunk chunk) {
		boolean ok = super.add(chunk);
                String str = this.getMovie().getName() + " " + this.getAlgorithm();
		String md5 = MD5Checksum.get(str);
		this.setMessageDigest(md5);
		return ok;
	}

	public String toString() {
		String msg = "";
		msg += "SEQUENCE[";
		msg += "(" + this.getMovie() + ", " + "(OPERATION, "
				+ this.getOperation() + "), " + "(PARAMETERS, "
				+ this.getOperationParameters() + ")" + "), ";
		int chunks = this.size();
		for (int j = 0; j < chunks; j++) {
			Chunk chunk = this.get(j);
			if ((j + 1) < chunks) {
				msg += chunk + ", ";
			} else {
				msg += chunk + "]";
			}
		}
		return msg;
	}

	/*
	 * 
	 * @return unique string identifying transcoding algorithm
	 */
	public String getAlgorithm() {
		String algorithm = this.getOperation();
		if (this.getOperation().compareToIgnoreCase("FORMAT") == 0) {
			algorithm += " " + this.getOperationParameters();
		}
		return algorithm;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperationParameters() {
		return operationParameters;
	}

	public void setOperationParameters(String operationParameters) {
		this.operationParameters = operationParameters;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getMessageDigest() {
		return messageDigest;
	}

	public void setMessageDigest(String messageDigest) {
		this.messageDigest = messageDigest;
	}

}
