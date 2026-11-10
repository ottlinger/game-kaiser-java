# Project guidance

This project ports the original **KAISER.BAS BASIC script**, dated 09.08.1992,
whose listing is checked in as [`kaiser_basic1992.pdf`](./kaiser_basic1992.pdf).
Use that listing as the source of truth for game rules and turn order, rather than
other versions of Kaiser or Hamurabi. [BASIC-PORT.md](./BASIC-PORT.md) explains the
variable mapping, formulas, original quirks and intentional Java deviations.

## Tests and verification

Keep changes to the rules covered by deterministic tests. Use `BasicRandom` for
fixed random traces; it fails on unexpected random draws. Cover resource boundaries,
rounding, state transitions and turn order, not just the absence of exceptions.

Use the Maven Wrapper: `./mvnw test` runs the regression suite, while `./mvnw verify`
also runs the configured verification steps, including mutation testing. Use
`mvnw.cmd` instead of `./mvnw` on Windows.

### Standard coverage: JaCoCo

As in `mailversendala`, JaCoCo runs in the normal Maven lifecycle: its agent records
coverage during tests, and its report is generated in the `test` phase. Run
`./mvnw clean verify` to produce `target/site/jacoco/index.html` for local inspection
and `target/site/jacoco/jacoco.xml` for coverage services. The JaCoCo report is also
included in the Maven site. Do not commit generated reports under `target/` or
update the generated site under `docs/` by hand.

CI uses the same `clean verify` command. The existing Codecov action uploads the
explicit JaCoCo XML path with automatic report discovery disabled, so an optional
Clover report cannot be mixed into the standard upload. Keep this XML path stable
when changing coverage configuration.

### Optional coverage: OpenClover

For OpenClover coverage, run `./mvnw -Pclover clean verify`. The optional profile
instruments the sources, runs the tests and generates HTML and XML reports at
`target/site/clover/index.html` and `target/site/clover/clover.xml`. JaCoCo and PIT
are disabled for that run to keep coverage instrumentation separate from the
normal verification tools. Afterwards, use `./mvnw clean verify` to return to a
normal, non-Clover build; do not reuse instrumented classes for a release.

## Java imports and test assertions

Use explicit imports for classes and static members; wildcard imports are not allowed.
The shared `.editorconfig` prevents IntelliJ IDEA from collapsing imports into wildcards.
Checkstyle enforces this rule for production and test sources during Maven's `validate`
phase, so it also runs with `./mvnw test` and the CI build.

Use AssertJ for test assertions, including exception checks with `assertThatThrownBy`.
JUnit remains the test framework for annotations and parameterized tests. AssertJ's
version is managed through the imported `assertj-bom`, using the `assertj.version`
property inherited from the parent POM; do not version individual AssertJ dependencies.
