package at.rueckgr.spotlight.main;

import at.rueckgr.spotlight.service.SpotlightDownloader;

//@Log4j2
public class Main {

//    private static final int WORKERS = 50;
//    private static final int DEFAULT_NUM_OF_WORKERS_TO_ADD = 100;
//    private static ExecutorService EXECUTOR;
//    private static CloseableHttpClient HTTP_CLIENT;


    public static void main(String[] args) {
        new SpotlightDownloader().run();
    }

//    private static void start() throws IOException, InterruptedException {
//        log.info(String.format("Start fetching new images (%s Workers)", WORKERS));
//        HTTP_CLIENT = HttpClients.custom().disableDefaultUserAgent().build();
//        EXECUTOR = Executors.newFixedThreadPool(WORKERS);
////        printWorkerSize();
//        addWorkers();
//        int activeCount = 0;
//        int size = 0;
//        ThreadPoolExecutor executor = (ThreadPoolExecutor) EXECUTOR;
//        do {
//            size = executor.getQueue().size();
//            activeCount = executor.getActiveCount();
//            TimeUnit.SECONDS.sleep(5);
//        }
//        while (size > 0 && activeCount > 0);
//        EXECUTOR.shutdown();
//        while (!EXECUTOR.isTerminated()) {
//        }
//        HTTP_CLIENT.close();
//        log.info(">> Finished <<");
//    }
//
//    public static void addWorkers() {
//        int numOfWorkers = Worker.getImageList().size();
//        if (numOfWorkers < DEFAULT_NUM_OF_WORKERS_TO_ADD) {
//            numOfWorkers = DEFAULT_NUM_OF_WORKERS_TO_ADD;
//        }
//        for (int i = 0; i < numOfWorkers; i++) {
//            if (!EXECUTOR.isShutdown() && ((ThreadPoolExecutor) EXECUTOR).getQueue().size() < numOfWorkers) {
//                EXECUTOR.execute(new Worker(HTTP_CLIENT));
//            }
//        }
//    }

}


