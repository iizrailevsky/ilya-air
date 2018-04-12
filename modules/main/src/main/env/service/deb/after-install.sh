#!/bin/sh

case "$1" in
  configure|abort-remove|abort-deconfigure)
    for d in "${application.home}" "${log.dir}"; do \
      mkdir -p $d
      chown -R ${application.user}:${application.group} $d
    done
    for f in "${application.home}/bin/run" "${application.home}/service/run" "${application.home}/service/log/run"; do \
      chmod 755 $f
    done
    ln -s ${application.home}/service /etc/service/${application.name}
    ln -s /usr/bin/sv /etc/init.d/${application.name}
    ;;
  abort-upgrade)
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
