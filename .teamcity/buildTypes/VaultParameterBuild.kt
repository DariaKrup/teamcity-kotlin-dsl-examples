package buildTypes

import jetbrains.buildServer.configs.kotlin.BuildType
import jetbrains.buildServer.configs.kotlin.Template
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.remoteParameters.hashiCorpVaultParameter


object BuildTemplate: Template({
    name = "build.template"
    params {
        hashiCorpVaultParameter { name = "vault_parameter"; query="/path/to/some/secret!value" }
    }

    steps {
        script {
            name = "Parameter output"
            id = "ParameterOutput"
            scriptContent = "echo %vault_parameter%"
        }
    }
})

class VaultParameterBuild : BuildType {
    constructor() : super() {
        name = "Build Configuration with Vault parameter"
        templates(BuildTemplate)
    }
    constructor(block: VaultParameterBuild.() -> Unit = {}) : this() {
        block()
    }
}

