package com.hank.trace

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author hank
 * @date 2020/05/13
 * @desc 自定义方法检查插件
 *
 */
class MTracePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println '*****************MethodTracePlugin apply*********************'
        project.extensions.create("MTrace", MTraceConfig)

        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MTraceTransform(project))
    }
}