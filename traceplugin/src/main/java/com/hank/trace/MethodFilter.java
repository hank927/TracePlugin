package com.hank.trace;

public class MethodFilter {


        public static boolean isConstructor(String methodName){
            if(StringUtils.isEmpty(methodName)){
                return false;
            }
            return methodName.contains("<init>");
        }

}