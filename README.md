# Scrabbler

### Build

cd $project_dir
sbt assembly

### Run Indexer

cd $project_dir/bin
./scrabble-indexer ../words.txt

### Run Suggester

cd $project_dir/bin
./scrabble-suggester (lookup_query) (result_count)

### Run Profiler

cd $project_dir/bin
./profiler (num_test_cases)


#### Design principles

Scrabbler is designed to optimize the performance of scrabble-suggester. In order to quickly load from disk to memory, data is partitioned amongst directories whose paths are the prefix letters used in word lookup. Words are stored using the GADDAG data structure to ensure a fast lookup of all scrabble matching possibilities. Scrabble scores are stored with words to avoid the need of any additional computation during the execution of scrabble suggester. 

scrabble-suggester is profiled by a profiler that runs a specified number of test cases with differing search string. Results are displayed graphically showing the performance relation of scrabble-suggester with the complexity(scrabble score), the length of the lookup word and the limit of query results to return.

The index takes on 59 MB of disk space for the official (over one million words) english scrabble word list. The average operation time of scrabble-suggester is 400 ms on a 4 core 2.4ghz i7 with 8 GB of DDR3 RAM. The memory footprint is rather small as the data is partitioned, Further optimizations could be made by asynchronously reading the part of the file where the substring is expected to exist, rather than loading the full indexed file into memory. Larger files could be further partitioned as well.
