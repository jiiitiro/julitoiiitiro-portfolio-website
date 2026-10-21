#!/usr/bin/env bash
# Checks the live site: every page in the sitemap loads, every link and image on those pages
# works, and the resume PDF generates. Exits with 1 if anything is broken.
#
# Usage: scripts/check-site.sh [site-url]
#   scripts/check-site.sh                          # the live site
#   scripts/check-site.sh http://localhost:8080    # a local run

set -uo pipefail

SITE="${1:-https://julitoiiitiro-portfolio.up.railway.app}"
SITE="${SITE%/}"
UA="Mozilla/5.0 (compatible; portfolio-site-check)"
failures=0
warnings=0

status() {
    curl -s -o /dev/null -L -A "$UA" --max-time 30 --retry 2 --retry-delay 3 -w '%{http_code}' "$1"
}

# Pages come from the sitemap; its URLs are rewritten to $SITE so a local run checks the local copy.
sitemap=$(curl -fsS -A "$UA" --max-time 60 --retry 3 --retry-delay 5 "$SITE/sitemap.xml") || {
    echo "FAIL  could not load $SITE/sitemap.xml"
    exit 1
}
pages=$(echo "$sitemap" | grep -o '<loc>[^<]*' | sed -E "s#<loc>https?://[^/]+#$SITE#")
echo "Checking $(echo "$pages" | wc -l | tr -d ' ') pages on $SITE"

# Every href and src on those pages (skipping mailto: and in-page #anchors).
links=$(for page in $pages; do curl -s -A "$UA" --max-time 30 "$page"; done \
    | grep -oE '(href|src)="[^"#]+"' \
    | sed -E 's/^(href|src)="//; s/"$//; s/&amp;/\&/g' \
    | grep -v '^mailto:' \
    | sed -E "s#^/#$SITE/#" \
    | sort -u)

for url in $pages $links; do
    code=$(status "$url")
    case "$code" in
        2??) ;;
        429|999) echo "WARN  $code $url (rate-limited or bot-blocked, check it by hand)"; warnings=$((warnings + 1)) ;;
        *) echo "FAIL  $code $url"; failures=$((failures + 1)) ;;
    esac
done
echo "Checked $(echo "$pages $links" | wc -w | tr -d ' ') URLs"

if [ "$(curl -s -A "$UA" --max-time 60 "$SITE/resume.pdf" | head -c 5)" = "%PDF-" ]; then
    echo "OK    resume.pdf generates"
else
    echo "FAIL  resume.pdf did not return a PDF"
    failures=$((failures + 1))
fi

echo "$failures broken, $warnings to check by hand"
[ "$failures" -eq 0 ]
