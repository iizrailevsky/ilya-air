#!/bin/sh

HOME=${application.home}
NAME=${application.name}
USER=${application.user}
GROUP=${application.group}
DAEMON=${application.daemon.enable}

case "$1" in
  0|1)
    for dir in "$HOME" "$LOG"; do
      /bin/rm -rf $dir
    done

    if [ "$DAEMON" = "true" ]; then
      /bin/rm -rf /etc/init.d/$NAME
    fi

    getent passwd $USER >/dev/null && userdel -f $USER
    getent group $GROUP >/dev/null && groupdel $GROUP

    # note: needed to swallow the !0 response code
    s=$?
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
