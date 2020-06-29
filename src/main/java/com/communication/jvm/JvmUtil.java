package com.communication.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

public class JvmUtil {

    public static void main(String args[]){
        JvmUtil.printMem();
    }

    static String mb(long s) {
        return String.format("%d (%.2f M)", s, (double) s / (1024 * 1024));
    }

    public static void  printMem(){

        System.out.println();

        long totalMemory =  Runtime.getRuntime().totalMemory();
        long  maxMemory = Runtime.getRuntime().maxMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();

        float div = (float)( 1024 * 1024.0);

        System.out.println(String.format("totalMemory = %.2fM , maxMemory = %.2f M, freeMemory = %.2fM   ",
                totalMemory/div,maxMemory/div,freeMemory/div));

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage =  memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        System.out.println(String.format("heapMemoryUsage　init=%.2fM,Max = %.2fM,used=%.2fM",
                heapMemoryUsage.getInit()/div,heapMemoryUsage.getMax()/div,heapMemoryUsage.getUsed()/div));

        System.out.println(String.format("nonHeapMemoryUsage　init=%.2fM,Max = %.2fM,used=%.2fM",
                nonHeapMemoryUsage.getInit()/div,nonHeapMemoryUsage.getMax()/div,nonHeapMemoryUsage.getUsed()/div));


        for (MemoryPoolMXBean mp : ManagementFactory.getMemoryPoolMXBeans()) {

            System.out.println("Pool: " + mp.getName() + " (type " + mp.getType() + ")" + " = " + mb(mp.getUsage().getMax()));
        }

        System.out.println();
    }

}
