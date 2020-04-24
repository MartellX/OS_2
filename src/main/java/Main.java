

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    public static void main(String[] args) throws IOException {

        Queue<Integer> manufacturerQueue = new ArrayDeque<Integer>(200);
        Queue<Integer> consumerQueue = new ArrayDeque<Integer>();
        ThreadPoolExecutor manufacturer = (ThreadPoolExecutor)  Executors.newFixedThreadPool(3);
        ThreadPoolExecutor consumer = (ThreadPoolExecutor)  Executors.newFixedThreadPool(2);
        Random rd = new Random();
        boolean isSleep = false;
        int i = 0;
        while (System.in.read() != 'q')
        {


            if (!isSleep){
                manufacturer.submit(() -> manufacturerQueue.offer(rd.nextInt(100)));
                if (manufacturerQueue.size() >= 100) {
                    isSleep = true;
                }
            }

            if (manufacturerQueue.size() > 0) {
                consumer.submit(() -> consumerQueue.offer(manufacturerQueue.poll()));
            }

            if (manufacturerQueue.size() <= 80) {
                isSleep = false;
            }
            i++;
        }
        while (manufacturer.getQueue().size() > 0){}
        System.out.println("Конец цикла");
        while (manufacturerQueue.size() != 0){
            consumer.submit(() -> consumerQueue.offer(manufacturerQueue.poll()));
        }
        System.out.println("manufacturerQueue: " + manufacturerQueue);
        System.out.println("consumerQueue: " + consumerQueue);
        consumer.shutdown();
        manufacturer.shutdown();
    }
}
