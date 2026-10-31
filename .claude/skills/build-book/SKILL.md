---
name: build-book
description: Build the mdBook into ./book, regenerating the solution ZIPs first. Use when asked to build, render, or verify the book compiles.
---

# Build the book

1. From the repository root, run:

   ```shell
   ./zip-solutions.sh && mdbook build
   ```

   `zip-solutions.sh` must run first: it regenerates `src/solutions/*.zip`, which pages link to.

2. If `mdbook` is not found, try `bash -l -c "mdbook build"`. If it is still missing, tell the user to install it (CI uses mdBook 0.5.4) rather than installing it silently.

3. Report any mdBook errors or warnings. Common causes are a broken `{{#include ...}}` path into `solutions/` and a page missing from `src/SUMMARY.md`.

Output goes to `./book`, which is gitignored. To preview interactively, use `mdbook serve` instead of `mdbook build`, after `./zip-solutions.sh`.
