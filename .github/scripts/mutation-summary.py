#!/usr/bin/env python3
"""Render mutation testing reports as GitHub-flavoured Markdown.

Supported inputs:
  * PIT (Java):        one or more ``mutations.xml`` files (one per Maven module)
  * Stryker (JS/TS):   the ``mutation.json`` report (mutation-testing-report-schema)

The Markdown is printed on stdout so the workflow can append it to $GITHUB_STEP_SUMMARY.

Usage:
  mutation-summary.py pit     <title> <root-dir>
  mutation-summary.py stryker <title> <mutation.json>
"""
import json
import sys
import xml.etree.ElementTree as ET
from collections import defaultdict
from pathlib import Path

MAX_LISTED_MUTANTS = 100

# Normalised statuses
KILLED, SURVIVED, NO_COVERAGE, TIMEOUT, OTHER = "Killed", "Survived", "No coverage", "Timeout", "Other"
COLUMNS = [KILLED, SURVIVED, NO_COVERAGE, TIMEOUT, OTHER]

PIT_STATUS = {
    "KILLED": KILLED,
    "SURVIVED": SURVIVED,
    "NO_COVERAGE": NO_COVERAGE,
    "TIMED_OUT": TIMEOUT,
}
STRYKER_STATUS = {
    "Killed": KILLED,
    "Survived": SURVIVED,
    "NoCoverage": NO_COVERAGE,
    "Timeout": TIMEOUT,
}


def score(counts):
    """Detected mutants / mutants that were actually tested (same formula as PIT and Stryker)."""
    detected = counts[KILLED] + counts[TIMEOUT]
    valid = detected + counts[SURVIVED] + counts[NO_COVERAGE]
    return None if valid == 0 else 100.0 * detected / valid


def badge(value):
    if value is None:
        return "n/a"
    icon = "🟢" if value >= 80 else "🟠" if value >= 60 else "🔴"
    return f"{icon} {value:.2f}%"


def read_pit(root):
    """Return {module: [mutant dict]} from every target/pit-reports/mutations.xml under root."""
    groups = {}
    for report in sorted(Path(root).glob("**/target/pit-reports/mutations.xml")):
        module = report.parent.parent.parent.name
        mutants = []
        for m in ET.parse(report).getroot().iter("mutation"):
            mutants.append({
                "status": PIT_STATUS.get(m.get("status"), OTHER),
                "location": f"{m.findtext('mutatedClass')}:{m.findtext('lineNumber')}",
                "method": m.findtext("mutatedMethod"),
                "mutator": (m.findtext("mutator") or "").rsplit(".", 1)[-1],
                "description": m.findtext("description") or "",
            })
        groups[module] = mutants
    return groups


def read_stryker(report):
    """Return {file: [mutant dict]} from a Stryker mutation.json report."""
    data = json.loads(Path(report).read_text(encoding="utf-8"))
    groups = {}
    for file_name, file_report in sorted(data.get("files", {}).items()):
        mutants = []
        for m in file_report.get("mutants", []):
            if m.get("status") in ("Ignored", "Pending"):
                continue
            start = m.get("location", {}).get("start", {})
            mutants.append({
                "status": STRYKER_STATUS.get(m.get("status"), OTHER),
                "location": f"{file_name}:{start.get('line', '?')}",
                "method": "",
                "mutator": m.get("mutatorName", ""),
                "description": "",
                "replacement": " ".join((m.get("replacement") or "").split())[:80],
            })
        groups[file_name] = mutants
    return groups


def count(mutants):
    counts = defaultdict(int)
    for m in mutants:
        counts[m["status"]] += 1
    return counts


def escape(text):
    return (text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
            .replace("|", "&#124;").replace("`", "&#96;"))


def describe(mutant):
    if "replacement" in mutant:
        return f"replaced by <code>{escape(mutant['replacement']) or '&nbsp;'}</code>"
    return escape(mutant["description"])


def render(title, group_label, groups):
    out = [f"## 🧬 Mutation testing — {title}", ""]
    if not groups:
        out.append("> ⚠️ No mutation report found.")
        return "\n".join(out)

    all_mutants = [m for mutants in groups.values() for m in mutants]
    total = count(all_mutants)
    out += [
        f"**Mutation score: {badge(score(total))}** — {len(all_mutants)} mutants",
        "",
        f"| {group_label} | Score | " + " | ".join(COLUMNS) + " | Total |",
        "|---|---:|" + "---:|" * len(COLUMNS) + "---:|",
    ]
    for name, mutants in groups.items():
        c = count(mutants)
        out.append(f"| `{name}` | {badge(score(c))} | " + " | ".join(str(c[k]) for k in COLUMNS) + f" | {len(mutants)} |")
    out.append(f"| **Total** | **{badge(score(total))}** | " + " | ".join(f"**{total[k]}**" for k in COLUMNS) + f" | **{len(all_mutants)}** |")
    out.append("")

    undetected = [m for m in all_mutants if m["status"] in (SURVIVED, NO_COVERAGE)]
    if undetected:
        out += [
            f"<details><summary>🧟 {len(undetected)} undetected mutants (survived / no coverage)</summary>",
            "",
            "| Status | Location | Mutator | Description |",
            "|---|---|---|---|",
        ]
        for m in undetected[:MAX_LISTED_MUTANTS]:
            location = f"{m['location']} ({m['method']})" if m["method"] else m["location"]
            out.append(f"| {m['status']} | <code>{escape(location)}</code> | {escape(m['mutator'])} | {describe(m)} |")
        if len(undetected) > MAX_LISTED_MUTANTS:
            out.append(f"| … | {len(undetected) - MAX_LISTED_MUTANTS} more, see the HTML report artifact | | |")
        out += ["", "</details>"]
    out.append("")
    return "\n".join(out)


def main(argv):
    if len(argv) != 4 or argv[1] not in ("pit", "stryker"):
        sys.exit(__doc__)
    kind, title, path = argv[1], argv[2], argv[3]
    if kind == "pit":
        print(render(title, "Module", read_pit(path)))
    else:
        groups = read_stryker(path) if Path(path).is_file() else {}
        print(render(title, "File", groups))


if __name__ == "__main__":
    main(sys.argv)
