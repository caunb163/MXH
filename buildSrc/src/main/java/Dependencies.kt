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
    const val coroutines = "1.4.3"
    const val navigation = "2.3.5"
    const val glide = "4.12.0"
    const val paging = "3.0.0-alpha11"
    const val gson = "2.8.6"

    const val firebase = "28.0.1"
    const val serviceAuth = "19.0.0"
    const val blurview = "1.6.5"
    const val circleImgView = "3.1.0"
    const val lifecycle = "2.3.0"
    const val photoView = "2.3.0"
    const val customTab = "28.0.0"
    const val countrycode = "2.5.1"
    const val pinView = "1.4.4"
    const val linkPreview = "1.0.9"
    const val jsoup = "1.11.3"
    const val picasso = "2.71828"
    const val exoplayer = "2.11.3"
    const val kohii = "1.1.0.2011003-RC2"
    const val autodispose = "0.3.1"
}

object Deps {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinCoroutineCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val kotlinCoroutineAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    const val kotlinCoroutinePlayServices =
        "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
    const val coroutineAutodispose = "com.github.satoshun.coroutine.autodispose:autodispose:${Versions.autodispose}"

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
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebase}"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseFirestore = "com.google.firebase:firebase-firestore-ktx"
    const val firebaseStorage = "com.google.firebase:firebase-storage-ktx"
    const val serviceAuth = "com.google.android.gms:play-services-auth:${Versions.serviceAuth}"

    const val blurview = "com.eightbitlab:blurview:${Versions.blurview}"
    const val circleImgView = "de.hdodenhof:circleimageview:${Versions.circleImgView}"
    const val lifecycle = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    const val photoView = "com.github.chrisbanes:PhotoView:${Versions.photoView}"
    const val countryCode = "com.hbb20:ccp:${Versions.countrycode}"
    const val customTab = "com.android.support:customtabs:${Versions.customTab}"
    const val pinView = "com.chaos.view:pinview:${Versions.pinView}"

    const val linkPreview = "io.github.ponnamkarthik:richlinkpreview:${Versions.linkPreview}"
    const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"
    const val picasso = "com.squareup.picasso:picasso:${Versions.picasso}"
    const val exoPlayer = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"
    const val kohiiCore = "im.ene.kohii:kohii-core:${Versions.kohii}"
    const val kohiiExoPlayer = "im.ene.kohii:kohii-exoplayer:${Versions.kohii}"
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
        Deps.gson,

        Deps.koin,
        Deps.koinScope,
        Deps.koinViewModel,

        Deps.kotlinCoroutineCore,
        Deps.kotlinCoroutineAndroid,
        Deps.kotlinCoroutinePlayServices,
        Deps.coroutineAutodispose
    )

    val appDependencies = arrayOf(
        Deps.navigation,
        Deps.navigationFrag
    )

    val linkPreview = arrayOf(
        Deps.linkPreview,
        Deps.jsoup,
        Deps.picasso
    )

    val firebaseDependencies = arrayOf(
        Deps.firebaseAnalytics,
        Deps.firebaseAuth,
        Deps.firebaseFirestore,
        Deps.firebaseStorage,
        Deps.serviceAuth
    )

    val kohii = arrayOf(
        Deps.kohiiCore,
        Deps.kohiiExoPlayer,
        Deps.exoPlayer
    )

    val testDependencies = arrayOf(TestDeps.junit)

    val testAndroidDependencies = arrayOf(
        TestDeps.junitAndroid,
        TestDeps.espresso
    )
}



