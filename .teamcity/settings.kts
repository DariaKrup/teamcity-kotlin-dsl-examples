import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.remoteParameters.hashiCorpVaultParameter

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

version = "2023.11"

project {
    description = "Project with TeamCity Kotlin DSL examples"

    buildType(VaultParameterBuild)
    params {
        text("text_parameter_project", "text_value",
              regex = "a*", validationMessage = "NOT")
        checkbox("checkbox_project", "true",
                  checked = "true", unchecked = "false")
        password("password_project", "******", label = "password")
        select("select_project", "a1", description = "select",
                allowMultiple = true, valueSeparator = ";",
                options = listOf("a1" to "1", "a2" to "2"))
    }

    template(BuildTemplate)
}

object VaultParameterBuild : BuildType({
    templates(BuildTemplate)
    name = "Build Configuration with Vault parameter"

    params {
        hashiCorpVaultParameter { name = "vault_parameter"; query="/path/to/some/secret!overridden_value" }
    }
})

object BuildTemplate : Template({
    name = "build.template"

    params {
        hashiCorpVaultParameter {
            name = "vault_parameter"
            query = "/path/to/some/secret!value"
        }
        text("text_parameter_template", "text_value",
              regex = "a*", validationMessage = "NOT")
        checkbox("checkbox_template", "true",
                  checked = "true", unchecked = "false")
        password("password_template", "******", label = "password")
        select("select_template", "a1", description = "select",
                allowMultiple = true, valueSeparator = ";",
                options = listOf("a1" to "1", "a2" to "2"))
    }

    steps {
        script {
            name = "Parameter output"
            id = "ParameterOutput"
            scriptContent = "echo %vault_parameter%"
        }
    }
})
