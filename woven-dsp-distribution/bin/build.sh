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
     ln -f -s ../libs/$file lib
     ln -f -s ../logs/$file logs
 fi
 done
echo ">>>link jars end"

cd ${woven_dsp_dir}
project_path=$(cd `dirname $0`; pwd)
project_name="${project_path##*/}"
echo $project_path
echo $project_name

cd ../
dir_path=$(pwd)
dir_name="${dir_path##*/}"
echo "current path ${dir_path}"
echo "current dir ${dir_name}"

tarFile=${project_name}.tar.gz
rm -rf ../../../binaries/$dir_name/*
tar -zvcf ../../../binaries/$dir_name/$tarFile $project_name

echo 'clean current dir'
rm -rf ./*

