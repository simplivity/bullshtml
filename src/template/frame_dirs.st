$template/header()$
<h5>Directories</h5>
<h6><a href="frame_summary.html" target="summary">Overall Summary</a></h6>
<table width="100%%">
<tbody>
$srcDirList: {
	<tr>
		<td nowrap='nowrap'><a target='summary' href='$it.normalizedPath$.html'>$src.path.name$</a> 
			<i>$src.functionCoverage$%</i>
		</td>
	</tr>
}$
</tbody>
</table>
</body>
</html>
