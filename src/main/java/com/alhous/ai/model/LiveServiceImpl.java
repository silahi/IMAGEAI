package com.alhous.ai.model;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LiveServiceImpl implements ILiveService {
    
    private Camera camera;
    private VideoCapture videoCapture;
    private Thread liveThread;
    private Mat mat = new Mat();

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void start() {
        videoCapture = new VideoCapture(camera.getNumber(), Videoio.CAP_DSHOW);
        liveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (videoCapture.isOpened()) {
                        videoCapture.read(mat);
                    }
                }
            }
        });
        liveThread.start();
    }

    @Override
    public void pause(long time) {
        try {
            liveThread.wait(time);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        liveThread.interrupt();
        videoCapture.release();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public VideoCapture getVideoCapture() {
        return videoCapture;
    }

    public void setVideoCapture(VideoCapture videoCapture) {
        this.videoCapture = videoCapture;
    }

    public Thread getLiveThread() {
        return liveThread;
    }

    public void setLiveThread(Thread liveThread) {
        this.liveThread = liveThread;
    }

    public Mat getMat() {
        return mat;
    }

    public void setMat(Mat mat) {
        this.mat = mat;
    }

}
