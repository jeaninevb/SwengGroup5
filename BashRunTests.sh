#!/bin/bash
clear 

####################################################
# VARIABLES
####################################################

# variables used to determine whether or not it is safe to push
num_skipped_total=0
num_failures_total=0
num_errors_total=0

# output to text files, only temporary
jvm_results=jvm_test_results.txt
instrumented_results=instrumented_test_results.txt

####################################################
# JVM TEST
####################################################
echo Beginning JVM Unit Tests

# Run the JVM UTs
chmod u+x gradlew
./gradlew test > $jvm_results

echo Finished JVM Unit Tests

#####################################################
# INSTRUMENTED TEST
#####################################################
echo Beginning Instrumented Unit Tests

# Run the Instrumented UTs
chmod u+x gradlew
./gradlew cAT > $instrumented_results

echo Finished Instrumented Unit Tests

#####################################################
# PRINT JVM TEST RESULTS
#####################################################
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
update_totals () {
    # update totals
   (( num_skipped_total += num_skipped ))
   (( num_failures_total += num_failures ))
   (( num_errors_total += num_errors ))
}
print_results () {
    # print out the results
    echo "			Name: $name1 "
    echo "			Duration = $time1 :"
    echo "						Number of tests         = $num_tests"
    echo "						Number of skipped tests = $num_skipped"
	echo "						Number of test errors   = $num_errors"
    echo "						Number of failed tests  = $num_failures "
}
echo JVM Test Results:
# Get the .xml files that contain the results
chmod u+x ./app/build/test-results/debug/

 
#Loop through each file in the directory
for file in ./app/build/test-results/debug/*
do
while read_dom; do
    parse_dom
done < $file #${FILES[$c]}
update_totals
print_results
done   

#####################################################
# PRINT INSTRUMENTED TEST RESULTS
#####################################################
echo "Instrumented Test Results:"
chmod u+x ./app/build/outputs/androidTest-results/connected/*
for file1 in ./app/build/outputs/androidTest-results/connected/*
do
while read_dom; do
    parse_dom
done < "${file1}"  #${FILES[$c]}
update_totals
print_results
done


#####################################################
# DETERMINE WHETHER TO ALLOW PUSH OR NOT
#####################################################

total_results=$((num_skipped_total+num_failures_total+num_errors_total))
jvm_build=$( tail -5 $jvm_results | head -1 )
instrumented_build=$( tail -5 $instrumented_results | head -1 )


if [ $total_results != 0 ] 
then
	echo ""
	echo "################################################################"
	echo "*         !!! DO NOT PUSH !!! YOU HAVE TEST ERROS !!!          *"  
	echo "################################################################"     
	echo ""
elif [  "$jvm_build"  != "BUILD SUCCESSFUL" ]
then
	echo ""
	echo "################################################################"
	echo "*       !!! DO NOT PUSH !!! YOU HAVE JVM BUILD ERROS !!!       *"  
	echo "################################################################"     
	echo ""
elif [ "$instrumented_build" != "BUILD SUCCESSFUL" ]
then
	echo ""
	echo "################################################################"
	echo "*  !!! DO NOT PUSH !!! YOU HAVE INSTRUMENTED BUILD ERROS !!!   *"  
	echo "################################################################"     
	echo ""
else
	echo ""
	echo "**************************************************************"
	echo "			CLEAR TO PUSH								"
	echo "**************************************************************"
	echo ""
fi