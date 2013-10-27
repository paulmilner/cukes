package com.jeannot;

import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)

// Note:
// "monochrome" parameter is just to strip out ANSI colour codes which won't work on Windows console without custom code
// Uncomment whichever option set you need to run the appropriate set of tests

//Default:
//@Cucumber.Options(monochrome = true)

//All tests:
//@Cucumber.Options(format={"progress","html:item/cucumber","junit:item/junit.xml","json-pretty:item/cucumber-report.json"},monochrome = true)

//Item tests:
//@Cucumber.Options(format={"pretty","html:item/cucumber","junit:item/junit.xml","json-pretty:item/cucumber-report.json"},monochrome = true,tags = "@itemting")

//Basic login tests:
//@Cucumber.Options(format={"pretty","html:item/cucumber","junit:item/junit.xml","json-pretty:item/cucumber-report.json"},monochrome = true,tags = "@availability")

//Run the test which is "work in progress"...
@Cucumber.Options(format={"progress","html:target/cucumber","junit:target/junit.xml","json-pretty:target/cucumber-report.json"},monochrome = true,tags = "@wip")

public class RunTests {
}