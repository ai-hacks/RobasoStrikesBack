#/bin/sh
set -eu

INPUT=$1
OUTPUT_PREFIX=$2

URL=$(curl -L -F corpus=@"$INPUT" -F formtype=simple http://www.speech.cs.cmu.edu/cgi-bin/tools/lmtool/run |grep -A 1 "base name" |grep http | sed -e 's/^.*\="//' | sed -e 's/\.tgz.*$//' | sed -e 's/TAR//')

curl $URL.dic > $OUTPUT_PREFIX.dic
curl $URL.lm > $OUTPUT_PREFIX.lm
