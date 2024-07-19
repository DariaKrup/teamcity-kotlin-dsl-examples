package patches.buildTypes

import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.remoteParameters.hashiCorpVaultParameter
import jetbrains.buildServer.configs.kotlin.ui.*

/*
This patch script was generated by TeamCity on settings change in UI.
To apply the patch, change the buildType with id = 'VaultParameterBuild'
accordingly, and delete the patch script.
*/
changeBuildType(RelativeId("VaultParameterBuild")) {
    params {
        expect {
            hashiCorpVaultParameter {
                name = "vault_parameter"
                query = "/path/to/some/secret!overridden_value"
            }
        }
        update {
            hashiCorpVaultParameter {
                name = "vault_parameter"
                query = "/path/to/some/secret!value"
            }
        }
    }
}