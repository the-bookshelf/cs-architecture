#!/bin/sh

TOPDIR=${TOPDIR:-$(git rev-parse --show-toplevel)}
SRCDIR=${SRCDIR:-$TOPDIR/src}
MANDIR=${MANDIR:-$TOPDIR/doc/man}

READERCOIND=${READERCOIND:-$SRCDIR/readercoind}
READERCOINCLI=${READERCOINCLI:-$SRCDIR/readercoin-cli}
READERCOINTX=${READERCOINTX:-$SRCDIR/readercoin-tx}
READERCOINQT=${READERCOINQT:-$SRCDIR/qt/readercoin-qt}

[ ! -x $READERCOIND ] && echo "$READERCOIND not found or not executable." && exit 1

# The autodetected version git tag can screw up manpage output a little bit
RDCVER=($($READERCOINCLI --version | head -n1 | awk -F'[ -]' '{ print $6, $7 }'))

# Create a footer file with copyright content.
# This gets autodetected fine for readercoind if --version-string is not set,
# but has different outcomes for readercoin-qt and readercoin-cli.
echo "[COPYRIGHT]" > footer.h2m
$READERCOIND --version | sed -n '1!p' >> footer.h2m

for cmd in $READERCOIND $READERCOINCLI $READERCOINTX $READERCOINQT; do
  cmdname="${cmd##*/}"
  help2man -N --version-string=${RDCVER[0]} --include=footer.h2m -o ${MANDIR}/${cmdname}.1 ${cmd}
  sed -i "s/\\\-${RDCVER[1]}//g" ${MANDIR}/${cmdname}.1
done

rm -f footer.h2m
