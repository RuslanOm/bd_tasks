package com.task2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.task2.Main.factorize;


public class MultiThreading{

    private BlockingQueue<BigInteger> bq;
    private BigInteger totalMultipliers = BigInteger.ZERO;
    private final Object lock = new Object();

    private class countingThread extends Thread {
        @Override
        public void run() {
            while(true) {
                BigInteger num = bq.poll();
                if (num == null) break;
                BigInteger multipliers = factorize(num);
                synchronized (lock) {
                    totalMultipliers = totalMultipliers.add(multipliers);
                }
            }
        }
    }

    ArrayBlockingQueue<BigInteger> readData(int limit) {
        ArrayBlockingQueue<BigInteger> bq = new ArrayBlockingQueue<>(limit);
        try {
            FileInputStream fileStream = new FileInputStream("numbers.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader((fileStream)));
            String line;
            while((line = br.readLine())!=null) {
                bq.put(new BigInteger(line));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return bq;
    }

    BigInteger count(int threads, int limit) {
        bq = readData(limit);
        countingThread[] counters = new countingThread[threads];
        for (int i=0; i < threads; i++) {
            counters[i] = new countingThread();
            counters[i].start();
        }
        for (int thread=0; thread < threads; thread++) {
            try {
                counters[thread].join();
            }
            catch (InterruptedException e) {
                counters[thread].interrupt();
            }
        }
        return totalMultipliers;
    }
}