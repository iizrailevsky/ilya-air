#Hello World, Github & Git!
Test Content
##Prerequisites

#### On Mac
```shell
# install home brew
% ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
% brew doctor
% brew update

# install git
% brew install git
% brew install git-flow-avh

# install maven
% brew install maven

# install fpm
% brew install libxml2 libxslt
% brew link --force libxml2 libxslt
% brew install rpm
% sudo gem install fpm
# workaround for ruby find issue. comment out line 40 in:
% vi /System/Library/Frameworks/Ruby.framework/Versions/2.0/usr/lib/ruby/2.0.0/find.rb
```

#### On Windows
```shell
# install cygwin [ http://www.cygwin.com/install.html ], including packages in sections:
# 	Devel: git, git-completion, git-gui, gut-review, gitk
#	Utils: util-linux
#	Web: wget
# in cygwin, do:
% git clone git://github.com/petervanderdoes/gitflow.git
% cd gitflow/contrib
% chmod +x gitflow-installer.sh
% ./gitflow-installer.sh install stable
# install maven to C: drive [ http://mirror.ibcp.fr/pub/apache/maven/binaries/apache-maven-3.2.1-bin.zip ]
% vi .bash_profile
# add: export PATH=$PATH:/cygdrive/c/apache-maven-3.2.1/bin
% which mvn
```

#### Continue (Platform-independent)
```shell
# install  java [ http://www.oracle.com/technetwork/java/javase/downloads ]
# resources
# [optional] SourceTree
% open http://www.sourcetreeapp.com
```

##Checkout out project working copy

```shell
% git clone https://github.com/iizrailevsky/hello-world.git 
% cd hello-world
# initialize git branches. set 1st (production) to master, 2nd (next release) develop, use defaults for rest
% git flow init
# report change status
% git status
# revert changed file
% git checkout -- <filename>
```

##Build/develop/run project

working with branches provides significant benefits. as such, if you are interested in working with branches then consider the following optional steps:

```shell
# [optional] initialize a local feature branch
% git flow feature start [NAME]
```

```shell
# report change status
% git status
% mvn clean package
% java -jar modules/main/target/proto-main-*-SNAPSHOT-all.jar
```

##Package

```shell
# generate package
% ./bin/fpm.sh
```

##Deploy

#### Test your rpm on your machine's Linux VM:
Start your Linux VM: [https://github.com/vagrant](https://github.com/vagrant)
```shell
# copy your generated rpm to the share data directory
cp modules/main/*.rpm ~/[ws]/data
# on your Linux VM, install the rpm
sudo yum -y install /data/hello-word-main*.rpm
```

##Verify or change branch

```shell
# check your current branch
% git branch
# switch branches (e.g. to develop)
% git checkout develop
```

##Check-in your changes locally

```shell
# get latest updates from others
% git pull origin develop
# report change status
% git status
# register changes
% git add ...
% git commit
# report change status
% git status
```

##Push your changes to main Git repository

if you're working with branches, then apply the following steps:

```shell
# [optional] publish changesets for review
% git flow feature publish
% git flow feature finish
```

if you're working on default (develop) branch, then do:

```shell
% git push origin develop
```

##Release milestone build
```shell
# create milestone release
% ./bin/release.sh
```

##Store your Git login credentials (on Mac)
```shell
# save my icode credentials in the osx keychain
% git config --global credential.helper osxkeychain
# verify it worked afterwards
% git config -l
```
