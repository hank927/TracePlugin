package com.hank.trace;

import org.objectweb.asm.Opcodes;

/**
 *
 */
public class MethodTrace {

    private int id = 0;
    private int accessFlag = 0;
    private String className;
    private String methodName;
    private String desc;

   public static MethodTrace create(int id, int accessFlag, String className, String methodName, String desc){
        MethodTrace traceMethod = new MethodTrace();
        traceMethod.id = id;
        traceMethod.accessFlag = accessFlag;
        traceMethod.className = className.replace("/", ".");
        traceMethod.methodName = methodName;
        traceMethod.desc = desc.replace("/", ".");
        return traceMethod;
    }


    public String getMethodNameText(){

        if (desc == null || isNativeMethod()) {
            return  this.className + "." + this.methodName;
        } else {
            return  this.className + "." + this.methodName + "." + desc;
        }
    }


    @Override
    public String toString(){
        if (desc == null || isNativeMethod()) {
            return id +"," + accessFlag + "," + className + "," + methodName;
        } else {
            return id +"," + accessFlag + "," + className + "," + methodName + "," + desc;
        }
    }


    public boolean isNativeMethod(){
        return (accessFlag & Opcodes.ACC_NATIVE) != 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MethodTrace) {
            MethodTrace trace = (MethodTrace) obj;
            if(!StringUtils.isEmpty(trace.getMethodNameText())){
                return trace.getMethodNameText().equals(getMethodNameText());
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }
}