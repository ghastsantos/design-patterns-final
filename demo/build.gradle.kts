plugins {
    application
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(project(":library"))
}

application {
    mainClass.set("br.pucpr.demo.DemoApplication")
}

tasks.named<JavaExec>("run") {
    workingDir = rootProject.projectDir
}
