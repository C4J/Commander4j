# Jar / dependency cleanup — Commander4j core (Scratchpad)

**Date of work:** 2026-05-03

This note records the jar/dependency tidy-up done on the c4j_commander4j Scratchpad
workspace, including the creation of the local **c4j_cups4j** fork. Keep this as a
running reference — jar problems never go away, and the rationale for each decision
matters as much as the change itself.

---

## 1. Goal

Move the entire project to a **single, coherent jakarta.xml.bind stack**, eliminate
classpath bloat from legacy `javax.xml.bind` jars, and stop the silent runtime
breakage caused by deps that resolved to the wrong namespace or version.

Three independent issues collapsed into one workstream:

1. **cups4j 0.8.1** still binds against `javax.xml.bind` (legacy namespace).
   Pulling it into a Jakarta EE 9+ project meant **two complete JAXB stacks** on
   the classpath simultaneously.
2. **JasperReports 7.0.6** ships a `jasperreports-javaflow` jar that
   bundles a duplicate `jasperreports_extension.properties`, producing a
   startup `WARN` and 3.8 MB of dead weight.
3. **Several `.jrxml` reports** referenced `org.apache.commons.lang.StringUtils`
   (commons-lang **2.x**) but the project only has `commons-lang3`. Reports threw
   `groovy.lang.MissingPropertyException: No such property: org` when those
   expressions evaluated.

---

## 2. The c4j_cups4j fork

A private fork of cups4j 0.8.1 with `javax.xml.bind` swapped for `jakarta.xml.bind`.
**Not** contributed back upstream — it lives entirely inside the Commander4j tree.

### Location and coordinates

| | |
|---|---|
| Source workspace | `/Users/dave/Commander4j/Source/b8/Scratchpad/c4j_cups4j/` |
| Maven coordinates | `com.commander4j:c4j_cups4j:0.8.1-jakarta` |
| Local m2 install | `~/.m2/repository/com/commander4j/c4j_cups4j/0.8.1-jakarta/` |
| Original Maven-layout source (no longer canonical) | `~/Downloads/cups4j-version-0-8-1/` |

### Layout

Eclipse-flat (no `src/main/java`):

```
c4j_cups4j/
├── src/                              ← Java sources directly under here
│   ├── ch/ethz/vppserver/ippclient/
│   ├── org/cups4j/
│   └── config/ippclient/             ← XML resources (must stay at this path)
│       ├── ipp-list-of-tag.xml
│       └── ipp-list-of-attributes.xml
├── lib/                              ← compile classpath (14 jars)
├── pom.xml                           ← Maven build (sourceDirectory=src)
├── build.xml                         ← Ant build (default target = jar)
├── getdependencies.sh                ← runs mvn dependency:copy-dependencies
├── c4j_cups4j-0.8.1-jakarta.jar      ← Ant build output (project root)
└── .classpath                        ← Eclipse: 1 src + 14 lib entries
```

Resources **must** live at `src/config/ippclient/` because
`IIppAttributeProvider.TAG_LIST_FILENAME` is the literal string
`config/ippclient/ipp-list-of-tag.xml`. If the IPP XMLs go missing or move,
`IppAttributeProvider.getInstance()` throws `RuntimeException: Error
unmarshalling tag list` on first use.

### Build entrypoints

| Command | Effect |
|---|---|
| `ant jar` (or just `ant`) | Compile `src/` → `bin/`, jar into project root |
| `ant clean jar` | Wipe `bin/` and the old jar, then rebuild fresh |
| `ant rebuild` | Alias for `clean jar` |
| `bash getdependencies.sh` | `mvn dependency:copy-dependencies` → `target/dependency/` |
| `mvn clean install` | Build + install to `~/.m2/` so c4j_commander4j can resolve it |

### Dependencies declared

```
jakarta.xml.bind:jakarta.xml.bind-api:4.0.5     (compile)
org.glassfish.jaxb:jaxb-runtime:4.0.7           (runtime)
org.apache.httpcomponents.client5:httpclient5:5.4.3
org.slf4j:slf4j-api:2.0.17
commons-io:commons-io:2.21.0
org.apache.commons:commons-lang3:3.20.0
commons-codec:commons-codec:1.21.0
```

Versions chosen to align with c4j_commander4j's existing direct declarations.
`jakarta.xml.bind-api` set at `4.0.5` (matches `jaxb-runtime:4.0.7`'s own
internally-declared API version, avoiding mediation noise). Java release target
is **21** (matches c4j_commander4j's `<maven.compiler.release>`).

### Source changes vs upstream cups4j 0.8.1

1. **13 import lines**, 12 files: `javax.xml.bind.*` → `jakarta.xml.bind.*`. No
   API surface changes — Jakarta XML Binding 4.0 is package-rename-only for the
   surface this codebase uses (`@XmlRootElement`, `@XmlElement`,
   `JAXBContext.newInstance(Class)`, `Unmarshaller.unmarshal(InputStream)`).
2. **`URL` → `URI` cleanups** (post-migration, separate concerns):
   - `CupsGetPrintersOperation.java:65` — `new URL(cupsURL + "/printers")` → `URI.create(cupsURL + "/printers")`; URL import dropped.
   - `IppCancelJobOperation.java:114-119` — `URL url`/`URL urlService` → `String url`/`URI urlService`; URL import dropped.
   - `IppCreateJobOperation.java:84` — internal call switched to `getIppHeader(URI.create(url.toString()), createAttributeMap())`.
   - `IppCreateJobOperation.java:167-171` — added `@Deprecated(forRemoval = true) @Override @SuppressWarnings("removal")` on the `request(URL,Map,CupsAuth)` override.
   - `IppGetJobAttributesOperation.java:156, 162` and `IppGetJobsOperation.java:129, 135` — `new URL(s)` → `URI.create(s).toURL()` (Java 20+ deprecation).
   - `IppCreateJobOperation.java:218` — `IOUtils.toString(InputStream).getBytes()` → `IOUtils.toByteArray(InputStream)` (avoids charset roundtrip on raw bytes).
3. **Pom rewrite** (sourceDirectory + resources for Eclipse-flat layout, jakarta deps, stripped upstream's release-publishing plugins which fail on JDK 25 / require GPG / target Sonatype Central).

Build status after all of the above: **0 warnings**.

---

## 3. Cleanup of c4j_commander4j's pom and lib

### XML library swap

**Before:** lib had **both** the legacy javax stack (from cups4j's transitives)
and the modern jakarta stack (from direct declarations). Two complete sets of
JAXB jars on the classpath.

**After:** single jakarta stack only.

Removed from lib:

| jar | reason |
|---|---|
| `cups4j-0.8.1.jar` | Replaced by `c4j_cups4j-0.8.1-jakarta.jar` |
| `jaxb-api-2.3.1.jar` | Legacy `javax.xml.bind` API |
| `jaxb-impl-2.3.5.jar` | Legacy `javax.xml.bind` runtime |
| `jakarta.xml.bind-api-2.3.3.jar` | **Misleading** — bridge jar with `jakarta` groupId but `javax.xml.bind` package contents |
| `javax.activation-api-1.2.0.jar` | Legacy javax activation API |
| `jakarta.activation-1.2.2.jar` | `com.sun.activation:jakarta.activation` bridge runtime impl |

Kept (jakarta side):

```
jakarta.xml.bind-api-4.0.5.jar
jaxb-runtime-4.0.7.jar
jaxb-core-4.0.7.jar
istack-commons-runtime-4.1.2.jar
jakarta.activation-api-2.1.4.jar
angus-activation-2.0.3.jar
```

Pom changes in `c4j_commander4j/pom.xml`:

```xml
<properties>
    <jaxb.version>4.0.7</jaxb.version>
    <jaxb.api.version>4.0.5</jaxb.api.version>     <!-- added -->
</properties>

<!-- added explicit declaration so version isn't mediated -->
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>${jaxb.api.version}</version>
</dependency>
```

### jasperreports-javaflow removal

Removed because:

- Two jars (`jasperreports-7.0.6.jar` and `jasperreports-javaflow-7.0.6.jar`)
  ship byte-for-byte-identical `jasperreports_extension.properties`. The
  `DefaultExtensionsRegistry` warns about the duplicate at every startup.
- `jasperreports-javaflow` is a near-complete repackaging (2,383 class files)
  contributing only **two** unique classes:
  `JRContinuationSubreportRunner.class` + `JRContinuationSubreportRunnerFactory.class`.
- Commander4j explicitly opts into the **other** runner via
  `reports/jasperreports.properties` line 155:
  `net.sf.jasperreports.subreport.runner.factory=…ThreadPoolSubreportRunnerFactory`.
- `grep` of source + `.jrxml` files shows zero references to javaflow /
  continuation runners.

So: 3.8 MB jar contributing only the warning. Removed from pom + lib.

### commons-lang 2.x → lang3 in reports

**Symptom:** running `pallet_a4_x1` (and 6 other reports) threw:

```
groovy.lang.MissingPropertyException: No such property: org
  for class: pallet_a4_x1_<hash>
…
Error evaluating expression for source text:
  org.apache.commons.lang.StringUtils.leftPad($F{EAN},14,"0")
```

**Cause:** the reports referenced `org.apache.commons.lang.*` (commons-lang
**2.x** package) but the classpath only ever had `commons-lang3`. No jar
provided `org.apache.commons.lang.StringUtils` — the reports had been broken
since whenever lang3 replaced lang on this classpath. Nothing about the cups4j
or javaflow work caused it; the jakarta migration just happened to be the
moment those reports were re-run.

**Verified scope:**

| Path | Result |
|---|---|
| `lib/*.jar` providing `org.apache.commons.lang.StringUtils` | **0 of 118 jars** |
| `mvn dependency:tree -Dverbose` for any commons-lang 2.x | **0 nodes** |
| `.jrxml` files using `org.apache.commons.lang.` | **7** |
| `.jrxml` files using `org.apache.commons.lang3.` | **0** |

**Fix:** mechanical search-and-replace across the 7 affected reports
(`org.apache.commons.lang.StringUtils` → `org.apache.commons.lang3.StringUtils`,
65 total occurrences). The `leftPad(String, int, String)` signature is
identical between 2.x and 3.x.

Affected reports:
- `pack_label_01.jrxml` (6)
- `pack_label_02.jrxml` (6)
- `pallet_a4_x1.jrxml` (12)
- `pallet_a4_raw_x1.jrxml` (9)
- `pallet_a5_x1.jrxml` (12)
- `pallet_letter_x1.jrxml` (12)
- `univer_pallet_a4_x1.jrxml` (8)

After the source edits, **all 137** `.jrxml` files were recompiled via
`compilereports.sh` to refresh the `.jasper` binaries (the old `.jasper` files
had the broken expressions baked in).

**Project-wide audit afterwards:** scanned every `.jrxml` for fully-qualified
class references and `<import>` declarations. Across all 137 reports the only
non-JDK class reference is `org.apache.commons.lang3.StringUtils`. No custom
scriptlets, no custom subreport runner factories, no other risky refs.

### Decoupling c4j_cups4j from Maven's tree

After all the above worked, the c4j_cups4j dep was **removed** from
`c4j_commander4j/pom.xml` so the jar can be managed independently of Maven
resolution. Reasoning:

- It's the only artifact that lives entirely in local m2 (not on Maven Central).
  Without it Maven, `getdependencies.sh` could be run on any machine without
  needing the local fork pre-installed.
- Manual management is fine — the jar updates rarely.
- It produces a clean diff signal: the **only** lib-vs-target difference will
  be `c4j_cups4j-0.8.1-jakarta.jar`. Any other diff is a real anomaly.

To preserve the HttpClient 5 jars that c4j_cups4j was contributing transitively,
`httpclient5:5.4.3` was added as a direct dependency in c4j_commander4j's pom.

### Final lib-vs-target reconciliation

After all of the above, ran `getdependencies.sh` and reconciled `lib/` against
`target/dependency/`:

**Removed from lib (orphans / duplicates):**

| jar | why orphan |
|---|---|
| `jgoodies-common-1.6.0.jar` | Was a transitive of upstream cups4j 0.8.1; not used by any project source (`grep com.jgoodies` → 0 hits). The c4j_cups4j fork dropped this dep deliberately. |
| `jgoodies-forms-1.7.1.jar` | Same. |
| `net.tascalate.javaflow.api-2.7.5.jar` | Was a transitive of `jasperreports-javaflow` (now removed). No source references. |
| `groovy_4.0.31.jar` | Same content as `groovy-4.0.31.jar` (sha1 `334bf3cc…`); just a renamed copy. |

**Added to lib:** `groovy-4.0.31.jar` (Maven's canonical filename, replacing
the underscore-named copy).

**Final lib state:** **115 jars**. Matches `target/dependency` exactly except
for `c4j_cups4j-0.8.1-jakarta.jar` (the deliberate out-of-band entry).

---

## 4. Workflow going forward

### Refreshing dependencies

```
cd c4j_commander4j
bash getdependencies.sh
diff <(ls target/dependency/*.jar | xargs -n1 basename | sort) \
     <(ls lib/*.jar | xargs -n1 basename | sort)
```

Expected output:

```
> c4j_cups4j-0.8.1-jakarta.jar
```

Anything else in the diff is a real change to investigate before propagating
into `lib/`.

### Updating the c4j_cups4j fork

When you change anything in the c4j_cups4j source:

```
cd /Users/dave/Commander4j/Source/b8/Scratchpad/c4j_cups4j
ant clean jar          # builds c4j_cups4j-0.8.1-jakarta.jar in project root
mvn clean install      # also installs to ~/.m2/ for any future Maven re-coupling
cp ~/.m2/repository/com/commander4j/c4j_cups4j/0.8.1-jakarta/c4j_cups4j-0.8.1-jakarta.jar \
   /Users/dave/Commander4j/Source/b8/Scratchpad/c4j_commander4j/lib/
```

### Recompiling .jasper files after .jrxml edits

```
cd c4j_commander4j
bash compilereports.sh    # ant -f compile_reports.xml compile-reports
```

This deletes all `.jasper` files and rebuilds every `.jrxml` against the
current `lib/*.jar` classpath. ~2 seconds for the full set.

---

## 5. Architectural decisions worth remembering

### Two namespaces, one project

`javax.xml.bind` and `jakarta.xml.bind` are **different classes to the JVM**.
They can coexist on a classpath without compile errors, but they're not
interchangeable at runtime — code compiled against one cannot bind to the
other. The whole point of this work was getting Commander4j onto exactly one
of them (jakarta). Never let a future dep update silently re-introduce the
javax stack — watch for `javax.activation*`, `javax.xml.bind*`,
`com.sun.xml.bind*`, `com.sun.activation:jakarta.activation` (a bridge jar
with `javax.xml.bind` package contents under a jakarta groupId — confusingly
named).

### `jakarta.xml.bind-api:2.3.x` is **not** the jakarta namespace

Versions 2.3.2 and 2.3.3 of the `jakarta.xml.bind:jakarta.xml.bind-api`
artifact still ship `javax.xml.bind.*` classes. The first version with
`jakarta.xml.bind.*` classes is **3.0.0**. Anything `4.0.x` is
`jakarta.xml.bind` and Java 11+ baseline. **Don't trust the groupId; check
the package contents** if you ever see a 2.3.x version of this artifact
appear.

### Reports are compiled artefacts too

JasperReports compiles `.jrxml` → `.jasper`. After **any** classpath change
that affects what the reports' Groovy/Java expressions can resolve, the
`.jasper` files must be rebuilt (`compilereports.sh`). Otherwise the runtime
runs old expressions baked into stale binaries.

### Groovy expression resolution is dynamic

Groovy doesn't fail at JR compile time when a class reference is unresolvable
— it fails at runtime when the expression is first evaluated. So a green
`compilereports.sh` is **not** proof that all expressions will resolve
correctly. Run a representative report (or do an exhaustive FQN scan, like
the audit done here).

### When Maven mediation surprises you

`mvn dependency:tree -Dverbose` shows omitted-for-conflict / omitted-for-duplicate
nodes. If a jar appears in lib at a version you didn't expect, this is the
first place to look. Direct declarations always win over transitives at any
depth — so when you want to lock a version, declare it directly even if a
transitive is already pulling it.

---

## 6. File index

| Path | Purpose |
|---|---|
| `c4j_commander4j/pom.xml` | Direct deps for the main app; c4j_cups4j explicitly **not** listed; `jakarta.xml.bind-api:4.0.5` and `httpclient5:5.4.3` declared directly. |
| `c4j_commander4j/getdependencies.sh` | Single-line wrapper around `mvn dependency:copy-dependencies`. |
| `c4j_commander4j/compilereports.sh` | `ant -f compile_reports.xml compile-reports` — rebuilds all `.jasper` files. |
| `c4j_commander4j/compile_reports.xml` | Ant build for the report compilation. |
| `c4j_commander4j/lib/` | Final classpath — 115 jars matching `target/dependency` + `c4j_cups4j-0.8.1-jakarta.jar`. |
| `c4j_commander4j/reports/*.jrxml` | Report sources (137 files); 7 were edited for the lang→lang3 fix. |
| `c4j_commander4j/reports/*.jasper` | Compiled report binaries — regenerate after any classpath/source change. |
| `c4j_cups4j/` | The local jakarta fork. Independent build (Ant + Maven both work). |
| `~/Downloads/cups4j_jakarta_migration_issue.md` | Draft GitHub issue prepared for upstream cups4j maintainer; **never filed** — the fork is the chosen path. |

---

## 7. c4j_web_react webapp follow-up (2026-05-03)

The same dual-stack problem existed in the b8 `c4j_web_react` mobile/RF webapp.
Migration done in a single session, mirroring the c4j_commander4j approach.

### Pom (`b7web_pom.xml`) changes

- Removed `org.cups4j:cups4j:0.8.1` (manage jar manually, like c4j_commander4j).
- Removed `commons-lang:commons-lang:2.6` (active goal: eliminate legacy 2.x).
- Removed `httpclient` 4.5.14 + `httpcore` 4.4.16 (HC4) — webapp source has zero
  HC refs; commander4j.jar uses HC5 only since the c4j_cups4j fork.
- Added `httpclient5:5.4.3`, `jakarta.xml.bind-api:4.0.5`, `jaxb-runtime:4.0.7`.
- Bumped versions to match lib reality: commons-codec 1.18→1.21, commons-io
  2.18→2.21, commons-lang3 3.17→3.20, commons-logging 1.3.3→1.3.6, commons-text
  1.13→1.15, log4j-* 2.24.3→2.25.3, slf4j-* 2.0.16→2.0.17, jakarta.activation-api
  2.1.3→2.1.4, angus-activation 2.0.2→2.0.3.
- Same "managed outside Maven" comment block as c4j_commander4j.

### `WebContent/WEB-INF/lib/` reconciliation

Removed: `cups4j-0.8.1.jar`, `jaxb-api-2.3.1.jar`, `jaxb-impl-2.3.5.jar`,
`javax.activation-api-1.2.0.jar`, `jgoodies-common-1.6.0.jar`,
`jgoodies-forms-1.7.1.jar`, `httpclient-4.5.14.jar`, `httpcore-4.4.16.jar`.

Added: `c4j_cups4j-0.8.1-jakarta.jar` (copied from c4j_commander4j/lib) plus the
modern jakarta jaxb 4.x stack and angus mail/activation from `target/dependency`.

### Two extra cleanups not in the c4j_commander4j cleanup

**`simple-xml-2.7.1.jar` and its 3 transitives (`stax-1.2.0`, `stax-api-1.0.1`,
`xpp3-1.1.3.3`) removed.** A `jdeps` audit across `commander4j.jar`, the
c4j_cups4j fork, and the webapp source showed **zero** references to
`org.simpleframework.*` or `org.xmlpull.*`. Dave had previously pruned these
jars from `WEB-INF/lib/` but forgot to drop the pom declaration; running
`getdependencies.sh` was quietly bringing them back. Pom now reflects reality.

**`xalan-2.7.3.jar` removed — was actively dangerous.**

- No source uses `org.apache.xalan.*` or `org.apache.xpath.*`. The
  `JXSLTransformer` and `JXMLDocument` classes in commander4j.jar use plain
  JAXP (`TransformerFactory.newInstance()`, `XPathFactory.newInstance()`).
- `xalan-2.7.3.jar` ships
  `META-INF/services/javax.xml.transform.TransformerFactory` and
  `META-INF/services/javax.xml.xpath.XPathFactory` pointing at
  `org.apache.xalan.processor.TransformerFactoryImpl` and
  `org.apache.xpath.jaxp.XPathFactoryImpl` — these **hijack the JDK's bundled
  transformer** via `ServiceLoader`.
- xalan 2.7.x was split into `xalan` + `xalan-serializer` artifacts; we never
  shipped the serializer. `jdeps -v xalan-2.7.3.jar` shows ~25 unmet refs to
  `org.apache.xml.serializer.*` and `org.apache.xerces.parsers.*`. So the
  hijacked factory is itself broken — any code path reaching the serializer
  would throw `NoClassDefFoundError`.
- Removing xalan lets the JDK's complete bundled fork at
  `com.sun.org.apache.xalan.internal.*` take over via the same ServiceLoader
  mechanism. Strictly safer.

**Open follow-up (not done in this session):** the same broken xalan-2.7.3 jar
likely sits in `c4j_commander4j/lib/`, since `JXSLTransformer` is in the
desktop's source tree and its dependency graph fed the original webapp.
Apply the same removal there next time.

### Third cleanup: ant + ant-launcher removed (later same day)

`org.apache.ant:ant:1.10.14` was a direct pom declaration pulling in
`ant-1.10.14.jar` and `ant-launcher-1.10.14.jar`. Audit found:

- Webapp Java/JSP source: **0** references to `org.apache.tools.ant`.
- `commander4j.jar` (`jdeps -v`): **0** references to `org.apache.tools.ant`.
- `build.xml`: only uses Ant property syntax (`${ant.project.name}`); the
  build is invoked by external Ant (Eclipse / system), not by this jar
  programmatically.

Pure dead weight. Removed the dependency block from `b7web_pom.xml` and
deleted both jars from `WebContent/WEB-INF/lib/`. Diff vs `target/dependency/`
remained clean (only the 5 deliberate out-of-band entries).

The mail stack (`angus-mail`, `jakarta.mail-api`, `angus-activation`) was
audited at the same time but **left in place**: `commander4j.jar` ships
`com.commander4j.email.SendEmail` which uses `jakarta.mail.*`. No webapp code
path currently invokes it, but the jars are kept as a `NoClassDefFoundError`
safety net in case any forgotten path reaches `SendEmail` at runtime.

### Final state

- `WebContent/WEB-INF/lib/`: **29 jars**.
- Diff vs `target/dependency/`: only the 5 deliberate out-of-band entries
  (`c4j_cups4j-0.8.1-jakarta.jar`, `commander4j.jar`, `mssql-jdbc`,
  `mysql-connector-j`, `ojdbc17`).
- `ant clean war` clean; audit confirmed zero jars expose `javax/xml/bind/`
  or `org.apache.commons.lang.*`.

### Build.xml side note (left unchanged)

`build.xml` declares both `<fileset dir="WebContent"/>` and
`<lib dir="WebContent/WEB-INF/lib"/>` inside the `<war>` task. The fileset
already includes `WebContent/WEB-INF/lib/`, so the `<lib>` element duplicates
every jar in the WAR. Cosmetic, not functional — fix is to drop the `<lib>`
element. Deferred at Dave's request.

---

## 8. c4j_web_WS jakarta migration + HC5 stack alignment + mail bumps (2026-05-03 continued)

After sections 1–7, an inter-project dependency conflict report
(`~/Downloads/project_dependencies_conflicts.xml`) flagged remaining version
drift across the 7 c4j modules. Three actionable items emerged.

### c4j_web_WS — finished the jakarta.xml.bind migration

The third webapp (Ant-only, no pom; Eclipse + ant build to
`target/c4j_web_WS.war`). `WEB-INF/lib/` is the source of truth.

Pre-state was already much cleaner than desktop or c4j_web_react had been
before their migrations: `jakarta.xml.bind-api-3.0.1.jar` with **no JAXB
runtime, no legacy javax stack, no xalan, no commons-lang 2.x**. The 3.0.1 API
jar was on the classpath only because Jersey expects it; a source audit found
zero direct JAXB usage in the app's own code (no `JAXBContext`, no `@XmlRoot`,
no `javax.xml.bind`). 3.0.x and 4.0.x are both the real `jakarta.xml.bind.*`
namespace and binary-compatible for the surface Jersey 3.0.x touches.

Changes:
- Swapped `jakarta.xml.bind-api-3.0.1.jar` → `jakarta.xml.bind-api-4.0.5.jar`
  (copied from `c4j_commander4j/lib/`).
- Removed `jakarta.ws.rs-api-3.0.0-sources.jar` — a sources jar accidentally
  shipped in `WEB-INF/lib/` (~244 KB, treated by Tomcat as classes).

`ant clean war`: BUILD SUCCESSFUL. WAR ships exactly one jaxb-api 4.0.5; zero
`javax/xml/bind/*` classes anywhere.

Out-of-scope items noted but not touched:
- `jakarta.servlet-api-5.0.0.jar` bundled in `WEB-INF/lib/` while `web.xml`
  declares 6.0 and Tomcat 11 provides the API at runtime.
- ~15 Jersey/HK2/yasson/parsson jars potentially dead weight (CLAUDE.md says
  the app uses servlet mappings + Gson, not JAX-RS) — bigger investigation,
  deferred.
- build.xml's duplicate `<fileset>` + `<lib>` jar listing in `<war>` (same
  pattern as c4j_web_react's deferred issue).

### HC5 stack alignment — c4j_commander4j + c4j_web_react bumped to match middleware

The conflicts report showed `c4j_middleware4j` independently bumped to
`httpclient5:5.6 + httpcore5:5.4.2 + httpcore5-h2:5.4.2` while c4j_commander4j
and c4j_web_react were on `httpclient5:5.4.3 + httpcore5:5.3.4`. Dave chose to
align desktop + react webapp to middleware's stack.

**Coupling note:** httpclient5 and httpcore5 are tested as a paired stack —
bumping one without the other is an untested combo. The full HC5 trio moved
together.

**Maven gotcha:** `httpclient5:5.6` transitively pulls `httpcore5:5.4` (not
5.4.2). To match middleware exactly, explicit `httpcore5:5.4.2` and
`httpcore5-h2:5.4.2` declarations were added alongside the httpclient5 bump.

Source audit beforehand confirmed safety:
- desktop: zero `org.apache.hc.*` references (HC5 only enters via the cups4j jar).
- cups4j: 16 stable HC5 classes used (`HttpClients`, `CloseableHttpClient`,
  `ConnectionConfig`, `HttpPost`, `ClassicHttpResponse`, `EntityUtils`, etc.) —
  all part of the stable HC5 5.0+ API surface; nothing deprecated/removed in
  5.5/5.6.
- web_react: zero HC refs (already documented).

Changes:
- `c4j_commander4j/pom.xml`: `httpclient5` 5.4.3 → 5.6, plus explicit
  `httpcore5` + `httpcore5-h2` 5.4.2 overrides.
- `c4j_web_react/pom.xml`: same three changes.
- `c4j_cups4j/pom.xml`: `httpclient5` 5.4.3 → 5.6.
- All three lib/ directories swapped to the new HC5 trio (3 jars per project).

Builds: `ant clean build` (commander4j) + `ant clean war` (web_react) both clean.

#### HC5 5.6 runtime regression — cups4j source fix required

The "no cups4j rebuild needed" assumption (HC5 5.x binary compat) **turned
out to be wrong on one code path**. First desktop launch failed with:

```
java.lang.ExceptionInInitializerError
    at org.cups4j.operations.IppOperation.sendRequest(IppOperation.java:222)
    ...
Caused by: java.lang.NullPointerException: Cannot invoke
  "org.apache.hc.core5.util.TimeValue.divide(long, java.util.concurrent.TimeUnit)"
  because "this.maxIdleTime" is null
    at org.apache.hc.client5.http.impl.classic.HttpClientBuilder.build(HttpClientBuilder.java:1118)
    at org.cups4j.operations.IppHttp.<clinit>(IppHttp.java:37)
```

`IppHttp.<clinit>` (the cups4j fork's static initializer that builds the
shared `CloseableHttpClient`) called `.evictExpiredConnections()` standalone.
In HC5 5.4.x that worked; in HC5 5.6 the build path now references
`maxIdleTime` even when only the no-arg expired-connections evictor was
configured, NPEing because no `.evictIdleConnections(TimeValue)` companion
call was made.

Fix: one-line addition in
`c4j_cups4j/src/org/cups4j/operations/IppHttp.java`:

```java
.evictExpiredConnections()
.evictIdleConnections(Timeout.ofSeconds(30))   // added — HC5 5.6 NPEs otherwise
.setRetryStrategy(new DefaultHttpRequestRetryStrategy())
```

Then: `ant clean jar` + `mvn clean install` (refreshes
`~/.m2/repository/com/commander4j/c4j_cups4j/0.8.1-jakarta/`) and copy the
rebuilt `c4j_cups4j-0.8.1-jakarta.jar` into both
`c4j_commander4j/lib/` and `c4j_web_react/WebContent/WEB-INF/lib/`. Confirmed
working.

**Lesson:** Apache HC5's "5.x line is binary-compatible" claim isn't airtight.
Watch the `HttpClientBuilder` connection-eviction methods specifically when
bumping HC5 — `evictExpiredConnections()` alone now requires an
`evictIdleConnections(TimeValue)` companion to avoid the NPE in `build()`.

### c4j_web_react mail stack pom alignment

Two mail-related entries in the conflicts report — both isolated to web_react:

- `jakarta.mail-api`: web_react=2.1.3, all others=2.1.5.
- `angus-mail`: web_react=2.0.3, all others=2.0.5.

In both cases the report was scanning poms — the `WEB-INF/lib/` jars showed
the lib was already at 2.0.5 for angus-mail (classic "lib is authoritative"
drift). So the fix was a pom + jar swap for jakarta.mail-api, and a pom-only
update for angus-mail.

Changes:
- `c4j_web_react/pom.xml`: `jakarta.mail-api` 2.1.3 → 2.1.5 (with lib jar swap).
- `c4j_web_react/pom.xml`: `angus-mail` 2.0.3 → 2.0.5 (no jar swap; lib already
  on 2.0.5).

After all three rounds the lib-vs-target diff for c4j_web_react is **exactly**
the documented 5 out-of-band entries (`c4j_cups4j`, `commander4j.jar`,
`mssql-jdbc`, `mysql-connector-j`, `ojdbc17`). No version skew remaining.

### Side fix: c4j_web_react getdependencies.sh script rename

`c4j_web_react/getdependencies.sh` referenced `b7web_pom.xml` but the file has
been renamed to `pom.xml`. Script updated to use the new filename. Earlier
sections of this document (notably section 7) still mention `b7web_pom.xml` —
those are historically accurate; the rename happened later.

### Final cross-project HC5 + jakarta state

| | c4j_commander4j | c4j_web_react | c4j_web_WS | c4j_middleware4j | c4j_cups4j (pom) |
|---|---|---|---|---|---|
| jakarta.xml.bind-api | 4.0.5 | 4.0.5 | 4.0.5 | n/a | (via deps) |
| httpclient5 | 5.6 | 5.6 | n/a | 5.6 | 5.6 |
| httpcore5 / -h2 | 5.4.2 | 5.4.2 | n/a | 5.4.2 | (transitive) |

---

## 9. jasperreports-data-adapters-http removal — eliminates HC4 stack from desktop (2026-05-03)

Spotted while auditing what still pulled in the legacy Apache HC4 jars.
`httpclient-4.5.14.jar` and `httpcore-4.4.16.jar` were sitting in
`c4j_commander4j/lib/` even though the project had moved to HC5 5.6 in section 8.

### Source

`mvn dependency:tree -Dincludes=org.apache.httpcomponents:httpclient,org.apache.httpcomponents:httpcore -Dverbose`:

```
\- net.sf.jasperreports:jasperreports-data-adapters-http:jar:7.0.6
   +- org.apache.httpcomponents:httpclient:jar:4.5.14
   |  \- (org.apache.httpcomponents:httpcore:jar:4.4.16 - omitted for duplicate)
   \- org.apache.httpcomponents:httpcore:jar:4.4.16
```

Single root cause: `jasperreports-data-adapters-http:7.0.6` declared at
`pom.xml:541-547`. That jar provides 9 classes in
`net.sf.jasperreports.data.http` / `net.sf.jasperreports.dataadapters.http`
(`HttpDataConnection`, `HttpDataService`, `HttpLocationParameter`,
`StandardHttpDataLocation`, `HttpDataExtensionsRegistryFactory`,
`HttpDataFileServiceFactory`, `HttpDataLocation`, `RequestMethod`, plus an
inner class) — JasperStudio's "HTTP Data Adapter" plumbing for fetching
report datasets over HTTP instead of via JDBC.

### Verified usage = zero

| Path | HTTP-adapter refs |
|---|---|
| `src/**/*.java` | 0 |
| `reports/**/*.jrxml` | 0 |
| `.jrdax` data-adapter definition files | none exist |

Commander4j's reports use JDBC connections. The only `org.apache.http*`
mentions in the project are three `<Logger>` entries in `xml/log/log4j2.xml`
(lines 33, 36, 39) — SLF4J logger-name suppression rules; no classpath
dependency.

Same dead-weight pattern as `jasperreports-javaflow` (section 3).

### Changes

- `c4j_commander4j/pom.xml`: removed the `jasperreports-data-adapters-http`
  dependency block.
- `c4j_commander4j/lib/`: deleted `jasperreports-data-adapters-http-7.0.6.jar`,
  `httpclient-4.5.14.jar`, `httpcore-4.4.16.jar` (3 jars in total).
- `getdependencies.sh` re-resolved cleanly; lib-vs-target diff: only the
  documented `c4j_cups4j-0.8.1-jakarta.jar` out-of-band entry.
- Lib count: 115 → 112 jars.
- `ant clean build`: BUILD SUCCESSFUL.

`.jasper` files were **not** rebuilt — no `.jrxml` referenced any of the
removed classes, so there's nothing to re-resolve. (Per section 5, recompile
reports whenever a classpath change *could* affect what an expression
resolves to; this change can't.)

### Result

Apache HC4 is now **completely absent** from c4j_commander4j (and from every
other c4j module — c4j_web_react dropped HC4 in section 7, c4j_web_WS never
had it, c4j_middleware4j is HC5-only). The `<Logger>` suppression entries in
`xml/log/log4j2.xml` are now harmless dead config — they could be deleted on
a future tidy pass.

---

## 10. JasperReports module trim — drop 13 unused JR add-ons (2026-05-03)

After section 9, audited the remaining 22 `jasperreports-*` jars in `lib/`.
Goal: keep only the modules backed by actual usage, remove the rest.

### Audit method

Three signals combined:

1. **`.jrxml` survey** across all 137 reports:
   - `jasperReport@language`: 92 java + 29 groovy = 121 (+ 16 in subdirs)
   - `queryString@language`: only `sql` / `SQL` (zero hql, json, xpath, jsonql, etc.)
   - chart elements: 0 — `customizerClass=`: 0 — barbecue components: 0 — crosstabs: 0
   - barcode4j components: **15 reports**
   - DateTime/Logical functions (NOW, TODAY, CONCATENATE, IF): **5 reports**
   - Fonts referenced: `DejaVu Sans`, `DejaVu Serif`, `SansSerif`
2. **Java source** (`grep "net\.sf\.jasperreports" src/`): only `JasperFillManager`,
   `JasperPrint`, `JasperExportManager.exportReportToPdfFile` (`JLaunchReport.java:140`),
   `JRPrintServiceExporter`, `JRResultSetDataSource`, `JRViewer`, `JRParameter`,
   `JRPropertiesUtil`, `DefaultJasperReportsContext`. All in core or
   `jasperreports-pdf`.
3. **`jdeps -v` cross-module audit**: confirmed (a) keep-jars have **zero**
   bytecode refs into any drop-jar's package namespace; (b) core jasperreports.jar
   **does** reference `net.sf.jasperreports.metadata.*` and
   `net.sf.jasperreports.annotations.properties.*` — so `jasperreports-metadata`
   is required even though no app code touches it directly.

### Decisions

**Kept (9 jars, all backed by direct evidence):**

| Jar | Why kept |
|---|---|
| `jasperreports` | Core API used directly in `JLaunchReport`/`JInternalFrameReportViewer`. |
| `jasperreports-metadata` | Core `jasperreports.jar` has bytecode refs to its classes (jdeps-confirmed). |
| `jasperreports-fonts` | `.jrxml` files use `DejaVu Sans` / `DejaVu Serif` font extensions. |
| `jasperreports-functions` | 5 reports use built-in DateTime/Logical functions. |
| `jasperreports-pdf` | `JasperExportManager.exportReportToPdfFile` resolves `net.sf.jasperreports.pdf.JRPdfExporter` via `Class.forName`; without it throws `extensions.missing.extension.pdf`. |
| `jasperreports-groovy` | 29 reports use `language="groovy"`. |
| `jasperreports-jdt` | 92 reports use `language="java"`; JDT is the compiler at build time (also referenced via `compiler.X=` opt-in in `reports/jasperreports.properties`). |
| `jasperreports-ant` | `compile_reports.xml` uses `JRAntCompileTask` from this jar. |
| `jasperreports-barcode4j` | 15 reports use barcode4j components. |

**Dropped (13 jars, plus 14 transitive deps = 27 jars total):**

| Dropped JR jar | Reason |
|---|---|
| `jasperreports-data-adapters` | No `.jrdax` files; reports get a JDBC `Connection` directly. |
| `jasperreports-charts` | Zero `<chart>` / `<pieChart>` / `<barChart>` etc. anywhere. |
| `jasperreports-chart-themes` | Depends on charts (which is being dropped). |
| `jasperreports-chart-customizers` | Zero `customizerClass=` references. |
| `jasperreports-excel-poi` | No XLS/XLSX export in source; `JasperExportManager` only used for PDF. |
| `jasperreports-javascript` | Zero `language="javascript"` reports (the `compiler.javascript=` opt-in in `jasperreports.properties` is dormant). |
| `jasperreports-json` | No JSON datasource / no `language="json"`. |
| `jasperreports-jaxen` | No XPath queries. |
| `jasperreports-hibernate` | All 137 reports use SQL — no `language="hql"` (the `query.executer.factory.hql=` opt-in is dormant). |
| `jasperreports-spring` | No Spring usage. |
| `jasperreports-servlets` | Desktop app — no servlets. |
| `jasperreports-barbecue` | Zero barbecue components (the modern barcode4j is used instead). |
| `jasperreports-annotation-processors` | Compile-time-only annotation processor for JR's own `@Property` SDK; commander4j has none. |

The 13 jasperreports-* drops cascaded into **14 transitive jars**:
`hibernate-core-6.3.1.Final` + `hibernate-commons-annotations-6.0.6.Final` +
`jandex-3.1.2` + `classmate-1.5.1` + `antlr-2.7.7` + `antlr4-runtime-4.10.1` +
`jakarta.persistence-api-3.1.0` + `jakarta.transaction-api-2.0.1` +
`jakarta.inject-api-2.0.1` + `jboss-logging-3.5.0.Final` (Hibernate stack) +
`spring-beans-6.2.11` + `spring-core-6.2.11` (Spring stack) + `rhino-1.8.1` +
`rhino-tools-1.8.1` (Mozilla Rhino JS engine pulled in by jasperreports-javascript).

### Verification — `compilereports.sh` is the gate

`ant clean build` only checks the 2 commander4j Java files that touch JR — both
import only core classes, so it passes regardless of the trim. The real test
is rebuilding **all 137 `.jrxml` files** against the trimmed classpath: JR's
compile path runs each report's expressions through JDT (java) or Groovy
(groovy) and resolves imports/auto-imports against `lib/`. Any expression or
implicit import that touches a dropped class fails visibly here, instead of
six months later when someone runs that report.

```
$ bash compilereports.sh
[jrc] Compiling 137 report design files.
BUILD SUCCESSFUL
Total time: 2 seconds
```

Zero WARN, ERROR, FAIL, or Exception lines. All 137 → 137 `.jasper` files.

### Bonus: PropertyProcessor warning silenced

Section 8's `ant clean build` showed:

```
[javac] warning: Supported source version 'RELEASE_8' from annotation processor
        'net.sf.jasperreports.annotations.processors.properties.PropertyProcessor'
        less than -source '25'
```

That processor lived in `jasperreports-annotation-processors-7.0.6.jar`. With
that jar removed, post-trim `ant clean build` is clean — no annotation
processor activates against commander4j source, since commander4j has no
`@Property` annotations.

### Final state

- `pom.xml`: 9 jasperreports-* declarations remain (was 22).
- `lib/`: **85 jars** (was 112 after section 9 → was 115 originally).
- `target/dependency/` matches `lib/` exactly except the documented
  `c4j_cups4j-0.8.1-jakarta.jar` out-of-band entry.
- `compilereports.sh` clean. `ant clean build` clean. PropertyProcessor warning
  gone.

### When you'd need to put a jar back

If a future report adds a chart, switches a query to `language="hql"`, uses
JSON/XPath as a datasource, or you start exporting to XLSX, the pom decl
needs to come back (and HC4 transitives may follow if the new jar pulls
something HTTP-flavoured). The path is mechanical: re-add the dependency,
`bash getdependencies.sh`, copy the new jar(s) into `lib/`, re-run
`compilereports.sh`.

---

## 11. Webapp lib/ trim — same audit applied across all 3 webapps (2026-05-03)

After section 10's desktop trim, applied the same source-import + jdeps +
web.xml audit pattern to the three webapps. Result: **38 jars removed across
the three** — including the entire Jersey/HK2/JSON-B stack from c4j_web_WS
that section 8 had flagged as "deferred."

### Audit method (per webapp)

1. **Source imports** — `grep "^import "` on each webapp's `src/` to
   establish what classes are actually referenced.
2. **`jdeps -v commander4j.jar`** — for the two webapps that bundle
   commander4j.jar (web_react, web_Issue), confirm what runtime needs the
   bundled jar has (the bundled jar provides domain logic the webapps
   delegate to).
3. **`web.xml` and `META-INF/services/`** — *the* critical gate the
   advisor flagged. A jar can be load-bearing via `<servlet-class>`,
   `<filter-class>`, `<listener-class>`, or `META-INF/services/` discovery
   even when source has zero imports. All three webapp web.xml files were
   audited — every servlet/filter/listener is in the webapp's own
   `com.commander4j.*` packages; zero Jersey/JSON-B/OSGi wiring; zero
   META-INF/services entries.
4. **`ant clean war`** — compile-time gate. Note: this does NOT verify
   runtime behaviour. Each WAR still needs a Tomcat 11 smoke test before
   promotion.

### Key cross-webapp findings

- **`commander4j.jar` references `org.jfree.chart.*`, `net.sf.jasperreports.*`,
  `com.fazecast.jSerialComm`, `com.healthmarketscience.jackcess`, `com.opencsv.*`,
  `com.sun.jna`, `org.apache.poi.*`** — none of which ship in any webapp's
  `WEB-INF/lib/`. Those code paths are desktop-only (Swing launchers, label
  printing, chart panels) and never reachable from a servlet request. So:
  jars purely supporting those subsystems (e.g. `jcommon-1.0.23.jar`, JFreeChart's
  util layer) are dead weight in any webapp regardless of whether they appear
  in commander4j.jar's package refs.
- **`commons-lang-2.6.jar`** is now eliminated from all four projects (desktop
  + 3 webapps). Active goal achieved.

### Per-webapp results

#### c4j_web_react (29 → 27, dropped 2)

Webapp source: 2 .java files (servlets only). Lib exists primarily to satisfy
commander4j.jar's runtime needs.

| Dropped | Reason |
|---|---|
| `jaxen-2.0.0.jar` | Source: 0 refs. commander4j.jar: 0 refs. Pure orphan declared in pom. |
| `jcommon-1.0.23.jar` | JFreeChart's util layer. JFreeChart isn't on the classpath, so any chart code path in commander4j.jar fails regardless. Source: 0 refs. |

Both were direct pom decls (not transitives). Dropped from `pom.xml` and
`WebContent/WEB-INF/lib/`. `getdependencies.sh` re-resolved cleanly.
`ant clean war` clean.

#### c4j_web_Issue (27 → 17, dropped 10)

Ant-only (no pom; lib is source of truth). Webapp uses Gson + servlets +
JDBC + commander4j.jar; entities have `@JsonbProperty` and `@Entity`
annotations but Gson does the actual serialization.

| Dropped | Reason |
|---|---|
| `commons-lang-2.6.jar` | **Dave's active goal.** Webapp source: 0 refs to `org.apache.commons.lang.*`. commander4j.jar: 0 refs (since section 7's lang→lang3 cleanup). |
| `simple-xml-2.7.1.jar` | Section 7 verified zero refs across project. Same orphan in this webapp. |
| `jcommon-1.0.23.jar` | Same JFreeChart util orphan as c4j_web_react. |
| `jaxen-2.0.0.jar` | Same XPath orphan as c4j_web_react. |
| `org.osgi.core-6.0.0.jar` | OSGi core — no OSGi container in this app. |
| `osgi-resource-locator-1.0.3.jar` | Jersey/HK2 service-locator dep — no Jersey here. |
| `yasson-2.0.4.jar` | JSON-B impl. Source uses only `@JsonbProperty` annotations (which live in `jakarta.json.bind-api` — kept); zero `JsonbBuilder.create()` calls. The annotation is inert at runtime when Gson does the serialization. |
| `parsson-1.0.0.jar` | JSON-P impl — pulled in by JSON-B; not needed without it. |
| `jakarta.json-api-2.0.2.jar` | JSON-P API — same. |
| `jakarta.json-2.0.0-module.jar` | JSON-P module wrapper — same. |

`ant clean war` clean. War contents verified: zero remaining
Jersey/yasson/jaxen/simple-xml/commons-lang2/jcommon/osgi entries.

**Kept** (17): commander4j.jar, commons-codec/io/lang3/logging/text, gson,
jakarta.json.bind-api (annotation source), jakarta.persistence-api
(annotation source for `@Entity`), log4j-{api,core,jcl}, mssql-jdbc,
mysql-connector-j, ojdbc17, slf4j-{api,nop}.

#### c4j_web_WS (39 → 13, dropped 26 — the big one)

Ant-only (no pom). 40 .java files, all servlets. Section 8 of this document
explicitly flagged the Jersey/HK2/yasson/parsson stack as "potentially dead
weight (CLAUDE.md says the app uses servlet mappings + Gson, not JAX-RS) —
bigger investigation, deferred." This is that investigation.

**Webapp source survey was decisive:** zero imports of
`org.glassfish.jersey.*`, `jakarta.ws.rs.*`, `org.eclipse.yasson.*`,
`jakarta.json.*`, `jakarta.xml.bind.*`, `jakarta.annotation.*`,
`jakarta.inject.*`, `jakarta.validation.*`. Source uses Gson directly, not
JAX-RS or JSON-B. web.xml declares only the app's own
`com.commander4j.c4jWS.*` servlet/listener classes — no Jersey
`ServletContainer`, no JSON-B `Application` bootstrap.

**Dropped (26 jars):** the entire Jersey 3.x stack
(`jersey-{client,common,server,container-servlet,container-servlet-core,hk2,media-jaxb,media-json-binding,media-sse}`),
HK2 stack
(`hk2-{api,locator,utils}`, `aopalliance-repackaged`, `javassist`,
`jakarta.inject-api`), JSON-B/JSON-P stack
(`yasson`, `parsson`, `jakarta.json-api`, `jakarta.json-2.0.0-module`,
`jakarta.json.bind-api`), `jakarta.ws.rs-api` (JAX-RS API),
`jakarta.validation-api`, `jakarta.annotation-api`, `jakarta.xml.bind-api`
(was kept in section 8 "because Jersey expects it" — with Jersey gone,
unneeded), `osgi-resource-locator`, `org.osgi.core`.

**Kept (13):** commons-codec/io/lang3/text, gson, jakarta.persistence-api
(`@Entity` annotation source), jakarta.servlet-api 5.0 (compile-time
crutch — Tomcat 11 provides at runtime; harmless to keep per advisor),
log4j-{api,core,jcl}, mssql-jdbc, mysql-connector-j, ojdbc17.

`ant clean war` clean. War verified: zero Jersey/HK2/JSON-B/JAX-RS jars
remaining.

### Aggregate result

| Project | Before | After | Removed |
|---|---|---|---|
| c4j_commander4j (desktop) | 115 → 112 → 85 (sections 9–10) | 85 | (28 cumulative) |
| c4j_web_react | 29 | **27** | 2 |
| c4j_web_Issue | 27 | **17** | 10 |
| c4j_web_WS | 39 | **13** | 26 |

**38 jars removed across the 3 webapps** in this session
(plus 28 from the desktop in §§9–10 = **66 jars removed total**).

Bonus achievement: `commons-lang-2.6.jar` is now eliminated from every
project — closes the long-running active goal first noted in
`eliminate_commons_lang2.md`.

### Verification gate left to the user

`ant clean war` proved compilation. **Tomcat 11 smoke-test still required**
before promoting any of these WARs to Development/Release: deploy each WAR,
hit one live endpoint, watch `catalina.out` for `Failed to start component`
or any `ClassNotFoundError` / `NoClassDefFoundError`. The source-import +
web.xml audit makes runtime failure unlikely, but `ant clean war` does not
exercise servlet startup, so the smoke test is the actual ratification.

---

## 12. httpclient5 5.6 → 5.6.1 patch bump (2026-05-05)

Maintenance release. No breaking changes. Bumped across all four c4j
projects to keep the HC5 stack uniform.

### Why bump

Apache HC5 5.6.1 release notes explicitly include
**"Fix NPE in connection evictor setup (#774)"** — the exact bug that
forced the `evictIdleConnections(Timeout.ofSeconds(30))` workaround in
`c4j_cups4j/src/org/cups4j/operations/IppHttp.java:38` (see §8). With
5.6.1 that NPE is gone at the source. Other 5.6.1 changes (SCRAM auth
disabled by default, `IdleConnectionEvictor` sleep-time correction,
`DefaultManagedHttpClientConnection` socket-timeout restoration) are all
benign for our usage.

The IppHttp.java workaround is **kept in place** — harmless, documents
intent, and provides defence if someone later downgrades HC5.

### httpcore5 stays put

httpclient5 5.6.1's POM still transitively requests `httpcore5:5.4` (not
5.4.2). Same Maven gotcha as §8. The explicit `httpcore5:5.4.2` and
`httpcore5-h2:5.4.2` overrides remain required and unchanged.

### Pre-existing drift fixed in same pass

`c4j_cups4j/lib/` was found on the **old HC5 5.4.3 + 5.3.4 stack** —
drift from its own pom (which §8 had already bumped to 5.6 but the lib
was never refreshed). Brought to 5.6.1 / 5.4 / 5.4 to match the pom and
the rest of the project. Cups4j's source uses only stable HC5 5.0+ API
surface, so the drift had no runtime impact, but it made the ant build
compile against a different HC5 version than the runtime saw. Now
consistent.

### Changes

- `c4j_commander4j/pom.xml`: `httpclient5` 5.6 → 5.6.1.
- `c4j_web_react/pom.xml`: `httpclient5` 5.6 → 5.6.1.
- `c4j_middleware4j/pom.xml`: `httpclient5` 5.6 → 5.6.1.
- `c4j_cups4j/pom.xml`: `httpclient5` 5.6 → 5.6.1.
- `c4j_commander4j/lib/`: `httpclient5-5.6.jar` → `httpclient5-5.6.1.jar`.
- `c4j_web_react/WebContent/WEB-INF/lib/`: same.
- `c4j_middleware4j/lib/`: same.
- `c4j_cups4j/lib/`: full HC5 trio swapped (was on 5.4.3 + 5.3.4 + 5.3.4
  → now 5.6.1 + 5.4 + 5.4).
- `c4j_cups4j-0.8.1-jakarta.jar` rebuilt (`ant clean jar` + `mvn clean
  install`) and re-copied into `c4j_commander4j/lib/` and
  `c4j_web_react/WebContent/WEB-INF/lib/`.

### Verification

- `ant clean build` (c4j_commander4j) — BUILD SUCCESSFUL.
- `ant clean war` (c4j_web_react) — BUILD SUCCESSFUL.
- `mvn -DskipTests compile` (c4j_middleware4j) — clean.
- `ant clean jar` + `mvn clean install` (c4j_cups4j) — clean.
- lib-vs-target diff for all four projects: only the documented
  out-of-band entries remain.

---

## 13. log4j 2.25.3 → 2.25.4 patch bump (2026-05-05)

GitHub Dependabot alert flagged log4j 2.25.3. Bumped across all five
c4j projects that ship log4j (`c4j_cups4j` does not — it uses the
slf4j-api facade only).

### CVEs fixed in 2.25.4

- **CVE-2026-34477** — TLS hostname verification bypass: `verifyHostName`
  attribute on `<Ssl>` config elements was silently ignored on all
  versions through 2.25.3.
- **CVE-2026-34478** — log injection via CRLF in `Rfc5424Layout`
  (syslog appender).

### CVE applicability to c4j — zero direct exposure

Audit of all 7 `log4j2.xml` configs across the 5 webapp/desktop projects
plus 2 middleware sample configs: zero `<Ssl>` elements, zero
`Rfc5424Layout`, zero `SyslogAppender`. Neither CVE is reachable in
default config. Bump is defence-in-depth and silences the Dependabot
alert.

### Other 2.25.4 fixes (all benign for our usage)

Null parameterised-logging args, stack-trace rendering improvements,
invalid-char handling in XmlLayout / Log4j1XmlLayout / MapMessage /
JsonWriter, restored `Rfc5424Layout` documented parameter names, debug
logs in `Loader`. No breaking changes; patch-level drop-in.

### Inventory

| Project | log4j-api | log4j-core | log4j-jcl | Source of truth |
|---|---|---|---|---|
| c4j_commander4j | ✓ | ✓ | — | pom (`<log4j.version>` property) |
| c4j_web_react | ✓ | ✓ | ✓ | pom (literal versions) |
| c4j_middleware4j | ✓ | ✓ | — | pom (literal versions) |
| c4j_web_Issue | ✓ | ✓ | ✓ | **lib only** (Ant-only, no pom) |
| c4j_web_WS | ✓ | ✓ | ✓ | **lib only** (Ant-only, no pom) |
| c4j_cups4j | — | — | — | slf4j-api only |

13 jars total swapped across 5 lib folders.

### Changes

- `c4j_commander4j/pom.xml`: `<log4j.version>` 2.25.3 → 2.25.4 (single
  property, propagates to api + core).
- `c4j_web_react/pom.xml`: 3 literal `<version>` refs → 2.25.4 (api,
  core, jcl).
- `c4j_middleware4j/pom.xml`: 2 literal `<version>` refs → 2.25.4 (api,
  core).
- `c4j_commander4j/lib/`: api, core swapped.
- `c4j_web_react/WebContent/WEB-INF/lib/`: api, core, jcl swapped.
- `c4j_middleware4j/lib/`: api, core swapped.
- `c4j_web_Issue/src/main/webapp/WEB-INF/lib/`: api, core, jcl swapped
  (sourced from `c4j_web_react/target/dependency/`).
- `c4j_web_WS/src/main/webapp/WEB-INF/lib/`: api, core, jcl swapped
  (same source).

### Verification

- `ant clean build` (c4j_commander4j) — BUILD SUCCESSFUL.
- `ant clean war` (c4j_web_react) — BUILD SUCCESSFUL.
- `mvn -DskipTests compile` (c4j_middleware4j) — BUILD SUCCESS.
- `ant clean war` (c4j_web_Issue) — BUILD SUCCESSFUL.
- `ant clean war` (c4j_web_WS) — BUILD SUCCESSFUL.

The Tomcat 11 smoke test still pending from §11 now also covers this
log4j bump for the three webapps.

---

## 14. Cross-project sweep — log4j 2.25.4 propagated to 12 more projects (2026-05-05)

After §13, a wider scan across every project under `b8/Scratchpad/`
turned up 12 additional projects on the old log4j stack — 11 on 2.25.3,
1 on 2.20.0. Section §13 had been scoped to the six "main" projects;
this section closes the gap.

### Lesson

When bumping a transitively-shared library, **scan the whole workspace,
not just the project where the alert fired**. The §13 scan-of-record
missed nearly two-thirds of the affected projects because it only
inspected the six projects already documented in this file. Future
security bumps should start with a workspace-wide
`find ... -name "<artifact>-*.jar"` audit before touching any pom.

### Projects covered

| Project | Build | log4j-api | log4j-core | log4j-jcl | log4j-slf4j2-impl | Notes |
|---|---|---|---|---|---|---|
| c4j_autolab4j | maven | ✓ | ✓ | — | — | pom.xml |
| c4j_labelserver4j | maven | ✓ | ✓ | — | — | pom.xml |
| c4j_sftpget4j | maven | ✓ | ✓ | — | ✓ | pom.xml |
| c4j_sftpput4j | maven | ✓ | ✓ | — | ✓ | pom.xml |
| c4j_sftpTransfer | maven | ✓ | ✓ | — | ✓ | pom.xml |
| c4j_launchpad | ant-only | ✓ | ✓ | — | — | no pom |
| c4j_menu4j | ant-only | ✓ | ✓ | — | — | no pom |
| c4j_messagesplitter4j | ant-only | ✓ | ✓ | — | — | no pom |
| c4j_tester4j | ant-only | ✓ | ✓ | — | — | no pom |
| c4j_treedoc4j | ant-only | ✓ | ✓ | ✓ | ✓ | no pom; richest log4j surface |
| c4j_xml_viewer | ant-only | ✓ | ✓ | — | — | no pom |
| util_caseConverter | ant (eclipse runnable-jar export) | ✓ | ✓ | — | — | was on **2.20.0** — 5 minor versions behind |

### New log4j artifact discovered: `log4j-slf4j2-impl`

Section §13 only handled `log4j-api` / `log4j-core` / `log4j-jcl`. The
sftp trio and `c4j_treedoc4j` additionally ship `log4j-slf4j2-impl-*.jar`
— the SLF4J 2.x → log4j-core bridge. It pulls in `log4j-core` so it's
covered by the same CVE advisories; bumping it together is the right
move.

During this work, `c4j_sftpget4j` and `c4j_sftpput4j` had
non-standard pom filenames (`b7sftpGet_pom.xml`, `b7sftpPut_pom.xml`)
referenced via `mvn -f` in their `getdependencies.sh` scripts. After
the bump, both poms were renamed to `pom.xml` and the scripts were
updated to match. Workspace now has zero non-standard pom filenames.

### CVE applicability — still zero

Audited all 11 `log4j2.xml` configs in these projects (`xml/config/log4j2.xml`
in most; `xml/log/log4j2.xml` in xml_viewer). Zero `<Ssl>`, zero
`Rfc5424Layout`, zero `SyslogAppender`. CVE-2026-34477 / 34478 do not
trigger anywhere. Bumps are pure defence-in-depth.

### Changes

- **5 poms edited** to bump 2.25.3 → 2.25.4: `c4j_autolab4j/pom.xml`
  (api, core), `c4j_labelserver4j/pom.xml` (api, core),
  `c4j_sftpget4j/b7sftpGet_pom.xml` (api, core, slf4j2-impl),
  `c4j_sftpput4j/b7sftpPut_pom.xml` (api, core, slf4j2-impl),
  `c4j_sftpTransfer/pom.xml` (api, core, slf4j2-impl).
- **12 lib folders updated.** Maven projects sourced from their own
  `target/dependency/`. Ant-only projects sourced from
  `c4j_commander4j/target/dependency/` (api, core),
  `c4j_web_react/target/dependency/` (jcl), and
  `c4j_sftpget4j/target/dependency/` (slf4j2-impl).
- `util_caseConverter` jumped from **2.20.0 → 2.25.4** (5 minor
  versions); log4j 2.x binary compat held — no source/build changes
  needed.

### Eclipse WTP runtime cache

`.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/` still
contains old 2.25.3 jars for `c4j_web_Issue`, `c4j_web_react`,
`c4j_web_WS` (and old HC5 5.6 jars for `c4j_web_react`). These are
auto-managed by Eclipse — refreshed on next "Clean..."/redeploy from
each project's lib/. Not touched by hand.

### Verification

`ant clean build` on all 11 projects with conventional ant builds:
BUILD SUCCESSFUL. `util_caseConverter` only ships an Eclipse-generated
runnable-jar build (no `clean` target); `ant create_run_jar` packages
cleanly. No compile-time log4j API surface changed across the bump.

### Workspace-wide final state

After §§12–14, every `lib/`-managed log4j across the workspace is on
2.25.4, every HC5 stack is on 5.6.1 + 5.4.2 + 5.4.2 (or 5.6.1 + 5.4 in
cups4j's own lib). The only old jars remaining are the auto-managed
Eclipse WTP cache, which will refresh themselves on next deploy.
