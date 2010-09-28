$template/header()$
<h5>Files</h5>
<table width="100%">
<tbody>
$srcFileList: {
	<tr>
		<td nowrap='nowrap'><a target='summary' href='$it.normalizedPath$.html'>$it.path.name$</a> 
			<span class="smallcovf">$it.functionCoverageString$%</span> <span class="smallcovb">$it.branchCoverageString$%</span>
		</td>
	</tr>
}$
</tbody>
</table>
</body>
</html>
