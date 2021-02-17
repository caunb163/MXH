object Config {
    const val compileSdk = 30
    const val buildTool = "30.0.2"
    const val minSdk = 23
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0"
}

object Modules {
    const val app = ":app"
    const val domain = ":domain"
    const val data = ":data"
    const val ui = ":ui"
}

object Versions {
    const val kotlin = "1.4.21"
    const val coreKtx = "1.3.2"
    const val material = "1.2.1"
    const val appCompat = "1.2.0"
    const val constraintLayout = "2.0.4"

    const val koin = "2.2.2"
    const val coroutines = "1.4.2"
    const val navigation = "2.3.2"
    const val glide = "4.11.0"
    const val paging = "3.0.0-alpha11"

}

object Deps {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinCoroutineCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinCoroutineAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"

    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"


    const val navigation = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationFrag = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"

    const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
}

object TestDeps {
    const val junit = "junit:junit:4.13.1"
    const val junitAndroid = "androidx.test.ext:junit:1.1.2"
    const val espresso = "androidx.test.espresso:espresso-core:3.3.0"
}

object Dependencies {
    val coreDependencies = arrayOf(
        Deps.kotlin,

        Deps.appCompat,
        Deps.coreKtx,
        Deps.material,
        Deps.constraintLayout,

        Deps.koin,
        Deps.koinScope,
        Deps.koinViewModel,

        Deps.kotlinCoroutineCore,
        Deps.kotlinCoroutineAndroid
    )

    val appDependencies = arrayOf(
        Deps.navigation,
        Deps.navigationFrag
    )

    val testDependencies = arrayOf(
        TestDeps.junit
    )

    val testAndroidDependencies = arrayOf(
        TestDeps.junitAndroid,
        TestDeps.espresso
    )
}



