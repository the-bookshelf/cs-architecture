#!/bin/sh
find ./ -type f -readable -writable -exec sed -i "s/readercoin/readercoin/g" {} ";"
find ./ -type f -readable -writable -exec sed -i "s/Readercoin/Readercoin/g" {} ";"
find ./ -type f -readable -writable -exec sed -i "s/ReaderCoin/ReaderCoin/g" {} ";"
find ./ -type f -readable -writable -exec sed -i "s/READERCOIN/READERCOIN/g" {} ";"
find ./ -type f -readable -writable -exec sed -i "s/readercoind/readercoind/g" {} ";"

find ./ -type f -readable -writable -exec sed -i "s/RDC/RDC/g" {}  ";"
find ./ -type f -readable -writable -exec sed -i "s/rdc/rdc/g" {}  ";"

