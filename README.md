# game-kaiser-java
An adaption of the old Kaiser/Sumeria game

## Build status
[![Github Action main branch status](https://github.com/ottlinger/game-kaiser-java/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/ottlinger/game-kaiser-java/actions)

[![codecov.io](https://codecov.io/github/ottlinger/game-kaiser-java/coverage.svg?branch=main)](https://codecov.io/github/ottlinger/game-kaiser-java?branch=main)

## Introduction

The game basically starts with a fixed population and land size.
Each round generates a harvest and you will be prompted to sell/buy/transform the harvest and the next round is about to begin.

## How to run

```
$ ./run.sh
```

Have fun :)

# DevHints

## BigDecimal

```java
firstBigDecimal.compareTo(secondBigDecimal) < 0 // "<"
firstBigDecimal.compareTo(secondBigDecimal) > 0 // ">"    
firstBigDecimal.compareTo(secondBigDecimal) == 0 // "=="  
firstBigDecimal.compareTo(secondBigDecimal) >= 0 // ">="   
```
