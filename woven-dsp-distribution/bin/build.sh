#!/usr/bin/env bash

echo ">>>run build shell"
workdir=$(cd $(dirname $0); pwd)
echo "workdir: ${workdir}"
woven_dsp_dir=${workdir}/
echo "woven_dsp_dir: ${woven_dsp_dir}"

echo ">>>create jars dir"
jarsdir="jars"
mkdir -p ${woven_dsp_dir}/libs/${jarsdir}

for file in `ls ${woven_dsp_dir}/libs/`
do
 result=$(echo ${file} | grep "${jarsdir}")
 if [[  "$result" == "" ]]
 then
     cd  ${woven_dsp_dir}/libs/${file}
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
     
     mkdir -p ${woven_dsp_dir}/$file
     cd ${woven_dsp_dir}/$file
     ln -f -s ../bin bin
     ln -f -s ../conf conf
     ln -f -s ../libs/$file libs
     ln -f -s ../logs/$file logs
 fi
 done
echo ">>>link jars end"