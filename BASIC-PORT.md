# KAISER.BAS port notes

The source of truth is the scanned listing dated 09.08.1992 in
[`kaiser_basic1992.pdf`](./kaiser_basic1992.pdf). Comments in the engine refer to
its BASIC line numbers. The Java port preserves the rules, including unusual
branches, rather than silently substituting rules from other Hamurabi variants.

## Names

| BASIC | Java | Meaning |
| --- | --- | --- |
| `Z` | `year` | Report year, 1 through 11 |
| `P` | `population` | Current inhabitants |
| `A` | `landArea` | Owned hectares |
| `S` | `grainStocks` | Stored grain in DZ |
| `Y` | `harvestYield`, `landPrice` | Previous harvest per hectare; later the current trading price |
| `H` | `totalHarvest` | Harvest quantity, **not humans** |
| `E` | `ratLoss` | Grain eaten by rats |
| `I` | `immigrants` | Arrivals reported at the next year start |
| `D` | planted area argument, `starvationDeaths` | Planted hectares; later deaths from starvation |
| `D1` | `totalStarvationDeaths` | Accumulated starvation deaths |
| `P1` | `averageStarvationPercentage` | Running average of annual starvation percentages |
| `Q` | `foodAllocated`, `plagueRoll` | Distributed grain; later next year's plague roll |
| `C` | local rolls, `immigrationFactor`, supported population | Several unrelated intermediate values |
| `L` | `getLandPerPerson()` | Unrounded hectares per inhabitant |

## Turn order and formulas

1. Show the introduction and request the start confirmation. The original access-code
   gate is deliberately omitted; it has no effect on the game rules.
2. Increment the year; report last year's starvation deaths and immigrants.
   Add immigrants to the population. If the previous plague roll is `<= 0`,
   replace population with `floor(population / 2)`.
3. Report population, owned land, previous harvest yield, rat losses and stocks.
   Year 11 proceeds directly to evaluation, without trading or planting.
4. Draw land price `floor(10 * RND) + 17`: **17–26**. Buy land, or offer selling
   if the purchase quantity is zero. Selling all owned land is forbidden.
5. Allocate grain to feeding and retain the quantity for the starvation calculation.
6. Validate planting: area `<=` owned land, `floor(area / 2) <` remaining stocks,
   area `< 10 * population`. Consume `floor(area / 2)` grain as seed.
   Planting zero bypasses these checks and seed consumption, **not** steps 7–9.
7. Draw yield `floor(5 * RND) + 1`: **1–5**. Harvest = planted area * yield.
   Draw another 1–5 roll. Even rolls cause `floor(stocks / roll)` rat damage;
   odd rolls cause no damage. Stocks = stocks - damage + harvest.
8. Draw another 1–5 roll as the immigration factor:
   `immigrants = floor(factor * (20 * land + stocks) / population / 100 + 1)`.
9. Supported population = `floor(foodAllocated / 20)`. Only now draw
   `plagueRoll = floor(10 * RND - 0.3)` for next year.
10. If supported population exceeds population, report zero starvation deaths
    next year, without changing population or the death statistics.
    Otherwise, deaths = population - supported population. More than 45% deaths
    ends the game immediately, before updating the accumulated statistics.
    For a surviving reign:
    `average = ((year - 1) * average + deaths * 100 / population) / year`;
    population = supported population; accumulated deaths += deaths.

Initialization of owned land and rat losses happens **only once**, not every year.
Plague victims do not count as starvation deaths. The year-11 population update
(including possible plague) affects the final land-per-person evaluation.

## Evaluation and replay

Test the following tiers in order, using unrounded percentages and land ratios:

| Tier | Condition |
| --- | --- |
| Disastrous | average deaths > 33% **or** land per person < 7 |
| Tyrannical | average deaths > 10% **or** land per person < 9 |
| Mediocre | average deaths > 3% **or** land per person < 10 |
| Excellent | otherwise |

The mediocre message includes `floor(population * 0.8 * RND)` unhappy residents.
Only that branch reaches the replay question in the original listing
(lines 959–966). A replay resets the reign and returns to the introduction and
start confirmation, without an access-code prompt.
Negative action quantities terminate the game via the equivalent of line 850.

## Java-specific details and tests

- The original access-code check (input at BASIC line 2, validation at lines
  1000–1100) is intentionally removed. New games and replays begin directly with
  the introduction and `Start (J/N)` confirmation. The code was only an access
  gate, not a random seed or game setting, so removing it does not change the rules.
- One shared scanner prevents buffered multi-line input from being lost.
  Decimal quantities are accepted using the BASIC decimal point. Invalid numeric
  input is requested again; end-of-input terminates cleanly.
- `BigDecimal` retains fractional quantities. Explicit BASIC `INT` operations
  use floor rounding; non-integer statistics use `DECIMAL128` precision.
- `Random.nextDouble()` supplies independent values in `[0, 1)`. This reproduces
  the listing's formulas and draw order, not a particular vintage interpreter's
  random sequence or floating-point representation.
- ANSI colours and banners remain. Hardware-specific screen/control characters
  and the busy-wait subroutine at line 3000 are not emulated.
- `BasicRandom` supplies finite deterministic RND traces in tests and fails on
  unexpected draws. Tests cover formulas, strict boundaries, all evaluation
  tiers, input retries/aborts, complete ten-year reigns, final plague and replay.

Run the regression suite with the Maven Wrapper: `./mvnw test`
(or `mvnw.cmd test` on Windows). No separate Maven installation is required.
