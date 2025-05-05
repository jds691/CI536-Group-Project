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

rootProject.name = "Pantry Plan"
include(":app")

include(":feature:pantry")
include(":feature:recipes")
include(":feature:meal-planner")
include(":feature:settings")

include(":core:models")
include(":core:database")
include(":core:data-access")
include(":core:design-system")
include(":core:datastore")
