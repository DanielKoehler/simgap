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
 * Chunk.java
 *
 * Created on 13 March 2007, 13.51 by Giovanni Novelli
 *
 * $Id: Chunk.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.multimedia;

/**
 * This class is responsible to maintain information about the processing of a
 * single chunk
 * <p>
 * In particular it keeps information about:
 * <ul>
 * <li>sequence number in the movie chunks' sequence<li/>
 * <li>input size of the chunk before transcoding<li/>
 * <li>output size of the chunk after transcoding<li/>
 * <li>duration in milliseconds of processing in an ideal processing element<li/>
 * <li>duration of chunk during playing<li/> <ul/>
 * 
 * @author Giovanni Novelli
 */
public class Chunk {
        private boolean transcoded; // Used to mark a chunk as transcoded 
    
	private int sequenceNumber; // Sequence number of chunk in a ChunksSequence

	private int inputSize; // Input size of chunk

	private int outputSize; // Output size of chunk after transcoding

	private int MIPS; // MIPS required for transcoding

	private int duration; // Duration of chunk reproduction in milliseconds

	public Chunk() {
	}

        public Chunk(int sequenceNumber, int inputSize, int outputSize, int MIPS,
			int duration) {
		this.setSequenceNumber(sequenceNumber);
		this.setInputSize(inputSize);
		this.setOutputSize(outputSize);
		this.setMIPS(MIPS);
		this.setDuration(duration);
                this.setTranscoded(false);
	}
        
	@Override
	public Chunk clone() {
		return new Chunk(this.getSequenceNumber(), this.getInputSize(), this.getOutputSize(), this.getMIPS(), this.getDuration());
	}

	/**
	 * 
	 * @return a new chunk with input size equal to outputsize and MIPS 0
	 */
	public Chunk transcode() {
		Chunk chunk = new Chunk(this.getSequenceNumber(), this.getOutputSize(),
				this.getOutputSize(), 0, this.getDuration());
                chunk.setTranscoded(true);
		return chunk;
	}

	public String toString() {
		String msg = "";
		msg += "CHUNK[" + "(NUMBER, " + this.getSequenceNumber() + "), "
				+ "(INPUTSIZE, " + this.getInputSize() + "), "
				+ "(OUTPUTSIZE, " + this.getOutputSize() + "), " + "(MIPS, "
				+ this.getMIPS() + "), " + "(DURATION, " + this.getDuration()
				+ ")" + "]";
		return msg;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getInputSize() {
		return inputSize;
	}

	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	public int getOutputSize() {
		return outputSize;
	}

	public void setOutputSize(int outputSize) {
		this.outputSize = outputSize;
	}

	public int getMIPS() {
		return MIPS;
	}

	public void setMIPS(int MIPS) {
		this.MIPS = MIPS;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

    public boolean isTranscoded() {
        return transcoded;
    }

    public void setTranscoded(boolean transcoded) {
        this.transcoded = transcoded;
    }
}
