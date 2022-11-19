plugins {
    /**
     * https://github.com/allegro/axion-release-plugin
     *
     * 1. Current build version
     * ./gradlew cV
     *
     * 2. Move to next version
     * git tag v0.3.0-pre.1
     * git push origin v0.3.0-pre.1
     *
     * 3. Release
     * -> Update README.md
     * git tag v0.3.0 "Release"
     */
    id ("pl.allegro.tech.build.axion-release") version "1.14.2"
}

project.version = scmVersion.version
