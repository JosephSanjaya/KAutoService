---
title: Readable Code Principles
description: Core principles for writing self-documenting, readable code with strict comment guidelines - comments explain why, not what
inclusion: auto
---

# Readable Code Principles

- Names reveal intent; no abbreviations unless universally understood
- Variables: nouns (`userCount`, `activeSessions`)
- Functions: verbs (`fetchUser`, `validateEmail`); <20 lines; single responsibility
- Booleans: predicates (`isValid`, `hasPermission`, `canEdit`)
- No deep nesting: early returns and guard clauses
- Extract complex conditions into named variables/functions

## Comments: why only, never what

Allowed: non-obvious business logic, workarounds for external bugs, non-obvious consequences, complex algorithms that can't be simplified.

Prohibited: explaining what obvious code does, restating code in English, commented-out code.

Before adding: can you rename, extract, or simplify instead? If yes — do that.
