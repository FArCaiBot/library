# masking-library

Java library for reusable data masking logic across multiple projects.

## Coordinates

- `groupId`: `io.github.farcaibot`
- `artifactId`: `masking-library`
- `version`: `0.1.0`

## Installation

### Gradle (Groovy)

```gradle
dependencies {
    implementation "io.github.farcaibot:masking-library:0.1.0"
}
```

### Gradle (Kotlin)

```kotlin
dependencies {
    implementation("io.github.farcaibot:masking-library:0.1.0")
}
```

### Maven

```xml
<dependency>
  <groupId>io.github.farcaibot</groupId>
  <artifactId>masking-library</artifactId>
  <version>0.1.0</version>
</dependency>
```

## Build And Test

```bash
./gradlew test
```

On Windows:

```powershell
.\gradlew.bat test
```

## Release Process

1. Update `version` in `gradle.properties`.
2. Publish:
   - Snapshot: `.\gradlew.bat :lib:publishToMavenCentral`
   - Release: `.\gradlew.bat :lib:publishAndReleaseToMavenCentral`
3. Create and push a git tag:
   - `git tag v<version>`
   - `git push origin v<version>`

## Requirements

- JDK 21
- Gradle Wrapper (included)

## License

Apache License 2.0.
