package com.wenwanggarzagao.beeline.io;

public interface Discriminator<T> {

    boolean acceptable(T t);

}
