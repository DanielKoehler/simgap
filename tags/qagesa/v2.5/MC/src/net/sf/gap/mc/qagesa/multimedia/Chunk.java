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
 * $Id$
 *
 */

package net.sf.gap.mc.qagesa.multimedia;

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

	private long inputSize; // Input size of chunk

	private long outputSize; // Output size of chunk after transcoding

	private long MIPS; // MIPS required for transcoding

	private int duration; // Duration of chunk reproduction in milliseconds
        
        private double quality;

	public Chunk() {
	}

	public Chunk(int sequenceNumber, long inputSize, long outputSize, long MIPS,
			int duration, double quality) {
		this.setSequenceNumber(sequenceNumber);
		this.setInputSize(inputSize);
		this.setOutputSize(outputSize);
		this.setMIPS(MIPS);
		this.setDuration(duration);
		this.setTranscoded(false);
                this.setQuality(quality);
	}

	@Override
	public Chunk clone() {
		return new Chunk(this.getSequenceNumber(), this.getInputSize(), this
				.getOutputSize(), this.getMIPS(), this.getDuration(), this.getQuality());
	}

	/**
	 * 
	 * @return a new chunk with input size equal to outputsize and MIPS 0
	 */
	public Chunk transcode(double quality) {
		Chunk chunk = new Chunk(this.getSequenceNumber(), Math.round(this.getOutputSize()*quality*quality),
				Math.round(this.getOutputSize()*quality*quality), Math.round(this.getMIPS()*quality), this.getDuration(), quality);
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

	public long getInputSize() {
		return inputSize;
	}

	public void setInputSize(long inputSize) {
		this.inputSize = inputSize;
	}

	public long getOutputSize() {
		return outputSize;
	}

	public void setOutputSize(long outputSize) {
		this.outputSize = outputSize;
	}

	public long getMIPS() {
		return MIPS;
	}

	public void setMIPS(long MIPS) {
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

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }
}
