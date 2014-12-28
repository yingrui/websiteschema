#!/bin/bash

# mysqladmin -u root password "root"
# 
# mysql -uroot -proot<< EOF
# drop database if exists websiteschema;
# create database websiteschema;
# GRANT ALL ON websiteschema.* TO 'websiteschema'@'%' identified by 'websiteschema';
# GRANT ALL ON websiteschema.* TO 'websiteschema'@'localhost' identified by 'websiteschema';
# EOF

SQL_FILE="init-model-mysql.sql"

#创建一个临时文件
TMP_FILE=`mktemp`
#将要执行的命令写入临时文件
echo "mysql -uwebsiteschema -pwebsiteschema -Dwebsiteschema<< EOF" > $TMP_FILE
cat $SQL_FILE >> $TMP_FILE
echo "EOF" >> $TMP_FILE
#执行SQL
sh $TMP_FILE
#删除临时文件
rm $TMP_FILE
