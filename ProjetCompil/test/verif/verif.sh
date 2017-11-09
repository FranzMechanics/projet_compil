#!/bin/bash
# Test de la passe 2

if [ $# -eq 0 ]
  then
    for fich in *.cas
	do 
	    echo "---------------------------------------------------------------------"
	    echo "Fichier : $fich"
	    echo "---------------------------------------------------------------------"
	    cd ../../classes ; java -cp .:../lib/java-cup-11a-runtime.jar fr.esisar.compilation.verif/TestVerif ../test/verif/$fich
	    echo "---------------------------------------------------------------------"
	    echo "Appuyer sur Return"
	    read r
	done
   else
	echo "---------------------------------------------------------------------"
	echo "Fichier : $1"
	echo "---------------------------------------------------------------------"
	cd ../../classes ; java -cp .:../lib/java-cup-11a-runtime.jar fr.esisar.compilation.verif/TestVerif ../test/verif/$1
	echo "---------------------------------------------------------------------"
	echo "Appuyer sur Return"
	read r
fi

