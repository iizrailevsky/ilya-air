#!/bin/sh

case "$1" in
  purge|remove)
    for dir in "/etc/init.d/${application.name}" "${application.home}" "${log.dir}"; do
      /bin/rm -rf $dir
    done
    getent passwd ${application.user} >/dev/null && userdel -f ${application.user}
    getent group ${application.group} >/dev/null && groupdel ${application.group}
    # note: needed to swallow the !0 response code
    s=$?
    ;;
  abort-install|abort-upgrade|upgrade|failed-upgrade|disappear)
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
