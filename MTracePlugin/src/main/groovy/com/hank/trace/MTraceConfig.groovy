package com.hank.trace

/**
 * @author hank
 * @date 2020/05/13
 * @desc 为MethodTrace自定义的配置项extension
 *
 */
class MTraceConfig {
    String output
    boolean open
    String traceConfigFile
    boolean logTraceInfo

    MTraceConfig() {
        open = true
        output = ""
        logTraceInfo = false
    }
}