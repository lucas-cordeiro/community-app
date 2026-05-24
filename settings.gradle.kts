pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CommunityApp"
include(":app")

include(":app:shared:core")
include(":app:shared:network")
include(":app:shared:storage")
include(":app:shared:ui")
include(":app:shared:ui:test")
include(":app:shared:network:test")

include(":app:component:community")

include(":app:feature:community")
