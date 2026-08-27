# Lookup2 — Flexible Lookup Dialog

The flexible successor to `JDialogLookup`/`JLaunchLookup`. Criteria and sort order are
entered in two editable tables on separate tabs, so any number of filter conditions
(ANDed together) and any number of sort fields (each with its own direction and
precedence) can be used. Results appear in a configurable-column table.

No static state is involved: configuration goes in as a `JLookupDefinition`, the
outcome comes back as a `JLookupResult`. The old and new lookups coexist.

## Classes

| Class | Package | Role |
|---|---|---|
| `JDialogLookup2` | `com.commander4j.sys` | The modal dialog itself |
| `JLookupDefinition` | `com.commander4j.sys` | Fluent configuration + `show()` |
| `JLookupCriterion` | `com.commander4j.sys` | One filter row (field / operator / value) |
| `JLookupSortTerm` | `com.commander4j.sys` | One sort row (field + direction) |
| `JLookupResult` | `com.commander4j.sys` | Outcome of a lookup invocation |
| `JLaunchLookup2` | `com.commander4j.sys` | Registry of pre-configured lookups |
| `TableCellRenderer_LookupEditor` | `com.commander4j.renderer` | Row banding for the criteria/sort editor tables |

## Three ways to invoke

### 1. Registry entry as-is

```java
JLookupResult r = JLaunchLookup2.processOrders().show();

if (r.isSelected())
{
    jTextFieldOrder.setText(r.getKey());
}
```

### 2. Registry entry, refined

Each registry method returns a **fresh** `JLookupDefinition`, so it can be refined
before showing. Note that `withCriteria(...)` and `sortBy(...)` **append** to the
registry defaults — call `clearCriteria()` / `clearSort()` first for full control.

```java
JLookupResult r = JLaunchLookup2.processOrders()
        .withCriteria("REQUIRED_RESOURCE", JLookupCriterion.LIKE, "PACKER")
        .sortBy("DUE_DATE", JLookupDefinition.DESC)
        .sortBy("PROCESS_ORDER")
        .columns("PROCESS_ORDER", "REQUIRED_RESOURCE", "DUE_DATE", "STATUS")
        .show();
```

### 3. Fully custom definition

```java
JLookupResult r = new JLookupDefinition("APP_PROCESS_ORDER", "Process Orders", "PROCESS_ORDER")
        .withCriteria("REQUIRED_RESOURCE", JLookupCriterion.EQUALS, jTextFieldRequiredResource.getText())
        .withCriteria("STATUS", JLookupCriterion.EQUALS, "Ready")
        .columns("PROCESS_ORDER", "DESCRIPTION", "DUE_DATE")
        .sortBy("PROCESS_ORDER", JLookupDefinition.DESC)
        .show();
```

Constructor: `new JLookupDefinition(tableName, title, keyField)`
- `tableName` — unqualified table or view name (schema prefix applied automatically).
- `title` — dialog window title.
- `keyField` — the column returned by `JLookupResult.getKey()`. It does **not** have
  to be a displayed column; it is always added to the SELECT behind the scenes.

## JLookupDefinition — fluent options

All return `this`, so calls chain. `show()` ends the chain.

| Method | Effect |
|---|---|
| `withCriteria(field)` | Adds a visible criteria row, operator chosen automatically (LIKE for character/timestamp columns, `=` otherwise), empty value |
| `withCriteria(field, operator, value)` | Adds a visible, pre-filled criteria row. Rows are ANDed; blank-value rows are ignored at search time |
| `withFixedFilter(field, operator, value)` | Filter **always applied but never shown** to the user (e.g. `STATUS = Unassigned`) |
| `clearCriteria()` | Removes criteria rows configured so far (e.g. registry defaults) |
| `clearSort()` | Removes sort rows configured so far |
| `sortBy(field)` | Appends an ascending sort field |
| `sortBy(field, JLookupDefinition.DESC)` | Appends a sort field with direction (`ASC` / `DESC` constants) — order of calls sets precedence |
| `columns(col, col, ...)` | Restricts the results table (and SELECT) to these columns, in this order. Not called → **all** table columns shown. Any field in the sort table at search time is appended automatically if not already listed, so the ordering values are always visible |
| `hideDisabled()` | Adds an implicit `ENABLED = 'Y'` filter |
| `hideInactive()` | Adds an implicit `ACTIVE = 'Y'` filter |
| `autoExec(false)` | Do not run the search when the dialog opens (default is `true`); use for huge tables such as APP_PALLET |
| `title("...")` | Overrides the title (useful after taking a registry entry) |
| `show()` | Opens the modal dialog and returns the `JLookupResult` |

## Operators (`JLookupCriterion` constants)

| Constant | SQL |
|---|---|
| `AUTO` | Chosen from column type: `LIKE` for String/Timestamp, `=` otherwise |
| `EQUALS` | `=` |
| `NOT_EQUALS` | `<>` |
| `LIKE` | `LIKE` — if the value contains no `%`, it is wrapped as `%value%` automatically |
| `GREATER_THAN` | `>` |
| `GREATER_OR_EQUAL` | `>=` |
| `LESS_THAN` | `<` |
| `LESS_OR_EQUAL` | `<=` |

Values are bound as typed parameters (BigDecimal / Integer / Long / Double / String)
based on the column's JDBC type, so numeric comparisons work correctly.

## JLookupResult

| Method | Returns |
|---|---|
| `isSelected()` | `true` if the user picked a row (false on Cancel / window close) |
| `getKey()` | Value of the key field for the selected row, trimmed |
| `getValue("COL")` | Any other column fetched for the selected row |
| `getRowValues()` | The whole selected row as `LinkedHashMap<String,String>` |

## Registry entries (`JLaunchLookup2`)

Each mirrors a lookup from the original `JLaunchLookup`. Format: **method — table (key field)**.

- `customers()` — APP_CUSTOMER (CUSTOMER_ID)
- `equipmentType()` — APP_EQUIPMENT_TYPE (EQUIPMENT_TYPE)
- `groups()` — SYS_GROUPS (GROUP_ID)
- `journeys()` — APP_JOURNEY (JOURNEY_REF); fixed filters exclude NO_JOURNEY, STATUS=Unassigned
- `locations()` — APP_LOCATION (LOCATION_ID)
- `masterHoldNotices()` — APP_MHN (MHN_NUMBER)
- `materialBatches()` — APP_MATERIAL_BATCH (BATCH_NUMBER); `autoExec(false)`
- `materials()` — APP_MATERIAL (MATERIAL)
- `modules()` — SYS_MODULES (MODULE_ID)
- `operatives()` — APP_OPERATIVES (ID)
- `packingLine()` — APP_PACKING_LINES (PACKING_LINE_ID)
- `pallets()` — APP_PALLET (SSCC); `autoExec(false)` — millions of rows
- `panelFiller()` — view_selectlist_filler (VALUE)
- `panelUsers()` — APP_QM_USERS (USER_ID)
- `panelZWSIPANE()` — view_selectlist_ZWSIPANE (VALUE)
- `plantsPOResource()` — view_plants (PLANT_ID)
- `processOrders()` — APP_PROCESS_ORDER (PROCESS_ORDER); default criteria STATUS
- `processOrdersByResource()` — APP_PROCESS_ORDER (PROCESS_ORDER); default criteria REQUIRED_RESOURCE
- `qmInspections()` — APP_QM_INSPECTION (INSPECTION_ID)
- `reasons()` — APP_MHN_REASONS (REASON)
- `resources()` — APP_PROCESS_ORDER_RESOURCE (REQUIRED_RESOURCE)
- `samplePointLocations()` — view_sample_point_locations (LOCATION)
- `shiftNames()` — APP_SHIFT_NAMES (SHIFT_ID)
- `suppliers()` — APP_SUPPLIER (SUPPLIER_ID)
- `users()` — SYS_USERS (USER_ID); columns USER_ID, USER_COMMENT
- `wasteContainers()` — APP_WASTE_CONTAINERS (WASTE_CONTAINER_ID)
- `wasteLocations()` — APP_WASTE_LOCATIONS (WASTE_LOCATION_ID)
- `wasteMaterialsAll()` — APP_WASTE_MATERIAL (WASTE_MATERIAL_ID)
- `wasteMaterialsForLocation()` — view_waste_location_materials (WASTE_MATERIAL_ID)
- `wasteReasons()` — APP_WASTE_REASONS (WASTE_REASON_ID)
- `wasteReportIds()` — APP_WASTE_REPORTING_IDS (WASTE_REPORTING_ID)
- `weightSamplePoint()` — VIEW_APP_WEIGHT_SAMPLE_POINT (SAMPLE_POINT)
- `weightSamplePointGroups()` — view_sample_point_groups (REPORTING_GROUP)
- `wtContainerCode()` — APP_WEIGHT_CONTAINER_CODE (CONTAINER_CODE)
- `wtProductGroups()` — APP_WEIGHT_PRODUCT_GROUP (PRODUCT_GROUP)

## Behaviour notes

- The dialog is modal, non-resizable, Metal (Ocean) look and feel like the rest of
  the application, and centres itself over the main window.
- Criteria/sort rows supplied in the definition pre-populate the editable tables;
  the user can add, remove or change rows on screen (Add/Delete act on whichever
  tab is visible; Up/Down re-sequence sort precedence and are enabled only on the
  Sort By tab). Fixed filters and hideDisabled/hideInactive are invisible to the user.
- Criteria rows with a blank value are skipped when the SQL is built, so a
  pre-filled empty row costs nothing.
- Sort fields are appended to the results table when they are not among the
  assigned columns. The column list is rebuilt on every Search, so adding or
  removing sort rows in the dialog adds or removes those extra columns in the
  next result set.
- Double-clicking a results row is the same as Select.
- Definitions hold no static state — an instance can be kept and `show()`n again.
- Results columns are auto-sized from the first 50 rows (7px/char estimate,
  clamped 60–450px); longer values display with `...`.
