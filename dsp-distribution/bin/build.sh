#!/usr/bin/env bash

echo ">>>run build shell"
script_abs=$(readlink -f "$0")
script_dir=$(dirname $script_abs)
echo "script_dir: ${script_dir}"

cd ${script_dir}
cd ../
dsp_dir=$(pwd)
echo "current path $(pwd),dsp_dir: ${dsp_dir}"

echo ">>>create jars dir"
jarsdir="jars"
mkdir -p ${dsp_dir}/libs/${jarsdir}

for file in `ls ${dsp_dir}/libs/`
do
 result=$(echo ${file} | grep "${jarsdir}")
 if [[  "$result" == "" ]]
 then
     cd  ${dsp_dir}/libs/${file}
     for subFile in ./*.jar
     do
       cp -f $subFile ../${jarsdir}/
       rm -rf $subFile
       ln -s ../${jarsdir}/$subFile $subFile
     done
  
     cd ../..
     rm -rf logs/$file
     rm -rf $file/logs
     mkdir -p logs/$file
     
     mkdir -p ${dsp_dir}/$file
     cd ${dsp_dir}/$file
     ln -f -s ../bin bin
     ln -f -s ../conf conf
     ln -f -s ../libs/$file lib
     ln -f -s ../logs/$file logs
 fi
 done
echo ">>>link jars end"
