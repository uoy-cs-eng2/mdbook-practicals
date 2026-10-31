---
name: check-links
description: Check internal and external links of the built book with lychee, mirroring CI. Use when asked to check, verify, or fix links.
---

# Check links with lychee

1. Build the book first (use the `build-book` skill) so `./book` is up to date.

2. Run lychee with the same arguments as CI (`.github/workflows/check-book.yaml`), from the repository root:

   ```shell
   lychee --verbose --accept 200,429 --exclude-loopback --no-progress \
     --user-agent 'curl/7.79.1' --root-dir "$(pwd)/book" book
   ```

3. If `lychee` is not found, try `bash -l -c "..."`. If it is still missing, tell the user to install it (https://github.com/lycheeverse/lychee) rather than installing it silently.

4. Summarise the failing links grouped by source page. For each, find the originating markdown under `src/` and suggest a fix. Distinguish real breakage (404s, bad relative paths, missing solution ZIPs) from transient failures (timeouts, 403s from sites that block bots).

External checks need network access. If requests are blocked by the sandbox firewall, report that instead of treating the links as broken.
