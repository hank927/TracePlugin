package com.hank.trace;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
/**
 * author : Administrator
 * date   : 2020/5/15
 * desc   : 方法访问类
 */
public class TraceMethodVisitor extends AdviceAdapter {
    private String methodName;
    private String name;
    private String className;
    private Config traceConfig;
    private int maxSectionNameLength = 127;
    private int methodIdLocalIndex;
    public TraceMethodVisitor(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, String className, Config traceConfig) {
        super(api, methodVisitor, access, name, descriptor);
        this.className = className;
        MethodTrace traceMethod = MethodTrace.create(0, access, className, name, descriptor);
        this.methodName = traceMethod.getMethodNameText();
        this.traceConfig = traceConfig;
        this.name = name;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        methodIdLocalIndex = newLocal(Type.getType(long.class));
//
        mv.visitMethodInsn(
                INVOKESTATIC,
                traceConfig.mBeatClass,
                "begin",
                "()J",
                false
        );
        storeLocal(methodIdLocalIndex);
        if (traceConfig.mIsNeedLogTraceInfo) {
            if(StringUtils.isEmpty(methodName)){
                System.out.println("MTrace-trace-method: 未知");
            } else {
                System.out.println("MTrace-trace-method: " + methodName);
            }
        }

    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        mv.visitLdcInsn(generatorMethodName());
        loadLocal(methodIdLocalIndex);
        mv.visitMethodInsn(
                INVOKESTATIC,
                traceConfig.mBeatClass,
                "end",
                "(Ljava/lang/String;J)V",
                false
        );

    }

    public String generatorMethodName(){
        String sectionName = methodName;
        int length = !StringUtils.isEmpty(sectionName)?sectionName.length() : 0;
        if (length > maxSectionNameLength && !StringUtils.isEmpty(sectionName)) {
            // 先去掉参数
            int parmIndex = sectionName.indexOf('(');
            sectionName = sectionName.substring(0, parmIndex);
            // 如果依然更大，直接裁剪
            length = sectionName.length();
            if (length > 127) {
                sectionName = sectionName.substring(length - maxSectionNameLength);
            }
        }
        return sectionName;
    }
}
