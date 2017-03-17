package com.google.android.exoplayer2.extractor.ts;


import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.extractor.ExtractorOutput;
import com.google.android.exoplayer2.extractor.TrackOutput;
import com.google.android.exoplayer2.extractor.ts.TsPayloadReader.TrackIdGenerator;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.ParsableByteArray;

import java.util.ArrayList;
import java.util.List;


public class DvbSubtitlesReader implements ElementaryStreamReader {

    private static final String TAG= "DVBSubsReader";
    private final String language;
    private List<byte[]> initializationData = new ArrayList<>();

    private long sampleTimeUs;
    private int totalBytesWritten;
    private boolean writingSample;

    private TrackOutput output;

    public DvbSubtitlesReader(TsPayloadReader.EsInfo esInfo) {
        // we only support one subtitle service per PID
        this.language = esInfo.language;
        this.initializationData.add(new byte[] {esInfo.descriptorBytes[5]}); // subtitle subtype
        this.initializationData.add(new byte[] {esInfo.descriptorBytes[6], esInfo.descriptorBytes[7]}); // subtitle compose page
        this.initializationData.add(new byte[] {esInfo.descriptorBytes[8], esInfo.descriptorBytes[9]}); // subtitle ancillary page
    }


    @Override
    public void seek() {
        writingSample = false;
    }

    @Override
    public void createTracks(ExtractorOutput extractorOutput, TrackIdGenerator idGenerator) {
        output = extractorOutput.track(idGenerator.getNextId());
        output.format(Format.createImageSampleFormat(null, MimeTypes.APPLICATION_DVBSUBS, null, Format.NO_VALUE, initializationData, language, null));

//        idGenerator.generateNewId();
//        this.output = extractorOutput.track(idGenerator.getTrackId(), C.TRACK_TYPE_TEXT);
//        output.format(Format.createImageSampleFormat(idGenerator.getFormatId(), MimeTypes.APPLICATION_DVBSUBS, null, Format.NO_VALUE, initializationData, language, null));
    }


    @Override
    public void packetStarted(long pesTimeUs, boolean dataAlignmentIndicator) {
        if (!dataAlignmentIndicator) {
            return;
        }
        writingSample = true;
        sampleTimeUs = pesTimeUs;
        totalBytesWritten = 0;
    }

    @Override
    public void packetFinished() {
        output.sampleMetadata(sampleTimeUs, C.BUFFER_FLAG_KEY_FRAME, totalBytesWritten, 0, null);
        writingSample = false;
    }

    @Override
    public void consume(ParsableByteArray data) {
        if (writingSample) {
            totalBytesWritten += data.bytesLeft();
            output.sampleData(data, data.bytesLeft());
        }
    }
}