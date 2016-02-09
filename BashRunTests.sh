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


chmod u+x ./app/build/test-results/debug/TEST-soft.swenggroup5.MainActivityTest.xml
chmod u+x ./app/build/test-results/debug/TEST-soft.swenggroup5.EncoderUtilsTest.xml
FILES[0]=./app/build/test-results/debug/TEST-soft.swenggroup5.MainActivityTest.xml  
FILES[1]=./app/build/test-results/debug/TEST-soft.swenggroup5.EncoderUtilsTest.xml


#for each file

# convert the results to parseable object

read_dom () {
    local IFS=\>
    read -d \< ENTITY CONTENT
    local ret=$?
    TAG_NAME=${ENTITY%% *}
    ATTRIBUTES=${ENTITY#* }
    return $ret
}
parse_dom () {
    if [[ $TAG_NAME = "testsuite" ]] ; then
        eval local $ATTRIBUTES
        name1=$name
		num_tests=$tests
		num_skipped=$skipped
		num_errors=$errors
		num_failures=$failures
		time1=$time
    fi
} 
for (( c=0; c<2; c++ ))
do
while read_dom; do
    parse_dom
done < ${FILES[$c]}
	
    # update totals
   (( num_skipped_total += num_skipped ))
   (( num_failures_total += num_failures ))
   (( num_errors_total += num_errors ))

    # print out the results
    echo "			Name: $name1 "
    echo "			Duration = $time1 :"
    echo "						Number of tests = $num_tests"
    echo "						Number of skipped tests = $num_skipped"
	echo "						Number of test errors = $num_errors"
    echo "						Number of failed tests = $num_failures "
done   

#####################################################
#####################################################






