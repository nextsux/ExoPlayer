package com.google.android.exoplayer2.text.dvbsubs;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

import java.util.List;

public final class DvbSubsDecoder extends SimpleSubtitleDecoder {
    private final String TAG = "DVBSubs Decoder";

    private int subtitleSubtype;
    private int subtitleCompositionPage;
    private int subtitleAncillaryPage;

    DvbSubtitlesParser parser;

    public DvbSubsDecoder() {
        super("dvbsubs");
        parser = new DvbSubtitlesParser();
    }

    public DvbSubsDecoder(List<byte[]> initializationData) {
        super("dvbsubs");

        byte[] tempByteArray;

        tempByteArray = initializationData.get(0);
        subtitleSubtype = tempByteArray != null ? tempByteArray[0] & 0xFF: -1;

        if ((tempByteArray = initializationData.get(1)) != null) {
            this.subtitleCompositionPage = ((tempByteArray[0] & 0xFF) << 8) | (tempByteArray[1] & 0xFF);
            if ((tempByteArray = initializationData.get(2)) != null) {
                this.subtitleAncillaryPage = ((tempByteArray[0] & 0xFF) << 8) | (tempByteArray[1] & 0xFF);
                parser = new DvbSubtitlesParser(this.subtitleCompositionPage, this.subtitleAncillaryPage);
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