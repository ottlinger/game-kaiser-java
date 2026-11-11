# game-kaiser-java
An adaption of the old Kaiser/Sumeria game

## Build status
[![Github Action main branch status](https://github.com/ottlinger/game-kaiser-java/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/ottlinger/game-kaiser-java/actions)

[![codecov](https://codecov.io/github/ottlinger/game-kaiser-java/graph/badge.svg?token=PLNFT7G9OR)](https://codecov.io/github/ottlinger/game-kaiser-java)

[![CodeQL](https://github.com/ottlinger/game-kaiser-java/actions/workflows/codeql.yml/badge.svg?branch=main)](https://github.com/ottlinger/game-kaiser-java/actions/workflows/codeql.yml)

## Introduction

The game basically starts with a fixed population and land size.
Each round generates a harvest, and you will be prompted to sell/buy/transform the harvest and the next round is about to begin.

It is based on an old [📃 KAISER.BAS](./kaiser_basic1992.pdf) basic listing I found when I did some spring-cleaning.

## How to run

**Before your first game:** Read the German [Spielanleitung](./INTRO.md) for the
game's objective, turn order, required quantities and an example first round.

```
$ ./run.sh
```

The game opens directly with the introduction; no access code is required.
Confirm `Start (J/N)` with `j` to begin.
Enter `0` when you do not want to buy land; you can then sell land instead.
Negative quantities end the game, as in the BASIC listing. Decimal quantities use a point.

The Java rules and turn order follow the checked-in listing, including its strict
seed/worker limits and final report in year 11 after ten action years.
See [BASIC port notes](./BASIC-PORT.md) for variable names, formulas and original quirks.

Have fun :)
 
## Site reports

The [project site](https://ottlinger.github.io/game-kaiser-java/) includes this
README, the [Spielanleitung](./INTRO.md), [BASIC port notes](./BASIC-PORT.md), the
original BASIC listing and contribution documentation, alongside the technical
reports (coverage, Javadoc, tests and more).

`site.sh` publishes the generated site to `docs/`, commits and pushes it.

# DevHints

## Old basic interpreter

* https://repl.endbasic.dev/

## Console Colours

* https://talyian.github.io/ansicolors/
