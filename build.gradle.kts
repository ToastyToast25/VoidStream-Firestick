plugins {
	alias(libs.plugins.detekt)
	java
}

buildscript {
	dependencies {
		classpath(libs.android.gradle)
		classpath(libs.kotlin.gradle)
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

subprojects {
	// Configure linting
	apply<io.gitlab.arturbosch.detekt.DetektPlugin>()
	detekt {
		buildUponDefaultConfig = true
		ignoreFailures = true
		config = files("$rootDir/detekt.yaml")
		basePath = rootDir.absolutePath

		reports {
			sarif.enabled = true
		}
	}

	// Configure default Kotlin compiler options
	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile> {
		compilerOptions {
			jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
		}
	}

	// Configure default Android options
	plugins.withType<com.android.build.gradle.BasePlugin> {
		extensions.configure<com.android.build.api.dsl.CommonExtension> {
			compileOptions.apply {
				sourceCompatibility = JavaVersion.VERSION_21
				targetCompatibility = JavaVersion.VERSION_21
			}
		}
	}
}

tasks.withType<Test> {
	// Ensure Junit emits the full stack trace when a unit test fails through gradle
	useJUnit()

	testLogging {
		events(
			org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
			org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
			org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
		)
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
		showExceptions = true
		showCauses = true
		showStackTraces = true
	}
}
