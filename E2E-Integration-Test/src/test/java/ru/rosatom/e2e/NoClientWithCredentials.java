package ru.rosatom.e2e;

public class NoClientWithCredentials extends RuntimeException{
    public NoClientWithCredentials(String clientName) {
        super(String.format("No client with name %s", clientName));
    }
}
