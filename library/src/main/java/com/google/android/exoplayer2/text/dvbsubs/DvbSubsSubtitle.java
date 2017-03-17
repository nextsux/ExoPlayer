package com.google.android.exoplayer2.text.dvbsubs;


import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.Subtitle;

import java.util.Collections;
import java.util.List;

final class DvbSubsSubtitle implements Subtitle {
    private final List<Cue> cues;

    public DvbSubsSubtitle(Bitmap data) {
        Log.v("DVBSUB", "DvbSubsSubtitle");
        if (data == null) {
            Log.v("DVBSUB", "DvbSubsSubtitle1");
            this.cues = Collections.emptyList();
        } else {
            Log.v("DVBSUB", "DvbSubsSubtitle2");
            Cue cue = new Cue(data, 0, Cue.ANCHOR_TYPE_START, 0, Cue.ANCHOR_TYPE_START, 1);
            this.cues = Collections.singletonList(cue);
        }
    }

    @Override
    public int getNextEventTimeIndex(long timeUs) {
        return C.INDEX_UNSET;
    }

    @Override
    public int getEventTimeCount() {
        return 1;
    }

    @Override
    public long getEventTime(int index) {
        return 0;
    }

    @Override
    public List<Cue> getCues(long timeUs) {
        return cues;
    }
}
