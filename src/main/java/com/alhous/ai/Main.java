package com.alhous.ai;

import com.alhous.ai.model.Camera;
import com.alhous.ai.model.DatasetServiceImpl;
import com.alhous.ai.model.LiveServiceImpl;
import com.alhous.ai.view.Home;

import org.opencv.core.Core;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    static AnnotationConfigApplicationContext context;

    public static void main(String[] args) throws InterruptedException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        context = new AnnotationConfigApplicationContext();
        context.scan("com.alhous.ai");
        context.refresh();

        Camera camera = context.getBean(Camera.class);
        camera.setNumber(1);
        LiveServiceImpl liveService = context.getBean(LiveServiceImpl.class);
        liveService.setCamera(camera);
        liveService.start();

        DatasetServiceImpl ds = context.getBean(DatasetServiceImpl.class);
        new Home(liveService, ds).getFrame().setVisible(true);

    }
}
