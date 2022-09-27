package io.github.gaming32.dataexport;

@FunctionalInterface
public interface ExceptionBiConsumer<T, U> {
    void accept(T t, U u) throws Exception;
}
