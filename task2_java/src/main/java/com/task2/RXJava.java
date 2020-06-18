package com.task2;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;

import static com.task2.Main.factorize;

public class RXJava {

    BigInteger counter = BigInteger.ZERO;
    String[] data;

    void updateCounter(BigInteger value) {
        counter = counter.add(value);
    }

    void loadData() {
        try {
            FileInputStream fileStream = new FileInputStream("numbers.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader((fileStream)));
            String line;
            ArrayList<String> d = new ArrayList<>();
            while((line = br.readLine())!=null) {
                d.add(line);
            }
            data = d.toArray(new String[0]);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    BigInteger count() {
        loadData();
        Flowable.fromArray(data)
                .parallel()
                .runOn(Schedulers.computation())
                .map( i -> factorize(new BigInteger(i)))
                .sequential()
                .blockingSubscribe(this::updateCounter);
        return counter;
    }
}