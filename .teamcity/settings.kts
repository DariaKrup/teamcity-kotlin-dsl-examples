import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.powerShell
import jetbrains.buildServer.configs.kotlin.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    buildType(JobCNoncritical)
    buildType(JobBFail)
    buildType(Job2aPass)
    buildType(ParentComposite)
    buildType(SecondComposite)
    buildType(JobAPass)
}

object Job2aPass : BuildType({
    name = "job2a-pass"

    steps {
        script {
            name = "PassImmediately"
            id = "PassImmediately"
            scriptContent = "exit 0"
        }
    }
})

object JobAPass : BuildType({
    name = "job-a-pass"

    steps {
        powerShell {
            name = "Pass after 5 mins"
            id = "Pass_after_5_mins"
            scriptMode = script {
                content = """
                    sleep 300
                    exit 0
                """.trimIndent()
            }
        }
    }
})

object JobBFail : BuildType({
    name = "job-b-fail"

    steps {
        script {
            name = "FailImmediately"
            id = "FailImmediately"
            scriptContent = "exit 1"
        }
    }
})

object JobCNoncritical : BuildType({
    name = "job c noncritical"

    steps {
        script {
            name = "FailImmediately"
            id = "FailImmediately"
            scriptContent = "exit 1"
        }
    }
})

object ParentComposite : BuildType({
    name = "parent composite"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        showDependenciesChanges = true
    }

    dependencies {
        snapshot(JobAPass) {
            reuseBuilds = ReuseBuilds.NO
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
        snapshot(JobBFail) {
            onDependencyFailure = FailureAction.FAIL_TO_START
        }
    }
})

object SecondComposite : BuildType({
    name = "second composite"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        showDependenciesChanges = true
    }

    dependencies {
        snapshot(Job2aPass) {
            onDependencyCancel = FailureAction.ADD_PROBLEM
        }
        snapshot(ParentComposite) {
            onDependencyCancel = FailureAction.ADD_PROBLEM
        }
    }
})
