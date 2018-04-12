#!/bin/sh

case "$1" in
  remove|purge|upgrade)
    /bin/rm -rf /etc/service/${application.name}
    sleep 5
    ;;
  deconfigure|failed-upgrade)
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
