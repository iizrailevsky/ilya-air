#!/bin/sh

HOME=${application.home}
NAME=${application.name}
USER=${application.user}
GROUP=${application.group}
DAEMON=${application.daemon.enable}

case "$1" in
  1|2)
    getent group $GROUP >/dev/null || groupadd -r $GROUP
    getent passwd $USER >/dev/null || useradd -r -g $GROUP -d $HOME -s /usr/sbin/nologin -c "Iizrailevsky / $NAME" $USER

    # fixme: not required if --depends works as expected
    # fixme: verify jdk-1.7 yum id
    #yum install jdk-1.7

    #if [ "$DAEMON" = "true" ]; then
    #   yum install runit
    #fi
    ;;
  *)
    echo "`basename $0` called with unknown argument: $1" >&2
    ;;
esac
