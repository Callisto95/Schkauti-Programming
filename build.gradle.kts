plugins {
	`java-library`
	application
//    `maven-publish`
}

repositories {
	mavenLocal()
	maven {
		url = uri("https://repo.maven.apache.org/maven2/")
	}
}

application {
	// `gradle run -DmainClass=[class]
	mainClass = System.getProperty("mainClass") ?: "schkauti.sudoku.SudokuSolver"
	applicationDefaultJvmArgs = listOf("-ea")
}

group = "net.callisto"
version = "1.0-SNAPSHOT"
description = "Schkauti-Programming"
java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = java.sourceCompatibility

//publishing {
//    publications.create<MavenPublication>("maven") {
//        from(components["java"])
//    }
//}

dependencies {
	val jacksonVersion = "2.18.2"
	val lanternaVersion = "3.1.3"
	
	// What is needed for Jackson to work: I don't know; just use all of them
	// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
	implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
	// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
	implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
	// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
	implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
	
	// TUI
	// https://mvnrepository.com/artifact/com.googlecode.lanterna/lanterna
	implementation("com.googlecode.lanterna:lanterna:$lanternaVersion")
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
	options.encoding = "UTF-8"
}
