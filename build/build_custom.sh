.  /opt/ssfs/runtime/bin/tmp.sh
echo 'java server:' $JAVA_SERVER

export JAVA=$JAVA_SERVER

export ANT_PATH=/opt/ssfs/runtime/jar/ant/1_10_1/ant.jar:/opt/ssfs/runtime/jar/ant/1_10_1/optional.jar:/opt/ssfs/runtime/jar/ant/1_8_1/ant-launcher.jar


export CLASSPATH=$ANT_PATH:$INSTALL_DIR/jar/install_foundation.jar:$JAVA_HOME/lib/tools.jar:$INSTALL_DIR/jar/platform/10_0/resources.jar

 
export LOG_FILE=/opt/ssfs/runtime/logs/earbuild_0828.log


#Unexport the classpath to avoid any confusion

export CLASSPATH=


export APP_DCL_FILE=/opt/ssfs/runtime/properties/APPDynamicclasspath.cfg

echo $JAVA
$JAVA -classpath /opt/ssfs/runtime/jar/bootstrapper.jar -Dvendor=shell -DvendorFile=/opt/ssfs/runtime/properties/servers.properties $ANT_OPTS com.sterlingcommerce.woodstock.noapp.NoAppLoader -p "$ANTJARS" -class org.apache.tools.ant.Main -f $APP_DCL_FILE -invokeargs -f ./build_custom.xml  "$@" 2>&1  $1

