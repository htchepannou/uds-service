#!/bin/sh

#
# Usage: ./install.sh
#

APP=uds-service

# Install application
if [ ! -d "/opt/$APP" ]; then
  mkdir /opt/$APP
fi

if [ ! -d "/opt/$APP/log" ]; then
  mkdir /opt/$APP/log
fi

# startup script
cp initd.sh /etc/init.d/$APP
chmod +x /etc/init.d/$APP

/sbin/chkconfig --add $APP
/sbin/chkconfig $APP on

# application
cp $APP-exec.jar /opt/$APP/.

/etc/init.d/$APP restart
