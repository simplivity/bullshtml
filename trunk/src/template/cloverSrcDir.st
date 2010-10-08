<package name="$it.xmlEncodedNormalizedPath$">
    <metrics coveredelements="$it.coveredElementCount$" elements="$it.elementCount$" coveredconditionals="$it.coveredBranchCount$" conditionals="$it.branchCount$" coveredmethods="$it.coveredFunctionCount$" methods="$it.functionCount$" coveredstatements="0" statements="0" />
$it.childSrcFile:{ eachFile |	
        $eachFile:template/cloverSrcFile()$
}$
</package>