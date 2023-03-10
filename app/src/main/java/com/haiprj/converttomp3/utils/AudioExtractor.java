package com.haiprj.converttomp3.utils;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;

import com.haiprj.android_app_lib.mvp.model.DataResult;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class AudioExtractor {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;
    private static final String TAG = "AudioExtractorDecoder";
    private static Thread thread;

    @SuppressWarnings({"ConstantConditions", "resource"})
    @SuppressLint({"NewApi", "WrongConstant"})
    public static void genVideoUsingMuxer(String srcPath, String dstPath, int startMs, int endMs, boolean useAudio, boolean useVideo, DataResult dataResult){
        // Set up MediaExtractor to read from the source.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MediaExtractor extractor = new MediaExtractor();
                try {
                    extractor.setDataSource(srcPath);
                } catch (IOException e) {
                    dataResult.onDataResultFailed(e.getMessage());
                    thread.interrupt();
                }
                int trackCount = extractor.getTrackCount();
                // Set up MediaMuxer for the destination.
                MediaMuxer muxer = null;
                try {
                    muxer = new MediaMuxer(dstPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                } catch (IOException e) {
                    dataResult.onDataResultFailed(e.getMessage());
                    thread.interrupt();
                }
                // Set up the tracks and retrieve the max buffer size for selected
                // tracks.
                HashMap<Integer, Integer> indexMap = new HashMap<>(trackCount);
                int bufferSize = -1;
                for (int i = 0; i < trackCount; i++) {
                    MediaFormat format = extractor.getTrackFormat(i);
                    String mime = format.getString(MediaFormat.KEY_MIME);
                    boolean selectCurrentTrack = false;
                    if (mime.startsWith("audio/") && useAudio) {
                        selectCurrentTrack = true;
                    } else if (mime.startsWith("video/") && useVideo) {
                        selectCurrentTrack = true;
                    }
                    if (selectCurrentTrack) {
                        extractor.selectTrack(i);
                        int dstIndex = muxer.addTrack(format);
                        indexMap.put(i, dstIndex);
                        if (format.containsKey(MediaFormat.KEY_MAX_INPUT_SIZE)) {
                            int newSize = format.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE);
                            bufferSize = Math.max(newSize, bufferSize);
                        }
                    }
                }
                if (bufferSize < 0) {
                    bufferSize = DEFAULT_BUFFER_SIZE;
                }
                // Set up the orientation and starting time for extractor.
                MediaMetadataRetriever retrieverSrc = new MediaMetadataRetriever();
                retrieverSrc.setDataSource(srcPath);
                String degreesString = retrieverSrc.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
                if (degreesString != null) {
                    int degrees = Integer.parseInt(degreesString);
                    if (degrees >= 0) {
                        muxer.setOrientationHint(degrees);
                    }
                }
                if (startMs > 0) {
                    extractor.seekTo(startMs * 1000L, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
                }
                // Copy the samples from MediaExtractor to MediaMuxer. We will loop
                // for copying each sample and stop when we get to the end of the source
                // file or exceed the end time of the trimming.
                int offset = 0;
                int trackIndex = -1;
                ByteBuffer dstBuf = ByteBuffer.allocate(bufferSize);
                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                muxer.start();
                while (true) {
                    bufferInfo.offset = offset;
                    bufferInfo.size = extractor.readSampleData(dstBuf, offset);
                    if (bufferInfo.size < 0) {
                        bufferInfo.size = 0;
                        dataResult.onDataResultSuccess("ConvertSuccess", new File(dstPath));
                        thread.interrupt();
                        break;
                    } else {
                        bufferInfo.presentationTimeUs = extractor.getSampleTime();
                        if (endMs > 0 && bufferInfo.presentationTimeUs > (endMs * 1000L)) {
                            break;
                        } else {
                            bufferInfo.flags = extractor.getSampleFlags();
                            trackIndex = extractor.getSampleTrackIndex();
                            muxer.writeSampleData(indexMap.get(trackIndex), dstBuf, bufferInfo);
                            extractor.advance();
                        }
                    }
                }
                muxer.stop();
                muxer.release();
            }
        };
        thread = new Thread(runnable);
        thread.start();
    }

}
