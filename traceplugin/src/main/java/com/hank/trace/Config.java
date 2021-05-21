package com.hank.trace;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * author : Administrator
 * date   : 2020/5/13
 * desc   :
 */
public class Config {

    //一些默认无需插桩的类
    public static String[] UNNEED_TRACE_CLASS = {"R.class", "R$", "Manifest", "BuildConfig"};

    //插桩配置文件
    public String mTraceConfigFile;

    //需插桩的包
    public HashSet<String> mNeedTracePackageMap = new HashSet<>();

    //在需插桩的包范围内的 无需插桩的白名单
    public HashSet<String> mWhiteClassMap = new HashSet<>();

    //在需插桩的包范围内的 无需插桩的包名
    public HashSet<String> mWhitePackageMap = new HashSet<>();

    //插桩代码所在类
    public String mBeatClass;

    //是否需要打印出所有被插桩的类和方法
    public boolean mIsNeedLogTraceInfo = false;

    public boolean isNeedTraceClass(String fileName) {
        boolean isNeed = true;
        if (fileName.endsWith(".class")) {
            for (String trace : UNNEED_TRACE_CLASS) {
                if (fileName.contains(trace)) {
                    isNeed = false;
                    break;
                }
            }
        } else {
            isNeed = false;
        }
        return isNeed;
    }

    //判断是否是traceConfig.txt中配置范围的类
    public boolean isConfigTraceClass(String className){
        if(mNeedTracePackageMap.isEmpty()){
            return !(isInWhitePackage(className) || isInWhiteClass(className));
        } else {
            if (isInNeedTracePackage(className)) {
                return !(isInWhitePackage(className) || isInWhiteClass(className));
            } else {
                return false;
            }
        }
    }

    public boolean isInNeedTracePackage(String className){
        return isInMap(className,mNeedTracePackageMap);
    }

    public boolean isInWhitePackage(String className){
        return isInMap(className,mWhitePackageMap);
    }
    public boolean isInWhiteClass(String className){
        return isInMap(className,mWhiteClassMap);
    }
    private boolean isInMap(String className, HashSet<String> map){
        boolean isIn = false;
        Iterator<String> it = map.iterator();
        while (it.hasNext()){
            String name = it.next();
            if(className.contains(name)){
                isIn = true;
                break;
            }
        }
        return isIn;
    }

    public void parseTraceConfigFile() throws FileNotFoundException {

        System.out.println("parseTraceConfigFile start!!!!!!!!!!!!");
        File traceConfigFile = new File(mTraceConfigFile);
        if (!traceConfigFile.exists()) {
            throw new FileNotFoundException("Trace config file not exist, Please read quickstart.找不到配置文件, 尝试阅读一下 QuickStart。"+mTraceConfigFile);
        }

        String configStr = FileUtils.readFileAsString(traceConfigFile.getAbsolutePath());

        String[] configArray =
                configStr.split(System.lineSeparator());

        if (configArray != null) {
            for(int i=0;i<configArray.length;i++){
                String config = configArray[i];
                System.out.println("configArray:"+config);
                if(StringUtils.isEmpty(config)){
                    continue;
                }
                if (config.startsWith("#")) {
                    continue;
                }
                if (config.startsWith("[")) {
                    continue;
                }
                if(config.startsWith("-tracepackage ")) {
                    config = config.replace("-tracepackage ", "");
                    mNeedTracePackageMap.add(config);
                    System.out.println("tracepackage:"+config);
                }
                if(config.startsWith("-keepclass ")) {
                    config = config.replace("-keepclass ", "");
                    mWhiteClassMap.add(config);
                    System.out.println("keepclass:"+config);
                }
                if(config.startsWith("-keeppackage ") ) {
                    config = config.replace("-keeppackage ", "");
                    mWhitePackageMap.add(config);
                    System.out.println("keeppackage:"+config);
                }
                if(config.startsWith("-beatclass ") ) {
                    config = config.replace("-beatclass ", "");
                    mBeatClass = config;
                    System.out.println("beatclass:"+config);
                }
            }
        }
    }
}
