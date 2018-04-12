#!/bin/sh

NAME=${application.name}
DAEMON=${application.daemon.enable}

case "$1" in
  0|1)
    if [ "$DAEMON" = "true" ]; then
      /etc/init.d/$NAME stop
      /bin/rm -rf /etc/service/$NAME
    fi
    sleep 5
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
