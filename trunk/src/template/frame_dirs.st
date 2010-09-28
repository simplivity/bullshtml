$template/header()$
<h5>Directories</h5>
<h6><a href="frame_summary.html" target="summary">Overall Summary</a></h6>


<table width="100%">
<tbody>
$srcDirList: { it |
	$if(it.worthToPrint)$
	<tr>
		<td nowrap='nowrap'><a target='summary' href='$it.normalizedPath$.html'>$it.path$</a> 
			<span class="smallcovf">$it.functionCoverageString$%</span> <span class="smallcovb">$it.branchCoverageString$%</span>
		</td>
	</tr>
	$endif$
}$
</tbody>
</table>
<span class="smallcovf">function</span> 
<span class="smallcovb">branch</span> 

</body>
</html>
