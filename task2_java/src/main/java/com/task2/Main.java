package com.task2;

import java.io.*;
import java.math.*;
import java.util.ArrayList;
import java.util.Random;



public class Main {

    private static void generator(int limit, int bits) {
        Random rand = new Random();
        try(FileWriter writer = new FileWriter("numbers.txt"))
        {
            ArrayList<String> list = new ArrayList<>();
            for(int i=1; i<limit; i++) {
                BigInteger randInt = new BigInteger(bits, rand);
                list.add(randInt.toString());
            }
            writer.append(String.join("\n", list));
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static BigInteger factorize(BigInteger num) {
        BigInteger currentNum = num;
        BigInteger limit = currentNum.sqrt();
        BigInteger currentMultiplier = BigInteger.valueOf(2);
        int result = 0;
        while(currentMultiplier.compareTo(BigInteger.ONE) > 0) {
            while (currentNum.mod(currentMultiplier).equals(BigInteger.ZERO)){
                result ++;
                currentNum = currentNum.divide(currentMultiplier);
                if(currentNum.equals(BigInteger.ONE))
                    break;
            }
            currentMultiplier = currentMultiplier.add(BigInteger.ONE);
            if(currentMultiplier.compareTo(limit) > 0) {
                result ++;
                break;
            }

        }
        return BigInteger.valueOf(result);
    }

    private static int continuous() {
        int result = 0;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("numbers.txt")))) {
            String line;
            while((line = br.readLine())!=null) {
                result += factorize(new BigInteger(line)).intValue();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static double starter(String method, int threads, int limit) {
        long time = System.currentTimeMillis();
        switch (method) {
            case "simple":
                continuous();
                break;
            case "multithread":
                new MultiThreading().count(threads, limit);
                break;
            case "rxjava":
                new RXJava().count();
                break;
            default:
                System.out.println("Unknown method");
                return -1.0;
        }
        return (System.currentTimeMillis() - time) * 1.0 / 1000;
    }

    public static ArrayList<String> experiment(int bit){
        ArrayList<String> ans = new ArrayList<>();
        generator(2000, bit);
        String[] methods = {"simple", "multithread", "rxjava"};
        long time;
        for(String method: methods){
            time = System.currentTimeMillis();
            starter(method, 8, 2000);
            ans.add(String.valueOf((System.currentTimeMillis()-time) * 1.0 / 1000));
        }
        return ans;
    }

    public static void main(String[] args) {

        Integer[] bits = {12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 40, 42};
        ArrayList<String> arr;
        try(FileWriter writer = new FileWriter("expres.csv")) {
            writer.write("bits,simple,multithread,rxjava\n");
            for (int bit : bits) {
                writer.write(bit + ",");
                System.out.println(bit);
                arr = experiment(bit);
                writer.write(String.join(",", arr) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
