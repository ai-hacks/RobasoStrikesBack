#!/usr/bin/env bash
set -eu

INPUT=$1
OUTPUT_PREFIX=$2

OUT_DIR=$(dirname $OUTPUT_PREFIX)

if [ -d "$OUT_DIR" ]; then
true;
else
    mkdir -p "$OUT_DIR"
fi


URL=$(curl -L -F corpus=@"$INPUT" -F formtype=simple http://www.speech.cs.cmu.edu/cgi-bin/tools/lmtool/run |grep -A 1 "base name" |grep http | sed -e 's/^.*\="//' | sed -e 's/\.tgz.*$//' | sed -e 's/TAR//')

curl $URL.lm | tr '[:upper:]' '[:lower:]' > $OUTPUT_PREFIX.lm


