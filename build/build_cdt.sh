#!/bin/bash
sh ./cdt/cdt_export_container.sh
cd /opt/ssfs/sources/CDT
ls
echo "CDT export success"
git config user.email "mreyas@us.ibm.com"
git config user.name "Moideen Parampil Mohamed Reyas"
git add -v .
echo "git add success"
git commit -m "Committing latest CDT xmls"
echo "git commit success"
cd /opt/ssfs
eval $(ssh-agent -s)
ssh-add ssh-privatekey
echo "ssh private key add success"
cd /opt/ssfs/sources/CDT
git push origin master
echo "git push success"