<file path="$it.unixStylePath$" name="$it.xmlEncodedNormalizedName$">
    <metrics coveredelements="$it.coveredElementCount$" elements="$it.elementCount$" coveredconditionals="$it.coveredBranchCount$" conditionals="$it.branchCount$" coveredmethods="$it.coveredFunctionCount$" methods="$it.functionCount$" coveredstatements="0" statements="0" />
$it.functions:{ function |
    <line num="$function.line$" signature="$function.xmlEncodedName$" type="method" complexity="$function.complexity$" count="$function.coveredCount$"/>
    $function.decisionPoints: { decisionPoint |
<line falsecount="$decisionPoint.decisionCoverType.falseCount$" num="$decisionPoint.line$" type="cond" truecount="$decisionPoint.decisionCoverType.trueCount$"/>
    }$
}$
</file>