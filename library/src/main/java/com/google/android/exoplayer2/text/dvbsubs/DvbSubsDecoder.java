package com.google.android.exoplayer2.text.dvbsubs;

import com.google.android.exoplayer2.text.SimpleSubtitleDecoder;

public final class DvbSubsDecoder extends SimpleSubtitleDecoder {
    private final String TAG = "DVBSubs Decoder";

    DvbSubtitlesParser parser;

    public DvbSubsDecoder() {
        super("dvbsubs");
        parser = new DvbSubtitlesParser();
    }

    @Override
    protected DvbSubsSubtitle decode(byte[] data, int length) {
        return new DvbSubsSubtitle(parser.dvbSubsDecode(data, length));
    }
}