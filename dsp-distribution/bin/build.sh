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

cd ${dsp_dir}
project_path=$(pwd)
project_name="${project_path##*/}"
echo "current path ${project_path}"
echo "current dir ${project_name}"

cd ../
echo "current path $(pwd)"

dir_path=$(pwd)
dir_name="${dir_path##*/}"
echo "current path ${dir_path}"
echo "current dir ${dir_name}"

tarFile=${project_name}.tar.gz
echo "The final archive file name is ${tarFile}"

mkdir -p ../../../binaries/$dir_name
#rm -rf ../../../binaries/$dir_name/*
tar -zvcf ../../../binaries/$dir_name/$tarFile $project_name

echo 'clean current dir'
rm -rf ./$project_name

cd ../../../binaries/$dir_name/
dir_path=$(pwd)
echo "The final archive file path is ${dir_path}/${tarFile}"
