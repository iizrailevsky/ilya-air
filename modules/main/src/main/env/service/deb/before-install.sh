#!/bin/sh

case "$1" in
  install|upgrade)
    getent group ${application.group} >/dev/null || groupadd -r ${application.group}
    getent passwd ${application.user} >/dev/null || useradd -r -g ${application.group} -d /opt/${application.name} \
      -s /usr/sbin/nologin -c "Level Up Analytics / ${application.name}" ${application.user}
    ;;
  abort-upgrade)
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
