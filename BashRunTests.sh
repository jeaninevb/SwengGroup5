#!/bin/bash
clear 

####################################################
# VARIABLES
####################################################
# padding for pretty outputting
padding=50
# variables used to determine whether or not it is safe to push
num_skipped_total=0
num_failures_total=0
num_errors_total=0

# output to text files, only temporary
jvm_results=jvm_test_results.txt
instrumented_results=instrumented_test_results.txt
####################################################
####################################################

####################################################
# JVM TEST
####################################################
echo Beginning JVM Unit Tests

# Run the JVM UTs
chmod u+x gradlew
./gradlew test > $jvm_results

echo Finished JVM Unit Tests
#####################################################
#####################################################

#####################################################
# INSTRUMENTED TEST
#####################################################
echo Beginning Instrumented Unit Tests

# Run the Instrumented UTs
chmod u+x gradlew
echo ./gradlew cAT > $instrumented_results


echo Finished Instrumented Unit Tests
#####################################################

#####################################################
# PRINT JVM TEST RESULTS
#####################################################

echo "JVM Test Results:"


# Get the .xml files that contain the results
chmod u+x ./app/build/test-results/debug/TEST-soft.swenggroup5.ExampleUnitTest.xml
files=./app/build/test-results/debug/TEST-soft.swenggroup5.ExampleUnitTest.xml

#for each file
i=0
count=${#files}
echo $count
while [ $i -lt 1 ]
 do
# convert the results to parseable object
    report=load $files

	

    # get relevant information from results
   
    name=($report grep -oP '(?<=testsuite name>)[^<]+')	
    num_tests=($report grep -oP '(?<=testsuite>)[^<]+')
    num_skipped=($report grep -oP '(?<=testsuite>)[^<]+')
    num_failures=($report grep -oP '(?<=testsuite>)[^<]+')
    num_errors=($report grep -oP '(?<=testsuite>)[^<]+')
    time=($report xml_grep -oP '(?<=testsuite>)[^<]+')

    # update totals
    $num_skipped_total=$((num_skipped + num_skipped_total))
    $num_failures_total=$((num_failures + num_failures_total))
    $num_errors_total=$((num_errors + num_errors_total))

    # print out the results
    echo $name 
    echo ”Duration = $time :”
    echo "Number of tests = $num_tests“
    echo "Number of skipped tests = $num_skipped“
    echo "Number of failed tests = $num_failures“
    echo "Number of test errors = $num_errors“
 $i=$((i + 1))
done
#####################################################
#####################################################






