package com.project.nhatrotot.util;

import java.io.IOException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;

public class S3Listener implements ProgressListener {
    private SseEmitter sseEmitter;
    private long bytes;
    private long totalBytes;
    private double previousPercent;

    private S3Listener(SseEmitter sseEmitter, long totalBytes) {
        this.sseEmitter = sseEmitter;
        this.bytes = 0;
        this.totalBytes = totalBytes;
        previousPercent = 0;
    }

    public static S3Listener create(SseEmitter sseEmitter, long totalBytes) {
        return new S3Listener(sseEmitter, totalBytes);
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        bytes += progressEvent.getBytesTransferred();
        double percent = ((double) bytes / totalBytes) * 100;
        if (percent > previousPercent + 5) {
            try {
                sseEmitter.send(sseEmitter.event().name("progress").data(String.format("%.0f", percent)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            previousPercent = percent;
        }
    }

}
