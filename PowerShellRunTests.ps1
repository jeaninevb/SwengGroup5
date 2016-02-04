clear

####################################################
# VARIABLES
####################################################
# padding for pretty outputting
$padding = 50
# variables used to determine whether or not it is safe to push
$num_skipped_total = 0
$num_failures_total = 0
$num_errors_total = 0

# output to text files, only temporary
$jvm_results = ".\jvm_test_results.txt"
$instrumented_results = ".\instrumented_test_results.txt"
####################################################
####################################################


####################################################
# JVM TEST
####################################################
Write-Host "Beginning JVM Unit Tests"

# Run the JVM UTs
./gradlew test | Out-File $jvm_results

Write-Host "Finished JVM Unit Tests"
#####################################################
#####################################################


#####################################################
# INSTRUMENTED TEST
#####################################################
Write-Host "Beginning Instrumented Unit Tests"

# Run the Instrumented UTs
./gradlew cAT | Out-File $instrumented_results

Write-Host "Finished Instrumented Unit Tests"
#####################################################
#####################################################


#####################################################
# PRINT JVM TEST RESULTS
#####################################################
Write-Host "`n"
Write-Host "JVM Test Results:"
Write-Host "`n"

# Get the .xml files that contain the results
$files = Get-ChildItem ".\app\build\test-results\debug\"

# for each file
for ($i=0; $i -lt $files.Count; $i++) {
    # convert the results to parseable object
    [xml]$report = Get-Content $files[$i].FullName

    # get relevant information from results
    $name = $report.testsuite.name
    $num_tests =  $report.testsuite.tests
    $num_skipped = $report.testsuite.skipped
    $num_failures = $report.testsuite.failures
    $num_errors = $report.testsuite.errors
    $time = $report.testsuite.time

    # update totals
    $num_skipped_total += $num_skipped
    $num_failures_total += $num_failures
    $num_errors_total += $num_errors

    # print out the results
    Write-Host $name "(Duration =" $time "):"
    Write-Host "`tNumber of tests = ".PadRight($padding, " ") $num_tests
    Write-Host "`tNumber of skipped tests = ".PadRight($padding, " ") $num_skipped
    Write-Host "`tNumber of failed tests = ".PadRight($padding, " ") $num_failures
    Write-Host "`tNumber of test errors = ".PadRight($padding, " ") $num_errors
}
#####################################################
#####################################################


#####################################################
# PRINT INSTRUMENTED TEST RESULTS
#####################################################
Write-Host "`n"
Write-Host "Instrumented Test Results:"
Write-Host "`n"

# Get the .xml files that contain the results
$files = Get-ChildItem ".\app\build\outputs\androidTest-results\connected\"

# for each file
for ($i=0; $i -lt $files.Count; $i++) {
    # convert the results to parseable object
    [xml]$report = Get-Content $files[$i].FullName

    # get relevant information from results
    $name = $report.testsuite.name
    $num_tests =  $report.testsuite.tests
    $num_skipped = $report.testsuite.skipped
    $num_failures = $report.testsuite.failures
    $num_errors = $report.testsuite.errors
    $time = $report.testsuite.time

    # update totals
    $num_skipped_total += $num_skipped
    $num_failures_total += $num_failures
    $num_errors_total += $num_errors

    # print out the results
    Write-Host $name "(Duration =" $time "):"
    Write-Host "`tNumber of tests = ".PadRight($padding, " ") $num_tests
    Write-Host "`tNumber of skipped tests = ".PadRight($padding, " ") $num_skipped
    Write-Host "`tNumber of failed tests = ".PadRight($padding, " ") $num_failures
    Write-Host "`tNumber of test errors = ".PadRight($padding, " ") $num_errors
}
#####################################################
#####################################################


#####################################################
# DETERMINE WHETHER TO ALLOW PUSH OR NOT
#####################################################
$jvm_build_status = get-content $jvm_results | select -Last 3 | select -First 1
$instrumented_build_status = get-content $instrumented_results | select -Last 3 | select -First 1

$total_results = $num_skipped_total + $num_failures_total + $num_errors_total
if($total_results -ne 0) {
    Write-Host "`n"
    Write-Host "****************************************"
    Write-Host "DO NOT PUSH. YOU HAVE TEST ERRORS".PadLeft(35, " ")
    Write-Host "****************************************"
    Write-Host "`n"
}
elseif ($jvm_build_status -ne "BUILD SUCCESSFUL") {
    Write-Host "`n"
    Write-Host "****************************************"
    Write-Host "DO NOT PUSH. YOUR JVM FAILED TO BUILD"
    Write-Host "****************************************"
    Write-Host "`n"
}
elseif ($instrumented_build_status -ne "BUILD SUCCESSFUL") {
    Write-Host "`n"
    Write-Host "****************************************"
    Write-Host "DO NOT PUSH. YOUR ANDROID FAILED TO BUILD"
    Write-Host "****************************************"
    Write-Host "`n"
}
else {
    Write-Host "`n"
    Write-Host "****************************************"
    Write-Host "CLEAR TO PUSH".PadLeft(25, " ")
    Write-Host "****************************************"
    Write-Host "`n"
}
#####################################################
#####################################################


#####################################################
# Clean up
#####################################################
rm $jvm_results
rm $instrumented_results
#####################################################
#####################################################