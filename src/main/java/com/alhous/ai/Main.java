package com.alhous.ai;

import com.alhous.ai.model.IDatasetService;
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

        LiveServiceImpl liveService = context.getBean(LiveServiceImpl.class);
        liveService.start();

        new Home(context).getFrame().setVisible(true);
    }
}