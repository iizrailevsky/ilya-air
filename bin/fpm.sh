#!/bin/sh

usage () {
  echo "usage: `basename $0` [-n name] [-v version] [-p profile] [-h home] [-l log] [-t timestamp] [-m modules] [-d]"
  exit
}

export JAVA_HOME=${JAVA_HOME:-/usr/local/java}
export PATH=$JAVA_HOME/bin:$PATH

fromPom() {
  case $# in
    2) mvn -f modules/$1/pom.xml help:evaluate -Dexpression=$2 | sed -n -e '/^\[.*\]/ !{ p; }';;
    3) mvn -f modules/$1/pom.xml help:evaluate -Dexpression=$2 | sed -n -e '/^\[.*\]/ !{ p; }' | \
         python -c "import xml.etree.ElementTree as ET; import sys; print ET.parse(sys.stdin).getroot().findall(\"$3\")[0].text"
  esac
}

exitOnError() {
  echo "error cause: $1"

  wget ${JENKINS_URL}jnlpJars/jenkins-cli.jar || exitOnError "failed to build rpm"
  java -jar jenkins-cli.jar set-build-result unstable

  exit 1
}


name=`fromPom . project.name`
version=`fromPom . project.version`
profile=development
timestamp=`date -u "+%Y%m%d%H%M%S"`
modules=main
daemon=false
credentials="deployment:deployment123"
nexus="http://nexus-tbd.url.com/nexus/content/repositories"
repository="repository-tbd-name"

while getopts "n:v:p:i:h:l:t:d" option; do
  case "$option" in
    n) name="$OPTARG";;
    v) version="$OPTARG";;
    p) profile="$OPTARG";;
    h) home="$OPTARG";;
    l) log="$OPTARG";;
    t) timestamp="$OPTARG";;
    m) modules="$OPTARG";;
    d) daemon="true";;
    :) echo "Error: -$OPTARG requires an argument"
       usage
       exit 1
       ;;
    ?) echo "Error: unknown option -$OPTARG"
       usage
       exit 1
       ;;
  esac
done

for module in "$modules"; do
  name=`fromPom $module/. project.name`
  version=`fromPom $module/. project.version`
  group=`fromPom $module/. project.groupId`
  id=$name-$version-$profile
  home=${home:-/usr/local/$name}
  log=${log:-/var/log/$name}
  # note: not resolving dependencies for now
  #deps=`fromPom $module/. project.properties application.dependencies`
  daemon=`fromPom $module/. project.properties application.daemon.enable`
  #daemon_deps=`fromPom $module/. project.properties application.daemon.dependencies`

  echo "packaging service: $id"

  common="-s dir --force --debug --architecture noarch --name ${name}-${profile} --version ${version}\
    --iteration ${timestamp} --license \"[license tbd]\" --vendor \"Iizrailevsky\"\
    --category application --provides ${name}-${profile}\
    --description \"Iizrailevsky: ${name}, ${version} [${profile}] ...\"\
    --url http://iizrailevsky.com/${name} --maintainer ${name}@iizrailevsky.com\
    --directories ${home} --directories ${log}"

  if [ "$daemon" = "true" ]; then
    common="$common    --directories /etc/services/${id}"
  fi

  resources="extra-resources/service/run=${home}/service/run\
    extra-resources/service/run=${home}/bin/run\
    extra-resources/service/log/run=${home}/service/log/run\
    classes/logback.xml=${home}/conf/logback.xml\
    ${id}-all.jar=${home}/lib/${id}-all.jar"

  if [ "$daemon" = "true" ]; then
    resources="$resources    extra-resources/service/run=/etc/service/${id}/run"
  fi

  if [ ! -z "$deps" ]; then
    for dep in $deps; do
      depends="$depends --depends $dep"
    done
  fi

  if [ "$daemon" = "true" -a ! -z "$daemon_deps" ]; then
    for dep in $daemon_deps; do
      depends="$depends --depends $dep"
    done
  fi

  deb="-t deb"
  rpm="-t rpm --rpm-os linux"
  scripts="--before-install extra-resources/service/[PKG]/before-install.sh\
    --after-install extra-resources/service/[PKG]/after-install.sh\
    --before-remove extra-resources/service/[PKG]/before-remove.sh\
    --after-remove extra-resources/service/[PKG]/after-remove.sh"

  for pkg in "deb" "rpm"; do
    fpm="$common ${!pkg} `echo $scripts | sed -e "s/\[PKG\]/${pkg}/g"` $depends $resources"
    (cd modules/$module/target; eval fpm $fpm) || exitOnError "failed to build rpm: $module"

    if [ "${version/-SNAPSHOT}" == "$version" ]; then
      artifact=`find modules/$module/target -name \*$pkg -type f`
      path=$nexus/$repository/`echo $group | sed "s/\./\//g"`/$artifact/$version/`basename $artifact`

      echo "archiving file: $artifact, path: $path"

      curl -v -u $credentials --upload-file $artifact $path || exitOnError "upload to nexus failed"
    fi
  done
done
