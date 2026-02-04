plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.15.3" apply false
    //id("dev.kikugie.j52j") version "1.0.2" apply false // Enables asset processing by writing json5 files
}
stonecutter active "1.21.1" /* [SC] DO NOT EDIT */

// Builds every version into `build/libs/{mod.version}/`
stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) {
    group = "project"
    ofTask("buildAndCollect")
}

/*
stonecutter configureEach {
    /*
    See src/main/java/com/example/TemplateMod.java
    and https://stonecutter.kikugie.dev/
    */
    // Swaps replace the scope with a predefined value
    swap("mod_version", "\"${property("mod.version")}\";")
    // Constants add variables available in conditions
    const("release", property("mod.id") != "template")
    // Dependencies add targets to check versions against
    // Using `project.property()` in this block gets the versioned property
    dependency("fapi", project.property("deps.fabric_api").toString())
}
*/


// GitHub Action Stuff
var releaseText = StringBuilder()

releaseText.append("name: Create Release\n")

releaseText.append("on:\n")
releaseText.append("  release:\n")
releaseText.append("    types: [published]\n")

releaseText.append("jobs:\n")
releaseText.append("  build:\n")
releaseText.append("    runs-on: ubuntu-latest\n")
releaseText.append("    steps:\n")
releaseText.append("      - name: checkout repository\n")
releaseText.append("        uses: actions/checkout@v2\n\n")
releaseText.append("      - name: setup jdk 21\n")
releaseText.append("        uses: actions/setup-java@v1\n")
releaseText.append("        with:\n")
releaseText.append("          java-version: 21\n\n")
releaseText.append("      - name: make gradle wrapper executable\n")
releaseText.append("        run: chmod +x ./gradlew\n\n")
releaseText.append("      - name: build\n")
releaseText.append("        run: ./gradlew chiseledBuild\n")




var actionFile = file("$rootDir/.github/workflows/publish.yml")
if (actionFile.exists()) {
    actionFile.writeText(releaseText.toString())
}
else {
    actionFile.createNewFile()
    actionFile.writeText(releaseText.toString())
}



