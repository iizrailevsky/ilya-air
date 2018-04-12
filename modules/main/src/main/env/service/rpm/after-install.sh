#!/bin/sh

HOME=${application.home}
NAME=${application.name}
USER=${application.user}
GROUP=${application.group}
LOG=${log.dir}
DAEMON=${application.daemon.enable}

case "$1" in
  1|2)
    for d in "$HOME" "$LOG"; do \
      mkdir -p $d
      chown -R $USER:$GROUP $d
    done

    chmod 755 "$HOME/bin/run"

    if [ "$DAEMON" = "true" ]; then
      chmod 755 "/etc/service/$NAME/run"
      ln -s /sbin/sv /etc/init.d/$NAME
      /etc/init.d/$NAME start
    fi
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
