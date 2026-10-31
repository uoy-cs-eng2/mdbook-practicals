---
name: upgrade-micronaut-5
description: Upgrade a Micronaut 4 project (Gradle or Maven, e.g. under solutions/) to Micronaut 5 following the official upgrade guide. Use when asked to migrate, upgrade, or bump a project to Micronaut 5.
---

# Upgrade a Micronaut 4 project to Micronaut 5

Source of truth: https://github.com/micronaut-projects/micronaut-core/wiki/Update-to-Micronaut-5
Re-fetch it before starting, since versions may have moved on. The values below were taken from it and may be stale.
Check the latest stable versions (Gradle plugin portal for `io.micronaut.*` plugins, docs.micronaut.io for the latest Micronaut core version) rather than trusting the numbers below. Micronaut core (`micronautVersion`) and the Gradle plugins are versioned separately: core was 5.2.4 and the `io.micronaut.*` plugins 5.0.2 when last verified. Apply plugin edits to the `id("...") version "..."` form used in build files, and confirm with `grep` that they took effect.

Target one project directory at a time (e.g. `solutions/practical2`). Ask which one if unclear. Detect the build tool from `build.gradle` or `pom.xml`.

## Gradle applications

- Java: set compatibility to Java 25 (the Micronaut 5 baseline), including the toolchain and any `sourceCompatibility`/`targetCompatibility`.
- `gradle.properties`: `micronautVersion=5.2.4`.
- Gradle wrapper: upgrade to 9.5.0 (`./gradlew wrapper --gradle-version 9.5.0`), and check the Gradle 9.x upgrade guide for build-script breakage.
- Plugins:
  - `io.micronaut.application` 4.6.x → 5.0.2 (apply the same to other `io.micronaut.*` plugins in use)
  - `com.gradleup.shadow` → 9.4.1
  - Kotlin projects: `org.jetbrains.kotlin.jvm` and `org.jetbrains.kotlin.plugin.allopen` → 2.3.21; `com.google.devtools.ksp` → 2.3.7

## Maven applications

- `micronaut-parent` version and the `micronaut.version` property → 5.2.4.
- `jdk.version` and `release.version` properties → 25.

## Other changes to look for

Grep the project for each of these and fix what applies:

- **Nullability:** Micronaut 5 uses JSpecify annotations. Expect possible new nullability warnings or errors.
- **Jackson:** `micronaut-jackson-databind` now uses Jackson 3. Check imports and direct Jackson usage.
- **Security:** `@RolesAllowed`, `@PermitAll` and `@DenyAll` mappers moved to `micronaut-security-processor`. Replace `micronaut-security-annotations` with it in the annotation processor configuration.
- **Data:** the embedded-fields naming strategy changed. Either add `@MappedProperty` to embedded fields, or set the system property `micronaut.data.embedded.naming.strategy=LEGACY`. Check whether schemas or queries depend on the old column names before choosing.
- **Testcontainers:** use the artifact `org.testcontainers:testcontainers-junit-jupiter`.
- **Views:** Turbo support is now `io.micronaut.views:micronaut-views-turbo`, and `@TurboView` is renamed `@TurboStreamView`.
- **EclipseStore:** `micronaut-eclipsestore-annotations` → `micronaut-eclipsestore-processor` (`io.micronaut.eclipsestore`), as an annotation processor.
- **Removed modules:** RxJava 2 is no longer supported. MicroStream is removed; migrate to EclipseStore.
- The guide links per-module breaking-change pages (core, data, security, web, cloud, messaging, others). Read those for each Micronaut module the project depends on.

## Procedure

1. Read the build files and list the Micronaut modules in use.
2. Apply the version and plugin changes above, then the module-specific changes that match the project's dependencies.
3. Build and test (`./gradlew build` or `./mvnw verify`). Java 25 must be available. If it is missing, tell the user instead of changing the toolchain to something older.
4. Fix compile and test failures against the breaking-change pages. Do not suppress them.
5. Summarise what changed, what you could not verify, and any behaviour changes (e.g. the data naming strategy) the user should decide on.

## Book-specific notes

Documentation links to `docs.micronaut.io` use the minor-version path (e.g. `https://docs.micronaut.io/5.2.x/guide/`), not a patch version such as `5.2.4`, which returns 404. Update the minor version in those links when bumping to a new 5.x minor, and confirm the new path with `curl` or the link check.

In this repository the worksheets in `src/practicals/` quote the project setup (versions, plugin IDs, dependencies, generated code) and `solutions/` holds the matching projects. When upgrading a solution, also check the matching worksheet text for version numbers and snippets that need updating, and rebuild the book with the `build-book` skill. Do not change worksheet wording beyond what the upgrade requires.
