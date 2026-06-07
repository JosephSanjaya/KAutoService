---
title: Code Structure Patterns
description: Patterns for organizing code structure, functions, variables, and data to maximize readability and maintainability
inclusion: auto
---

# Code Structure Patterns

**Functions:** narrative flow (high → low level); early returns first; extract complex blocks

**Variables:** declare close to first use; minimize lifetime

**Grouping:** blank lines between logical steps; related ops stay together

**Naming (Kotlin):** `camelCase` functions/vars · `PascalCase` classes · `SCREAMING_SNAKE_CASE` constants · `lowercase` packages · be specific: `userCount` not `count`

**Error handling:** throw, don't return null silently; fail fast with `require`/`check`

**Data structures:** Set for uniqueness, Map for lookup, List for ordered; data classes over Pair/Triple
