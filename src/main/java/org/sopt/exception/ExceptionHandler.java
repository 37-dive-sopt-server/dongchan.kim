package org.sopt.exception;

public class ExceptionHandler {

    public static void handle(Throwable e){
        if(e instanceof DomainException){
            System.out.println("❌ 오류 발생" + e.getMessage());
        }
    }
}
