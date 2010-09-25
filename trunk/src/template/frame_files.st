$template/header()$
<h5>Files</h5>
<table width="100%%">
<tbody>
$srcFileList: {
	<tr>
		<td nowrap='nowrap'><a target='summary' href='$it.path$.html'>$it.path.name$</a> <i>$it.functionCoverage$%</i></td>
	</tr>
}$
</tbody>
</table>
</body>
</html>
