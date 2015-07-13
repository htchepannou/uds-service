#!/bin/sh

#
# Usage: ./install.sh <profile>
#

if [ $# -ne 1 ];then
    echo "Install failed! Illegal number of parameters"
    echo "Usage: ./install.sh <spring-profile>"
    exit 1
fi

PROFILE=$1

# Install application
if [ ! -d "/opt/$APP" ]; then
  mkdir /opt/$APP
fi

if [ ! -d "/opt/$APP/log" ]; then
  mkdir /opt/$APP/log
fi

chown -R $APP_USER:$APP_GROUP /opt/$APP


# /etc/init.d
cat initd.sh | sed -e "s/__ACTIVE_PROFILE__/$PROFILE/" > /etc/init.d/$APP

chmod +x /etc/init.d/$APP

/etc/init.d/$APP restart
