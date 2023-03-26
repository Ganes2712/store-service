package org.ta.store.aspect;

public class StoreServiceException extends Exception{

    public StoreServiceException(String message){
        super(message);
    }


    public StoreServiceException(Throwable cause){
        super(cause);
    }

    public StoreServiceException(String message,Throwable cause){
        super(message, cause);
    }


}
