package com.hank.trace;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 */
public class TraceClassVisitor extends ClassVisitor {
    private String className;
    private boolean isABSClass = false;
    private boolean isBeatClass = false;
    private boolean isConfigTraceClass = false;
    private Config traceConfig;

    public TraceClassVisitor(int api, ClassVisitor classVisitor, Config traceConfig) {
        super(api, classVisitor);
        this.traceConfig = traceConfig;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);

        this.className = name;
        //抽象方法或者接口
        if ((access & Opcodes.ACC_ABSTRACT) > 0 || (access & Opcodes.ACC_INTERFACE) > 0) {
            this.isABSClass = true;
        }

        //插桩代码所属类
        String resultClassName = name.replace(".", "/");

        if (resultClassName.contains(traceConfig.mBeatClass)) {
            this.isBeatClass = true;
        }

        //是否是配置的需要插桩的类
        isConfigTraceClass = traceConfig.isConfigTraceClass(className);
        boolean isNotNeedTraceClass = isABSClass || isBeatClass || !isConfigTraceClass;
        if (traceConfig.mIsNeedLogTraceInfo && !isNotNeedTraceClass) {
            if(StringUtils.isEmpty(className)){
                System.out.println("MethodTraceMan-trace-class: 未知");
            } else {
                System.out.println("MethodTraceMan-trace-class: " + className);
            }

        }
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        boolean isConstructor = MethodFilter.isConstructor(name);
        if (isABSClass || isBeatClass || !isConfigTraceClass || isConstructor) {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        } else {
            MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new TraceMethodVisitor(api, mv, access, name, descriptor, className, traceConfig);
        }
    }
}
