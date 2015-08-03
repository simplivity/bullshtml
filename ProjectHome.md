Html Reporter for [BullseyeCoverage](http://www.bullseye.com) tool.

**Please use 1.0.5 version if you have problems using Bullseye 8.0.X version.
  * Bullshtml 1.0.X version is faster than 0.5 version and supports hudson clover report officially.** You can integrate Bullseye coverage in Hudson using the clover formatted report(clover.xml) and Hudson clover plugin.

**Release Note
  * http://code.google.com/p/bullshtml/source/browse/trunk/releasenote.txt**

[BullseyeCoverage](http://www.bullseye.com) for C/C++ test coverage measurement is powerful to use from the very limited embedded systems to enterprise level applications. However, there is the lack of enough HTML reporting features. BullsHtml uses the bullseye coverage CLI command to get the coverage result and converts it into the html report (One of java test coverage tool).

## How to use. ##

  1. install the binary
    * Prerequisite: Java 1.5 above.
    * JAVA\_HOME env var should be provided.
    * Environment variable COVFILE should be set.
    * bullseye coverage binary directory must be in PATH.
  1. select the scope you want to generate html
    * use covselect (http://www.bullseye.com/help/ref_covselect.html)
  1. run it.
    * Windows : bullshtml.exe [Option](Option.md) report\_directory
    * Linux/Mac : bullshtml.sh [Option](Option.md) report\_directory
    * Option : -e source\_encoding\_name (Ex: -e UTF-8)
