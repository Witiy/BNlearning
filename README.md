# BNlearning 

This is the "Bayesian Network Structures Learning Project" (BNlearning), an open-source Java package that offers PEWOBS, WINASOBS, ASOBS, OBS algorithms.
The PEWOBS algorithm is the major algorithm which developed by Ruihong Xu. And other algorithms are as experimental algorithms which are referenced from Mauro Scanagatta. 
Beside the algorithms for constructing BN with candidate parents sets, this project also contains two approachs for searching candidate parents sets. They are used to process the datasets with few variables and the datasets with mass variables respectively.

## References

This package implements the algorithms detailed in the following papers: 
* [Learning Bayesian Networks with Thousands of Variables](https://papers.nips.cc/paper/5803-learning-bayesian-networks-with-thousands-of-variables) (NIPS 2015) Mauro Scanagatta, Giorgio Corani, Cassio P. de Campos, Marco Zaffalon
* [Learning Treewidth-Bounded Bayesian Networks with Thousands of Variables](https://papers.nips.cc/paper/6232-learning-treewidth-bounded-bayesian-networks-with-thousands-of-variables) (NIPS 2016) Mauro Scanagatta, Giorgio Corani, Cassio P. de Campos, Marco Zaffalon


## Usage

The usage of this project are shown as follow.

### Input Dataset Format

The format for the input dataset must be "XXX.dat", namely a space-separated file containing: 

    * First line: list of variables names, separated by space;
    * Second line: list of variables cardinalities, separated by space;
    * Following lines: list of values taken by the variables in each datapoint, separated by space.
   
### Candidate Parents Sets Identification 

The first step of building a Bayesian network structure is searching candidate parents sets for each variable.

```
java -jar xxx.jar scorer.is -d input.dat -j output.jkl -t 10 -b 0 
```

Main options: 
* -d VAL : Datafile input path (.dat format)
* -j VAL : Parent set scores output file (.jkl format)
* -t N   : Maximum time limit, in seconds (default: 10)
* -b N   : Number of machine cores to use - if 0, all are used  (default: 1)

### Learning BN Structures form Candidate Parents Sets

On the basis of candidate parents sets, select parents set for each variable and construct a DAG which is the final BN structures.

#### PEWOBS

```
java -jar xxx.jar solver.pewobs -j input.jkl -r output.res -lm 10 -rt 2 -rr 4 -win 4 -t 10 -b 0 
```

Main options: 
* -j VAL : Candidate Parents sets file (.jkl format)
* -r VAL : BN Structure file (.res format)
* -lm N  : Maximum number of variables to calculate FinalScore （default: 10)
* -rt N  : The number of the shuffle initial orders (default: 2)
* -rr N  : The rate of the shuffle initial order (default: 4)
* -win N ：Maximum window size(default: 4)
* -t N   : Maximum time limit, in seconds (default: 10)
* -b N   : Number of machine cores to use - if 0, all are used  (default: 1)

#### WINASOBS

```
java -jar xxx.jar solver.winasobs -j input.jkl -r output.res -ent input.dat -win 5 -t 10 -b 0 
```

Main options: 
* -j VAL : Candidate Parents sets file (.jkl format)
* -r VAL : BN Structure file (.res format)
* -ent VAL: Datafile input path for using ent approach to generate initial order(.dat format)
* -win N ：Maximum window size(default: 5)
* -t N   : Maximum time limit, in seconds (default: 10)
* -b N   : Number of machine cores to use - if 0, all are used  (default: 1)

#### ASOBS

```
java -jar xxx.jar solver.asobs -j input.jkl -r output.res -ent -t 10 -b 0 
```

Main options: 
* -j VAL : Candidate Parents sets file (.jkl format)
* -r VAL : BN Structure file (.res format)
* -t N   : Maximum time limit, in seconds (default: 10)
* -b N   : Number of machine cores to use - if 0, all are used  (default: 1)

#### OBS

```
java -jar xxx.jar solver.obs -j input.jkl -r output.res -t 10 -b 0 
```

Main options: 
* -j VAL : Candidate Parents sets file (.jkl format)
* -r VAL : BN Structure file (.res format)
* -t N   : Maximum time limit, in seconds (default: 10)
* -b N   : Number of machine cores to use - if 0, all are used  (default: 1)
