# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

mdBook sources for the ENG2 practical worksheets (University of York). Part 1 (practicals 1–5) covers data-intensive systems with Micronaut/Kafka/Docker; Part 2 (practicals 6–11) covers model-driven engineering with Epsilon/EMF/Sirius.

## Commands

mdBook 0.5.4 is used in CI. The solution ZIPs must be generated before building:

```shell
./zip-solutions.sh && mdbook serve --open   # local preview
./zip-solutions.sh && mdbook build          # static HTML into ./book (gitignored)
lychee --exclude-loopback book              # link check after building (CI also passes --accept 200,429 and --root-dir)
```

There are no tests; CI (`.github/workflows/check-book.yaml`) builds the book and runs lychee on every non-main push and PR. `deploy-book.yaml` publishes from main.

## Architecture

- `src/SUMMARY.md` defines the book's table of contents. New pages must be added there. Part 1 pages live in `src/practicals/<topic>/NN-name.md` with a top-level `NN-*.md` landing page; Part 2 uses `src/practicals/<topic>/index.md` instead.
- `solutions/` holds the complete solution projects (Gradle/Micronaut projects for practicals 1–5, Eclipse/Epsilon files for 6–11), named `practicalN[-suffix]`. `zip-solutions.sh` zips each `practical*` directory (excluding `build/`) into `src/solutions/`, which is generated, so book pages can link to `../../solutions/practicalN.zip`.
- Some pages embed solution files directly with `{{#include ../../../solutions/practicalN/...}}`, so renaming or moving files under `solutions/` can break the book. Keep relative paths in sync.
- `custom-languages/*.js` are highlight.js definitions for Epsilon languages (Emfatic, EOL, EVL, ETL, EGX, EGL), loaded via `additional-js` in `book.toml`; `init.js` triggers highlighting. Use those language names for code fences.
