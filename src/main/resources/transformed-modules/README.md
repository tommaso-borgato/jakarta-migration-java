git remote add bstansberry git@github.com:bstansberry/wildfly.git
git fetch bstansberry WFLY-16652
git checkout bstansberry/WFLY-16652
git diff ORIG_HEAD HEAD > transformed-modules.txt
