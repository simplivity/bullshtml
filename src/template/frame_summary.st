$template/header()$
<h5>Directory Summary</h5>
<div class="separator">&nbsp;</div> 
<table class="report" id="riskyFiles"> 
<thead><tr> <td class="heading" width="60%">Top 10 Risky Files</td> <td class="heading"  width="20%"><a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">Function Coverage</a></td>  <td class="heading"  width="20%"><a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">Branch Coverage</a></td></tr></thead> 
<tbody> 
	$srcFileList:{
		$it:template/SrcFile()$
	}$
</tbody>
</table>
<script type="text/javascript"> 
var packageTable = new SortableTable(document.getElementById("riskyFiles"),
    ["String", "Percentage", "Percentage"]);
</script> 
<div class="separator">&nbsp;</div> 
<table class="report" id="packageResults"> 
<thead><tr> <td class="heading" width="60%">Directory</td><td class="heading" width="10%"># Files </td>  <td class="heading"  width="20%"><a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">Function Coverage</a></td>  <td class="heading"  width="20%"><a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">Branch Coverage</a></td></tr></thead> 
<tbody> 
$dirList:{
	$it:template/SrcDir()$						
}$
</tbody>
</table>
<script type="text/javascript"> 
var packageTable = new SortableTable(document.getElementById("packageResults"),
    ["String", "Percentage", "Percentage"]);

</script> 
$template/footer()$
