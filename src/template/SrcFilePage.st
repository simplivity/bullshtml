$template/header()$
<h5>Coverage Report - $srcFile.path$ <a href="$srcFile.parentDir.normalizedPath$.html"><img src='images/upper.png' border="0"/></a></h5> 
<br>
<div class="separator">&nbsp;</div> 
<table class="report" id="fileList"> 
	<thead>
		<tr>
			<td class="heading" width="50%">File</td>  
			<td class="heading" width="10%">#Function</td>  
			<td class="heading"  width="20%">
				<a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">
					Function Coverage
				</a>
			</td>
			<td class="heading"  width="20%">
				<a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">
					Branch Coverage
				</a>
			</td>
		</tr>
	</thead> 
	
	
	<tbody> 
	$srcFile:template/SrcFile()$
	</tbody> 
</table> 

<div class="separator">&nbsp;</div> 
<table class="report" id="currentDir"> 
	<thead>
		<tr>
			<td class="heading" width="50%">Functions</td>  
			<td class="heading" width="25%">Function Covered</td>  
			<td class="heading"  width="25%">
				<a class="dfn" href="help.html" onclick="popupwindow('help.html'); return false;">
					Branch Coverage
				</a>
			</td>
		</tr>
	</thead> 
	<tbody> 
	$srcFile.functions:{ function |
		$function:template/SrcFunction()$
	}$	
	</tbody> 
</table> 

<script type="text/javascript"> 
	var currentDirTable = new SortableTable(document.getElementById("currentDir"),
    	["String", "Number", "Percentage", "Percentage", "FormattedNumber"]);
	currentDirTable.sort(0);
</script> 

<div class="separator">&nbsp;</div> 
$srcFile.content$
$template/footer()$