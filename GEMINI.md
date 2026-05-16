Project Goal: RuneLite to Discord SaaS for Clan/FFA loot tracking.

Tech Stack: Java (RuneLite API), Cloudflare Workers (Edge Logic), Cloudflare KV (Deduplication Cache).

Key Constraint: 60-second TTL for message hashes to ensure privacy and prevent duplicates.

Auth: X-SaaS-Token header for multi-tenancy.

The cloudflare section is in a seperate, private repo. This repo is only for the Runelite plugin.

See AGENTS.md for details about Runelite plugin guidelines.

See GEMINI_FULL_PLAN.md for the full architecture planning conversation.