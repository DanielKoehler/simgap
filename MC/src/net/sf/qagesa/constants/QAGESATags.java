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
 * QAGESATags.java
 *
 * Created on 8 August 2006, 22.01 by Giovanni Novelli
 *
 * $Id: QAGESATags.java 1142 2007-07-18 20:49:58Z gnovelli $
 *
 */

package net.sf.qagesa.constants;

import net.sf.gap.constants.Tags;

/**
 * 
 * @author Giovanni Novelli
 */
public class QAGESATags extends Tags {

        public static final int RESPONSE_TIME_REQ = 2001;
        public static final int RESPONSE_TIME_REP = 2002;
        
	// ReF Service
	public static final int REF_PLAY_REQ      = 2100; // From USER to ReF

	public static final int REF_PLAY_START_REP        = 2101; // From ReF to USER

	public static final int REF_PLAY_END_REP     = 2102; // From ReF to USER

        // MuM Service
	public static final int MUM_SEARCH_REQ = 2110; // From ReF to MuM

	public static final int MUM_SEARCH_REP = 2111; // From MuM to ReF

	// CEL Service
	public static final int CEL_SEARCH_REQ = 2120; // From ReF to CEL

	public static final int CEL_SEARCH_REP = 2121; // From CeL to ReF

	// AL Service
	public static final int AL_SEARCH_REQ = 2130; // From ReF to AL

	public static final int AL_SEARCH_REP = 2131; // From AL to ReF

	public static final int AL_UPDATE_REQ = 2132; // From ReF to AL

	public static final int AL_UPDATE_REP = 2133; // From AL to ReF

	// TA (Transcoding Agent)
	// Getting a chunk of a movie from the SE containing the movie
	public static final int GET_CHUNK_REQ = 3201; // From TA to

	public static final int GET_CHUNK_REP = 3202; // From QAGESAGridElement of

	// Sending chunk to SE
	public static final int SENDING_FIRST_CHUNK_REQ = 3210;     // From TA to USER
	public static final int TRANSCODED_FIRST_CHUNK_REQ = 3211;  // From TA to USER

        public static final int SEND_CHUNK_REQ = 3212;              // From TA to USER

	public static final int SEND_CHUNK_REP = 3213;              // From USER to TA

	public static final int SENT_LAST_CHUNK_REQ  = 3214;        // From TA to USER

	// Caching chunks to SE
	public static final int CACHE_CHUNKS_REQ = 3221;            // From TA to
													// QAGESAGridElement of type
	/*
	 * 
	 * TRANSCODE_CHUNKS_REQ tag is used to activate a transcoding agent on a
	 * specific configuration Configuration is specified as: - User requesting
	 * the transcoded stream - The movieTag that identifies the movie and its
	 * whole transcoding sequence - Storage Element containing the movie
	 * 
	 * Note: - In order to simulate transcoded chunks caching on SEs the
	 * original chunks sequence should be updated with: -- input size equal to
	 * original output size -- output size left unaltered -- processing cost
	 * equal to 0 MIPS - When getting a chunks sequence with a processing cost
	 * equal to 0 MIPS the transcoding process won't be repeated and is skipped
	 * to get advantage of caching process
	 * 
	 * Transcoding process consists of: - getting a chunks sequence (even with
	 * not continuous sequence numbers) from an SE - processing them (simulating
	 * the submission of gridlets to local CE and obtaining back gridlets
	 * results) - streaming them to the user
	 */
	public static final int TRANSCODE_CHUNKS_REQ = 3231; // From ReF to TA

        public static final int TRANSCODE_CHUNKS_REP = 3232; // From TA to ReF

        /**
	 * Creates a new instance of QAGESATags
	 */
	public QAGESATags() {
	}

	@Override
	public String otherTags(int tag) {
		String str = null;
		switch (tag) {
		// ReF Service
		case QAGESATags.REF_PLAY_REQ:
			str = "REF_PLAY_REQUEST";
			break;

		// CEL Service
		case QAGESATags.CEL_SEARCH_REQ:
			str = "CEL_SEARCH_REQUEST";
			break;
		case QAGESATags.CEL_SEARCH_REP:
			str = "CEL_SEARCH_REPLY";
			break;

		// AL Service
		case QAGESATags.AL_SEARCH_REQ:
			str = "AL_SEARCH_REQUEST";
			break;
		case QAGESATags.AL_SEARCH_REP:
			str = "AL_SEARCH_REPLY";
			break;

		// MuM Service
		case QAGESATags.MUM_SEARCH_REQ:
			str = "MUM_SEARCH_REQUEST";
			break;
		case QAGESATags.MUM_SEARCH_REP:
			str = "MUM_SEARCH_REPLY";
			break;

                // TA (Transcoding Agent)
		case QAGESATags.GET_CHUNK_REQ:
			str = "GET_CHUNK_REQUEST";
			break;
		case QAGESATags.GET_CHUNK_REP:
			str = "GET_CHUNK_REPLY";
			break;

		case QAGESATags.SEND_CHUNK_REQ:
			str = "SEND_CHUNK";
			break;

		case QAGESATags.CACHE_CHUNKS_REQ:
			str = "CACHE_CHUNKS";
			break;

		default:
			str = "UNKNOWN_TAG";
			break;
		}
		return str;
	}
}
