#!/bin/bash
echo "Generating new mvn site ..."
./mvnw clean org.pitest:pitest-maven:mutationCoverage site:site 
echo "DONE - ready to commit and push"
cp -rf target/site/* docs
git add -f docs/
git commit -a -m "#3: Update current site"
git push
