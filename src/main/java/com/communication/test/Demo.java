package com.communication.test;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

public class Demo {

    public static void main(String args[]){
        ByteBufAllocator poolByteBufAllocator = new PooledByteBufAllocator(true);
        ByteBufAllocator unpoolByteBufAllocator = new UnpooledByteBufAllocator(true);
        ByteBufTest.poolAndUnpoolTest(poolByteBufAllocator);
    }
}
