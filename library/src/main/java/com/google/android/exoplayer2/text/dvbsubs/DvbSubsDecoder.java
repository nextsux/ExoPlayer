package com.google.android.exoplayer2.text.dvbsubs;

import android.util.Log;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

import java.util.Arrays;
import java.util.List;

public final class DvbSubsDecoder extends SimpleSubtitleDecoder {
    private final String TAG = "DVBSubs Decoder";

    private int subtitleSubtype;
    private int subtitleCompositionPage;
    private int subtitleAncillaryPage;
    private  String subtitleContainer;

    private int flags = 0;

    DvbSubtitlesParser parser;

    public DvbSubsDecoder() {
        super("dvbsubs");
        parser = new DvbSubtitlesParser();
    }

    public DvbSubsDecoder(List<byte[]> initializationData) {
        super("dvbsubs");

        Log.v("DVBSUB", "DvbSubsDecoder");

        byte[] tempByteArray;

        tempByteArray = initializationData.get(0);
        subtitleSubtype = tempByteArray != null ? tempByteArray[0] & 0xFF: -1;

        tempByteArray = initializationData.get(3);
        if (tempByteArray != null ) {
            subtitleContainer = Arrays.toString(tempByteArray);
            if (subtitleContainer.equals("mkv")) {
                flags |= DvbSubtitlesParser.FLAG_PES_STRIPPED_DVBSUB;
            }
        }

        if ((tempByteArray = initializationData.get(1)) != null) {
            this.subtitleCompositionPage = ((tempByteArray[0] & 0xFF) << 8) | (tempByteArray[1] & 0xFF);
            if ((tempByteArray = initializationData.get(2)) != null) {
                this.subtitleAncillaryPage = ((tempByteArray[0] & 0xFF) << 8) | (tempByteArray[1] & 0xFF);
                parser = new DvbSubtitlesParser(this.subtitleCompositionPage, this.subtitleAncillaryPage, flags);
            }
        } else {
            parser = new DvbSubtitlesParser();
        }

    }

    @Override
    protected DvbSubsSubtitle decode(byte[] data, int length) {
        return new DvbSubsSubtitle(parser.dvbSubsDecode(data, length));
    }
}