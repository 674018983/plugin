package  com.hc.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        System.out.println("========================")
        System.out.println("构建CMSettings.apk")
        System.out.println("========================")
        output(project)
        System.out.println("========================")
    }

    void output(Project project){
        project.android.applicationVariants.all { variant ->
            variant.outputs.all {
                outputFileName = "TelpoSettings-${getName()}-${versionName}-${buildTime()}.apk"
            }

        }
    }

    def buildTime(){
        return new Date().format("yyyy-MM-dd")
    }
    def getGitTag = {->
        def cmd = "git describe --tags --abbrev=0"
        return cmd.execute().text.trim()
    }


    def getVersionTag = { ->
        def tag = getGitTag()
        if(tag.startsWith("v") || tag.startsWith("V")){
            tag = tag.substring(1)
        }
        if(tag == null || tag.equals("")){
            tag = "1.0"
        }
        return tag
    }

    def getVersionBuild =
            { ->
                def tag = getGitTag()
                def cmd = "git rev-list HEAD --first-parent --count"
                if(tag !=null && !tag.equals("")){
                    cmd = "git rev-list HEAD ^"+tag+" --first-parent --count"
                }
                return cmd.execute().text.trim().toInteger()
            }



    def getVersionCode = {->
        def cmd = "git rev-list HEAD --first-parent --count"
        return cmd.execute().text.trim().toInteger()
    }


// versionName 增加最后一笔提交code 方便回滚定位问题
    def getVersionName = { ->

        def cmd = "git rev-parse --short HEAD"
        def commit = cmd.execute().text.trim().toString()
        return getVersionTag()+"."+getVersionCode()+"-"+commit
    }

}